package com.eluon.vepc.mano.service;

import java.util.HashMap;

/**
 * options 부분 서비스
 * @author jykim325
 *
 */
public interface AtViewOptionsService {

	
	//###############Options##############
	//policy
	public String getPolicy(HashMap<String, Object> hm);
	public int postAndPutPolicy(HashMap<String, Object> hm);
	public int deletePolicy(HashMap<String, Object> hm);
	
	//qos
	public String getQos(HashMap<String, Object> hm);
	public int postAndPutQos(HashMap<String, Object> hm);
	public int deleteQos(HashMap<String, Object> hm);
		
	//lifecycle
	public String getLifecycle(HashMap<String, Object> hm);
	public int postAndPutLifecycle(HashMap<String, Object> hm);
	public int deleteLifecycle(HashMap<String, Object> hm);
		
	//monitoring
	public String getMonitoring(HashMap<String, Object> hm);
	public int postAndPutMonitoring(HashMap<String, Object> hm);
	public int deleteMonitoring(HashMap<String, Object> hm);

	//vnfdep
	public String getVnfdep(HashMap<String, Object> hm);
	public int postAndPutVnfdep(HashMap<String, Object> hm);
	public int deleteVnfdep(HashMap<String, Object> hm);
	
	
	//nfp
	public String getNfpOne(HashMap<String, Object> hm);
	public int postNfp(HashMap<String, Object> hm);
	public int putNfp(HashMap<String, Object> hm);
	public int deleteNfp(HashMap<String, Object> hm);

	
	//vnfc
	public String getVnfc(HashMap<String, Object> hm);
	public int postVnfc(HashMap<String, Object> hm);
	public int putVnfc(HashMap<String, Object> hm);
	public int deleteVnfc(HashMap<String, Object> hm);
	
	
	//vnffgd
	public String getVnffgd(HashMap<String, Object> hm);
	public int postVnffgd(HashMap<String, Object> hm);
	public int putVnffgd(HashMap<String, Object> hm);
	public int deleteVnffgd(HashMap<String, Object> hm);
	
	
	//vnfrtd
	public String getVnfrtd(HashMap<String, Object> hm);
	public int postVnfrtd(HashMap<String, Object> hm);
	public int putVnfrtd(HashMap<String, Object> hm);
	public int deleteVnfrtd(HashMap<String, Object> hm);
	
	
	//constraint
	public String getConstraint(HashMap<String, Object> hm);
	public int postAndPutConstraint(HashMap<String, Object> hm);
	public int deleteConstraint(HashMap<String, Object> hm);
	
	
	//DF(flavour)
	public String getDf(HashMap<String, Object> hm);
	public int postDf(HashMap<String, Object> hm);
	public int putDf(HashMap<String, Object> hm);
	public int deleteDf(HashMap<String, Object> hm);
	
	
	//sdf
	public String getSdf(HashMap<String, Object> hm);
	public int postSdf(HashMap<String, Object> hm);
	public int putSdf(HashMap<String, Object> hm);
	public int deleteSdf(HashMap<String, Object> hm);
	
	
	//nfvi(vnf_status)
	public String getState(HashMap<String, Object> hm);
	public int postAndPutState(HashMap<String, Object> hm);
	public int deleteState(HashMap<String, Object> hm);
}
