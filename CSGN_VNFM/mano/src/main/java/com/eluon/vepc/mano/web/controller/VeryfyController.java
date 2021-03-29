package com.eluon.vepc.mano.web.controller;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eluon.vepc.mano.common.service.NFVCommonService;
import com.eluon.vepc.mano.service.VeryfyService;

/**
 * Veryfy Controller (VeryfyController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VeryfyController.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
@Controller
@RequestMapping("/static")
public class VeryfyController{
	protected final Log logger = LogFactory.getLog(getClass());
	protected final Log loggerTra = LogFactory.getLog("MANO_PROCESS_TRA");
	protected final Log loggerSin = LogFactory.getLog("MANO_PROCESS_SIN");
	@Autowired
	private VeryfyService veryfyService;
	
	@Autowired
	private NFVCommonService NFVcommonService;

	@RequestMapping(value = "/qos.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView qos(Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		model.addAttribute("qosList",veryfyService.getQosList(hm));
		mav.setViewName("veryfy/qos");
		mav.addObject("model", model);
		return mav;
	}
	
	
	/*한서구*/
	@RequestMapping(value = "/vld.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vld(
			@RequestParam(value ="title", required = false, defaultValue = "") String title, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("title", "vld");
		model.addAttribute("vldTree",veryfyService.getVldTree(hm));
		hm.put("title", "cp");
		model.addAttribute("cpTree",veryfyService.getVldTree(hm));
		hm.put("title", "nsd");
		model.addAttribute("nsdTree",veryfyService.getVldTree(hm));
		hm.put("title", "pnfd");
		model.addAttribute("pnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vdu");
		model.addAttribute("vduTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnfd");
		model.addAttribute("vnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnffgd");
		model.addAttribute("vnffgdTree",veryfyService.getVldTree(hm));
		mav.setViewName("veryfy/vldTreeData");
		mav.addObject("model", model);
		return mav;
	}
	@RequestMapping(value = "/vld0.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vld0(
			@RequestParam(value ="title", required = false, defaultValue = "") String title, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("title", title);
		model.addAttribute("vld",veryfyService.getVld(hm));
		model.addAttribute("veryfyList",veryfyService.getVeryfyList(hm));
		model.addAttribute("qosList",veryfyService.getQosList(hm));
		model.addAttribute("connectionList",veryfyService.getConnectionList(hm));
		model.addAttribute("securityList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/treeData");
		mav.addObject("model", model);
		return mav;
	}
	@RequestMapping(value = "/vldInsertPageGo.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vldInsertPageGo(
			@RequestParam(value ="title", required = false, defaultValue = "") String title, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("title", title);
		model.addAttribute("qosList",veryfyService.getQosList(hm));
		model.addAttribute("connectionList",veryfyService.getConnectionList(hm));
		model.addAttribute("securityList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/treeData");
		mav.addObject("model", model);
		return mav;
	}
	
	@RequestMapping(value = "/vldUpdate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vldUpdate(
			@RequestParam(value ="number_of_endpoints", required = false, defaultValue = "") String number_of_endpoints, 
			@RequestParam(value ="root_requirement", required = false, defaultValue = "") String root_requirement, 
			@RequestParam(value ="leaf_requirement", required = false, defaultValue = "") String leaf_requirement, 
			@RequestParam(value ="qos", required = false, defaultValue = "") String qos, 
			@RequestParam(value ="test_access", required = false, defaultValue = "") String test_access, 
			@RequestParam(value ="connection", required = false, defaultValue = "") String connection, 
			@RequestParam(value ="connectivity_type", required = false, defaultValue = "") String connectivity_type, 
			@RequestParam(value ="vld_security", required = false, defaultValue = "") String vld_security, 
			@RequestParam(value ="title", required = false, defaultValue = "") String title, 
			@RequestParam(value ="qos_ref_no", required = false, defaultValue = "") String qos_ref_no, 
			@RequestParam(value ="connection_ref_no", required = false, defaultValue = "") String connection_ref_no, 
			@RequestParam(value ="vld_security_ref_no", required = false, defaultValue = "") String vld_security_ref_no, 
			@RequestParam(value ="vld_no", required = false, defaultValue = "") String vld_no, 
			@RequestParam(value ="version_no", required = false, defaultValue = "") String version_no, 

			Model model
			) {
		    String[] qosArray;
		    qosArray = qos.split(",");
		    String[] connectionArray;
		    connectionArray = connection.split(",");
		
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("title", title);
		hm.put("version_no", version_no);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("root_requirement", root_requirement);
		hm.put("leaf_requirement", leaf_requirement);
		hm.put("test_access", test_access);
		hm.put("connectivity_type", connectivity_type);
		
		veryfyService.getVldUpdate(hm);
		
		hm.put("qos_ref_no", qos_ref_no);
		veryfyService.QosDelete(hm);
		for(int i=0; i<qosArray.length; i++){
			hm.put("qos_no", qosArray[i]);
			veryfyService.QosInsert(hm);
		}
		
		hm.put("cp_ref_no", connection_ref_no);
		veryfyService.ConnectionDelete(hm);
		for(int i=0; i<connectionArray.length; i++){
			hm.put("cp_no", connectionArray[i]);
			veryfyService.ConnectionInsert(hm);
		}
		
		hm.put("security_ref_no", vld_security_ref_no);//기존값
		hm.put("security_no", vld_security);//내가 선택한거
		veryfyService.VldSecurityDelete(hm);
		if(!vld_security.equals("N"))
		veryfyService.VldSecurityInsert(hm);
		
		model.addAttribute("vld",veryfyService.getVld(hm));
		model.addAttribute("veryfyList",veryfyService.getVeryfyList(hm));
		model.addAttribute("qosList",veryfyService.getQosList(hm));
		model.addAttribute("connectionList",veryfyService.getConnectionList(hm));
		model.addAttribute("securityList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/treeData");
		mav.addObject("model", model);
		return mav;
	}
	
	@RequestMapping(value = "/vldSave.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vldSave(
			@RequestParam(value ="vld_id", required = false, defaultValue = "") String vld_id, 
			@RequestParam(value ="vendor", required = false, defaultValue = "") String vendor, 
			@RequestParam(value ="descriptor_version", required = false, defaultValue = "") String descriptor_version, 
			@RequestParam(value ="number_of_endpoints", required = false, defaultValue = "") String number_of_endpoints, 
			@RequestParam(value ="root_requirement", required = false, defaultValue = "") String root_requirement, 
			@RequestParam(value ="leaf_requirement", required = false, defaultValue = "") String leaf_requirement, 
			@RequestParam(value ="qos", required = false, defaultValue = "") String qos, 
			@RequestParam(value ="test_access", required = false, defaultValue = "") String test_access, 
			@RequestParam(value ="connection", required = false, defaultValue = "") String connection, 
			@RequestParam(value ="connectivity_type", required = false, defaultValue = "") String connectivity_type, 
			@RequestParam(value ="vld_security", required = false, defaultValue = "") String vld_security, 
			@RequestParam(value ="title", required = false, defaultValue = "") String title, 

			Model model
			) {
		    String[] qosArray;
		    qosArray = qos.split(",");
		    String[] connectionArray;
		    connectionArray = connection.split(",");
		
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		String version_no = NFVcommonService.getSeq("tab10");
		hm.put("version_no", version_no);
		hm.put("id", vld_id);
		//hm.put("title", vld_id);
		hm.put("vendor", vendor);
		hm.put("version", descriptor_version);
		veryfyService.getVersionInsert(hm);//insert
		String vld_no = NFVcommonService.getSeq("tab31");
		hm.put("vld_no", vld_no);//만든값
		hm.put("version_no", version_no);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("root_requirement", root_requirement);
		hm.put("leaf_requirement", leaf_requirement);
		String qos_ref_no = NFVcommonService.getSeq("tab82");
		hm.put("qos_ref_no", qos_ref_no);//만든값
		for(int i=0; i<qosArray.length; i++){
			hm.put("qos_no", qosArray[i]);
			veryfyService.QosInsert(hm);//insert
		}
		hm.put("test_access", test_access);
		String connection_ref_no = NFVcommonService.getSeq("tab08");
		hm.put("cp_ref_no", connection_ref_no);//만든값
		hm.put("connection_ref_no", connection_ref_no);//만든값
		for(int i=0; i<connectionArray.length; i++){
			hm.put("cp_no", connectionArray[i]);
			veryfyService.ConnectionInsert(hm);//insert
		}
		hm.put("connectivity_type", connectivity_type);
		String vld_security_ref_no = NFVcommonService.getSeq("tab02");
		hm.put("security_ref_no", vld_security_ref_no);//만든값
		hm.put("vld_security_ref_no", vld_security_ref_no);//만든값
		hm.put("security_no", vld_security);
		if(!vld_security.equals("N"))
			veryfyService.VldSecurityInsert(hm);//insert
		
		veryfyService.getVldInsert(hm);
		
		hm.put("title", "vld");
		model.addAttribute("vldTree",veryfyService.getVldTree(hm));
		hm.put("title", "cp");
		model.addAttribute("cpTree",veryfyService.getVldTree(hm));
		hm.put("title", "nsd");
		model.addAttribute("nsdTree",veryfyService.getVldTree(hm));
		hm.put("title", "pnfd");
		model.addAttribute("pnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vdu");
		model.addAttribute("vduTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnfd");
		model.addAttribute("vnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnffgd");
		model.addAttribute("vnffgdTree",veryfyService.getVldTree(hm));
		mav.setViewName("veryfy/vldTreeData");
		mav.addObject("model", model);
		return mav;
	}
	
	
	@RequestMapping(value = "/vldDelete.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView vldDelete(
			@RequestParam(value ="vld_no", required = false, defaultValue = "") String vld_no,
			@RequestParam(value ="version_no", required = false, defaultValue = "") String version_no,
			@RequestParam(value ="qos_ref_no", required = false, defaultValue = "") String qos_ref_no,
			@RequestParam(value ="connection_ref_no", required = false, defaultValue = "") String connection_ref_no,
			@RequestParam(value ="vld_security_ref_no", required = false, defaultValue = "") String vld_security_ref_no,
			Model model
			) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("vld_no", vld_no);
		hm.put("version_no", version_no);
		hm.put("qos_ref_no", qos_ref_no);
		hm.put("cp_ref_no", connection_ref_no);
		hm.put("security_ref_no", vld_security_ref_no);
		veryfyService.QosDelete(hm);
		veryfyService.ConnectionDelete(hm);
		veryfyService.VldSecurityDelete(hm);
		veryfyService.getVldDelete(hm);
		veryfyService.getVersionDelete(hm);
		
		hm.put("title", "vld");
		model.addAttribute("vldTree",veryfyService.getVldTree(hm));
		hm.put("title", "cp");
		model.addAttribute("cpTree",veryfyService.getVldTree(hm));
		hm.put("title", "nsd");
		model.addAttribute("nsdTree",veryfyService.getVldTree(hm));
		hm.put("title", "pnfd");
		model.addAttribute("pnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vdu");
		model.addAttribute("vduTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnfd");
		model.addAttribute("vnfdTree",veryfyService.getVldTree(hm));
		hm.put("title", "vnffgd");
		model.addAttribute("vnffgdTree",veryfyService.getVldTree(hm));
		mav.setViewName("veryfy/vldTreeData");
		mav.addObject("model", model);
		return mav;
	}
	
	@RequestMapping(value = "/security.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView security(Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		model.addAttribute("veryfyList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/security");
		mav.addObject("model", model);
		return mav;
	}
	
	
	@RequestMapping(value = "/securityUpdate.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView securityUpdate(
			@RequestParam(value ="security_no", required = false, defaultValue = "") String security_no, 
			@RequestParam(value ="item", required = false, defaultValue = "") String item, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("security_no", security_no);
		hm.put("item", item);
		veryfyService.getSecurityUpdate(hm);
		model.addAttribute("veryfyList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/security");
		mav.addObject("model", model);
		return mav;
	}
	
	@RequestMapping(value = "/securityDelete.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView securityDelete(
			@RequestParam(value ="security_no", required = false, defaultValue = "") String security_no, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("security_no", security_no);
		veryfyService.getSecurityDelete(hm);
		model.addAttribute("veryfyList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/security");
		mav.addObject("model", model);
		return mav;
	}
	
	@RequestMapping(value = "/securitySave.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView securitySave(
			@RequestParam(value ="item", required = false, defaultValue = "") String item, 
			Model model) {
		ModelAndView mav = new ModelAndView();
		HashMap<String,Object> hm = new HashMap<String,Object>();
		String security_no = NFVcommonService.getSeq("tab01");
		hm.put("item", item);
		hm.put("security_no", security_no);
		veryfyService.getSecuritySave(hm);
		model.addAttribute("veryfyList",veryfyService.getSecurityList(hm));
		mav.setViewName("veryfy/security");
		mav.addObject("model", model);
		return mav;
	}
	/*한서구*/
	
	
	
}