package com.eluon.pim.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.SwitchRepository;
import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.SwitchStatVO;
import com.eluon.pim.value.SwitchVO;

@Service
public class PimSwitchService {
	@Autowired
	SwitchRepository repository;
	private static final Logger logger = LoggerFactory.getLogger(PimServerService.class);

	public List<SwitchVO> switchList() throws Exception{
		return repository.getSwitchList();
	}
	
	public SwitchVO switchOne(int switchId) throws Exception{
		return repository.getSwitch(switchId);
	}
	
	public int createSwitch(SwitchVO param)
	{
		int result=0;
		
		// IP/PORT
		param.setSwitchIP("udp:"+param.getSwitchIP()+"/"+param.getSwitchPort());
		
		// REG_TIME
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		param.setRegTime(format.format(now));
		try {
			result = repository.insertSwitchInfo(param);
			
			logger.debug("SwitchNo"+param.getSwitchNo());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;

	}
	
	public Integer[] deleteSwitch(int switchId) throws Exception{
		Integer[] result = new Integer[5];
		
		try {
			result[4] = repository.deleteSwitchAlarmInfo(switchId);
			result[3] = repository.deleteSwitchEventInfo(switchId);
			result[2] = repository.deleteSwitchStatInfo(switchId);
			result[1] = repository.deleteSwitchStat10S(switchId);
			result[0] = repository.deleteSwitchInfo(switchId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		logger.debug(result.toString());
		return result;
	}
	
	public List<SwitchStatVO> switchStatList(StatParamVO param) throws Exception {
		return repository.getSwitchStatInfo(param);
	}
}
