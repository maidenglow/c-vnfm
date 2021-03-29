package com.eluon.vepc.mano.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.Server.Status;
import org.openstack4j.model.compute.actions.LiveMigrateOptions;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.openstack.OSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eluon.vepc.mano.service.LiveMigrationService;
import com.eluon.vepc.mano.vo.LiveMigrationVO;
import com.eluon.vepc.mano.vo.OpenstackServiceVO;
import com.eluon.vepc.mano.vo.OpenstackServicesVO;
import com.google.gson.Gson;

@Service("liveMigrationService")
public class LiveMigrationImpl implements LiveMigrationService {

	@Autowired
	private CompositeConfiguration config;

	public String getInstanceList(String zoneName) {

		List<? extends HostAggregate> listHost = getOSClient().compute().hostAggregates().list();
		HostAggregate hostaggregate = null;
		for (int i = 0; i < listHost.size(); i++) {
			HostAggregate host = listHost.get(i);
			if (host.getName().equals(zoneName)) {
				hostaggregate = host;
			}
		}

		List<? extends Server> serverList = getOSClient().compute().servers().list();
		List resultList = new ArrayList();
		List<LiveMigrationVO> instanceVOList = new ArrayList<LiveMigrationVO>();

		List hyperList = new ArrayList();

		for (int i = 0; i < hostaggregate.getHosts().size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("hypervisor", hostaggregate.getHosts().get(i));
			hyperList.add(i, map);
		}

		for (Server servere : serverList) {

			Map<String, List<? extends Address>> addressMap = servere.getAddresses().getAddresses();

			LiveMigrationVO instanceVO = new LiveMigrationVO();
			instanceVO.setHypervisor(servere.getHypervisorHostname());
			instanceVO.setId(servere.getId());
			instanceVO.setName(servere.getName());

			List<String> addressList;

			for (Map.Entry<String, List<? extends Address>> entry : addressMap.entrySet()) {
				// instanceVO.setAddressList(entry.getValue());

				addressList = new ArrayList<String>();
				List<? extends Address> list = entry.getValue();

				for (Address ad : list) {
					addressList.add(ad.getAddr());
				}

				instanceVO.setAddressList(addressList);
			}

			instanceVOList.add(instanceVO);

		}
		resultList.add(hyperList);
		resultList.add(instanceVOList);

		return new Gson().toJson(resultList);

	}

	public List<OpenstackServiceVO> getComputerList() {

		List<? extends Server> servers = getOSClient().compute().servers().list();

		Gson gson = new Gson();
		System.out.println(gson.toJson(servers));

		// return servers;

		String tokenId = getOSClient().getAccess().getToken().getId();

		List<? extends Endpoint> EndpointList = getOSClient().identity().listTokenEndpoints();

		String identityAdminUrl = "";

		List<OpenstackServiceVO> openstackServiceVOList = null;
		for (Endpoint endpoint : EndpointList) {

			if (endpoint.getType().toUpperCase().equals(ServiceType.IDENTITY.toString().toUpperCase())) {
				System.out.println("ServiceTypes=" + ServiceType.IDENTITY.toString());
				identityAdminUrl = endpoint.getAdminURL() + "/OS-KSADM/services";
				System.out.println("identityAdminUrl=" + identityAdminUrl);
				System.out.println("tokenId=" + tokenId);
				// return addHostName(endpoint.getAdminURL().getHost(),
				// identityAdapter.listServices(tokenId, identityAdminUrl));

				String listServices = "";
				CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse closeableHttpResponse = null;
				RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000)
						.setConnectTimeout(3000).setSocketTimeout(3000).build();
				HttpGet httpGet = new HttpGet(identityAdminUrl);
				httpGet.setConfig(requestConfig);
				httpGet.addHeader("X-Auth-Token", tokenId);
				// commonService.setHttpGetRequestHeaders(headers, httpGet);
				System.out.println("#################  MANO BEFORE #################");
				try {
					closeableHttpResponse = httpclient.execute(httpGet);
					// System.out.println("HTTP_RES = " +
					// closeableHttpResponse.getStatusLine().getStatusCode());
					listServices = EntityUtils.toString(closeableHttpResponse.getEntity());
					listServices = StringUtils.replace(listServices, "OS-KSADM:services", "osKsadmServcies");
					System.out.println("listServices=" + listServices);
					OpenstackServicesVO openstackServicesVO = gson.fromJson(listServices, OpenstackServicesVO.class);

					openstackServiceVOList = openstackServicesVO.getOsKsadmServcies();

				} catch (IOException e) {
					System.out.println("exception=" + e);
				}
			}
		}

		return openstackServiceVOList;
	}

	public boolean setMigration(String serverId, String hyperId, boolean option) {

		boolean returnVal = false;
		ActionResponse serverList = getOSClient().compute().servers().liveMigrate(serverId,
				LiveMigrateOptions.create().host(hyperId).blockMigration(option).diskOverCommit(false));

		Server migrateList = getOSClient().compute().servers().waitForServerStatus(serverId, Status.ACTIVE, 180,
				TimeUnit.SECONDS);

		if (serverList.isSuccess() != false || migrateList.getStatus().toString() == "ACTIVE") {
			returnVal = true;
		}

		return returnVal;
	}

	private static OSClientV3 osV3;
	private static OSClientV2 os;

	public OSClientV3 getOSClientV3() {

		osV3 = OSFactory.builderV3().endpoint(config.getString("AUTH_ENDPOINT"))
				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"),
						Identifier.byName("Default"))
				.scopeToProject(Identifier.byName(config.getString("AUTH_TENANT_NAME")), Identifier.byName("default"))
				.authenticate();

		return osV3;
	}

	public OSClientV2 getOSClient() {

		os = OSFactory.builderV2().endpoint(config.getString("AUTH_ENDPOINT"))
				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"))
				.tenantName(config.getString("AUTH_TENANT_NAME")).authenticate();

		return os;
	}

}
