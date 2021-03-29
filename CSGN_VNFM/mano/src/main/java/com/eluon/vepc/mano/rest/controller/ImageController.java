package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;
import org.openstack4j.model.image.StoreType;
import org.openstack4j.openstack.image.domain.GlanceImage;
import org.openstack4j.openstack.image.domain.GlanceImage.ImageConcreteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eluon.vepc.mano.rest.validator.ImageValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.VNFDescriptorVO;

/**
 * Images Controller (ImageController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImageController.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/image")
public class ImageController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private CompositeConfiguration config;

	@Autowired
	private VNFMService vnfmService;
	
	/**
	 * Image List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public List<? extends Image> listImages() {
		List<? extends Image> listImages = imageService.listImages();
		loggerTra.debug("listImages="+ listImages);
		return listImages;
	}

	/**
	 * @param imageId
	 * @return
	 */
	@RequestMapping(value = "/images/{imageId}", method = RequestMethod.GET)
	public Image getImage(@PathVariable("imageId") String imageId) {
		Image image = imageService.getImage(imageId);
		loggerTra.debug("image="+ image);
		return image;
	}

	/**
	 * @param glanceImage
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/images", method = RequestMethod.POST)
	public Image createImage(@RequestPart(value = "file") MultipartFile file,
			@RequestPart(required = false, value = "data") GlanceImage glanceImage) throws IOException {
		Payload<InputStream> payload = Payloads.create(file.getInputStream());
		Image image = imageService.createImage(glanceImage, payload);
		loggerTra.debug("image="+ image);
		return image;
	}
	
	@RequestMapping(value = "/imageUploads", method = RequestMethod.POST)
	public Image uploadImageFile(@RequestPart(value = "file") MultipartFile file,
			@RequestPart(required = false, value = "data") GlanceImage glanceImage) throws IOException {
		String fileName = file.getOriginalFilename();

		String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
		if(fileType.equals("img")){
			fileType = "iso";
		}else {
			fileType = DiskFormat.QCOW2.value();
		}
		
		ImageConcreteBuilder icb = (ImageConcreteBuilder) glanceImage.toBuilder();
		icb.diskFormat(DiskFormat.value(fileType));
		icb.containerFormat(ContainerFormat.BARE);
		icb.size(file.getSize());
		icb.minRam((long)0);
		icb.minDisk((long)2);
		icb.storeType(StoreType.FILE);
		glanceImage = (GlanceImage)icb.build();
		Payload<InputStream> payload = Payloads.create(file.getInputStream());

		Image image = imageService.uploadImage(glanceImage, payload);
		return image;
	}

	/**
	 * @param imageId
	 * @return
	 */
	@RequestMapping(value="/images/{imageId}", method = RequestMethod.PUT)
	public Image updateImage(@RequestBody GlanceImage glanceImage) {
		Image image = imageService.updateImage(glanceImage);
		loggerTra.debug("image="+ image);
		return image;
	}

	/**
	 * @param imageId
	 * @return
	 */
	@RequestMapping(value="/images/{imageId}", method = RequestMethod.DELETE)
	public Image deleteImage(@PathVariable("imageId") String imageId ) {
		
		Image image = imageService.getImage(imageId);
		String old_image_id = image.getId();
		
		imageService.deleteImage(imageId);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image.setId(null);
		image = imageService.createImage(image, null);
		VNFDescriptorVO vnfdInfoVO = new VNFDescriptorVO();
		vnfdInfoVO.old_image_id = old_image_id;
		vnfdInfoVO.image_id =image.getId();
		vnfmService.updateVnfd(vnfdInfoVO);
		
		
		return image;
	}

	/**
	 * @param imageId
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value="/images/{imageId}/members/{tenantId}", method = RequestMethod.PUT)
	public boolean createMember(@PathVariable("imageId") String imageId, @PathVariable("tenantId") String tenantId) {
		boolean res = imageService.createMember(imageId, tenantId);
		loggerTra.debug("res="+ res);
		return res;
	}
	
	/**
	 * @param imageId
	 * @param tenantId
	 * @return
	 */
	@RequestMapping(value="/images/{imageId}/members/{tenantId}", method = RequestMethod.DELETE)
	public boolean deleteMember(@PathVariable("imageId") String imageId, @PathVariable("tenantId") String tenantId) {
		boolean res = imageService.deleteMember(imageId, tenantId);
		loggerTra.debug("res="+ res);	
		return res;
	}
	
	/**
	 * @param imageId
	 * @return
	 */
	@RequestMapping(value="/images/{imageId}/members", method = RequestMethod.GET)
	public List<? extends ImageMember> listMembers(@PathVariable("imageId") String imageId) {
		List<? extends ImageMember> imageMember = imageService.listMembers(imageId);
		loggerTra.debug("imageMember="+ imageMember);
		return imageMember;
	}
	
}