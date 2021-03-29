package com.eluon.vepc.mano.service;

import java.util.HashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.AccountGradeVO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.LoginVO;

/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public interface AccountService {
	public AccountVO findByUserIdAndPassword(LoginVO loginVO);
	public AccountVO getAccount(String userId);
	public List<AccountGradeVO> getAccountGradeList();
	public void addAccount(AccountVO accountVO);
	public void updateAccount(AccountVO accountVO);
	public int getAccountListTotalCount(HashMap<String, Object> hm);
	public List<AccountVO> getAccountList(HashMap<String, Object> hm);
	public void deleteAccount(String userId);
}
