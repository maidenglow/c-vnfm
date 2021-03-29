package com.eluon.vepc.mano.rest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.ObjectStorageService;
import com.eluon.vepc.mano.vo.ObjectStorageVO;

/**
 * ObjectStorage Validator (ObjectStorageValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ObjectStorageValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ObjectStorageValidator implements Validator {
	@Autowired
	private ObjectStorageService objectStorageService;

	public boolean supports(Class<?> clazz) {
		return ObjectStorageVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}