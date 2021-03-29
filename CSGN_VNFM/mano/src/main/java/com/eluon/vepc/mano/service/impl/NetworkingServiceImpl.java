package com.eluon.vepc.mano.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.Builders;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.network.IP;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.model.network.builder.RouterBuilder;
import org.openstack4j.openstack.networking.domain.NeutronNetwork;
import org.openstack4j.openstack.networking.domain.NeutronNetworkUpdate;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.openstack4j.openstack.networking.domain.NeutronRouter;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.NetworkingAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.IdentityService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.vo.NetQuotaVO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * NetworkServiceImpl (NetworkServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkServiceImpl.java,v 1.0 2015/03/30 00:00:00 SimByungChul Express $
 */
@Service("networkingService")
public class NetworkingServiceImpl implements NetworkingService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private NetworkingAdapter networkingAdapter;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;
	
	private ObjectMapper mapper = new ObjectMapper();

	public NetworkingServiceImpl() {
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
	    mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public List<? extends Network> listNetworks() {
		return networkingAdapter.listNetworks();
	}

	@Override
	public Network getNetwork(String networkId) {
		return networkingAdapter.getNetwork(networkId);
	}

	@Override
	public Network createNetwork(NeutronNetwork neutronNetwork) {

		Gson gson = new Gson();
		String neutronNetworkJson = gson.toJson(neutronNetwork,NeutronNetwork.class);
		JsonElement jsonElement = gson.fromJson(neutronNetworkJson, JsonElement.class);
		jsonElement.getAsJsonObject().addProperty("neutronSubnets", (String)null);
		
		NeutronNetwork network = gson.fromJson(jsonElement, NeutronNetwork.class);
		return networkingAdapter.createNetwork(network);
	}

	@Override
	public Network updateNetwork(String networkId, NeutronNetworkUpdate neutronNetworkUpdate) {
		return networkingAdapter.updateNetwork(networkId, neutronNetworkUpdate);
	}

	@Override
	public ActionResponse deleteNetwork(String networkId) {
		return networkingAdapter.deleteNetwork(networkId);
	}

	@Override
	public List<? extends Subnet> listSubnets() {
		return networkingAdapter.listSubnets();
	}

	@Override
	public Subnet getSubnet(String subnetId) {
		return networkingAdapter.getSubnet(subnetId);
	}

	@Override
	public Subnet createSubnet(NeutronSubnet neutronSubnet) {
//		Subnet buiidSubnet = Builders.subnet()
//				.name(neutronSubnet.getName())
//				.networkId(neutronSubnet.getNetworkId())
//				.tenantId(neutronSubnet.getTenantId())
//				.addPool(neutronSubnet.getAllocationPools().get(0).getStart(),neutronSubnet.getAllocationPools().get(0).getEnd() )
//				.ipVersion(neutronSubnet.getIpVersion())
//				.cidr(neutronSubnet.getCidr())
//				.build();
		return networkingAdapter.createSubnet(neutronSubnet);
	}

	@Override
	public ResponseEntity<String> updateSubnet(NeutronSubnet neutronSubnet) {
		
		
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.NETWORK.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.NETWORK.toString());

				String networkSubnetUri = endpoint.getPublicURL() + "/v2.0" + config.getString("NETWORK_SUBNET_URI");
				logger.debug("networkSubnetsUri="+networkSubnetUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				// convert request body to json
				Gson gson = new GsonBuilder().serializeNulls().create();
				JsonObject jsonObject = new JsonObject();
				JsonElement subnetElement = gson.toJsonTree(neutronSubnet, NeutronSubnet.class);
				JsonObject orgSubnetObject =  subnetElement.getAsJsonObject();
				JsonObject subnetObject = new JsonObject();
				subnetObject.addProperty("name", neutronSubnet.getName());
				subnetObject.add("dns_nameservers", orgSubnetObject.get("dnsNames"));
				subnetObject.add("host_routes", orgSubnetObject.get("hostRoutes"));
				if(neutronSubnet.getGateway()==null){
					subnetObject.add("gateway_ip", null);
				}else{
					subnetObject.addProperty("gateway_ip", neutronSubnet.getGateway());
				}
				subnetObject.addProperty("enable_dhcp", neutronSubnet.isDHCPEnabled());
				subnetObject.addProperty("name", neutronSubnet.getName());
				
				jsonObject.add("subnet", subnetObject);
				
				loggerTra.debug("########## " + jsonObject.toString());
				String reqBody = gson.toJson(jsonObject);
				
				loggerTra.debug("########## " + reqBody);
				
//				ResponseEntity<String> resp = networkingAdapter.updateSubnet(tokenId, networkSubnetUri, neutronSubnet.getId(), reqBody);
//				JsonObject resultObject = gson.fromJson(resp.getBody(), JsonObject.class);
//				
//				Subnet result = null;
//				
//				try {
//					result = mapper.readValue(resp.getBody(), new TypeReference<NeutronSubnet>() {});
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
						
				return networkingAdapter.updateSubnet(tokenId, networkSubnetUri, neutronSubnet.getId(), reqBody);
			}
		}
		return null;
		
		
		//return networkingAdapter.updateSubnet(neutronSubnet);
	}

	@Override
	public ActionResponse deleteSubnet(String subnetId) {
		return networkingAdapter.deleteSubnet(subnetId);
	}

	@Override
	public List<? extends Port> listPorts() {
		return networkingAdapter.listPorts();
	}

	@Override
	public Port getPort(String portId) {
		return networkingAdapter.getPort(portId);
	}

	@Override
	public Port createPort(NeutronPort neutronPort) {
		String ipAddress ="";
		String subnetId = "";
		Set<? extends IP> fixedIps = neutronPort.getFixedIps(); 
		for(IP fixedIp : fixedIps) {
		    if ( fixedIp != null) {
		    	ipAddress = fixedIp.getIpAddress();
		    	subnetId = fixedIp.getSubnetId();
		    }
		}
		Port buiidPort = Builders.port()
				.name(neutronPort.getName())
				.networkId(neutronPort.getNetworkId())
				.fixedIp(ipAddress, subnetId)
				.build();
		return networkingAdapter.createPort(buiidPort);
	}

	@Override
	public Port updatePort(NeutronPort neutronPort) {
		return networkingAdapter.updatePort(neutronPort);
	}

	@Override
	public ActionResponse deletePort(String portId) {
		return networkingAdapter.deletePort(portId);
	}
	
	@Override
	public List<? extends Router> listRouters() {
		return networkingAdapter.listRouters();
	}
	
	@Override
	public Router getRouter(String routerId) {
		return networkingAdapter.getRouter(routerId);
	}
	
	@Override
	public Router createRouter(NeutronRouter neutronRouter) {
		RouterBuilder builderRouter = Builders.router();
		builderRouter.name(neutronRouter.getName());
		builderRouter.adminStateUp(neutronRouter.isAdminStateUp());
		if(neutronRouter.getExternalGatewayInfo()!=null)
			builderRouter.externalGateway(neutronRouter.getExternalGatewayInfo().getNetworkId());
		if(neutronRouter.getRoutes()!=null)
			builderRouter.route(neutronRouter.getRoutes().get(0).getDestination(), neutronRouter.getRoutes().get(0).getNexthop());
		
		Router router = builderRouter.build();
		return networkingAdapter.createRouter(router);
	}
	
	@Override
	public Router updateRouter(NeutronRouter neutronRouter) {
		return networkingAdapter.updateRouter(neutronRouter);
	}
	
	@Override
	public ActionResponse deleteRouter(String routerId) {
		return networkingAdapter.deleteRouter(routerId);
	}
	
	@Override
	public ResponseEntity<String> addGatewayRouter(Router router) {
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.NETWORK.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.NETWORK.toString());

				String networkRouterGatewayUri = endpoint.getPublicURL() + "/v2.0" + config.getString("NETWORK_ROUTER_GATEWAY_URI");
				logger.debug("networkRouterGatewayUri="+networkRouterGatewayUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				JsonObject routerObject = new JsonObject();
				JsonObject gatewayObject = new JsonObject();
				gatewayObject.addProperty("network_id", router.getExternalGatewayInfo().getNetworkId());
				gatewayObject.addProperty("enable_snat", router.getExternalGatewayInfo().isEnableSnat());
				routerObject.add("external_gateway_info", gatewayObject);
				jsonObject.add("router", routerObject);
				String reqBody = gson.toJson(jsonObject);
				
				return networkingAdapter.addGatewayRouter(tokenId, networkRouterGatewayUri, router.getId(), reqBody);
			}
		}
		return null;
		
		
	}
	
	@Override
	public ResponseEntity<String> removeGatewayRouter(Router router) {

		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.NETWORK.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.NETWORK.toString());
				
				String networkRouterGatewayUri = endpoint.getPublicURL() + "/v2.0" + config.getString("NETWORK_ROUTER_GATEWAY_URI");
				logger.debug("networkRouterGatewayUri="+networkRouterGatewayUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				// convert request body to json
				Gson gson = new Gson();
				JsonObject jsonObject = new JsonObject();
				JsonObject routerObject = new JsonObject();
				JsonObject gatewayObject = new JsonObject();
				routerObject.add("external_gateway_info", gatewayObject);
				jsonObject.add("router", routerObject);
				String reqBody = gson.toJson(jsonObject);

				return networkingAdapter.deleteGatewayRouter(tokenId, networkRouterGatewayUri, router.getId(), reqBody);
			}
		}
		return null;
	}

	@Override
	public RouterInterface addRouterInterface(String routerId, Subnet subnet, String ipAddress) {
		if(ipAddress ==null || "".equals(ipAddress.trim())){
			return networkingAdapter.addRouterInterface(routerId, subnet.getId(), null);
		}
		
		Port neutronPort = NeutronPort.builder().networkId(subnet.getNetworkId()).fixedIp(ipAddress, subnet.getId()).build();
		Port port = createPort((NeutronPort)neutronPort);
		return networkingAdapter.addRouterInterface(routerId, subnet.getId(), port.getId());
	}

	@Override
	public RouterInterface removeRouterInterface(String routerId,
			String subnetId, String portId) {
		return networkingAdapter.deleteRouterInterface(routerId, subnetId, portId);
	}
	
	
	@Override
	public List<? extends NetFloatingIP> listFloatingip(){
		return networkingAdapter.listFloatingip();
	}
	
	@Override
	public NetFloatingIP associateFloatingip(String floatingIpId,String portId){
		return networkingAdapter.associateFloatingipToPort(floatingIpId, portId);
	}
	
	@Override
	public NetFloatingIP disassociateFloatingip(String floatingIpId){
		return networkingAdapter.disassociateFloatingipToPort(floatingIpId);
	}

	@Override
	public NetQuotaVO quotas(String tenantId){
		
		List<? extends Endpoint> listTokenEndpoints = identityService.listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.NETWORK.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.NETWORK.toString());
				
				String networkQuotasUri = endpoint.getPublicURL() + "/v2.0" + config.getString("NETWORK_QUOTAS_URI") + "/" + tenantId;
				logger.debug("networkQuotasUri="+networkQuotasUri);
				
				String tokenId = identityService.getToken().getId();
				logger.debug("tokenId="+tokenId);

				ResponseEntity<String> responseEntity =  networkingAdapter.quotas(tokenId,networkQuotasUri );

				if(responseEntity.getStatusCode() == HttpStatus.OK){
					try {
						return mapper.readValue(responseEntity.getBody(), NetQuotaVO.class);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					
				}
				
			}
		}
		return null;
		
	}
}