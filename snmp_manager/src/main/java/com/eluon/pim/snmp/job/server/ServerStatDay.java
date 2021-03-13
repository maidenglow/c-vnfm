package com.eluon.pim.snmp.job.server;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.pim.snmp.dao.ServerRepository;
import com.eluon.pim.snmp.value.server.PimServerStatVO;
import com.eluon.pim.snmp.value.server.PimServerVO;

public class ServerStatDay implements Job{
	private static final Logger logger = LoggerFactory.getLogger(ServerStatDay.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("ServerStatDay start");
		// DAO 객체 생성
		ServerRepository repo = new ServerRepository();

		try {
			List<PimServerVO> serverList = repo.getServerList();
			
			
			for(PimServerVO target : serverList){
				
				int targetId = target.getServerId();
				
				PimServerStatVO selectParam = new PimServerStatVO();
				
				selectParam.setServerId(targetId);
				selectParam.setStatType(2);
				
				List<PimServerStatVO> statData = repo.getServerStat(selectParam);
				
				if(statData == null || statData.size() == 0){
					return;
				}
				
				ServerUsageCal cal = new ServerUsageCal();
				PimServerStatVO insertParam =  cal.getStatData(statData);
				insertParam.setServerId(targetId);
				insertParam.setStatType(3);
				repo.insertServerStat(insertParam);
			}
		} catch (Exception e) {
			logger.error("ServerStatDay Database insert error : " + e.getMessage());
		}
		logger.debug("ServerStatDay End");
	}
}
