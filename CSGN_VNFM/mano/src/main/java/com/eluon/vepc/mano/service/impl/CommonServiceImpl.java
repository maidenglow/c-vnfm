package com.eluon.vepc.mano.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.vo.PaginationVO;

/**
 * CommonService Impl (CommonServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: CommonServiceImpl.java,v1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CompositeConfiguration config;

	public CommonServiceImpl() {
	}

	@Override
	public CompositeConfiguration getConfiguration() {
		return config;
	}

	@Override
	public OSClientV2 buildOSFactory() {
		return OSFactory.builderV2()
				.endpoint(config.getString("AUTH_ENDPOINT"))
				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"))
				.tenantName(config.getString("AUTH_TENANT_NAME"))
				.authenticate();
	}	
	
	@Override
	public OSClientV3 buildOSFactoryV3() {
		return OSFactory.builderV3()
				.endpoint(config.getString("AUTH_ENDPOINT3"))
				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"), Identifier.byName("Default"))
				.scopeToProject(Identifier.byName(config.getString("AUTH_TENANT_NAME")), Identifier.byName("default"))
				.authenticate();
	}
	
	@Override
	public ContainerFormat getContainerFormat(String containerFormat) {
		if (containerFormat.toUpperCase().equals("BARE")) {
			return ContainerFormat.BARE;
		} else if (containerFormat.toUpperCase().equals("OVF")) {
			return ContainerFormat.OVF;
		} else if (containerFormat.toUpperCase().equals("AKI")) {
			return ContainerFormat.AKI;
		} else if (containerFormat.toUpperCase().equals("ARI")) {
			return ContainerFormat.ARI;
		} else if (containerFormat.toUpperCase().equals("AMI")) {
			return ContainerFormat.AMI;
		} else  {
			return ContainerFormat.UNRECOGNIZED;
		}
	}

	@Override
	public DiskFormat getDiskFormat(String diskFormat) {
		if (diskFormat.toUpperCase().equals("RAW")) {
			return DiskFormat.RAW;
		} else if (diskFormat.toUpperCase().equals("VHD")) {
			return DiskFormat.VHD;
		} else if (diskFormat.toUpperCase().equals("VMDK")) {
			return DiskFormat.VMDK;
		} else if (diskFormat.toUpperCase().equals("VDI")) {
			return DiskFormat.VDI;
		} else if (diskFormat.toUpperCase().equals("ISO")) {
			return DiskFormat.ISO;
		} else if (diskFormat.toUpperCase().equals("QCOW2")) {
			return DiskFormat.QCOW2;
		} else if (diskFormat.toUpperCase().equals("AKI")) {
			return DiskFormat.AKI;
		} else if (diskFormat.toUpperCase().equals("ARI")) {
			return DiskFormat.ARI;
		} else if (diskFormat.toUpperCase().equals("AMI")) {
			return DiskFormat.AMI;
		} else  {
			return DiskFormat.UNRECOGNIZED;
		}
	}
	
	@Override
	public String getServerId(String hostName) {
		return config.getString(hostName+".SERVER_ID");
	}

	@Override
	public String getServerId() {
		try {
			return config.getString(InetAddress.getLocalHost().getHostName().toUpperCase()+".SERVER_ID");
		} catch (UnknownHostException e) {
			return null;
		}
	}

	@Override
	public String getServerSourceIp(String hostName) {
		return config.getString(hostName+".SERVER_SOURCE_IP");
	}

	@Override
	public String getServerSourceIp() {
		try {
			return config.getString(InetAddress.getLocalHost().getHostName().toUpperCase()+".SERVER_SOURCE_IP");
		} catch (UnknownHostException e) {
			return null;
		}
	}

	@Override
	public String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName().toUpperCase();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	@Override
	public String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	@Override
	public String getServerMode() {
		try {
			return config.getString(InetAddress.getLocalHost().getHostName().toUpperCase()+".MODE");
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
	@Override
	public HashMap<String,String> getRequestHeaders(HttpServletRequest req) {
		HashMap<String,String> hashMap = new HashMap<String,String>();
		Enumeration enumeration = req.getHeaderNames();
		String headerName = null;
		while (enumeration.hasMoreElements()) {
			headerName = (String) enumeration.nextElement();
			hashMap.put(headerName, req.getHeader(headerName));
			if (config.getString("DETAIL_DEBUG_FLAG").equals("Y")) {
				loggerTra.debug("headerName =" + headerName + ": " + req.getHeader(headerName));
			}
		}
		return hashMap;
	}	
	
	@Override	
	public byte[] extractHttpRequestBody(HttpServletRequest req) {
		BufferedInputStream inputstream = null;
		ByteArrayOutputStream bout = null;
		byte[] extractData = null;
		try {
			inputstream = new BufferedInputStream(req.getInputStream());
			int n = 0;
			int bufSize = 512;
			bout = new ByteArrayOutputStream();
			while ( n>=0 ) {
				byte[] buf = new byte[bufSize];
				n = inputstream.read(buf, 0, bufSize);
				if ( n>0 ) {
					bout.write(buf, 0, n);
				}
			}
			extractData = bout.toByteArray();
		} catch (IOException e) {
			loggerTra.debug("private byte[] extractRequestBody(HttpServletRequest req) , Exception="+e);
		} finally {
			if(bout != null) try { bout.close(); } catch(Exception e) {};
			if(inputstream != null) try { inputstream.close(); } catch(Exception e) {};
		}
		return extractData;
	}

	@Override
	public void setHttpGetRequestHeaders(HashMap<String, String> headers, HttpGet httpGet) {
		for (String headerName : headers.keySet()) {
			if (!headerName.toLowerCase().equals("content-length")) {
				httpGet.addHeader(headerName, headers.get(headerName));
			}
			if (config.getString("DETAIL_DEBUG_FLAG").equals("Y")) {
				loggerTra.debug("header name =" + headerName + ": " + headers.get(headerName));
			}
		}
	}

	@Override
	public void setHttpPostRequestHeaders(HashMap<String, String> headers, HttpPost httpPost) {
		for (String headerName : headers.keySet()) {
			if (!headerName.toLowerCase().equals("content-length")) {
				httpPost.addHeader(headerName, headers.get(headerName));
			}
			if (config.getString("DETAIL_DEBUG_FLAG").equals("Y")) {
				loggerTra.debug("header name =" + headerName + ": " + headers.get(headerName));
			}
		}
	}
	
	@Override
	public void setCloseableHttpResponseHeaders(HttpServletResponse res, CloseableHttpResponse closeableHttpResponse) {
		if (closeableHttpResponse != null) {
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for(Header h : headers){
				res.setHeader(h.getName(), h.getValue());
			}
		}
	}

	@Override
	public int getResponseHeaderSize(CloseableHttpResponse closeableHttpResponse) {
		int headerSize =0;
		if (closeableHttpResponse != null) {
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for(Header h : headers){
				headerSize = headerSize + h.getName().length();
				headerSize = headerSize + h.getValue().length();
			}
		}
		return headerSize;
	}

	@Override
	public PaginationVO getPaginationForm(int totalCount, int currentPage, int pageSize, String strUri) {
		PaginationVO paginationVO = new PaginationVO();
		int maxPage = pageSize == 0 ? totalCount : (totalCount - 1) / pageSize + 1;
		currentPage = currentPage > maxPage ? maxPage : currentPage;
		int nStartPageList = ( ( currentPage - 1 ) / 10 ) * 10 + 1;
		int nEndPageList = nStartPageList + 10;
		StringBuffer sb = new StringBuffer();
		if( nEndPageList > maxPage ) nEndPageList = maxPage + 1;
		if( 10 < nStartPageList ) {
			sb.append(strUri+"1');\">&nbsp;&nbsp;<img src=\"/mano/images/btn_first.gif\" width=\"17\" height=\"11\" alt=\"처음\" align=\"absmiddle\"/></a>&nbsp;&nbsp;&nbsp;");
			sb.append(strUri+(nStartPageList-10)+"');\">&nbsp;&nbsp;<img src=\"/mano/images/btn_prev.gif\" width=\"11\" height=\"11\" alt=\"이전\" align=\"absmiddle\"/></a>&nbsp;&nbsp;&nbsp;");
		}
		for( int i = nStartPageList ; i < nEndPageList ; i++ ) {
			if( i == currentPage ) {
				sb.append(currentPage+"&nbsp;&nbsp;");
			} else {
//				sb.append(strUri+i+"\">"+i+"</a>&nbsp;&nbsp;");
				sb.append(strUri+i+"');\">"+i+"</a>&nbsp;&nbsp;");

			}
		}
		if( nEndPageList <= maxPage ) {
			sb.append(strUri+nEndPageList+"');\">&nbsp;&nbsp;<img src=\"/mano/images/btn_next.gif\" width=\"11\" height=\"11\" alt=\"다음\" align=\"absmiddle\"/></a>&nbsp;&nbsp;&nbsp;");
			sb.append(strUri+maxPage+"');\">&nbsp;&nbsp;<img src=\"/mano/images/btn_end.gif\" width=\"17\" height=\"11\" alt=\"끝\" align=\"absmiddle\"/></a>&nbsp;&nbsp;&nbsp;");
		}
		paginationVO.setTotalCount(totalCount);
		paginationVO.setPageSize(pageSize);
		paginationVO.setMaxPage(maxPage);
		paginationVO.setCurrentPage(currentPage);
		paginationVO.setNStartPageList(nStartPageList);
		paginationVO.setNEndPageList(nEndPageList);
		paginationVO.setPageDivideForm(sb.toString());
		loggerTra.debug("paginationVO =" + paginationVO );
		return paginationVO;
	}

}