package com.eluon.vepc.mano.rest.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.networking.domain.NeutronFloatingIP;
import org.openstack4j.openstack.networking.domain.NeutronNetwork;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.openstack4j.openstack.networking.domain.NeutronRouter;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.NetworkingValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.NetworkingService;
import com.eluon.vepc.mano.vo.CommonRespVO;
import com.eluon.vepc.mano.vo.NetQuotaVO;
import com.eluon.vepc.mano.vo.NeutronNetworkUpdateExt;
import com.eluon.vepc.mano.vo.NeutronSubnetExt;
import com.eluon.vepc.mano.vo.RouterInterfaceVO;

/**
 * NetworkingService Controller (NetworkingController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkingController.java,v 1.0 2015/03/30 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/networking")
public class NetworkingController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");

	@Autowired
	private NetworkingService networkingService;
	@Autowired
	private ComputeService computeService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private NetworkingValidator networkingValidator;

	/**
	 * @return
	 */
	@RequestMapping(value = "/networks", method = RequestMethod.GET)
	public List<? extends Network> listNetworks() {
		List<? extends Network> listNetworks = networkingService.listNetworks();
		loggerTra.debug("listNetworks="+ listNetworks);
		return listNetworks;
	}

	/**
	 * @param networkId
	 * @return
	 */
	@RequestMapping(value = "/networks/{networkId}", method = RequestMethod.GET)
	public Network getNetwork(@PathVariable("networkId") String networkId) {
		Network network = networkingService.getNetwork(networkId);
		loggerTra.debug("network="+ network);
		return network;
	}

	/**
	 * @param neutronNetwork
	 * @return
	 */
	@RequestMapping(value = "/networks", method = RequestMethod.POST)
	public CommonRespVO<Network> createNetwork(@RequestBody NeutronNetwork neutronNetwork) {
		loggerTra.debug("neutronNetwork="+ neutronNetwork);
		CommonRespVO<Network> result = null;
		Network network = null;
		try{
			network = networkingService.createNetwork(neutronNetwork);

			List<? extends Subnet> subnets = neutronNetwork.getNeutronSubnets();
			if(subnets !=null && subnets.size() > 0){
				Subnet subnet = subnets.get(0);
				if (subnet instanceof NeutronSubnet) {
					loggerTra.debug("subnet="+ subnet);
					subnet = subnet.toBuilder().network(network).build();
					loggerTra.debug("neutronSubnet="+ subnet);
					networkingService.createSubnet((NeutronSubnet)subnet);
				}
			}
			loggerTra.debug("network="+ network);
			result = new CommonRespVO<Network>(true,null,network);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			if(network != null){
				networkingService.deleteNetwork(network.getId());
			}
			result = new CommonRespVO<Network>(false,e.getMessage(),null);
		}

		return result;
	}

	/**
	 * @param networkId
	 * @param neutronNetworkUpdate
	 * @return
	 */
	@RequestMapping(value="/networks/{networkId}", method = RequestMethod.PUT)
	public Network updateNetwork(@PathVariable("networkId") String networkId, @RequestBody NeutronNetworkUpdateExt neutronNetworkUpdate) {
		loggerTra.debug("NeutronNetworkUpdate="+ neutronNetworkUpdate);
		Network network = networkingService.updateNetwork(networkId, neutronNetworkUpdate);
		loggerTra.debug("network="+ network);
		return network;
	}

	/**
	 * @param networkId
	 * @return
	 */
	@RequestMapping(value="/networks/{networkId}", method = RequestMethod.DELETE)
	public ActionResponse deleteNetwork(@PathVariable("networkId") String networkId ) {
		ActionResponse actionResponse = networkingService.deleteNetwork(networkId);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/subnets", method = RequestMethod.GET)
	public List<? extends Subnet> listSubnets() {
		List<? extends Subnet> listSubnets = networkingService.listSubnets();
		loggerTra.debug("listSubnets="+ listSubnets);
		return listSubnets;
	}

	/**
	 * @param subnetId
	 * @return
	 */
	@RequestMapping(value = "/subnets/{subnetId}", method = RequestMethod.GET)
	public Subnet getSubnet(@PathVariable("subnetId") String subnetId) {
		Subnet subnet = networkingService.getSubnet(subnetId);
		loggerTra.debug("subnet="+ subnet);
		return subnet;
	}

	/**
	 * @param neutronSubnet
	 * @return
	 */
	@RequestMapping(value = "/subnets", method = RequestMethod.POST)
	public CommonRespVO<Subnet> createSubnet(@RequestBody NeutronSubnetExt neutronSubnet) {
		CommonRespVO<Subnet> result = null;
		try{
			Subnet subnet = networkingService.createSubnet(neutronSubnet);
			loggerTra.debug("subnet="+ subnet);
			result = new CommonRespVO<Subnet>(true,null,subnet);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			result = new CommonRespVO<Subnet>(false,e.getMessage(),null);
		}
		return result;
	}

	/**
	 * @param neutronSubnet
	 * @return
	 */
	@RequestMapping(value="/subnets/{subnetId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateSubnet(@RequestBody NeutronSubnetExt neutronSubnet) {
		ResponseEntity<String> subnet = networkingService.updateSubnet(neutronSubnet);
		loggerTra.debug("subnet="+ subnet);
		return subnet;
	}

	/**
	 * @param subnetId
	 * @return
	 */
	@RequestMapping(value="/subnets/{subnetId}", method = RequestMethod.DELETE)
	public ActionResponse deleteSubnet(@PathVariable("subnetId") String subnetId ) {
		ActionResponse actionResponse = networkingService.deleteSubnet(subnetId);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ports", method = RequestMethod.GET)
	public List<? extends Port> listPorts() {
		List<? extends Port> listPorts = networkingService.listPorts();
		loggerTra.debug("listPorts="+ listPorts);
		return listPorts;
	}

	/**
	 * @param portId
	 * @return
	 */
	@RequestMapping(value = "/ports/{portId}", method = RequestMethod.GET)
	public Port getPort(@PathVariable("portId") String portId) {
		Port port = networkingService.getPort(portId);
		loggerTra.debug("port="+ port);
		return port;
	}

	/**
	 * @param neutronPort
	 * @return
	 */
	@RequestMapping(value = "/ports/", method = RequestMethod.POST)
	public Port createPort(@RequestBody NeutronPort neutronPort) {
		Port port = networkingService.createPort(neutronPort);
		loggerTra.debug("port="+ port);
		return port;
	}

	/**
	 * @param neutronPort
	 * @return
	 */
	@RequestMapping(value="/ports/{portId}", method = RequestMethod.PUT)
	public Port updatePort(@RequestBody NeutronPort neutronPort) {
		Port port = networkingService.updatePort(neutronPort);
		loggerTra.debug("port="+ port);
		return port;
	}

	/**
	 * @param portId
	 * @return
	 */
	@RequestMapping(value="/ports/{portId}", method = RequestMethod.DELETE)
	public ActionResponse deletePort(@PathVariable("portId") String portId ) {
		ActionResponse actionResponse = networkingService.deletePort(portId);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/routers", method = RequestMethod.GET)
	public List<? extends Router> listRouters() {
		List<? extends Router> listRouters = networkingService.listRouters();
		loggerTra.debug("listRouters="+ listRouters);
		return listRouters;
	}

	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value = "/routers/{routerId}", method = RequestMethod.GET)
	public Router getRouter(@PathVariable("routerId") String routerId) {
		Router router = networkingService.getRouter(routerId);
		loggerTra.debug("router="+ router);
		return router;
	}

	/**
	 * @param neutronRouter
	 * @return
	 */
	@RequestMapping(value = "/routers", method = RequestMethod.POST)
	public Router createRouter(@RequestBody NeutronRouter neutronRouter) {
		Router router = networkingService.createRouter(neutronRouter);
		loggerTra.debug("router="+ router);
		return router;
	}

	/**
	 * @param neutronRouter
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}", method = RequestMethod.PUT)
	public Router updateRouter(@RequestBody NeutronRouter neutronRouter) {
		Router router = networkingService.updateRouter(neutronRouter);
		loggerTra.debug("router="+ router);
		return router;
	}

	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}", method = RequestMethod.DELETE)
	public ActionResponse deleteRouter(@PathVariable("routerId") String routerId ) {
		ActionResponse actionResponse = networkingService.deleteRouter(routerId);
		loggerTra.debug("actionResponse="+ actionResponse);
		return actionResponse;
	}


	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}/gateway", method = RequestMethod.POST)
	public ResponseEntity<String> addGatewayRouter(@PathVariable("routerId") String routerId,@RequestBody NeutronRouter neutronRouter) {
		return networkingService.addGatewayRouter(neutronRouter);
	}

	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}/gateway", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteGatewayRouter(@PathVariable("routerId") String routerId ) {
		Router router = networkingService.getRouter(routerId);
		Router updateRouter = NeutronRouter.builder().from(router).build();
		return networkingService.removeGatewayRouter((NeutronRouter) updateRouter);
	}


	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}/interface", method = RequestMethod.POST)
	public CommonRespVO<RouterInterface> addRouterInterface(@PathVariable("routerId") String routerId,@RequestBody RouterInterfaceVO routerInterfaceVO) {
		loggerTra.debug(routerInterfaceVO.toString());
		CommonRespVO<RouterInterface> result = null;
		try{
			RouterInterface routerInterface = networkingService.addRouterInterface(routerId, routerInterfaceVO.getSubnet(), routerInterfaceVO.getIpAddress());
			loggerTra.debug("routerInterface="+ routerInterface);
			result = new CommonRespVO<RouterInterface>(true,null,routerInterface);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			result = new CommonRespVO<RouterInterface>(false,e.getMessage(),null);
		}

		return result;
	}

	/**
	 * @param routerId
	 * @return
	 */
	@RequestMapping(value="/routers/{routerId}/interface", method = RequestMethod.PUT)
	public CommonRespVO<RouterInterface> deleteRouterInterface(@PathVariable("routerId") String routerId, @RequestBody NeutronPort neutronPort) {

		CommonRespVO<RouterInterface> result = null;
		try{
			RouterInterface routerInterface = networkingService.removeRouterInterface(routerId, neutronPort.getFixedIps().iterator().next().getSubnetId(), neutronPort.getId());
			loggerTra.debug("routerInterface="+ routerInterface);
			result = new CommonRespVO<RouterInterface>(true,null,routerInterface);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			result = new CommonRespVO<RouterInterface>(false,e.getMessage(),null);
		}

		return result;
	}




	/**
	 * @return
	 */
	@RequestMapping(value = "/floatingip", method = RequestMethod.GET)
	public List<? extends NetFloatingIP> listFloatingip() {
		List<? extends NetFloatingIP> listPorts = networkingService.listFloatingip();
		loggerTra.debug("listFloatingIP="+ listPorts);
		return listPorts;
	}


	/**
	 * @return
	 */
	@RequestMapping(value = "/floatingip", method = RequestMethod.POST)
	public CommonRespVO<NetFloatingIP> associateFloatingip(@RequestBody NeutronFloatingIP neutronFloatingIP) {

		CommonRespVO<NetFloatingIP> result = null;
		try{
			NetFloatingIP resultFloatingIP = networkingService.associateFloatingip(neutronFloatingIP.getId(), neutronFloatingIP.getPortId());
			loggerTra.debug("resultFloatingIP="+ resultFloatingIP);
			result = new CommonRespVO<NetFloatingIP>(true,null,resultFloatingIP);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			result = new CommonRespVO<NetFloatingIP>(false,e.getMessage(),null);
		}

		return result;
	}


	/**
	 * @return
	 */
	@RequestMapping(value = "/floatingip/{serverId}", method = RequestMethod.DELETE)
	public CommonRespVO<NetFloatingIP> disassociateFloatingip(@PathVariable("serverId") String serverId) {

		CommonRespVO<NetFloatingIP> result = null;
		try{

			String portId = null;
			List<? extends Port> portList = networkingService.listPorts();
			for (Port port : portList) {
				if(serverId.equals(port.getDeviceId())){
					portId = port.getId();
				}
			}

			String floatingIpId = null;
			if(portId!=null){
				List<? extends NetFloatingIP> listPorts = networkingService.listFloatingip();
				for (NetFloatingIP netFloatingIP : listPorts) {
					if(portId.equals(netFloatingIP.getPortId())){
						floatingIpId = netFloatingIP.getId();
					}
				}
			}

			if(floatingIpId ==null){
				throw new Exception("No floating IPs to disassociate.");
			}

			NetFloatingIP resultFloatingIP = networkingService.disassociateFloatingip(floatingIpId);
			loggerTra.debug("resultFloatingIP="+ resultFloatingIP);
			result = new CommonRespVO<NetFloatingIP>(true,null,resultFloatingIP);
		}catch(Exception e){
			loggerTra.error(e.getMessage(),e);
			result = new CommonRespVO<NetFloatingIP>(false,e.getMessage(),null);
		}

		return result;
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/{tenantId}/quotas", method = RequestMethod.GET)
	public NetQuotaVO quotas(@PathVariable("tenantId") String tenantId) {
		NetQuotaVO netQuotaVo = networkingService.quotas(tenantId);
		List<? extends NetFloatingIP> listFloatingip = networkingService.listFloatingip();
		netQuotaVo.setFloatingIpUsed(listFloatingip.size());
		return netQuotaVo;
	}


	/**
	 * @return
	 */
	@RequestMapping(value = "/network_topology", method = RequestMethod.GET)
	public Map<String,Object> getNetworktopology() {
		Map<String,Object> result = new HashMap<String,Object>();

		List<? extends Port> listPorts = networkingService.listPorts();
		List<? extends Network> listNetworks = networkingService.listNetworks();
		List<? extends Router> listRouters = networkingService.listRouters();
		List<? extends Server> listServers = computeService.listServers();
		List<? extends Subnet> listSubnets = networkingService.listSubnets();

		Collections.sort(listNetworks, new Comparator<Network>(){

			@Override
			public int compare(Network o1, Network o2) {
				if(o1.isRouterExternal() && o2.isRouterExternal())
					return 0;
				else if(o1.isRouterExternal())
					return -1;
				else if (o2.isRouterExternal())
					return 1;
				else
				return 0;
			}

		});

		result.put("ports", listPorts);
		result.put("networks", listNetworks);
		result.put("routers", listRouters);
		result.put("servers", listServers);
		result.put("subnets", listSubnets);

		return result;
	}

}