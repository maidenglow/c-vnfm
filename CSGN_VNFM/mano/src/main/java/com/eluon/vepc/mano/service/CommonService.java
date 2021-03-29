package com.eluon.vepc.mano.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;

import com.eluon.vepc.mano.vo.PaginationVO;

/**
 * Common Service Interface(CommonService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: CommonService.java,v1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public interface CommonService {
	public CompositeConfiguration getConfiguration();
	public OSClientV2 buildOSFactory();
	public OSClientV3 buildOSFactoryV3();
	public ContainerFormat getContainerFormat(String containerFormat);
	public DiskFormat getDiskFormat(String diskFormat);
	public String getServerId(String hostName);
	public String getServerId();
	public String getServerSourceIp(String hostName);
	public String getServerSourceIp();
	public String getHostName();
	public String getHostAddress();
	public String getServerMode();
	public HashMap<String,String> getRequestHeaders(HttpServletRequest req);
	public byte[] extractHttpRequestBody(HttpServletRequest req);
	public void setHttpGetRequestHeaders(HashMap<String, String> headers, HttpGet httpGet);
	public void setHttpPostRequestHeaders(HashMap<String, String> headers, HttpPost httpPost);
	public void setCloseableHttpResponseHeaders(HttpServletResponse res, CloseableHttpResponse closeableHttpResponse);
	public int getResponseHeaderSize(CloseableHttpResponse closeableHttpResponse);
	public PaginationVO getPaginationForm(int totalCount, int currentPage, int pageSize, String strUri);
}
