package com.eluon.pim.snmp.dao;

import java.util.List;

import com.eluon.pim.snmp.value.PimSubscriptionVO;

public class SubscriptionRepository {
	
	public List<PimSubscriptionVO> getSubscription(PimSubscriptionVO param){
		List<PimSubscriptionVO> result = SqlConnector.selectList("subscription.getSubscription", param);
		return result;
	}
}
