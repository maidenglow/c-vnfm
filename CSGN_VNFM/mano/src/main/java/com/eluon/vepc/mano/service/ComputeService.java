package com.eluon.vepc.mano.service;

import java.util.List;
import java.util.Map;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.model.compute.Limits;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.compute.VolumeAttachment;
import org.openstack4j.model.compute.ext.AvailabilityZone;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.springframework.http.ResponseEntity;

/**
 * ComputeService Interface(ComputeService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ComputeService.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
public interface ComputeService {
	public List<Flavor> listFlavors();
	public ResponseEntity<String> listFlavorsDetail(Map<String, String> allRequestParams);
	public Flavor getFlavor(String flavorId);
	public Flavor createFlavor(Flavor flavor);
	public Map<String, String> setProperty(String flavorId, Map<String, String> map);
	public Map<String, String> getListProperty(String flavorId);
	public Flavor updateFlavor(Flavor flavor);
	public ActionResponse deleteFlavor(String flavorId);
	public Map<String,String> listFlavorExtraSpecs(String flavorId);
	public Map<String,String> createAndUpdateFlavorExtraSpecs(String flavorId,Map<String,String> spec);
	public void deleteFlavorExtraSpecs(String flavorId,String key);
	public ResponseEntity<String> listFlavorAccess(String tenantId, String flavorId);
	public ResponseEntity<String> createFlavorAccess(String tenantName, String flavorId, String reqBody);
	public ResponseEntity<String> deleteFlavorAccess(String tenantName, String flavorId, String reqBody);
	public List<? extends Server> listServers();
	public Server getServer(String serverId);
	public Server createServer(ServerCreate server);
	public ActionResponse reBootServer(String serverId, RebootType Type);
	public ActionResponse deleteServer(String serverId);
	public ActionResponse actionServer(String serverId, Action action);
	public String createSnapshot(String serverId, String snapshotName);
	public Limits limit();
	public List<? extends Image> listImages();
	public Image getImage(String imageId);
	public List<? extends AvailabilityZone> listZones();

	//by jch add Zone api
	public HostAggregate getHostAggregate(String id);
	public List<? extends HostAggregate> listHostAggregate();
	public HostAggregate createZone(String zoneName, String availabilityZone);
	public HostAggregate addHost(String hostAggregateId, String host);
	public HostAggregate removeHost(String hostAggregateId, String host);
	public ActionResponse deleteZone(String id);

	public List<? extends Hypervisor> getHypervisors();
	public HypervisorStatistics getHypervisorStatistics();
	public VNCConsole getConsole(String serverId,VNCConsole.Type type);
	public VolumeAttachment attachVolume(String serverId, String volumeId, String device);
	public ResponseEntity<String> listOsServices();
	public List<String> listFloatingIpsPoolNames();
	public FloatingIP allocateFloatingIp(String poolName);
	public List<Keypair> listKeypairs();
	
}
