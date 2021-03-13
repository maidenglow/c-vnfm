package com.eluon.pim.repository;

import java.util.List;

import com.eluon.pim.value.SubscriptionInfoVO;

public interface SubscriptionRepository {
	public List<SubscriptionInfoVO> getSubscriptionList() throws Exception;
	public int insertSubscription(SubscriptionInfoVO param);
	public int deleteSubscription(SubscriptionInfoVO param);
}
