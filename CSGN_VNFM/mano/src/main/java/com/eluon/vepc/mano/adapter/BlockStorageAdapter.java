package com.eluon.vepc.mano.adapter;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.BlockLimits;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.openstack.storage.block.domain.CinderVolume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eluon.vepc.mano.service.CommonService;

/**
 * BlockStorage Adapter (BlockStorageAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BlockStorageAdapter.java,v1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class BlockStorageAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private RestTemplate restTemplate;

	public BlockStorageAdapter() {
	}

	public ResponseEntity<String> listVolumes(String tokenId,String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>("", headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
		
		//return (List<? extends Volume>) commonService.buildOSFactory().blockStorage().volumes().list();
	}

	public ResponseEntity<String> getVolume(String tokenId,String url,String volumeId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>("", headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class,volumeId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}

	
	public Volume getVolume(String volumeId) {
		return commonService.buildOSFactory().blockStorage().volumes().get(volumeId);
	}

	public Volume createVolume(Volume buiidVolume) {
		return commonService.buildOSFactory().blockStorage().volumes().create(buiidVolume);
	}

	public ActionResponse updateVolume(CinderVolume cinderVolume) {
		return commonService.buildOSFactory().blockStorage().volumes().update(cinderVolume.getId(), cinderVolume.getName(), cinderVolume.getDescription());
	}

	public ActionResponse deleteVolume(String volumeId) {
		return commonService.buildOSFactory().blockStorage().volumes().delete(volumeId);
	}
	
	public ResponseEntity<String> extendVolumeSize(String tokenId, String url,String volumeId,String reqBody){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.POST, reqEntity, String.class,volumeId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}
	
	public VolumeSnapshot createSnapshot(VolumeSnapshot snapshot) {
		return commonService.buildOSFactory().blockStorage().snapshots().create(snapshot);
	}
	
	public List<? extends VolumeType> listVolumeTypes() {
		return commonService.buildOSFactory().blockStorage().volumes().listVolumeTypes();
	}
	
	public BlockLimits limit() {
		return commonService.buildOSFactory().blockStorage().getLimits();
	}
}