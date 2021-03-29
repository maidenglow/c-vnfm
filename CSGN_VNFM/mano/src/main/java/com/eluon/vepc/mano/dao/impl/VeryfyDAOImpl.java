package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.VeryfyDAO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.VeryfyVO;

/**
 * AccountDAO Implement (AccountDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAOImpl.java,v 1.1 2014/12/15 00:00:00 SimByungChul Express $
 */
@Repository("veryfyDAO")
public class VeryfyDAOImpl implements VeryfyDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private SqlSession sqlSession;

	public VeryfyDAOImpl() {
	}

	
	
	@Override
	public int getQosUpdate(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getQosUpdate", hm);
	}
	
	@Override
	public int getQosDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getQosDelete", hm);
	}
	
	@Override
	public int getQosSave(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getQosSave", hm);
	}
	
	
/*한서구*/	
	@Override
	public List<VeryfyVO> getVldTree(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getVldTree", hm);
	}
	@Override
	public List<VeryfyVO> getVld(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getVld", hm);
	}
	@Override
	public int getVldUpdate(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getVldUpdate", hm);
	}
	@Override
	public int QosDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.QosDelete", hm);
	}
	@Override
	public int QosInsert(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.QosInsert", hm);
	}
	@Override
	public int ConnectionDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.ConnectionDelete", hm);
	}
	@Override
	public int ConnectionInsert(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.ConnectionInsert", hm);
	}
	@Override
	public int VldSecurityDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.VldSecurityDelete", hm);
	}
	@Override
	public int VldSecurityInsert(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.VldSecurityInsert", hm);
	}
	@Override
	public List<VeryfyVO> getVeryfyList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getVeryfyList", hm);
	}
	@Override
	public List<VeryfyVO> getQosList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getQosList", hm);
	}
	@Override
	public List<VeryfyVO> getConnectionList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getConnectionList", hm);
	}
	@Override
	public List<VeryfyVO> getSecurityList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Veryfy.getSecurityList", hm);
	}
	@Override
	public int getSecurityUpdate(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getSecurityUpdate", hm);
	}
	@Override
	public int getSecurityDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getSecurityDelete", hm);
	}
	@Override
	public int getSecuritySave(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getSecuritySave", hm);
	}
	@Override
	public int getVersionInsert(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getVersionInsert", hm);
	}
	@Override
	public int getVldInsert(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getVldInsert", hm);
	}
	@Override
	public int getVldDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getVldDelete", hm);
	}
	@Override
	public int getVersionDelete(HashMap<String, Object> hm) {
		return sqlSession.update("Veryfy.getVersionDelete", hm);
	}
	/*한서구*/	
		

}