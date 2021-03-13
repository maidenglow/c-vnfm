package com.eluon.pim.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimSwitchService;
import com.eluon.pim.value.StatParamVO;
import com.eluon.pim.value.SwitchVO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/pim/v1/switchs")
public class PimSwitchController {
	
	@Autowired
	PimSwitchService switchSvc;
	
	private static final Logger logger = LoggerFactory.getLogger(PimSwitchController.class);

	@RequestMapping(method=RequestMethod.GET)
	public Object getSwitches() throws Exception{
		
		List<SwitchVO>  result = switchSvc.switchList();
		
		return result;
	}
	@RequestMapping(value="/{switchNo}",method=RequestMethod.GET)
	public Object getSwitch(@PathVariable String switchNo) throws Exception{
		
		SwitchVO result  = switchSvc.switchOne(Integer.parseInt(switchNo));
		
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Object createSwitch(HttpServletRequest request, @RequestBody SwitchVO param,BindingResult bindingResult) throws Exception{
		logger.debug(param.toString());
		
		//validation 		
		if(bindingResult.hasErrors()){
			logger.error("input Data error");
			List<ObjectError> errors = bindingResult.getAllErrors();
			
			for(ObjectError err : errors)
				logger.error(err.getDefaultMessage());
			return errors;
		}
		if(0 < switchSvc.createSwitch(param)){
			return new Gson().toJson(param);
		}		
		return "switch insert failed";
	}
	
	@RequestMapping(value="/{switchNo}",method=RequestMethod.DELETE) 
	public Object deleteSwitch(@PathVariable String switchNo) throws Exception{
		Integer [] result = null;
		String resultStr = new String();
		result = switchSvc.deleteSwitch(Integer.parseInt(switchNo));
		
		resultStr = String.format(
				  "SWITCH_ALARM_INFO : %d rows deleted\n"
				+ "SWITCH_EVENT_INFO : %d rows deleted\n"
				+ "SWITCH_STAT_INFO : %d rows deleted\n"
				+ "SWITCH_STAT_10S : %d rows deleted\n"
				+ "SWITCH_INFO : %d rows deletedd\n", 
				result[4], result[3], result[2], result[1], result[0]);
		
		return resultStr;
	}
	
	@RequestMapping(value="/statics/{switchNo}",method=RequestMethod.GET) 
	public Object getSwitchStat(@PathVariable String switchNo, String Type, String Start_date, String End_date ) throws Exception{
		StatParamVO param = new StatParamVO();
		logger.debug("switchNo={}, Type={}, start={}, end={}", switchNo, Type, Start_date, End_date);
	
		if(param.isValidStatType(Type) == false)
		{
			return "Invald Type";
		}
		param.setStatType(Integer.parseInt(Type));
		param.setStartTime(Start_date);
		param.setEndTime(End_date);
		return switchSvc.switchStatList(param);
	}
	
}


