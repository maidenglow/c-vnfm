package com.eluon.vepc.mano.rest.controller;

/**
 * @author seon
 *
 */
import java.io.IOException;
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

import com.eluon.vepc.mano.common.Constants;
import com.eluon.vepc.mano.service.AtViewSubService;


@RestController
public class AtViewSubController {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private  AtViewSubService atViewSubService;
	
	
	//TODO constituent
	@RequestMapping(value = "/core/eluon/constituent/{constituentId}", method = RequestMethod.GET)
	public void getConstituent(HttpServletResponse httpServletResponse, @PathVariable("constituentId") String constituent_id) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", constituent_id);
		
		httpServletResponse.getWriter().println(atViewSubService.getConstituent(hm));
		
	}

	//insert
	@RequestMapping(value = "/core/eluon/constituent/{constituentId}", method = RequestMethod.POST)
	public void postVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("constituentId") String constituent_id) throws IOException {

		String instance_cnt = httpServletRequest.getParameter("instance_cnt");
		String[] vnfc_key_list = httpServletRequest.getParameterValues("vnfc_key");		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", constituent_id);	
		hm.put("instance_cnt", instance_cnt);
		hm.put("vnfc_key_list", vnfc_key_list);
		
		httpServletResponse.getWriter().println(atViewSubService.postConstituent(hm));
	
	}

	//update
	@RequestMapping(value = "/core/eluon/constituent/{constituentId}", method = RequestMethod.PUT)
	public void putVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("constituentId") String constituent_id) throws IOException {
		
		String instance_cnt = httpServletRequest.getParameter("instance_cnt");
		String[] vnfc_key_list = httpServletRequest.getParameterValues("vnfc_key");		
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", constituent_id);	
		hm.put("instance_cnt", instance_cnt);
		hm.put("vnfc_key_list", vnfc_key_list);
		
		httpServletResponse.getWriter().println(atViewSubService.putConstituent(hm));
		
	}
	
	//delete
	@RequestMapping(value = "/core/eluon/constituent/{constituentId}", method = RequestMethod.DELETE)
	public void deleteVnfrtd(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest,  @PathVariable("constituentId") String constituent_id) throws IOException {

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", constituent_id);	
		
		httpServletResponse.getWriter().println(atViewSubService.deleteConstituent(hm));
		
	}		
	
	
	//TODO nfvic
	@RequestMapping(value = "/core/eluon/nfvic/{nfvicId}", method = RequestMethod.GET)
	public void getNfvic(HttpServletResponse httpServletResponse, @PathVariable("nfvicId") String nfvic_id) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfvic_id);
		
		httpServletResponse.getWriter().println(atViewSubService.getNfvic(hm));
		
	}
	
	//insert
	@RequestMapping(value = "/core/eluon/nfvic/{nfvicId}", method = RequestMethod.POST)
	public void postNfvic(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("nfvicId") String nfvic_id) throws IOException {

		String[] mnt_key_list = httpServletRequest.getParameterValues("mnt_key");	
		String[] func_key_list = httpServletRequest.getParameterValues("func_key");	
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfvic_id);	
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("func_key_list", func_key_list);
		
		httpServletResponse.getWriter().println(atViewSubService.postNfvic(hm));
	
	}
	
	//update
	@RequestMapping(value = "/core/eluon/nfvic/{nfvicId}", method = RequestMethod.PUT)
	public void putNfvic(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("nfvicId") String nfvic_id) throws IOException {

		String[] mnt_key_list = httpServletRequest.getParameterValues("mnt_key");	
		String[] func_key_list = httpServletRequest.getParameterValues("func_key");	
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfvic_id);	
		hm.put("mnt_key_list", mnt_key_list);
		hm.put("func_key_list", func_key_list);
		
		httpServletResponse.getWriter().println(atViewSubService.putNfvic(hm));
	
	}
	
	//delete
	@RequestMapping(value = "/core/eluon/nfvic/{nfvicId}", method = RequestMethod.DELETE)
	public void deleteNfvic(HttpServletResponse httpServletResponse, @PathVariable("nfvicId") String nfvic_id) throws IOException {
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("id", nfvic_id);
		
		httpServletResponse.getWriter().println(atViewSubService.deleteNfvic(hm));
		
	}
	
	//TODO cvnf(constituent vnf)	
	@RequestMapping(value = "/core/eluon/cvnf/{cvnfId}", method = RequestMethod.GET)
	public void getCvnf(HttpServletResponse httpServletResponse, @PathVariable("cvnfId") String cvnf_id) throws IOException {
		
		String vnf_id = null;
		String flavour_nm = null;
		
		if(!"".equals(cvnf_id)) {
			String[] epc_small = cvnf_id.split(Constants.EPC_SMALL);
			
			if(epc_small.length > 1) {
				flavour_nm = Constants.EPC_SMALL;
				vnf_id = epc_small[1].substring(1);
			} 
			else {
				String[] epc_medium = cvnf_id.split(Constants.EPC_MEDIUM);
				
				if(epc_medium.length > 1) {
					flavour_nm = Constants.EPC_MEDIUM;
					vnf_id = epc_medium[1].substring(1);
				} 
				else {
					String[] epc_large = cvnf_id.split(Constants.EPC_LARGE);
					
					if(epc_large.length > 1) {
						flavour_nm = Constants.EPC_LARGE;
						vnf_id = epc_large[1].substring(1);
					} 
					else {
						httpServletResponse.getWriter().println(-1);
					}
				}
			}
		} else {
			httpServletResponse.getWriter().println(-1);
		}

		logger.info("cvnf_id: " + cvnf_id + ", vnf_id: " + vnf_id + ", flavour_nm: " + flavour_nm);
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);
		
		httpServletResponse.getWriter().println(atViewSubService.getCvnf(hm));
	}	
	
	//insert
	@RequestMapping(value = "/core/eluon/cvnf/{cvnfId}", method = RequestMethod.POST)
	public void postCvnf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("cvnfId") String cvnf_id) throws IOException {
		
		String vnf_id = null;
		String flavour_nm = null;
		
		if(!"".equals(cvnf_id)) {
			String[] epc_small = cvnf_id.split(Constants.EPC_SMALL);
			
			if(epc_small.length > 1) {
				flavour_nm = Constants.EPC_SMALL;
				vnf_id = epc_small[1].substring(1);
			} 
			else {
				String[] epc_medium = cvnf_id.split(Constants.EPC_MEDIUM);
				
				if(epc_medium.length > 1) {
					flavour_nm = Constants.EPC_MEDIUM;
					vnf_id = epc_medium[1].substring(1);
				} 
				else {
					String[] epc_large = cvnf_id.split(Constants.EPC_LARGE);
					
					if(epc_large.length > 1) {
						flavour_nm = Constants.EPC_LARGE;
						vnf_id = epc_large[1].substring(1);
					} 
					else {
						httpServletResponse.getWriter().println(-1);
					}
				}
			}
		} else {
			httpServletResponse.getWriter().println(-1);
		}
		
		String rddc_model = httpServletRequest.getParameter("redundancy_model");	
		String affinity = httpServletRequest.getParameter("affinity");	
		String vnf_cap = httpServletRequest.getParameter("vnf_cap");	
		String vnf_instance_cnt = httpServletRequest.getParameter("number_of_instances");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);
		hm.put("rddc_model", rddc_model);
		hm.put("affinity", affinity);
		hm.put("vnf_cap", vnf_cap);
		hm.put("vnf_instance_cnt", vnf_instance_cnt);
		
		httpServletResponse.getWriter().println(atViewSubService.postCvnf(hm));
	}
	
	//update
	@RequestMapping(value = "/core/eluon/cvnf/{cvnfId}", method = RequestMethod.PUT)
	public void putCvnf(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @PathVariable("cvnfId") String cvnf_id) throws IOException {

		String vnf_id = null;
		String flavour_nm = null;
		
		if(!"".equals(cvnf_id)) {
			String[] epc_small = cvnf_id.split(Constants.EPC_SMALL);
			
			if(epc_small.length > 1) {
				flavour_nm = Constants.EPC_SMALL;
				vnf_id = epc_small[1].substring(1);
			} 
			else {
				String[] epc_medium = cvnf_id.split(Constants.EPC_MEDIUM);
				
				if(epc_medium.length > 1) {
					flavour_nm = Constants.EPC_MEDIUM;
					vnf_id = epc_medium[1].substring(1);
				} 
				else {
					String[] epc_large = cvnf_id.split(Constants.EPC_LARGE);
					
					if(epc_large.length > 1) {
						flavour_nm = Constants.EPC_LARGE;
						vnf_id = epc_large[1].substring(1);
					} 
					else {
						httpServletResponse.getWriter().println(-1);
					}
				}
			}
		} else {
			httpServletResponse.getWriter().println(-1);
		}
		
		String rddc_model = httpServletRequest.getParameter("redundancy_model");	
		String affinity = httpServletRequest.getParameter("affinity");	
		String vnf_cap = httpServletRequest.getParameter("vnf_cap");	
		String vnf_instance_cnt = httpServletRequest.getParameter("number_of_instances");
		
		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);
		hm.put("rddc_model", rddc_model);
		hm.put("affinity", affinity);
		hm.put("vnf_cap", vnf_cap);
		hm.put("vnf_instance_cnt", vnf_instance_cnt);
		
		httpServletResponse.getWriter().println(atViewSubService.putCvnf(hm));
	
	}
	
	//delete
	@RequestMapping(value = "/core/eluon/cvnf/{cvnfId}", method = RequestMethod.DELETE)
	public void deleteCvnf(HttpServletResponse httpServletResponse, @PathVariable("cvnfId") String cvnf_id) throws IOException {
			
		String vnf_id = null;
		String flavour_nm = null;
		
		if(!"".equals(cvnf_id)) {
			String[] epc_small = cvnf_id.split(Constants.EPC_SMALL);
			
			if(epc_small.length > 1) {
				flavour_nm = Constants.EPC_SMALL;
				vnf_id = epc_small[1].substring(1);
			} 
			else {
				String[] epc_medium = cvnf_id.split(Constants.EPC_MEDIUM);
				
				if(epc_medium.length > 1) {
					flavour_nm = Constants.EPC_MEDIUM;
					vnf_id = epc_medium[1].substring(1);
				} 
				else {
					String[] epc_large = cvnf_id.split(Constants.EPC_LARGE);
					
					if(epc_large.length > 1) {
						flavour_nm = Constants.EPC_LARGE;
						vnf_id = epc_large[1].substring(1);
					} 
					else {
						httpServletResponse.getWriter().println(-1);
					}
				}
			}
		} else {
			httpServletResponse.getWriter().println(-1);
		}

		HashMap<String,Object> hm = new HashMap<String,Object>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);		
		
		httpServletResponse.getWriter().println(atViewSubService.deleteCvnf(hm));
		
	}

	
}
