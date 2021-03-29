package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
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
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.VerificationService;

@RestController
public class VerificationController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private  VerificationService verificationService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/core/eluon/managementPolicyList", method = RequestMethod.GET)
	public void getManagementPolicyList(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getManagementPolicyListXml start");
		
		httpServletResponse.getWriter().println(verificationService.getManagementPolicyList(null));
		
	}
	
	@RequestMapping(value = "/core/eluon/managementPolicyList", method = RequestMethod.PUT)
	public void putManagementPolicyList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("putManagementPolicyList start");
		
		String[] mnt_policy_no_list = httpServletRequest.getParameterValues("mnt_policy_no");
		String[] mnt_policy_value_list = httpServletRequest.getParameterValues("mnt_policy_value");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("mnt_policy_no_list", mnt_policy_no_list);
		hm.put("mnt_policy_value_list", mnt_policy_value_list);
		
		httpServletResponse.getWriter().println(verificationService.putManagementPolicyList(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/Policy/{mnt_policy_id}/xml", method = RequestMethod.GET)
	public void getManagementPolicyByXml(HttpServletResponse httpServletResponse, @PathVariable("mnt_policy_id") String mnt_policy_id) throws IOException {
		
		logger.debug("getManagementPolicy start");
		
		logger.debug("mnt_policy_id = " + mnt_policy_id);
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("mnt_policy_id", mnt_policy_id);
		
		httpServletResponse.getWriter().println(verificationService.getManagementPolicyOne(hm));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/Policy/{mnt_policy_id}/json", method = RequestMethod.GET)
	public void getManagementPolicyByJson(HttpServletResponse httpServletResponse, @PathVariable("mnt_policy_id") String mnt_policy_id) throws IOException {
		
		logger.debug("getManagementPolicy start");
		
		
		logger.debug("mnt_policy_id = " + mnt_policy_id);
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("mnt_policy_id", mnt_policy_id);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(verificationService.getManagementPolicyOne(hm)));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/Policy/all/xml", method = RequestMethod.GET)
	public void getManagementPolicyAlBylXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getManagementPolicy start");
		
		httpServletResponse.getWriter().println(verificationService.getManagementPolicyAll(null));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/Policy/all/json", method = RequestMethod.GET)
	public void getManagementPolicyAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getManagementPolicy start");
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(verificationService.getManagementPolicyAll(null)));
		
	}
	
	@RequestMapping(value = "/verisdnfv/target/all", method = RequestMethod.GET)
	public void getTargetList(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getTargetList start");
		
		httpServletResponse.getWriter().println(verificationService.getTargetList(null));
		
	}
	
	@RequestMapping(value = "/verisdnfv/policy/all", method = RequestMethod.GET)
	public void getPolicyList(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getPolicyList start");
		
		httpServletResponse.getWriter().println(verificationService.getPolicyList(null));
		
	}
	
	@RequestMapping(value = "/verisdnfv/host/all", method = RequestMethod.GET)
	public void getHostList(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getHostList start");

		
		httpServletResponse.getWriter().println(verificationService.getHostList(null));
		
	}
	
	@RequestMapping(value = "/verisdnfv/compute/all", method = RequestMethod.GET)
	public void getComputeList(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getComputeList start");
		
		httpServletResponse.getWriter().println(verificationService.getComputeList(null));
		
	}
	
	@RequestMapping(value = "/verisdnfv/{standard}/{content}/{descriptorId}/{targetId}/{policyId}/{hostId}", method = RequestMethod.GET)
	public void getResultVerification(
					HttpServletResponse httpServletResponse,
					@PathVariable("standard") String standard,
					@PathVariable("content") String content,
					@PathVariable("descriptorId") String descriptorId,
					@PathVariable("targetId") String targetId,
					@PathVariable("policyId") String policyId,
					@PathVariable("hostId") String hostId
				) throws IOException {
		
		System.out.println("standard = " + standard);
		System.out.println("content = " + content);
		System.out.println("descriptorId = " + descriptorId);
		System.out.println("targetId = " + targetId);
		System.out.println("policyId = " + policyId);
		System.out.println("hostId = " + hostId);
		
		logger.debug("getPropertyList start");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("standard", standard);
		hm.put("content", content);
		hm.put("descriptorId", descriptorId);
		hm.put("targetId", targetId);
		hm.put("policyId", policyId);
		hm.put("hostId", hostId);
		
		httpServletResponse.getWriter().println(verificationService.getResultVerification(hm));
		
	}
	
	@RequestMapping(value = "/verisdnfv/{standard}/{content}/{descriptorId}/{targetId}/{policyId}/{hostId}/json", method = RequestMethod.GET)
	public void getResultVerificationJson(
					HttpServletResponse httpServletResponse,
					@PathVariable("standard") String standard,
					@PathVariable("content") String content,
					@PathVariable("descriptorId") String descriptorId,
					@PathVariable("targetId") String targetId,
					@PathVariable("policyId") String policyId,
					@PathVariable("hostId") String hostId
				) throws IOException {
		
		System.out.println("standard = " + standard);
		System.out.println("content = " + content);
		System.out.println("descriptorId = " + descriptorId);
		System.out.println("targetId = " + targetId);
		System.out.println("policyId = " + policyId);
		System.out.println("hostId = " + hostId);
		
		logger.debug("getPropertyList start");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		
		hm.put("standard", standard);
		hm.put("content", content);
		hm.put("descriptorId", descriptorId);
		hm.put("targetId", targetId);
		hm.put("policyId", policyId);
		hm.put("hostId", hostId);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(verificationService.getResultVerification(hm)));
		
	}
	
}
