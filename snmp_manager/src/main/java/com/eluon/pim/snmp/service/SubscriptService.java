package com.eluon.pim.snmp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.pim.snmp.dao.SubscriptionRepository;
import com.eluon.pim.snmp.util.HttpConnectionUtil;
import com.eluon.pim.snmp.value.PimSubscriptionVO;

public class SubscriptService {
	
	private static final Logger logger = LoggerFactory.getLogger(SubscriptService.class);
	
	public void sendSubscription(int type, int target, String message){
		SubscriptionRepository repository = new SubscriptionRepository();
		PimSubscriptionVO param = new PimSubscriptionVO();
		param.setType(type);
		param.setTargetNo(target);
		List<PimSubscriptionVO> list = repository.getSubscription(param);
		
		for(PimSubscriptionVO temp : list){
			HttpConnectionUtil util = new HttpConnectionUtil();
			
			Map<String, String> params = new HashMap<>();
			params.put("message", message);
			try {
				util.sendPost(temp.getNotifyUrl(), params);
			} catch (Exception e) {
				logger.error(temp.getNotifyUrl() +" error " + e.getMessage());
			}
		}
	}
	
}
