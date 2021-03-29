package com.eluon.vepc.mano.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.JDOMException;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;


public interface VerificationDAO {
	public List<LinkedHashMap<String, String>> getManagementPolicyList(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getManagementPolicy(HashMap<String, Object> hm);
	public List<String> getManagementPolicyIdList(HashMap<String, Object> hm);
	public int postManagementPolicy(HashMap<String, Object> hm);
	public int postManagementPolicyRef(HashMap<String, Object> hm);
	public int putManagementPolicy(HashMap<String, Object> hm);
	public int deleteManagementPolicy(HashMap<String, Object> hm);
	public int deleteManagementPolicyRef(HashMap<String, Object> hm);
	public List<? extends Hypervisor> getHostList(HashMap<String, Object> hm);
	public List<? extends Server> getComputeList(HashMap<String, Object> hm);
	public String getResultVerification(HashMap<String, Object> hm);
	public String getResultVerificationTest(HashMap<String, Object> hm);
}
