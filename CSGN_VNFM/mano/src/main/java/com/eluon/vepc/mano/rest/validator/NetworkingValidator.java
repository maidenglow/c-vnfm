package com.eluon.vepc.mano.rest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.vo.NetworkingVO;

/**
 * Network Validator (NetworkValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class NetworkingValidator implements Validator {
	@Autowired
	private NetworkingService networkingService;

	public boolean supports(Class<?> clazz) {
		return NetworkingVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}