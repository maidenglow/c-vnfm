package com.eluon.pim.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimAlarmService;

@RestController
@RequestMapping("/pim/v1/alarms") 
public class PimAlarmController {
	@Autowired
	PimAlarmService alarm;
	
	private static final Logger logger = LoggerFactory.getLogger(PimAlarmController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public Object getAlarmList(String Type, String Count)throws Exception
	{
		logger.debug("Type:"+Type+", Count:"+Count);
		if( Type.equals("서버") )
			return alarm.serverAlarmList(Integer.parseInt(Count));
		else if(Type.equals("스위치"))
			return alarm.switchAlarmList(Integer.parseInt(Count));
		else if( Type.equals("스토리지"))
			return alarm.storageAlarmList(Integer.parseInt(Count));
		return "invalid Type";
	}

}
