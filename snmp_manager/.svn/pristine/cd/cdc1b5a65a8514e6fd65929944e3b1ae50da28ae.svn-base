package com.eluon.pim.snmp.job.server;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.pim.snmp.dao.ServerRepository;
import com.eluon.pim.snmp.value.server.PimServerSecVO;
import com.eluon.pim.snmp.value.server.PimServerStatVO;
import com.eluon.pim.snmp.value.server.PimServerVO;

public class ServerStatMin implements Job{
	private static final Logger logger = LoggerFactory.getLogger(ServerStatMin.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("ServerStatMin start");
		
		// DAO 객체 생성
		ServerRepository repo = new ServerRepository();
		try {
			List<PimServerVO> serverList = repo.getServerList();
			
			
			for(PimServerVO target : serverList){
				
				int targetId = target.getServerId();
				List<PimServerSecVO> secData = repo.getServerStatSec(targetId);
				
				if(secData == null || secData.size() == 0){
					return;
				}
				
				ServerUsageCal cal = new ServerUsageCal();
				PimServerStatVO insertParam =  cal.getStatMinData(secData);
				insertParam.setServerId(targetId);
				insertParam.setStatType(1);
				repo.insertServerStat(insertParam);
			}
		} catch (Exception e) {
			logger.error("ServerStatMin Database insert error : " + e.getMessage());
		}
		
		logger.debug("ServerStatMin End");
	}
}
