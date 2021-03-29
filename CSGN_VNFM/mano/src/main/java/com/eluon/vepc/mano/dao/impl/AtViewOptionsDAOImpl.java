package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.AtViewOptionsDAO;

/**
 * 
 * @author jykim325
 *
 */
@Repository("atViewOptionsDAO")
public class AtViewOptionsDAOImpl implements AtViewOptionsDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SqlSession sqlSession;

	//policy
	@Override
	public List<LinkedHashMap<String, String>> getPolicy(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getPolicy", hm);
	}

	@Override
	public int postPolicy(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postPolicy", hm);
	}

	@Override
	public int putPolicy(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putPolicy", hm);
	}

	@Override
	public int passableDeletePolicy(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeletePolicy", hm);
	}

	@Override
	public int deletePolicy(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deletePolicy", hm);
	}
	
	//policyRef
	public int postPolicyRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postPolicyRef", hm);
	}
	
	
	//qos
	@Override
	public List<LinkedHashMap<String, String>> getQos(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getQos", hm);
	}

	@Override
	public int postQos(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postQos", hm);
	}

	@Override
	public int putQos(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putQos", hm);
	}

	@Override
	public int passableDeleteQos(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteQos", hm);
	}

	@Override
	public int deleteQos(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteQos", hm);
	}
	
	
	
	//lifecycle
	@Override
	public List<LinkedHashMap<String, String>> getLifecycle(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getLifecycle", hm);
	}

	@Override
	public int postLifecycle(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postLifecycle", hm);
	}

	@Override
	public int putLifecycle(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putLifecycle", hm);
	}

	@Override
	public int passableDeleteLifecycle(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteLifecycle", hm);
	}

	@Override
	public int deleteLifecycle(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteLifecycle", hm);
	}
	
	
	//monitoring
	@Override
	public List<LinkedHashMap<String, String>> getMonitoring(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getMonitoring", hm);
	}

	@Override
	public int postMonitoring(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postMonitoring", hm);
	}

	@Override
	public int putMonitoring(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putMonitoring", hm);
	}

	@Override
	public int passableDeleteMonitoring(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteMonitoring", hm);
	}

	@Override
	public int deleteMonitoring(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteMonitoring", hm);
	}

	//nfp
	@Override
	public int getNfpCount(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getNfpCount", hm);
	}
	
	public List<LinkedHashMap<String, String>> getNfpOne(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getNfpOne", hm);
	}

	@Override
	public int postNfp(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postNfp", hm);
	}
	
	@Override
	public int deletePolicyRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deletePolicyRef", hm);
	}

	@Override
	public LinkedHashMap<String, String> getNfp(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getNfp", hm);
	}

	@Override
	public int deleteNfp(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteNfp", hm);
	}
	
	@Override
	public int passableDeleteNfp(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteNfp", hm);
	}

	//vnfc
	@Override
	public int getVnfcCount(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getVnfcCount", hm);
	}

	@Override
	public List<LinkedHashMap<String, String>> getVnfc(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getVnfc", hm);
	}

	@Override
	public int postVnfc(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postVnfc", hm);
	}

	@Override
	public LinkedHashMap<String, String> getVnfcOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getVnfcOne", hm);
	}

	@Override
	public int deleteVnfc(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteVnfc", hm);
	}

	@Override
	public int passableDeleteVnfc(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteVnfc", hm);
	}

	@Override
	public List<LinkedHashMap<String, String>> getVnffgd(	HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getVnffgd", hm);
	}
	
	@Override
	public int postVnffgd(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postVnffgd", hm);
	}

	@Override
	public int postFunctionRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postFunctionRef", hm);		
	}

	@Override
	public int postNfpRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postNfpRef", hm);		
	}

	@Override
	public LinkedHashMap<String, String> getVnffgdOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getVnffgdOne", hm);
	}

	@Override
	public int putVnffgd(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.putVnffgd", hm);	
	}

	@Override
	public int deleteFunctionRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteFunctionRef", hm);
	}

	@Override
	public int deleteNfpRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteNfpRef", hm);
	}

	@Override
	public int deleteVnffgd(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteVnffgd", hm);
	}

	//vnfdep
	@Override
	public List<LinkedHashMap<String, String>> getVnfdep(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getVnfdep", hm);
	}

	@Override
	public int postVnfdep(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postVnfdep", hm);
	}

	@Override
	public int putVnfdep(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putVnfdep", hm);
	}

	@Override
	public int passableDeleteVnfdep(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteVnfdep", hm);
	}

	@Override
	public int deleteVnfdep(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteVnfdep", hm);
	}

	
	
	@Override
	public List<LinkedHashMap<String, String>> getVnfrtd(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getVnfrtd", hm);
	}

	@Override
	public int postVnfrtd(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postVnfrtd", hm);
	}

	@Override
	public LinkedHashMap<String, String> getVnfrtdOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getVnfrtdOne", hm);
	}

	@Override
	public int passableDeleteVnfrtd(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteVnfrtd", hm);
	}

	@Override
	public int deleteVnfrtd(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteVnfrtd", hm);
	}

	

	//constraint
	@Override
	public List<LinkedHashMap<String, String>> getConstraint(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getConstraint", hm);
	}

	@Override
	public int postConstraint(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postConstraint", hm);
	}

	@Override
	public int putConstraint(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putConstraint", hm);
	}

	@Override
	public int passableDeleteConstraint(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteConstraint", hm);
	}

	@Override
	public int deleteConstraint(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteConstraint", hm);
	}

	@Override
	public List<LinkedHashMap<String, String>> getDf(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getDf", hm);
	}

	@Override
	public int postDf(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postDf", hm);
	}

	@Override
	public int passableDeleteDf(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteDf", hm);
	}

	@Override
	public int deleteDf(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteDf", hm);
	}

	@Override
	public LinkedHashMap<String, String> getDfOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getDfOne", hm);
	}

	@Override
	public int passablePostDf(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passablePostDf", hm);
	}

	@Override
	public int postConstraintRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postConstraintRef", hm);
	}

	@Override
	public int postConstituentRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postConstituentRef", hm);
	}

	@Override
	public int deleteConstraintRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteConstraintRef", hm);
	}

	@Override
	public int deleteConstituentRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteConstituentRef", hm);
	}

	@Override
	public int putDf(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putDf", hm);
	}
	
	
	//SDF
	@Override
	public List<LinkedHashMap<String, String>> getSdf(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getSdf", hm);
	}

	@Override
	public int postSdf(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postSdf", hm);
	}

	@Override
	public LinkedHashMap<String, String> getSdfOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getSdfOne", hm);
	}

	@Override
	public int passableDeleteSdf(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteSdf", hm);
	}

	@Override
	public int deleteSdf(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteSdf", hm);
	}

	@Override
	public int postConvnfRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postConvnfRef", hm);
	}

	@Override
	public int deleteConvnfRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteConvnfRef", hm);
	}

	@Override
	public int putSdf(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putSdf", hm);
	}

	//nfvi(vnf_status)
	@Override
	public List<LinkedHashMap<String, String>> getState(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewOptions.getState", hm);
	}
	
	@Override
	public int getVersionCnt(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.getVersionCnt", hm);
	}

	@Override
	public int postState(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewOptions.postState", hm);
	}

	@Override
	public int putState(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewOptions.putState", hm);
	}

	@Override
	public int passableDeleteState(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewOptions.passableDeleteState", hm);
	}
	
	@Override
	public int deleteState(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewOptions.deleteState", hm);
	}


}