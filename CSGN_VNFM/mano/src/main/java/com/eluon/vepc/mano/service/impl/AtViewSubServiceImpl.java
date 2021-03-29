package com.eluon.vepc.mano.service.impl;

/**
 * @author seon
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eluon.vepc.mano.common.CommonXmlUtil;
import com.eluon.vepc.mano.common.dao.NFVCommonDAO;
import com.eluon.vepc.mano.dao.AtViewDAO;
import com.eluon.vepc.mano.dao.AtViewSubDAO;
import com.eluon.vepc.mano.service.AtViewSubService;

@Service("atViewSubService")
public class AtViewSubServiceImpl implements AtViewSubService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AtViewDAO atViewDAO;
	
	@Autowired
	private AtViewSubDAO atViewSubDAO;
	
	@Autowired
	private NFVCommonDAO nfvCommonDAO;
	
	@Resource(name="nfv")
	private Properties nfv;
	
	// constituent
	@Transactional
	public String getConstituent(HashMap<String, Object> hm)
	{
		List<LinkedHashMap<String, String>> resultList = atViewSubDAO.getConstituent(hm);
		
		if(resultList == null || resultList.size() < 1){

			Element rootElement = new Element("constituent");
			return CommonXmlUtil.getElementToString(rootElement);
		}
		
		List<String> id = new ArrayList<String>();
		List<String> instance_cnt = new ArrayList<String>();
		List<String> vnfc_no = new ArrayList<String>();
		List<String> vnfc_id = new ArrayList<String>();

		for(LinkedHashMap<String, String> map : resultList){
			id.add(map.get("vdu_id"));
			instance_cnt.add(map.get("instance_cnt"));
			vnfc_no.add(map.get("vnfc_no"));
			vnfc_id.add(map.get("vnfc_id"));
		}
				
		Element rootElement = new Element("constituent");
		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("instance_cnt").addContent(instance_cnt.get(0)));
		
		Element vnfc_list_el = new Element("vnfc_list");		
		int i = 0;
		for(String s : vnfc_id) {
			if( !"NULL".equals(s) ){
				Element vnfc_el = new Element("vnfc");
				vnfc_el.addContent(new Element("key").addContent(vnfc_no.get(i)));
				vnfc_el.addContent(new Element("id").addContent(s));

				vnfc_list_el.addContent(vnfc_el);
			}

			i++;
		}
		rootElement.addContent(vnfc_list_el);		
		
		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postConstituent(HashMap<String, Object> hm) {
		//table_seq
		String cstt_seq = nfvCommonDAO.getSeq("75");
		String vnfc_ref_seq = nfvCommonDAO.getSeq("88");	
		logger.info("----------------------cstt_seq: " + cstt_seq + ", vnfc_ref_seq: " + vnfc_ref_seq);
		
		hm.put("cstt_no", cstt_seq);
		hm.put("vnfc_ref_no", vnfc_ref_seq);
		
		// 1. nfv_constituent_tab insert
		atViewSubDAO.postConstituent(hm);
		
		// 2. nfv_vnfc_ref insert
		String[] vnfc_key_list = (String[])hm.get("vnfc_key_list");
		for(String s : vnfc_key_list){
			hm.put("vnfc_no", s);
			atViewSubDAO.postVnfcRef(hm);
		}
		
		//3. nfv_tmenu_ref insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("CONSTITUENT"));
		hm.put("func_ref_no", cstt_seq);
		atViewDAO.postTmenuRef(hm);		
		
		return 1;
	}

	@Transactional
	public int putConstituent(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> constituent_result = atViewSubDAO.getConstituentOne(hm);
		
		// 1. nfv_vnfc_ref delete
		String vnfc_ref_no = constituent_result.get("vnfc_ref_no");
		hm.put("vnfc_ref_no", vnfc_ref_no);
		atViewSubDAO.deleteVnfcRef(hm);
		
		// 2. nfv_vnfc_ref insert
		String[] vnfc_key_list = (String[])hm.get("vnfc_key_list");		
		for(String s : vnfc_key_list){
			hm.put("vnfc_no", s);
			atViewSubDAO.postVnfcRef(hm);
		}
		
		// 3. nfv_constituent update
		atViewSubDAO.putConstituent(hm);
		
		return 1;
	}

	@Transactional
	public int deleteConstituent(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> constituent_result = atViewSubDAO.getConstituentOne(hm);

		// 1. nfv_constituent_tab를 참고하는 nfv_constituent_ref 있는지 체크
		String cstt_no = constituent_result.get("cstt_no");
		hm.put("cstt_no", cstt_no);
		int result = atViewSubDAO.passableDeleteConstituent(hm);
		
		if(result == 0) {
			// 2. nfv_vnfc_ref delete
			String vnfc_ref_no = constituent_result.get("vnfc_ref_no");
			hm.put("vnfc_ref_no", vnfc_ref_no);
			atViewSubDAO.deleteVnfcRef(hm);
			
			// 3. nfv_constituent delete
			atViewSubDAO.deleteConstituent(hm);
			
			// 4. nfv_tmenu_ref delete
			hm.put("func_ref_no", cstt_no);
			atViewDAO.deleteTmenuRef(hm);
		} else {
			return 0;
		}

		return 1;
	}

	// nfvic
	@Transactional
	public String getNfvic(HashMap<String, Object> hm) {
		List<LinkedHashMap<String, String>> resultList = atViewSubDAO.getNfvic(hm);
		
		if(resultList == null || resultList.size() < 1){

			Element rootElement = new Element("nfvic");
			return CommonXmlUtil.getElementToString(rootElement);
		}
		
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
		
		Element resource_list_el = new Element("resource_list");	
		int i = 0;
		for(String s : mnt_item) {
			if( !"NULL".equals(s) ){
				Element mnt_el = new Element("resource");
				mnt_el.addContent(new Element("key").addContent(mnt_no.get(i)));
				mnt_el.addContent(new Element("id").addContent(s));

				resource_list_el.addContent(mnt_el);
			}

			i++;
		}
		rootElement.addContent(resource_list_el);	
		
		Element vnf_el = new Element("vnf_list");	
		int j = 0;
		for(String k : ver_id) {
			if( !"NULL".equals(k) ){
				Element vnf_info_el = new Element("vnf");
				vnf_info_el.addContent(new Element("key").addContent(function_no.get(j)));
				vnf_info_el.addContent(new Element("id").addContent(k));
				vnf_info_el.addContent(new Element("status").addContent(ver_item.get(j)));
				
				vnf_el.addContent(vnf_info_el);
			}

			j++;
		}
		rootElement.addContent(vnf_el);
	
		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postNfvic(HashMap<String, Object> hm) {
		//table_seq
		String nfvidc_seq = nfvCommonDAO.getSeq("95");
		String mnt_ref_seq = nfvCommonDAO.getSeq("42");
		String func_ref_seq = nfvCommonDAO.getSeq("11");		
		logger.info("----------------------nfvidc_seq: " + nfvidc_seq + ", mnt_ref_seq: " + mnt_ref_seq + ", func_ref_seq: " + func_ref_seq);
		
		hm.put("nfvidc_no", nfvidc_seq);
		hm.put("mnt_ref_no", mnt_ref_seq);
		hm.put("func_ref_no", func_ref_seq);
		
		// 1. nfv_constituent_tab insert
		atViewSubDAO.postNfvic(hm);
		
		// 2. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[])hm.get("mnt_key_list");
		for(String s : mnt_key_list){
			hm.put("mnt_no", s);
			atViewSubDAO.postMntRef(hm);
		}
		
		// 3. nfv_function_ref insert
		String[] func_key_list = (String[])hm.get("func_key_list");
		for(String s : func_key_list){
			hm.put("func_no", s);
			atViewSubDAO.postFuncRef(hm);
		}
		
		//4. nfv_tmenu_ref insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NFVIC"));
		hm.put("func_ref_no", nfvidc_seq);
		atViewDAO.postTmenuRef(hm);		
	
		return 1;
	}

	@Transactional
	public int putNfvic(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nfvic_result = atViewSubDAO.getNfvicOne(hm);
		
		// 1. nfv_monitoring_ref delete
		String mnt_ref_no = nfvic_result.get("mnt_ref_no");
		hm.put("mnt_ref_no", mnt_ref_no);
		atViewSubDAO.deleteMntRef(hm);
		
		// 2. nfv_function_ref delete
		String func_ref_no = nfvic_result.get("func_ref_no");
		hm.put("func_ref_no", func_ref_no);
		atViewSubDAO.deleteFuncRef(hm);
		
		// 3. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[])hm.get("mnt_key_list");
		for(String s : mnt_key_list){
			hm.put("mnt_no", s);
			atViewSubDAO.postMntRef(hm);
		}
		
		// 4. nfv_function_ref insert
		String[] func_key_list = (String[])hm.get("func_key_list");
		for(String s : func_key_list){
			hm.put("func_no", s);
			atViewSubDAO.postFuncRef(hm);
		}
		
		return 1;
	}

	@Transactional
	public int deleteNfvic(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nfvic_result = atViewSubDAO.getNfvicOne(hm);

		// 1. nfv_monitoring_ref delete
		String mnt_ref_no = nfvic_result.get("mnt_ref_no");
		hm.put("mnt_ref_no", mnt_ref_no);
		atViewSubDAO.deleteMntRef(hm);
		
		// 2. nfv_function_ref delete
		String func_ref_no = nfvic_result.get("func_ref_no");
		hm.put("func_ref_no", func_ref_no);
		atViewSubDAO.deleteFuncRef(hm);
		
		// 3. nfv_nfvidc_tab delete
		atViewSubDAO.deleteNfvic(hm);
		
		// 4. nfv_tmenu_ref delete
		String nfvidc_no = nfvic_result.get("nfvidc_no");
		hm.put("func_ref_no", nfvidc_no);
		atViewDAO.deleteTmenuRef(hm);
		
		return 1;
	}

	// cvnf(constituent vnf)
	@Transactional
	public String getCvnf(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> resultList = atViewSubDAO.getCvnf(hm);
		
		if(resultList == null || resultList.size() < 1){
			Element rootElement = new Element("cvnf");
			return CommonXmlUtil.getElementToString(rootElement);
		}
		
		String version_no = resultList.get("version_no");
		String vnf_id = resultList.get("id");
		String vendor = resultList.get("vendor");
		String version = resultList.get("version");
		
		String convnf_no = resultList.get("convnf_no");
		String flavour_nm = resultList.get("flavour_nm");
		String rddc_model = resultList.get("rddc_model");
		String affinity = resultList.get("affinity");
		String vnf_cap = resultList.get("vnf_cap");
		String vnf_instance_cnt = resultList.get("vnf_instance_cnt");
		
		
		Element rootElement = new Element("cvnf");
		rootElement.addContent(new Element("key").addContent(convnf_no));
		rootElement.addContent(new Element("id").addContent(flavour_nm + "_" + vnf_id));
		
		rootElement.addContent(new Element("vnf_flavour_id_reference").addContent(flavour_nm));
		rootElement.addContent(new Element("redundancy_model").addContent(rddc_model));
		rootElement.addContent(new Element("affinity").addContent(affinity));
		rootElement.addContent(new Element("vnf_cap").addContent(vnf_cap));
		rootElement.addContent(new Element("number_of_instances").addContent(vnf_instance_cnt));
		
		Element vnf_info_el = new Element("vnf");
		vnf_info_el.addContent(new Element("key").addContent(version_no));
		vnf_info_el.addContent(new Element("item").addContent(vnf_id));
		rootElement.addContent(vnf_info_el);
		
		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postCvnf(HashMap<String, Object> hm) {
		int cnt = atViewSubDAO.getCvnfCount(hm);
		if(cnt > 0) {
			return -1;
		}
		
		//table_seq
		String convnf_seq = nfvCommonDAO.getSeq("79");
		logger.info("----------------------convnf_seq: " + convnf_seq);
		
		hm.put("convnf_no", convnf_seq);
		
		// 1. nfv_version_tab select
		LinkedHashMap<String, String> version_result = atViewSubDAO.getVersion(hm);
		if(version_result == null || version_result.size() < 1) {
			return -1;
		}
		String version_no = version_result.get("version_no");
		hm.put("version_no", version_no);
		
		// 2. nfv_convnf_tab insert
		atViewSubDAO.postCvnf(hm);
		
		// 2. nfv_tmenu_ref insert 
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("CVNF"));
		hm.put("func_ref_no", convnf_seq);
		atViewDAO.postTmenuRef(hm);
		
		return 1;
	}

	@Transactional
	public int putCvnf(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> cvnf_result = atViewSubDAO.getCvnfOne(hm);
		String convnf_no = cvnf_result.get("convnf_no");
		
		// 1. nfv_convnf_tab update
		hm.put("convnf_no", convnf_no);
		atViewSubDAO.putCvnf(hm);
		
		return 1;
	}

	@Transactional
	public int deleteCvnf(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> cvnf_result = atViewSubDAO.getCvnfOne(hm);
		
		// 1. nfv_convnf_tab를 참고하는 nfv_convnf_ref 있는지 체크
		String convnf_no = cvnf_result.get("convnf_no");
		hm.put("convnf_no", convnf_no);
		
		int result = atViewSubDAO.passableDeleteCvnf(hm);
		if(result == 0) {
			// 2. nfv_convnf_tab delete
			atViewSubDAO.deleteCvnf(hm);
			
			// 3. nfv_tmenu_ref delete
			hm.put("func_ref_no", convnf_no);
			atViewDAO.deleteTmenuRef(hm);
		} else {
			return 0;
		}
		
		return 1;
	}	

	
}
