package com.eluon.pim.snmp.job.server;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.pim.snmp.dao.ServerRepository;
import com.eluon.pim.snmp.service.SnmpServerService;
import com.eluon.pim.snmp.service.SubscriptService;
import com.eluon.pim.snmp.value.server.PimServerSecVO;
import com.eluon.pim.snmp.value.server.PimServerVO;
import com.google.gson.Gson;

public class ServerStatSec implements Job{
	private static final Logger logger = LoggerFactory.getLogger(ServerStatSec.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("ServerStatSec start"+ this);
		// DAO 객체 생성
		ServerRepository repo = new ServerRepository();
		List<PimServerVO> serverList = null;


		try {
			// 서버 목록 조회
			serverList = repo.getServerList();
		} catch (Exception e) {
			logger.error("get server list fail");
		}

		for(PimServerVO target : serverList){
			PimServerVO updateParam = new PimServerVO();
			PimServerSecVO insertParam = new PimServerSecVO();

			try {
				logger.debug(target.getServerId() + "get Snmp Info" + this);
				SnmpServerService service = new SnmpServerService(target.getServerIP());
				Map<String, Object> cpu = service.getCpuInfo();
				Map<String, Object> nic = service.getNicInfo();
				Map<String, Float> storage = service.getStorageInfo();

				String currentNicUsage = new Gson().toJson(nic.get("nicUsage"));

				// Server_Stat table update Param 설정
				updateParam.setServerId( target.getServerId() );
				updateParam.setCpuInfo( cpu.get("cpuInfo").toString() );
				updateParam.setCpuUsage( new Gson().toJson(cpu.get("cpuUsage")) );
				updateParam.setHddTotal( storage.get("hddTotal") );
				updateParam.setHddUsage( storage.get("hddUsage") );
				updateParam.setMemTotal( storage.get("memTotal") );
				updateParam.setMemUsage( storage.get("memUsage") );
				updateParam.setNicInfo( new Gson().toJson(nic.get("nicInfo")) );
				updateParam.setNicUsage( currentNicUsage );
				updateParam.setStatus(1);
				// server_stat_10s table insert param 설정
				insertParam.setServerId( target.getServerId() );
				insertParam.setCpuUsage( new Gson().toJson(cpu.get("cpuUsage")) );
				insertParam.setHddTotal( storage.get("hddTotal") );
				insertParam.setHddUsage( storage.get("hddUsage") );
				insertParam.setMemTotal( storage.get("memTotal") );
				insertParam.setMemUsage( storage.get("memUsage") );
				insertParam.setNicUsage( currentNicUsage );
				logger.debug(target.getServerId() + "get Snmp Info END" + this);
			} catch (Exception e) {
				//TODO: Alarm 테이블 추가 로직 필요
				updateParam.setServerId( target.getServerId() );
				updateParam.setStatus(0);

				SubscriptService subscription = new SubscriptService();
				String message = "target ip :" + target.getServerIP() + "message" + e.getMessage();
				subscription.sendSubscription(1, 0, message);
			}

			try {

				logger.debug(target.getServerId() + "DB Insert" + this);
				// 상테 업데이트
				repo.updateServerInfo(updateParam);
				// 사용량 insert 상태가 이상이 없는 경우에만 insert
				if(updateParam.getStatus() == 1){
					repo.insertServerSec(insertParam);
				}

				logger.debug(target.getServerId() + "DB Insert END" + this);
			} catch (Exception e) {
				logger.error("ServerStatSec Database insert / update error : " + e.getMessage());
			}



		}
		logger.debug("ServerStatSec End"+ this);
	}

}
