package com.eluon.pim.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.pim.repository.SubscriptionRepository;
import com.eluon.pim.value.SubscriptionInfoVO;

@Service
public class PimSubscriptionService {
	@Autowired
	SubscriptionRepository repository;
	private static final Logger logger = LoggerFactory.getLogger(PimSubscriptionService.class);
	
	public List <SubscriptionInfoVO> subscriptionList() throws Exception{
		return repository.getSubscriptionList();
	}
	public int createSubscription(SubscriptionInfoVO param)throws Exception{
		int result=0;
		logger.debug(param.toString());
		try {
			result = repository.insertSubscription(param);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}
	
	public int deleteSubscription(SubscriptionInfoVO param)throws Exception{
		int result=0;
		logger.debug(param.toString());
		try{
			result = repository.deleteSubscription(param);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

}
