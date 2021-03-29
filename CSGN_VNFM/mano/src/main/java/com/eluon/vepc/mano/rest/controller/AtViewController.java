package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.common.CommonXmlUtil;
import com.eluon.vepc.mano.service.AtViewService;

@RestController
public class AtViewController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private  AtViewService atViewService;
	
	@RequestMapping(value = "/core/eluon/NFV/VLD/atview/meta/xml", method = RequestMethod.GET)
	public void getVldAtViewMetaXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVldAtViewMeta start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		
		httpServletResponse.getWriter().println(atViewService.getVldAtViewMeta(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VLD/atview/meta/json", method = RequestMethod.GET)
	public void getVldAtViewMetaJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVldAtViewMeta start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getVldAtViewMeta(hm)));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VM/atview/meta/xml", method = RequestMethod.GET)
	public void getVmAtViewMetaXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVmAtViewMeta start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		
		httpServletResponse.getWriter().println(atViewService.getVmAtViewMeta(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VM/atview/meta/json", method = RequestMethod.GET)
	public void getVmAtViewMetaJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVmAtViewMeta start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getVmAtViewMeta(hm)));
		
	}
	
	@RequestMapping(value = "/core/eluon/tmenu/xml", method = RequestMethod.GET)
	public void getTmenuXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getTmenu start");
		
		httpServletResponse.getWriter().println(atViewService.getTmenu());
		
	}
	
	@RequestMapping(value = "/core/eluon/tmenu/json", method = RequestMethod.GET)
	public void getTmenuJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getTmenu start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getTmenu()));
		
	}
	
	@RequestMapping(value = "/core/eluon/toption/xml", method = RequestMethod.GET)
	public void getToptionXml(HttpServletResponse httpServletResponse) throws IOException {
		
		
		httpServletResponse.getWriter().println(atViewService.getToption());
		
	}
	
	@RequestMapping(value = "/core/eluon/toption/json", method = RequestMethod.GET)
	public void getToptionJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getToption start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getToption()));
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/main/xml", method = RequestMethod.GET)
	public void getMainMenuXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getMainMenuXml start");
		
		httpServletResponse.getWriter().println(atViewService.getMainMenu());
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/main/json", method = RequestMethod.GET)
	public void getMainMenuJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getMainMenuJson start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getMainMenu()));
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/sub/xml", method = RequestMethod.GET)
	public void getSubMenuXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getSubMenuXml start");
		
		httpServletResponse.getWriter().println(atViewService.getSubMenu());
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/sub/json", method = RequestMethod.GET)
	public void getSubMenuJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getSubMenuJson start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getSubMenu()));
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/option/xml", method = RequestMethod.GET)
	public void getOptionMenuXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getOptionMenuXml start");
		
		httpServletResponse.getWriter().println(atViewService.getOptionMenu());
		
	}
	
	@RequestMapping(value = "/core/eluon/menu/option/json", method = RequestMethod.GET)
	public void getOptionMenuJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getOptionMenuJson start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(atViewService.getOptionMenu()));
		
	}
	
	@RequestMapping(value = "/core/eluon/vld/{vldId}", method = RequestMethod.GET)
	public void getVld(HttpServletResponse httpServletResponse, @PathVariable("vldId") String vldId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("vldId", vldId);
		
		httpServletResponse.getWriter().println(atViewService.getVld(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/vld/{vldId}", method = RequestMethod.POST)
	public void postVld(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vldId") String vldId) throws IOException {
		
		String id = vldId;
		String vendor = httpServletRequest.getParameter("vendor");
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String number_of_endpoints = httpServletRequest.getParameter("number_of_endpoints");
		String root_requirement = httpServletRequest.getParameter("root_requirement");
		String leaf_requirement = httpServletRequest.getParameter("leaf_requirement");
		String test_access = httpServletRequest.getParameter("test_access");
		String[] qos_key_list = httpServletRequest.getParameterValues("qos_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");
		String connectivity_type = httpServletRequest.getParameter("connectivity_type");
		String[] security_list = httpServletRequest.getParameterValues("security");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("vendor", vendor);
		hm.put("version", descriptor_version);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("root_requirement", root_requirement);
		hm.put("leaf_requirement", leaf_requirement);
		hm.put("test_access", test_access);
		hm.put("qos_key_list", qos_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("connectivity_type", connectivity_type);
		hm.put("security_list", security_list);
		
		httpServletResponse.getWriter().println(atViewService.postVld(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/vld/{vldId}", method = RequestMethod.PUT)
	public void putVld(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vldId") String vldId) throws IOException {

		String id = vldId;
		String vendor = httpServletRequest.getParameter("vendor");
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String number_of_endpoints = httpServletRequest.getParameter("number_of_endpoints");
		String root_requirement = httpServletRequest.getParameter("root_requirement");
		String leaf_requirement = httpServletRequest.getParameter("leaf_requirement");
		String test_access = httpServletRequest.getParameter("test_access");
		String[] qos_key_list = httpServletRequest.getParameterValues("qos_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");
		String connectivity_type = httpServletRequest.getParameter("connectivity_type");
		String[] security_list = httpServletRequest.getParameterValues("security");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("vendor", vendor);
		hm.put("version", descriptor_version);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("root_requirement", root_requirement);
		hm.put("leaf_requirement", leaf_requirement);
		hm.put("test_access", test_access);
		hm.put("qos_key_list", qos_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("connectivity_type", connectivity_type);
		hm.put("security_list", security_list);
		
		httpServletResponse.getWriter().println(atViewService.putVld(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/vld/{vldId}", method = RequestMethod.DELETE)
	public void deleteVld(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vldId") String vldId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", vldId);
		httpServletResponse.getWriter().println(atViewService.deleteVld(hm));
		
	}
	
	//cp
	@RequestMapping(value = "/core/eluon/cp/{cpId}", method = RequestMethod.GET)
	public void getCp(HttpServletResponse httpServletResponse, @PathVariable("cpId") String cpId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", cpId);
		
		httpServletResponse.getWriter().println(atViewService.getCp(hm));
	}
	
	@RequestMapping(value = "/core/eluon/cp/{cpId}", method = RequestMethod.POST)
	public void postCp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("cpId") String cpId) throws IOException {
		
		String id = cpId;
		String virtual_link_reference = httpServletRequest.getParameter("virtual_link_reference");
		String type = httpServletRequest.getParameter("type");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("virtual_link_reference", virtual_link_reference);
		hm.put("type", type);
		
		httpServletResponse.getWriter().println(atViewService.postCp(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/cp/{cpId}", method = RequestMethod.PUT)
	public void putCp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("cpId") String cpId) throws IOException {
		
		String id = cpId;
		String virtual_link_reference = httpServletRequest.getParameter("virtual_link_reference");
		String type = httpServletRequest.getParameter("type");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("virtual_link_reference", virtual_link_reference);
		hm.put("type", type);
		
		httpServletResponse.getWriter().println(atViewService.putCp(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/cp/{cpId}", method = RequestMethod.DELETE)
	public void deleteCp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("cpId") String cpId) throws IOException {
		
		String id = cpId;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", id);
		
		httpServletResponse.getWriter().println(atViewService.deleteCp(hm));
		
	}
	
	//pnfd
	@RequestMapping(value = "/core/eluon/pnfd/{pnfdId}", method = RequestMethod.GET)
	public void getPnfd(HttpServletResponse httpServletResponse, @PathVariable("pnfdId") String pnfdId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", pnfdId);
		
		httpServletResponse.getWriter().println(atViewService.getPnfd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/pnfd/{pnfdId}", method = RequestMethod.POST)
	public void postPnfd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("pnfdId") String pnfdId) throws IOException {
		
		String id = pnfdId;
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String description = httpServletRequest.getParameter("description");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String[] security_list = httpServletRequest.getParameterValues("security");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("description", description);
		hm.put("cp_key_list", cp_key_list);
		hm.put("descriptor_version", descriptor_version);
		hm.put("security_list", security_list);
		
		httpServletResponse.getWriter().println(atViewService.postPnfd(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/pnfd/{pnfdId}", method = RequestMethod.PUT)
	public void putPnfd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("pnfdId") String pnfdId) throws IOException {
		
		String id = pnfdId;
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String description = httpServletRequest.getParameter("description");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String[] security_list = httpServletRequest.getParameterValues("security");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("id", id);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("description", description);
		hm.put("cp_key_list", cp_key_list);
		hm.put("descriptor_version", descriptor_version);
		hm.put("security_list", security_list);
		
		httpServletResponse.getWriter().println(atViewService.putPnfd(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/pnfd/{pnfdId}", method = RequestMethod.DELETE)
	public void deletePnfd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("pnfdId") String pnfdId) throws IOException {
		
		String id = pnfdId;
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", id);
		
		httpServletResponse.getWriter().println(atViewService.deletePnfd(hm));
		
	}
	
	//security
	@RequestMapping(value = "/core/eluon/security/all/get", method = RequestMethod.GET)
	public void getSecurity(HttpServletResponse httpServletResponse) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		httpServletResponse.getWriter().println(atViewService.getSecurity(hm));
	}
	
	
	@RequestMapping(value = "/core/eluon/security/all/put", method = RequestMethod.PUT)
	public void postAndputSecurity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		
		String[] security_no_list = httpServletRequest.getParameterValues("security_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");
		
		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("security_no_list", Arrays.asList(security_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));
		
		httpServletResponse.getWriter().println(atViewService.postAndPutSecurity(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/security/all/delete", method = RequestMethod.PUT)
	public void deleteSecurity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");
		
		HashMap<String, Object> hm = new HashMap<String,Object>();
		
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));
		

		httpServletResponse.getWriter().println(atViewService.deleteSecurity(hm));
		
	}
	
	//vdu
	@RequestMapping(value = "/core/eluon/vdu/{vduId}", method = RequestMethod.GET)
	public void getVdu(HttpServletResponse httpServletResponse, @PathVariable("vduId") String vduId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vduId);
		
		httpServletResponse.getWriter().println(atViewService.getVdu(hm));
	}
	
	@RequestMapping(value = "/core/eluon/vdu/{vduId}", method = RequestMethod.POST)
	public void postVdu(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vduId") String vduId) throws IOException {
		
		String id = vduId;
		String vm_image = httpServletRequest.getParameter("vm_image");
		String computation_requirement = httpServletRequest.getParameter("computation_requirement");
		String virtual_memory_resource_element = httpServletRequest.getParameter("virtual_memory_resource_element");
		String virtual_network_bandwidth_resource = httpServletRequest.getParameter("virtual_network_bandwidth_resource");
		String[] lifecycle_event_key_list = httpServletRequest.getParameterValues("lifecycle_event_key");
		String constraint = httpServletRequest.getParameter("constraint");
		String high_availability = httpServletRequest.getParameter("high_availability");
		String scale_in_out = httpServletRequest.getParameter("scale_in_out");
		String[] vnfc_key_list = httpServletRequest.getParameterValues("vnfc_key");
		String[] monitoring_parameter_key_list = httpServletRequest.getParameterValues("monitoring_parameter_key");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("vdu_id", id);
		hm.put("vm_image", vm_image);
		hm.put("computation_requirement", computation_requirement);
		hm.put("vmre", virtual_memory_resource_element);
		hm.put("vnbr", virtual_network_bandwidth_resource);
		hm.put("lifecycle_event_key_list", lifecycle_event_key_list);
		hm.put("vdu_constraint", constraint);
		hm.put("high_availability", high_availability);
		hm.put("scale_in_out", scale_in_out);
		hm.put("vnfc_key_list", vnfc_key_list);
		hm.put("monitoring_parameter_key_list", monitoring_parameter_key_list);
		
		httpServletResponse.getWriter().println(atViewService.postVdu(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/vdu/{vduId}", method = RequestMethod.PUT)
	public void putVdu(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vduId") String vduId) throws IOException {

		String id = vduId;
		String vm_image = httpServletRequest.getParameter("vm_image");
		String computation_requirement = httpServletRequest.getParameter("computation_requirement");
		String virtual_memory_resource_element = httpServletRequest.getParameter("virtual_memory_resource_element");
		String virtual_network_bandwidth_resource = httpServletRequest.getParameter("virtual_network_bandwidth_resource");
		String[] lifecycle_event_key_list = httpServletRequest.getParameterValues("lifecycle_event_key");
		String constraint = httpServletRequest.getParameter("constraint");
		String high_availability = httpServletRequest.getParameter("high_availability");
		String scale_in_out = httpServletRequest.getParameter("scale_in_out");
		String[] vnfc_key_list = httpServletRequest.getParameterValues("vnfc_key");
		String[] monitoring_parameter_key_list = httpServletRequest.getParameterValues("monitoring_parameter_key");
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("vdu_id", id);
		hm.put("vm_image", vm_image);
		hm.put("computation_requirement", computation_requirement);
		hm.put("vmre", virtual_memory_resource_element);
		hm.put("vnbr", virtual_network_bandwidth_resource);
		hm.put("lifecycle_event_key_list", lifecycle_event_key_list);
		hm.put("vdu_constraint", constraint);
		hm.put("high_availability", high_availability);
		hm.put("scale_in_out", scale_in_out);
		hm.put("vnfc_key_list", vnfc_key_list);
		hm.put("monitoring_parameter_key_list", monitoring_parameter_key_list);
		
		httpServletResponse.getWriter().println(atViewService.putVdu(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/vdu/{vduId}", method = RequestMethod.DELETE)
	public void deleteVdu(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vduId") String vduId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("vdu_id", vduId);
		httpServletResponse.getWriter().println(atViewService.deleteVdu(hm));
		
	}
	
	
	//TODO
	/** nfvi(기존 nfvid) */
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.GET)
	public void getNfvi(HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		
		httpServletResponse.getWriter().println(atViewService.getNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.POST)
	public void postNfvi(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {

		//String[] mnt_key_list = httpServletRequest.getParameterValues("virtual_resource_key");
		//String[] func_key_list = httpServletRequest.getParameterValues("component_info_key");
		
		String[] image_name_list = httpServletRequest.getParameterValues("image_name");
				
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);		
		hm.put("image_name_list", image_name_list);

		
		httpServletResponse.getWriter().println(atViewService.postNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.PUT)
	public void putNfvi(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
	
		String[] image_name_list = httpServletRequest.getParameterValues("image_name");
		
		for(String s : image_name_list){
			System.out.println("#### " + s);
		}
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);		
		hm.put("image_name_list", image_name_list);
		
		httpServletResponse.getWriter().println(atViewService.putNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.DELETE)
	public void deleteNfvi(HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		
		httpServletResponse.getWriter().println(atViewService.deleteNfvi(hm));
	}
	
	//기존 소스
/*	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.GET)
	public void getNfvi(HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		
		httpServletResponse.getWriter().println(atViewService.getNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.POST)
	public void postNfvi(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {

		String[] mnt_key_list = httpServletRequest.getParameterValues("virtual_resource_key");
		String[] func_key_list = httpServletRequest.getParameterValues("component_info_key");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("func_key_list", func_key_list);
		
		httpServletResponse.getWriter().println(atViewService.postNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.PUT)
	public void putNfvi(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
		
		String[] mnt_key_list = httpServletRequest.getParameterValues("virtual_resource_key");
		String[] func_key_list = httpServletRequest.getParameterValues("component_info_key");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("func_key_list", func_key_list);
		
		httpServletResponse.getWriter().println(atViewService.putNfvi(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nfvi/{nfviId}", method = RequestMethod.DELETE)
	public void deleteNfvi(HttpServletResponse httpServletResponse, @PathVariable("nfviId") String nfviId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfviId);
		
		httpServletResponse.getWriter().println(atViewService.deleteNfvi(hm));
	}*/
	
	/** vnfd */
	@RequestMapping(value = "/core/eluon/vnfd/{vnfdId}", method = RequestMethod.GET)
	public void getVnfd(HttpServletResponse httpServletResponse, @PathVariable("vnfdId") String vnfdId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnfdId);
		
		httpServletResponse.getWriter().println(atViewService.getVnfd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/vnfd/{vnfdId}", method = RequestMethod.POST)
	public void postVnfd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vnfdId") String vnfdId) throws IOException {
		
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String[] vdu_key_list = httpServletRequest.getParameterValues("vdu_key");
		String[] vld_key_list = httpServletRequest.getParameterValues("virtual_link_key"); // vld_version_no_key_list
		String[] vnfd_cp_key_list = httpServletRequest.getParameterValues("connection_point_key");
		String[] life_key_list = httpServletRequest.getParameterValues("lifecycle_key");
		String[] dep_key_list = httpServletRequest.getParameterValues("dependency_key");
		String[] mnt_key_list = httpServletRequest.getParameterValues("monitoring_key");
		String[] flavour_key_list = httpServletRequest.getParameterValues("flavour_key");
		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] security_key_list = httpServletRequest.getParameterValues("security_key");
		String manifest_file = httpServletRequest.getParameter("manifest_file");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnfdId);
		hm.put("descriptor_version", descriptor_version);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("vdu_key_list", vdu_key_list);
		hm.put("vld_key_list", vld_key_list);
		hm.put("vnfd_cp_key_list", vnfd_cp_key_list);
		hm.put("lifecycle_key_list", life_key_list);
		hm.put("dep_key_list", dep_key_list);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("flavour_key_list", flavour_key_list);
		hm.put("policy_key_list", policy_key_list);
		hm.put("security_key_list", security_key_list);
		hm.put("manifest_file", manifest_file);
		
		httpServletResponse.getWriter().println(atViewService.postVnfd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/vnfd/{vnfdId}", method = RequestMethod.PUT)
	public void putVnfd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vnfdId") String vnfdId) throws IOException {
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String[] vdu_key_list = httpServletRequest.getParameterValues("vdu_key");
		String[] vld_key_list = httpServletRequest.getParameterValues("virtual_link_key");
		String[] vnfd_cp_key_list = httpServletRequest.getParameterValues("connection_point_key");
		String[] life_key_list = httpServletRequest.getParameterValues("lifecycle_key");
		String[] dep_key_list = httpServletRequest.getParameterValues("dependency_key");
		String[] mnt_key_list = httpServletRequest.getParameterValues("monitoring_key");
		String[] flavour_key_list = httpServletRequest.getParameterValues("flavour_key");
		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] security_key_list = httpServletRequest.getParameterValues("security_key");
		String manifest_file = httpServletRequest.getParameter("manifest_file");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnfdId);
		hm.put("descriptor_version", descriptor_version);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("vdu_key_list", vdu_key_list);
		hm.put("vld_key_list", vld_key_list);
		hm.put("vnfd_cp_key_list", vnfd_cp_key_list);
		hm.put("lifecycle_key_list", life_key_list);
		hm.put("dep_key_list", dep_key_list);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("flavour_key_list", flavour_key_list);
		hm.put("policy_key_list", policy_key_list);
		hm.put("security_key_list", security_key_list);
		hm.put("manifest_file", manifest_file);
		
		httpServletResponse.getWriter().println(atViewService.putVnfd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/vnfd/{vnfdId}", method = RequestMethod.DELETE)
	public void deleteVnfd(HttpServletResponse httpServletResponse, @PathVariable("vnfdId") String vnfdId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnfdId);
		
		httpServletResponse.getWriter().println(atViewService.deleteVnfd(hm));
	}
	
	/** nsd */
	@RequestMapping(value = "/core/eluon/nsd/{nsdId}", method = RequestMethod.GET)
	public void getNsd(HttpServletResponse httpServletResponse, @PathVariable("nsdId") String nsdId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nsdId);
		
		httpServletResponse.getWriter().println(atViewService.getNsd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nsd/{nsdId}", method = RequestMethod.POST)
	public void postNsd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nsdId") String nsdId) throws IOException {
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String[] vnfd_key_list = httpServletRequest.getParameterValues("vnfd_key");
		String[] vnffgd_key_list = httpServletRequest.getParameterValues("vnffgd_key");
		String[] vld_key_list = httpServletRequest.getParameterValues("vld_key");
		String[] life_key_list = httpServletRequest.getParameterValues("lifecycle_key");
		String[] vnfdep_key_list = httpServletRequest.getParameterValues("vnfdep_key");
		String[] mnt_key_list = httpServletRequest.getParameterValues("monitoring_key");
		String[] sdf_key_list = httpServletRequest.getParameterValues("sdf_key");
		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("connectioin_point_key");
		String[] pnfd_key_list = httpServletRequest.getParameterValues("pnfd_key");
		String[] security_key_list = httpServletRequest.getParameterValues("security_key");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nsdId);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("vnfd_key_list", vnfd_key_list);
		hm.put("vnffgd_key_list", vnffgd_key_list);
		hm.put("vld_key_list", vld_key_list);
		hm.put("lifecycle_key_list", life_key_list);
		hm.put("vnfdep_key_list", vnfdep_key_list);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("sdf_key_list", sdf_key_list);
		hm.put("policy_key_list", policy_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("pnfd_key_list", pnfd_key_list);
		hm.put("security_key_list", security_key_list);
		
		httpServletResponse.getWriter().println(atViewService.postNsd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nsd/{nsdId}", method = RequestMethod.PUT)
	public void putNsd(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nsdId") String nsdId) throws IOException {
		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String[] vnfd_key_list = httpServletRequest.getParameterValues("vnfd_key");
		String[] vnffgd_key_list = httpServletRequest.getParameterValues("vnffgd_key");
		String[] vld_key_list = httpServletRequest.getParameterValues("vld_key");
		String[] life_key_list = httpServletRequest.getParameterValues("lifecycle_key");
		String[] vnfdep_key_list = httpServletRequest.getParameterValues("vnfdep_key");
		String[] mnt_key_list = httpServletRequest.getParameterValues("monitoring_key");
		String[] sdf_key_list = httpServletRequest.getParameterValues("sdf_key");
		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("connectioin_point_key");
		String[] pnfd_key_list = httpServletRequest.getParameterValues("pnfd_key");
		String[] security_key_list = httpServletRequest.getParameterValues("security_key");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nsdId);
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("vnfd_key_list", vnfd_key_list);
		hm.put("vnffgd_key_list", vnffgd_key_list);
		hm.put("vld_key_list", vld_key_list);
		hm.put("lifecycle_key_list", life_key_list);
		hm.put("vnfdep_key_list", vnfdep_key_list);
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("sdf_key_list", sdf_key_list);
		hm.put("policy_key_list", policy_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("pnfd_key_list", pnfd_key_list);
		hm.put("security_key_list", security_key_list);
		
		httpServletResponse.getWriter().println(atViewService.putNsd(hm));
	}
	
	@RequestMapping(value = "/core/eluon/nsd/{nsdId}", method = RequestMethod.DELETE)
	public void deleteNsd(HttpServletResponse httpServletResponse, @PathVariable("nsdId") String nsdId) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nsdId);
		
		httpServletResponse.getWriter().println(atViewService.deleteNsd(hm));
	}
	
}
