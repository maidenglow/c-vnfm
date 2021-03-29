package com.eluon.vepc.mano.service.impl;

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
import com.eluon.vepc.mano.dao.AtViewOptionsDAO;
import com.eluon.vepc.mano.service.AtViewOptionsService;


@Service("atViewOptionsService")
public class AtViewOptionsServiceImpl implements AtViewOptionsService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AtViewOptionsDAO atViewOptionsDAO;

	@Autowired
	private AtViewDAO atViewDAO;

	@Autowired
	private NFVCommonDAO nfvCommonDAO;

	@Resource(name="nfv")
	private Properties nfv;

	//##################Options####################
	//policy
	@Transactional
	public String getPolicy(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getPolicy(hm);

		if(resultList == null || resultList.size() < 1){

			Element rootElement = new Element("policy_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		for (int i = 0; i < resultList.size(); i++) {
			logger.info(" i : " + i);
			logger.info(resultList.get(i).get("policy"));
		}

		Element rootElement = new Element("policy_list");
		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element policy_item = new Element("policy_item");
			policy_item.addContent(new Element("policy_no").addContent(resultMap.get("policy_no")));
			policy_item.addContent(new Element("policy").addContent(resultMap.get("policy")));		
			rootElement.addContent(policy_item);
		}
		return CommonXmlUtil.getElementToString(rootElement);
	}


	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutPolicy(HashMap<String, Object> hm) {

		List<String> policy_no_list = (List<String>)hm.get("policy_no_list");
		List<String> policy_list = (List<String>)hm.get("policy_list");
		List<String> checked_list = (List<String>)hm.get("checked_list");

		int resultCnt = 0;


		HashMap<String, Object> paramMap;

		int k = 0;

		for(String s : checked_list){

			if("true".equals(s)){

				if ("".equals(policy_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("policy", policy_list.get(k));
					resultCnt += postPolicy(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("policy_no", policy_no_list.get(k));
					paramMap.put("policy", policy_list.get(k));
					resultCnt += putPolicy(paramMap);
				}

			}

			k++;
		}


		return resultCnt;
	}

	public int postPolicy(HashMap<String, Object> hm) {

		String policy_no = nfvCommonDAO.getSeq("83");

		hm.put("policy_no", policy_no);
		hm.put("policy", hm.get("policy"));

		int i = atViewOptionsDAO.postPolicy(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("POLICY"));
		hm.put("func_ref_no", policy_no);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	// @Transactional
	public int putPolicy(HashMap<String, Object> hm) {

		int i = atViewOptionsDAO.putPolicy(hm);
		return i;

	}

	@Transactional
	public int deletePolicy(HashMap<String, Object> hm) {

		List<String> policy_list = (List<String>) hm.get("policy_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("policy", policy_list.get(k));

				int result = atViewOptionsDAO.passableDeletePolicy(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getPolicy(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String policy_no = resultMap.get("policy_no");

					hm.put("func_ref_no", policy_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deletePolicy(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// qos
	public String getQos(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getQos(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("qos_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("qos_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("qos");
			security_el.addContent(new Element("qos_no").addContent(resultMap.get("qos_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutQos(HashMap<String, Object> hm) {

		List<String> qos_no_list = (List<String>) hm.get("qos_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				if ("".equals(qos_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postQos(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("qos_no", qos_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putQos(paramMap);
				}

			}

			k++;
		}

		return resultCnt;
	}

	public int postQos(HashMap<String, Object> hm) {

		String qos_no = nfvCommonDAO.getSeq("81");

		hm.put("qos_no", qos_no);

		int i = atViewOptionsDAO.postQos(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("QOS"));
		hm.put("func_ref_no", qos_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	public int putQos(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putQos(hm);
		return i;

	}

	@Transactional
	public int deleteQos(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewOptionsDAO.passableDeleteQos(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getQos(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String qos_no = resultMap.get("qos_no");

					hm.put("func_ref_no", qos_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deleteQos(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// lifecycle
	public String getLifecycle(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getLifecycle(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("lifecycle_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("lifecycle_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("lifecycle");
			security_el.addContent(new Element("life_no").addContent(resultMap.get("life_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutLifecycle(HashMap<String, Object> hm) {

		List<String> life_no_list = (List<String>) hm.get("life_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				if ("".equals(life_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postLifecycle(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("life_no", life_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putLifecycle(paramMap);
				}

			}

			k++;
		}

		return resultCnt;
	}

	public int postLifecycle(HashMap<String, Object> hm) {

		String life_no = nfvCommonDAO.getSeq("81");

		hm.put("life_no", life_no);

		int i = atViewOptionsDAO.postLifecycle(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("LIFECYCLE"));
		hm.put("func_ref_no", life_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	public int putLifecycle(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putLifecycle(hm);
		return i;

	}

	@Transactional
	public int deleteLifecycle(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewOptionsDAO.passableDeleteLifecycle(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getLifecycle(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String life_no = resultMap.get("life_no");

					hm.put("func_ref_no", life_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deleteLifecycle(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// monitoring
	public String getMonitoring(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getMonitoring(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("monitoring_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("monitoring_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("monitoring");
			security_el.addContent(new Element("mnt_no").addContent(resultMap.get("mnt_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutMonitoring(HashMap<String, Object> hm) {

		List<String> mnt_no_list = (List<String>) hm.get("mnt_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				if ("".equals(mnt_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postMonitoring(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("mnt_no", mnt_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putMonitoring(paramMap);
				}

			}

			k++;
		}

		return resultCnt;
	}

	public int postMonitoring(HashMap<String, Object> hm) {

		String mnt_no = nfvCommonDAO.getSeq("81");

		hm.put("mnt_no", mnt_no);

		int i = atViewOptionsDAO.postMonitoring(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("MONITORING"));
		hm.put("func_ref_no", mnt_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	public int putMonitoring(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putMonitoring(hm);
		return i;

	}

	@Transactional
	public int deleteMonitoring(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewOptionsDAO.passableDeleteMonitoring(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getMonitoring(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String mnt_no = resultMap.get("mnt_no");

					hm.put("func_ref_no", mnt_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deleteMonitoring(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// nfp
	@Override
	public String getNfpOne(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getNfpOne(hm);
		// return CommonXmlUtil.getElementToString(getNfpMetaElement(hm));
		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("nfp");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> nfp_ref_no = new ArrayList<String>();
		List<String> nfp_id = new ArrayList<String>();
		List<String> policy = new ArrayList<String>();
		List<String> policy_no = new ArrayList<String>();
		List<String> connection = new ArrayList<String>();
		List<String> connection_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			logger.info("policy_no : " + map.get("policy_no"));
			logger.info("cp_no : " + map.get("cp_no"));

			nfp_ref_no.add(map.get("nfp_ref_no"));
			nfp_id.add(map.get("nfp_id"));
			policy.add(map.get("policy"));
			policy_no.add(map.get("policy_no"));
			connection.add(map.get("nfp_con"));
			connection_no.add(map.get("cp_no"));

		}

		Element rootElement = new Element("nfp");
		rootElement.addContent(new Element("id").addContent(nfp_id.get(0)));

		Element policy_item = new Element("policy_item");

		int i = 0;
		for (String s : policy) {

			if (!"NULL".equals(s)) {

				policy_item.addContent(new Element("key").addContent(policy_no.get(i)));
				policy_item.addContent(new Element("policy").addContent(s));

				break;
			}

			i++;
		}

		rootElement.addContent(policy_item);

		Element connection_list_el = new Element("connection_list");

		i = 0;
		for (String s : connection) {

			if (!"NULL".equals(s)) {

				Element connection_el = new Element("connection");
				connection_el.addContent(new Element("key").addContent(connection_no.get(i)));
				connection_el.addContent(new Element("id").addContent(s));

				connection_list_el.addContent(connection_el);
			}

			i++;

		}

		rootElement.addContent(connection_list_el);

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@Transactional
	public int postNfp(HashMap<String, Object> hm) {

		String nfp_seq = nfvCommonDAO.getSeq("85");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String policy_ref_seq = nfvCommonDAO.getSeq("84");

		hm.put("nfp_no", nfp_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("policy_ref_no", policy_ref_seq);

		atViewOptionsDAO.postNfp(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {

			hm.put("cp_ref_seq", cp_ref_seq);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] policy_key_list = (String[]) hm.get("policy_key_list");

		for (String s : policy_key_list) {
			if (!s.equals("")) {
				hm.put("policy_ref_no", policy_ref_seq);
				hm.put("policy_no", s);
				atViewOptionsDAO.postPolicyRef(hm);
			} else {
				logger.info("#################");
				logger.info("policy key 값이 null 입니다.");
				logger.info("#################");
			}
		}

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NFP"));
		hm.put("func_ref_no", nfp_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;

	}

	@Override
	public int putNfp(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> nfp_result = atViewOptionsDAO.getNfp(hm);

		String policy_ref_no = nfp_result.get("policy_ref_no");
		String cp_ref_no = nfp_result.get("connection_ref_no");

		hm.put("policy_ref_no", policy_ref_no);
		hm.put("cp_ref_no", cp_ref_no);

		atViewOptionsDAO.deletePolicyRef(hm);
		atViewDAO.deleteCpRef(hm);

		// nfp 본인은 변경할게 없다고 판단됨.
		// atViewOptionsDAO.putNfp(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {

			hm.put("cp_ref_seq", cp_ref_no);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] policy_key_list = (String[]) hm.get("policy_key_list");

		for (String s : policy_key_list) {
			if (!s.equals("")) {
				hm.put("policy_ref_no", policy_ref_no);
				hm.put("policy_no", s);
				atViewOptionsDAO.postPolicyRef(hm);
			} else {
				logger.info("#################");
				logger.info("policy key 값이 null 입니다.");
				logger.info("#################");
			}

		}

		return 1;
	}

	@Override
	public int deleteNfp(HashMap<String, Object> hm) {

		int resultCnt = atViewOptionsDAO.passableDeleteNfp(hm);

		if (resultCnt > 0) {
			return -2;
		}

		LinkedHashMap<String, String> nfp_result = atViewOptionsDAO.getNfp(hm);

		String policy_ref_no = nfp_result.get("policy_ref_no");
		String cp_ref_no = nfp_result.get("connection_ref_no");
		String nfp_no = nfp_result.get("nfp_no");

		hm.put("policy_ref_no", policy_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("nfp_no", nfp_no);

		atViewOptionsDAO.deletePolicyRef(hm);
		atViewDAO.deleteCpRef(hm);
		atViewOptionsDAO.deleteNfp(hm);

		hm.put("func_ref_no", nfp_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// VNFC
	@Override
	public String getVnfc(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getVnfc(hm);
		// return CommonXmlUtil.getElementToString(getNfpMetaElement(hm));
		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vnfc");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> vnfc_id = new ArrayList<String>();
		List<String> connection = new ArrayList<String>();
		List<String> connection_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			logger.info("vnfc_id : " + map.get("vnfc_id"));
			logger.info("cp_no : " + map.get("cp_no"));
			logger.info("cp_id : " + map.get("cp_id"));

			vnfc_id.add(map.get("vnfc_id"));
			connection.add(map.get("cp_id"));
			connection_no.add(map.get("cp_no"));

		}

		Element rootElement = new Element("vnfc");
		rootElement.addContent(new Element("id").addContent(vnfc_id.get(0)));

		Element connection_list_el = new Element("connection_list");

		int i = 0;
		for (String s : connection) {

			if (!"NULL".equals(s)) {

				Element connection_el = new Element("connection");
				connection_el.addContent(new Element("key").addContent(connection_no.get(i)));
				connection_el.addContent(new Element("id").addContent(s));

				connection_list_el.addContent(connection_el);
			}

			i++;

		}

		rootElement.addContent(connection_list_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Override
	public int postVnfc(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewOptionsDAO.getVnfcOne(hm);

		if (version_result != null && version_result.size() > 0) {
			return -1;
		}

		String vnfc_seq = nfvCommonDAO.getSeq("87");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");

		hm.put("vnfc_no", vnfc_seq);
		hm.put("cp_ref_no", cp_ref_seq);

		atViewOptionsDAO.postVnfc(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_seq);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VNFC"));
		hm.put("func_ref_no", vnfc_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Override
	public int putVnfc(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> vnfc_result = atViewOptionsDAO.getVnfcOne(hm);

		String cp_ref_no = vnfc_result.get("connection_ref_no");

		hm.put("cp_ref_no", cp_ref_no);

		atViewDAO.deleteCpRef(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_no);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		return 1;
	}

	@Override
	public int deleteVnfc(HashMap<String, Object> hm) {

		int resultCnt = atViewOptionsDAO.passableDeleteVnfc(hm);

		if (resultCnt > 0) {
			return -2;
		}

		LinkedHashMap<String, String> vnfc_result = atViewOptionsDAO.getVnfcOne(hm);

		String vnfc_no = vnfc_result.get("vnfc_no");
		String cp_ref_no = vnfc_result.get("connection_ref_no");

		hm.put("cp_ref_no", cp_ref_no);
		hm.put("vnfc_no", vnfc_no);

		atViewOptionsDAO.deleteVnfc(hm);
		atViewDAO.deleteCpRef(hm);

		hm.put("func_ref_no", vnfc_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// vnffgd
	public String getVnffgd(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getVnffgd(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vnffgd");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> id = new ArrayList<String>();
		List<String> vendor = new ArrayList<String>();
		List<String> version = new ArrayList<String>();
		List<String> number_of_endpoints = new ArrayList<String>();
		List<String> number_of_virtual_links = new ArrayList<String>();
		List<String> dvl = new ArrayList<String>();
		List<String> dvl_no = new ArrayList<String>();
		List<String> nfp_id = new ArrayList<String>();
		List<String> nfp_no = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>();
		List<String> cp_no = new ArrayList<String>();
		List<String> descriptor_version = new ArrayList<String>();
		List<String> constituent = new ArrayList<String>();
		List<String> constituent_no = new ArrayList<String>();
		List<String> asvnffgd_security = new ArrayList<String>();
		List<String> security_no = new ArrayList<String>();

		List<String> nfp_id_temp = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			version.add(map.get("version"));
			number_of_endpoints.add(map.get("number_of_endpoints"));
			number_of_virtual_links.add(map.get("number_of_virtual_links"));
			dvl.add(map.get("dvl"));
			dvl_no.add(map.get("dvl_no"));
			nfp_id.add(map.get("nfp_id"));
			nfp_no.add(map.get("nfp_no"));
			cp_id.add(map.get("cp_id"));
			cp_no.add(map.get("cp_no"));
			descriptor_version.add(map.get("descriptor_version"));
			constituent.add(map.get("constituent"));
			constituent_no.add(map.get("constituent_no"));
			asvnffgd_security.add(map.get("asvnffgd_security"));
			security_no.add(map.get("security_no"));

		}

		Element rootElement = new Element("vnffgd");

		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
		rootElement.addContent(new Element("version").addContent(version.get(0)));
		rootElement.addContent(new Element("number_of_endpoints").addContent(number_of_endpoints.get(0)));
		rootElement.addContent(new Element("number_of_virtual_links").addContent(number_of_virtual_links.get(0)));

		// 실제 값이 vld에서 가져온 것이기 때문에 vld로 생성하여 준다.
		Element vld_list_el = new Element("vld_list");
		int i = 0;
		for (String s : dvl) {

			if (!"NULL".equals(s)) {

				Element vld_el = new Element("vld");
				vld_el.addContent(new Element("key").addContent(dvl_no.get(i)));
				vld_el.addContent(new Element("id").addContent(s));

				vld_list_el.addContent(vld_el);
			}

			i++;

		}

		rootElement.addContent(vld_list_el);

		Element nfp_list_el = new Element("nfp_list");
		i = 0;
		for (String s : nfp_id) {

			if (!"NULL".equals(s) && !nfp_id_temp.contains(s)) {
				nfp_id_temp.add(s);
				Element nfp_el = new Element("nfp");
				nfp_el.addContent(new Element("key").addContent(nfp_no.get(i)));
				nfp_el.addContent(new Element("id").addContent(s));

				nfp_list_el.addContent(nfp_el);
			}

			i++;

		}

		rootElement.addContent(nfp_list_el);

		Element connection_list_el = new Element("connection_list");
		i = 0;
		for (String s : cp_id) {

			if (!"NULL".equals(s)) {

				Element connection_el = new Element("connection");
				connection_el.addContent(new Element("key").addContent(cp_no.get(i)));
				connection_el.addContent(new Element("id").addContent(s));

				connection_list_el.addContent(connection_el);
			}

			i++;

		}
		rootElement.addContent(connection_list_el);

		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));

		Element vnfd_list_el = new Element("vnfd_list");
		i = 0;
		for (String s : constituent) {

			if (!"NULL".equals(s)) {

				Element vnfd_el = new Element("vnfd");
				vnfd_el.addContent(new Element("key").addContent(constituent_no.get(i)));
				vnfd_el.addContent(new Element("id").addContent(s));

				vnfd_list_el.addContent(vnfd_el);
			}

			i++;

		}
		rootElement.addContent(vnfd_list_el);

		Element vnffgd_security_el = new Element("vnffgd_security");
		i = 0;
		for (String s : asvnffgd_security) {

			if (!"NULL".equals(s)) {

				vnffgd_security_el.addContent(new Element("key").addContent(security_no.get(i)));
				vnffgd_security_el.addContent(new Element("item").addContent(s));

				break;
			}

			i++;

		}
		rootElement.addContent(vnffgd_security_el);

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@Transactional
	public int postVnffgd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		if (version_result != null && version_result.size() > 0) {
			return -1;
		}

		// table_seq
		String vnffgd_seq = nfvCommonDAO.getSeq("31");
		String version_seq = nfvCommonDAO.getSeq("10");
		String vld_ref_seq = nfvCommonDAO.getSeq("11");
		String nfp_ref_seq = nfvCommonDAO.getSeq("86");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String vnfd_ref_seq = nfvCommonDAO.getSeq("12");
		String sec_seq = nfvCommonDAO.getSeq("02");

		hm.put("vnffgd_no", vnffgd_seq);
		hm.put("version_no", version_seq);
		hm.put("vld_ref_no", vld_ref_seq);
		hm.put("nfp_ref_no", nfp_ref_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("vnfd_ref_no", vnfd_ref_seq);
		hm.put("security_ref_no", sec_seq);

		// 1. nfv_vnffgd_tab insert
		atViewOptionsDAO.postVnffgd(hm);
		atViewDAO.postVersion(hm);

		// 2. nfv_function_ref(vld(dvl)) insert
		// vld는 version을 바라보기 때문에 version_no를 받는다.
		String[] vld_version_no_list = (String[]) hm.get("vld_version_no_list");
		for (String s : vld_version_no_list) {
			// hm.put("id", s);
			// LinkedHashMap<String, String> vld_version_result =
			// atViewDAO.getVersionOne(hm);
			// String vld_version_no = vld_version_result.get("version_no");
			hm.put("function_ref_no", vld_ref_seq);
			hm.put("function_no", s);

			atViewOptionsDAO.postFunctionRef(hm);
		}

		// 3. nfv_nfp_ref insert
		String[] nfp_key_list = (String[]) hm.get("nfp_key_list");
		for (String s : nfp_key_list) {
			hm.put("nfp_no", s);
			atViewOptionsDAO.postNfpRef(hm);
		}

		// 4. nfv_cp_ref insert
		String[] cp_key_list = (String[]) hm.get("cp_key_list");
		for (String s : cp_key_list) {
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		// 5. nfv_function_ref(vnfd(constituent)) insert
		// vnfd는 version을 바라보기 때문에 version_no를 받는다.
		String[] vnfd_version_no_list = (String[]) hm.get("vnfd_version_no_list");
		for (String s : vnfd_version_no_list) {
			// hm.put("id", s);
			// LinkedHashMap<String, String> vld_version_result =
			// atViewDAO.getVersionOne(hm);
			// String vld_version_no = vld_version_result.get("version_no");
			hm.put("function_ref_no", vnfd_ref_seq);
			hm.put("function_no", s);
			atViewOptionsDAO.postFunctionRef(hm);
		}

		// 6. nfv_security_ref insert
		String[] vnfd_security_list = (String[]) hm.get("vnfd_security_list");
		for (String s : vnfd_security_list) {
			hm.put("security_no", s);
			atViewDAO.postSecurityRef(hm);
		}

		// 7. tmenu
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VNFFGD"));
		hm.put("func_ref_no", version_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putVnffgd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> vnffgd_result = atViewOptionsDAO.getVnffgdOne(hm);

		String vld_ref_no = vnffgd_result.get("dvl_ref_no");
		String nfp_ref_no = vnffgd_result.get("nfp_ref_no");
		String cp_ref_no = vnffgd_result.get("connection_point_ref_no");
		String vnfd_ref_no = vnffgd_result.get("constituent_vnfs_ref_no");
		String security_ref_no = vnffgd_result.get("vnffgd_security_ref_no");

		// hm.put("vld_ref_no", vld_ref_no);
		hm.put("nfp_ref_no", nfp_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		// hm.put("vnfd_ref_no", vnfd_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. vld 관련 삭제 -> nfv_function_ref
		hm.put("function_ref_no", vld_ref_no);
		atViewOptionsDAO.deleteFunctionRef(hm);
		// 2. vnfd 삭제 -> nfv_function_ref
		hm.put("function_ref_no", vnfd_ref_no);
		atViewOptionsDAO.deleteFunctionRef(hm);
		// 3. nfv_nfp_ref 삭제
		atViewOptionsDAO.deleteNfpRef(hm);
		// 4. nfv_cp_ref 삭제
		atViewDAO.deleteCpRef(hm);
		// 5. nfv_security_ref 삭제
		atViewDAO.deleteSecurityRef(hm);

		// 6. nfv_version_tab 수정
		atViewDAO.putVersion(hm);
		// 7. nfv_vnffgd_tab 수정
		atViewOptionsDAO.putVnffgd(hm);

		// 8. nfv_function_ref(vld(dvl)) insert
		// 반복문 돌면서, vld_id로 version 조회 후 나온 version_no로 functionRef 등록
		String[] vld_version_no_list = (String[]) hm.get("vld_version_no_list");
		for (String s : vld_version_no_list) {
			// hm.put("id", s);
			// LinkedHashMap<String, String> vld_version_result =
			// atViewDAO.getVersionOne(hm);
			// String vld_version_no = vld_version_result.get("version_no");
			hm.put("function_ref_no", vld_ref_no);
			hm.put("function_no", s);

			atViewOptionsDAO.postFunctionRef(hm);
		}

		// 9. nfv_nfp_ref insert
		String[] nfp_key_list = (String[]) hm.get("nfp_key_list");
		for (String s : nfp_key_list) {
			hm.put("nfp_no", s);
			atViewOptionsDAO.postNfpRef(hm);
		}

		// 10. nfv_cp_ref insert
		String[] cp_key_list = (String[]) hm.get("cp_key_list");
		for (String s : cp_key_list) {
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		// 11. nfv_function_ref(vnfd(constituent)) insert
		String[] vnfd_version_no_list = (String[]) hm.get("vnfd_version_no_list");
		for (String s : vnfd_version_no_list) {
			// hm.put("id", s);
			// LinkedHashMap<String, String> vld_version_result =
			// atViewDAO.getVersionOne(hm);
			// String vld_version_no = vld_version_result.get("version_no");
			hm.put("function_ref_no", vnfd_ref_no);
			hm.put("function_no", s);
			atViewOptionsDAO.postFunctionRef(hm);
		}

		// 12. nfv_security_ref insert
		String[] vnfd_security_list = (String[]) hm.get("vnfd_security_list");
		for (String s : vnfd_security_list) {
			hm.put("security_no", s);
			atViewDAO.postSecurityRef(hm);
		}

		return 1;
	}

	@Transactional
	public int deleteVnffgd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("function_no", version_no);

		int resultCnt = atViewDAO.passableDeleteMainContent(hm);

		if (resultCnt > 0) {
			return -2;
		}

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> vnffgd_result = atViewOptionsDAO.getVnffgdOne(hm);

		String vld_ref_no = vnffgd_result.get("dvl_ref_no");
		String nfp_ref_no = vnffgd_result.get("nfp_ref_no");
		String cp_ref_no = vnffgd_result.get("connection_point_ref_no");
		String vnfd_ref_no = vnffgd_result.get("constituent_vnfs_ref_no");
		String security_ref_no = vnffgd_result.get("vnffgd_security_ref_no");

		hm.put("nfp_ref_no", nfp_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. vld 관련 삭제 -> nfv_function_ref
		hm.put("function_ref_no", vld_ref_no);
		atViewOptionsDAO.deleteFunctionRef(hm);
		// 2. vnfd 삭제 -> nfv_function_ref
		hm.put("function_ref_no", vnfd_ref_no);
		atViewOptionsDAO.deleteFunctionRef(hm);
		// 3. nfv_nfp_ref 삭제
		atViewOptionsDAO.deleteNfpRef(hm);
		// 4. nfv_cp_ref 삭제
		atViewDAO.deleteCpRef(hm);
		// 5. nfv_security_ref 삭제
		atViewDAO.deleteSecurityRef(hm);
		// 6. nfv_version_tab 삭제
		atViewDAO.deleteVersion(hm);
		// 7. nfv_vnffgd_tab 삭제
		atViewOptionsDAO.deleteVnffgd(hm);

		hm.put("func_ref_no", version_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// vnfdep
	public String getVnfdep(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getVnfdep(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vnfdep_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("vnfdep_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("vnfdep");
			security_el.addContent(new Element("vnfdep_no").addContent(resultMap.get("vnfdep_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutVnfdep(HashMap<String, Object> hm) {
		List<String> vnfdep_no_list = (List<String>) hm.get("vnfdep_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;
		HashMap<String, Object> paramMap;

		int k = 0;
		for (String s : checked_list) {
			if ("true".equals(s)) {
				if ("".equals(vnfdep_no_list.get(k))) {
					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postVnfdep(paramMap);

				} else {
					paramMap = new HashMap<String, Object>();
					paramMap.put("vnfdep_no", vnfdep_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putVnfdep(paramMap);
				}
			}
			k++;
		}

		return resultCnt;
	}

	public int postVnfdep(HashMap<String, Object> hm) {

		String vnfdep_no = nfvCommonDAO.getSeq("45");

		hm.put("vnfdep_no", vnfdep_no);

		int i = atViewOptionsDAO.postVnfdep(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NSD-VNFDEP"));
		hm.put("func_ref_no", vnfdep_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	public int putVnfdep(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putVnfdep(hm);
		return i;

	}

	@Transactional
	public int deleteVnfdep(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewOptionsDAO.passableDeleteVnfdep(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getVnfdep(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String vnfdep_no = resultMap.get("vnfdep_no");

					hm.put("func_ref_no", vnfdep_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deleteVnfdep(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// vnfrtd
	@Override
	public String getVnfrtd(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getVnfrtd(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vnfrtd");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		// vnfrtd_mnt_item
		// vnfrtd_mnt_no

		List<String> id = new ArrayList<String>();
		List<String> mnt_item = new ArrayList<String>();
		List<String> mnt_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {
			id.add(map.get("vnfrtd_id"));
			mnt_item.add(map.get("vnfrtd_mnt_item"));
			mnt_no.add(map.get("vnfrtd_mnt_no"));
		}

		Element rootElement = new Element("vnfrtd");

		rootElement.addContent(new Element("id").addContent(id.get(0)));

		Element mnt_list_el = new Element("mnt_list");
		int i = 0;
		for (String s : mnt_item) {

			if (!"NULL".equals(s)) {

				Element mnt_el = new Element("mnt");
				mnt_el.addContent(new Element("key").addContent(mnt_no.get(i)));
				mnt_el.addContent(new Element("item").addContent(s));

				mnt_list_el.addContent(mnt_el);
			}

			i++;

		}
		rootElement.addContent(mnt_list_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postVnfrtd(HashMap<String, Object> hm) {

		// table_seq
		String vnfrtd_seq = nfvCommonDAO.getSeq("91");
		String mnt_ref_seq = nfvCommonDAO.getSeq("42");

		hm.put("vnfrtd_no", vnfrtd_seq);
		hm.put("mnt_ref_no", mnt_ref_seq);

		// 1. nfv_vnffgd_tab insert
		atViewOptionsDAO.postVnfrtd(hm);

		// 2. nfv_cp_ref insert
		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		for (String s : mnt_key_list) {
			hm.put("mnt_no", s);
			atViewDAO.postMonitoringRef(hm);
		}

		// 3. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VNFRTD"));
		hm.put("func_ref_no", vnfrtd_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putVnfrtd(HashMap<String, Object> hm) {
		// TODO Auto-generated method stub

		LinkedHashMap<String, String> vnfrtd_result = atViewOptionsDAO.getVnfrtdOne(hm);

		String vnfrtd_no = vnfrtd_result.get("vnfrtd_no");
		String mnt_ref_no = vnfrtd_result.get("mnt_ref_no");

		hm.put("vnfrtd_no", vnfrtd_no);
		hm.put("mnt_ref_no", mnt_ref_no);

		atViewDAO.deleteMonitoringRef(hm);

		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		for (String s : mnt_key_list) {
			hm.put("mnt_no", s);
			atViewDAO.postMonitoringRef(hm);
		}

		return 1;
	}

	@Transactional
	public int deleteVnfrtd(HashMap<String, Object> hm) {

		int resultCnt = atViewOptionsDAO.passableDeleteVnfrtd(hm);

		if (resultCnt > 0) {
			return -2;
		}

		LinkedHashMap<String, String> vnfrtd_result = atViewOptionsDAO.getVnfrtdOne(hm);

		String vnfrtd_no = vnfrtd_result.get("vnfrtd_no");
		String mnt_ref_no = vnfrtd_result.get("mnt_ref_no");

		hm.put("vnfrtd_no", vnfrtd_no);
		hm.put("mnt_ref_no", mnt_ref_no);

		atViewDAO.deleteMonitoringRef(hm);
		atViewOptionsDAO.deleteVnfrtd(hm);

		hm.put("func_ref_no", vnfrtd_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// constraint
	public String getConstraint(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getConstraint(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("constraint_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("constraint_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("constraint");
			security_el.addContent(new Element("constraint_no").addContent(resultMap.get("cstr_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutConstraint(HashMap<String, Object> hm) {

		List<String> constraint_no_list = (List<String>) hm.get("constraint_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				if ("".equals(constraint_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postConstraint(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("constraint_no", constraint_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putConstraint(paramMap);
				}

			}

			k++;
		}

		return resultCnt;
	}

	public int postConstraint(HashMap<String, Object> hm) {

		String constraint_no = nfvCommonDAO.getSeq("77");

		hm.put("constraint_no", constraint_no);

		int i = atViewOptionsDAO.postConstraint(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("DF-CONSTRAINT"));
		hm.put("func_ref_no", constraint_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	public int putConstraint(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putConstraint(hm);
		return i;

	}

	@Transactional
	public int deleteConstraint(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewOptionsDAO.passableDeleteConstraint(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getConstraint(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String constraint_no = resultMap.get("constraint_no");

					hm.put("func_ref_no", constraint_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewOptionsDAO.deleteConstraint(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// TODO DF(flavour)
	@Override
	public String getDf(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getDf(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("df");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> flavour_id = new ArrayList<String>();
		List<String> flavour_flag = new ArrayList<String>();
		List<String> flavour_key = new ArrayList<String>();
		List<String> cstr_item = new ArrayList<String>();
		List<String> cstr_key = new ArrayList<String>();
		List<String> vdu_id = new ArrayList<String>();
		List<String> constituent_key = new ArrayList<String>();
		List<String> instance_cnt = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			flavour_id.add(map.get("flavour_id"));
			flavour_flag.add(map.get("flavour_flag"));
			flavour_key.add(map.get("flavour_key"));
			cstr_item.add(map.get("cstr_item"));
			cstr_key.add(map.get("cstr_key"));
			vdu_id.add(map.get("vdu_id"));
			constituent_key.add(map.get("constituent_key"));
			instance_cnt.add(map.get("instance_cnt"));
			cp_id.add(map.get("cp_id"));
		}

		Element rootElement = new Element("df");

		rootElement.addContent(new Element("id").addContent(flavour_id.get(0)));
		rootElement.addContent(new Element("flavour_key").addContent(flavour_key.get(0)));

		Element constraint_list_el = new Element("constraint_list");

		int i = 0;
		for (String s : cstr_item) {

			if (!"NULL".equals(s)) {

				Element constraint_el = new Element("constraint");
				constraint_el.addContent(new Element("item").addContent(s));
				constraint_el.addContent(new Element("key").addContent(cstr_key.get(i)));

				constraint_list_el.addContent(constraint_el);
			}

		}

		rootElement.addContent(constraint_list_el);

		i = 0;
		Element constituent_list_el = new Element("constituent_list");
		for (String s : vdu_id) {

			if (!"NULL".equals(s)) {

				Element constituent_el = new Element("constituent");
				constituent_el.addContent(new Element("id").addContent(s));
				constituent_el.addContent(new Element("key").addContent(constituent_key.get(i)));

				constituent_list_el.addContent(constituent_el);
			}
			i++;
		}

		rootElement.addContent(constituent_list_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postDf(HashMap<String, Object> hm) {
		// db에 flavour_id 와 flavour_flag값으로 데이터가 있는 경우 insert하지 않는다.
		int k = atViewOptionsDAO.passablePostDf(hm);

		if (k > 0) {
			return -1;
		}

		// table_seq
		String flavour_seq = nfvCommonDAO.getSeq("73");
		String constraint_ref_seq = nfvCommonDAO.getSeq("76");
		String constituent_ref_seq = nfvCommonDAO.getSeq("74");

		hm.put("flavour_no", flavour_seq);
		hm.put("constraint_ref_no", constraint_ref_seq);
		hm.put("constituent_ref_no", constituent_ref_seq);

		// 1. nfv_flavour_tab insert
		atViewOptionsDAO.postDf(hm);

		// 2. nfv_constraint_ref insert
		String[] constraint_key_list = (String[]) hm.get("constraint_key_list");
		for (String s : constraint_key_list) {

			if (!s.equals("")) {
				hm.put("constraint_no", s);
				atViewOptionsDAO.postConstraintRef(hm);
			} else {
				logger.info("#################");
				logger.info("constraint key 값이 null 입니다.");
				logger.info("#################");
			}

		}

		// 3 nfv_constituent_ref insert
		String[] constituent_key_list = (String[]) hm.get("constituent_key_list");
		for (String s : constituent_key_list) {
			hm.put("constituent_no", s);
			atViewOptionsDAO.postConstituentRef(hm);
		}

		// 3. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("DF"));
		hm.put("func_ref_no", flavour_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putDf(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> flavour_result = atViewOptionsDAO.getDfOne(hm);

		String constraint_ref_no = flavour_result.get("cstr_ref_no");
		String constituent_ref_no = flavour_result.get("cstt_ref_no");

		hm.put("constraint_ref_no", constraint_ref_no);
		hm.put("constituent_ref_no", constituent_ref_no);

		// nfv_constraint_ref 삭제
		atViewOptionsDAO.deleteConstraintRef(hm);

		// nfv_constituent_ref 삭제
		atViewOptionsDAO.deleteConstituentRef(hm);

		// nfv_constraint_ref 재 insert
		String[] constraint_key_list = (String[]) hm.get("constraint_key_list");
		for (String s : constraint_key_list) {
			if (!s.equals("")) {
				hm.put("constraint_no", s);
				atViewOptionsDAO.postConstraintRef(hm);
			} else {
				logger.info("#################");
				logger.info("constraint key 값이 null 입니다.");
				logger.info("#################");
			}
		}

		// nfv_constituent_ref 재 insert
		String[] constituent_key_list = (String[]) hm.get("constituent_key_list");
		logger.info("##############");
		logger.info("constituent_key_list_length : " + constituent_key_list.length);

		for (String s : constituent_key_list) {
			logger.info("##############");
			logger.info("constituent_key_list : " + s);
			hm.put("constituent_no", s);
			atViewOptionsDAO.postConstituentRef(hm);
		}

		// nfv_flavour_tab update
		atViewOptionsDAO.putDf(hm);

		return 1;
	}

	@Transactional
	public int deleteDf(HashMap<String, Object> hm) {

		int resultCnt = atViewOptionsDAO.passableDeleteDf(hm);

		if (resultCnt > 0) {
			return -2;
		}

		LinkedHashMap<String, String> flavour_result = atViewOptionsDAO.getDfOne(hm);

		String flavour_no = flavour_result.get("flavour_no");
		String constraint_ref_no = flavour_result.get("cstr_ref_no");
		String constituent_ref_no = flavour_result.get("cstt_ref_no");

		hm.put("constraint_ref_no", constraint_ref_no);
		hm.put("constituent_ref_no", constituent_ref_no);

		// nfv_constraint_ref 삭제
		atViewOptionsDAO.deleteConstraintRef(hm);

		// nfv_constituent_ref 삭제
		atViewOptionsDAO.deleteConstituentRef(hm);

		// nfv_flavour_tab 삭제
		int j = atViewOptionsDAO.deleteDf(hm);

		hm.put("func_ref_no", flavour_no);
		atViewDAO.deleteTmenuRef(hm);

		return j;
	}

	// TODO SDF
	@Override
	public String getSdf(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getSdf(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("sdf");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		// vnfrtd_mnt_item
		// vnfrtd_mnt_no

		List<String> id = new ArrayList<String>();
		List<String> sdf_flavour_key = new ArrayList<String>();
		List<String> vnf_ref_id = new ArrayList<String>();
		List<String> convnf_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {
			id.add(map.get("sdf_id"));
			sdf_flavour_key.add(map.get("sdf_flavour_key"));
			vnf_ref_id.add(map.get("vnf_ref_id"));
			convnf_no.add(map.get("convnf_no"));
		}

		Element rootElement = new Element("sdf");

		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("flavour_key").addContent(sdf_flavour_key.get(0)));

		Element constituent_vnf_list_el = new Element("constituent_vnf_list");
		int i = 0;
		for (String s : vnf_ref_id) {

			if (!"NULL".equals(s)) {

				Element constituent_vnf_el = new Element("constituent_vnf");
				constituent_vnf_el.addContent(new Element("key").addContent(convnf_no.get(i)));
				constituent_vnf_el.addContent(new Element("item").addContent(s));

				constituent_vnf_list_el.addContent(constituent_vnf_el);
			}

			i++;

		}
		rootElement.addContent(constituent_vnf_list_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postSdf(HashMap<String, Object> hm) {

		// table_seq
		String sdf_seq = nfvCommonDAO.getSeq("47");
		String convnf_ref_seq = nfvCommonDAO.getSeq("78");

		hm.put("sdf_no", sdf_seq);
		hm.put("convnf_ref_no", convnf_ref_seq);

		// 1. nfv_sdf_tab insert
		atViewOptionsDAO.postSdf(hm);

		// 2. nfv_convnf_ref insert
		String[] convnf_key_list = (String[]) hm.get("convnf_key_list");
		for (String s : convnf_key_list) {
			hm.put("convnf_no", s);
			atViewOptionsDAO.postConvnfRef(hm);
		}

		// 3. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("SDF"));
		hm.put("func_ref_no", sdf_seq);
		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putSdf(HashMap<String, Object> hm) {
		// TODO Auto-generated method stub

		LinkedHashMap<String, String> sdf_result = atViewOptionsDAO.getSdfOne(hm);

		String sdf_no = sdf_result.get("sdf_no");
		String convnf_ref_no = sdf_result.get("convnf_ref_no");

		hm.put("sdf_no", sdf_no);
		hm.put("convnf_ref_no", convnf_ref_no);

		atViewOptionsDAO.deleteConvnfRef(hm);

		String[] convnf_key_list = (String[]) hm.get("convnf_key_list");
		for (String s : convnf_key_list) {
			hm.put("convnf_no", s);
			atViewOptionsDAO.postConvnfRef(hm);
		}

		atViewOptionsDAO.putSdf(hm);

		return 1;
	}

	@Transactional
	public int deleteSdf(HashMap<String, Object> hm) {

		int resultCnt = atViewOptionsDAO.passableDeleteSdf(hm);

		if (resultCnt > 0) {
			return -2;
		}

		LinkedHashMap<String, String> sdf_result = atViewOptionsDAO.getSdfOne(hm);

		String sdf_no = sdf_result.get("sdf_no");
		String convnf_ref_no = sdf_result.get("convnf_ref_no");

		hm.put("sdf_no", sdf_no);
		hm.put("convnf_ref_no", convnf_ref_no);

		atViewOptionsDAO.deleteConvnfRef(hm);
		atViewOptionsDAO.deleteSdf(hm);

		hm.put("func_ref_no", sdf_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// TODO nfvi(vnf_status)
	@Transactional
	public String getState(HashMap<String, Object> hm) {
		List<LinkedHashMap<String, String>> resultList = atViewOptionsDAO.getState(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("nfvi_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("nfvi_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element nfvi_el = new Element("nfvi");
			nfvi_el.addContent(new Element("state_no").addContent(resultMap.get("state_no")));
			nfvi_el.addContent(new Element("version_no").addContent(resultMap.get("version_no")));
			nfvi_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(nfvi_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutState(HashMap<String, Object> hm) {
		List<String> state_no_list = (List<String>) hm.get("state_no_list");
		List<String> version_no_list = (List<String>) hm.get("version_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;
		HashMap<String, Object> paramMap;

		int i = 0;
		for (String s : checked_list) {
			if ("true".equals(s)) {
				if ("".equals(state_no_list.get(i))) {
					paramMap = new HashMap<String, Object>();
					paramMap.put("version_no", version_no_list.get(i));
					paramMap.put("item", item_list.get(i));
					resultCnt += postState(paramMap);
				} else {
					paramMap = new HashMap<String, Object>();
					paramMap.put("state_no", state_no_list.get(i));
					paramMap.put("version_no", version_no_list.get(i));
					paramMap.put("item", item_list.get(i));
					resultCnt += putState(paramMap);
				}
			}
			i++;
		}

		return resultCnt;
	}

	private int postState(HashMap<String, Object> hm) {
		// table_seq
		String state_no = nfvCommonDAO.getSeq("99");

		// 1. nfv_version_tab select
		int versionCnt = atViewOptionsDAO.getVersionCnt(hm);
		if (versionCnt < 1) {
			return 0;
		}

		// 2. nfv_state_tab insert
		hm.put("state_no", state_no);
		hm.put("version_no", hm.get("version_no"));
		hm.put("item", hm.get("item"));
		atViewOptionsDAO.postState(hm);

		// 1. nfv_tmenu_ref insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NFVIC"));
		hm.put("func_ref_no", state_no);
		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	private int putState(HashMap<String, Object> hm) {
		int i = atViewOptionsDAO.putState(hm);
		return i;
	}

	@Transactional
	public int deleteState(HashMap<String, Object> hm) {
		List<String> state_no_list = (List<String>) hm.get("state_no_list");
		List<String> version_no_list = (List<String>) hm.get("version_no_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;
		HashMap<String, Object> paramMap;

		int i = 0;
		for (String s : checked_list) {
			if ("true".equals(s)) {
				String state_no = state_no_list.get(i);
				String version_no = version_no_list.get(i);

				paramMap = new HashMap<String, Object>();
				paramMap.put("state_no", state_no);
				paramMap.put("version_no", version_no);

				int result = atViewOptionsDAO.passableDeleteState(paramMap);
				if (result == 0) {
					// 1. nfv_state_tab delete
					int delResult = atViewOptionsDAO.deleteState(paramMap);

					// 2. nfv_tmenu_ref delete
					paramMap.put("func_ref_no", state_no);
					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += delResult;
				}
			}
			i++;
		}

		return resultCnt;
	}

}
