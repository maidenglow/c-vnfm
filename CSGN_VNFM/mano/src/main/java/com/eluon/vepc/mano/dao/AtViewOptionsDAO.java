package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * @author jykim325
 *
 */
public interface AtViewOptionsDAO {
	
	//policy
	public List<LinkedHashMap<String, String>> getPolicy(HashMap<String, Object> hm);
	public int postPolicy(HashMap<String, Object> hm);
	public int putPolicy(HashMap<String, Object> hm);
	public int passableDeletePolicy(HashMap<String, Object> hm);
	public int deletePolicy(HashMap<String, Object> hm);
	
	//qos
	public List<LinkedHashMap<String, String>> getQos(HashMap<String, Object> hm);
	public int postQos(HashMap<String, Object> hm);
	public int putQos(HashMap<String, Object> hm);
	public int passableDeleteQos(HashMap<String, Object> hm);
	public int deleteQos(HashMap<String, Object> hm);
	
	//lifecycle
	public List<LinkedHashMap<String, String>> getLifecycle(HashMap<String, Object> hm);
	public int postLifecycle(HashMap<String, Object> hm);
	public int putLifecycle(HashMap<String, Object> hm);
	public int passableDeleteLifecycle(HashMap<String, Object> hm);
	public int deleteLifecycle(HashMap<String, Object> hm);
	
	//monitoring
	public List<LinkedHashMap<String, String>> getMonitoring(HashMap<String, Object> hm);
	public int postMonitoring(HashMap<String, Object> hm);
	public int putMonitoring(HashMap<String, Object> hm);
	public int passableDeleteMonitoring(HashMap<String, Object> hm);
	public int deleteMonitoring(HashMap<String, Object> hm);
	
	//nfp
	public int getNfpCount(HashMap<String, Object> hm);
	public List<LinkedHashMap<String, String>> getNfpOne(HashMap<String, Object> hm);
	public int postNfp(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getNfp(HashMap<String, Object> hm);
	public int deleteNfp(HashMap<String, Object> hm);
	public int passableDeleteNfp(HashMap<String, Object> hm);
	
	//vnfc
	public int getVnfcCount(HashMap<String, Object> hm);
	public List<LinkedHashMap<String, String>> getVnfc(HashMap<String, Object> hm);
	public int postVnfc(HashMap<String, Object> hm);	
	public LinkedHashMap<String, String> getVnfcOne(HashMap<String, Object> hm);
	public int deleteVnfc(HashMap<String, Object> hm);
	public int passableDeleteVnfc(HashMap<String, Object> hm);
	
	
	//vnffgd
	public List<LinkedHashMap<String, String>> getVnffgd(HashMap<String, Object> hm);
	public int postVnffgd(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVnffgdOne(HashMap<String, Object> hm);
	public int putVnffgd(HashMap<String, Object> hm);
	public int deleteVnffgd(HashMap<String, Object> hm);
	
	
	//vnfdep
	public List<LinkedHashMap<String, String>> getVnfdep(HashMap<String, Object> hm);
	public int postVnfdep(HashMap<String, Object> hm);
	public int putVnfdep(HashMap<String, Object> hm);
	public int passableDeleteVnfdep(HashMap<String, Object> hm);
	public int deleteVnfdep(HashMap<String, Object> hm);
	
	
	//vnfrtd
	public List<LinkedHashMap<String, String>> getVnfrtd(HashMap<String, Object> hm);
	public int postVnfrtd(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getVnfrtdOne(HashMap<String, Object> hm);
	public int passableDeleteVnfrtd(HashMap<String, Object> hm);
	public int deleteVnfrtd(HashMap<String, Object> hm);
		
	
	//constraint
	public List<LinkedHashMap<String, String>> getConstraint(HashMap<String, Object> hm);
	public int postConstraint(HashMap<String, Object> hm);
	public int putConstraint(HashMap<String, Object> hm);
	public int passableDeleteConstraint(HashMap<String, Object> hm);
	public int deleteConstraint(HashMap<String, Object> hm);
	
	//df
	public List<LinkedHashMap<String, String>> getDf(HashMap<String, Object> hm);
	public int postDf(HashMap<String, Object> hm);
	public int passableDeleteDf(HashMap<String, Object> hm);
	public int deleteDf(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getDfOne(HashMap<String, Object> hm);
	public int passablePostDf(HashMap<String, Object> hm);
	public int putDf(HashMap<String, Object> hm);
	
	
	//sdf
	public List<LinkedHashMap<String, String>> getSdf(HashMap<String, Object> hm);
	public int postSdf(HashMap<String, Object> hm);
	public LinkedHashMap<String, String> getSdfOne(HashMap<String, Object> hm);
	public int passableDeleteSdf(HashMap<String, Object> hm);
	public int deleteSdf(HashMap<String, Object> hm);
	public int putSdf(HashMap<String, Object> hm);
	
	
	//ref
	public int postPolicyRef(HashMap<String, Object> hm);
	public int deletePolicyRef(HashMap<String, Object> hm);
	
	public int postFunctionRef(HashMap<String, Object> hm);
	public int deleteFunctionRef(HashMap<String, Object> hm);
	
	public int postNfpRef(HashMap<String, Object> hm);
	public int deleteNfpRef(HashMap<String, Object> hm);
	
	public int postConstraintRef(HashMap<String, Object> hm);
	public int postConstituentRef(HashMap<String, Object> hm);
	
	public int deleteConstraintRef(HashMap<String, Object> hm);
	public int deleteConstituentRef(HashMap<String, Object> hm);
	
	public int postConvnfRef(HashMap<String, Object> hm);
	public int deleteConvnfRef(HashMap<String, Object> hm);


	//nfvi(vnf_status)
	public List<LinkedHashMap<String, String>> getState(HashMap<String, Object> hm);
	public int getVersionCnt(HashMap<String, Object> hm);
	public int postState(HashMap<String, Object> hm);
	public int putState(HashMap<String, Object> hm);
	public int passableDeleteState(HashMap<String, Object> hm);
	public int deleteState(HashMap<String, Object> hm);
	
}
