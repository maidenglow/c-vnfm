package com.eluon.pim.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimEventService;

@RestController
@RequestMapping("/pim/v1/events") 
public class PimEventController {
	@Autowired
	PimEventService event;
	
	private static final Logger logger = LoggerFactory.getLogger(PimEventController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public Object getEventList(String Type, String Count)throws Exception
	{
		logger.debug("Type:"+Type+", Count:"+Count);
		if( Type.equals("서버") )
			return event.serverEventList(Integer.parseInt(Count));
		else if(Type.equals("스위치"))
			return event.switchEventList(Integer.parseInt(Count));
		else if( Type.equals("스토리지"))
			return event.storageEventList(Integer.parseInt(Count));
		return "invalid Type";
	}
}
