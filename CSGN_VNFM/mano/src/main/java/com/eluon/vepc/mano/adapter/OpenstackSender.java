package com.eluon.vepc.mano.adapter;

import java.util.HashMap;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eluon.vepc.mano.service.CommonService;

/**
 * OpenstackSender (OpenstackSender)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OpenstackSender.java,v1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Component
public class OpenstackSender {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;

	public OpenstackSender() {
	}
	
	public CloseableHttpResponse callOpenstackRESTAPI(HashMap<String, String> headers, String hostIp, String endPointUrl, String clientIp) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse closeableHttpResponse = null;
		if (StringUtils.stripToEmpty(config.getString("DEBUG_FLAG")).equals("Y")) {
			loggerTra.debug("CLIENT_IP = " + clientIp + "|HOST_IP = " + hostIp + "|END_POINT_URL = " + endPointUrl);
		}
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(config.getInt("REQ_READ_TIMEOUT")).setConnectTimeout(config.getInt("REQ_CONN_TIMEOUT")).setSocketTimeout(config.getInt("REQ_READ_TIMEOUT")).build();
		if (endPointUrl == null || endPointUrl.equals("")) endPointUrl = config.getString("TEST_END_POINT_URL");
		HttpGet httpGet = new HttpGet(endPointUrl);
		httpGet.setConfig(requestConfig);
		commonService.setHttpGetRequestHeaders(headers, httpGet);
		if (StringUtils.stripToEmpty(config.getString("DEBUG_FLAG")).equals("Y")) {
			loggerTra.debug("#########  MANO BEFORE #########");
		}
		closeableHttpResponse = httpclient.execute(httpGet);
		if (StringUtils.stripToEmpty(config.getString("DEBUG_FLAG")).equals("Y")) {
			loggerTra.debug("HTTP_RES = " + closeableHttpResponse.getStatusLine().getStatusCode());
			String mmsGwUploadResData = EntityUtils.toString(closeableHttpResponse.getEntity()); 
			loggerTra.debug("mmsGwUploadResData=" + mmsGwUploadResData);
			loggerTra.debug("#########  MANO AFTER ##########");
		}
		return closeableHttpResponse;
	}
	
}