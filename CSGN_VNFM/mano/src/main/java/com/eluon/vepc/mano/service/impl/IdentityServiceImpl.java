package com.eluon.vepc.mano.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.lang.StringUtils;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.IdentityAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.IdentityService;
import com.eluon.vepc.mano.vo.OpenstackServiceVO;
import com.eluon.vepc.mano.vo.OpenstackServicesVO;
import com.google.gson.Gson;

/**
 * IdentityServiceImpl (IdentityServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: IdentityServiceImpl.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Service("identityService")
public class IdentityServiceImpl implements IdentityService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private IdentityAdapter identityAdapter;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public IdentityServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}

	@Override
	public String listServices() {
		String identityAdminUrl = "";
		String tokenId = getToken().getId();
		List<? extends Endpoint> listTokenEndpoints = listTokenEndpoints();
		logger.debug("listTokenEndpoints="+listTokenEndpoints);
		for (Endpoint endpoint : listTokenEndpoints) {
			if (endpoint.getType().toUpperCase().equals(ServiceType.IDENTITY.toString().toUpperCase())){
				logger.debug("ServiceTypes="+ServiceType.IDENTITY.toString());
				identityAdminUrl = endpoint.getAdminURL() + config.getString("IDENTITY_SERVICE_LIST_URI");
				logger.debug("identityAdminUrl="+identityAdminUrl);
				logger.debug("tokenId="+tokenId);
				return addHostName(endpoint.getAdminURL().getHost(), identityAdapter.listServices(tokenId, identityAdminUrl));
			}
		}
		return null;
	}
	
	@Override
	public List<? extends Tenant> listTenant(){
		return identityAdapter.listTenants();
	}

	@Override
	public ResponseEntity<String> listAllTenant() {
		return identityAdapter.listAllTenants();
	}
	
	private String addHostName(String host, String listServices) {
		Gson gson = new Gson();
		logger.debug("host="+host);
		logger.debug("listServices="+listServices);
		listServices = StringUtils.replace(listServices, "OS-KSADM:services", "osKsadmServcies");
		OpenstackServicesVO openstackServicesVO= gson.fromJson(listServices, OpenstackServicesVO.class);
		logger.debug("openstackServicesVO="+openstackServicesVO);
		List<OpenstackServiceVO> openstackServiceList = openstackServicesVO.getOsKsadmServcies();
		for (OpenstackServiceVO openstackServiceVO : openstackServiceList) {
			openstackServiceVO.setHost(host);
		}
		logger.debug("openstackServiceList="+openstackServiceList);
		return (String) gson.toJson(openstackServiceList);
	}

	@Override
	public Token getToken() {
		return identityAdapter.getToken();
	}
	
	@Override
	public List<? extends Endpoint> listTokenEndpoints() {
		return identityAdapter.listTokenEndpoints();
	}

	@Override
	public List<? extends org.openstack4j.model.identity.v2.Access.Service> getServiceCatalog() {
		return identityAdapter.getServiceCatalog();
	}
}
