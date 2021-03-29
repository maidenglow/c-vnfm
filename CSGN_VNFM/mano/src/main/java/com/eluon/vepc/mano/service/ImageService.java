package com.eluon.vepc.mano.service;

import java.util.List;

import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;

/**
 * ImagesService Interface(ImagesService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImagesService.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
public interface ImageService {
	public List<? extends Image> listImages();
	public Image getImage(String imageId);
	public Image createImage(Image image, Payload<?> payload);
	public Image updateImage(Image image);
	public Image uploadImage(Image image, Payload<?> payload);
	public ActionResponse deleteImage(String imageId);
	public boolean createMember(String imageId, String tenantId);
	public boolean deleteMember(String imageId, String tenantId);
	public List<? extends ImageMember> listMembers(String imageId);
}
