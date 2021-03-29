package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.ParserDao;

/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */

@Repository("parserDAO")
public class ParserDaoImpl implements ParserDao{
	
	@Autowired
	private SqlSession sqlSession;
	
	//vld
	public List<LinkedHashMap<String, String>> getVldOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVldOne", hm);
	}
	
	public int getVldCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVldCount", hm);
	}
	
	public int getVldCount() {
		return sqlSession.selectOne("Parser.getVldCount");
	}
	
	public List<String> getVldAllId() {
		return sqlSession.selectList("Parser.getVldAllId");
	}
	
	//pnfd
	public List<LinkedHashMap<String, String>> getPnfdOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getPnfdOne", hm);
	}
	
	public int getPnfdCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getPnfdCount", hm);
	}
	
	public int getPnfdCount() {
		return sqlSession.selectOne("Parser.getPnfdCount");
	}
	
	public List<String> getPnfdAllId() {
		return sqlSession.selectList("Parser.getPnfdAllId");
	}
	
	//cp
	public LinkedHashMap<String, String> getCpOne(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getCpOne", hm);
	}
	
	public int getCpCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getCpCount", hm);
	}
	
	public int getCpCount() {
		return sqlSession.selectOne("Parser.getCpCount");
	}
	
	public List<String> getCpAllId() {
		return sqlSession.selectList("Parser.getCpAllId");
	}
	
	//nfp
	public List<LinkedHashMap<String, String>> getNfpOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getNfpOne", hm);
	}
	
	public int getNfpCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getNfpCount", hm);
	}
	
	public int getNfpCount() {
		return sqlSession.selectOne("Parser.getNfpCount");
	}
	
	public List<String> getNfpAllId() {
		return sqlSession.selectList("Parser.getNfpAllId");
	}
		
	//vnffgd
	public List<LinkedHashMap<String, String>> getVnffgdOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVnffgdOne", hm);
	}
	
	public int getVnffgdCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVnffgdCount", hm);
	}
	
	public int getVnffgdCount() {
		return sqlSession.selectOne("Parser.getVnffgdCount");
	}
	
	public List<String> getVnffgdAllId() {
		return sqlSession.selectList("Parser.getVnffgdAllId");
	}
	
	
	//vnfc
	public List<LinkedHashMap<String, String>> getVnfcList(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVnfcOne", hm);
	}
	
	public int getVnfcCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVnfcCount", hm);
	}
	
	public int getVnfcCount() {
		return sqlSession.selectOne("Parser.getVnfcCount");
	}
	
	public List<String> getVnfcAllId() {
		return sqlSession.selectList("Parser.getVnfcAllId");
	}
	
	//vdu
	public List<LinkedHashMap<String, String>> getVduOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVduOne", hm);
	}
	
	public int getVduCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVduCount", hm);
	}
	
	public int getVduCount() {
		return sqlSession.selectOne("Parser.getVduCount");
	}
	
	public List<String> getVduAllId() {
		return sqlSession.selectList("Parser.getVduAllId");
	}
	
	//df
	public List<LinkedHashMap<String, String>> getDfOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getDfOne", hm);
	}
	
	public int getDfCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getDfCount", hm);
	}
	
	public int getDfCount() {
		return sqlSession.selectOne("Parser.getDfCount");
	}
	
	public List<LinkedHashMap<String, String>> getDfAllId() {
		return sqlSession.selectList("Parser.getDfAllId");
	}
	
	//vnfd
	public List<LinkedHashMap<String, String>> getVnfdOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVnfdOne", hm);
	}
	
	public int getVnfdCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVnfdCount", hm);
	}
	
	public int getVnfdCount() {
		return sqlSession.selectOne("Parser.getVnfdCount");
	}
	
	public List<String> getVnfdAllId() {
		return sqlSession.selectList("Parser.getVnfdAllId");
	}
	
	//sdf
	public List<LinkedHashMap<String, String>> getSdfOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getSdfOne", hm);
	}
	
	public int getSdfCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getSdfCount", hm);
	}
	
	public int getSdfCount() {
		return sqlSession.selectOne("Parser.getSdfCount");
	}
	
	public List<String> getSdfAllId() {
		return sqlSession.selectList("Parser.getSdfAllId");
	}
	
	//nsd
	public List<LinkedHashMap<String, String>> getNsdOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getNsdOne", hm);
	}
	
	public int getNsdCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getNsdCount", hm);
	}
	
	public int getNsdCount() {
		return sqlSession.selectOne("Parser.getNsdCount");
	}
	
	public List<String> getNsdAllId() {
		return sqlSession.selectList("Parser.getNsdAllId");
	}
	
	//vnfrtd
	public List<LinkedHashMap<String, String>> getVnfrtdOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getVnfrtdOne", hm);
	}
	
	public int getVnfrtdCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getVnfrtdCount", hm);
	}
	
	public int getVnfrtdCount() {
		return sqlSession.selectOne("Parser.getVnfrtdCount");
	}
	
	public List<String> getVnfrtdAllId() {
		return sqlSession.selectList("Parser.getVnfrtdAllId");
	}
	
	//nfvi (기존 nfvid)
	public List<LinkedHashMap<String, String>> getNfvidOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getNfvidOne", hm);
	}
	
	public int getNfvidCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getNfvidCount", hm);
	}
	
	public int getNfvidCount() {
		return sqlSession.selectOne("Parser.getNfvidCount");
	}
	
	public List<String> getNfvidAllId() {
		return sqlSession.selectList("Parser.getNfvidAllId");
	}

	// nfvic(기존 nfvidc)
	@Override
	public List<LinkedHashMap<String, String>> getNfvicOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getNfvicOne", hm);
	}

	@Override
	public int getNfvicCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getNfvicCount", hm);
	}

	@Override
	public int getNfvicCount() {
		return sqlSession.selectOne("Parser.getNfvicCount");
	}

	@Override
	public List<String> getNfvicAllId() {
		return sqlSession.selectList("Parser.getNfvicAllId");
	}

	// constituent
	@Override
	public List<LinkedHashMap<String, String>> getConstituenOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getConstituenOne", hm);
	}

	@Override
	public int getConstituenCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getConstituenCount", hm);
	}

	@Override
	public int getConstituenCount() {
		return sqlSession.selectOne("Parser.getConstituenCount");
	}

	@Override
	public List<String> getConstituenAllId() {
		return sqlSession.selectList("Parser.getConstituenAllId");
	}

	// cvnf(constituent vnf)
	@Override
	public List<LinkedHashMap<String, String>> getCvnfOne(HashMap<String, String> hm) {
		return sqlSession.selectList("Parser.getCvnfOne", hm);
	}

	@Override
	public int getCvnfCount(HashMap<String, String> hm) {
		return sqlSession.selectOne("Parser.getCvnfCount", hm);
	}

	@Override
	public int getCvnfCount() {
		return sqlSession.selectOne("Parser.getCvnfCount");
	}

	@Override
	public List<LinkedHashMap<String, String>> getCvnfAllId() {
		return sqlSession.selectList("Parser.getCvnfAllId");
	}
}
