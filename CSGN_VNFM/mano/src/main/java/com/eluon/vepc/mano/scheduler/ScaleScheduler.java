package com.eluon.vepc.mano.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.common.dao.NFVCommonDAO;
import com.eluon.vepc.mano.dao.ScaleDao;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.IdentityService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.service.SystemService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.AlarmDashboardVO;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.AlarmVO;
import com.google.gson.Gson;
 
//byjch 아래 주석시 스케쥴러 미동작
@Component("sendDocumentPollingScheduler")
public class ScaleScheduler {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ScaleDao scaleDao;
	
	@Autowired
	private ComputeService computeService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private NetworkingService networkingService;
	
	@Autowired
	private VNFMService vnfmService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private CompositeConfiguration config;
	
	/* openStack와 동기화 진행 
	 * 동기화 주기: 3초 
	 * */
	@Scheduled(fixedRate = 3000) // 3초
	public void doSynchronization() throws Exception {
		
		//현재 alarm테이블의 알람들
		List<AlarmVO> arrSeleDB = systemService.listAlarms(null);
		
		//현재 마노에서 발생할 알람을 담음
		List<AlarmVO> arrCheckArm = new ArrayList<AlarmVO>();
		
		//instances
		List<? extends Server> listServer = computeService.listServers();
		for (int i = 0; i < listServer.size(); i++) {
			Server server = listServer.get(i);
			if(!server.getStatus().equals(Status.ACTIVE)){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(server.getId());
				alarm.setAlarmName("instance:"+server.getName());
				alarm.setAlarmGradeCdNm("1111");
				alarm.setAlarmContents("Status : " + server.getStatus());
				alarm.setAlarmGrade("AG001");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		//compute 서비스 체크
		List<AlarmNovaServicesVO> listNovaAlarm = vnfmService.getAlarmNovaServicesList(null);
		for (int i = 0; i < listNovaAlarm.size(); i++) {
			AlarmNovaServicesVO hy = listNovaAlarm.get(i);
			if(hy.disabled != 0 || hy.forced_down != 0){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(Integer.toString(hy.id));
				alarm.setAlarmName("compute:"+hy.binary);
				alarm.setAlarmGradeCdNm("1111");
				if(hy.disabled != 0){
					alarm.setAlarmContents(hy.disabled_reason);
				}else if(hy.forced_down != 0){
					alarm.setAlarmContents("상태 : DOWN");
				}
				alarm.setAlarmGrade("AG001");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		
		//network agent 체크
		List<AlarmNeutronAgentVO> listNeutronAlarm = vnfmService.getAlarmNertronList(null);
		for (int i = 0; i < listNeutronAlarm.size(); i++) {
			AlarmNeutronAgentVO hy = listNeutronAlarm.get(i);
//			System.out.println(hy.agent_type+" = " + hy.admin_state_up);
			if(hy.admin_state_up != 1 ){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(hy.id);
				alarm.setAlarmName("agent:"+hy.agent_type);
				alarm.setAlarmGradeCdNm("1111");
				alarm.setAlarmContents("Status : Disabled");
				alarm.setAlarmGrade("AG002");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		//services
		String listServices = identityService.listServices();
		Gson gson = new Gson();
		AlarmDashboardVO[] arrService = gson.fromJson(listServices, AlarmDashboardVO[].class);
		for (int j = 0; j < arrService.length; j++) {
			if(!arrService[j].enabled.equals("true")){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(arrService[j].id);
				alarm.setAlarmName("service:"+arrService[j].name);
				alarm.setAlarmGradeCdNm("1112");
				alarm.setAlarmContents("Enabled : " + arrService[j].enabled);
				alarm.setAlarmGrade("AG002");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		//ports
		List<? extends Port> listPort = networkingService.listPorts();
		for (int j = 0; j < listPort.size(); j++) {
			Port port = listPort.get(j);
			if(!port.getState().toString().equalsIgnoreCase("Active")){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(port.getId());
				if(port.getName() == null || port.getName() == "")
					alarm.setAlarmName("port:"+port.getId());
				else{
					alarm.setAlarmName("port:"+port.getName());
				}
				
				alarm.setAlarmGradeCdNm("1112");
				alarm.setAlarmContents("Enabled : " + port.getState());
				alarm.setAlarmGrade("AG002");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		//networks
		List<? extends Network> listNetwork = networkingService.listNetworks();
		for (int j = 0; j < listNetwork.size(); j++) {
			Network network = listNetwork.get(j);
			if(!network.getStatus().toString().equalsIgnoreCase("Active")){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(network.getId());
				alarm.setAlarmName("network:"+network.getName());
				alarm.setAlarmGradeCdNm("1112");
				alarm.setAlarmContents("Enabled : " + network.getStatus());
				alarm.setAlarmGrade("AG003");
				alarm.setAlarmStatus("AS001");
//				alarm.setRegisterDate(new Date());
				arrCheckArm.add(alarm);
			}
		}
		
		//images
		List<? extends Image> listImage = imageService.listImages();
		for (int j = 0; j < listImage.size(); j++) {
			Image image = listImage.get(j);
			if(!image.getStatus().toString().equalsIgnoreCase("active")){
				AlarmVO alarm = new AlarmVO();
				alarm.setAlarmId(image.getId());
				alarm.setAlarmName("image:"+image.getName());
				alarm.setAlarmGradeCdNm("1112");
				alarm.setAlarmContents("Enabled : " + image.getStatus());
				alarm.setAlarmGrade("AG003");
				alarm.setAlarmStatus("AS001");
				arrCheckArm.add(alarm);
			}
		}
		
		
		for (int i = 0; i < arrCheckArm.size(); i++) {
			boolean check = true;
			int j = 0;
			for (; j < arrSeleDB.size(); j++) {
				if(arrCheckArm.get(i).getAlarmId().equals(arrSeleDB.get(j).getAlarmId())){
					check = false;
					break;
				}
			}

			if(check){
				try {
					systemService.registAlarm(arrCheckArm.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * db에 체크한 부분이 없으면 db에 delete
		 */
		for (int i = 0; i < arrSeleDB.size(); i++) {
			boolean check = true;
			int j = 0;
			for (; j < arrCheckArm.size(); j++) {
				if(arrSeleDB.get(i).getAlarmId().equals(arrCheckArm.get(j).getAlarmId())){
					check = false;
					break;
				}
			}
			if(check){
				systemService.deleteAlarm(arrSeleDB.get(i));
			}
		}
		vnfmService.setVnfcInfo();
	}
	
}
