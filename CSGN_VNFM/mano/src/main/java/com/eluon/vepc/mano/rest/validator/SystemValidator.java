package com.eluon.vepc.mano.rest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.SystemService;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * Systems Validator (SystemsValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemsValidator,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
@Component
public class SystemValidator implements Validator {
	@Autowired
	private SystemService systemService;
	@Autowired
	private CommonService commonService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
	}

	public SystemVO validateSystem(SystemVO systemVO) {
		if (systemVO == null ||
			systemVO.getSystemId() == null 
			) return null;
		return systemVO;
	}


}