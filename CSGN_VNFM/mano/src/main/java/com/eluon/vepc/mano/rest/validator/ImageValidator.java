package com.eluon.vepc.mano.rest.validator;

import org.openstack4j.model.image.Image;
import org.openstack4j.openstack.image.domain.GlanceImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ImageService;

/**
 * Images Validator (ImagesValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImagesValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ImageValidator implements Validator {
	@Autowired
	private ImageService imageService;
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

	public Image validateImage(GlanceImage glanceImage) {
		if (glanceImage == null ||
			glanceImage.getLocation() == null ||
			glanceImage.getName() == null ||
			glanceImage.getContainerFormat() == null ||
			glanceImage.getDiskFormat() == null
			) return null;
		return glanceImage;
	}


}