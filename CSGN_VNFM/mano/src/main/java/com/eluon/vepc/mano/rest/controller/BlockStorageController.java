package com.eluon.vepc.mano.rest.controller;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.BlockLimits;
import org.openstack4j.model.storage.block.BlockLimits.Absolute;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.openstack.storage.block.domain.CinderVolume;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.BlockStorageValidator;
import com.eluon.vepc.mano.service.BlockStorageService;
import com.eluon.vepc.mano.service.CommonService;

/**
 * BlockStorages Controller (BlockStorageController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BlockStorageController.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/blockStorage")
public class BlockStorageController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	@Autowired
	private BlockStorageService blockStorageService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private BlockStorageValidator blockStorageValidator;

	/**
	 * BlockStorage List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/volumes", method = RequestMethod.GET)
	public ResponseEntity<String> listVolumes(@PathVariable("tenantId") String tenantId) {
		ResponseEntity<String> listVolumes = blockStorageService.listVolumes(tenantId);
		loggerTra.debug("listVolumes="+ listVolumes);
		return listVolumes;
	}

	/**
	 * @param volumeId
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/volumes/{volumeId}", method = RequestMethod.GET)
	public ResponseEntity<String> getVolume(@PathVariable("volumeId") String volumeId) {
		ResponseEntity<String> volume = blockStorageService.getVolume(volumeId);
		loggerTra.debug("volume="+ volume);
		return volume;
	}

	/**
	 * @param cinderVolume
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/volumes", method = RequestMethod.POST)
	public Volume createVolume(@RequestBody CinderVolume cinderVolume) {
		Volume volume = blockStorageService.createVolume(cinderVolume);
		loggerTra.debug("volume="+ volume);
		return volume;
	}

	/**
	 * @param cinderVolume
	 * @return
	 */
	@RequestMapping(value="/{tenantId}/volumes/{volumeId}", method = RequestMethod.PUT)
	public ActionResponse updateVolume(@RequestBody CinderVolume cinderVolume) {
		ActionResponse actionResponse = blockStorageService.updateVolume(cinderVolume);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}
	
	/**
	 * @param cinderVolume
	 * @return
	 */
	@RequestMapping(value="/{tenantId}/volumes/{volumeId}/extend", method = RequestMethod.POST)
	public ResponseEntity<String>  extendVolumeSize(@PathVariable("tenantId") String tenantId, @RequestBody CinderVolume cinderVolume) {
		try{
			ResponseEntity<String>  responseEntity = blockStorageService.extendVolumeSize(tenantId, cinderVolume);
			loggerTra.debug("responseEntity="+ responseEntity.toString());
			
			return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
		}catch(Exception e){
			loggerTra.warn(e.getMessage() ,e);
			
			return new ResponseEntity<String>("{\"success\" : false}", HttpStatus.OK);
		}
	}

	/**
	 * @param volumeId
	 * @return
	 */
	@RequestMapping(value="/{tenantId}/volumes/{volumeId}", method = RequestMethod.DELETE)
	public ActionResponse deleteVolume(@PathVariable("volumeId") String volumeId ) {
		ActionResponse actionResponse = blockStorageService.deleteVolume(volumeId);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}
	
	/**
	 * BlockStorage limits
	 * @method limits
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/limits", method = RequestMethod.GET)
	public Absolute limits() {
		BlockLimits limits = blockStorageService.limit();
		loggerTra.debug("limits : " + limits);
		if(limits == null){
			return null;
		}
		loggerTra.debug("limits absolute : " + limits.getAbsolute());
		return limits.getAbsolute();
	}
	
	
	/**
	 * BlockStorage Volume Types
	 * @method listTypes
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/volume-types", method = RequestMethod.GET)
	public List<? extends VolumeType> listVolumeTypes() {
		List<? extends VolumeType> types = blockStorageService.listVolumeTypes();
		loggerTra.debug("volume types: " + types);
		loggerTra.debug("volume types size : " + types.size());
		return types;
	}
	
	/**
	 * @param snapshot
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/snapshots", method = RequestMethod.POST)
	public VolumeSnapshot createSnapshot(@RequestBody CinderVolumeSnapshot snapshot) {
		return blockStorageService.createSnapshot(snapshot);
	}

}