package com.eluon.pim.snmp.job.server;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eluon.pim.snmp.dao.ServerRepository;
import com.eluon.pim.snmp.value.server.PimServerStatVO;
import com.eluon.pim.snmp.value.server.PimServerVO;

public class ServerStatHr implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// DAO 객체 생성
		ServerRepository repo = new ServerRepository();
		ServerUsageCal cal = new ServerUsageCal();

		try {
			List<PimServerVO> serverList = repo.getServerList();
			
			
			for(PimServerVO target : serverList){
				
				int targetId = target.getServerId();
				
				PimServerStatVO selectParam = new PimServerStatVO();
				selectParam.setServerId(targetId);
				selectParam.setStatType(1);
				
				List<PimServerStatVO> statData = repo.getServerStat(selectParam);
				
				if(statData == null || statData.size() == 0){
					return;
				}
				
				PimServerStatVO insertParam =  cal.getStatData(statData);
				insertParam.setServerId(targetId);
				insertParam.setStatType(2);
				repo.insertServerStat(insertParam);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
