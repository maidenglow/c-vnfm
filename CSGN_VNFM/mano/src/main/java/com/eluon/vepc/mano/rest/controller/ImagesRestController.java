package com.eluon.vepc.mano.rest.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.service.CommonService;

@RestController
@RequestMapping("/images_back")
public class ImagesRestController {

	/** Logger */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CompositeConfiguration config;
	
	
	@Autowired 
	private CommonService comonService;

	/**
	 * Image List
	 * @method list
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<? extends Image> list() {

		OSClientV2 os = comonService.buildOSFactory(); 

		List<? extends Image> images = os.images().list();

		return images;
	}

	/**
	 * Create Image
	 * @method save
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Image save() throws MalformedURLException {
		
		OSClientV2 os = comonService.buildOSFactory(); 

		// payload
		Payload<URL> payload = Payloads.create(new URL("https://idn.irise.com/download/attachments/2556063/Simbank+CSR.zip?version=1&modificationDate=1337093877149"));
		
		// build image
		Image buildImage = Builders.image()
			.name("Cirros 0.3.0 x64")
			.isPublic(true)
			.containerFormat(ContainerFormat.BARE)
			.diskFormat(DiskFormat.QCOW2)
			.build();

		// created image
		Image image = os.images().create(buildImage, payload);

		return image;
	}

	/**
	 * Image
	 * @method get
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Image get(@PathVariable("id") String id) {
		
		OSClientV2 os = comonService.buildOSFactory(); 

		Image image = os.images().get(id);

		return image;
	}

	/**
	 * Image Delete
	 * @method delete
	 * @param id
	 * @return 
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ActionResponse delete(@PathVariable("id") String id) {
		logger.debug("delete!!!! id: {}", id);
		
		OSClientV2 os = comonService.buildOSFactory(); 

		return os.images().delete(id);
	}
}
