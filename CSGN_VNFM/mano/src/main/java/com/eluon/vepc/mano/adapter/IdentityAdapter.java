package com.eluon.vepc.mano.adapter;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.identity.v2.Access.Service;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eluon.vepc.mano.service.CommonService;

/**
 * Identity Adapter (IdentityAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: IdentityAdapter.java,v1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class IdentityAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private RestTemplate restTemplate;

	public IdentityAdapter() {
	}
	
	public List<? extends Endpoint> listTokenEndpoints() {
		return commonService.buildOSFactory().identity().listTokenEndpoints();
	}

	public String listServices(String tokenId, String identityAdminUrl) {
		String listServices = "";
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
			listServices = EntityUtils.toString(closeableHttpResponse.getEntity());
		} catch (IOException e) {
			loggerTra.debug("exception=" + e);
		}
		loggerTra.debug("listServices=" + listServices);
		loggerTra.debug("#################  MANO AFTER ###################");
		return listServices;
	}

	public List<? extends Service> getServiceCatalog() {
		return commonService.buildOSFactory().getAccess().getServiceCatalog();
	}

	public Token getToken() {
		return commonService.buildOSFactory().getAccess().getToken();
	}
	
	
	public List<? extends Tenant> listTenants() {
		return commonService.buildOSFactory().identity().tenants().list();
	}

	public ResponseEntity<String> listAllTenants() {

		OSClientV2 osClient = commonService.buildOSFactory();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		String tokenId = osClient.getAccess().getToken().getId();
		httpHeaders.add("X-Auth-Token", tokenId);

		HttpEntity<String> reqEntity = new HttpEntity<String>(httpHeaders);

		String endpoint = osClient.getEndpoint();
		String url = endpoint + config.getString("IDENTITY_TENANTS_URI");

		ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, reqEntity, String.class);
		loggerTra.debug("HTTP_RES = " + resEntity.getStatusCode());
		loggerTra.debug("HTTP_BODY = " + resEntity.getBody());

		return resEntity;
	}	
}