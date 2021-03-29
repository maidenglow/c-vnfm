package com.eluon.vepc.mano.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.AccountService;
import com.eluon.vepc.mano.vo.AccountVO;

/**
 * Account Validator (AccountValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountValidator,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Component
public class AccountValidator implements Validator {
	@Autowired
	private AccountService accountService;

	public boolean supports(Class<?> clazz) {
		return AccountVO.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		AccountVO formLogin = (AccountVO)target;
//		Loigin loginVO = this.loginService.findAccount(formAccount.getUserName());
//		if (loginVO != null && loginVO.getUserId() != formAccount.getUserId()) errors.rejectValue("loginName", "duplicateUserName");
	}

}