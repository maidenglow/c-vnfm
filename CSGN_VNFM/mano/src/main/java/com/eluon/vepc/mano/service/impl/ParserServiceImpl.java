package com.eluon.vepc.mano.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.Element;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eluon.vepc.mano.adapter.ComputeAdapter;
import com.eluon.vepc.mano.common.CommonXmlUtil;
import com.eluon.vepc.mano.dao.ParserDao;
import com.eluon.vepc.mano.service.ParserService;



/**
 * AccountService Interface(AccountService)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountService.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Service("parserService")
public class ParserServiceImpl implements ParserService{

	@Autowired
	private ParserDao parserDao;
	
	@Autowired
	private ComputeAdapter computeAdapter;
	
	protected final Logger logger = LoggerFactory.getLogger(ParserServiceImpl.class);
	
	//vld
	private Element getVldElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVldCount(hm);
		
		if(cnt < 1){
			return new Element("vld");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVldOne(hm);
		
		List<String> id= new ArrayList<String>();
		List<String> vendor= new ArrayList<String>();
		List<String> descriptor_version= new ArrayList<String>();
		List<String> number_of_endpoints= new ArrayList<String>();
		List<String> root_requirement= new ArrayList<String>();
		List<String> leaf_requirement= new ArrayList<String>();
		List<String> qos= new ArrayList<String>();
		List<String> test_access= new ArrayList<String>();
		List<String> connection= new ArrayList<String>();
		List<String> connectivity_type= new ArrayList<String>();
		List<String> vld_security= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			descriptor_version.add(map.get("descriptor_version"));
			number_of_endpoints.add(map.get("number_of_endpoints"));
			root_requirement.add(map.get("root_requirement"));
			leaf_requirement.add(map.get("leaf_requirement"));
			qos.add(map.get("qos"));
			test_access.add(map.get("test_access"));
			connection.add(map.get("connection"));
			connectivity_type.add(map.get("connectivity_type"));
			vld_security.add(map.get("vld_security"));
			
		}
		
		Element rootElement = new Element("vld");
		
		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));
		rootElement.addContent(new Element("number_of_endpoints").addContent(number_of_endpoints.get(0)));
		rootElement.addContent(new Element("root_requirement").addContent(root_requirement.get(0)));
		rootElement.addContent(new Element("leaf_requirement").addContent(leaf_requirement.get(0)));
		
		Element qos_el = new Element("qos");
		
		for(String s : qos){
			
			if( !"NULL".equals(s) ){
				qos_el.addContent(new Element("item").addContent(s));
        	}
			
		}
		
		rootElement.addContent(qos_el);
		
		rootElement.addContent(new Element("test_access").addContent(test_access.get(0)));
		
		Element connection_el = new Element("connection");
		
		for(String s : connection){
			
			if( !"NULL".equals(s) ){
				connection_el.addContent(new Element("ref").addContent(s));
        	}
			
		}

		rootElement.addContent(connection_el);
		
		rootElement.addContent(new Element("connectivity_type").addContent(connectivity_type.get(0)));
		
		
		Element vld_security_el = new Element("vld_security");
		
		
		for(String s : vld_security){
			
			if( !"NULL".equals(s) ){
				vld_security_el.addContent(s);
				break;
        	}
			
		}
		
		rootElement.addContent(vld_security_el);
		
		return rootElement;
	}
	
	@Override
	public String getVldOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVldElement(hm));
        
	}

	@Override
	public String getVldAll() {
		
		int cnt = parserDao.getVldCount();
		
		Element vld_all = new Element("vld_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vld_all);
		}
		
		
		List<String> list = parserDao.getVldAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vldId", id);
			
			vld_all.addContent(getVldElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(vld_all);
	}
	
	//pnfd
	private Element getPnfdElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getPnfdCount(hm);
		
		if(cnt < 1){
			return new Element("pnfd");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getPnfdOne(hm);
		
		List<String> id= new ArrayList<String>();
		List<String> vendor= new ArrayList<String>();
		List<String> version= new ArrayList<String>();
		List<String> description= new ArrayList<String>();
		List<String> connection_point_id= new ArrayList<String>();
		List<String> connection_point_type= new ArrayList<String>();
		List<String> descriptor_version= new ArrayList<String>();
		List<String> pnfd_security= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			version.add(map.get("version"));
			description.add(map.get("description"));
			connection_point_id.add(map.get("connection_point_id"));
			connection_point_type.add(map.get("connection_point_type"));
			descriptor_version.add(map.get("descriptor_version"));
			pnfd_security.add(map.get("pnfd_security"));
			
		}
		
		Element rootElement = new Element("pnfd");
		
		rootElement.addContent(new Element("id").addContent(id.get(0)));
        rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
        rootElement.addContent(new Element("version").addContent(version.get(0)));
        rootElement.addContent(new Element("description").addContent(description.get(0)));
        
        Element connection_point_el = new Element("connection_point");
        
        int i = 0;
        for(String s : connection_point_id){
        	
        	if( !"NULL".equals(s) ){
        		
        		Element connection_point_info_el = new Element("connection_point_info");
        		connection_point_info_el.addContent(new Element("id").addContent(s));
        		connection_point_info_el.addContent(new Element("type").addContent(connection_point_type.get(i)));
        		connection_point_el.addContent(connection_point_info_el);
        	}
        	
        	i++;
        }
        
        rootElement.addContent(connection_point_el);
        
        rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));
        
        Element pnfd_security_el = new Element("pnfd_security");
        
        for(String s : pnfd_security){
        	
        	if( !"NULL".equals(s) ){
        		pnfd_security_el.addContent(s);
        		break;
        	}
        }
        
        rootElement.addContent(pnfd_security_el);
        
		return rootElement;
	}
	
	@Override
	public String getPnfdOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getPnfdElement(hm));
        
	}

	@Override
	public String getPnfdAll() {
		
		int cnt = parserDao.getPnfdCount();
		
		Element pnfd_all = new Element("pnfd_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(pnfd_all);
		}
		
		
		List<String> list = parserDao.getPnfdAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("pnfdId", id);
			
			pnfd_all.addContent(getPnfdElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(pnfd_all);
	}
	
	//cp
	private Element getCpElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getCpCount(hm);
		
		if(cnt < 1){
			return new Element("cp");
		}
		
		LinkedHashMap<String, String> resultMap = parserDao.getCpOne(hm);
		
		Element rootElement = new Element("cp");
		
		rootElement.addContent(new Element("id").addContent(resultMap.get("id")));
        rootElement.addContent(new Element("virtual_link_reference").addContent(resultMap.get("virtual_link_reference")));
        rootElement.addContent(new Element("type").addContent(resultMap.get("type")));
        
		return rootElement;
	}
	
	@Override
	public String getCpOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getCpElement(hm));
        
	}

	@Override
	public String getCpAll() {
		
		int cnt = parserDao.getCpCount();
		
		Element cp_all = new Element("cp_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(cp_all);
		}
		
		List<String> list = parserDao.getCpAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("cpId", id);
			
			cp_all.addContent(getCpElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(cp_all);
	}
	
	//nfp
	private Element getNfpElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getNfpCount(hm);
		
		if(cnt < 1){
			return new Element("nfp");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getNfpOne(hm);
		
		List<String> nfp_ref_no= new ArrayList<String>();
		List<String> nfp_id= new ArrayList<String>();
		List<String> policy= new ArrayList<String>();
		List<String> nfp_con= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			nfp_ref_no.add(map.get("nfp_ref_no"));
			nfp_id.add(map.get("nfp_id"));
			policy.add(map.get("policy"));
			nfp_con.add(map.get("nfp_con"));
			
		}

		Element rootElement = new Element("nfp");
		
		rootElement.addContent(new Element("id").addContent(nfp_id.get(0)));
		
		Element policy_el = new Element("policy");
		
		for(String s : policy){
			
			if( !"NULL".equals(s) ){
				policy_el.addContent(s);
        		break;
        	}
			
		}
		
		rootElement.addContent(policy_el);
		
		Element connection_el = new Element("connection");
		
		for(String s : nfp_con){
			
			if( !"NULL".equals(s) ){
				connection_el.addContent(new Element("item").addContent(s));
        	}
			
		}

		rootElement.addContent(connection_el);
		
		return rootElement;
	}
	
	@Override
	public String getNfpOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getNfpElement(hm));
        
	}

	@Override
	public String getNfpAll() {
		
		int cnt = parserDao.getNfpCount();
		
		Element nfp_all = new Element("nfp_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(nfp_all);
		}
		
		
		List<String> list = parserDao.getNfpAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("nfpId", id);
			
			nfp_all.addContent(getNfpElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(nfp_all);
	}
	
	//vnffgd
	private Element getVnffgdElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVnffgdCount(hm);
		
		if(cnt < 1){
			return new Element("vnffgd");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVnffgdOne(hm);
		
		List<String> id= new ArrayList<String>();
		List<String> vendor= new ArrayList<String>();
		List<String> version= new ArrayList<String>();
		List<String> number_of_endpoints= new ArrayList<String>();
		List<String> number_of_virtual_links= new ArrayList<String>();
		List<String> dvl= new ArrayList<String>();
		List<String> nfp_id= new ArrayList<String>();
		List<String> nfp_id_temp= new ArrayList<String>();
		List<String> policy= new ArrayList<String>();
		List<String> nfp_con= new ArrayList<String>();
		List<String> con= new ArrayList<String>();
		List<String> descriptor_version= new ArrayList<String>();
		List<String> constituent= new ArrayList<String>();
		List<String> asvnffgd_security= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			version.add(map.get("version"));
			number_of_endpoints.add(map.get("number_of_endpoints"));
			number_of_virtual_links.add(map.get("number_of_virtual_links"));
			dvl.add(map.get("dvl"));
			nfp_id.add(map.get("nfp_id"));
			policy.add(map.get("policy"));
			nfp_con.add(map.get("nfp_con"));
			con.add(map.get("con"));
			descriptor_version.add(map.get("descriptor_version"));
			constituent.add(map.get("constituent"));
			asvnffgd_security.add(map.get("asvnffgd_security"));
			
		}
		
		
        // create root element
        Element rootElement = new Element("vnffgd");

        
        rootElement.addContent(new Element("id").addContent(id.get(0)));
        rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
        rootElement.addContent(new Element("version").addContent(version.get(0)));
        rootElement.addContent(new Element("number_of_endpoints").addContent(number_of_endpoints.get(0)));
        rootElement.addContent(new Element("number_of_virtual_links").addContent(number_of_virtual_links.get(0)));
        
        Element dependent_virtual_link_el = new Element("dependent_virtual_link");
        
        for(String o : dvl){
        	
        	if( !"NULL".equals(o) ){
        		dependent_virtual_link_el.addContent(new Element("ref").addContent(o));
        	}
        	
        }
        
        rootElement.addContent(dependent_virtual_link_el);
        
        boolean nfp_id_flag = false;
        
        for(String o : nfp_id){
        	if( !"NULL".equals(o) ){
        		nfp_id_flag = true;
        		break;
        	}
        }
        
        if(nfp_id_flag){
        	
        	Element network_forwarding_path_el = new Element("network_forwarding_path");
        	
        	int i = 0;
        	String network_forwarding_path_id_temp;
        	
        	for(String o : nfp_id){
        		
        		if( !"NULL".equals(o) && !nfp_id_temp.contains(o)){
        			
        			nfp_id_temp.add(o);
        			Element network_forwarding_path_info_el = new Element("network_forwarding_path_info");
        			network_forwarding_path_info_el.addContent(new Element("id").addContent(o));
        			network_forwarding_path_info_el.addContent(new Element("policy").addContent(policy.get(i)));
        			
        			network_forwarding_path_id_temp = o;
        			
        			Element connection_el = new Element("connection");
        			
        			int k = 0;
        			for(String n : nfp_con){
        				
        				if(!"NULL".equals(n) && network_forwarding_path_id_temp.equals(nfp_id.get(k))){
        					connection_el.addContent(new Element("item").addContent(n));
        				}
        				
        				k++;
        			}
        			
        			network_forwarding_path_info_el.addContent(connection_el);
        			
        			
        			//network_forwarding_path_el
        			
        			network_forwarding_path_el.addContent(network_forwarding_path_info_el);
            	}
        		
        		i++;
        		
        	}
        	
        	rootElement.addContent(network_forwarding_path_el);
        	
        }
        
        Element connection_point_el = new Element("connection_point");
        
        for(String c : con){
        	
        	if( !"NULL".equals(c)){
        		connection_point_el.addContent(new Element("ref").addContent(c));
        	}
        }
        
        rootElement.addContent(connection_point_el);
        rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));
        
        Element constituent_vnfs_el = new Element("constituent_vnfs");
        
        for(String c : constituent){
        	
        	if( !"NULL".equals(c)){
        		constituent_vnfs_el.addContent(new Element("ref").addContent(c));
        	}
        }
        
        rootElement.addContent(constituent_vnfs_el);
        
        
        for(String c : asvnffgd_security){
        	
        	if( !"NULL".equals(c)){
        		rootElement.addContent(new Element("vnffgd_security").addContent(c));
        	}
        	
        }
		
		return rootElement;
	}
	@Override
	public String getVnffgdOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVnffgdElement(hm));
        
	}

	@Override
	public String getVnffgdAll() {
		
		int cnt = parserDao.getVnffgdCount();
		
		Element vnffgd_all = new Element("vnffgd_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vnffgd_all);
		}
		
		
		List<String> list = parserDao.getVnffgdAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vnffgdId", id);
			
			vnffgd_all.addContent(getVnffgdElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(vnffgd_all);
	}
	
	//vnfc
	private Element getVnfcElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVnfcCount(hm);
		
		if(cnt < 1){
			return new Element("vnfc");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVnfcList(hm);
		
		List<String> vnfc_ref_no= new ArrayList<String>();
		List<String> vnfc_no= new ArrayList<String>();
		List<String> vnfc_id= new ArrayList<String>();
		List<String> vnfc_id_temp= new ArrayList<String>();
		List<String> connection_ref_no= new ArrayList<String>();
		List<String> cp_ref_no= new ArrayList<String>();
		List<String> cp_id= new ArrayList<String>();
		List<String> virtual_link_reference= new ArrayList<String>();
		List<String> type= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			vnfc_ref_no.add(map.get("vnfc_ref_no"));
			vnfc_no.add(map.get("vnfc_no"));
			vnfc_id.add(map.get("vnfc_id"));
			connection_ref_no.add(map.get("connection_ref_no"));
			cp_ref_no.add(map.get("cp_ref_no"));
			cp_id.add(map.get("cp_id"));
			virtual_link_reference.add(map.get("virtual_link_reference"));
			type.add(map.get("type"));
			
		}
		
        Element rootElement = new Element("vnfc");

        int i = 0;
        
        Element connection_point_el_temp = null;
        
        for(String o : vnfc_id){
        	
        	if( !"NULL".equals(o)){
        		
        		if(vnfc_id_temp.contains(o)){
        			
        			Element connection_point_info_el = new Element("connection_point_info");
            		connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
            		connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(virtual_link_reference.get(i)));
            		connection_point_info_el.addContent(new Element("type").addContent(type.get(i)));
            		connection_point_el_temp.addContent(connection_point_info_el);
        			
        		}else{
        		
        			rootElement.addContent(new Element("id").addContent(o));
            		
            		Element connection_point_el = new Element("connection_point");
            		connection_point_el_temp  = connection_point_el;
            		Element connection_point_info_el = new Element("connection_point_info");
            		connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
            		connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(virtual_link_reference.get(i)));
            		connection_point_info_el.addContent(new Element("type").addContent(type.get(i)));
            		connection_point_el.addContent(connection_point_info_el);
            		
            		rootElement.addContent(connection_point_el);
            		
        		}
        		vnfc_id_temp.add(o);
        	}
        	
        	i++;
        }
        
		return rootElement;
	}
	
	@Override
	public String getVnfcOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVnfcElement(hm));
        
	}

	@Override
	public String getVnfcAll() {
		
		int cnt = parserDao.getVnfcCount();
		
		Element vnfc_all = new Element("vnfc_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vnfc_all);
		}
		
		
		List<String> list = parserDao.getVnfcAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vnfcId", id);
			
			vnfc_all.addContent(getVnfcElement(hm));
		}
		
		return CommonXmlUtil.getElementToString(vnfc_all);
	}
	
	//vdu
	private Element getVduElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVduCount(hm);
		
		if(cnt < 1){
			return new Element("vdu");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVduOne(hm);
		
		List<String> vdu_no= new ArrayList<String>();
		List<String> vdu_id= new ArrayList<String>();
		List<String> vm_image= new ArrayList<String>();
		List<String> computation_requirement= new ArrayList<String>();
		List<String> vmre= new ArrayList<String>();
		List<String> vnbr= new ArrayList<String>();
		List<String> life_item= new ArrayList<String>();
		List<String> vdu_constraint= new ArrayList<String>();
		List<String> high_availability= new ArrayList<String>();
		List<String> scale_in_out= new ArrayList<String>();
		List<String> vnfc_id= new ArrayList<String>();
		List<String> vnfc_id_temp= new ArrayList<String>();
		List<String> cp_id= new ArrayList<String>();
		List<String> cp_vlr= new ArrayList<String>();
		List<String> cp_type= new ArrayList<String>();
		List<String> mnt_item= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			vdu_no.add(map.get("vdu_no"));
			vdu_id.add(map.get("vdu_id"));
			vm_image.add(map.get("vm_image"));
			computation_requirement.add(map.get("computation_requirement"));
			vmre.add(map.get("vmre"));
			vnbr.add(map.get("vnbr"));
			life_item.add(map.get("life_item"));
			vdu_constraint.add(map.get("vdu_constraint"));
			high_availability.add(map.get("high_availability"));
			scale_in_out.add(map.get("scale_in_out"));
			vnfc_id.add(map.get("vnfc_id"));
			cp_id.add(map.get("cp_id"));
			cp_vlr.add(map.get("cp_vlr"));
			cp_type.add(map.get("cp_type"));
			mnt_item.add(map.get("mnt_item"));
			
		}
		
        Element rootElement = new Element("vdu");
        
        rootElement.addContent(new Element("id").addContent(vdu_id.get(0)));
        rootElement.addContent(new Element("vm_image").addContent(vm_image.get(0)));
        rootElement.addContent(new Element("computation_requirement").addContent(computation_requirement.get(0)));
        rootElement.addContent(new Element("virtual_memory_resource_element").addContent(vmre.get(0)));
        rootElement.addContent(new Element("virtual_network_bandwidth_resource").addContent(vnbr.get(0)));
        
        Element lifecycle_event_el = new Element("lifecycle_event");
        
        for(String o : life_item){
        	
        	if( !"NULL".equals(o)){
        		lifecycle_event_el.addContent(new Element("item").addContent(o));
        	}
        }
        
        rootElement.addContent(lifecycle_event_el);
        rootElement.addContent(new Element("constraint").addContent(vdu_constraint.get(0)));
        rootElement.addContent(new Element("high_availability").addContent(high_availability.get(0)));
        rootElement.addContent(new Element("scale_in_out").addContent(scale_in_out.get(0)));
        
        Element vnfc_el = new Element("vnfc");
        
        int i = 0;
        
        Element connection_point_el_temp = null;
        
        for(String o : vnfc_id){
        	
        	if( !"NULL".equals(o)){
        		
        		if(vnfc_id_temp.contains(o)){
        			
        			Element connection_point_info_el = new Element("connection_point_info");
            		connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
            		connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(i)));
            		connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(i)));
            		connection_point_el_temp.addContent(connection_point_info_el);
        			
        		}else{
        		
        			Element vnfc_info_el = new Element("vnfc_info");
            		vnfc_info_el.addContent(new Element("id").addContent(o));
            		
            		Element connection_point_el = new Element("connection_point");
            		connection_point_el_temp  = connection_point_el;
            		Element connection_point_info_el = new Element("connection_point_info");
            		connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
            		connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(i)));
            		connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(i)));
            		connection_point_el.addContent(connection_point_info_el);
            		
            		vnfc_info_el.addContent(connection_point_el);
            		
            		vnfc_el.addContent(vnfc_info_el);
            		
        		}
        		
        		vnfc_id_temp.add(o);
        	}
        	
        	i++;
        }

        rootElement.addContent(vnfc_el);
        
        boolean mnt_item_flag = false;
        
        for(String o : mnt_item){
        	
        	if( !"NULL".equals(o)){
        		mnt_item_flag = true;
        		break;
        	}
        }

        if(mnt_item_flag){
        	
        	Element monitoring_parameter_el = new Element("monitoring_parameter");
        	
        	for(String o : mnt_item){
            	
            	if( !"NULL".equals(o)){
            		monitoring_parameter_el.addContent(new Element("item").addContent(o));
            	}
            }
        	
        	rootElement.addContent(monitoring_parameter_el);
        	
        }

		return rootElement;
	}
	
	@Override
	public String getVduOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVduElement(hm));
        
	}

	@Override
	public String getVduAll() {
		
		int cnt = parserDao.getVduCount();
		
		Element vdu_all = new Element("vdu_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vdu_all);
		}
		
		
		List<String> list = parserDao.getVduAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vduId", id);
			
			vdu_all.addContent(getVduElement(hm));
		}
		
		return CommonXmlUtil.getElementToString(vdu_all);
	}
	
	//df
	private Element getDfElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getDfCount(hm);
		
		if(cnt < 1){
			return new Element("df");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getDfOne(hm);
		
		List<String> flavour_id= new ArrayList<String>();
		List<String> flavour_flag= new ArrayList<String>();
		List<String> flavour_key= new ArrayList<String>();
		List<String> cstr_item= new ArrayList<String>();
		List<String> vdu_id= new ArrayList<String>();
		List<String> instance_cnt= new ArrayList<String>();
		List<String> cp_id= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			flavour_id.add(map.get("flavour_id"));
			flavour_flag.add(map.get("flavour_flag"));
			flavour_key.add(map.get("flavour_key"));
			cstr_item.add(map.get("cstr_item"));
			vdu_id.add(map.get("vdu_id"));
			instance_cnt.add(map.get("instance_cnt"));
			cp_id.add(map.get("cp_id"));
			
		}

		Element rootElement = new Element("deployment_flavour");
		
		rootElement.addContent(new Element("id").addContent(flavour_id.get(0)));
		rootElement.addContent(new Element("flavour_key").addContent(flavour_key.get(0)));
		
		Element constraint_el = new Element("constraint");		
		
		for(String s : cstr_item){
			
			if( !"NULL".equals(s) ){
				constraint_el.addContent(new Element("item").addContent(s));
        	}
			
		}
		
		rootElement.addContent(constraint_el);
		
		Element constituent_vdu_el = new Element("constituent_vdu");
		Element constituent_vdu_info_el = new Element("constituent_vdu_info");
		
		
		for(String s : vdu_id){
			
			if( !"NULL".equals(s) ){
				constituent_vdu_info_el.addContent(new Element("vdu_reference").addContent(s));
				break;
        	}
			
		}
		
		for(String s : instance_cnt){
			
			if( !"NULL".equals(s) ){
				constituent_vdu_info_el.addContent(new Element("number_of_instances").addContent(s));
				break;
        	}
			
		}
		
		Element constituent_vnfc_el = new Element("constituent_vnfc");
		for(String s : cp_id){
			
			if( !"NULL".equals(s) ){
				constituent_vnfc_el.addContent(new Element("ref").addContent(s));				
        	}
			
		}
		
		constituent_vdu_info_el.addContent(constituent_vnfc_el);
		constituent_vdu_el.addContent(constituent_vdu_info_el);
		

		rootElement.addContent(constituent_vdu_el);
		
		return rootElement;
	}
	
	@Override
	public String getDfOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getDfElement(hm));
        
	}

	@Override
	public String getDfAll() {
		
		int cnt = parserDao.getDfCount();
		
		Element df_all = new Element("deployment_flavour_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(df_all);
		}
		
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getDfAllId();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("flavour_id", map.get("flavour_id"));
			hm.put("flavour_flag", map.get("flavour_flag"));
			
			df_all.addContent(getDfElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(df_all);
	}
	
	//vnfd
	private Element getVnfdElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVnfdCount(hm);
		
		if(cnt < 1){
			return new Element("vnfd");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVnfdOne(hm);
		
		List<String> vnfd_id= new ArrayList<String>();		//1
		List<String> vnfd_vendor= new ArrayList<String>();		//2
		List<String> vnfd_no= new ArrayList<String>();		//3
		List<String> version_no= new ArrayList<String>();		//4
		List<String> descriptor_version= new ArrayList<String>();		//5
		List<String> vdu_id= new ArrayList<String>();		//6
		List<String> vdu_id_temp= new ArrayList<String>();
		List<String> vm_image= new ArrayList<String>();		//7
		List<String> computation_requirement= new ArrayList<String>();		//8
		List<String> vmre= new ArrayList<String>();		//9
		List<String> vnbr= new ArrayList<String>();		//10

		List<String> life_item= new ArrayList<String>();		//11
		List<String> vdu_constraint= new ArrayList<String>();		//12
		List<String> high_availability= new ArrayList<String>();		//13
		List<String> scale_in_out= new ArrayList<String>();		//14
		List<String> vnfc_id= new ArrayList<String>();		//15
		List<String> vnfc_id_temp= new ArrayList<String>();
		List<String> cp_id= new ArrayList<String>();		//16
		List<String> cp_vlr= new ArrayList<String>();		//17
		List<String> cp_type= new ArrayList<String>();		//18
		List<String> mnt_item= new ArrayList<String>();		//19
		List<String> vld_no= new ArrayList<String>();		//20

		List<String> vld_id= new ArrayList<String>();		//21
		List<String> vld_id_temp= new ArrayList<String>();
		List<String> vld_vendor= new ArrayList<String>();		//22
		List<String> vld_version= new ArrayList<String>();		//23
		List<String> number_of_endpoints= new ArrayList<String>();		//24
		List<String> root_requirement= new ArrayList<String>();		//25
		List<String> leaf_requirement= new ArrayList<String>();		//26
		List<String> vld_qos= new ArrayList<String>();		//27
		List<String> test_access= new ArrayList<String>();		//28
		List<String> vld_con= new ArrayList<String>();		//29
		List<String> vld_con_type= new ArrayList<String>();		//30

		List<String> vld_security= new ArrayList<String>();		//31
		List<String> vnfd_cp_id= new ArrayList<String>();		//32
		List<String> vnfd_cp_id_temp= new ArrayList<String>();
		List<String> vnfd_cp_vlr= new ArrayList<String>();		//33
		List<String> vnfd_cp_type= new ArrayList<String>();		//34
		List<String> vnfd_life_item= new ArrayList<String>();		//35
		List<String> dep_item= new ArrayList<String>();		//36
		List<String> vnfd_mnt_item= new ArrayList<String>();		//37
		List<String> flavour_id= new ArrayList<String>();		//38
		List<String> flavour_id_temp= new ArrayList<String>();		//38
		List<String> flavour_key= new ArrayList<String>();		//39
		List<String> cstr_item= new ArrayList<String>();		//40

		List<String> cstr_vdu_id= new ArrayList<String>();		//41
		List<String> cstr_vdu_id_temp= new ArrayList<String>();
		List<String> instance_cnt= new ArrayList<String>();		//42
		List<String> cstr_vnfc_id= new ArrayList<String>();		//43
		List<String> policy_item= new ArrayList<String>();		//44
		List<String> manifest_file= new ArrayList<String>();		//45
		List<String> seq_item= new ArrayList<String>();		//46
		
		for(LinkedHashMap<String, String> map : resultList){
			
			vnfd_id.add(map.get("vnfd_id"));
			vnfd_vendor.add(map.get("vnfd_vendor"));
			vnfd_no.add(map.get("vnfd_no"));
			version_no.add(map.get("version_no"));
			descriptor_version.add(map.get("descriptor_version"));
			vdu_id.add(map.get("vdu_id"));
			vm_image.add(map.get("vm_image"));
			computation_requirement.add(map.get("computation_requirement"));
			vmre.add(map.get("vmre"));
			vnbr.add(map.get("vnbr"));

			life_item.add(map.get("life_item"));
			vdu_constraint.add(map.get("vdu_constraint"));
			high_availability.add(map.get("high_availability"));
			scale_in_out.add(map.get("scale_in_out"));
			vnfc_id.add(map.get("vnfc_id"));
			cp_id.add(map.get("cp_id"));
			cp_vlr.add(map.get("cp_vlr"));
			cp_type.add(map.get("cp_type"));
			mnt_item.add(map.get("mnt_item"));
			vld_no.add(map.get("vld_no"));

			vld_id.add(map.get("vld_id"));
			vld_vendor.add(map.get("vld_vendor"));
			vld_version.add(map.get("vld_version"));
			number_of_endpoints.add(map.get("number_of_endpoints"));
			root_requirement.add(map.get("root_requirement"));
			leaf_requirement.add(map.get("leaf_requirement"));
			vld_qos.add(map.get("vld_qos"));
			test_access.add(map.get("test_access"));
			vld_con.add(map.get("vld_con"));
			vld_con_type.add(map.get("vld_con_type"));

			vld_security.add(map.get("vld_security"));
			vnfd_cp_id.add(map.get("vnfd_cp_id"));
			vnfd_cp_vlr.add(map.get("vnfd_cp_vlr"));
			vnfd_cp_type.add(map.get("vnfd_cp_type"));
			vnfd_life_item.add(map.get("vnfd_life_item"));
			dep_item.add(map.get("dep_item"));
			vnfd_mnt_item.add(map.get("vnfd_mnt_item"));
			flavour_id.add(map.get("flavour_id"));
			flavour_key.add(map.get("flavour_key"));
			cstr_item.add(map.get("cstr_item"));

			cstr_vdu_id.add(map.get("cstr_vdu_id"));
			instance_cnt.add(map.get("instance_cnt"));
			cstr_vnfc_id.add(map.get("cstr_vnfc_id"));
			policy_item.add(map.get("policy_item"));
			manifest_file.add(map.get("manifest_file"));
			seq_item.add(map.get("seq_item"));
			
		}

		Element rootElement = new Element("vnfd");
		
		rootElement.addContent(new Element("id").addContent(vnfd_id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vnfd_vendor.get(0)));
		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));
		
		Element vdu_el = new Element("vdu");
		
		// vdu start
		int i = 0;
		for(String s : vdu_id){
			
			if( !"NULL".equals(s) && !vdu_id_temp.contains(s)){
				
				Element vdu_info_el = new Element("vdu_info");
				vdu_info_el.addContent(new Element("id").addContent(s));
				vdu_info_el.addContent(new Element("vm_image").addContent(vm_image.get(i)));
				vdu_info_el.addContent(new Element("computation_requirement").addContent(computation_requirement.get(i)));
				vdu_info_el.addContent(new Element("virtual_memory_resource_element").addContent(vmre.get(i)));
				vdu_info_el.addContent(new Element("virtual_network_bandwidth_resource").addContent(vnbr.get(i)));
				
				Element lifecycle_event_el = new Element("lifecycle_event");
				
				int j = 0;
				for(String o : life_item){
					
					if(!"NULL".equals(o) && s.equals(vdu_id.get(j))){
						lifecycle_event_el.addContent(new Element("item").addContent(o));
					}
					
					j++;
				}
				
				vdu_info_el.addContent(lifecycle_event_el);
				
				vdu_info_el.addContent(new Element("constraint").addContent(vdu_constraint.get(i)));
				vdu_info_el.addContent(new Element("high_availability").addContent(high_availability.get(i)));
				vdu_info_el.addContent(new Element("scale_in_out").addContent(scale_in_out.get(i)));
				
				
				Element vnfc_el = new Element("vnfc");
				
				int k = 0;
				
				Element connection_point_el = null;
				
				for(String o : vnfc_id){
					
					if(!"NULL".equals(o) && s.equals(vdu_id.get(k))){
						
						
						
						if(vnfc_id_temp.contains(o)){
							
							Element connection_point_info_el = new Element("connection_point_info");
							
							connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(k)));
							connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(k)));
							connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(k)));
							
							connection_point_el.addContent(connection_point_info_el);
							
						}else{
							
							Element vnfc_info_el = new Element("vnfc_info");
							vnfc_info_el.addContent(new Element("id").addContent(o));
							
							connection_point_el = new Element("connection_point");
							Element connection_point_info_el = new Element("connection_point_info");
							
							connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(k)));
							connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(k)));
							connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(k)));
							
							connection_point_el.addContent(connection_point_info_el);
							vnfc_info_el.addContent(connection_point_el);
							vnfc_el.addContent(vnfc_info_el);
							
						}
						
						vnfc_id_temp.add(o);
						
					}
					
					k++;
				}
				
				vdu_info_el.addContent(vnfc_el);
				
				Element monitoring_parameter_el = new Element("monitoring_parameter");
				
				int u = 0;
				for(String o : mnt_item){
					
					if(!"NULL".equals(o) && s.equals(vdu_id.get(u))){
						
						monitoring_parameter_el.addContent(new Element("item").addContent(o));

					}
					
					u++;
				}
				
				vdu_info_el.addContent(monitoring_parameter_el);
				
				vdu_el.addContent(vdu_info_el);
				
				
				vdu_id_temp.add(s);
			}
			
			i++;
		}
		
		rootElement.addContent(vdu_el);
		
		// vdu end
		
		// virtual_link start
		
		Element virtual_link_el = new Element("virtual_link");
		
		i = 0;
		for(String s : vld_id){
			
			if(!"NULL".equals(s) && !vld_id_temp.contains(s)){
				
				Element virtual_link_info_el = new Element("virtual_link_info");
				virtual_link_info_el.addContent(new Element("id").addContent(s));
				
				virtual_link_info_el.addContent(new Element("connectivity_type").addContent(vld_con_type.get(i)));
				
				Element connection_point_reference_el = new Element("connection_point_reference");
				
				int k = 0;
				for(String q : vld_con){
					
					if(!"NULL".equals(q) && s.equals(vld_id.get(k)) ){
						connection_point_reference_el.addContent(new Element("ref").addContent(q));
						
					}
					k++;
				}
				
				virtual_link_info_el.addContent(connection_point_reference_el);
				
				virtual_link_info_el.addContent(new Element("root_requirement").addContent(root_requirement.get(i)));
				virtual_link_info_el.addContent(new Element("leaf_requirement").addContent(leaf_requirement.get(i)));
				
				Element qos_el = new Element("qos");
				
				k = 0;
				for(String q : vld_qos){
					
					if(!"NULL".equals(q) && s.equals(vld_id.get(k)) ){
						qos_el.addContent(new Element("item").addContent(q));
						
					}
					k++;
				}
				
				//virtual_link_info_el.addContent(new Element("qos").addContent(new Element("item").addContent(vld_qos.get(i))));
				virtual_link_info_el.addContent(qos_el);
				virtual_link_info_el.addContent(new Element("test_access").addContent(test_access.get(i)));
				
				virtual_link_el.addContent(virtual_link_info_el);
				
				vld_id_temp.add(s);
			}
			
			i++;
		}
		
		rootElement.addContent(virtual_link_el);
		
		// virtual_link end
		// connection_point start
		
		Element connection_point_el = new Element("connection_point");
		
		i = 0;
		for(String s : vnfd_cp_id){
			
			if(!"NULL".equals(s) && !vnfd_cp_id_temp.contains(s)){
				
				Element connection_point_info_el = new Element("connection_point_info");
				connection_point_info_el.addContent(new Element("id").addContent(s));
				connection_point_info_el.addContent(new Element("virtual_link_reference").addContent(vnfd_cp_vlr.get(i)));
				connection_point_info_el.addContent(new Element("type").addContent(vnfd_cp_type.get(i)));
				
				connection_point_el.addContent(connection_point_info_el);
				vnfd_cp_id_temp.add(s);
			}
			
			i++;
		}
		
		rootElement.addContent(connection_point_el);
		// connection_point end
		// lifecycle_event start
		
		Element lifecycle_event_el = new Element("lifecycle_event");
		
		for(String o : vnfd_life_item){
			
			if(!"NULL".equals(o)){
				lifecycle_event_el.addContent(new Element("item").addContent(o));
			}
			
		}
		
		rootElement.addContent(lifecycle_event_el);
		
		// lifecycle_event end
		// dependency start
		
		Element dependency_el = new Element("dependency");
		
		for(String o : dep_item){
			
			if(!"NULL".equals(o)){
				dependency_el.addContent(new Element("item").addContent(o));
			}
			
		}
		
		rootElement.addContent(dependency_el);
		
		// dependency end
		// monitoring_parameter start
		
		Element monitoring_parameter_el = new Element("monitoring_parameter");
		
		for(String o : vnfd_mnt_item){
			
			if(!"NULL".equals(o)){
				monitoring_parameter_el.addContent(new Element("item").addContent(o));
			}
			
		}
		
		rootElement.addContent(monitoring_parameter_el);
		
		// monitoring_parameter end
		
		// deployment_flavour start
		
		Element deployment_flavour_el = new Element("deployment_flavour");
		
		i = 0;
		for(String o : flavour_id){
			
			if(!"NULL".equals(o) && !flavour_id_temp.contains(o)){
				Element deployment_flavour_info_el = new Element("deployment_flavour_info");
				deployment_flavour_info_el.addContent(new Element("id").addContent(o));
				deployment_flavour_info_el.addContent(new Element("flavour_key").addContent(flavour_key.get(i)));
				
				Element constraint_el = new Element("constraint");
				
				int q = 0;
				for(String k : cstr_item){
					
					if(!"NULL".equals(k) && o.equals(flavour_id.get(q))){
						
						constraint_el.addContent(new Element("item").addContent(k));
					}
					q++;
				}
				
				deployment_flavour_info_el.addContent(constraint_el);
				
				
				Element constituent_vdu_el = new Element("constituent_vdu");
				
				q = 0;
				for(String k : cstr_vdu_id){
					
					if(!"NULL".equals(k) && o.equals(flavour_id.get(q)) && !cstr_vdu_id_temp.contains(o+k)){
						Element constituent_vdu_info_el = new Element("constituent_vdu_info");
						
						constituent_vdu_info_el.addContent(new Element("vdu_reference").addContent(k));
						constituent_vdu_info_el.addContent(new Element("number_of_instances").addContent(instance_cnt.get(q)));
						
						Element constituent_vnfc_el = new Element("constituent_vnfc");
						
						int b = 0;
						for(String m : cstr_vnfc_id){
							
							if(!"NULL".equals(m) && o.equals(flavour_id.get(b))){
								
								constituent_vnfc_el.addContent(new Element("ref").addContent(m));
							}
							
							b++;
						}
						
						constituent_vdu_info_el.addContent(constituent_vnfc_el);
						
						constituent_vdu_el.addContent(constituent_vdu_info_el);
						cstr_vdu_id_temp.add(o+k);
					}
					
					q++;
				}
				
				deployment_flavour_info_el.addContent(constituent_vdu_el);
				
				deployment_flavour_el.addContent(deployment_flavour_info_el);
				flavour_id_temp.add(o);
			}
			
			i++;
			
		}
		
		rootElement.addContent(deployment_flavour_el);
		
		// deployment_flavour end
		
		// auto_scale_policy start
		
		Element auto_scale_policy_el = new Element("auto_scale_policy");
		
		for(String o : policy_item){
			
			if(!"NULL".equals(o)){
				
				auto_scale_policy_el.addContent(new Element("item").addContent(o));
				
			}
			
		}
		
		rootElement.addContent(auto_scale_policy_el);
		
		// auto_scale_policy end
		
		rootElement.addContent(new Element("manifest_file").addContent(manifest_file.get(0)));
		
		
		Element manifest_file_security_el = new Element("manifest_file_security");
		
		for(String o : seq_item){
					
			if(!"NULL".equals(o)){
				
				manifest_file_security_el.addContent(new Element("item").addContent(o));
				
			}
			
		}
		
		rootElement.addContent(manifest_file_security_el);

		return rootElement;
	}
	
	@Override
	public String getVnfdOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVnfdElement(hm));
        
	}

	@Override
	public String getVnfdAll() {
		
		int cnt = parserDao.getVnfdCount();
		
		Element vnfd_all = new Element("vnfd_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vnfd_all);
		}
		
		
		List<String> list = parserDao.getVnfdAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vnfdId", id);
			
			vnfd_all.addContent(getVnfdElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(vnfd_all);
	}
	
	//sdf
	private Element getSdfElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getSdfCount(hm);
		
		if(cnt < 1){
			return new Element("sdf");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getSdfOne(hm);
		
		List<String> convnf_ref_no= new ArrayList<String>();
		List<String> convnf_no= new ArrayList<String>();
		List<String> sdf_id= new ArrayList<String>();
		List<String> flavour_key= new ArrayList<String>();
		List<String> convnf_id= new ArrayList<String>();
		
		List<String> flavour_id= new ArrayList<String>();
		List<String> rddc_model= new ArrayList<String>();
		List<String> affinity= new ArrayList<String>();
		List<String> vnf_cap= new ArrayList<String>();
		List<String> vnf_instance_cnt= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			convnf_ref_no.add(map.get("convnf_ref_no"));
			convnf_no.add(map.get("convnf_no"));
			sdf_id.add(map.get("sdf_id"));
			flavour_key.add(map.get("sdf_flavour_key"));
			convnf_id.add(map.get("vnf_ref_id")); //convnf_id.add(map.get("convnf_id"));
			
			flavour_id.add(map.get("vnf_flavour_id"));
			rddc_model.add(map.get("rddc_model"));
			affinity.add(map.get("affinity"));
			vnf_cap.add(map.get("vnf_cap"));
			vnf_instance_cnt.add(map.get("vnf_instance_cnt"));
			
		}
		
		Element rootElement = new Element("service_deployment_flavour");
		
		rootElement.addContent(new Element("id").addContent(sdf_id.get(0)));
		rootElement.addContent(new Element("flavour_key").addContent(flavour_key.get(0)));
		
		Element constituent_vnf_el = new Element("constituent_vnf");
		
		int i = 0;
		for(String s : convnf_id){
			
			Element constituent_vnf_info_el = new Element("constituent_vnf_info");
			constituent_vnf_info_el.addContent(new Element("vnf_reference").addContent(s));
			constituent_vnf_info_el.addContent(new Element("vnf_flavour_id_reference").addContent(flavour_id.get(i)));
			constituent_vnf_info_el.addContent(new Element("redundancy_model").addContent(rddc_model.get(i)));
			constituent_vnf_info_el.addContent(new Element("affinity").addContent(affinity.get(i)));
			constituent_vnf_info_el.addContent(new Element("vnf_cap").addContent(vnf_cap.get(i)));
			constituent_vnf_info_el.addContent(new Element("number_of_instances").addContent(vnf_instance_cnt.get(i)));
			
			constituent_vnf_el.addContent(constituent_vnf_info_el);
			i++;
		}
		
		rootElement.addContent(constituent_vnf_el);
        
		return rootElement;
	}
	
	@Override
	public String getSdfOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getSdfElement(hm));
        
	}

	@Override
	public String getSdfAll() {
		
		
		int cnt = parserDao.getSdfCount();
		
		Element sdf_all = new Element("service_deployment_flavour_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(sdf_all);
		}
		
		List<String> list = parserDao.getSdfAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("sdfId", id);
			
			sdf_all.addContent(getSdfElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(sdf_all);
	}
	
	//nsd
	private Element getNsdElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getNsdCount(hm);
		
		if(cnt < 1){
			return new Element("nsd");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getNsdOne(hm);
		
		List<String> nsd_id= new ArrayList<String>();
		List<String> nsd_vendor= new ArrayList<String>();
		List<String> nsd_version= new ArrayList<String>();
		List<String> version_no= new ArrayList<String>();
		List<String> vnfd_item= new ArrayList<String>();
		List<String> vnffgd_item= new ArrayList<String>();

		List<String> vld_item= new ArrayList<String>();
		List<String> nsd_life_item= new ArrayList<String>();
		List<String> nsd_vnfdep_item= new ArrayList<String>();
		List<String> nsd_mnt_item= new ArrayList<String>();
		List<String> sdf_id= new ArrayList<String>();
		List<String> sdf_id_temp= new ArrayList<String>();

		List<String> sdf_flavour_key= new ArrayList<String>();
		List<String> convnf_ref_no= new ArrayList<String>();
		List<String> vnf_ref_id= new ArrayList<String>();
		List<String> vnf_flavour_id= new ArrayList<String>();
		List<String> rddc_model= new ArrayList<String>();

		List<String> affinity= new ArrayList<String>();
		List<String> vnf_cap= new ArrayList<String>();
		List<String> vnf_instance_cnt= new ArrayList<String>();
		List<String> nsd_policy_item= new ArrayList<String>();
		List<String> nsd_cp_id= new ArrayList<String>();

		List<String> nsd_cp_vlr= new ArrayList<String>();
		List<String> nsd_cp_type= new ArrayList<String>();
		List<String> pnfd_item= new ArrayList<String>();
		List<String> nsd_seq_item= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			nsd_id.add(map.get("nsd_id"));
			nsd_vendor.add(map.get("nsd_vendor"));
			nsd_version.add(map.get("nsd_version"));
			
			version_no.add(map.get("version_no"));
			vnfd_item.add(map.get("vnfd_item"));
			vnffgd_item.add(map.get("vnffgd_item"));

			vld_item.add(map.get("vld_item"));
			nsd_life_item.add(map.get("nsd_life_item"));
			nsd_vnfdep_item.add(map.get("nsd_vnfdep_item"));
			nsd_mnt_item.add(map.get("nsd_mnt_item"));
			sdf_id.add(map.get("sdf_id"));

			sdf_flavour_key.add(map.get("sdf_flavour_key"));
			convnf_ref_no.add(map.get("convnf_ref_no"));
			vnf_ref_id.add(map.get("vnf_ref_id"));
			vnf_flavour_id.add(map.get("vnf_flavour_id"));
			rddc_model.add(map.get("rddc_model"));

			affinity.add(map.get("affinity"));
			vnf_cap.add(map.get("vnf_cap"));
			vnf_instance_cnt.add(map.get("vnf_instance_cnt"));
			nsd_policy_item.add(map.get("nsd_policy_item"));
			nsd_cp_id.add(map.get("nsd_cp_id"));

			nsd_cp_vlr.add(map.get("nsd_cp_vlr"));
			nsd_cp_type.add(map.get("nsd_cp_type"));
			pnfd_item.add(map.get("pnfd_item"));
			nsd_seq_item.add(map.get("nsd_seq_item"));
			
		}
		
		Element rootElement = new Element("nsd");
		
		rootElement.addContent(new Element("id").addContent(nsd_id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(nsd_vendor.get(0)));
		rootElement.addContent(new Element("version").addContent(nsd_version.get(0)));
		
		Element vnfd_item_el = new Element("vnfd");
		
		for(String s : vnfd_item){
			
			if(!"NULL".equals(s)){
				vnfd_item_el.addContent(new Element("ref").addContent(s));
			}
			
		}
		
		rootElement.addContent(vnfd_item_el);
		
		Element vnffgd_item_el = new Element("vnffgd");
		
		for(String s : vnffgd_item){
			
			if(!"NULL".equals(s)){
				vnffgd_item_el.addContent(new Element("ref").addContent(s));
			}
			
		}
		
		rootElement.addContent(vnffgd_item_el);
		
		Element vld_item_el = new Element("vld");
		
		for(String s : vld_item){
			
			if(!"NULL".equals(s)){
				vld_item_el.addContent(new Element("ref").addContent(s));
			}
			
		}
		
		rootElement.addContent(vld_item_el);
		
		Element nsd_life_item_el = new Element("lifecycle_event");
		
		for(String s : nsd_life_item){
			
			if(!"NULL".equals(s)){
				nsd_life_item_el.addContent(new Element("item").addContent(s));
			}
			
		}
		
		rootElement.addContent(nsd_life_item_el);
		
		Element nsd_vnfdep_item_el = new Element("vnf_dependency");
		
		for(String s : nsd_vnfdep_item){
			
			if(!"NULL".equals(s)){
				nsd_vnfdep_item_el.addContent(new Element("item").addContent(s));
			}
			
		}
		
		rootElement.addContent(nsd_vnfdep_item_el);
		
		Element nsd_mnt_item_el = new Element("monitoring_parameter");
		
		for(String s : nsd_mnt_item){
			
			if(!"NULL".equals(s)){
				nsd_mnt_item_el.addContent(new Element("item").addContent(s));
			}
			
		}
		
		rootElement.addContent(nsd_mnt_item_el);
		
		Element service_deployment_flavour_el = new Element("service_deployment_flavour");
		
		int i = 0;
		for(String s : sdf_id){
			
			if(!"NULL".equals(s) && !sdf_id_temp.contains(s)){
				
				Element service_deployment_flavour_info_el = new Element("service_deployment_flavour_info");
				service_deployment_flavour_info_el.addContent(new Element("id").addContent(s));
				service_deployment_flavour_info_el.addContent(new Element("flavour_key").addContent(sdf_flavour_key.get(i)));
				
				Element constituent_vnf_el = new Element("constituent_vnf");
				
				int k = 0;
				
				for(String o : vnf_ref_id){
					
					if(!"NULL".equals(o) && s.equals(sdf_id.get(k))){
						
						Element constituent_vnf_info_el = new Element("constituent_vnf_info");
						constituent_vnf_info_el.addContent(new Element("vnf_reference").addContent(o));
						constituent_vnf_info_el.addContent(new Element("vnf_flavour_id_reference").addContent(vnf_flavour_id.get(k)));
						constituent_vnf_info_el.addContent(new Element("redundancy_model").addContent(rddc_model.get(k)));
						constituent_vnf_info_el.addContent(new Element("affinity").addContent(affinity.get(k)));
						constituent_vnf_info_el.addContent(new Element("capability").addContent(vnf_cap.get(k)));
						constituent_vnf_info_el.addContent(new Element("number_of_instances").addContent(vnf_instance_cnt.get(k)));
						
						constituent_vnf_el.addContent(constituent_vnf_info_el);
	
					}
					
					k++;
				}
				service_deployment_flavour_info_el.addContent(constituent_vnf_el);
				
				service_deployment_flavour_el.addContent(service_deployment_flavour_info_el);
				
				sdf_id_temp.add(s);
			}
			
			i++;
		}
		
		rootElement.addContent(service_deployment_flavour_el);
		
		Element auto_scale_policy_el = new Element("auto_scale_policy");
		
		for(String s : nsd_policy_item){
			
			if(!"NULL".equals(s)){
				auto_scale_policy_el.addContent(new Element("item").addContent(s));
			}
			
		}
		
		rootElement.addContent(auto_scale_policy_el);
		
		Element connection_point_el = new Element("connection_point");
		
		int q = 0;
		for(String s : nsd_cp_id){
			
			if(!"NULL".equals(s)){
				Element connection_point_info_el = new Element("connection_point_info");
				
				connection_point_info_el.addContent(new Element("id").addContent(s));
				connection_point_info_el.addContent(new Element("type").addContent(nsd_cp_type.get(q)));
				
				connection_point_el.addContent(connection_point_info_el);
			}
			q++;
			
		}
		
		rootElement.addContent(connection_point_el);
		
		Element pnfd_el = new Element("pnfd");
		
		for(String s : pnfd_item){
			
			if(!"NULL".equals(s)){
				
				pnfd_el.addContent(new Element("ref").addContent(s));
			}
			
		}
		
		rootElement.addContent(pnfd_el);

		
		for(String s : nsd_seq_item){
			
			if(!"NULL".equals(s)){
				
				rootElement.addContent(new Element("nsd_security").addContent(s));
				break;
			}
			
		}
        
		return rootElement;
	}
	
	@Override
	public String getNsdOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getNsdElement(hm));
        
	}

	@Override
	public String getNsdAll() {
		
		
		int cnt = parserDao.getNsdCount();
		
		Element nsd_all = new Element("nsd_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(nsd_all);
		}
		
		List<String> list = parserDao.getNsdAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("nsdId", id);
			
			nsd_all.addContent(getNsdElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(nsd_all);
	}
	
	//vnfrtd
	private Element getVnfrtdElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getVnfrtdCount(hm);
		
		if(cnt < 1){
			return new Element("vnfrtd");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getVnfrtdOne(hm);
		
		//List<String> vnfrtd_ref_no= new ArrayList<String>();
		List<String> vnfrtd_id= new ArrayList<String>();
		List<String> mnt_ref_no= new ArrayList<String>();
		List<String> vnfrtd_mnt_item= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map :  resultList){
			
			//vnfrtd_ref_no.add(map.get("vnfrtd_ref_no"));
			vnfrtd_id.add(map.get("vnfrtd_id"));
			mnt_ref_no.add(map.get("mnt_ref_no"));
			vnfrtd_mnt_item.add(map.get("vnfrtd_mnt_item"));
			
		}
		
		Element rootElement = new Element("vnfrtd");
		
		rootElement.addContent(new Element("id").addContent(vnfrtd_id.get(0)));
		
		Element monitoring_parameter_el = new Element("monitoring_parameter");
		
		for(String s : vnfrtd_mnt_item){
			monitoring_parameter_el.addContent(new Element("item").addContent(s));
		}
		
		rootElement.addContent(monitoring_parameter_el);
		
		return rootElement;
	}
	
	@Override
	public String getVnfrtdOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getVnfrtdElement(hm));
        
	}

	@Override
	public String getVnfrtdAll() {
		
		
		int cnt = parserDao.getVnfrtdCount();
		
		Element vnfrtd_all = new Element("vnfrtd_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(vnfrtd_all);
		}
		
		List<String> list = parserDao.getVnfrtdAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vnfrtdId", id);
			
			vnfrtd_all.addContent(getVnfrtdElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(vnfrtd_all);
	}
	
	//nfvi(기존nfvid) 
	/*private Element getNfvidElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getNfvidCount(hm);
		
		if(cnt < 1){
			return new Element("nfvi");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getNfvidOne(hm);
		
		List<String> nfvid_ref_no= new ArrayList<String>();
		List<String> nfvid_no= new ArrayList<String>();
		List<String> nfvid_id= new ArrayList<String>();
		List<String> nfvidc_no= new ArrayList<String>();
		List<String> nfvidc_id= new ArrayList<String>();
		List<String> nfvidc_id_temp= new ArrayList<String>();
		
		List<String> mnt_ref_no= new ArrayList<String>();
		List<String> func_ref_no= new ArrayList<String>();
		List<String> mnt_no= new ArrayList<String>();
		List<String> mnt_item= new ArrayList<String>();
		List<String> function_no= new ArrayList<String>();
		
		List<String> ver_item= new ArrayList<String>();
		List<String> ver_id= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			nfvid_ref_no.add(map.get("nfvid_ref_no"));
			nfvid_no.add(map.get("nfvid_no"));
			nfvid_id.add(map.get("nfvid_id"));
			nfvidc_no.add(map.get("nfvidc_no"));
			nfvidc_id.add(map.get("nfvidc_id"));
			
			mnt_ref_no.add(map.get("mnt_ref_no"));
			func_ref_no.add(map.get("func_ref_no"));
			mnt_no.add(map.get("mnt_no"));
			mnt_item.add(map.get("mnt_item"));
			function_no.add(map.get("function_no"));
			
			ver_item.add(map.get("ver_item"));
			ver_id.add(map.get("ver_id"));
		}
		
		Element rootElement = new Element("nfvi");
		
		rootElement.addContent(new Element("id").addContent(nfvid_id.get(0)));
		
		Element virtual_resource_el = new Element("virtual_resource");
		
		int i = 0;
		for(String s : mnt_item){
			
			if( ! "NULL".equals(s) && "NULL".equals(nfvidc_id.get(i))){
				virtual_resource_el.addContent(new Element("item").addContent(s));
			}
			
			i++;
		}
		
		rootElement.addContent(virtual_resource_el);
		
		Element component_el = new Element("component");
		
		i = 0;
		for(String s : nfvidc_id){
			
			if( ! "NULL".equals(s) && !nfvidc_id_temp.contains(s)){
				
				Element component_info_el = new Element("component_info");
				component_info_el.addContent(new Element("id").addContent(s));
				
				Element resource_el = new Element("resource");
				
				int q = 0;
				for(String k : mnt_item){
					
					if(! "NULL".equals(k) && s.equals(nfvidc_id.get(q))){
						resource_el.addContent(new Element("item").addContent(k));
					}
					
					q++;
				}
				
				component_info_el.addContent(resource_el);
				
				Element vnf_el = new Element("vnf");
				
				q = 0;
				for(String k : ver_id){
					
					if(! "NULL".equals(k) && s.equals(nfvidc_id.get(q))){
						
						Element vnf_info_el = new Element("vnf_info");
						
						vnf_info_el.addContent(new Element("id").addContent(k));
						vnf_info_el.addContent(new Element("status").addContent(ver_item.get(q)));
						
						vnf_el.addContent(vnf_info_el);
					}
					
					q++;
				}

				
				component_info_el.addContent(vnf_el);
				
				component_el.addContent(component_info_el);
				
				nfvidc_id_temp.add(s);
			}
			
			i++;
		}
		
		rootElement.addContent(component_el);
		
		
		
		
		return rootElement;
	}
	
	@Override
	public String getNfvidOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getNfvidElement(hm));
        
	}

	@Override
	public String getNfvidAll() {
		
		
		int cnt = parserDao.getNfvidCount();
		
		Element nfvi_all = new Element("nfvi_all");
		
		if(cnt < 1){
			return CommonXmlUtil.getElementToString(nfvi_all);
		}
		
		List<String> list = parserDao.getNfvidAllId();
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("nfvidId", id);
			
			nfvi_all.addContent(getNfvidElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(nfvi_all);
	}*/

	
	//TODO NFVI 신규 구현.
	private Element getNfvidElement(HashMap<String, String> hm) {
		
		//TODO UI단에서 예외처리 하려 함. 추후 수정 가능.
		//int cnt = parserDao.getNfvidCount(hm);
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getNfvidOne(hm);
		
		if(resultList.size() < 1){
			return new Element("nfvi");
		}		
		
		List<String> nfvi_no= new ArrayList<String>();
		List<String> nfvi_id= new ArrayList<String>();
		List<String> image_no= new ArrayList<String>();
		List<String> image_name= new ArrayList<String>();

        
		for(LinkedHashMap<String, String> map : resultList){
			
			nfvi_no.add(map.get("nfvi_no"));
			nfvi_id.add(map.get("nfvi_id"));
			image_no.add(map.get("image_no"));
			image_name.add(map.get("image_name"));
		}
		
		
		Element rootElement = new Element("nfvi");
		
		rootElement.addContent(new Element("id").addContent(nfvi_id.get(0)));
		
		
		//TODO 추후 구현. 합을 구해야 함.
		Element virtual_resource_el = new Element("virtual_resource");
		
		int i = 0;
		
		//Hypervisor 리스트를 가져온다. listServers는 instance가 존재하지 않는 경우 Hypervisor가 표출되지 않기 때문.
		List<? extends Hypervisor> HypervisorList= computeAdapter.hypervisors();

		//openstack serverlist를 가져온다.
		List<? extends Server> serverResult = computeAdapter.listServers();	
		
		logger.debug("######Server List######");
		logger.debug(serverResult.toString());
		logger.debug("######Hypervisor List#######");
		logger.debug(HypervisorList.toString());
		logger.debug("#####################");
		

		
		//virtual resource 계산 부분. hypervisor의 자원들을 합산한다.
		int cpu = 0;
		int usage_cpu = 0;
		int memory = 0;
		int usage_memory = 0;
		int disk = 0;
		int usage_disk = 0;
		int network = 0;
		int usage_network = 0;
		
		for (int j = 0; j < HypervisorList.size(); j++) {
			cpu += HypervisorList.get(j).getVirtualCPU();
			usage_cpu += HypervisorList.get(j).getVirtualUsedCPU();
			memory += HypervisorList.get(j).getLocalMemory();
			usage_memory += HypervisorList.get(j).getLocalMemoryUsed();
			disk += HypervisorList.get(j).getLocalDisk();
			usage_disk += HypervisorList.get(j).getLocalDiskUsed();
			network += 10;
			usage_network +=3;
		}
		
		virtual_resource_el.addContent(new Element("item").addContent("cpu, " + cpu));
		virtual_resource_el.addContent(new Element("item").addContent("usage_cpu, " + usage_cpu));
		virtual_resource_el.addContent(new Element("item").addContent("memory, " + memory));
		virtual_resource_el.addContent(new Element("item").addContent("usage_memory, " + usage_memory));
		virtual_resource_el.addContent(new Element("item").addContent("disk, " + disk));
		virtual_resource_el.addContent(new Element("item").addContent("usage_disk, " + usage_disk));
		virtual_resource_el.addContent(new Element("item").addContent("network, "+network));
		virtual_resource_el.addContent(new Element("item").addContent("usage_network, "+usage_network));
		
		rootElement.addContent(virtual_resource_el);

		
		Element component_el = new Element("component");
		
		//i = 0;
		
		for (int j = 0; j < HypervisorList.size(); j++) {
			
			Element component_info_el = new Element("component_info");
			component_info_el.addContent(new Element("id").addContent(HypervisorList.get(j).getHypervisorHostname()));				
			
			Element resource_el = new Element("resource");
			resource_el.addContent(new Element("item").addContent("cpu, "+HypervisorList.get(j).getVirtualCPU()));				
			resource_el.addContent(new Element("item").addContent("usage_cpu, "+HypervisorList.get(j).getVirtualUsedCPU()));
			resource_el.addContent(new Element("item").addContent("memory, "+HypervisorList.get(j).getLocalMemory()));
			resource_el.addContent(new Element("item").addContent("usage_memory, "+HypervisorList.get(j).getLocalMemoryUsed()));
			resource_el.addContent(new Element("item").addContent("disk, "+HypervisorList.get(j).getLocalDisk()));
			resource_el.addContent(new Element("item").addContent("usage_disk, "+HypervisorList.get(j).getLocalDiskUsed()));
			resource_el.addContent(new Element("item").addContent("network, "+10));
			resource_el.addContent(new Element("item").addContent("usage_network, "+3));
			
			component_info_el.addContent(resource_el);
			
			//serverResult.get(0).get
			
			Element vnf_el = new Element("vnf");
			
			for (int k = 0; k < serverResult.size(); k++) {
				
				if (HypervisorList.get(j).getHypervisorHostname().equals(serverResult.get(k).getHypervisorHostname())) {					
				
					for (int k2 = 0; k2 < resultList.size(); k2++) {
						
						if (resultList.get(k2).get("image_name").equals(serverResult.get(k).getImage().getName())) {
							logger.debug(k+"/"+k2);
							logger.debug("image 명이 같음. resultList image_name : "+resultList.get(k2).get("image_name")+", serverResult image_name : " + serverResult.get(k).getImage().getName());
							logger.debug(serverResult.get(k).getImage().getName());
							
							Element vnf_info_el = new Element("vnf_info");				
							vnf_info_el.addContent(new Element("id").addContent(resultList.get(k2).get("image_name")));
							vnf_info_el.addContent(new Element("instance_id").addContent(serverResult.get(k).getName()));
							vnf_info_el.addContent(new Element("status").addContent(serverResult.get(k).getStatus().toString()));						
							
							vnf_el.addContent(vnf_info_el);
						}					
					}
				}
				
			}
			
			component_info_el.addContent(vnf_el);
			component_el.addContent(component_info_el);
		}
		
		rootElement.addContent(component_el);	
		
		return rootElement;
	}
	
	@Override
	public String getNfvidOne(HashMap<String, String> hm) {
		
        return CommonXmlUtil.getElementToString(getNfvidElement(hm));
        
	}

	@Override
	public String getNfvidAll() {
		
		
		//TODO 추후 구현하여 count가 없는 경우 널처리.
		//int cnt = parserDao.getNfvidCount();
		
		Element nfvi_all = new Element("nfvi_all");
		
/*		if(cnt < 1){
			return CommonXmlUtil.getElementToString(nfvi_all);
		}*/
		
		List<String> list = parserDao.getNfvidAllId();
		
		if(list.size() < 1){
			return CommonXmlUtil.getElementToString(nfvi_all);
		}
		
		for(String id : list){
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("nfvidId", id);
			
			nfvi_all.addContent(getNfvidElement(hm));
		}
		
		
		return CommonXmlUtil.getElementToString(nfvi_all);
	}
	
	
	/** nfvic(기존nfvidc) */
	private Element getNfvicElement(HashMap<String, String> hm) {
		String reqNfvicId = hm.get("nfvicId");
		
		int cnt = parserDao.getNfvicCount(hm);
		if(cnt < 1){
			return new Element("nfvic");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getNfvicOne(hm);
		
		List<String> nfvidc_no= new ArrayList<String>();
		List<String> nfvidc_id= new ArrayList<String>();
		
		List<String> mnt_ref_no= new ArrayList<String>();
		List<String> func_ref_no= new ArrayList<String>();
		List<String> mnt_no= new ArrayList<String>();
		List<String> mnt_item= new ArrayList<String>();
		List<String> function_no= new ArrayList<String>();
		
		List<String> ver_item= new ArrayList<String>();
		List<String> ver_id= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			
			nfvidc_no.add(map.get("nfvidc_no"));
			nfvidc_id.add(map.get("nfvidc_id"));
			
			mnt_ref_no.add(map.get("mnt_ref_no"));
			func_ref_no.add(map.get("func_ref_no"));
			mnt_no.add(map.get("mnt_no"));
			mnt_item.add(map.get("mnt_item"));
			function_no.add(map.get("function_no"));
			
			ver_item.add(map.get("ver_item"));
			ver_id.add(map.get("ver_id"));
		}
		
		Element rootElement = new Element("nfvic");
		rootElement.addContent(new Element("id").addContent(nfvidc_id.get(0)));
		
		Element resource_el = new Element("resource");
		
		int i = 0;
		for(String s : mnt_item) {
			if( ! "NULL".equals(s) && reqNfvicId.equals(nfvidc_id.get(i))){
				resource_el.addContent(new Element("item").addContent(s));
			}
			
			i++;
		}
		rootElement.addContent(resource_el);
		
		
		Element vnf_el = new Element("vnf");
		
		int j = 0;
		for(String k : ver_id){
			if(!"NULL".equals(k) && reqNfvicId.equals(nfvidc_id.get(j))){
				Element vnf_info_el = new Element("vnf_info");
				vnf_info_el.addContent(new Element("id").addContent(k));
				vnf_info_el.addContent(new Element("status").addContent(ver_item.get(j)));
				vnf_el.addContent(vnf_info_el);
			}
			
			j++;
		}

		rootElement.addContent(vnf_el);

		return rootElement;
	}

	@Override
	public String getNfvicOne(HashMap<String, String> hm) {
		return CommonXmlUtil.getElementToString(getNfvicElement(hm));
	}

	
	@Override
	public String getNfvicAll() {
		int cnt = parserDao.getNfvidCount();
		
		Element nfvic_all = new Element("nfvic_all");
		
		if(cnt < 1) {
			return CommonXmlUtil.getElementToString(nfvic_all);
		}
		
		List<String> list = parserDao.getNfvicAllId();
		
		for(String id : list) {
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vdu_id", id);
			
			nfvic_all.addContent(getNfvicElement(hm));
		}
		
		return CommonXmlUtil.getElementToString(nfvic_all);
	}
	
	
	/** constituent */
	private Element getConstituentElement(HashMap<String, String> hm) {
		
		int cnt = parserDao.getConstituenCount(hm);
		
		if(cnt < 1) {
			return new Element("constituent_vdu");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getConstituenOne(hm);
		
		List<String> vdu_id= new ArrayList<String>();
		List<String> instance_cnt= new ArrayList<String>();
		
		List<String> vnfc_no= new ArrayList<String>();
		List<String> vnfc_id= new ArrayList<String>();
		
		List<String> cp_no= new ArrayList<String>();
		List<String> cp_id= new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			vdu_id.add(map.get("vdu_id"));
			instance_cnt.add(map.get("instance_cnt"));
			
			vnfc_no.add(map.get("vnfc_no"));
			vnfc_id.add(map.get("vnfc_id"));
			
			cp_no.add(map.get("cp_no"));
			cp_id.add(map.get("cp_id"));
		}
		
		Element rootElement = new Element("constituent_vdu");
		
		Element constituent_vdu_el = new Element("constituent_vdu_info");
		constituent_vdu_el.addContent(new Element("vdu_reference").addContent(vdu_id.get(0)));
		constituent_vdu_el.addContent(new Element("number_of_instances").addContent(instance_cnt.get(0)));
		
		Element constituent_vnfc_el = new Element("constituent_vnfc");
		int i = 0;
		for(String s : cp_id) {
			if( ! "NULL".equals(s) ){
				constituent_vnfc_el.addContent(new Element("ref").addContent(cp_id.get(i)));
			}
			
			i++;
		}
		constituent_vdu_el.addContent(constituent_vnfc_el);
		rootElement.addContent(constituent_vdu_el);

		return rootElement;
	}

	@Override
	public String getConstituentOne(HashMap<String, String> hm) {
		return CommonXmlUtil.getElementToString(getConstituentElement(hm));
	}

	
	@Override
	public String getConstituentAll() {
		int cnt = parserDao.getConstituenCount();
		
		Element nfvic_all = new Element("constituent_vdu_all");
		
		if(cnt < 1) {
			return CommonXmlUtil.getElementToString(nfvic_all);
		}
		
		List<String> list = parserDao.getConstituenAllId();
		
		for(String id : list) {
			
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vdu_id", id);
			
			nfvic_all.addContent(getConstituentElement(hm));
		}
		
		return CommonXmlUtil.getElementToString(nfvic_all);
	}

	/** cvnf(constituent_vnf) */
	private Element getCvnfElement(HashMap<String, String> hm) {
		int cnt = parserDao.getCvnfCount(hm);
		
		if(cnt < 1) {
			return new Element("constituent_vnf");
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getCvnfOne(hm);
		
		List<String> version_no = new ArrayList<String>();
		List<String> vnf_id = new ArrayList<String>();
		List<String> vendor = new ArrayList<String>();
		List<String> version = new ArrayList<String>();
		
		List<String> convnf_no = new ArrayList<String>();
		List<String> flavour_nm = new ArrayList<String>();
		List<String> rddc_model = new ArrayList<String>();
		List<String> affinity = new ArrayList<String>();
		List<String> vnf_cap = new ArrayList<String>();
		List<String> vnf_instance_cnt = new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			version_no.add(map.get("version_no"));
			vnf_id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			version.add(map.get("version"));
			
			convnf_no.add(map.get("convnf_no"));
			flavour_nm.add(map.get("flavour_nm"));
			rddc_model.add(map.get("rddc_model"));
			affinity.add(map.get("affinity"));
			vnf_cap.add(map.get("vnf_cap"));
			vnf_instance_cnt.add(map.get("vnf_instance_cnt"));
		}
		
		Element rootElement = new Element("constituent_vnf");
		
		int i = 0;
		for(String s : vnf_id){
//			Element constituent_vnf_info_el = new Element("constituent_vnf_info");
			rootElement.addContent(new Element("id").addContent(flavour_nm.get(i) + "_" + vnf_id.get(i)));
			rootElement.addContent(new Element("vnf_reference").addContent(s));
			rootElement.addContent(new Element("vnf_flavour_id_reference").addContent(flavour_nm.get(i)));
			rootElement.addContent(new Element("redundancy_model").addContent(rddc_model.get(i)));
			rootElement.addContent(new Element("affinity").addContent(affinity.get(i)));
			rootElement.addContent(new Element("vnf_cap").addContent(vnf_cap.get(i)));
			rootElement.addContent(new Element("number_of_instances").addContent(vnf_instance_cnt.get(i)));
			
//			rootElement.addContent(constituent_vnf_info_el);
			i++;
		}
		
		return rootElement;
	}
	
	@Override
	public String getCvnfOne(HashMap<String, String> hm) {
		return CommonXmlUtil.getElementToString(getCvnfElement(hm));
	}

	@Override
	public String getCvnfAll() {
		int cnt = parserDao.getCvnfCount();
		
		Element cvnf_all = new Element("constituent_vnf_all");
		
		if(cnt < 1) {
			return CommonXmlUtil.getElementToString(cvnf_all);
		}
		
		List<LinkedHashMap<String, String>> resultList = parserDao.getCvnfAllId();
		
		List<String> flavour_nm = new ArrayList<String>();
		List<String> vnf_id = new ArrayList<String>();
		
		for(LinkedHashMap<String, String> map : resultList){
			flavour_nm.add(map.get("flavour_nm"));
			vnf_id.add(map.get("id"));
		}
		
		int i = 0;
		for(String id : vnf_id) {
			HashMap<String,String> hm = new HashMap<String,String>();
			hm.put("vnf_id", id);
			hm.put("flavour_nm", flavour_nm.get(i));			
			
			cvnf_all.addContent(getCvnfElement(hm));
		
			i++;
		}
		
		return CommonXmlUtil.getElementToString(cvnf_all);
	}
	
	
}
