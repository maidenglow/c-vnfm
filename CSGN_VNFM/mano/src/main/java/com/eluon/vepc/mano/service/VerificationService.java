package com.eluon.vepc.mano.service;

import java.util.HashMap;


public interface VerificationService {
	
	public String getManagementPolicyList(HashMap<String, Object> hm);
	public int putManagementPolicyList(HashMap<String, Object> hm);
	public String getManagementPolicyOne(HashMap<String, Object> hm);
	public String getManagementPolicyAll(HashMap<String, Object> hm);
	public int postManagementPolicy(HashMap<String, Object> hm);
	public int putManagementPolicy(HashMap<String, Object> hm);
	public int deleteManagementPolicy(HashMap<String, Object> hm);
	
	public String getTargetList(HashMap<String, Object> hm);
	public String getPolicyList(HashMap<String, Object> hm);
	public String getHostList(HashMap<String, Object> hm);
	public String getComputeList(HashMap<String, Object> hm);
	public String getResultVerification(HashMap<String, Object> hm);
	

}
