package com.eluon.vepc.mano.adapter;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eluon.vepc.mano.service.CommonService;

/**
 * Images Adapter (ImagesAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImagesAdapter.java,v1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ImageAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;

	public ImageAdapter() {
	}
	
	public List<? extends Image> listImages() {
		return commonService.buildOSFactory().images().list();
	}
	
	public Image getImage(String imageId) {
		return commonService.buildOSFactory().images().get(imageId);
	}

	public ActionResponse deleteImage(String imageId) {
		return commonService.buildOSFactory().images().delete(imageId);
	}

	public Image createImage(Image image, Payload<?> payload) {
		return commonService.buildOSFactory().images().create(image, payload);
	}

	public Image updateImage(Image image) {
		return commonService.buildOSFactory().images().update(image);
	}

	public Image uploadImage(Image image, Payload<?> payload) {		
		return commonService.buildOSFactory().images().upload(image.getId(), payload, image);
	}
	
	public boolean createMember(String imageId, String tenantId) {
		return commonService.buildOSFactory().images().addMember(imageId, tenantId);
	}

	public boolean deleteMember(String imageId, String tenantId) {
		return commonService.buildOSFactory().images().removeMember(imageId, tenantId);
	}

	public List<? extends ImageMember> listMembers(String imageId) {
		return commonService.buildOSFactory().images().listMembers(imageId);
	}
}