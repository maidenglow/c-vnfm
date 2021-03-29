package com.eluon.vepc.mano.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.openstack.compute.domain.NovaFlavor;
import org.openstack4j.openstack.compute.domain.ext.ExtHypervisor;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.adapter.IdentityAdapter;
import com.eluon.vepc.mano.adapter.ImageAdapter;
import com.eluon.vepc.mano.adapter.NetworkingAdapter;
import com.eluon.vepc.mano.dao.VNFMDAO;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.ActionLogVO;
import com.eluon.vepc.mano.vo.AlarmNeutronAgentVO;
import com.eluon.vepc.mano.vo.AlarmNovaServicesVO;
import com.eluon.vepc.mano.vo.EndpointVO;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * ComputeServiceImpl (ComputeServiceImpl)
 *
 */
@Service("vnfmService")
public class VNFMServiceImpl implements VNFMService {	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private ComputeAdapter computeAdapter;
	@Autowired
	private NetworkingAdapter networkingAdapter;
	
	@Autowired
	private ImageAdapter imageAdapter;
	
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private VNFMDAO vnfmDAO;

	@Autowired
	private IdentityAdapter identityAdapter;
	
	ObjectMapper mapper = new ObjectMapper();
	
	public VNFMServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}
	
	@Override
	public void addVnfdInfo(VNFDescriptorVO vnfdVO){
		vnfmDAO.addVnfdInfo(vnfdVO);
	}
	@Override
	public int updateVnfd(VNFDescriptorVO vnfdVO){
		return vnfmDAO.updateVnfd(vnfdVO);
	}
	@Override
	public List<VNFDescriptorVO> getVnfdList(VNFDescriptorVO vnfdVO){
		return vnfmDAO.getVnfdList(vnfdVO);
	}
	@Override
	public int deleteVnfd(VNFDescriptorVO vnfdVO){
		return vnfmDAO.deleteVnfd(vnfdVO);
	}
	
	@Override
	public String getVnfdNamefromVnfcId(String inData){
		return vnfmDAO.getVnfdNamefromVnfcId(inData);
	}
	
	@Override
	public void addVnfcInfo(VnfcVO vnfdVO){
		vnfmDAO.addVnfcInfo(vnfdVO);
	}
	@Override
	public int updateVnfc(VnfcVO vnfdVO){
		return vnfmDAO.updateVnfc(vnfdVO);
	}
	@Override
	public List<VnfcVO> getVnfcList(VnfcVO vnfdVO){
		return vnfmDAO.getVnfcList(vnfdVO);
	}
	@Override
	public List<VnfcVO> notCompleteList(String inData){
		return vnfmDAO.notCompleteList(inData);
	}
	
	@Override
	public List<VnfcVO> getVnfdRepoList(VnfcVO vnfdVO){
		return vnfmDAO.getVnfdRepoList(vnfdVO);
	}
	
	@Override
	public int deleteVnfc(VnfcVO vnfdVO){
		return vnfmDAO.deleteVnfc(vnfdVO);
	}
	
	@Override
	public String getVnfcLastIndex(HashMap<String, Object> hm){
		return vnfmDAO.getVnfcLastIndex(hm);
	}
	
	@Override
	public List<AlarmNovaServicesVO> getAlarmNovaServicesList(AlarmNovaServicesVO vnfdVO){
		return vnfmDAO.getAlarmNovaServicesList(vnfdVO);
	}
	
	@Override
	public List<AlarmNeutronAgentVO> getAlarmNertronList(AlarmNeutronAgentVO vnfdVO){ 
		return vnfmDAO.getAlarmNertronList(vnfdVO);
	}
	 
	@Override
	public List<EndpointVO> getEndpointList(EndpointVO vnfdVO){
		return vnfmDAO.getEndpointList(vnfdVO);
	}
	
	@Override
	public List<ActionLogVO> getInstanceActionLog(String inData){
		return vnfmDAO.getInstanceActionLog(inData);
	}
	
	@Override
	public void addEvnetLogInfo(EventLogVO vnfdVO){
		vnfmDAO.addEvnetLogInfo(vnfdVO);
	}
	
	@Override
	public List<EventLogVO> getEvnetLogList(EventLogVO vnfdVO){
		return vnfmDAO.getEvnetLogList(vnfdVO);
	}
	
	@Override
	public String setNetwork(String name, String network_name, String ipAddress){
		
		List<? extends Network> listNetwork = networkingAdapter.listNetworks();
		
		Network network = null;
		for (int i = 0; i < listNetwork.size(); i++) {
			if(listNetwork.get(i).getName().equals(network_name)){
				network = listNetwork.get(i);
			}
		}
		if(network == null) return ""; 
		NeutronPort buiidPort = (NeutronPort) Builders.port()
				.name(name)
				.networkId(network.getId())
				.fixedIp(ipAddress, null)
				.build();
		Port port = networkingAdapter.createPort(buiidPort);
		return port.getId();
	}
	
	public ResponseEntity<String> createFlavorAccess(String tenantName, String flavorId, String reqBody) {
		List<? extends Endpoint> listTokenEndpoints = identityAdapter.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());

				String computeAdminUrl = endpoint.getPublicURL() + config.getString("COMPUTE_FLAVORS_ACTION_URI");
				logger.debug("computeAdminUrl="+computeAdminUrl);

				String tokenId = identityAdapter.getToken().getId();
				logger.debug("tokenId="+tokenId);
				
				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				JsonElement jsonElement = gson.fromJson(reqBody, JsonElement.class);
				jsonObject.add("addTenantAccess", jsonElement);
				reqBody = gson.toJson(jsonObject);

				return computeAdapter.createFlavorAccess(tokenId, computeAdminUrl, flavorId, reqBody);
			}
		}
		return null;
	}
	
	@Override
	public Flavor createFlavor(String tenantId, String param) throws JsonParseException, JsonMappingException, IOException {
		
		logger.info("paramparamparam"+ param);
		
		HashMap<String,?> paramMap = mapper.readValue(param, new TypeReference<HashMap<String,?>>() {});
		NovaFlavor novaFlavor = mapper.readValue(param, new TypeReference<NovaFlavor>() {});
		List<Map> projectList = (List<Map>) paramMap.get("projects");
		boolean isPublic = true;
		if(projectList !=null && projectList.size() > 0 ){
			isPublic = false;
		}
		
		Flavor flavor = NovaFlavor.builder().from(novaFlavor).isPublic(isPublic).build();
		
		
		Flavor newFlavor = computeAdapter.createFlavor(flavor);
		
		if(projectList !=null && projectList.size() > 0 ){
			for (Map<String,?> project : projectList) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("tenant", (String)project.get("id"));
				createFlavorAccess(tenantId, newFlavor.getId(), jsonObject.toString());
			}
		}
		return newFlavor;
	}
	
	
	public void rollBackProcess(String vnfdId, ArrayList<String> arrPortId, ArrayList<String> arrServerId){
		
		for (int i = 0; i < arrServerId.size(); i++) {
			computeAdapter.deleteServer(arrServerId.get(i));
			VnfcVO param2 = new VnfcVO();
			param2.vnfc_id = arrServerId.get(i);
			deleteVnfc(param2);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < arrPortId.size(); i++) {
			networkingAdapter.deletePort(arrPortId.get(i));
		}
	}
	
	@Override
	public String serviceScaleOutProcess(VnfcVO vnfc){
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "VM SCALE_OUT start";
		event.name = vnfc.vnfc_name;
		event.log_id = vnfc.vnfc_id; 
		addEvnetLogInfo(event);
		
		ArrayList<String> arrPortId = new ArrayList<String>();
		ArrayList<String> arrServerId = new ArrayList<String>();
		
		VnfcVO paramSize = new VnfcVO();
		paramSize.vnfd_id = vnfc.vnfd_id;
		paramSize.vnfc_real_name = vnfc.vnfc_real_name;
		int size = vnfmDAO.getVnfcList(paramSize).size();

		if(size >= Integer.parseInt(vnfc.max_instances)){
			logger.info("=================================================================");
			logger.info("VM size over  ::::::: "+ size);
			logger.info("=================================================================");
			return "VM size over  ::::::: "+ size;
		}
		
		VNFDescriptorVO param2 = new VNFDescriptorVO();
		param2.vnfd_id = vnfc.vnfd_id;
		VNFDescriptorVO vnfd2 = getVnfdList(param2).get(0);
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("vnfc_real_name", vnfc.vnfc_real_name);
		hm.put("replace_name", vnfd2.vnfd_name+"_"+vnfc.vnfc_real_name+"_");
		hm.put("vnfd_id", vnfc.vnfd_id);
		
		String lastIndex = getVnfcLastIndex(hm);
		String vnfc_name_sub = vnfd2.vnfd_name+"_"+vnfc.vnfc_real_name;

		int index = Integer.parseInt(lastIndex);
		String desName = null;
		
		desName = vnfc_name_sub+"_"+(index);
		
		VnfcVO timeParam = new VnfcVO();
		timeParam.vnfc_name = desName;
		VnfcVO timeVnfc = getVnfcList(timeParam).get(0);
		
		long currentTime = System.currentTimeMillis();
		if(timeVnfc.guard_time != 1 && currentTime - timeVnfc.guard_time < Integer.parseInt(timeVnfc.break_policy)*1000){
			event = new EventLogVO();
			event.descriptor = "GuardTime : "+timeVnfc.break_policy+ "중  :"+ ((currentTime - timeVnfc.guard_time)/1000)+"초 경과";
			event.name = timeVnfc.vnfc_name;
			event.log_id = timeVnfc.vnfc_id; 
			addEvnetLogInfo(event);
			return event.descriptor;
		}
		
		String scaleIn[] = timeVnfc.in_policy.split(",");
		if(currentTime - timeVnfc.scale_in_time < Long.parseLong(scaleIn[2])*1000){
			event = new EventLogVO();
			event.descriptor = "SCALE_OUT Time : "+scaleIn[2]+ "중  :"+ ((currentTime - timeVnfc.scale_in_time)/1000)+"초 경과";
			event.name = timeVnfc.vnfc_name;
			event.log_id = timeVnfc.vnfc_id; 
			addEvnetLogInfo(event);
			return event.descriptor;
		}
		
		VnfcVO guardParam = new VnfcVO(); 
		guardParam.vnfc_name = desName;
		guardParam.scale_in_time = 1;
		guardParam.scale_out_time = 1;
		guardParam.guard_time = System.currentTimeMillis();
		vnfmDAO.updateVnfc(guardParam);
		
		EventLogVO event1 = new EventLogVO();
		event1.descriptor = "GuardTime 설정";
		event1.name = desName;
		event1.log_id = vnfc.vnfc_id; 
		addEvnetLogInfo(event1);
		
		desName = vnfc_name_sub+"_"+(index-1);
		guardParam.vnfc_name = desName;
		guardParam.scale_in_time = 1;
		guardParam.scale_out_time = 1;
		guardParam.guard_time = System.currentTimeMillis();
		vnfmDAO.updateVnfc(guardParam);
		
		event1 = new EventLogVO();
		event1.descriptor = "GuardTime 설정";
		event1.name = desName;
		event1.log_id = vnfc.vnfc_id; 
		addEvnetLogInfo(event1);
		
		desName = vnfc_name_sub+"_"+(index+1);
		try {
			
			
			VNFDescriptorVO param = new VNFDescriptorVO();
			param.vnfd_id = vnfc.vnfd_id;
			
			VNFDescriptorVO vnfd = vnfmDAO.getVnfdList(param).get(0);
			
			Server serverIn = computeAdapter.getServer(vnfc.vnfc_id);
			String zoneName = serverIn.getAvailabilityZone();
			
			
			List<? extends HostAggregate> listHost = computeAdapter.listHostAggregate();
			HostAggregate selHostagg = null;
			for ( HostAggregate host : listHost ) {
				if(host.getName().equals(zoneName)){
					selHostagg = host;
					break;
				}
			}
			
			List<Hypervisor> selListHyper = new ArrayList<Hypervisor>();
			List<? extends Hypervisor> listHyper = computeAdapter.hypervisors();
			for ( Hypervisor hyper : listHyper ) {
				if(selHostagg.getHosts().contains(hyper.getHypervisorHostname())){
					selListHyper.add(hyper);
				}
			}
			
			int freeDisk = 0;
			int freeMem = 0;
			int freeCpu = 0;
			
			for ( Hypervisor hyper : selListHyper ) {
				freeDisk = freeDisk + hyper.getFreeDisk();
				freeMem = freeMem + hyper.getFreeRam();
				freeCpu = freeCpu +(hyper.getVirtualCPU() - hyper.getVirtualUsedCPU());
			}
			
			Flavor flavor = computeAdapter.getFlavor(vnfc.flavor_id);
			freeDisk = freeDisk - flavor.getDisk()*2;
			freeMem = freeMem - flavor.getRam()*2;
			freeCpu = freeCpu - flavor.getVcpus()*2;
			
			if(freeDisk < 0){
				return "Disk"+ (freeDisk*-1)+"G 부족";
			}else if(freeMem < 0){
				return "Mem "+ (freeMem/1000*-1)+"G 부족";
			}else if(freeCpu < 0){
				return "cpu "+ (freeCpu*-1)+"core 부족";
			}
			
			
			ArrayList<String> listZoneName = new ArrayList<String>();
			if(vnfc.affinity.equals("0")){
				listZoneName.add(zoneName);
				listZoneName.add(zoneName);
			}else {
				List<Object> ListHost = getHosts(vnfc.affinity, selListHyper, vnfc);
				if(vnfc.affinity.equals("1")){
					if(ListHost.get(0).getClass() == ExtHypervisor.class){
						listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
						listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
					}else {
						return (String)ListHost.get(0);
					}
				}else if(vnfc.affinity.equals("2")){
					if(ListHost.get(0).getClass() == ExtHypervisor.class){
						listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(0)).getHypervisorHostname());
						listZoneName.add(zoneName+":"+((Hypervisor)ListHost.get(1)).getHypervisorHostname());
					}else {
						return (String)ListHost.get(0);
					}
				}
			}
			
			
			String flavorId = vnfc.flavor_id;
			String arrTypeNetIp[] = vnfc.network.split(";");
			String pId = null;
			for (int i = 0; i < arrTypeNetIp.length; i = i+2 ) {
				String portId = setNetwork(desName+"_"+arrTypeNetIp[i+1], arrTypeNetIp[i+1], arrTypeNetIp[i].split(",")[index]);
				arrPortId.add(portId);
				if(pId == null){
					pId = portId;
				}else {
					pId = pId+","+portId;
				}
			}
			
			ServerCreate buildServer = null;
			buildServer = Builders.server()
					.name(desName).image(vnfd.image_id)
					.flavor(flavorId)
					.availabilityZone(listZoneName.get(0)).build();
			
			String arrPid[] = pId.split(",");
			
			for (int i = 0; i < arrPid.length; i++) {
				buildServer.addNetworkPort(arrPid[i]);
			}
	
			Server server = computeAdapter.createServer(buildServer);
			arrServerId.add(server.getId());
			vnfc.vnfc_id = server.getId();
			vnfc.vnfc_name = desName;
			vnfc.port_id = pId;
			vnfc.status = "CREATED_END";
			vnfc.scale_in_time = 1;
			vnfc.scale_out_time = 1;
			vnfc.guard_time = System.currentTimeMillis();
			vnfmDAO.addVnfcInfo(vnfc);
			
			server = computeAdapter.waitForServerStatus(server.getId(), Status.ACTIVE, 180, TimeUnit.SECONDS);
			
			if(server.getFault() != null){
				rollBackProcess(vnfc.vnfd_id, arrPortId, arrServerId);
//				computeAdapter.deleteServer(server.getId());
				return server.getFault().getMessage();
			}
			
			event = new EventLogVO();
			event.descriptor = "VM SCALE_OUT end";
			event.name = vnfc.vnfc_name;
			event.log_id = vnfc.vnfc_id; 
			vnfmDAO.addEvnetLogInfo(event);
			
			event1 = new EventLogVO();
			event1.descriptor = "GuardTime 설정";
			event1.name = desName;
			event1.log_id = vnfc.vnfc_id; 
			addEvnetLogInfo(event1);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(vnfc.num_instances.equals("2")){
				
				String desName2 = vnfc_name_sub+"_"+(index+2);
				
				String pid = null;
				
				for (int i = 0; i < arrTypeNetIp.length; i = i+2 ) {
					String portId = setNetwork(desName2+"_"+arrTypeNetIp[i+1], arrTypeNetIp[i+1], arrTypeNetIp[i].split(",")[index+1]);
					arrPortId.add(portId);
					if(pid == null){
						pid = portId;
					}else {
						pid = pid+","+portId;
					}
				}
				
				ServerCreate buildServer2 = null;
				buildServer2 = Builders.server()
						.name(desName2).image(vnfd.image_id)
						.flavor(flavorId)
						.availabilityZone(listZoneName.get(1)).build();
				
				arrPid = pid.split(",");
				for (int i = 0; i < arrPid.length; i++) {
					buildServer2.addNetworkPort(arrPid[i]);
				}
		
				Server server2 = computeAdapter.createServer(buildServer2);
				arrServerId.add(server2.getId());
				vnfc.vnfc_id = server2.getId();
				vnfc.vnfc_name = desName2;
				vnfc.scale_in_time = 1;
				vnfc.scale_out_time = 1;
				vnfc.guard_time = System.currentTimeMillis();
				vnfc.port_id = pid;
				vnfmDAO.addVnfcInfo(vnfc);
				
				
				server2 = computeAdapter.waitForServerStatus(server2.getId(), Status.ACTIVE, 180, TimeUnit.SECONDS);
				
				if(server2.getFault() != null){
					rollBackProcess(vnfc.vnfd_id, arrPortId, arrServerId);
//					computeAdapter.deleteServer(server.getId());
					return server.getFault().getMessage();
				}
				
				event = new EventLogVO();
				event.descriptor = "VM SCALE_OUT end";
				event.name = vnfc.vnfc_name;
				event.log_id = vnfc.vnfc_id; 
				vnfmDAO.addEvnetLogInfo(event);
				
				event1 = new EventLogVO();
				event1.descriptor = "GuardTime 설정";
				event1.name = desName2;
				event1.log_id = vnfc.vnfc_id; 
				addEvnetLogInfo(event1);
			}
			
			
			
		} catch (Exception e) {
			rollBackProcess(vnfc.vnfd_id, arrPortId, arrServerId);
			e.printStackTrace();
			return e.toString();
		}
//		setVnfcInfo();
		return "0";
		//agent에 생성 노티를 보냄
	}
	
	@Override
	public String serviceScaleInProcess(VnfcVO vnfc){
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "VM SCALE_IN start";
		event.name = vnfc.vnfc_name;
		event.log_id = vnfc.vnfc_id; 
		addEvnetLogInfo(event);
		
		VnfcVO paramSize = new VnfcVO();
		paramSize.vnfd_id = vnfc.vnfd_id;
		paramSize.vnfc_real_name = vnfc.vnfc_real_name;
		int size = vnfmDAO.getVnfcList(paramSize).size();
		
		if(size <= Integer.parseInt(vnfc.num_instances)){
			logger.info("=================================================================");
			logger.info("VM size under ::::::: "+ size);
			logger.info("=================================================================");
			return "VM size under ::::::: "+ size;
		}
		
		VNFDescriptorVO param = new VNFDescriptorVO();
		param.vnfd_id = vnfc.vnfd_id;
		VNFDescriptorVO vnfd = getVnfdList(param).get(0);
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("vnfc_real_name", vnfc.vnfc_real_name);
		hm.put("replace_name", vnfd.vnfd_name+"_"+vnfc.vnfc_real_name+"_");
		hm.put("vnfd_id", vnfc.vnfd_id);
		String lastIndex = getVnfcLastIndex(hm);
		
//		String vnfc_name =vnfc.vnfc_name;
		String vnfc_name_sub = vnfd.vnfd_name+"_"+vnfc.vnfc_real_name;
		
		int index = Integer.parseInt(lastIndex);
		String desName = null;
		desName = vnfc_name_sub+"_"+(index);
		
		VnfcVO param2 = new VnfcVO();
		param2.vnfc_name = desName;
		VnfcVO scaleInvnfc = vnfmDAO.getVnfcList(param2).get(0);
		
		long currentTime = System.currentTimeMillis();
		if(vnfc.guard_time != 1 && currentTime - vnfc.guard_time < Integer.parseInt(vnfc.break_policy)*1000){
			event = new EventLogVO();
			event.descriptor = "GuardTime : "+vnfc.break_policy+ "중  :"+ ((currentTime - vnfc.guard_time)/1000)+"초 경과";
			event.name = vnfc.vnfc_name;
			event.log_id = vnfc.vnfc_id; 
			addEvnetLogInfo(event);
			return event.descriptor;
		}
		String scaleOut[] = vnfc.out_policy.split(",");
		if(currentTime - vnfc.scale_out_time < Long.parseLong(scaleOut[2])*1000){
			event = new EventLogVO();
			event.descriptor = "SCALE_IN Time : "+scaleOut[2]+ "중  :"+ ((currentTime - vnfc.scale_out_time)/1000)+"초 경과";
			event.name = vnfc.vnfc_name;
			event.log_id = vnfc.vnfc_id; 
			addEvnetLogInfo(event);
			return event.descriptor;
		}
		
		VnfcVO realNameParam2 = new VnfcVO();
		realNameParam2.vnfd_id = vnfc.vnfd_id;
		List<VnfcVO> listVnfc2 = getVnfcList(realNameParam2);
		for (int i = 0; i < listVnfc2.size(); i++) {
			VnfcVO vnfcInfo = listVnfc2.get(i);
			if(vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")){
				if(vnfcInfo.vnfc_name.startsWith("CSGN")){
					String splIp[] = vnfcInfo.network.split(";");
					int netIndex = Integer.parseInt(vnfcInfo.vnfc_name.split("_")[2]);
					setAgentData("delete:"+scaleInvnfc.vnfc_name+":"+scaleInvnfc.vnfc_id, splIp[0].split(",")[netIndex-1]);
				}
				
			}
		}
		
		event = new EventLogVO();
		event.descriptor = "VM SCALE_IN agent응답대기";
		event.name = scaleInvnfc.vnfc_name;
		event.log_id = scaleInvnfc.vnfc_id; 
		vnfmDAO.addEvnetLogInfo(event);
		
		if(vnfc.num_instances.equals("2")){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			desName = vnfc_name_sub+"_"+(index-1);
			param2.vnfc_name = desName;
			VnfcVO scaleInvnfc2 = vnfmDAO.getVnfcList(param2).get(0);
			
			VnfcVO realNameParam = new VnfcVO();
			realNameParam.vnfd_id = vnfc.vnfd_id;
			List<VnfcVO> listVnfc = getVnfcList(realNameParam);
			for (int i = 0; i < listVnfc.size(); i++) {
				VnfcVO vnfcInfo = listVnfc.get(i);
				if(vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")){
					if(vnfcInfo.vnfc_name.startsWith("CSGN")){
						String splIp[] = vnfcInfo.network.split(";");
						int netIndex = Integer.parseInt(vnfcInfo.vnfc_name.split("_")[2]);
						setAgentData("delete:"+scaleInvnfc2.vnfc_name+":"+scaleInvnfc2.vnfc_id, splIp[0].split(",")[netIndex-1]);
					}
				}
			}
			
			event = new EventLogVO();
			event.descriptor = "VM SCALE_IN agent응답대기";
			event.name = scaleInvnfc2.vnfc_name;
			event.log_id = scaleInvnfc2.vnfc_id; 
			vnfmDAO.addEvnetLogInfo(event);
			
			desName = vnfc_name_sub+"_"+(index-2);
			param2.vnfc_name = desName;
			param2.scale_in_time = 1;
			param2.scale_out_time = 1;
			param2.guard_time = System.currentTimeMillis();
			vnfmDAO.updateVnfc(param2);
			
			EventLogVO event1 = new EventLogVO();
			event1.descriptor = "GuardTime 설정";
			event1.name = desName;
			event1.log_id = scaleInvnfc2.vnfc_id; 
			addEvnetLogInfo(event1);
			
			desName = vnfc_name_sub+"_"+(index-3);
			param2.vnfc_name = desName;
			param2.scale_in_time = 1;
			param2.scale_out_time = 1;
			param2.guard_time = System.currentTimeMillis();
			vnfmDAO.updateVnfc(param2);
			
			event1 = new EventLogVO();
			event1.descriptor = "GuardTime 설정";
			event1.name = desName;
			event1.log_id = scaleInvnfc2.vnfc_id; 
			addEvnetLogInfo(event1);
			
		}
		
		
		return null;
//		setVnfcInfo();
	}
	
	@Override
	public void deletePortSplit(String portId){
		if(portId == null || portId.equals("")){
			return;
		}
		String[] portArr = portId.split(",");
		for (int i = 0; i < portArr.length; i++) {
			networkingAdapter.deletePort(portArr[i]);
		}
	}
	
	@Override
	public void deleteFolder(String parentPath) {

	    File file = new File(parentPath);
	    if(!file.exists())
	    	return;
	    String[] fnameList = file.list();
	    int fCnt = fnameList.length;
	    String childPath = "";
	    
	    for(int i = 0; i < fCnt; i++) {
			childPath = parentPath + "/" + fnameList[i];
			File f = new File(childPath);
			if (!f.isDirectory()) {
				f.delete(); // 파일이면 바로 삭제
			} else {
				deleteFolder(childPath);
			}
	    }
	    
	    File f = new File(parentPath);
	    f.delete();   //폴더는 맨 나중에 삭제
	}
	
	@Override
	public void deleteFile(String filePath) {

	    File file = new File(filePath);
	    if(file.exists()){
	    	file.delete();
	    }
	}
	
	@Override
	public String deleteServerProcess(String vnfc_id){
		
		VnfcVO param = new VnfcVO();
		param.vnfc_id = vnfc_id;
		VnfcVO vnfc =  getVnfcList(param).get(0);
		String vnfd_id = vnfc.vnfd_id;
		
		try {
			computeAdapter.deleteServer(vnfc_id);
			Thread.sleep(1000);
			deletePortSplit(vnfc.port_id);
			
			if(vnfc.snapshot_id != null){
				try {
					computeAdapter.deleteImage(vnfc.snapshot_id.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			VnfcVO param2 = new VnfcVO();
			param2.vnfc_id = vnfc.vnfc_id;
			vnfmDAO.deleteVnfc(param2);
		
			VnfcVO param5 = new VnfcVO();
			param5.vnfc_real_name = vnfc.vnfc_real_name;
			param5.vnfd_id = vnfc.vnfd_id;
			List<VnfcVO> listNameVnfc = getVnfcList(param5);
			
			if(listNameVnfc == null || listNameVnfc.size() == 0){
				deleteFolder(config.getString("FILE_PATH")+vnfd_id+"/"+vnfc.vnfc_real_name);
				computeAdapter.deleteFlavor(vnfc.flavor_id);
			}
			
			EventLogVO event = new EventLogVO();
			if(vnfc.status.equals("SCALE_IN")){
				event.descriptor = "VM SCALE_IN 완료";
			}else {
				event.descriptor = "VM 삭제 완료";
			}
			event.name = vnfc.vnfc_name;
			event.log_id = vnfc.vnfc_id; 
			vnfmDAO.addEvnetLogInfo(event);
			
		} catch (Exception e) {
			EventLogVO event2 = new EventLogVO();
			event2.descriptor = e.toString();
			event2.name = vnfc.vnfc_name;
			event2.log_id = vnfc.vnfd_id; 
			vnfmDAO.addEvnetLogInfo(event2);
			e.printStackTrace();
			return e.toString();
		}
		
		VnfcVO param3 = new VnfcVO();
		param3.vnfd_id = vnfd_id;
		List<VnfcVO> listVnfc = getVnfcList(param3);
		
		if(listVnfc == null || listVnfc.size() == 0 ){
			VNFDescriptorVO param4 = new VNFDescriptorVO();
			param4.vnfd_id = vnfd_id;
			VNFDescriptorVO desVnfdVO =  getVnfdList(param4).get(0);

			if(desVnfdVO.image_id != null){
				imageAdapter.deleteImage(desVnfdVO.image_id);
			}

			VNFDescriptorVO vnfdInfoVO = new VNFDescriptorVO();
			vnfdInfoVO.vnfd_id = vnfd_id;
			deleteVnfd(vnfdInfoVO);
			deleteFolder(config.getString("FILE_PATH")+vnfd_id);
			deleteFile(config.getString("FILE_PATH")+vnfd_id+".xml");
			
			EventLogVO event2 = new EventLogVO();
			event2.descriptor = "VNF 삭제 완료";
			event2.name = desVnfdVO.vnfd_name;
			event2.log_id = desVnfdVO.vnfd_id; 
			vnfmDAO.addEvnetLogInfo(event2);	
		}
		
//		setVnfcInfo();
		return null;
	}
	@Override
	public String getVnfcInfo(String vnfc_id){
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		Server server = computeAdapter.getServer(vnfc_id);
		String agentData = null;

		agentData = server.getName()+" "+server.getAvailabilityZone().replace("nova", "")+" "+server.getHypervisorHostname().replace("compute", "");
		
		return agentData;
	}
	
	@Override
	public void setVnfcInfo(){
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		List<? extends Server> listServer = computeAdapter.listServers();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < listServer.size(); i++) {
			Server server = listServer.get(i);
			if(server.getName() != null && server.getAvailabilityZone() != null 
					&& server.getHypervisorHostname() != null && server.getName().startsWith("CSGN"))
			sb.append(server.getName()+" "+server.getAvailabilityZone().replace("nova", "")+" "+server.getHypervisorHostname().replace("compute", ""));
			sb.append("\n");
		}
		
		String FILE_PATH = config.getString("VNFC_INFO_FILE_PATH");
		String fileName = "vmsoa.cfg";
		String saveFilePath = FILE_PATH+fileName;
		
		File savefile = new File(FILE_PATH);
		if(!savefile.isDirectory()){
			savefile.mkdirs();
		}
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(saveFilePath);
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			int bytesRead = -1;
			byte[] buffer = new byte[4096];
			while ((bytesRead = in.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
			fos.close();in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getVnfdName(String vnfd_id){
		List<VNFDescriptorVO> listVnfD = getVnfdList(null);
		String vnfdName = "CSGN1";
		ArrayList<String> arrName = new ArrayList<String>();
		for (int i = 0; i < listVnfD.size(); i++) {
			if(!listVnfD.get(i).vnfd_id.equals(vnfd_id)){
				arrName.add(listVnfD.get(i).vnfd_name);
			}
		}
		
		if(listVnfD == null || listVnfD.size() == 0 || arrName.size() == 0 || !arrName.contains("CSGN1")){
			vnfdName = "CSGN1";
		}else if(!arrName.contains("CSGN2")){
			vnfdName = "CSGN2";
		}else if(!arrName.contains("CSGN3")){
			vnfdName = "CSGN3";
		}else if(!arrName.contains("CSGN4")){
			vnfdName = "CSGN4";
		}else if(!arrName.contains("CSGN5")){
			vnfdName = "CSGN5";
		}else if(!arrName.contains("CSGN6")){
			vnfdName = "CSGN6";
		}else {
			vnfdName = "error";
		}
		
		return vnfdName;
	}
	
	@Override
	public void setAgentData(String msg, String ip){
		System.out.println("realtime data :"+ msg + "ip :"+ ip);
		int port = 22000;
		msg = msg.trim();
//		String address = config.getString("AGENT_UDP_URL");
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatagramPacket pack = null;
		DatagramSocket ds = null;
		try {
			byte[] data = msg.getBytes();
			pack = new DatagramPacket(data, data.length, inet, port);
			ds = new DatagramSocket();
			ds.send(pack);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public List<Object> getHosts(String type, List<Hypervisor> selListhyper, VnfcVO vnfc){
		List<Object> sortCpuListhyper = new ArrayList<Object>();
		
		if(!type.equals("1") && !type.equals("2")){
			sortCpuListhyper.add("affinity_type이 잘못되었습니다.");
			return sortCpuListhyper;
		}
		
		if(selListhyper.size() == 0){
			sortCpuListhyper.add("선택한 zone에 compute가 없습니다.");
			return sortCpuListhyper;
		}
		
		Hypervisor cpuHighHy = null;
		Flavor flavor = computeAdapter.getFlavor(vnfc.flavor_id);
		
		if(type.equals("1")){
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk()*2 >= 0)
					&& (hypervisor.getFreeRam() - flavor.getRam()*2 >= 0)
					&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus()*2 >= 0))
				{
					if(cpuHighHy == null){
						cpuHighHy = hypervisor;
					}else if((cpuHighHy.getVirtualCPU() - cpuHighHy.getVirtualUsedCPU()) <= (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
						cpuHighHy = hypervisor;
					}
				}
			}
			if(cpuHighHy != null){
				sortCpuListhyper.add(cpuHighHy);
				return sortCpuListhyper;
			}else {
				sortCpuListhyper.add("affinity조건에 맞는 용량의 compute가 없습니다.");
				return sortCpuListhyper;
			}
		}else if(type.equals("2")){
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk() >= 0)
				&& (hypervisor.getFreeRam() - flavor.getRam() >= 0)
				&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus() >= 0))
				{
					if(cpuHighHy == null){
						cpuHighHy = hypervisor;
					}else if((cpuHighHy.getVirtualCPU() - cpuHighHy.getVirtualUsedCPU()) <= (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
						cpuHighHy = hypervisor;
					}
				}
			}
			if(cpuHighHy != null){
				sortCpuListhyper.add(cpuHighHy);
			}
			Hypervisor cpuHighHy2 = null;
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk() >= 0)
					&& (hypervisor.getFreeRam() - flavor.getRam() >= 0)
					&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus() >= 0))
				{
					if(!hypervisor.getHypervisorHostname().equals(cpuHighHy.getHypervisorHostname())){
						if(cpuHighHy2 == null){
							cpuHighHy2 = hypervisor;
						}else if((cpuHighHy2.getVirtualCPU() - cpuHighHy2.getVirtualUsedCPU()) <= (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
							cpuHighHy2 = hypervisor;
						}
					}
				}
			}
			if(cpuHighHy2 != null){
				sortCpuListhyper.add(cpuHighHy2);
			}
			
			if(sortCpuListhyper.size() == 2){
				return sortCpuListhyper;
			}else if(sortCpuListhyper.size() == 1){
				sortCpuListhyper.remove(0);
				sortCpuListhyper.add("anti-affinity조건에 맞는 용량의 compute가 1개밖에 없습니다.");
				return sortCpuListhyper;
			}else {
				sortCpuListhyper.add("anti-affinity조건에 맞는 용량의 compute가 없습니다.");
				return sortCpuListhyper;
			}
		}
		
		sortCpuListhyper.add("affinity_type이 잘못되었습니다.");
		return sortCpuListhyper;
	}
	
}