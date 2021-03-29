package com.eluon.vepc.mano.rest.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.common.CommonUtil;
import com.eluon.vepc.mano.common.CommonXmlUtil;
import com.eluon.vepc.mano.common.Constants;
import com.eluon.vepc.mano.service.ParserService;

@RestController
public class ParserController {
	
	protected final Logger logger = LoggerFactory.getLogger(ParserController.class);
	
	@Autowired
	private ParserService parserService;
	
	//vld
	@RequestMapping(value = "/core/eluon/NFV/VLD/{vld-id}/xml", method = RequestMethod.GET)
	public void getVldByXML(HttpServletResponse httpServletResponse, @PathVariable("vld-id") String vldId) throws IOException {
		
		logger.debug("getVldByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vldId", vldId);
		
		String result = parserService.getVldOne(hm);
		
		httpServletResponse.getWriter().println(result);
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VLD/{vld-id}/json", method = RequestMethod.GET)
	public void getVldByJson(HttpServletResponse httpServletResponse, @PathVariable("vld-id") String vldId) throws IOException {
		
		logger.debug("getVldByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vldId", vldId);
		
		String result = parserService.getVldOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VLD/all/xml", method = RequestMethod.GET)
	public void getVldAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVldAllByXML start");
		
		String result = parserService.getVldAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VLD/all/json", method = RequestMethod.GET)
	public void getVldAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVldAllByJson start");
		
		String result = parserService.getVldAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//pnfd
	@RequestMapping(value = "/core/eluon/NFV/PNFD/{pnfd-id}/xml", method = RequestMethod.GET)
	public void getPnfdByXML(HttpServletResponse httpServletResponse, @PathVariable("pnfd-id") String pnfdId) throws IOException {
		
		logger.debug("getPnfdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("pnfdId", pnfdId);
		
		String result = parserService.getPnfdOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/PNFD/{pnfd-id}/json", method = RequestMethod.GET)
	public void getPnfdByJson(HttpServletResponse httpServletResponse, @PathVariable("pnfd-id") String pnfdId) throws IOException {
		
		logger.debug("getPnfdByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("pnfdId", pnfdId);
		
		String result = parserService.getPnfdOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/PNFD/all/xml", method = RequestMethod.GET)
	public void getPnfdAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getPnfdAllByXML start");
		
		String result = parserService.getPnfdAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/PNFD/all/json", method = RequestMethod.GET)
	public void getPnfdAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getPnfdAllByJson start");
		
		String result = parserService.getPnfdAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}

	//cp
	@RequestMapping(value = "/core/eluon/NFV/CP/{cp-id}/xml", method = RequestMethod.GET)
	public void getCpByXML(HttpServletResponse httpServletResponse, @PathVariable("cp-id") String cpId) throws IOException {
		
		logger.debug("getCpByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("cpId", cpId);
		
		String result = parserService.getCpOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CP/{cp-id}/json", method = RequestMethod.GET)
	public void getCpByJson(HttpServletResponse httpServletResponse, @PathVariable("cp-id") String cpId) throws IOException {
		
		logger.debug("getCpByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("cpId", cpId);
		
		String result = parserService.getCpOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CP/all/xml", method = RequestMethod.GET)
	public void getCpAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getCpAllByXML start");
		
		String result = parserService.getCpAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CP/all/json", method = RequestMethod.GET)
	public void getCpAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getCpAllByJson start");
		
		String result = parserService.getCpAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//nfp
	@RequestMapping(value = "/core/eluon/NFV/NFP/{nfp-id}/xml", method = RequestMethod.GET)
	public void getNfpByXML(HttpServletResponse httpServletResponse, @PathVariable("nfp-id") String nfpId) throws IOException {
		
		logger.debug("getNfpByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfpId", nfpId);
		
		String result = parserService.getNfpOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFP/{nfp-id}/json", method = RequestMethod.GET)
	public void getNfpByJson(HttpServletResponse httpServletResponse, @PathVariable("nfp-id") String nfpId) throws IOException {
		
		logger.debug("getNfpByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfpId", nfpId);
		
		String result = parserService.getNfpOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFP/all/xml", method = RequestMethod.GET)
	public void getNfpAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfpAllByXML start");
		
		String result = parserService.getNfpAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFP/all/json", method = RequestMethod.GET)
	public void getNfpAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfpAllByJson start");
		
		String result = parserService.getNfpAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	
	
	//vnffgd
	@RequestMapping(value = "/core/eluon/NFV/VNFFGD/{vnffgd-id}/xml", method = RequestMethod.GET)
	public void getVnffgdByXML(HttpServletResponse httpServletResponse, @PathVariable("vnffgd-id") String vnffgdId) throws IOException {
		
		logger.debug("getVnffgdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnffgdId", vnffgdId);
		
		String result = parserService.getVnffgdOne(hm);
		
		httpServletResponse.getWriter().println(result);
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFFGD/all/xml", method = RequestMethod.GET)
	public void getVnffgdAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnffgdAllByXML start");
		
		String result = parserService.getVnffgdAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFFGD/{vnffgd-id}/json", method = RequestMethod.GET)
	public void getVnffgdByJson(HttpServletResponse httpServletResponse, @PathVariable("vnffgd-id") String vnffgdId) throws IOException {
		
		logger.debug("getVnffgdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnffgdId", vnffgdId);
		
		String result = parserService.getVnffgdOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFFGD/all/json", method = RequestMethod.GET)
	public void getVnffgdAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnffgdAllByXML start");
		
		String result = parserService.getVnffgdAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//vnfc
	@RequestMapping(value = "/core/eluon/NFV/VNFC/{vnfc-id}/xml", method = RequestMethod.GET)
	public void getVnfcByXML(HttpServletResponse httpServletResponse, @PathVariable("vnfc-id") String vnfcId) throws IOException {
		
		logger.debug("getVnfcByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfcId", vnfcId);
		
		String result = parserService.getVnfcOne(hm);
		
		httpServletResponse.getWriter().println(result);
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFC/all/xml", method = RequestMethod.GET)
	public void getVnfcAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfcAllByXML start");
		
		String result = parserService.getVnfcAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFC/{vnfc-id}/json", method = RequestMethod.GET)
	public void getVnfcByJson(HttpServletResponse httpServletResponse, @PathVariable("vnfc-id") String vnfcId) throws IOException {
		
		logger.debug("getVnfcByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfcId", vnfcId);
		
		String result = parserService.getVnfcOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFC/all/json", method = RequestMethod.GET)
	public void getVnfcAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfcAllByXML start");
		
		String result = parserService.getVnfcAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//vdu
	@RequestMapping(value = "/core/eluon/NFV/VDU/{vdu-id}/xml", method = RequestMethod.GET)
	public void getVduByXML(HttpServletResponse httpServletResponse, @PathVariable("vdu-id") String vduId) throws IOException {
		
		logger.debug("getVduByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vduId", vduId);
		
		String result = parserService.getVduOne(hm);
		
		httpServletResponse.getWriter().println(result);
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VDU/all/xml", method = RequestMethod.GET)
	public void getVduAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVduAllByXML start");
		
		String result = parserService.getVduAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VDU/{vdu-id}/json", method = RequestMethod.GET)
	public void getVduByJson(HttpServletResponse httpServletResponse, @PathVariable("vdu-id") String vduId) throws IOException {
		
		logger.debug("getVduByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vduId", vduId);
		
		String result = parserService.getVduOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VDU/all/json", method = RequestMethod.GET)
	public void getVduAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVduAllByXML start");
		
		String result = parserService.getVduAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	
	//df
	@RequestMapping(value = "/core/eluon/NFV/DF/{df-id}/xml", method = RequestMethod.GET)
	public void getDfByXML(HttpServletResponse httpServletResponse, @PathVariable("df-id") String dfId) throws IOException {
		
		logger.debug("getDfByXML start");
		
		String flavour_id = null;
		String flavour_flag = null;
		
		flavour_flag = CommonUtil.getDfFlag(dfId);
		
		if(flavour_flag != null){
			flavour_id = dfId.substring(flavour_flag.length()+1 );
		}
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("flavour_id", flavour_id);
		hm.put("flavour_flag", flavour_flag);
		
		String result = parserService.getDfOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/DF/{df-id}/json", method = RequestMethod.GET)
	public void getDfByJson(HttpServletResponse httpServletResponse, @PathVariable("df-id") String dfId) throws IOException {
		
		logger.debug("getDfByJson start");
		
		String flavour_id = null;
		String flavour_flag = null;
		
		flavour_flag = CommonUtil.getDfFlag(dfId);
		
		if(flavour_flag != null){
			flavour_id = dfId.substring(flavour_flag.length()+1 );
		}
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("flavour_id", flavour_id);
		hm.put("flavour_flag", flavour_flag);
		
		String result = parserService.getDfOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/DF/all/xml", method = RequestMethod.GET)
	public void getDfAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getDfAllByXML start");
		
		String result = parserService.getDfAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/DF/all/json", method = RequestMethod.GET)
	public void getDfAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getDfAllByJson start");
		
		String result = parserService.getDfAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//vnfd
	@RequestMapping(value = "/core/eluon/NFV/VNFD/{vnfd-id}/xml", method = RequestMethod.GET)
	public void getVnfdByXML(HttpServletResponse httpServletResponse, @PathVariable("vnfd-id") String vnfdId) throws IOException {
		
		logger.debug("getVnfdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfdId", vnfdId);
		
		String result = parserService.getVnfdOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFD/{vnfd-id}/json", method = RequestMethod.GET)
	public void getVnfdByJson(HttpServletResponse httpServletResponse, @PathVariable("vnfd-id") String vnfdId) throws IOException {
		
		logger.debug("getVnfdByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfdId", vnfdId);
		
		String result = parserService.getVnfdOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFD/all/xml", method = RequestMethod.GET)
	public void getVnfdAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfdAllByXML start");
		
		String result = parserService.getVnfdAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFD/all/json", method = RequestMethod.GET)
	public void getVnfdAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfdAllByJson start");
		
		String result = parserService.getVnfdAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//sdf
	@RequestMapping(value = "/core/eluon/NFV/SDF/{sdf-id}/xml", method = RequestMethod.GET)
	public void getSdfByXML(HttpServletResponse httpServletResponse, @PathVariable("sdf-id") String sdfId) throws IOException {
		
		logger.debug("getSdfByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("sdfId", sdfId);
		
		String result = parserService.getSdfOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/SDF/{sdf-id}/json", method = RequestMethod.GET)
	public void getSdfByJson(HttpServletResponse httpServletResponse, @PathVariable("sdf-id") String sdfId) throws IOException {
		
		logger.debug("getSdfByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("sdfId", sdfId);
		
		String result = parserService.getSdfOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/SDF/all/xml", method = RequestMethod.GET)
	public void getSdfAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getSdfAllByXML start");
		
		String result = parserService.getSdfAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/SDF/all/json", method = RequestMethod.GET)
	public void getSdfAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getSdfAllByJson start");
		
		String result = parserService.getSdfAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//nsd
	@RequestMapping(value = "/core/eluon/NFV/NSD/{nsd-id}/xml", method = RequestMethod.GET)
	public void getNsdByXML(HttpServletResponse httpServletResponse, @PathVariable("nsd-id") String nsdId) throws IOException {
		
		logger.debug("getNsdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nsdId", nsdId);
		
		String result = parserService.getNsdOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NSD/{nsd-id}/json", method = RequestMethod.GET)
	public void getNsdByJson(HttpServletResponse httpServletResponse, @PathVariable("nsd-id") String nsdId) throws IOException {
		
		logger.debug("getNsdByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nsdId", nsdId);
		
		String result = parserService.getNsdOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NSD/all/xml", method = RequestMethod.GET)
	public void getNsdAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNsdAllByXML start");
		
		String result = parserService.getNsdAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NSD/all/json", method = RequestMethod.GET)
	public void getNsdAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNsdAllByJson start");
		
		String result = parserService.getNsdAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//vnfrtd
	@RequestMapping(value = "/core/eluon/NFV/VNFRTD/{vnfrtd-id}/xml", method = RequestMethod.GET)
	public void getVnfrtdByXML(HttpServletResponse httpServletResponse, @PathVariable("vnfrtd-id") String vnfrtdId) throws IOException {
		
		logger.debug("getVnfrtdByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfrtdId", vnfrtdId);
		
		String result = parserService.getVnfrtdOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFRTD/{vnfrtd-id}/json", method = RequestMethod.GET)
	public void getVnfrtdByJson(HttpServletResponse httpServletResponse, @PathVariable("vnfrtd-id") String vnfrtdId) throws IOException {
		
		logger.debug("getVnfrtdByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vnfrtdId", vnfrtdId);
		
		String result = parserService.getVnfrtdOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFRTD/all/xml", method = RequestMethod.GET)
	public void getVnfrtdAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfrtdAllByXML start");
		
		String result = parserService.getVnfrtdAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/VNFRTD/all/json", method = RequestMethod.GET)
	public void getVnfrtdAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getVnfrtdAllByJson start");
		
		String result = parserService.getVnfrtdAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	
	//TODO nfvi 수정.
	//nfvi(기존 nfvid)
	@RequestMapping(value = "/core/eluon/NFV/NFVI/{nfvi-id}/xml", method = RequestMethod.GET)
	public void getNfvidByXML(HttpServletResponse httpServletResponse, @PathVariable("nfvi-id") String nfvidId) throws IOException {
		
		logger.debug("getNfviByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvidId", nfvidId);
		
		String result = parserService.getNfvidOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/{nfvid-id}/json", method = RequestMethod.GET)
	public void getNfvidByJson(HttpServletResponse httpServletResponse, @PathVariable("nfvid-id") String nfvidId) throws IOException {
		
		logger.debug("getNfviByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvidId", nfvidId);
		
		String result = parserService.getNfvidOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/all/xml", method = RequestMethod.GET)
	public void getNfvidAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfviAllByXML start");
		
		String result = parserService.getNfvidAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/all/json", method = RequestMethod.GET)
	public void getNfvidAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfviAllByJson start");
		
		String result = parserService.getNfvidAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}	
	
	
/*	@RequestMapping(value = "/core/eluon/NFV/NFVI/{nfvi-id}/xml", method = RequestMethod.GET)
	public void getNfvidByXML(HttpServletResponse httpServletResponse, @PathVariable("nfvi-id") String nfvidId) throws IOException {
		
		logger.debug("getNfviByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvidId", nfvidId);
		
		String result = parserService.getNfvidOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/{nfvid-id}/json", method = RequestMethod.GET)
	public void getNfvidByJson(HttpServletResponse httpServletResponse, @PathVariable("nfvid-id") String nfvidId) throws IOException {
		
		logger.debug("getNfviByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvidId", nfvidId);
		
		String result = parserService.getNfvidOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
		
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/all/xml", method = RequestMethod.GET)
	public void getNfvidAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfviAllByXML start");
		
		String result = parserService.getNfvidAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVI/all/json", method = RequestMethod.GET)
	public void getNfvidAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfviAllByJson start");
		
		String result = parserService.getNfvidAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}*/
	
	/** nfvic(기존 nfvidc) */
	@RequestMapping(value = "/core/eluon/NFV/NFVIC/{nfvic-id}/xml", method = RequestMethod.GET)
	public void getNfvicByXML(HttpServletResponse httpServletResponse, @PathVariable("nfvic-id") String nfvic_id) throws IOException {
		
		logger.debug("getNfvicByXML start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvicId", nfvic_id);
		
		String result = parserService.getNfvicOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVIC/{nfvic-id}/json", method = RequestMethod.GET)
	public void getNfvicByJson(HttpServletResponse httpServletResponse, @PathVariable("nfvic-id") String nfvic_id) throws IOException {
		
		logger.debug("getNfvicByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("nfvicId", nfvic_id);
		
		String result = parserService.getNfvicOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVIC/all/xml", method = RequestMethod.GET)
	public void getNfvicAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfvicAllByXML start");
		
		String result = parserService.getNfvicAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/NFVIC/all/json", method = RequestMethod.GET)
	public void getNfvicAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getNfvicAllByJson start");
		
		String result = parserService.getNfvicAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	//constituent 
	@RequestMapping(value = "/core/eluon/NFV/CONSTITUENT/{constituent-id}/xml", method = RequestMethod.GET)
	public void getConstituentByXml(HttpServletResponse httpServletResponse, @PathVariable("constituent-id") String constituent_id) throws IOException {
		
		logger.debug("getConstituentByXml start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vdu_id", constituent_id);
		
		String result = parserService.getConstituentOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CONSTITUENT/{constituent-id}/json", method = RequestMethod.GET)
	public void getConstituentByJson(HttpServletResponse httpServletResponse, @PathVariable("constituent-id") String constituent_id) throws IOException {
		
		logger.debug("getConstituentByJson start");
		
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("vdu_id", constituent_id);
		
		String result = parserService.getConstituentOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CONSTITUENT/all/xml", method = RequestMethod.GET)
	public void getConstituentAllByXML(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getConstituentAllByXML start");
		
		String result = parserService.getConstituentAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CONSTITUENT/all/json", method = RequestMethod.GET)
	public void getConstituentAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getConstituentAllByJson start");
		
		String result = parserService.getConstituentAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
		
	//cvnf(constituent_vnf)
	@RequestMapping(value = "/core/eluon/NFV/CVNF/{cvnf-id}/xml", method = RequestMethod.GET)
	public void getCvnfByXml(HttpServletResponse httpServletResponse, @PathVariable("cvnf-id") String cvnf_id) throws IOException {
		
		logger.debug("getCvnfByXml start");
		
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

		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);
		
		String result = parserService.getCvnfOne(hm);
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CVNF/{cvnf-id}/json", method = RequestMethod.GET)
	public void getCvnfByJson(HttpServletResponse httpServletResponse, @PathVariable("cvnf-id") String cvnf_id) throws IOException {
		
		logger.debug("getCvnfByJson start");
		
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

		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("cvnf_id", cvnf_id);
		hm.put("vnf_id", vnf_id);
		hm.put("flavour_nm", flavour_nm);
		
		String result = parserService.getCvnfOne(hm);
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CVNF/all/xml", method = RequestMethod.GET)
	public void getCvnfAllByXml(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getCvnfAllByXml start");
		
		String result = parserService.getCvnfAll();
		
		httpServletResponse.getWriter().println(result);
	}
	
	@RequestMapping(value = "/core/eluon/NFV/CVNF/all/json", method = RequestMethod.GET)
	public void getCvnfAllByJson(HttpServletResponse httpServletResponse) throws IOException {
		
		logger.debug("getCvnfAllByJson start");
		
		String result = parserService.getCvnfAll();
		
		httpServletResponse.getWriter().println(CommonXmlUtil.getXmlToJson(result));
	}
		
}



