package com.eluon.vepc.mano.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.ibatis.session.SqlSession;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eluon.vepc.mano.dao.VerificationDAO;
import com.eluon.vepc.mano.service.CommonService;

@Repository("verificationDAO")
public class VerificationDAOImpl implements VerificationDAO {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CompositeConfiguration config;
	
	@Autowired
	private SqlSession sqlSession;

	public List<LinkedHashMap<String, String>> getManagementPolicyList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Verification.getManagementPolicyList", hm);
	}
	
	public LinkedHashMap<String, String> getManagementPolicy(HashMap<String, Object> hm) {
		return sqlSession.selectOne("Verification.getManagementPolicy", hm);
	}
	
	public List<String> getManagementPolicyIdList(HashMap<String, Object> hm) {
		return sqlSession.selectList("Verification.getManagementPolicyIdList", hm);
	}
	
	public int postManagementPolicy(HashMap<String, Object> hm) {
		return sqlSession.insert("Verification.postManagementPolicy", hm);
	}
	
	public int postManagementPolicyRef(HashMap<String, Object> hm) {
		return sqlSession.insert("Verification.postManagementPolicyRef", hm);
	}
	
	public int putManagementPolicy(HashMap<String, Object> hm) {
		return sqlSession.update("Verification.putManagementPolicy", hm);
	}
	
	public int deleteManagementPolicy(HashMap<String, Object> hm) {
		return sqlSession.delete("Verification.deleteManagementPolicy", hm);
	}
	
	public int deleteManagementPolicyRef(HashMap<String, Object> hm) {
		return sqlSession.delete("Verification.deleteManagementPolicyRef", hm);
	}
	
	public List<? extends Hypervisor> getHostList(HashMap<String, Object> hm) {
		
		OSClientV2 os = commonService.buildOSFactory();
//		OSClientV3 os = OSFactory.builderV3()
//				.endpoint(config.getString("AUTH_ENDPOINT"))
//				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"), Identifier.byName("Default"))
//				.scopeToProject(Identifier.byName(config.getString("AUTH_TENANT_NAME")))
////                .endpoint("http://112.133.98.4:35357/v2.0/")
////                .credentials("admin",".osn0801")
////                .tenantName("admin")
//                .authenticate();
		
		return os.compute().hypervisors().list();
	}
	
	public List<? extends Server> getComputeList(HashMap<String, Object> hm) {
		
		OSClientV2 os = commonService.buildOSFactory();
		
//		OSClientV3 os = OSFactory.builderV3()
//				.endpoint(config.getString("AUTH_ENDPOINT"))
//				.credentials(config.getString("AUTH_CREDENTIALS_ID"), config.getString("AUTH_CREDENTIALS_PASSWORD"), Identifier.byName("Default"))
//				.scopeToProject(Identifier.byName(config.getString("AUTH_TENANT_NAME")))
////                .endpoint("http://112.133.98.4:35357/v2.0/")
////                .credentials("admin",".osn0801")
////                .tenantName("admin")
//                .authenticate();
		
		return os.compute().servers().list();
	}
	
	public String getResultVerification(HashMap<String, Object> hm) {
			
		String standard = hm.get("standard").toString();
		String content = hm.get("content").toString();
		String descriptorId = hm.get("descriptorId").toString();
		String targetId = hm.get("targetId").toString();
		String policyId = hm.get("policyId").toString();
		String hostId = hm.get("hostId").toString();
		
		
		String protocol = "http://";
		// Main Server
//		String serverUrl = "134.75.238.115:8080/verisdnfv/"+standard+"/"+content+"/"+descriptorId+"/"+targetId+"/"+policyId+"/"+hostId;
		// Test Server
		String serverUrl = "134.75.238.113:8080/verisdnfv/"+standard+"/"+content+"/"+descriptorId+"/"+targetId+"/"+policyId+"/"+hostId;
		
		System.out.println("serverUrl = " + serverUrl);
		
		HttpClient httpclient = null;
		HttpGet httpget = null;
		
		String tempString = "";
		String resultString = "";
		
		try {
			// HttpGet�앹꽦
			
			httpclient = new DefaultHttpClient();
			
			httpget = new HttpGet(protocol + serverUrl);
			
			HttpResponse response = httpclient.execute(httpget);
 
			// �묐떟 寃곌낵
			System.out.println("mano HOME Status : " + response.getStatusLine());
			
			httpget.abort();
			
			
			int statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode == 200){
				
				httpget = new HttpGet(protocol + serverUrl);
				
				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
	 
				// �묐떟 寃곌낵
				System.out.println("request Status : " + response.getStatusLine());
				if (entity != null) {
					
					BufferedReader rd = new BufferedReader(new InputStreamReader(
							response.getEntity().getContent()));
					
					while ((tempString = rd.readLine()) != null) {
						resultString += tempString;
					}
					
				}
				
				httpget.abort();
			}
				
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(httpclient != null){
				httpclient.getConnectionManager().shutdown();
			}
		}
		
		return resultString;
	}
	
	
	public String getResultVerificationTest(HashMap<String, Object> hm) {
		
		String xmlString = "";
		
		try{
			
			ClassLoader classLoader = getClass().getClassLoader();
			
			File file = null;
			
			int i = 1;
			
			if(i == 1){
				file = new File(classLoader.getResource("verification/ra-nsd.xml").getFile());
			}else{
				file = new File(classLoader.getResource("verification/verisdnfv_response.xml").getFile());
			}
			
			String falePath = file.getAbsolutePath();
			
			System.out.println("falePath = " + falePath);
			
			String decodedPath = URLDecoder.decode(falePath, "UTF-8");
			
			System.out.println("decodedPath = " + decodedPath);
			
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(decodedPath);
			
			XMLOutputter fmt = new XMLOutputter();
			xmlString = fmt.outputString(document);
            
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return xmlString;
	}
	
}