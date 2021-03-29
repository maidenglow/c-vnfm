package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.ComputeValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * Compute Controller (VNFMController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VNFMController.java
 */
@RestController
@RequestMapping("/")
public class VNFAgentController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	
	@Autowired
	private VNFMService vnfmService;
	
	ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * CREATE_END인 vnfc name을 넘겨준다
	 * @param request
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/vnf_agent/get_vnfc_name/{vnfc_ip:.+}", method = RequestMethod.GET)
	public String AgentSwPkgStart(HttpServletRequest request, @PathVariable(value = "vnfc_ip") String vnfc_ip) throws InterruptedException {
		//status가 create_end
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "VM Agent_get_vnfc_name start";
		event.name = vnfc_ip;
		event.log_id = vnfc_ip; 
		vnfmService.addEvnetLogInfo(event);
		
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(null);
		VnfcVO vnfc = null;
		for (int i = 0; i < listVnfc.size(); i++) {
			VnfcVO vnfcInfo = listVnfc.get(i);
			if(vnfcInfo.status.equals("CREATED_END")){
				if(vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")){
					String sprNet[] = listVnfc.get(i).network.split(";");
					for (int j = 0; j < sprNet.length; j= j+2) {
						String ip[] = sprNet[j].split(",");
						if(vnfcInfo.vnfc_name.startsWith("CSGN")){
							int netIndex = Integer.parseInt(vnfcInfo.vnfc_name.split("_")[2]);
							if( (ip[netIndex-1].indexOf(vnfc_ip) > -1 )){
								vnfc = listVnfc.get(i);
								break;
							}
						}
					}
				}
			}
		}
		
		JsonAgentResponse outData = new JsonAgentResponse();
		
		if(vnfc == null){
			outData.name = "not_found";
		}else {
			String agentData = vnfmService.getVnfcInfo(vnfc.vnfc_id);
			outData.name = agentData;
			outData.instance_id = vnfc.vnfc_id;
			outData.vnfd_id = vnfc.vnfd_id;
			
			event = new EventLogVO();
			event.descriptor = "VM Agent_get_vnfc_name end";
			event.name = vnfc.vnfc_name;
			event.log_id = vnfc.vnfc_id; 
			vnfmService.addEvnetLogInfo(event);
		}
		
		Gson gson = new Gson();
		String outString = null;
		outString = gson.toJson(outData);
		System.out.println("realtime sendGetName : "+ outString);
		return outString;
	}
	/**
	 * Agent 연동 sw start
	 * @param request
	 * @param vnfc_server_id
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/vnf_agent/noti_sw_start/{vnfc_server_id}", method = RequestMethod.GET)
	public String agentVNFCSwPkgStart(HttpServletRequest request,@PathVariable(value = "vnfc_server_id") String vnfc_server_id) throws InterruptedException {
		Thread.sleep(1000);
		JsonAgentResponse outData = new JsonAgentResponse();
		outData.name = "";
		outData.instance_id = vnfc_server_id;
		
		VnfcVO vnfcVO = new VnfcVO();
		vnfcVO.status = "SW_START";
		vnfcVO.vnfc_id_ref = vnfc_server_id;
		vnfmService.updateVnfc(vnfcVO);
		
		Gson gson = new Gson();
		String outString = gson.toJson(outData);
		
		VnfcVO vnfc = new VnfcVO();
		vnfc.vnfc_id = vnfc_server_id;
		VnfcVO vnfcInfo = vnfmService.getVnfcList(vnfc).get(0);
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "VNFC Agent_noti_sw_start";
		event.name = vnfcInfo.vnfc_name;
		event.log_id = vnfc_server_id; 
		vnfmService.addEvnetLogInfo(event);
		
		return outString;
	}
	
	/**
	 * Agent 연동 sw end
	 * @param request
	 * @param response
	 * @param vnfc_server_id
	 * @throws InterruptedException
	 */
	@RequestMapping(value = "/vnf_agent/noti_sw_end/{vnfc_server_id}", method = RequestMethod.GET)
	public void agentVNFCSwPkgEnd(HttpServletRequest request,HttpServletResponse response,
			@PathVariable(value = "vnfc_server_id") String vnfc_server_id) throws InterruptedException {
		EventLogVO event = new EventLogVO();
		event.descriptor = "VM Agent_noti_sw_end start";
		event.name = vnfc_server_id;
		event.log_id = vnfc_server_id;
		vnfmService.addEvnetLogInfo(event);
		Thread.sleep(1000);
		
		JsonAgentResponse outData = new JsonAgentResponse();
		outData.name = "";
		outData.instance_id = vnfc_server_id;
		
		VnfcVO vnfcVO = new VnfcVO();
		vnfcVO.status = "Completed";
		vnfcVO.vnfc_id_ref = vnfc_server_id;
		vnfmService.updateVnfc(vnfcVO);
		
		
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(null);
		VnfcVO vnfc = null;
		for (int i = 0; i < listVnfc.size(); i++) {
			vnfc = listVnfc.get(i);
			if(vnfc_server_id.equals(vnfc.vnfc_id)){
				outData.name = vnfc.vnfc_name;
				break;
			}
		}
		
		VnfcVO param = new VnfcVO();
		param.vnfd_id = vnfc.vnfd_id;
		listVnfc = vnfmService.getVnfcList(param);
		
		for (int i = 0; i < listVnfc.size(); i++) {
			VnfcVO vnfcInfo = listVnfc.get(i);
			if(vnfcInfo.vnfc_id != null && !vnfcInfo.vnfc_id.equals("0")){
				if(vnfcInfo.vnfc_name.startsWith("CSGN")){
					String splIp[] = vnfcInfo.network.split(";");
					int netIndex = Integer.parseInt(vnfcInfo.vnfc_name.split("_")[2]);
					vnfmService.setAgentData("create:"+outData.name.trim()+":"+vnfc_server_id, splIp[0].split(",")[netIndex-1].trim());
				}
			}
		}
		
		event = new EventLogVO();
		event.descriptor = "VM Agent_noti_sw_end end";
		event.name = outData.name;
		event.log_id = vnfc_server_id;
		vnfmService.addEvnetLogInfo(event);
		
		try {
			Gson gson = new Gson();
			String outString = gson.toJson(outData);
			response.getWriter().println(outString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<VnfcVO> listVnfd = vnfmService.notCompleteList(vnfc.vnfd_id);
		if(listVnfd == null || listVnfd.size() == 0){
			VNFDescriptorVO param2 = new VNFDescriptorVO();
			param2.vnfd_id = vnfc.vnfd_id;
			VNFDescriptorVO vnfd = vnfmService.getVnfdList(param2).get(0);
			Thread.sleep(1000);
			
			EventLogVO event2 = new EventLogVO();
			event2.descriptor = "VNF 시작 완료(agent 포함)";
			event2.name = vnfd.vnfd_name;
			event2.log_id = vnfc.vnfd_id;
			vnfmService.addEvnetLogInfo(event2);
		}
	}
	
	/**
	 * Agent 실시간 통계
	 * @param request
	 * @param scaleData
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/vnf_agent/realtime", method = RequestMethod.POST)
	public String checkScaleInOut(HttpServletRequest request, @RequestBody JsonCheckScaleInOutRequest scaleData) throws Exception {
		System.out.println("realtime session: "+scaleData.session);
		System.out.println("realtime memory: "+scaleData.memory);
		System.out.println("realtime instance_id: "+scaleData.instance_id);
		System.out.println("realtime tps: "+scaleData.tps);
		
		String vnfc_server_id = scaleData.instance_id;
		
		if(scaleData.instance_id == null || scaleData.instance_id == ""){
			return "{\"fail\":\"instance_id is null\"}";
		}
		
		VnfcVO param = new VnfcVO();
		param.vnfc_id = vnfc_server_id;
		VnfcVO vnfcInfo = vnfmService.getVnfcList(param).get(0);
		
		if(!vnfcInfo.scaling_flag.equals("0")){
			return "{\"fail\":\"scaling_flag is"+vnfcInfo.scaling_flag+"\"}";
		}
		
		if(vnfcInfo.guard_time != 1 && System.currentTimeMillis()- vnfcInfo.guard_time < Integer.parseInt(vnfcInfo.break_policy)*1000){
			EventLogVO event = new EventLogVO();
			event.descriptor = "GuardTime : "+vnfcInfo.break_policy+ "중  :"+ ((System.currentTimeMillis() - vnfcInfo.guard_time)/1000)+"초 경과";
			event.name = vnfcInfo.vnfc_name;
			event.log_id = vnfcInfo.vnfc_id; 
			vnfmService.addEvnetLogInfo(event);
			return event.descriptor;
		}

		VnfcVO param1 = new VnfcVO();
		param1.vnfd_id = vnfcInfo.vnfd_id;
		param1.vnfc_real_name = vnfcInfo.vnfc_real_name;
		List<VnfcVO> listVnfc = vnfmService.getVnfcList(param1);
		
		boolean inflag = true;
		System.out.println("realtime size: "+listVnfc.size());
		if(listVnfc.size() > 2){
			if(vnfcInfo.in_policy != null){
				String scaleIn[] = vnfcInfo.in_policy.split(",");
				for (int k = 0; k < scaleIn.length; k=k+3) {
					if(scaleIn[k] != null ){
						if((scaleIn[k].equalsIgnoreCase("cpu") && Integer.parseInt(scaleIn[k+1]) > Integer.parseInt(scaleData.cpu))
							||(scaleIn[k].equalsIgnoreCase("tps") && Integer.parseInt(scaleIn[k+1]) > Integer.parseInt(scaleData.tps))
							||(scaleIn[k].equalsIgnoreCase("session") && Integer.parseInt(scaleIn[k+1]) > Integer.parseInt(scaleData.session))
							||(scaleIn[k].equalsIgnoreCase("mem") && Integer.parseInt(scaleIn[k+1]) > Integer.parseInt(scaleData.memory))){
							inflag = false;
							long currentTime = System.currentTimeMillis();
							if(vnfcInfo.scale_in_time == 1){
								VnfcVO vnfcParam = new VnfcVO();
								vnfcParam.scale_in_time = currentTime;
								vnfcParam.vnfc_id_ref = vnfc_server_id;
								vnfmService.updateVnfc(vnfcParam);
							}else if(currentTime - vnfcInfo.scale_in_time > Long.parseLong(scaleIn[k+2])*1000){
								vnfmService.serviceScaleInProcess(vnfcInfo);
								inflag = true;
								break;
							}
						}
					}
				}
				if(inflag){
					VnfcVO vnfcParam = new VnfcVO();
					vnfcParam.scale_in_time = 1;
					vnfcParam.vnfc_id_ref = vnfc_server_id;
					vnfmService.updateVnfc(vnfcParam);
				}
			}
		}
		
		if(listVnfc.size() < Integer.parseInt(vnfcInfo.max_instances)){
			if(vnfcInfo.out_policy != null){
				String scaleOut[] = vnfcInfo.out_policy.split(",");
				boolean outflag = true;
				for (int k = 0; k < scaleOut.length; k=k+3) {
					if(scaleOut[k] != null ){
						if((scaleOut[k].equalsIgnoreCase("cpu") && Integer.parseInt(scaleOut[k+1]) < Integer.parseInt(scaleData.cpu))
							||(scaleOut[k].equalsIgnoreCase("tps") && Integer.parseInt(scaleOut[k+1]) < Integer.parseInt(scaleData.tps))
							||(scaleOut[k].equalsIgnoreCase("session") && Integer.parseInt(scaleOut[k+1]) < Integer.parseInt(scaleData.session))
							||(scaleOut[k].equalsIgnoreCase("mem") && Integer.parseInt(scaleOut[k+1]) < Integer.parseInt(scaleData.memory))){
							outflag = false;
							long currentTime = System.currentTimeMillis();
							if(vnfcInfo.scale_out_time == 1){
								VnfcVO vnfcParam = new VnfcVO();
								vnfcParam.scale_out_time = currentTime;
								vnfcParam.vnfc_id_ref = vnfc_server_id;
								vnfmService.updateVnfc(vnfcParam);
							}else if(currentTime - vnfcInfo.scale_out_time > Long.parseLong(scaleOut[k+2])*1000){
								vnfmService.serviceScaleOutProcess(vnfcInfo);
								outflag = true;
								break;
							}
						}
					}
				}
				if(outflag){
					VnfcVO vnfcParam = new VnfcVO();
					vnfcParam.scale_out_time = 1;
					vnfcParam.vnfc_id_ref = vnfc_server_id;
					vnfmService.updateVnfc(vnfcParam);
				}
			}else {
				return ActionResponse.actionFailed("error", 400).toString();
			}
		}
		return ActionResponse.actionSuccess().toString();
	}
	
	/**
	 * Agent 연동 VM 삭제
	 * @param request
	 * @param vnfc_server_id
	 * @return
	 */
	@RequestMapping(value = "/vnf_agent/delete_vnfc/{vnfc_server_id}", method = RequestMethod.GET)
	public String deleteVnfc(HttpServletRequest request,@PathVariable(value = "vnfc_server_id") String vnfc_server_id) {
		
		System.out.println("realtime delete_vnfc: "+vnfc_server_id);
		
		JsonAgentResponse outData = new JsonAgentResponse();
		outData.name = "";
		outData.instance_id = vnfc_server_id;
		
		VnfcVO param = new VnfcVO();
		param.vnfc_id_ref = vnfc_server_id;
		param.status = "SCALE_IN";
		vnfmService.updateVnfc(param);
		vnfmService.deleteServerProcess(vnfc_server_id);
		
		Gson gson = new Gson();
		String outString = gson.toJson(outData);
		
		return outString;
	}
	
	
	public static final class JsonAgentResponse
	{
		public String name;
		public String instance_id;
		public String vnfd_id;
	}
	public static final class JsonCheckScaleInOutRequest
	{
		public String instance_id;
		public String cpu;
		public String memory;
		public String tps;
		public String cps;
		public String session;
	}
}