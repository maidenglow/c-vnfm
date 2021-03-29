package com.eluon.vepc.mano.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eluon.vepc.mano.common.CommonXmlUtil;
import com.eluon.vepc.mano.dao.VerificationDAO;
import com.eluon.vepc.mano.service.VerificationService;


@Service("verificationService")
public class VerificationServiceImpl implements VerificationService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Autowired
	private VerificationDAO verificationDAO;

	public String getManagementPolicyList(HashMap<String, Object> hm) {
		
		List<LinkedHashMap<String, String>> resultList = verificationDAO.getManagementPolicyList(hm);
		
		Element rootElement = new Element("mnt_policy_list");
		
		if(resultList == null || resultList.size() < 1){
			
			return CommonXmlUtil.getElementToString(rootElement);
		}
		
		for(LinkedHashMap<String, String> resultMap : resultList){
			
			Element mnt_policy_el = new Element("mnt_policy");
			mnt_policy_el.addContent(new Element("mnt_policy_no").addContent(resultMap.get("mnt_policy_no")));
			mnt_policy_el.addContent(new Element("mnt_policy_id").addContent(resultMap.get("mnt_policy_id")));
			mnt_policy_el.addContent(new Element("mnt_policy_nm").addContent(resultMap.get("mnt_policy_nm")));
			mnt_policy_el.addContent(new Element("mnt_policy_value").addContent(resultMap.get("mnt_policy_value")));
			rootElement.addContent(mnt_policy_el);
			
		}
		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	
	@Transactional
	public int putManagementPolicyList(HashMap<String, Object> hm) {
	
		String[] mnt_policy_no_list = (String[])hm.get("mnt_policy_no_list");
		String[] mnt_policy_value_list = (String[])hm.get("mnt_policy_value_list");
		
		int resultCnt = 0;
		
		int index = 0;
		
		HashMap<String, Object> paramMap;
		
		for(String s : mnt_policy_no_list){
			
			paramMap = new HashMap<String, Object>();
			paramMap.put("mnt_policy_no", s);
			paramMap.put("mnt_policy_value", mnt_policy_value_list[index]);
			resultCnt += verificationDAO.putManagementPolicy(paramMap);
			index++;
		}
		
		return resultCnt;
	}

	public String getManagementPolicyOne(HashMap<String, Object> hm) {
		
		LinkedHashMap<String, String> resultMap = verificationDAO.getManagementPolicy(hm);
		
		Element rootElement = new Element("managememt_policy");
		
		if(resultMap == null || resultMap.size() < 1){
			
			return CommonXmlUtil.getElementToString(rootElement);
		}

		rootElement.addContent(new Element("id").addContent(resultMap.get("mnt_policy_id")));
		rootElement.addContent(new Element("name").addContent(resultMap.get("mnt_policy_nm")));
		
		if(resultMap.get("mnt_policy_nm") != null && "energy_efficiency".equals(resultMap.get("mnt_policy_nm")) && "1".equals(resultMap.get("mnt_policy_value"))){
			rootElement.addContent(new Element("value").addContent("on"));
		}else if(resultMap.get("mnt_policy_nm") != null && "energy_efficiency".equals(resultMap.get("mnt_policy_nm")) && "0".equals(resultMap.get("mnt_policy_value"))){
			rootElement.addContent(new Element("value").addContent("off"));
		}else{
			rootElement.addContent(new Element("value").addContent(resultMap.get("mnt_policy_value")));
		}
		
		return CommonXmlUtil.getElementToString(rootElement);
		
	}
	
	public String getManagementPolicyAll(HashMap<String, Object> hm) {
		
		List<String> resultList = verificationDAO.getManagementPolicyIdList(hm);
		
		Element rootElement = new Element("managememt_policy_all");
		
		HashMap<String, Object> paramMap = null;
		
		Element managememt_policy_el = null;
		
		for(String s : resultList){
			
			paramMap = new HashMap<String, Object>();
			paramMap.put("mnt_policy_id", s);
			
			LinkedHashMap<String, String> resultMap = verificationDAO.getManagementPolicy(paramMap);
			
			managememt_policy_el = new Element("managememt_policy");
			
			managememt_policy_el.addContent(new Element("id").addContent(resultMap.get("mnt_policy_id")));
			managememt_policy_el.addContent(new Element("name").addContent(resultMap.get("mnt_policy_nm")));
			
			if(resultMap.get("mnt_policy_nm") != null && "energy_efficiency".equals(resultMap.get("mnt_policy_nm")) && "1".equals(resultMap.get("mnt_policy_value"))){
				managememt_policy_el.addContent(new Element("value").addContent("on"));
			}else if(resultMap.get("mnt_policy_nm") != null && "energy_efficiency".equals(resultMap.get("mnt_policy_nm")) && "0".equals(resultMap.get("mnt_policy_value"))){
				managememt_policy_el.addContent(new Element("value").addContent("off"));
			}else{
				managememt_policy_el.addContent(new Element("value").addContent(resultMap.get("mnt_policy_value")));
			}
			
			rootElement.addContent(managememt_policy_el);
			
		}
		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	
	public int postManagementPolicy(HashMap<String, Object> hm) {
		return 1;
	}
	
	public int putManagementPolicy(HashMap<String, Object> hm) {
		return 1;
	}
	
	public int deleteManagementPolicy(HashMap<String, Object> hm) {
		return 1;
	}
	
	public String getTargetList(HashMap<String, Object> hm) {
		
		Element rootElement = new Element("target_list");
		
		Element property = null;
		
		property = new Element("target");
		property.addContent(new Element("key").addContent("all"));
		property.addContent(new Element("item").addContent("All"));
		rootElement.addContent(property);
		
		property = new Element("target");
		property.addContent(new Element("key").addContent("key1"));
		property.addContent(new Element("item").addContent("Deployment Flavour"));
		rootElement.addContent(property);
		
		property = new Element("target");
		property.addContent(new Element("key").addContent("key2"));
		property.addContent(new Element("item").addContent("Affinity"));
		rootElement.addContent(property);
		
		property = new Element("target");
		property.addContent(new Element("key").addContent("key3"));
		property.addContent(new Element("item").addContent("Dependency"));
		rootElement.addContent(property);
		
		property = new Element("target");
		property.addContent(new Element("key").addContent("key4"));
		property.addContent(new Element("item").addContent("Auto Scaliing"));
		rootElement.addContent(property);
		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	public String getPolicyList(HashMap<String, Object> hm) {
		
		Element rootElement = new Element("policy_list");
		
		Element property = null;
		
		property = new Element("policy");
		property.addContent(new Element("key").addContent("key1"));
		property.addContent(new Element("item").addContent("Policy 1"));
		rootElement.addContent(property);
		
		property = new Element("policy");
		property.addContent(new Element("key").addContent("key2"));
		property.addContent(new Element("item").addContent("Policy 2"));
		rootElement.addContent(property);
		
		property = new Element("policy");
		property.addContent(new Element("key").addContent("key3"));
		property.addContent(new Element("item").addContent("Policy 3"));
		rootElement.addContent(property);
		
		property = new Element("policy");
		property.addContent(new Element("key").addContent("key4"));
		property.addContent(new Element("item").addContent("Policy 4"));
		rootElement.addContent(property);
		
		property = new Element("policy");
		property.addContent(new Element("key").addContent("key5"));
		property.addContent(new Element("item").addContent("Policy 5"));
		rootElement.addContent(property);
		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	
	public String getHostList(HashMap<String, Object> hm) {
		
		List<? extends Hypervisor> Hypervisors = verificationDAO.getHostList(null);
		
		Element rootElement = new Element("host_list");
		
		Element host_el = null;
		
		host_el = new Element("host");
		host_el.addContent(new Element("key").addContent("all"));
		host_el.addContent(new Element("item").addContent("All"));
		
		rootElement.addContent(host_el);
		
		for(Hypervisor h : Hypervisors){
			
			host_el = new Element("host");
			host_el.addContent(new Element("key").addContent(h.getService().getHost()));
			host_el.addContent(new Element("item").addContent(h.getService().getHost()));
			
			rootElement.addContent(host_el);
		}
		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	
	public String getComputeList(HashMap<String, Object> hm) {
		
		List<? extends Server> servers = verificationDAO.getComputeList(null);
		List<? extends Hypervisor> Hypervisors = verificationDAO.getHostList(null);
		
		System.out.println("#######################################");
		System.out.println("#############servers############");
		System.out.println(servers.toString());
		
		System.out.println("#######################################");
		System.out.println("#############hypervisors################");
		System.out.println(Hypervisors.toString());
		
		Collections.sort(servers, new Comparator<Server>(){

			public int compare(Server arg0, Server arg1) {
				return arg0.getUpdated().compareTo(arg1.getUpdated());
			}
			
		});
		
		Element rootElement = new Element("compute_group_list");
		Element compute_group_el =null;
		Element compute_el =null;

		for (int i = 0; i < Hypervisors.size(); i++) {
            
    		compute_group_el =new Element("compute_group");
    		compute_group_el.addContent(new Element("host").addContent(Hypervisors.get(i).getHypervisorHostname()));
    		
    		for (int j = 0; j < servers.size(); j++) {
				
    			if (Hypervisors.get(i).getHypervisorHostname().equals(servers.get(j).getHypervisorHostname())) {
	            	compute_el = new Element("compute");
	            	compute_el.addContent(new Element("key").addContent(servers.get(j).getId()));
	            	compute_el.addContent(new Element("item").addContent(servers.get(j).getName()));
	            	compute_el.addContent(new Element("image").addContent(servers.get(j).getImage().getName()));
	            	compute_group_el.addContent(compute_el);
				}
    			   			
    			
			}
    		rootElement.addContent(compute_group_el);
			
			
		}
		

		
		return CommonXmlUtil.getElementToString(rootElement);
	}
	
	public String getResultVerification(HashMap<String, Object> hm) {		
		
		return verificationDAO.getResultVerification(hm);
	}
}
