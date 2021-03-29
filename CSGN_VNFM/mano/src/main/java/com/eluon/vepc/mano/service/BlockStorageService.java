package com.eluon.vepc.mano.service;

import java.util.List;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.BlockLimits;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.openstack.storage.block.domain.CinderVolume;
import org.springframework.http.ResponseEntity;

/**
 * BlockStorageService Interface(BlockStorageService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BlockStorageService.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public interface BlockStorageService {
	public ResponseEntity<String> listVolumes(String tenantId);
	public ResponseEntity<String> getVolume(String volumeId);
	public Volume createVolume(CinderVolume cinderVolume);
	public ActionResponse updateVolume(CinderVolume cinderVolume);
	public ResponseEntity<String> extendVolumeSize(String tenantId, CinderVolume cinderVolume);
	
	public ActionResponse deleteVolume(String volumeId);
	
	public VolumeSnapshot createSnapshot(VolumeSnapshot snapshot);
	public List<? extends VolumeType> listVolumeTypes();
	public BlockLimits limit();
}
