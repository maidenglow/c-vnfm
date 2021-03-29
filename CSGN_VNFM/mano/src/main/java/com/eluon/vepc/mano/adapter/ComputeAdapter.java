package com.eluon.vepc.mano.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.compute.VolumeAttachment;
import org.openstack4j.model.compute.ext.AvailabilityZone;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.compute.ext.HypervisorStatistics;
import org.openstack4j.model.network.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eluon.vepc.mano.service.CommonService;

/**
 * Compute Adapter (ComputeAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ComputeAdapter.java,v1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ComputeAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private RestTemplate restTemplate;

	public ComputeAdapter() {
	}

	@SuppressWarnings("unchecked")
	public List<Flavor> listFlavors() {
		return (List<Flavor>) commonService.buildOSFactory().compute().flavors().list();
	}
	
	public ResponseEntity<String> listFlavorsDetail(String tokenId, String url)  {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}

	
	public Server waitForServerStatus(String serverId, Status status, int maxWait, TimeUnit maxWaitUnit) {
		return commonService.buildOSFactory().compute().servers().waitForServerStatus(serverId, status, maxWait, maxWaitUnit);
	}
	
	public Flavor getFlavor(String flavorId) {
		return commonService.buildOSFactory().compute().flavors().get(flavorId);
	}

	public Flavor createFlavor(Flavor flavor) {
		return commonService.buildOSFactory().compute().flavors().create(flavor);
	}

	public Map<String, String> setProperty(String flavorId, Map<String, String> map) {
		return commonService.buildOSFactory().compute().flavors().createAndUpdateExtraSpecs(flavorId, map);
	}
	
	public Map<String, String> getListProperty(String flavorId) {
		return commonService.buildOSFactory().compute().flavors().listExtraSpecs(flavorId);
	}
	
	public ActionResponse deleteFlavor(String flavorId) {
		return commonService.buildOSFactory().compute().flavors().delete(flavorId);
	}

	public Map<String,String> listFlavorExtraSpecs(String flavorId){
		return commonService.buildOSFactory().compute().flavors().listExtraSpecs(flavorId);
	}
	
	public Map<String,String> createAndUpdateFlavorExtraSpecs(String flavorId,Map<String,String> spec){
		return commonService.buildOSFactory().compute().flavors().createAndUpdateExtraSpecs(flavorId, spec);
	}
	
	public void deleteFlavorExtraSpecs(String flavorId,String key){
		commonService.buildOSFactory().compute().flavors().deleteExtraSpecs(flavorId, key);
	}

	public ResponseEntity<String> listFlavorAccess(String tokenId, String url, String flavorId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class, flavorId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}

	public ResponseEntity<String> createFlavorAccess(String tokenId, String url, String flavorId, String reqBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.POST, reqEntity, String.class, flavorId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}

	public ResponseEntity<String> deleteFlavorAccess(String tokenId, String url, String flavorId, String reqBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.POST, reqEntity, String.class, flavorId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}

	public List<? extends Image> listImages() {
		return commonService.buildOSFactory().compute().images().list();
	}

	public Image getImage(String imageId) {
		return commonService.buildOSFactory().compute().images().get(imageId);
	}

	public ActionResponse deleteImage(String imageId) {
		return commonService.buildOSFactory().compute().images().delete(imageId);
	}
	
	public List<? extends Server> listServers() {
		return (List<? extends Server>) commonService.buildOSFactory().compute().servers().list();
	}

	public Server getServer(String serverId) {
		return commonService.buildOSFactory().compute().servers().get(serverId);
	}

	public Server createServer(ServerCreate sc) {
		return commonService.buildOSFactory().compute().servers().boot(sc);
	}
	
	public ActionResponse reBootServer(String serverId, RebootType Type) {
		return commonService.buildOSFactory().compute().servers().reboot(serverId, Type);
	}
	
	public ActionResponse deleteServer(String serverId) {
		return commonService.buildOSFactory().compute().servers().delete(serverId);
	}

	public ActionResponse actionServer(String serverId, Action action) {
		return commonService.buildOSFactory().compute().servers().action(serverId, action);
	}

	public String createSnapshot(String serverId, String snapshotName)	{
		return commonService.buildOSFactory().compute().servers().createSnapshot(serverId, snapshotName);
	}
	
	public Limits limit() {
		return commonService.buildOSFactory().compute().quotaSets().limits();
	}

	
	public List<? extends AvailabilityZone> listZones() {
		return commonService.buildOSFactory().compute().zones().list();
	}
	
	public List<? extends Hypervisor> listSystems(){
		
		return commonService.buildOSFactory().compute().hypervisors().list();
	}

	//by jch :: network 관련 추가
	
	public Network createNetwork(Network net) {
		
		return  commonService.buildOSFactory().networking().network().create(net);
		
	}

	
	public Network getNetwork(String name) {
		List<? extends Network> listNetwork = commonService.buildOSFactory().networking().network().list();
		for (int i = 0; i < listNetwork.size(); i++) {
			Network net = listNetwork.get(i);
			if(net.getName().equals(name)){
				return net;
			}
		}
		return null;
	}
	
	
	public List<? extends Network> listNetworks() {
		return commonService.buildOSFactory().networking().network().list();
	}
	
	//by jch :: Zone 관련 추가
	
	public HostAggregate getHostAggregate(String id) {
		return commonService.buildOSFactory().compute().hostAggregates().get(id);
	}
	
	public List<? extends HostAggregate> listHostAggregate() {
		return commonService.buildOSFactory().compute().hostAggregates().list();
	}

	
	public HostAggregate createZone(String zoneName, String availabilityZone) {
		return commonService.buildOSFactory().compute().hostAggregates().create(zoneName, availabilityZone);
	}
	
	public HostAggregate addHost(String hostAggregateId, String host) {
		return commonService.buildOSFactory().compute().hostAggregates().addHost(hostAggregateId, host);
	}
	
	public HostAggregate removeHost(String hostAggregateId, String host) {
		return commonService.buildOSFactory().compute().hostAggregates().removeHost(hostAggregateId, host);
	}
	
	public ActionResponse deleteZone(String id) {
		return commonService.buildOSFactory().compute().hostAggregates().delete(id);
	}
	
	
	public HypervisorStatistics getHypervisorStatistics() {
		//commonService.buildOSFactory() = > OSClient
		return commonService.buildOSFactory().compute().hypervisors().statistics();
	}
	
	public VolumeAttachment attachVolume(String serverId, String volumeId, String device) {
		return commonService.buildOSFactory().compute().servers().attachVolume(serverId, volumeId, device);
	}


	public ResponseEntity<String> listOsServices(String tokenId, String identityAdminUrl) {
			String listOsServices = "";
			CloseableHttpClient httpclient = HttpClients.createDefault();
			CloseableHttpResponse closeableHttpResponse = null;
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(config.getInt("REQ_READ_TIMEOUT"))
					.setConnectTimeout(config.getInt("REQ_CONN_TIMEOUT"))
					.setSocketTimeout(config.getInt("REQ_READ_TIMEOUT")).build();
			HttpGet httpGet = new HttpGet(identityAdminUrl);
			httpGet.setConfig(requestConfig);
			httpGet.addHeader("X-Auth-Token", tokenId);
			// commonService.setHttpGetRequestHeaders(headers, httpGet);
			loggerTra.debug("#################  MANO BEFORE #################");
			try {
				closeableHttpResponse = httpclient.execute(httpGet);
				loggerTra.debug("HTTP_RES = " + closeableHttpResponse.getStatusLine().getStatusCode());
				listOsServices = EntityUtils.toString(closeableHttpResponse.getEntity());
			} catch (IOException e) {
				loggerTra.debug("exception=" + e);
			} 
			loggerTra.debug("listOsServices=" + listOsServices);
			loggerTra.debug("#################  MANO AFTER ###################");
			return new ResponseEntity<String>(listOsServices, HttpStatus.OK);
	}
	
	
	public List<String> listFloatingIpsPoolNames(){
		return commonService.buildOSFactory().compute().floatingIps().getPoolNames();
	}
	
	public FloatingIP allocateFloatingIp(String poolName){
		return commonService.buildOSFactory().compute().floatingIps().allocateIP(poolName);
	}

	@SuppressWarnings("unchecked")
	public List<Keypair> listKeypairs() {
		return (List<Keypair>) commonService.buildOSFactory().compute().keypairs().list();
	}
	
	public List<? extends Hypervisor> hypervisors(){
		return commonService.buildOSFactory().compute().hypervisors().list();
	}
	
	public VNCConsole getConsole(String serverId,VNCConsole.Type type){
		return commonService.buildOSFactory().compute().servers().getVNCConsole(serverId, type);
	}
	
}