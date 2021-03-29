package com.eluon.vepc.mano.adapter;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.AttachInterfaceType;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.NetworkUpdate;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;
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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eluon.vepc.mano.service.CommonService;

/**
 * Network Adapter (NetworkAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkAdapter.java,v1.0 2015/03/30 00:00:00 SimByungChul Express $
 */
@Component
public class NetworkingAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private RestTemplate restTemplate;

	public NetworkingAdapter() {
	}

	public List<? extends Network> listNetworks() {
		return (List<? extends Network>) commonService.buildOSFactory().networking().network().list();
	}

	public Network getNetwork(String networkId) {
		return commonService.buildOSFactory().networking().network().get(networkId);
	}

	public Network createNetwork(Network buiidNetwork) {
		return commonService.buildOSFactory().networking().network().create(buiidNetwork);
	}

	public Network updateNetwork(String networkId, NetworkUpdate networkUpdate) {
		return commonService.buildOSFactory().networking().network().update(networkId, networkUpdate);
	}
	
	public ActionResponse deleteNetwork(String networkId) {
		return commonService.buildOSFactory().networking().network().delete(networkId);
	}

	public List<? extends Subnet> listSubnets() {
		return (List<? extends Subnet>) commonService.buildOSFactory().networking().subnet().list();
	}

	public Subnet getSubnet(String subnetId) {
		return commonService.buildOSFactory().networking().subnet().get(subnetId);
	}

	public Subnet createSubnet(Subnet buiidSubnet) {
		return commonService.buildOSFactory().networking().subnet().create(buiidSubnet);
	}

	public Subnet updateSubnet(NeutronSubnet neutronSubnet) {
		return commonService.buildOSFactory().networking().subnet().update(neutronSubnet);
	}
	
	public ActionResponse deleteSubnet(String subnetId) {
		return commonService.buildOSFactory().networking().subnet().delete(subnetId);
	}
	
	public List<? extends Port> listPorts() {
		return (List<? extends Port>) commonService.buildOSFactory().networking().port().list();
	}
	
	public Port getPort(String portId) {
		return commonService.buildOSFactory().networking().port().get(portId);
	}
	
	public Port createPort(Port buiidPort) {
		return commonService.buildOSFactory().networking().port().create(buiidPort);
	}
	
	public Port updatePort(NeutronPort neutronPort) {
		return commonService.buildOSFactory().networking().port().update(neutronPort);
	}
	
	public ActionResponse deletePort(String portId) {
		return commonService.buildOSFactory().networking().port().delete(portId);
	}
	public List<? extends Router> listRouters() {
		return (List<? extends Router>) commonService.buildOSFactory().networking().router().list();
	}
	
	public Router getRouter(String routerId) {
		return commonService.buildOSFactory().networking().router().get(routerId);
	}
	
	public Router createRouter(Router buiidRouter) {
		return commonService.buildOSFactory().networking().router().create(buiidRouter);
	}
	
	public Router updateRouter(Router router) {
		return commonService.buildOSFactory().networking().router().update(router);
	}
	
	public ActionResponse deleteRouter(String routerId) {

		return commonService.buildOSFactory().networking().router().delete(routerId);
	}
	
	public RouterInterface addRouterInterface(String routerId, String subnetId, String portId) {
		if(portId != null && "".equals(portId.trim()) == false){
			return commonService.buildOSFactory().networking().router().attachInterface(routerId, AttachInterfaceType.PORT, portId);
		}else{
			return commonService.buildOSFactory().networking().router().attachInterface(routerId, AttachInterfaceType.SUBNET, subnetId);
		}
	}
	
	public RouterInterface deleteRouterInterface(String routerId, String subnetId, String portId) {
		return commonService.buildOSFactory().networking().router().detachInterface(routerId, subnetId, portId);
	}
	
	
	public ResponseEntity<String> addGatewayRouter(String tokenId, String url, String routerId, String reqBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.PUT, reqEntity, String.class, routerId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}
	
	public ResponseEntity<String> deleteGatewayRouter(String tokenId, String url, String routerId, String reqBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.PUT, reqEntity, String.class, routerId);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
		return resEntity;
	}
	
	
	public ResponseEntity<String> updateSubnet(String tokenId, String url, String subnetId, String reqBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(reqBody, headers);

		try{
			ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.PUT, reqEntity, String.class, subnetId);
			loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
			loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
			return resEntity;
		} catch(HttpStatusCodeException e){
			ResponseEntity<String> resEntity = new ResponseEntity<String>(e.getResponseBodyAsString(),HttpStatus.OK);
			loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
			loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
			return resEntity;
		} catch(RestClientException e){
		}
		return null;
	}
	
	
	
	public List<? extends NetFloatingIP> listFloatingip(){
		return commonService.buildOSFactory().networking().floatingip().list();
	}
	
	
	public NetFloatingIP associateFloatingipToPort(String floatingIpId, String portId){
		return commonService.buildOSFactory().networking().floatingip().associateToPort(floatingIpId, portId);
	}
	
	public NetFloatingIP disassociateFloatingipToPort(String floatingIpId){
		return commonService.buildOSFactory().networking().floatingip().disassociateFromPort(floatingIpId);
	}
	
	public ResponseEntity<String> quotas(String tokenId,String url){
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>("", headers);

		try{
			ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class);
			loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
			loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
			return resEntity;
		} catch(HttpStatusCodeException e){
			ResponseEntity<String> resEntity = new ResponseEntity<String>(e.getResponseBodyAsString(),HttpStatus.OK);
			loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
			loggerTra.debug("HTTP_BODY = " + resEntity.getBody());
			return resEntity;
		} catch(RestClientException e){
		}
		return null;
	}
}