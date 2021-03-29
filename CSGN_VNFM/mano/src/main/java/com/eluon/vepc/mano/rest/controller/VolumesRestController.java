package com.eluon.vepc.mano.rest.controller;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.service.CommonService;

@RestController
@RequestMapping("/volumes")
public class VolumesRestController {
	
	/** Logger */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CompositeConfiguration config;
	
	@Autowired
	private CommonService commonService;
	
	
	@RequestMapping(value = "/list.do")
	public List<? extends Volume> list() {

		OSClientV2 os = commonService.buildOSFactory(); 
		
		// Volums
		List<? extends Volume> volumes = os.blockStorage().volumes().list();
		logger.debug("volumes : {}", volumes);
		logger.debug("volumes size : {}", volumes.size());

		// Servers
		List<? extends Server> servers = os.compute().servers().list();
		logger.debug("servers : {}", servers);
		logger.debug("servers size: {}", servers.size());
		
		for (Volume volume : volumes) {
			logger.debug("volume : {}", volume);

			for (VolumeAttachment att : volume.getAttachments()) {
				logger.debug("att : {}", att);

				String serverId = att.getServerId();
				logger.debug("server id : {}", serverId);
			}
		}

		return volumes;
	}
}
