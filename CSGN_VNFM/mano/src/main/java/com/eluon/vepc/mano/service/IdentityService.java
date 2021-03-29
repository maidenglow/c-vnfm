package com.eluon.vepc.mano.service;

import java.util.List;

import org.openstack4j.model.identity.v2.Access.Service;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.Token;
import org.springframework.http.ResponseEntity;


/**
 * IdentityService Interface(IdentityService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: IdentityService.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
public interface IdentityService {
	public String listServices();
	public List<? extends Tenant> listTenant();
	public ResponseEntity<String> listAllTenant();
	public Token getToken();
	public List<? extends Endpoint> listTokenEndpoints();
	public List<? extends Service> getServiceCatalog();

}
