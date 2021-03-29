package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.AtViewDAO;

/**
 * AccountDAO Implement (AccountDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAOImpl.java,v 1.1 2014/12/15 00:00:00 SimByungChul Express $
 */
@Repository("atViewDAO")
public class AtViewDAOImpl implements AtViewDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<LinkedHashMap<String, String>> getVldAtViewMeta(HashMap<String, String> hm) {
		return sqlSession.selectList("Atview.getVldAtViewMeta");
	}

	@Override
	public List<LinkedHashMap<String, String>> getVmAtViewMeta(HashMap<String, String> hm) {
		return sqlSession.selectList("Atview.getVmAtViewMeta");
	}

	@Override
	public List<LinkedHashMap<String, String>> getTmenu(HashMap<String, String> hm) {
		return sqlSession.selectList("Atview.getTmenu", hm);
	}
	
	@Override
	public List<LinkedHashMap<String, String>> getToption(HashMap<String, String> hm) {
		return sqlSession.selectList("Atview.getTmenu", hm);
	}
	
	//vld
	public List<LinkedHashMap<String, String>> getVld(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getVld", hm);
	}
	
	public LinkedHashMap<String, String> getVersionOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getVersionOne", hm);
	}
	
	public LinkedHashMap<String, String> getVldOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getVldOne", hm);
	}
	
	public int postVld(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVld", hm);
	}
	
	public int postVersion(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVersion", hm);
	}
	
	public int postQosRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postQosRef", hm);
	}
	
	public int postCpRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postCpRef", hm);
	}
	
	public int postSecurityRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postSecurityRef", hm);
	}
	
	public int putVld(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putVld", hm);
	}
	
	public int putVersion(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putVersion", hm);
	}
	
	public int deleteVersion(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVersion", hm);
	}
	
	public int passableDeleteMainContent(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.passableDeleteMainContent", hm);
	}
	
	public int deleteVld(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVld", hm);
	}
	
	public int deleteQosRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteQosRef", hm);
	}
	
	public int deleteCpRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteCpRef", hm);
	}
	
	public int deleteSecurityRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteSecurityRef", hm);
	}

	//cp
	public List<LinkedHashMap<String, String>> getCp(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getCp", hm);
	}
	
	public int postCp(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postCp", hm);
	}
	
	public int putCp(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putCp", hm);
	}
	
	public int passableDeleteCp(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.passableDeleteCp", hm);
	}
	public int deleteCp(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteCp", hm);
	}
	
	//pnfd
	public List<LinkedHashMap<String, String>> getPnfd(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getPnfd", hm);
	}
	
	public LinkedHashMap<String, String> getPnfdOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getPnfdOne", hm);
	}
	
	public int postPnfd(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postPnfd", hm);
	}
	
	public int putPnfd(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putPnfd", hm);
	}
	
	public int deletePnfd(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deletePnfd", hm);
	}
	
	//tmenu
	
	public LinkedHashMap<String, String> getTmenuOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getTmenuOne", hm);
	}
	public int postTmenu(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postTmenu", hm);
	}
	
	public int deleteTmenu(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteTmenu", hm);
	}
		
	//tmenuRef
	public int postTmenuRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postTmenuRef", hm);
	}
	
	public int deleteTmenuRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteTmenuRef", hm);
	}
	
	public List<LinkedHashMap<String, String>> getSecurity(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getSecurity", hm);
	}
	
	public int postSecurity(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postSecurity", hm);
	}
	
	public int putSecurity(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putSecurity", hm);
	}
	
	public int passableDeleteSecurity(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.passableDeleteSecurity", hm);
	}
	
	public int deleteSecurity(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteSecurity", hm);
	}

	@Override
	public List<LinkedHashMap<String, String>> getPolicy(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getPolicy", hm);
	}

	@Override
	public int postPolicy(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postPolicy", hm);
	}

	@Override
	public int putPolicy(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putPolicy", hm);
	}

	@Override
	public int passableDeletePolicy(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.passableDeletePolicy", hm);
	}

	@Override
	public int deletePolicy(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deletePolicy", hm);
	}
	
	@Override
	public List<LinkedHashMap<String, String>> getVdu(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getVdu", hm);
	}
	
	@Override
	public int postVdu(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVdu", hm);
	}
	
	@Override
	public LinkedHashMap<String, String> getVduOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getVduOne", hm);
	}
	
	@Override
	public int putVdu(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putVdu", hm);
	}
	
	@Override
	public int deleteVdu(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVdu", hm);
	}
	
	@Override
	public int postLifecycleRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postLifecycleRef", hm);
	}
	
	@Override
	public int postVnfcRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVnfcRef", hm);
	}
	
	@Override
	public int postMonitoringRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postMonitoringRef", hm);
	}
	
	@Override
	public int deleteLifecycleRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteLifecycleRef", hm);
	}
	
	@Override
	public int deleteVnfcRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVnfcRef", hm);
	}
	
	@Override
	public int deleteMonitoringRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteMonitoringRef", hm);
	}
	
	//nfvi(기존 nfvid)
	@Override
	public List<LinkedHashMap<String, String>> getNfvi(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getNfvi", hm);
	}

	@Override
	public LinkedHashMap<String, String> getNfviOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getNfviOne", hm);
	}

	@Override
	public int postNfvi(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postNfvi", hm);
	}	
	
	@Override
	public int postImageTab(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postImageTab", hm);
	}

	@Override
	public int postMntRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postMntRef", hm);
	}

	@Override
	public int postFuncRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postFuncRef", hm);
	}

	@Override
	public int deleteNfvi(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteNfvi", hm);
	}
	
	@Override
	public int deleteImageTab(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteImageTab", hm);
	}

	@Override
	public int deleteMntRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteMntRef", hm);
	}

	@Override
	public int deleteFuncRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteFuncRef", hm);
	}

	//vnfd
	@Override
	public List<LinkedHashMap<String, String>> getVnfd(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getVnfd", hm);
	}

	@Override
	public LinkedHashMap<String, String> getVnfdOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getVnfdOne", hm);
	}

	@Override
	public int postVnfd(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVnfd", hm);
	}
	
	@Override
	public int postFlavourRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postFlavourRef", hm);
	}

	@Override
	public int postPolicyRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postPolicyRef", hm);
	}
	
	@Override
	public int putVnfd(HashMap<String, Object> hm) {
		return sqlSession.update("Atview.putVnfd", hm);
	}
	
	@Override
	public int deleteVnfd(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVnfd", hm);
	}

	@Override
	public int deleteFlavourRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteFlavourRef", hm);
	}

	@Override
	public int deletePolicyRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deletePolicyRef", hm);
	}

	//nsd
	@Override
	public List<LinkedHashMap<String, String>> getNsd(HashMap<String, Object> hm) {
		return sqlSession.selectList("Atview.getNsd", hm);
	}

	@Override
	public LinkedHashMap<String, String> getNsdOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Atview.getNsdOne", hm);
	}

	@Override
	public int postNsd(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postNsd", hm);
	}

	@Override
	public int postVnfdepRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postVnfdepRef", hm);
	}

	@Override
	public int postSdfRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Atview.postSdfRef", hm);
	}

	@Override
	public int deleteNsd(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteNsd", hm);
	}

	@Override
	public int deleteVnfdepRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteVnfdepRef", hm);
	}

	@Override
	public int deleteSdfRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Atview.deleteSdfRef", hm);
	}






}