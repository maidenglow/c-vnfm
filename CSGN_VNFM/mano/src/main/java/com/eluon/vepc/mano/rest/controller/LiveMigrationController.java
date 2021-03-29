package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.common.MessageSender;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.LiveMigrationService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VnfcVO;
import com.google.gson.Gson;


@RestController
public class LiveMigrationController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private  LiveMigrationService liveMigrationService;
	
	@Resource
	private MessageSender messageSender;
	
	@Autowired
	private ComputeService computeService;
	@Autowired
	private VNFMService vnfmService;
	
	@RequestMapping(value = "/livemigration/list/{zoneName}", method = RequestMethod.GET)
	public String getPolicy(HttpServletResponse httpServletResponse, @PathVariable("zoneName") String zoneName) throws IOException {

//		HashMap<String,Object> hm = new HashMap<String,Object>();

		return liveMigrationService.getInstanceList(zoneName);
	}
	
	@RequestMapping(value = "/livemigration/setmigration/{instanceId}/{hyperId}/{option}", method = RequestMethod.GET)
	public void setMigration(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,
			@PathVariable("instanceId") String instanceId,
			@PathVariable("hyperId") String hyperId,
			@PathVariable("option") boolean option
			) throws IOException, InterruptedException {
		
		
		VnfcVO vnfcParam = new VnfcVO();
		vnfcParam.vnfc_id = instanceId;
		VnfcVO vnfc = vnfmService.getVnfcList(vnfcParam).get(0);
		
		
		
		if(vnfc.affinity.equals("1")){
			Map<String, Object> responseJson = new HashMap<String, Object>();
			responseJson.put("result", false);
			responseJson.put("message", "Against the affinity rules.");
			Gson gson = new Gson();
			httpServletResponse.getWriter().println(gson.toJson(responseJson));
			return;
		}else if(vnfc.affinity.equals("2")){
			
			String splIndex[] = vnfc.vnfc_name.split("_"); 
			String vnfcName = null;
			if(Integer.parseInt(splIndex[2])%2 == 0){
				vnfcName =  splIndex[0] +"_"+ splIndex[1] +"_"+ (Integer.parseInt(splIndex[2])-1);
			}else {
				vnfcName =  splIndex[0] +"_"+ splIndex[1] +"_"+ (Integer.parseInt(splIndex[2])+1);
			}
			vnfcParam = new VnfcVO();
			vnfcParam.vnfc_name = vnfcName;
			VnfcVO vnfc2 = vnfmService.getVnfcList(vnfcParam).get(0);
			
			Server server = computeService.getServer(vnfc2.vnfc_id);
			
			if(server.getHypervisorHostname().equals(hyperId)){
				Map<String, Object> responseJson = new HashMap<String, Object>();
				responseJson.put("result", false);
				responseJson.put("message", "Against the anty-affinity rules.");
				Gson gson = new Gson();
				httpServletResponse.getWriter().println(gson.toJson(responseJson));
				return;
			}
		} 
		
		EventLogVO event = new EventLogVO();
		event.descriptor = "VM Live Migration start to "+ hyperId;
		event.name = vnfc.vnfc_name;
		event.log_id = vnfc.vnfc_id; 
		vnfmService.addEvnetLogInfo(event);
		
		//http://localhost:8282/osn/api/setMigration.do?serverId=ae28bdb8-fae2-4654-bd17-f2f51d1ef3b9&hyperId=compute1
		
		logger.info("###########Live Migration Start#############");
		logger.info("instanceId : " + instanceId +", hyperId : " + hyperId + ", blockmigration option : "+ option);		
			
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		boolean result = false;
		//instanceId : 현재 instance의 ID, hyperId : 변경하고자 하는 서버, option : block migration(true) 혹은 sharedmigration(false)
		//result =true;
		result = liveMigrationService.setMigration(instanceId, hyperId, option);		
		logger.info("###migration Result : " + result);
				
		Map<String, Boolean> responseJson = new HashMap<String, Boolean>();
		responseJson.put("result", result);
//		messageSender.VnfcStatusListener(vnfc, "active", "Live Migration");
		EventLogVO event2 = new EventLogVO();
		if(result){
			event2.descriptor = "VM Live Migration end to "+ hyperId;
		}else {
			event2.descriptor = "VM Live Migration fail to "+ hyperId;
		}
		event2.name = vnfc.vnfc_name;
		event2.log_id = vnfc.vnfc_id; 
		vnfmService.addEvnetLogInfo(event2);
		
//		vnfmService.setVnfcInfo();
		Gson gson = new Gson();
		httpServletResponse.getWriter().println(gson.toJson(responseJson));
				
	}
}
