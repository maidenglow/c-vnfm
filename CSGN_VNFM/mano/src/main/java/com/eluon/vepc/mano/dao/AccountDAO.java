package com.eluon.vepc.mano.dao;

import java.util.HashMap;
import java.util.List;

import com.eluon.vepc.mano.vo.AccountGradeVO;
import com.eluon.vepc.mano.vo.AccountVO;
import com.eluon.vepc.mano.vo.LoginVO;

/**
 * AccountDAO Interface (AccountDAO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountDAO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express
 *          $
 */
public interface AccountDAO {
	public AccountVO findByUserIdAndPassword(LoginVO loginVO);
	public AccountVO getAccount(String userId);
	public void addAccount(AccountVO accountVO);
	public int updateAccount(AccountVO accountVO);
	public int getAccountListTotalCount(HashMap<String, Object> hm);
	public List<AccountVO> getAccountList(HashMap<String, Object> hm);
	public void deleteAccount(String userId);
	public List<AccountGradeVO> getAccountGradeList();
	public String getGradeName(String gradeCode);
}
