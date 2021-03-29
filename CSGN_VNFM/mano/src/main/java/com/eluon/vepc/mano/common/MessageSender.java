package com.eluon.vepc.mano.common;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.EventLogVO;
import com.eluon.vepc.mano.vo.VnfcVO;




@Component
public class MessageSender {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ComputeService computeService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private CommonService commonService;
	@Autowired
	private VNFMService vnfmService;
	
	@Async
	public void VnfPowerListener(ArrayList<String> listVnfcId,String vnfd_id, String vnfd_name, String status) throws InterruptedException {
		
		
		ArrayList<String> checkVnfdId = new ArrayList<String>(listVnfcId);
		int k = 0;
		while (checkVnfdId.size() != 0 || k > 200) {
			for (int i = 0; i < listVnfcId.size(); i++) {
				if(checkVnfdId.contains(listVnfcId.get(i))){
					Server server = computeService.getServer(listVnfcId.get(i));
					if(server.getStatus().value().equals(status)){
						checkVnfdId.remove(server.getId());
					}
				}
			}
			Thread.sleep(1000);k++;
		}
		
		EventLogVO event2 = new EventLogVO();
		if(status.equals("shutoff"))
			event2.descriptor = "VNF Power off 완료";
		else {
			event2.descriptor = "VNF Power on 완료";
		}
		event2.name = vnfd_name;
		event2.log_id = vnfd_id;
		vnfmService.addEvnetLogInfo(event2);
		
	}
	
	@Async
	public void VnfStartListener(ArrayList<String> listVnfcId,String vnfd_id, String vnfd_name) throws InterruptedException {
		
		ArrayList<String> checkVnfdId = new ArrayList<String>(listVnfcId);
		String errorVnfcId = "";
		int k = 0;
		while (checkVnfdId.size() != 0 || k > 200) {
			for (int i = 0; i < listVnfcId.size(); i++) {
				if(checkVnfdId.contains(listVnfcId.get(i))){
					Server server = computeService.getServer(listVnfcId.get(i));
					if(server.getStatus().value().equalsIgnoreCase("active")){
						checkVnfdId.remove(server.getId());
						EventLogVO event = new EventLogVO();
						event.descriptor = "VM 생성완료";
						event.name = server.getName();
						event.log_id = server.getId(); 
						vnfmService.addEvnetLogInfo(event);
						
					}else if(server.getStatus().value().equalsIgnoreCase("error")){
						errorVnfcId = errorVnfcId+ " "+ server.getName();
						checkVnfdId.remove(server.getId());
						EventLogVO event = new EventLogVO();
						event.descriptor = "VM 생성 에러";
						event.name = server.getName();
						event.log_id = server.getId(); 
						vnfmService.addEvnetLogInfo(event);
						
					}else {
					}
				}
			}
			System.out.println("====================="+k);
			Thread.sleep(1000);k++;
		}
		
		EventLogVO event2 = new EventLogVO();
		if(errorVnfcId.length() > 2){
			event2.descriptor = "VNF 시작 실패 : "+ errorVnfcId;
		}else {
			event2.descriptor = "VNF 시작 성공";
		}
		event2.name = "VNF 시작 완료";
		event2.log_id = vnfd_id;
		vnfmService.addEvnetLogInfo(event2);
		
	}
	
	@Async
	public void VnfcStatusListener(VnfcVO vnfc, String status,String type) throws InterruptedException {

		String strSuccess = "";
		Server server = commonService.buildOSFactory().compute().servers().waitForServerStatus(vnfc.vnfc_id, Status.ACTIVE, 120, TimeUnit.SECONDS);
		if(server.getStatus().toString().equalsIgnoreCase(status)){
			strSuccess = " 성공";
		}else {
			strSuccess = " 실패";
		}
		
		EventLogVO event2 = new EventLogVO();
		event2.descriptor = "VM "+type+ strSuccess;
		event2.name = vnfc.vnfc_name;
		event2.log_id = vnfc.vnfc_id;
		vnfmService.addEvnetLogInfo(event2);
	}
	
	
	@Async
	public Future<String> send(String message) {

		// TODO Auto-generated method stub
		long sTime = System.currentTimeMillis();
		logger.info(System.currentTimeMillis() + " : " + "MessagesSenderImpl.send(String message) start. ");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long eTime = System.currentTimeMillis();
		logger.info(System.currentTimeMillis() + " : " +"MessagesSenderImpl.send(String message) end. ");
		return new AsyncResult<String>("[echo]" + message);
	}

	
	@Async
	public void send2(String message) {
		// TODO Auto-generated method stub
		long sTime = System.currentTimeMillis();
		logger.info(System.currentTimeMillis() + " : " + "MessagesSenderImpl.send2(String message) start. ");
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(3000);
				logger.info("test data : "+ i);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long eTime = System.currentTimeMillis();
		logger.info(System.currentTimeMillis() + " : " + "MessagesSenderImpl.send2(String message) end. ");
	}
	
}
