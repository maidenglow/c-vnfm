package com.eluon.pim.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pim/v1/audit") 
public class PimAuditController {
	
	private int systemNo;

	public int getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(int systemNo) {
		this.systemNo = systemNo;
	}

	
	@RequestMapping(method=RequestMethod.GET)
	public Object audit() throws Exception
	{
		Map<String, String> result = new HashMap<>();
		result.put("status", "1");
		//result.put("host_info", Integer.toString(systemNo));
		result.put("host_info", "1");
	
		return result;
	}
}
