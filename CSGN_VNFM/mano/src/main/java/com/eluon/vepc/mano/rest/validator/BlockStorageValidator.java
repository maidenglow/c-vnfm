package com.eluon.vepc.mano.rest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.BlockStorageService;
import com.eluon.vepc.mano.vo.BlockStorageVO;

/**
 * BlockStorage Validator (BlockStorageValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BlockStorageValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class BlockStorageValidator implements Validator {
	@Autowired
	private BlockStorageService blockStorageService;

	public boolean supports(Class<?> clazz) {
		return BlockStorageVO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}