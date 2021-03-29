package com.eluon.vepc.mano.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.AccountDAO;
import com.eluon.vepc.mano.vo.AccountGradeVO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.LoginVO;

/**
 * AccountDAO Implement (AccountDAOImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAOImpl.java,v 1.1 2014/12/15 00:00:00 SimByungChul Express $
 */
@Repository("accountDAO")
public class AccountDAOImpl implements AccountDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private SqlSession sqlSession;

	public AccountDAOImpl() {
	}

	@Override
	public AccountVO findByUserIdAndPassword(LoginVO loginVO) {
		return (AccountVO) sqlSession.selectOne("Account.findByUserIdAndPassword", loginVO);
	}

	@Override
	public AccountVO getAccount(String userId) {
		return (AccountVO) sqlSession.selectOne("Account.getAccount", userId);
	}

	@Override
	public void addAccount(AccountVO accountVO) {
		sqlSession.insert("Account.addAccount", accountVO);
	}

	@Override
	public int updateAccount(AccountVO accountVO) {
		return sqlSession.update("Account.updateAccount", accountVO);
	}

	@Override
	public int getAccountListTotalCount(HashMap<String, Object> hm) {
		String count = (String)sqlSession.selectOne("Account.getAccountListTotalCount", hm);
		return Integer.parseInt(count);
	}

	@Override
	public List<AccountVO> getAccountList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Account.getAccountList", hm);
	}

	@Override
	public void deleteAccount(String userId) {
		sqlSession.update("Account.deleteAccount", userId);
	}

	@Override
	public List<AccountGradeVO> getAccountGradeList() {
		return sqlSession.selectList("Account.getAccountGradeList");
	}

	@Override
	public String getGradeName(String gradeCode) {
		return sqlSession.selectOne("Account.getGradeName", gradeCode);
	}

}