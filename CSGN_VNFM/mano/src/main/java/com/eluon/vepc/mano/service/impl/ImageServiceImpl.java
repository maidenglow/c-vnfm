package com.eluon.vepc.mano.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.ImageAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ImageService;

/**
 * ImageServiceImpl (ImageServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImageServiceImpl.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private ImageAdapter imageAdapter;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public ImageServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public List<? extends Image> listImages() {
		return imageAdapter.listImages();
	}

	@Override
	public Image getImage(String imageId) {
		return imageAdapter.getImage(imageId);
	}

	@Override
	public ActionResponse deleteImage(String imageId) {
		return imageAdapter.deleteImage(imageId);
	}

	@Override
	public Image createImage(Image image, Payload<?> payload) {
		Image buildImage = Builders.image()
			.name(image.getName())
			.properties(image.getProperties())
			.isPublic(image.isPublic())
//			.isProtected(image.isProtected())
			// TODO .containerFormat(ContainerFormat.BARE) 고정, 추후 동적반영 개선
			.containerFormat(ContainerFormat.BARE)
			.diskFormat(image.getDiskFormat())
			.build();
		return imageAdapter.createImage(buildImage, payload);
	}

	@Override
	public Image updateImage(Image image) {
		return imageAdapter.updateImage(image);
	}

	@Override
	public Image uploadImage(Image image, Payload<?> payload) {
		return imageAdapter.uploadImage(image, payload);
	}
	
	@Override
	public boolean createMember(String imageId, String tenantId) {
		return imageAdapter.createMember(imageId, tenantId);
	}

	@Override
	public boolean deleteMember(String imageId, String tenantId) {
		return imageAdapter.deleteMember(imageId, tenantId);
	}

	@Override
	public List<? extends ImageMember> listMembers(String imageId) {
		return imageAdapter.listMembers(imageId);
	}
	
}