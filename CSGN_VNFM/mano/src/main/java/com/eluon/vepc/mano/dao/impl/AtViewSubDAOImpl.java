package com.eluon.vepc.mano.dao.impl;

/**
 * @author seon
 *
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.AtViewSubDAO;

@Repository("atViewSubDAO")
public class AtViewSubDAOImpl implements AtViewSubDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SqlSession sqlSession;
	
	/* constituent */
	@Override
	public List<LinkedHashMap<String, String>> getConstituent(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewSub.getConstituent", hm);
	}
	
	@Override
	public LinkedHashMap<String, String> getConstituentOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getConstituentOne", hm);
	}
	
	@Override
	public int postConstituent(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postConstituent", hm);
	}
	
	@Override
	public int postVnfcRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postVnfcRef", hm);
	}

	@Override
	public int putConstituent(HashMap<String, Object> hm) {
		return sqlSession.update("AtviewSub.putConstituent", hm);
	}

	@Override
	public int passableDeleteConstituent(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.passableDeleteConstituent", hm);
	}

	@Override
	public int deleteConstituent(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteConstituent", hm);
	}
	
	@Override
	public int deleteVnfcRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteVnfcRef", hm);
	}
	
	/* nfvic */
	@Override
	public List<LinkedHashMap<String, String>> getNfvic(HashMap<String, Object> hm) {
		return sqlSession.selectList("AtviewSub.getNfvic", hm);
	}

	@Override
	public LinkedHashMap<String, String> getNfvicOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getNfvicOne", hm);
	}

	@Override
	public int postNfvic(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postNfvic", hm);
	}
	
	@Override
	public int postMntRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postMntRef", hm);
	}

	@Override
	public int postFuncRef(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postFuncRef", hm);
	}

	@Override
	public int deleteNfvic(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteNfvic", hm);
	}

	@Override
	public int deleteMntRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteMntRef", hm);
	}

	@Override
	public int deleteFuncRef(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteFuncRef", hm);
	}

	/* cvnf(constituent vnf) */
	@Override
	public LinkedHashMap<String, String> getCvnf(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getCvnf", hm);
	}

	@Override
	public LinkedHashMap<String, String> getCvnfOne(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getCvnfOne", hm);
	}
	
	@Override
	public int getCvnfCount(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getCvnfCount", hm);
	}

	@Override
	public LinkedHashMap<String, String> getVersion(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.getVersion", hm);
	}
	
	@Override
	public int postCvnf(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.postCvnf", hm);
	}

	@Override
	public int putCvnf(HashMap<String, Object> hm) {
		return sqlSession.insert("AtviewSub.putCvnf", hm);
	}

	@Override
	public int deleteCvnf(HashMap<String, Object> hm) {
		return sqlSession.delete("AtviewSub.deleteCvnf", hm);
	}

	@Override
	public int passableDeleteCvnf(HashMap<String, Object> hm) {
		return sqlSession.selectOne("AtviewSub.passableDeleteCvnf", hm);
	}

}
