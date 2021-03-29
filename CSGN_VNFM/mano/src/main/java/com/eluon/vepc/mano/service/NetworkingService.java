package com.eluon.vepc.mano.service;

import java.util.List;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.NetFloatingIP;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.RouterInterface;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.networking.domain.NeutronNetwork;
import org.openstack4j.openstack.networking.domain.NeutronNetworkUpdate;
import org.openstack4j.openstack.networking.domain.NeutronPort;
import org.openstack4j.openstack.networking.domain.NeutronRouter;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;
import org.springframework.http.ResponseEntity;

import com.eluon.vepc.mano.vo.NetQuotaVO;

/**
 * NetworkingService Interface(NetworkingService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkingService.java,v 1.0 2015/03/30 00:00:00 SimByungChul Express $
 */
public interface NetworkingService {
	public List<? extends Network> listNetworks();
	public Network getNetwork(String networkId);
	public Network createNetwork(NeutronNetwork neutronNetwork);
	public Network updateNetwork(String networkId, NeutronNetworkUpdate neutronNetworkUpdate);
	public ActionResponse deleteNetwork(String networkId);
	public List<? extends Subnet> listSubnets();
	public Subnet getSubnet(String subnetId);
	public Subnet createSubnet(NeutronSubnet neutronSubnet);
	public ResponseEntity<String> updateSubnet(NeutronSubnet neutronSubnet);
	public ActionResponse deleteSubnet(String subnetId);
	public List<? extends Port> listPorts();
	public Port getPort(String portId);
	public Port createPort(NeutronPort neutronPort);
	public Port updatePort(NeutronPort neutronPort);
	public ActionResponse deletePort(String portId);
	public List<? extends Router> listRouters();
	public Router getRouter(String routerId);
	public Router createRouter(NeutronRouter neutronRouter);
	public Router updateRouter(NeutronRouter neutronRouter);
	public ActionResponse deleteRouter(String routerId);
	public ResponseEntity<String> addGatewayRouter(Router router);
	public ResponseEntity<String> removeGatewayRouter(Router router);
	public RouterInterface addRouterInterface(String routerId, Subnet subnet, String ipAddress); 
	public RouterInterface removeRouterInterface(String routerId, String subnetId, String portId);
	public List<? extends NetFloatingIP> listFloatingip();
	public NetFloatingIP associateFloatingip(String floatingIpId,String portId);
	public NetFloatingIP disassociateFloatingip(String floatingIpId);
	public NetQuotaVO quotas(String tenantId);
}
