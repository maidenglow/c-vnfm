package com.eluon.pim.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.pim.service.PimSubscriptionService;
import com.eluon.pim.value.SubscriptionInfoVO;


@RestController
@RequestMapping("/pim/v1/subscriptions")
public class PimSubscriptionController {

	@Autowired
	PimSubscriptionService subsc;
	private static final Logger logger = LoggerFactory.getLogger(PimSubscriptionController.class);

	@RequestMapping(method=RequestMethod.GET)
	public Object getSubscriptions() throws Exception{
		List<SubscriptionInfoVO> result = subsc.subscriptionList();
		return result;
	}
	
	// value안에 .이 들어가는 경우 .이후 데이터가 잘려서 들어가는 것을 방지하기 위한 reg ex
	@RequestMapping(value="/{clientId:.+}", method=RequestMethod.POST)
	public Object createSubscription(@PathVariable String clientId, HttpServletRequest request, @RequestBody SubscriptionInfoVO param,BindingResult bindingResult)throws Exception{
		int result; 
		logger.debug("clientId:"+clientId);
		param.setClientId(clientId);
		result = subsc.createSubscription(param);
		if( result > 0 ) return "subscription insert complete";
		return "subscription insert failed";
	}
	
	@RequestMapping(value="/{SubscriptionNo}/{clientId:.+}", method=RequestMethod.DELETE)
	public Object deleteSubscription(@PathVariable String SubscriptionNo, @PathVariable String clientId )throws Exception{
		int result; 
		SubscriptionInfoVO param = new SubscriptionInfoVO();
		
		param.setNo(Integer.parseInt(SubscriptionNo));
		param.setClientId(clientId);
			
		result = subsc.deleteSubscription(param);
		if( result > 0 ) return result+" rows deleted";
		return "no rows deleted";
		
	}
}
