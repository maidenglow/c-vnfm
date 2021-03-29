package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
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

import com.eluon.vepc.mano.common.CommonUtil;
import com.eluon.vepc.mano.service.AtViewOptionsService;

@RestController
public class AtViewOptionsController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private  AtViewOptionsService atViewOptionsService;

	@RequestMapping(value = "/core/eluon/policy/all/get", method = RequestMethod.GET)
	public void getPolicy(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getPolicy(hm));
	}

	@RequestMapping(value = "/core/eluon/policy/all/put", method = RequestMethod.PUT)
	public void postAndputPolicy(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] policy_no_list = httpServletRequest.getParameterValues("policy_no");
		String[] policy_list = httpServletRequest.getParameterValues("policy");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("policy_no_list", Arrays.asList(policy_no_list));
		hm.put("policy_list", Arrays.asList(policy_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutPolicy(hm));

	}


	@RequestMapping(value = "/core/eluon/policy/all/delete", method = RequestMethod.PUT)
	public void deleteSecurity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] policy_list = httpServletRequest.getParameterValues("policy");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("policy_list", Arrays.asList(policy_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deletePolicy(hm));

	}




	//qos
	@RequestMapping(value = "/core/eluon/qos/all/get", method = RequestMethod.GET)
	public void getQos(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getQos(hm));
	}


	@RequestMapping(value = "/core/eluon/qos/all/put", method = RequestMethod.PUT)
	public void postAndputQos(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] qos_no_list = httpServletRequest.getParameterValues("qos_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("qos_no_list", Arrays.asList(qos_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutQos(hm));

	}

	@RequestMapping(value = "/core/eluon/qos/all/delete", method = RequestMethod.PUT)
	public void deleteQos(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteQos(hm));

	}


	//lifecycle
	@RequestMapping(value = "/core/eluon/lifecycle/all/get", method = RequestMethod.GET)
	public void getLifecycle(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getLifecycle(hm));
	}


	@RequestMapping(value = "/core/eluon/lifecycle/all/put", method = RequestMethod.PUT)
	public void postAndputLifecycle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] life_no_list = httpServletRequest.getParameterValues("life_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("life_no_list", Arrays.asList(life_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutLifecycle(hm));

	}

	@RequestMapping(value = "/core/eluon/lifecycle/all/delete", method = RequestMethod.PUT)
	public void deleteLifecycle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteLifecycle(hm));

	}


	//monitoring
	@RequestMapping(value = "/core/eluon/monitoring/all/get", method = RequestMethod.GET)
	public void getMonitoring(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getMonitoring(hm));
	}


	@RequestMapping(value = "/core/eluon/monitoring/all/put", method = RequestMethod.PUT)
	public void postAndputMonitoring(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] mnt_no_list = httpServletRequest.getParameterValues("mnt_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("mnt_no_list", Arrays.asList(mnt_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutMonitoring(hm));

	}

	@RequestMapping(value = "/core/eluon/monitoring/all/delete", method = RequestMethod.PUT)
	public void deleteMonitoring(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteMonitoring(hm));

	}


	//NFP
	@RequestMapping(value = "/core/eluon/nfp/{nfpid}", method = RequestMethod.GET)
	public void getNfpByJson(HttpServletResponse httpServletResponse, @PathVariable("nfpid") String nfpId) throws IOException {


		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("nfp_Id", nfpId);

		httpServletResponse.getWriter().println(atViewOptionsService.getNfpOne(hm));
	}

	//insert
	@RequestMapping(value = "/core/eluon/nfp/{nfpid}", method = RequestMethod.POST)
	public void postNfp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfpid") String nfpId) throws IOException {

		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("nfp_id", nfpId);
		hm.put("policy_key_list", policy_key_list);
		hm.put("cp_key_list", cp_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.postNfp(hm));

	}

	//update
	@RequestMapping(value = "/core/eluon/nfp/{nfpid}", method = RequestMethod.PUT)
	public void putNfp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfpid") String nfpId) throws IOException {

		String[] policy_key_list = httpServletRequest.getParameterValues("policy_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");

		System.out.println("update API 호출.");
		System.out.println("policy key : " + httpServletRequest.getParameterValues("policy_key"));
		System.out.println("cp_key : " + httpServletRequest.getParameterValues("cp_key"));


		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("nfp_id", nfpId);
		hm.put("policy_key_list", policy_key_list);
		hm.put("cp_key_list", cp_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.putNfp(hm));

	}

	@RequestMapping(value = "/core/eluon/nfp/{nfpid}", method = RequestMethod.DELETE)
	public void deleteNfp(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("nfpid") String nfpId) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("nfp_id", nfpId);
		httpServletResponse.getWriter().println(atViewOptionsService.deleteNfp(hm));

	}


	//VNFC
	@RequestMapping(value = "/core/eluon/vnfc/{vnfc-id}", method = RequestMethod.GET)
	public void getVnfc(HttpServletResponse httpServletResponse, @PathVariable("vnfc-id") String vnfc_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("vnfc_id", vnfc_id);

		httpServletResponse.getWriter().println(atViewOptionsService.getVnfc(hm));

	}


	//insert
	@RequestMapping(value = "/core/eluon/vnfc/{vnfcid}", method = RequestMethod.POST)
	public void postVnfc(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vnfcid") String vnfc_id) throws IOException {


		System.out.println("vnfc post 호출.");

		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");


		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("vnfc_id", vnfc_id);
		hm.put("cp_key_list", cp_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.postVnfc(hm));

	}


	//update
	@RequestMapping(value = "/core/eluon/vnfc/{vnfcid}", method = RequestMethod.PUT)
	public void putVnfc(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vnfcid") String vnfc_id) throws IOException {		


		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");		

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("vnfc_id", vnfc_id);
		hm.put("cp_key_list", cp_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.putVnfc(hm));

	}


	//delete
	//update
	@RequestMapping(value = "/core/eluon/vnfc/{vnfcid}", method = RequestMethod.DELETE)
	public void deleteVnfc(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable("vnfcid") String vnfc_id) throws IOException {	

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("vnfc_id", vnfc_id);

		httpServletResponse.getWriter().println(atViewOptionsService.deleteVnfc(hm));

	}


	@RequestMapping(value = "/core/eluon/vnffgd/{vnffgdId}", method = RequestMethod.GET)
	public void getVnffgd(HttpServletResponse httpServletResponse, @PathVariable("vnffgdId") String vnffgd_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnffgd_id);

		httpServletResponse.getWriter().println(atViewOptionsService.getVnffgd(hm));

	}

	//insert
	@RequestMapping(value = "/core/eluon/vnffgd/{vnffgdId}", method = RequestMethod.POST)
	public void postVnffgd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnffgdId") String vnffgd_id) throws IOException {

		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");		


		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String number_of_endpoints = httpServletRequest.getParameter("number_of_endpoints");
		String number_of_virtual_links = httpServletRequest.getParameter("number_of_virtual_links");
		String[] vld_version_no_list = httpServletRequest.getParameterValues("vld_version_no"); //version을 보기 때문에 version_no를 받음.
		String[] nfp_key_list = httpServletRequest.getParameterValues("nfp_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");		
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String[] vnfd_version_no_list = httpServletRequest.getParameterValues("vnfd_version_no"); //version을 보기 때문에 version_no를 받음.
		String[] vnfd_security_list = httpServletRequest.getParameterValues("security");

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnffgd_id);		
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("number_of_virtual_links", number_of_virtual_links);
		hm.put("vld_version_no_list", vld_version_no_list);
		hm.put("nfp_key_list", nfp_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("descriptor_version", descriptor_version);
		hm.put("vnfd_version_no_list", vnfd_version_no_list);
		hm.put("vnfd_security_list", vnfd_security_list);

		httpServletResponse.getWriter().println(atViewOptionsService.postVnffgd(hm));

	}

	//update
	@RequestMapping(value = "/core/eluon/vnffgd/{vnffgdId}", method = RequestMethod.PUT)
	public void putVnffgd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnffgdId") String vnffgd_id) throws IOException {


		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");		


		String vendor = httpServletRequest.getParameter("vendor");
		String version = httpServletRequest.getParameter("version");
		String number_of_endpoints = httpServletRequest.getParameter("number_of_endpoints");
		String number_of_virtual_links = httpServletRequest.getParameter("number_of_virtual_links");
		String[] vld_version_no_list = httpServletRequest.getParameterValues("vld_version_no"); //version을 보기 때문에 version_no를 받음.
		String[] nfp_key_list = httpServletRequest.getParameterValues("nfp_key");
		String[] cp_key_list = httpServletRequest.getParameterValues("cp_key");		
		String descriptor_version = httpServletRequest.getParameter("descriptor_version");
		String[] vnfd_version_no_list = httpServletRequest.getParameterValues("vnfd_version_no"); //version을 보기 때문에 version_no를 받음.
		String[] vnfd_security_list = httpServletRequest.getParameterValues("security");

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnffgd_id);		
		hm.put("vendor", vendor);
		hm.put("version", version);
		hm.put("number_of_endpoints", number_of_endpoints);
		hm.put("number_of_virtual_links", number_of_virtual_links);
		hm.put("vld_version_no_list", vld_version_no_list);
		hm.put("nfp_key_list", nfp_key_list);
		hm.put("cp_key_list", cp_key_list);
		hm.put("descriptor_version", descriptor_version);
		hm.put("vnfd_version_no_list", vnfd_version_no_list);
		hm.put("vnfd_security_list", vnfd_security_list);

		httpServletResponse.getWriter().println(atViewOptionsService.putVnffgd(hm));

	}

	//delete
	@RequestMapping(value = "/core/eluon/vnffgd/{vnffgdId}", method = RequestMethod.DELETE)
	public void deleteVnffgd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnffgdId") String vnffgd_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnffgd_id);	

		httpServletResponse.getWriter().println(atViewOptionsService.deleteVnffgd(hm));

	}


	//vnfdep
	//vnfdep는 nsd에서만 사용하는 단일 옵션이기 때문에 앞에 nsd를 붙여 주었음.
	@RequestMapping(value = "/core/eluon/nsd-vnfdep/all/get", method = RequestMethod.GET)
	public void getVnfdep(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getVnfdep(hm));
	}


	@RequestMapping(value = "/core/eluon/nsd-vnfdep/all/put", method = RequestMethod.PUT)
	public void postAndputVnfdep(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] vnfdep_no_list = httpServletRequest.getParameterValues("vnfdep_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("vnfdep_no_list", Arrays.asList(vnfdep_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutVnfdep(hm));

	}

	@RequestMapping(value = "/core/eluon/nsd-vnfdep/all/delete", method = RequestMethod.PUT)
	public void deleteVnfdep(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteVnfdep(hm));

	}

	//vnfrtd
	@RequestMapping(value = "/core/eluon/vnfrtd/{vnfrtdId}", method = RequestMethod.GET)
	public void getVnfrtd(HttpServletResponse httpServletResponse, @PathVariable("vnfrtdId") String vnfrtd_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", vnfrtd_id);

		httpServletResponse.getWriter().println(atViewOptionsService.getVnfrtd(hm));

	}

	//insert
	@RequestMapping(value = "/core/eluon/vnfrtd/{vnfrtdId}", method = RequestMethod.POST)
	public void postVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnfrtdId") String vnfrtd_id) throws IOException {

		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");						

		String[] mnt_key_list = httpServletRequest.getParameterValues("mnt_key");		

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnfrtd_id);		
		hm.put("mnt_key_list", mnt_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.postVnfrtd(hm));

	}

	//update
	@RequestMapping(value = "/core/eluon/vnfrtd/{vnfrtdId}", method = RequestMethod.PUT)
	public void putVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnfrtdId") String vnfrtd_id) throws IOException {


		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");		


		String[] mnt_key_list = httpServletRequest.getParameterValues("mnt_key");		

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnfrtd_id);		
		hm.put("mnt_key_list", mnt_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.putVnfrtd(hm));

	}

	//delete
	@RequestMapping(value = "/core/eluon/vnfrtd/{vnfrtdId}", method = RequestMethod.DELETE)
	public void deleteVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("vnfrtdId") String vnfrtd_id) throws IOException {


		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");	


		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", vnfrtd_id);	

		httpServletResponse.getWriter().println(atViewOptionsService.deleteVnfrtd(hm));

	}


	//DF-CONSTRAINT
	//constraint는 DF에서만 사용하는 단일 옵션이기 때문에 앞에 nsd를 붙여 주었음.
	@RequestMapping(value = "/core/eluon/df-constraint/all/get", method = RequestMethod.GET)
	public void getConstraint(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getConstraint(hm));
	}


	@RequestMapping(value = "/core/eluon/df-constraint/all/put", method = RequestMethod.PUT)
	public void postAndputConstraint(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] constraint_no_list = httpServletRequest.getParameterValues("constraint_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("constraint_no_list", Arrays.asList(constraint_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutConstraint(hm));

	}

	@RequestMapping(value = "/core/eluon/df-constraint/all/delete", method = RequestMethod.PUT)
	public void deleteConstraint(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteConstraint(hm));

	}


	//TODO DF (nfv_flavour_tab) - ID값에 flag 값을 같이 받아서 유니크 키함을 유지한다.
	@RequestMapping(value = "/core/eluon/df/{dfId}", method = RequestMethod.GET)
	public void getDf(HttpServletResponse httpServletResponse, @PathVariable("dfId") String dfId) throws IOException {

		logger.info("----------------------------");		
		logger.info("dfId : " + dfId);		
		logger.info("----------------------------");		
		
		String flavour_id = null;
		String flavour_flag = null;
		
		flavour_flag = CommonUtil.getDfFlag(dfId);
		
		if(flavour_flag != null){
			flavour_id = dfId.substring(flavour_flag.length()+1 );
		}
		
		logger.info("----------------------------");		
		logger.info("flavour_flag : " + flavour_flag);		
		logger.info("flavour_id : " + flavour_id);			
		logger.info("----------------------------");		
		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("flavour_id", flavour_id);
		hm.put("flavour_flag", flavour_flag);
		httpServletResponse.getWriter().println(atViewOptionsService.getDf(hm));

	}

	
	//insert
	@RequestMapping(value = "/core/eluon/df/{dfId}", method = RequestMethod.POST)
	public void postDf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("dfId") String dfId) throws IOException {

		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");					
		
		//String changed_flavour_flag = httpServletRequest.getParameter("changed_flavour_flag");
		//String flavour_flag = httpServletRequest.getParameter("changed_flavour_flag");
		String flavour_flag = httpServletRequest.getParameter("flavour_flag");
		String flavour_key = httpServletRequest.getParameter("flavour_key");
		String[] constraint_key_list = httpServletRequest.getParameterValues("constraint_key");		
		String[] constituent_key_list = httpServletRequest.getParameterValues("constituent_key");


		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("flavour_id", dfId);
		hm.put("flavour_flag", flavour_flag);
		hm.put("flavour_key", flavour_key);	
		hm.put("constraint_key_list", constraint_key_list);
		hm.put("constituent_key_list", constituent_key_list);

		
		if (flavour_flag==null || flavour_flag.equals("")||flavour_flag.equals("NULL")
				||dfId==null || dfId.equals("")||dfId.equals("NULL")) {
			httpServletResponse.getWriter().println(-1);
		}		

		httpServletResponse.getWriter().println(atViewOptionsService.postDf(hm));

	}

	//update
	@RequestMapping(value = "/core/eluon/df/{dfId}", method = RequestMethod.PUT)
	public void putDf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("dfId") String dfId) throws IOException {
		
		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");		
		
		logger.info("----------------------------");		
		logger.info("dfId : " + dfId);		
		logger.info("----------------------------");		
		
		String flavour_id = null;
		String flavour_flag = null;

		
		flavour_flag = CommonUtil.getDfFlag(dfId);
		
		if(flavour_flag != null){
			flavour_id = dfId.substring(flavour_flag.length()+1 );
		}
		
		logger.info("----------------------------");		
		logger.info("flavour_flag : " + flavour_flag);		
		logger.info("flavour_id : " + flavour_id);	
		logger.info("----------------------------");		
	
		String flavour_key = httpServletRequest.getParameter("flavour_key");
		//String changed_flavour_flag =  httpServletRequest.getParameter("changed_flavour_flag");		
		String[] constraint_key_list = httpServletRequest.getParameterValues("constraint_key");		
		String[] constituent_key_list = httpServletRequest.getParameterValues("constituent_key");

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", dfId);	 // flag가 붙어 있는 전체 ID
		hm.put("flavour_id", flavour_id); //flag를 제외한 순수 ID
		hm.put("flavour_flag", flavour_flag); //현재 데이터의 flag
		//hm.put("changed_flavour_flag", changed_flavour_flag); // 사용자가 업데이트 하려고 하는 flag
		hm.put("flavour_key", flavour_key);
		hm.put("constraint_key_list", constraint_key_list);
		hm.put("constituent_key_list", constituent_key_list);

		if (flavour_flag==null || flavour_flag.equals("")||flavour_flag.equals("NULL")
				||dfId==null || dfId.equals("")|| dfId.equals("NULL")) {
			httpServletResponse.getWriter().println(-1);
		}

		httpServletResponse.getWriter().println(atViewOptionsService.putDf(hm));

	}

	//delete
	@RequestMapping(value = "/core/eluon/df/{dfId}", method = RequestMethod.DELETE)
	public void deleteDf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("dfId") String dfId) throws IOException {

		String flavour_id = null;
		String flavour_flag = null;
		
		flavour_flag = CommonUtil.getDfFlag(dfId);
		
		if(flavour_flag != null){
			flavour_id = dfId.substring(flavour_flag.length()+1 );
		}
		
		logger.info("----------------------------");		
		logger.info("df_id : " + dfId);	
		logger.info("flavour_flag : " + flavour_flag);		
		logger.info("flavour_id : " + flavour_id);	
		logger.info("----------------------------");		
	

		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", dfId);	 // flag가 붙어 있는 전체 ID
		hm.put("flavour_id", flavour_id); //flag를 제외한 순수 ID
		hm.put("flavour_flag", flavour_flag); //현재 데이터의 flag

		if (flavour_flag==null || flavour_flag.equals("")||flavour_flag.equals("NULL")
				||dfId==null || dfId.equals("")|| dfId.equals("NULL")) {
			httpServletResponse.getWriter().println(-1);
		}

		httpServletResponse.getWriter().println(atViewOptionsService.deleteDf(hm));
		
	}
	
	
	//TODO SDF
	@RequestMapping(value = "/core/eluon/sdf/{sdfId}", method = RequestMethod.GET)
	public void getSdf(HttpServletResponse httpServletResponse, @PathVariable("sdfId") String sdf_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", sdf_id);


		httpServletResponse.getWriter().println(atViewOptionsService.getSdf(hm));

	}

	//insert
	@RequestMapping(value = "/core/eluon/sdf/{sdfId}", method = RequestMethod.POST)
	public void postSdf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("sdfId") String sdf_id) throws IOException {

		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");						

		String sdf_flavour_key = httpServletRequest.getParameter("sdf_flavour_key");	
		String[] convnf_key_list = httpServletRequest.getParameterValues("convnf_key");		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", sdf_id);
		hm.put("sdf_flavour_key", sdf_flavour_key);	
		hm.put("convnf_key_list", convnf_key_list);

		httpServletResponse.getWriter().println(atViewOptionsService.postSdf(hm));

	}

	//update
	@RequestMapping(value = "/core/eluon/sdf/{sdfId}", method = RequestMethod.PUT)
	public void putSdf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("sdfId") String sdf_id) throws IOException {


		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");		


		String sdf_flavour_key = httpServletRequest.getParameter("sdf_flavour_key");	
		String[] convnf_key_list = httpServletRequest.getParameterValues("convnf_key");		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();

		hm.put("id", sdf_id);
		hm.put("sdf_flavour_key", sdf_flavour_key);	
		hm.put("convnf_key_list", convnf_key_list);
		
		httpServletResponse.getWriter().println(atViewOptionsService.putSdf(hm));

	}

	//delete
	@RequestMapping(value = "/core/eluon/sdf/{sdfId}", method = RequestMethod.DELETE)
	public void deleteSdf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("sdfId") String sdf_id) throws IOException {

		Enumeration params = httpServletRequest.getParameterNames();
		logger.info("----------------------------");
		while (params.hasMoreElements()){
			String name = (String)params.nextElement();
			logger.info(name + " : " +httpServletRequest.getParameter(name));
		}
		logger.info("----------------------------");	

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", sdf_id);	
		httpServletResponse.getWriter().println(atViewOptionsService.deleteSdf(hm));

	}
	
	
	// nfvi(vnf_status)
	@RequestMapping(value = "/core/eluon/state/all/get", method = RequestMethod.GET)
	public void getState(HttpServletResponse httpServletResponse) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();

		httpServletResponse.getWriter().println(atViewOptionsService.getState(hm));
	}
	
	@RequestMapping(value = "/core/eluon/state/all/put", method = RequestMethod.PUT)
	public void postAndPutState(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String[] state_no_list = httpServletRequest.getParameterValues("state_no");
		String[] version_no_list = httpServletRequest.getParameterValues("version_no");
		String[] item_list = httpServletRequest.getParameterValues("item");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("state_no_list", Arrays.asList(state_no_list));
		hm.put("version_no_list", Arrays.asList(version_no_list));
		hm.put("item_list", Arrays.asList(item_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.postAndPutState(hm));

	}

	@RequestMapping(value = "/core/eluon/state/all/delete", method = RequestMethod.PUT)
	public void deleteState(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String[] state_no_list = httpServletRequest.getParameterValues("state_no");
		String[] version_no_list = httpServletRequest.getParameterValues("version_no");
		String[] checked_list = httpServletRequest.getParameterValues("checked");

		HashMap<String, Object> hm = new HashMap<String,Object>();
		hm.put("state_no_list", Arrays.asList(state_no_list));
		hm.put("version_no_list", Arrays.asList(version_no_list));
		hm.put("checked_list", Arrays.asList(checked_list));

		httpServletResponse.getWriter().println(atViewOptionsService.deleteState(hm));

	}

	
}
