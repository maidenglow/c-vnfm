package com.eluon.vepc.mano.service.impl;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.client.utils.URIBuilder;
import org.openstack4j.api.types.ServiceType;
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
import org.openstack4j.model.identity.v2.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.IdentityService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * ComputeServiceImpl (ComputeServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ComputeServiceImpl.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Service("computeService")
public class ComputeServiceImpl implements ComputeService {	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private ComputeAdapter computeAdapter;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;
	@Autowired
	private IdentityService identityService;

	public ComputeServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public List<Flavor> listFlavors() {
		return computeAdapter.listFlavors();
	}
	
	@Override
	public ResponseEntity<String> listFlavorsDetail(Map<String, String> allRequestParams) {
		List<? extends Endpoint> listTokenEndpoints =identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());

				String computeAdminUrl = endpoint.getAdminURL() + config.getString("COMPUTE_FLAVORS_DETAIL");
				try {
					URIBuilder uriBuilder = new URIBuilder(computeAdminUrl);
					for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
						uriBuilder.addParameter(entry.getKey(), entry.getValue());
					}
					computeAdminUrl = uriBuilder.build().toString();
				} catch (URISyntaxException e) {
					logger.error("URISyntaxException : " + e.getMessage());
					return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				logger.debug("computeAdminUrl="+computeAdminUrl);

				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				return computeAdapter.listFlavorsDetail(tokenId, computeAdminUrl);
			}
		}
		return null;
	}

	@Override
	public Flavor getFlavor(String flavorId) {
		return computeAdapter.getFlavor(flavorId);
	}

	@Override
	public Flavor createFlavor(Flavor flavor) {
		return computeAdapter.createFlavor(flavor);
	}
	@Override
	public Map<String, String> setProperty(String flavorId, Map<String, String> map) {
		return computeAdapter.setProperty(flavorId, map);
	}
	
	@Override
	public Map<String, String> getListProperty(String flavorId) {
		return computeAdapter.getListProperty(flavorId);
	}
	
	@Override
	public Flavor updateFlavor(Flavor flavor) {
		
		Map<String,String> flavorExtraSpecs = computeAdapter.listFlavorExtraSpecs(flavor.getId());
		computeAdapter.deleteFlavor(flavor.getId());
		Flavor newFlavor = computeAdapter.createFlavor(flavor);
		computeAdapter.createAndUpdateFlavorExtraSpecs(newFlavor.getId(),flavorExtraSpecs);
		return newFlavor;
	}

	@Override
	public ActionResponse deleteFlavor(String flavorId) {
		return computeAdapter.deleteFlavor(flavorId);
	}
	
	@Override
	public Map<String,String> listFlavorExtraSpecs(String flavorId){
		return computeAdapter.listFlavorExtraSpecs(flavorId);
	}
	
	@Override
	public Map<String,String> createAndUpdateFlavorExtraSpecs(String flavorId,Map<String,String> spec){
		return computeAdapter.createAndUpdateFlavorExtraSpecs(flavorId,spec);
	}
	
	@Override
	public void deleteFlavorExtraSpecs(String flavorId,String key){
		computeAdapter.deleteFlavorExtraSpecs(flavorId,key);
	}
	
	@Override
	public ResponseEntity<String> listFlavorAccess(String tenantId, String flavorId) {
		List<? extends Endpoint> listTokenEndpoints =identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());

				String computeAdminUrl = endpoint.getAdminURL() + config.getString("COMPUTE_FLAVORS_ACCESS_URI");
				logger.debug("computeAdminUrl="+computeAdminUrl);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				return computeAdapter.listFlavorAccess(tokenId, computeAdminUrl, flavorId);
			}
		}
		return null;
	}

	@Override
	public ResponseEntity<String> createFlavorAccess(String tenantName, String flavorId, String reqBody) {
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());

				String computeAdminUrl = endpoint.getPublicURL() + config.getString("COMPUTE_FLAVORS_ACTION_URI");
				logger.debug("computeAdminUrl="+computeAdminUrl);

				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);
				
				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				JsonElement jsonElement = gson.fromJson(reqBody, JsonElement.class);
				jsonObject.add("addTenantAccess", jsonElement);
				reqBody = gson.toJson(jsonObject);

				return computeAdapter.createFlavorAccess(tokenId, computeAdminUrl, flavorId, reqBody);
			}
		}
		return null;
	}

	@Override
	public ResponseEntity<String> deleteFlavorAccess(String tenantName, String flavorId, String reqBody) {
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());

				String computeAdminUrl = endpoint.getPublicURL() + config.getString("COMPUTE_FLAVORS_ACTION_URI");
				logger.debug("computeAdminUrl="+computeAdminUrl);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				JsonElement jsonElement = gson.fromJson(reqBody, JsonElement.class);
				jsonObject.add("removeTenantAccess", jsonElement);
				reqBody = gson.toJson(jsonObject);

				return computeAdapter.deleteFlavorAccess(tokenId, computeAdminUrl, flavorId, reqBody);
			}
		}
		return null;
	}
	
	@Override
	public List<? extends Image> listImages() {
		return computeAdapter.listImages();
	}
	
	@Override
	public Image getImage(String imageId) {
		return computeAdapter.getImage(imageId);
	}

	@Override
	public List<? extends Server> listServers() {
		return computeAdapter.listServers();
	}

	@Override
	public Server getServer(String serverId) {
		return computeAdapter.getServer(serverId);
	}

	@Override
	public Server createServer(ServerCreate server) {
		return computeAdapter.createServer(server);
	}
	
	@Override
	public ActionResponse reBootServer(String serverId, RebootType Type) {
		return computeAdapter.reBootServer(serverId, Type);
	}
	
	@Override
	public ActionResponse deleteServer(String serverId) {
		return computeAdapter.deleteServer(serverId);
	}

	@Override
	public ActionResponse actionServer(String serverId, Action action) {
		return computeAdapter.actionServer(serverId, action);
	}
	
	public String createSnapshot(String serverId, String snapshotName)	{
		return computeAdapter.createSnapshot(serverId, snapshotName);
	}
	
	@Override
	public Limits limit() {
		return computeAdapter.limit();
	}

	@Override
	public List<? extends AvailabilityZone> listZones() {
		return computeAdapter.listZones();
	}
	
	@Override
	public HostAggregate getHostAggregate(String id) {
		return computeAdapter.getHostAggregate(id);
	}
	
	@Override
	public List<? extends HostAggregate> listHostAggregate() {
		return computeAdapter.listHostAggregate();
	}
	
	@Override
	public HostAggregate createZone(String zoneName, String availabilityZone){
		return computeAdapter.createZone(zoneName, availabilityZone);
	}
	
	@Override
	public HostAggregate addHost(String hostAggregateId, String host){
		return computeAdapter.addHost(hostAggregateId, host);
	}
	
	@Override
	public HostAggregate removeHost(String hostAggregateId, String host){
		return computeAdapter.removeHost(hostAggregateId, host);
	}
	
	@Override
	public ActionResponse deleteZone(String id){
		return computeAdapter.deleteZone(id);
	}
	@Override
	public VNCConsole getConsole(String serverId,VNCConsole.Type type){
		return computeAdapter.getConsole(serverId, type);
	}
	
	@Override
	public List<? extends Hypervisor> getHypervisors() {
		return computeAdapter.hypervisors();
	}
	
	@Override
	public HypervisorStatistics getHypervisorStatistics() {
		return computeAdapter.getHypervisorStatistics();
	}
	
	@Override
	public VolumeAttachment attachVolume(String serverId, String volumeId, String device) {
		return computeAdapter.attachVolume(serverId, volumeId, device);
	}
	
	@Override
	public ResponseEntity<String> listOsServices() {
		String computeAdminUrl = "";
		String tokenId = identityService.getToken().getId();
		List<? extends Endpoint> listTokenEndpoints =identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.COMPUTE.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.COMPUTE.toString());
				computeAdminUrl = endpoint.getAdminURL() + config.getString("COMPUTE_SERVICE_LIST_URI");
				logger.debug("computeAdminUrl="+computeAdminUrl);
				logger.debug("tokenId="+tokenId);
				return computeAdapter.listOsServices(tokenId, computeAdminUrl);
			}
		}
		return null;
	}
	
	@Override
	public List<String> listFloatingIpsPoolNames(){
		return computeAdapter.listFloatingIpsPoolNames();
	}
	
	@Override
	public FloatingIP allocateFloatingIp(String poolName){
		return computeAdapter.allocateFloatingIp(poolName);
	}

	@Override
	public List<Keypair> listKeypairs() {
		return computeAdapter.listKeypairs();
	}
}