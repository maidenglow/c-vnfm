package com.eluon.vepc.mano.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.storage.block.BlockLimits;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.openstack.storage.block.domain.CinderVolume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.eluon.vepc.mano.adapter.BlockStorageAdapter;
import com.eluon.vepc.mano.service.BlockStorageService;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.IdentityService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * BlockStorageServiceImpl (BlockStorageServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BlockStorageServiceImpl.java,v 1.0 2015/03/13 00:00:00 SimByungChul Express $
 */
@Service("blockStorageService")
public class BlockStorageServiceImpl implements BlockStorageService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private BlockStorageAdapter blockStorageAdapter;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public BlockStorageServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public ResponseEntity<String> listVolumes(String tenantId) {

		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			
			if (endpoint.getType().toUpperCase().equals(ServiceType.BLOCK_STORAGE.getType().toString().toUpperCase())){
				
				String volumeListUri = endpoint.getPublicURL() + config.getString("VOLUME_LIST_URI");
				logger.debug("volumeListUri="+volumeListUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				ResponseEntity<String> response = blockStorageAdapter.listVolumes(tokenId,volumeListUri);
				String body = response.getBody();
				Gson gson = new Gson();
				JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
				
				return new ResponseEntity<String>(gson.toJson(jsonObject.getAsJsonArray("volumes")), response.getStatusCode());
			}
		}
		return null;
	}

	@Override
	public ResponseEntity<String> getVolume(String volumeId) {
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			
			if (endpoint.getType().toUpperCase().equals(ServiceType.BLOCK_STORAGE.getType().toString().toUpperCase())){
				
				String volumeInfoUri = endpoint.getPublicURL() + config.getString("VOLUME_INFO_URI");
				logger.debug("volumeInfoUri="+volumeInfoUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);
				
				try{
					ResponseEntity<String> response = blockStorageAdapter.getVolume(tokenId,volumeInfoUri,volumeId);
					String body = response.getBody();
					Gson gson = new Gson();
					JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
					
					return new ResponseEntity<String>(gson.toJson(jsonObject.getAsJsonObject("volume")), response.getStatusCode());
				}catch(HttpClientErrorException e){
					if(e.getStatusCode() == HttpStatus.NOT_FOUND){
						return new ResponseEntity<String>("", HttpStatus.OK);
					}
				}
				
				
			}
		}
		return null;
		
		//return blockStorageAdapter.getVolume(volumeId);
	}

	@Override
	public Volume createVolume(CinderVolume cinderVolume) {
		Volume buiidVolume = Builders.volume()
				.name(cinderVolume.getName())
				.description(cinderVolume.getDescription())
				.size(cinderVolume.getSize())
				.build();
		return blockStorageAdapter.createVolume(buiidVolume);
	}

	@Override
	public ActionResponse updateVolume(CinderVolume cinderVolume) {
		return blockStorageAdapter.updateVolume(cinderVolume);
	}
	
	@Override
	public ResponseEntity<String> extendVolumeSize(String tenantId, CinderVolume cinderVolume){
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			
			if (endpoint.getType().toUpperCase().equals(ServiceType.BLOCK_STORAGE.getType().toString().toUpperCase())){
				
				String volumeActionUri = endpoint.getPublicURL() + config.getString("VOLUME_ACTION_URI");
				logger.debug("volumeActionUri="+volumeActionUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);
				
				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				
				JsonObject actionParam = new JsonObject();
				actionParam.addProperty("new_size", cinderVolume.getSize());
				jsonObject.add("os-extend", actionParam);
				String reqBody = gson.toJson(jsonObject);
				
				return blockStorageAdapter.extendVolumeSize(tokenId,volumeActionUri,cinderVolume.getId(), reqBody);
			}
		}
		return null;
	}

	@Override
	public ActionResponse deleteVolume(String volumeId) {
		return blockStorageAdapter.deleteVolume(volumeId);
	}


	@Override
	public List<? extends VolumeType> listVolumeTypes() {
		return blockStorageAdapter.listVolumeTypes();
	}

	@Override
	public VolumeSnapshot createSnapshot(VolumeSnapshot snapshot) {
		return blockStorageAdapter.createSnapshot(snapshot);
	}

	@Override
	public BlockLimits limit() {
		return blockStorageAdapter.limit();
	}
}
