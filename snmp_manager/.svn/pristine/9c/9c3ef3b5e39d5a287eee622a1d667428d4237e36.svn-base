package com.eluon.pim.snmp.job.switches;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.eluon.pim.snmp.dao.SwitchRepository;
import com.eluon.pim.snmp.service.SnmpSwitchService;
import com.eluon.pim.snmp.value.switches.PimSwitchSecVO;
import com.eluon.pim.snmp.value.switches.PimSwitchVO;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SwitchStatSec implements Job {
	private final Logger logger = LoggerFactory.getLogger(SwitchStatSec.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		SwitchRepository repo = new SwitchRepository();
		List<PimSwitchVO> switchList = null;
		Date now = new Date();
		SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try{
			switchList = repo.getSwitchList(); 		// SWITCH_INFO 테이블로부터 모든 값을 select함. 
		} catch (Exception e){
			e.printStackTrace();
		}
		
		logger.debug("=== Start ===");
		
		for( PimSwitchVO sw : switchList )			
		{
			SnmpSwitchService switchService = new SnmpSwitchService(sw.getIp());	// List를 하나씩 돌면서 IP값을 가져와서 SNMP 접속. 
			
			PimSwitchVO updateParam = new PimSwitchVO();							// 상태값 DB에 갱신할 객체 
			PimSwitchSecVO insertStat10SecParam = new PimSwitchSecVO();				// 10초 단위 nic_usage
			
			
			// start of SWITCH_INFO ------------- 
			Map<String, String> nic = null;
			try{
				nic = switchService.getNicInfo();				// SNMP로부터 가져온 정보를 Map객체로 저장.
				updateParam.setSwitchId(sw.getSwitchId());							// NO
				updateParam.setStatusTime(dateForm.format(now));					// STATUS_TIME
				updateParam.setNicInfo(new Gson().toJson(nic)); 					// NIC_INFO 
				//updateParam.setVlanInfo(vlanInfo);								// VLAN INFO
				updateParam.setStatus(1); 	// AVAIL								// STATUS : 정상인 경우 AVAIL로 저장. 
			} catch (Exception e) {
				//TODO: SNMP 예외 처리
				updateParam.setStatus(0);	//UNAVAIL								// STATUS : 비정상인 경우 UNAVIL로 저장. 
			}
			
			logger.debug("(Var) NO:{}, Status:{}, StatusTime:{}, NicInfo:{}, VlanInfo:{}", 
					sw.getSwitchId(), 
					(sw.getStatus()==1?"AVAIL":"UNAVIL"), 
					sw.getStatusTime(), 
					sw.getNicInfo(), 
					"NotReady");
			// end of SWITCH_INFO -------------
			
			// start of SWITCH_STAT_10SEC
			Map<String, String> nicUsage = null;
			try {
				nicUsage = switchService.getNicUsage();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			insertStat10SecParam.setNo(sw.getSwitchId());
			insertStat10SecParam.setStatusTime(dateForm.format(now));
			insertStat10SecParam.setNicUsage(new Gson().toJson(nicUsage));
			
			logger.debug("insertStat10SecParam={}", insertStat10SecParam.toString());
			// end of SWITCH_STAT_10SEC

			try{
				repo.updateSwitchInfo(updateParam);
				repo.insertSwitchStat10Sec(insertStat10SecParam);

			}catch (Exception e) {
				//TODO: DB 예외 처리
				e.printStackTrace();
				logger.trace("DB UPDATE Complete");
			}
		} /* end for FOR */
	} /* end for execute */
}
