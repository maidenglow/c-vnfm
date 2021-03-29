package com.eluon.vepc.mano.rest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.IdentityService;
import com.eluon.vepc.mano.vo.IdentityVO;

/**
 * Identity Validator (IdentityValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: IdentityValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class IdentityValidator implements Validator {
	@Autowired
	private IdentityService identityService;

	public boolean supports(Class<?> clazz) {
		return IdentityVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}