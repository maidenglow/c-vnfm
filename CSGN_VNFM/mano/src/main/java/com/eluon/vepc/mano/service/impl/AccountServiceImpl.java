package com.eluon.vepc.mano.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.dao.AccountDAO;
import com.eluon.vepc.mano.service.AccountService;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.vo.AccountGradeVO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.LoginVO;

/**
 * AccountServiceImpl (AccountServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountServiceImpl.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private AccountDAO accountDAO;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public AccountServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}
	
	@Override
	public AccountVO findByUserIdAndPassword(LoginVO loginVO) {
		return accountDAO.findByUserIdAndPassword(loginVO);
	}

	@Override
	public AccountVO getAccount(String userId) {
		return accountDAO.getAccount(userId);
	}

	@Override
	public List<AccountGradeVO> getAccountGradeList() {
		return accountDAO.getAccountGradeList();
	}

	@Override
	public void addAccount(AccountVO accountVO) {
		if (accountVO.getGradeName() == null || accountVO.getGradeName().equals("")) accountVO.setGradeName(getGradeName(accountVO.getGradeCode()));
		accountDAO.addAccount(accountVO);
	}

	@Override
	public void updateAccount(AccountVO accountVO) {
		if (accountVO.getGradeName() == null || accountVO.getGradeName().equals("")) accountVO.setGradeName(getGradeName(accountVO.getGradeCode()));
		accountDAO.updateAccount(accountVO);
	}

	@Override
	public int getAccountListTotalCount(HashMap<String, Object> hm) {
		return accountDAO.getAccountListTotalCount(hm);
	}

	@Override
	public List<AccountVO> getAccountList(HashMap<String, Object> hm) {
		return accountDAO.getAccountList(hm);
	}

	@Override
	public void deleteAccount(String userId) {
		accountDAO.deleteAccount(userId);
	}
	
	public String getGradeName(String gradeCode) {
		return accountDAO.getGradeName(gradeCode);
	}

}
