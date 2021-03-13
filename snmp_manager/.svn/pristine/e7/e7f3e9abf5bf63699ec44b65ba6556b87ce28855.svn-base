package com.eluon.pim.snmp.job.switches;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.pim.snmp.dao.SwitchRepository;
import com.eluon.pim.snmp.value.switches.PimSwitchStatVO;

public class SwitchStatDay implements Job {
	private final Logger logger = LoggerFactory.getLogger(SwitchStatDay.class);

	SwitchRepository repo = new SwitchRepository();
	SwitchUsageCal cal = new SwitchUsageCal();

	List<PimSwitchStatVO> switchList = null;
	Map<Integer, List<String>> swMap = new HashMap<>();
	Map<Integer, String> swResultMap = null;
	List<String> listStr = null;
	int lastNo=-1;
	
	Date now = new Date();
	SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try{
			switchList = repo.getSwitchStatInfoDay((dateForm.format(now)).toString()); 		// SWITCH_STAT_10S 테이블로부터 모든 값을 select함. 
		} catch (Exception e){
			e.printStackTrace();
		}
		
		for( PimSwitchStatVO sw : switchList )
		{
			if( lastNo != sw.getSwitchInfoNo())					// 새로운 switch에 대한 목록인 경우. 
			{
				if( lastNo >= 0 && listStr != null ) 	// 두 번째 switch이후에 대한 정보였을 경우, 직전까지의 정보를 put한다. 
				{
					swMap.put(lastNo, listStr);
				}
		
				//swMap.clear();						// Map정보에 새로운 정보를 담기 위해 clear
				if(listStr != null )					// String List에 데이터가 있다면 clear한 후 null초기화. 
				{
					//listStr.clear();
					listStr = null;
				}
				listStr = new ArrayList<String>();		// nic정보를 담기 위해 String List생성.

			}
			if( listStr != null ) 						// String List에 Nic Usage에 대한 정보를 담는다. 
			{
				listStr.add(sw.getNicUsage());

			}
			lastNo = sw.getSwitchInfoNo();
		}/* end for FOR */
		
		if( listStr != null )
		{
			swMap.put(lastNo, listStr);
		}
		
		swResultMap = new HashMap<>();
		swResultMap = cal.getNicSumUsage(swMap);
		
		for(Integer key : swResultMap.keySet())
		{	
			PimSwitchStatVO insertParam = new PimSwitchStatVO();
			
			insertParam.setSwitchInfoNo(key);
			insertParam.setStatType(3);				// define 필요. 
			insertParam.setStatTime(dateForm.format(now));
			insertParam.setNicUsage(swResultMap.get(key));
			logger.error(insertParam.toString());
			
			try {
				repo.insertSwitchStat(insertParam);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

