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
import com.eluon.vepc.mano.service.AtViewService;

/**
 * AccountServiceImpl (AccountServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountServiceImpl.java,v 1.0 2014/12/15 00:00:00 SimByungChul
 *          Express $
 */
@Service("atViewService")
public class AtViewServiceImpl implements AtViewService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AtViewDAO atViewDAO;

	@Autowired
	private NFVCommonDAO nfvCommonDAO;

	@Resource(name = "nfv")
	private Properties nfv;

	private Element getVldAtViewMetaElement(HashMap<String, String> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getVldAtViewMeta(hm);

		if (resultList == null || resultList.size() == 0) {
			return new Element("vld_list");
		}

		List<String> vld_no = new ArrayList<String>();
		List<String> vld_id = new ArrayList<String>();
		List<String> vld_id_temp = new ArrayList<String>();
		List<String> cp_no = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			vld_no.add(map.get("vld_no"));
			vld_id.add(map.get("vld_id"));
			cp_no.add(map.get("cp_no"));
			cp_id.add(map.get("cp_id"));

		}

		Element rootElement = new Element("vld_list");

		int i = 0;
		for (String s : vld_id) {

			if (!"NULL".equals(s) && !vld_id_temp.contains(s)) {

				Element vld_el = new Element("vld");
				vld_el.addContent(new Element("vld_no").addContent(vld_no.get(i)));
				vld_el.addContent(new Element("vld_id").addContent(s));

				Element cp_list_el = new Element("cp_list");

				int j = 0;
				for (String k : cp_id) {

					if (!"NULL".equals(k) && s.equals(vld_id.get(j))) {

						Element cp_el = new Element("cp");

						cp_el.addContent(new Element("cp_no").addContent(cp_no.get(j)));
						cp_el.addContent(new Element("cp_id").addContent(k));

						cp_list_el.addContent(cp_el);
					}
					j++;
				}

				vld_el.addContent(cp_list_el);

				rootElement.addContent(vld_el);

				vld_id_temp.add(s);
			}
			i++;

		}

		return rootElement;
	}

	public String getVldAtViewMeta(HashMap<String, String> hm) {

		return CommonXmlUtil.getElementToString(getVldAtViewMetaElement(hm));

	}

	private Element getVmAtViewMetaElement(HashMap<String, String> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getVmAtViewMeta(hm);

		if (resultList == null || resultList.size() == 0) {
			return new Element("vm_list");
		}

		List<String> vm_no = new ArrayList<String>();
		List<String> vm_id = new ArrayList<String>();
		List<String> vm_id_temp = new ArrayList<String>();
		List<String> vm_state = new ArrayList<String>();
		List<String> cp_no = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			vm_no.add(map.get("vm_no"));
			vm_id.add(map.get("vm_id"));
			vm_state.add(map.get("vm_state"));
			cp_no.add(map.get("cp_no"));
			cp_id.add(map.get("cp_id"));

		}

		Element rootElement = new Element("vm_list");

		int i = 0;
		for (String s : vm_id) {

			if (!"NULL".equals(s) && !vm_id_temp.contains(s)) {

				Element vm_el = new Element("vm");
				vm_el.addContent(new Element("vm_no").addContent(vm_no.get(i)));
				vm_el.addContent(new Element("vm_id").addContent(s));
				vm_el.addContent(new Element("vm_state").addContent(vm_state.get(i)));

				Element cp_list_el = new Element("cp_list");
				int j = 0;
				for (String k : cp_id) {

					if (!"NULL".equals(k) && s.equals(vm_id.get(j))) {

						Element cp_el = new Element("cp");
						cp_el.addContent(new Element("cp_no").addContent(cp_no.get(j)));
						cp_el.addContent(new Element("cp_id").addContent(k));
						cp_list_el.addContent(cp_el);
					}

					j++;
				}

				vm_el.addContent(cp_list_el);

				rootElement.addContent(vm_el);

				vm_id_temp.add(s);
			}

			i++;
		}
		return rootElement;

	}

	public String getVmAtViewMeta(HashMap<String, String> hm) {

		return CommonXmlUtil.getElementToString(getVmAtViewMetaElement(hm));
	}

	private Element getTmenuElement() {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delimiter", "MM");
		paramMap.put("delimiter2", "SM");

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getTmenu(paramMap);

		if (resultList == null || resultList.size() == 0) {
			return new Element("tmenu_all");
		}

		List<String> tmenu_no = new ArrayList<String>();
		List<String> tmenu_sort = new ArrayList<String>();
		List<String> tmenu_id = new ArrayList<String>();
		List<String> tmenu_id_temp = new ArrayList<String>();
		List<String> tmenu_delimiter = new ArrayList<String>();
		List<String> tmenu_option = new ArrayList<String>();
		List<String> func_ref_no = new ArrayList<String>();
		List<String> func_id = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			tmenu_no.add(map.get("tmenu_no"));
			tmenu_sort.add(map.get("tmenu_sort"));
			tmenu_id.add(map.get("tmenu_id"));
			tmenu_delimiter.add(map.get("tmenu_delimiter"));
			tmenu_option.add(map.get("tmenu_option"));
			func_ref_no.add(map.get("func_ref_no"));
			func_id.add(map.get("func_id"));

		}

		Element rootElement = new Element("tmenu_all");

		int i = 0;

		for (String s : tmenu_id) {

			if (!"NULL".equals(s) && !tmenu_id_temp.contains(s)) {

				Element tmenu_el = new Element("tmenu");
				tmenu_el.addContent(new Element("title").addContent(s));
				tmenu_el.addContent(new Element("key").addContent(tmenu_no.get(i)));
				tmenu_el.addContent(new Element("option").addContent(tmenu_option.get(i)));

				Element cp_list_el = new Element("children");
				int j = 0;
				for (String k : func_id) {

					if (!"NULL".equals(k) && s.equals(tmenu_id.get(j))) {

						Element child_el = new Element("child");

						child_el.addContent(new Element("title").addContent(k));
						child_el.addContent(new Element("key").addContent(func_ref_no.get(j)));
						cp_list_el.addContent(child_el);
					}

					j++;
				}

				tmenu_el.addContent(cp_list_el);

				rootElement.addContent(tmenu_el);

				tmenu_id_temp.add(s);
			}

			i++;
		}
		return rootElement;

	}

	public String getTmenu() {

		return CommonXmlUtil.getElementToString(getTmenuElement());
	}

	private Element getToptionElement() {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delimiter", "OM");

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getToption(paramMap);

		if (resultList == null || resultList.size() == 0) {
			return new Element("toption_all");
		}

		List<String> tmenu_no = new ArrayList<String>();
		List<String> tmenu_sort = new ArrayList<String>();
		List<String> tmenu_id = new ArrayList<String>();
		List<String> tmenu_id_temp = new ArrayList<String>();
		List<String> tmenu_delimiter = new ArrayList<String>();
		List<String> tmenu_option = new ArrayList<String>();
		List<String> func_ref_no = new ArrayList<String>();
		List<String> func_id = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			tmenu_no.add(map.get("tmenu_no"));
			tmenu_sort.add(map.get("tmenu_sort"));
			tmenu_id.add(map.get("tmenu_id"));
			tmenu_delimiter.add(map.get("tmenu_delimiter"));
			tmenu_option.add(map.get("tmenu_option"));
			func_ref_no.add(map.get("func_ref_no"));
			func_id.add(map.get("func_id"));

		}

		Element rootElement = new Element("toption_all");

		int i = 0;

		for (String s : tmenu_id) {

			if (!"NULL".equals(s) && !tmenu_id_temp.contains(s)) {

				Element tmenu_el = new Element("toption");
				tmenu_el.addContent(new Element("title").addContent(s));
				tmenu_el.addContent(new Element("key").addContent(tmenu_no.get(i)));
				tmenu_el.addContent(new Element("option").addContent(tmenu_option.get(i)));

				Element cp_list_el = new Element("children");
				int j = 0;
				for (String k : func_id) {

					if (!"NULL".equals(k) && s.equals(tmenu_id.get(j))) {

						Element child_el = new Element("child");

						child_el.addContent(new Element("title").addContent(k));
						child_el.addContent(new Element("key").addContent(func_ref_no.get(j)));
						cp_list_el.addContent(child_el);
					}

					j++;
				}

				tmenu_el.addContent(cp_list_el);

				rootElement.addContent(tmenu_el);

				tmenu_id_temp.add(s);
			}

			i++;
		}
		return rootElement;

	}

	public String getToption() {

		return CommonXmlUtil.getElementToString(getToptionElement());
	}

	private Element getMenuElement(HashMap<String, String> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getTmenu(hm);

		if (resultList == null || resultList.size() == 0) {
			return new Element("tmenu_all");
		}

		List<String> tmenu_no = new ArrayList<String>();
		List<String> tmenu_sort = new ArrayList<String>();
		List<String> tmenu_id = new ArrayList<String>();
		List<String> tmenu_id_temp = new ArrayList<String>();
		List<String> tmenu_delimiter = new ArrayList<String>();
		List<String> tmenu_option = new ArrayList<String>();
		List<String> func_ref_no = new ArrayList<String>();
		List<String> func_id = new ArrayList<String>();
		List<String> tmenu_show = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			tmenu_no.add(map.get("tmenu_no"));
			tmenu_sort.add(map.get("tmenu_sort"));
			tmenu_id.add(map.get("tmenu_id"));
			tmenu_delimiter.add(map.get("tmenu_delimiter"));
			tmenu_option.add(map.get("tmenu_option"));
			func_ref_no.add(map.get("func_ref_no"));
			func_id.add(map.get("func_id"));
			tmenu_show.add(map.get("tmenu_show"));

		}

		Element rootElement = new Element("tmenu_all");

		int i = 0;

		for (String s : tmenu_id) {

			if (!"NULL".equals(s) && !tmenu_id_temp.contains(s)) {

				Element tmenu_el = new Element("tmenu");
				tmenu_el.addContent(new Element("title").addContent(s));
				tmenu_el.addContent(new Element("show").addContent(tmenu_show.get(i)));
				tmenu_el.addContent(new Element("key").addContent(tmenu_no.get(i)));
				tmenu_el.addContent(new Element("option").addContent(tmenu_option.get(i)));

				Element cp_list_el = new Element("children");
				int j = 0;
				for (String k : func_id) {

					if (!"NULL".equals(k) && s.equals(tmenu_id.get(j))) {

						Element child_el = new Element("child");

						child_el.addContent(new Element("title").addContent(k));
						child_el.addContent(new Element("key").addContent(func_ref_no.get(j)));
						cp_list_el.addContent(child_el);
					}

					j++;
				}

				tmenu_el.addContent(cp_list_el);

				rootElement.addContent(tmenu_el);

				tmenu_id_temp.add(s);
			}

			i++;
		}
		return rootElement;

	}

	public String getMainMenu() {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delimiter", "MM");

		return CommonXmlUtil.getElementToString(getMenuElement(paramMap));
	}

	public String getSubMenu() {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delimiter", "SM");

		return CommonXmlUtil.getElementToString(getMenuElement(paramMap));
	}

	public String getOptionMenu() {

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delimiter", "OM");

		return CommonXmlUtil.getElementToString(getMenuElement(paramMap));
	}

	// vld
	public String getVld(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getVld(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vld");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> id = new ArrayList<String>();
		List<String> vendor = new ArrayList<String>();
		List<String> descriptor_version = new ArrayList<String>();
		List<String> number_of_endpoints = new ArrayList<String>();
		List<String> root_requirement = new ArrayList<String>();
		List<String> leaf_requirement = new ArrayList<String>();
		List<String> qos = new ArrayList<String>();
		List<String> qos_no = new ArrayList<String>();
		List<String> test_access = new ArrayList<String>();
		List<String> connection = new ArrayList<String>();
		List<String> connection_no = new ArrayList<String>();
		List<String> connectivity_type = new ArrayList<String>();
		List<String> vld_security = new ArrayList<String>();
		List<String> vld_security_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			descriptor_version.add(map.get("descriptor_version"));
			number_of_endpoints.add(map.get("number_of_endpoints"));
			root_requirement.add(map.get("root_requirement"));
			leaf_requirement.add(map.get("leaf_requirement"));
			qos.add(map.get("qos"));
			qos_no.add(map.get("qos_no"));
			test_access.add(map.get("test_access"));
			connection.add(map.get("connection"));
			connection_no.add(map.get("connection_no"));
			connectivity_type.add(map.get("connectivity_type"));
			vld_security.add(map.get("vld_security"));
			vld_security_no.add(map.get("vld_security_no"));

		}

		Element rootElement = new Element("vld");

		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));
		rootElement.addContent(new Element("number_of_endpoints").addContent(number_of_endpoints.get(0)));
		rootElement.addContent(new Element("root_requirement").addContent(root_requirement.get(0)));
		rootElement.addContent(new Element("leaf_requirement").addContent(leaf_requirement.get(0)));

		Element qos_list_el = new Element("qos_list");

		int i = 0;
		for (String s : qos) {

			if (!"NULL".equals(s)) {

				Element qos_el = new Element("qos");
				qos_el.addContent(new Element("key").addContent(qos_no.get(i)));
				qos_el.addContent(new Element("item").addContent(s));

				qos_list_el.addContent(qos_el);
			}

			i++;

		}

		rootElement.addContent(qos_list_el);

		rootElement.addContent(new Element("test_access").addContent(test_access.get(0)));

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

		rootElement.addContent(new Element("connectivity_type").addContent(connectivity_type.get(0)));

		Element vld_security_el = new Element("vld_security");

		i = 0;
		for (String s : vld_security) {

			if (!"NULL".equals(s)) {

				vld_security_el.addContent(new Element("key").addContent(vld_security_no.get(i)));
				vld_security_el.addContent(new Element("item").addContent(s));

				break;
			}

			i++;
		}

		rootElement.addContent(vld_security_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postVld(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		if (version_result != null && version_result.size() > 0) {
			return -1;
		}

		// table_seq
		String vld_seq = nfvCommonDAO.getSeq("31");
		String version_seq = nfvCommonDAO.getSeq("10");
		String qos_ref_seq = nfvCommonDAO.getSeq("82");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String seq_seq = nfvCommonDAO.getSeq("02");

		hm.put("vld_no", vld_seq);
		hm.put("version_no", version_seq);
		hm.put("qos_ref_no", qos_ref_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("security_ref_no", seq_seq);

		atViewDAO.postVld(hm);
		atViewDAO.postVersion(hm);

		String[] qos_key_list = (String[]) hm.get("qos_key_list");

		if (qos_key_list != null) {

			for (String s : qos_key_list) {
				hm.put("qos_ref_no", qos_ref_seq);
				hm.put("qos_no", s);
				atViewDAO.postQosRef(hm);
			}

		}

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_seq);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] security_list = (String[]) hm.get("security_list");

		if (security_list != null) {

			for (String s : security_list) {
				hm.put("security_ref_no", seq_seq);
				hm.put("security_no", s);
				atViewDAO.postSecurityRef(hm);
			}

		}

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VLD"));
		hm.put("func_ref_no", version_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putVld(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> vld_result = atViewDAO.getVldOne(hm);

		String qos_ref_no = vld_result.get("qos_ref_no");
		String cp_ref_no = vld_result.get("connection_ref_no");
		String security_ref_no = vld_result.get("vld_security_ref_no");

		hm.put("qos_ref_no", qos_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		atViewDAO.deleteQosRef(hm);
		atViewDAO.deleteCpRef(hm);
		atViewDAO.deleteSecurityRef(hm);

		atViewDAO.putVersion(hm);
		atViewDAO.putVld(hm);

		String[] qos_key_list = (String[]) hm.get("qos_key_list");

		for (String s : qos_key_list) {
			hm.put("qos_ref_no", qos_ref_no);
			hm.put("qos_no", s);
			atViewDAO.postQosRef(hm);
		}

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_no);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] security_list = (String[]) hm.get("security_list");

		for (String s : security_list) {
			hm.put("security_ref_no", security_ref_no);
			hm.put("security_no", s);
			atViewDAO.postSecurityRef(hm);
		}

		return 1;
	}

	@Transactional
	public int deleteVld(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("function_no", version_no);

		int resultCnt = atViewDAO.passableDeleteMainContent(hm);

		if (resultCnt > 0) {
			return -2;
		}

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> vld_result = atViewDAO.getVldOne(hm);

		String qos_ref_no = vld_result.get("qos_ref_no");
		String cp_ref_no = vld_result.get("connection_ref_no");
		String security_ref_no = vld_result.get("vld_security_ref_no");

		hm.put("qos_ref_no", qos_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		atViewDAO.deleteVersion(hm);
		atViewDAO.deleteVld(hm);
		atViewDAO.deleteQosRef(hm);
		atViewDAO.deleteCpRef(hm);
		atViewDAO.deleteSecurityRef(hm);

		hm.put("func_ref_no", version_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// cp
	public String getCp(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getCp(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("cp");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		LinkedHashMap<String, String> resultMap = resultList.get(0);

		Element rootElement = new Element("cp");
		rootElement.addContent(new Element("cp_no").addContent(resultMap.get("cp_no")));
		rootElement.addContent(new Element("id").addContent(resultMap.get("id")));
		rootElement
				.addContent(new Element("virtual_link_reference").addContent(resultMap.get("virtual_link_reference")));
		rootElement.addContent(new Element("type").addContent(resultMap.get("type")));

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@Transactional
	public int postCp(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getCp(hm);

		if (resultList != null && resultList.size() > 0) {
			return -1;
		}

		String cp_no = nfvCommonDAO.getSeq("07");

		hm.put("cp_no", cp_no);

		int i = atViewDAO.postCp(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("CP"));
		hm.put("func_ref_no", cp_no);

		atViewDAO.postTmenuRef(hm);

		return i;
	}

	@Transactional
	public int putCp(HashMap<String, Object> hm) {

		int i = atViewDAO.putCp(hm);
		return i;

	}

	@Transactional
	public int deleteCp(HashMap<String, Object> hm) {

		int resultCnt = atViewDAO.passableDeleteCp(hm);

		System.out.println("resultCnt =" + resultCnt);

		if (resultCnt > 0) {
			return -2;
		}

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getCp(hm);
		LinkedHashMap<String, String> resultMap = resultList.get(0);
		String cp_no = resultMap.get("cp_no");

		hm.put("func_ref_no", cp_no);

		atViewDAO.deleteTmenuRef(hm);

		int i = atViewDAO.deleteCp(hm);

		return i;
	}

	// pnfd
	public String getPnfd(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getPnfd(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("pnfd");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> id = new ArrayList<String>();
		List<String> vendor = new ArrayList<String>();
		List<String> version = new ArrayList<String>();
		List<String> description = new ArrayList<String>();
		List<String> connection_point_id = new ArrayList<String>();
		List<String> connection_point_no = new ArrayList<String>();
		List<String> connection_point_type = new ArrayList<String>();
		List<String> descriptor_version = new ArrayList<String>();
		List<String> pnfd_security = new ArrayList<String>();
		List<String> pnfd_security_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			id.add(map.get("id"));
			vendor.add(map.get("vendor"));
			version.add(map.get("version"));
			description.add(map.get("description"));
			connection_point_id.add(map.get("connection_point_id"));
			connection_point_no.add(map.get("connection_point_no"));
			connection_point_type.add(map.get("connection_point_type"));
			descriptor_version.add(map.get("descriptor_version"));
			pnfd_security.add(map.get("pnfd_security"));
			pnfd_security_no.add(map.get("pnfd_security_no"));

		}

		Element rootElement = new Element("pnfd");

		rootElement.addContent(new Element("id").addContent(id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vendor.get(0)));
		rootElement.addContent(new Element("version").addContent(version.get(0)));
		rootElement.addContent(new Element("description").addContent(description.get(0)));

		Element connection_list_el = new Element("connection_list");

		int i = 0;
		for (String s : connection_point_id) {

			if (!"NULL".equals(s)) {

				Element connection_el = new Element("connection");
				connection_el.addContent(new Element("key").addContent(connection_point_no.get(i)));
				connection_el.addContent(new Element("item").addContent(s));
				// connection_el.addContent(new
				// Element("type").addContent(connection_point_type.get(i)));
				connection_list_el.addContent(connection_el);
			}

			i++;
		}

		rootElement.addContent(connection_list_el);

		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));

		Element pnfd_security_el = new Element("pnfd_security");

		i = 0;
		for (String s : pnfd_security) {

			if (!"NULL".equals(s)) {

				pnfd_security_el.addContent(new Element("key").addContent(pnfd_security_no.get(i)));
				pnfd_security_el.addContent(new Element("item").addContent(s));

				break;
			}

			i++;
		}

		rootElement.addContent(pnfd_security_el);

		return CommonXmlUtil.getElementToString(rootElement);

	}

	@Transactional
	public int postPnfd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		if (version_result != null && version_result.size() > 0) {
			return -1;
		}

		// table_seq
		String pnfd_seq = nfvCommonDAO.getSeq("71");
		String version_seq = nfvCommonDAO.getSeq("10");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String seq_seq = nfvCommonDAO.getSeq("02");

		hm.put("pnfd_no", pnfd_seq);
		hm.put("version_no", version_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("security_ref_no", seq_seq);

		atViewDAO.postPnfd(hm);
		atViewDAO.postVersion(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_seq);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] security_list = (String[]) hm.get("security_list");

		for (String s : security_list) {
			hm.put("security_ref_no", seq_seq);
			hm.put("security_no", s);
			atViewDAO.postSecurityRef(hm);
		}

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("PNFD"));
		hm.put("func_ref_no", version_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putPnfd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> pnfd_result = atViewDAO.getPnfdOne(hm);

		String cp_ref_no = pnfd_result.get("connection_point_ref_no");
		String security_ref_no = pnfd_result.get("pnfd_security_ref_no");

		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		atViewDAO.deleteCpRef(hm);
		atViewDAO.deleteSecurityRef(hm);

		atViewDAO.putVersion(hm);
		atViewDAO.putPnfd(hm);

		String[] cp_key_list = (String[]) hm.get("cp_key_list");

		for (String s : cp_key_list) {
			hm.put("cp_ref_seq", cp_ref_no);
			hm.put("cp_no", s);
			atViewDAO.postCpRef(hm);
		}

		String[] security_list = (String[]) hm.get("security_list");

		for (String s : security_list) {
			hm.put("security_ref_no", security_ref_no);
			hm.put("security_no", s);
			atViewDAO.postSecurityRef(hm);
		}

		return 1;

	}

	@Transactional
	public int deletePnfd(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> version_result = atViewDAO.getVersionOne(hm);

		String version_no = version_result.get("version_no");

		hm.put("function_no", version_no);

		int resultCnt = atViewDAO.passableDeleteMainContent(hm);

		if (resultCnt > 0) {
			return -2;
		}

		hm.put("version_no", version_no);

		LinkedHashMap<String, String> pnfd_result = atViewDAO.getPnfdOne(hm);

		String cp_ref_no = pnfd_result.get("connection_point_ref_no");
		String security_ref_no = pnfd_result.get("pnfd_security_ref_no");

		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		atViewDAO.deleteVersion(hm);
		atViewDAO.deletePnfd(hm);
		atViewDAO.deleteCpRef(hm);
		atViewDAO.deleteSecurityRef(hm);

		hm.put("func_ref_no", version_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// tmenu
	@Transactional
	public int postTmenu(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> resultMap = atViewDAO.getTmenuOne(hm);

		if (resultMap != null && resultMap.size() > 0) {
			return -1;
		}

		String tmenu_no = nfvCommonDAO.getSeq("05");

		hm.put("tmenu_no", tmenu_no);

		atViewDAO.postTmenu(hm);

		return 1;
	}

	@Transactional
	public int deleteTmenu(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> resultMap = atViewDAO.getTmenuOne(hm);

		if (resultMap == null) {
			return -3;
		}

		if (!"".equals(resultMap.get("tmenu_ref_no"))) {
			return -1;
		}

		atViewDAO.deleteTmenu(hm);

		return 1;

	}

	// security
	@Transactional
	public String getSecurity(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getSecurity(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("security_list");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		Element rootElement = new Element("security_list");

		for (LinkedHashMap<String, String> resultMap : resultList) {
			Element security_el = new Element("security");
			security_el.addContent(new Element("security_no").addContent(resultMap.get("security_no")));
			security_el.addContent(new Element("item").addContent(resultMap.get("item")));
			rootElement.addContent(security_el);
		}

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int postAndPutSecurity(HashMap<String, Object> hm) {

		List<String> security_no_list = (List<String>) hm.get("security_no_list");
		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				if ("".equals(security_no_list.get(k))) {

					paramMap = new HashMap<String, Object>();

					paramMap.put("item", item_list.get(k));
					resultCnt += postSecurity(paramMap);

				} else {

					paramMap = new HashMap<String, Object>();
					paramMap.put("security_no", security_no_list.get(k));
					paramMap.put("item", item_list.get(k));
					resultCnt += putSecurity(paramMap);
				}

			}

			k++;
		}

		return resultCnt;
	}

	private int postSecurity(HashMap<String, Object> hm) {

		String security_no = nfvCommonDAO.getSeq("01");

		hm.put("security_no", security_no);

		atViewDAO.postSecurity(hm);

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("SECURITY"));
		hm.put("func_ref_no", security_no);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	private int putSecurity(HashMap<String, Object> hm) {
		int i = atViewDAO.putSecurity(hm);
		return i;
	}

	@Transactional
	public int deleteSecurity(HashMap<String, Object> hm) {

		List<String> item_list = (List<String>) hm.get("item_list");
		List<String> checked_list = (List<String>) hm.get("checked_list");

		int resultCnt = 0;

		HashMap<String, Object> paramMap;

		int k = 0;

		for (String s : checked_list) {

			if ("true".equals(s)) {

				paramMap = new HashMap<String, Object>();

				paramMap.put("item", item_list.get(k));

				int result = atViewDAO.passableDeleteSecurity(paramMap);

				if (result == 0) {

					List<LinkedHashMap<String, String>> resultList = atViewDAO.getSecurity(paramMap);
					LinkedHashMap<String, String> resultMap = resultList.get(0);
					String security_no = resultMap.get("security_no");

					hm.put("func_ref_no", security_no);

					atViewDAO.deleteTmenuRef(paramMap);

					resultCnt += atViewDAO.deleteSecurity(paramMap);
				}
			}

			k++;
		}

		return resultCnt;
	}

	// vud
	public String getVdu(HashMap<String, Object> hm) {

		List<LinkedHashMap<String, String>> resultList = atViewDAO.getVdu(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vdu");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> vdu_no = new ArrayList<String>();
		List<String> vdu_id = new ArrayList<String>();
		List<String> vm_image = new ArrayList<String>();
		List<String> computation_requirement = new ArrayList<String>();
		List<String> vmre = new ArrayList<String>();
		List<String> vnbr = new ArrayList<String>();
		List<String> life_item = new ArrayList<String>();
		List<String> life_no = new ArrayList<String>();
		List<String> vdu_constraint = new ArrayList<String>();
		List<String> high_availability = new ArrayList<String>();
		List<String> scale_in_out = new ArrayList<String>();
		List<String> vnfc_id = new ArrayList<String>();
		List<String> vnfc_no = new ArrayList<String>();
		List<String> vnfc_id_temp = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>();
		List<String> cp_vlr = new ArrayList<String>();
		List<String> cp_type = new ArrayList<String>();
		List<String> mnt_item = new ArrayList<String>();
		List<String> mnt_no = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			vdu_no.add(map.get("vdu_no"));
			vdu_id.add(map.get("vdu_id"));
			vm_image.add(map.get("vm_image"));
			computation_requirement.add(map.get("computation_requirement"));
			vmre.add(map.get("vmre"));
			vnbr.add(map.get("vnbr"));
			life_item.add(map.get("life_item"));
			life_no.add(map.get("life_no"));
			vdu_constraint.add(map.get("vdu_constraint"));
			high_availability.add(map.get("high_availability"));
			scale_in_out.add(map.get("scale_in_out"));
			vnfc_id.add(map.get("vnfc_id"));
			vnfc_no.add(map.get("vnfc_no"));
			cp_id.add(map.get("cp_id"));
			cp_vlr.add(map.get("cp_vlr"));
			cp_type.add(map.get("cp_type"));
			mnt_item.add(map.get("mnt_item"));
			mnt_no.add(map.get("mnt_no"));

		}

		Element rootElement = new Element("vdu");

		rootElement.addContent(new Element("id").addContent(vdu_id.get(0)));
		rootElement.addContent(new Element("vm_image").addContent(vm_image.get(0)));
		rootElement.addContent(new Element("computation_requirement").addContent(computation_requirement.get(0)));
		rootElement.addContent(new Element("virtual_memory_resource_element").addContent(vmre.get(0)));
		rootElement.addContent(new Element("virtual_network_bandwidth_resource").addContent(vnbr.get(0)));

		Element lifecycle_event_list_el = new Element("lifecycle_event_list");

		int i = 0;
		for (String o : life_item) {

			if (!"NULL".equals(o)) {

				Element lifecycle_event_el = new Element("lifecycle_event");
				lifecycle_event_el.addContent(new Element("key").addContent(life_no.get(i)));
				lifecycle_event_el.addContent(new Element("item").addContent(o));
				lifecycle_event_list_el.addContent(lifecycle_event_el);

			}

			i++;
		}

		rootElement.addContent(lifecycle_event_list_el);
		rootElement.addContent(new Element("constraint").addContent(vdu_constraint.get(0)));
		rootElement.addContent(new Element("high_availability").addContent(high_availability.get(0)));
		rootElement.addContent(new Element("scale_in_out").addContent(scale_in_out.get(0)));

		Element vnfc_list_el = new Element("vnfc_list");

		i = 0;

		Element connection_point_el_temp = null;

		for (String o : vnfc_id) {

			if (!"NULL".equals(o)) {

				if (vnfc_id_temp.contains(o)) {

					Element connection_point_info_el = new Element("connection_point_info");
					connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
					connection_point_info_el
							.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(i)));
					connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(i)));
					connection_point_el_temp.addContent(connection_point_info_el);

				} else {

					Element vnfc_el = new Element("vnfc");
					vnfc_el.addContent(new Element("key").addContent(vnfc_no.get(i)));
					vnfc_el.addContent(new Element("id").addContent(o));

					Element connection_point_el = new Element("connection_point");
					connection_point_el_temp = connection_point_el;
					Element connection_point_info_el = new Element("connection_point_info");
					connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(i)));
					connection_point_info_el
							.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(i)));
					connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(i)));
					connection_point_el.addContent(connection_point_info_el);

					vnfc_el.addContent(connection_point_el);

					vnfc_list_el.addContent(vnfc_el);

				}

				vnfc_id_temp.add(o);
			}

			i++;
		}

		rootElement.addContent(vnfc_list_el);

		boolean mnt_item_flag = false;

		for (String o : mnt_item) {

			if (!"NULL".equals(o)) {
				mnt_item_flag = true;
				break;
			}
		}

		Element monitoring_parameter_list_el = new Element("monitoring_parameter_list");

		i = 0;

		if (mnt_item_flag) {

			for (String o : mnt_item) {

				if (!"NULL".equals(o)) {
					Element monitoring_parameter_el = new Element("monitoring_parameter");
					monitoring_parameter_el.addContent(new Element("key").addContent(mnt_no.get(i)));
					monitoring_parameter_el.addContent(new Element("item").addContent(o));
					monitoring_parameter_list_el.addContent(monitoring_parameter_el);

				}

				i++;

			}

		}

		rootElement.addContent(monitoring_parameter_list_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postVdu(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> vdu_result = atViewDAO.getVduOne(hm);

		if (vdu_result != null && vdu_result.size() > 0) {
			return -1;
		}

		// table_seq
		String vdu_seq = nfvCommonDAO.getSeq("33");
		String life_ref_seq = nfvCommonDAO.getSeq("44");
		String vnfc_ref_seq = nfvCommonDAO.getSeq("88");
		String monitoring_ref_seq = nfvCommonDAO.getSeq("42");

		hm.put("vdu_no", vdu_seq);
		hm.put("vnfc_ref_no", vnfc_ref_seq);
		hm.put("mnt_ref_no", monitoring_ref_seq);
		hm.put("life_ref_no", life_ref_seq);

		atViewDAO.postVdu(hm);

		String[] lifecycle_event_key_list = (String[]) hm.get("lifecycle_event_key_list");

		HashMap<String, Object> refTableMap = null;

		for (String s : lifecycle_event_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("life_ref_no", life_ref_seq);
			refTableMap.put("life_no", s);
			atViewDAO.postLifecycleRef(refTableMap);

		}

		String[] vnfc_key_list = (String[]) hm.get("vnfc_key_list");

		for (String s : vnfc_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("vnfc_ref_no", vnfc_ref_seq);
			refTableMap.put("vnfc_no", s);
			atViewDAO.postVnfcRef(refTableMap);
		}

		String[] monitoring_parameter_key_list = (String[]) hm.get("monitoring_parameter_key_list");

		for (String s : monitoring_parameter_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("mnt_ref_no", monitoring_ref_seq);
			refTableMap.put("mnt_no", s);
			atViewDAO.postMonitoringRef(refTableMap);
		}

		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VDU"));
		hm.put("func_ref_no", vdu_seq);

		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putVdu(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> vdu_result = atViewDAO.getVduOne(hm);

		if (vdu_result == null || vdu_result.size() == 0) {
			return -1;
		}

		// table_seq
		String life_ref_seq = vdu_result.get("life_ref_no");
		String vnfc_ref_seq = vdu_result.get("vnfc_ref_no");
		String monitoring_ref_seq = vdu_result.get("mnt_ref_no");

		atViewDAO.putVdu(hm);

		hm.put("life_ref_no", life_ref_seq);
		hm.put("vnfc_ref_no", vnfc_ref_seq);
		hm.put("mnt_ref_no", monitoring_ref_seq);

		atViewDAO.deleteLifecycleRef(hm);
		atViewDAO.deleteVnfcRef(hm);
		atViewDAO.deleteMonitoringRef(hm);

		String[] lifecycle_event_key_list = (String[]) hm.get("lifecycle_event_key_list");

		HashMap<String, Object> refTableMap = null;

		for (String s : lifecycle_event_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("life_ref_no", life_ref_seq);
			refTableMap.put("life_no", s);
			atViewDAO.postLifecycleRef(refTableMap);

		}

		String[] vnfc_key_list = (String[]) hm.get("vnfc_key_list");

		for (String s : vnfc_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("vnfc_ref_no", vnfc_ref_seq);
			refTableMap.put("vnfc_no", s);
			atViewDAO.postVnfcRef(refTableMap);
		}

		String[] monitoring_parameter_key_list = (String[]) hm.get("monitoring_parameter_key_list");

		for (String s : monitoring_parameter_key_list) {

			refTableMap = new HashMap<String, Object>();

			refTableMap.put("mnt_ref_no", monitoring_ref_seq);
			refTableMap.put("mnt_no", s);
			atViewDAO.postMonitoringRef(refTableMap);
		}

		return 1;
	}

	@Transactional
	public int deleteVdu(HashMap<String, Object> hm) {

		LinkedHashMap<String, String> vdu_result = atViewDAO.getVduOne(hm);

		String vdu_no = vdu_result.get("vdu_no");

		hm.put("function_no", vdu_no);

		int resultCnt = atViewDAO.passableDeleteMainContent(hm);

		if (resultCnt > 0) {
			return -2;
		}

		String life_ref_seq = vdu_result.get("life_ref_no");
		String vnfc_ref_seq = vdu_result.get("vnfc_ref_no");
		String monitoring_ref_seq = vdu_result.get("mnt_ref_no");

		hm.put("life_ref_no", life_ref_seq);
		hm.put("vnfc_ref_no", vnfc_ref_seq);
		hm.put("mnt_ref_no", monitoring_ref_seq);

		atViewDAO.deleteLifecycleRef(hm);
		atViewDAO.deleteVnfcRef(hm);
		atViewDAO.deleteMonitoringRef(hm);

		atViewDAO.deleteVdu(hm);

		hm.put("func_ref_no", vdu_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// nfvi 실제 데이터 연동.
	@Transactional
	public String getNfvi(HashMap<String, Object> hm) {
		List<LinkedHashMap<String, String>> resultList = atViewDAO.getNfvi(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("nfvi");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> nfvi_no = new ArrayList<String>();
		List<String> nfvi_id = new ArrayList<String>();
		List<String> image_no = new ArrayList<String>();
		List<String> image_name = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {

			nfvi_id.add(map.get("nfvi_id"));
			nfvi_no.add(map.get("nfvi_no"));
			image_no.add(map.get("image_no"));
			image_name.add(map.get("image_name"));
		}

		Element rootElement = new Element("nfvi");
		rootElement.addContent(new Element("id").addContent(nfvi_id.get(0)));
		Element image_list_el = new Element("image_list");

		int i = 0;
		for (String s : image_name) {
			if (!"NULL".equals(s)) {
				Element image_resource_el = new Element("image");
				image_resource_el.addContent(new Element("key").addContent(image_no.get(i)));
				image_resource_el.addContent(new Element("item").addContent(s));
				image_list_el.addContent(image_resource_el);
			}

			i++;
		}

		rootElement.addContent(image_list_el);
		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postNfvi(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);

		if (nfvi_result != null && nfvi_result.size() > 0) {
			logger.debug("존재하는 id 입니다. : " + hm.get("id"));
			return -1;
		}

		// table_seq
		String nfvi_no = nfvCommonDAO.getSeq("N1");
		// String image_no = nfvCommonDAO.getSeq("N2");

		hm.put("nfvi_no", nfvi_no);
		// hm.put("image_no", image_no);

		// 1. nfv_nfvi_tab insert
		atViewDAO.postNfvi(hm);

		// 2. nfv_image_tab insert
		String[] image_name_list = (String[]) hm.get("image_name_list");
		for (String s : image_name_list) {
			String image_no = nfvCommonDAO.getSeq("N2");

			hm.put("image_no", image_no);
			hm.put("image_name", s);

			atViewDAO.postImageTab(hm);
		}

		// 3. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NFVI"));
		hm.put("func_ref_no", nfvi_no);
		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putNfvi(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);

		// 1. nfv_image_tab delete
		String nfvi_no = nfvi_result.get("nfvi_no");
		hm.put("nfvi_no", nfvi_no);
		atViewDAO.deleteImageTab(hm);

		// 2. nfv_image_tab insert
		String[] image_name_list = (String[]) hm.get("image_name_list");
		for (String s : image_name_list) {
			String image_no = nfvCommonDAO.getSeq("N2");

			hm.put("image_no", image_no);
			hm.put("image_name", s);

			atViewDAO.postImageTab(hm);
		}

		return 1;
	}

	@Transactional
	public int deleteNfvi(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);

		// 1. nfv_image_tab delete
		String nfvi_no = nfvi_result.get("nfvi_no");
		hm.put("nfvi_no", nfvi_no);
		atViewDAO.deleteImageTab(hm);

		// 2. nfv_nfvi_tab delete
		atViewDAO.deleteNfvi(hm);

		// 3. tmenu delete
		hm.put("func_ref_no", nfvi_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// nfvi(기존 nfvid)
	/*
	 * @Transactional public String getNfvi(HashMap<String, Object> hm) {
	 * List<LinkedHashMap<String, String>> resultList = atViewDAO.getNfvi(hm);
	 * 
	 * if(resultList == null || resultList.size() < 1){
	 * 
	 * Element rootElement = new Element("nfvi"); return
	 * CommonXmlUtil.getElementToString(rootElement); }
	 * 
	 * List<String> nfvid_ref_no= new ArrayList<String>(); List<String> nfvid_no=
	 * new ArrayList<String>(); List<String> nfvid_id= new ArrayList<String>();
	 * List<String> nfvidc_no= new ArrayList<String>(); List<String> nfvidc_id= new
	 * ArrayList<String>(); List<String> nfvidc_id_temp= new ArrayList<String>();
	 * 
	 * List<String> mnt_ref_no= new ArrayList<String>(); List<String> func_ref_no=
	 * new ArrayList<String>(); List<String> mnt_no= new ArrayList<String>();
	 * List<String> mnt_item= new ArrayList<String>(); List<String> function_no= new
	 * ArrayList<String>();
	 * 
	 * List<String> ver_item= new ArrayList<String>(); List<String> ver_id= new
	 * ArrayList<String>();
	 * 
	 * for(LinkedHashMap<String, String> map : resultList){
	 * 
	 * nfvid_ref_no.add(map.get("nfvid_ref_no")); nfvid_no.add(map.get("nfvid_no"));
	 * nfvid_id.add(map.get("nfvid_id")); nfvidc_no.add(map.get("nfvidc_no"));
	 * nfvidc_id.add(map.get("nfvidc_id"));
	 * 
	 * mnt_ref_no.add(map.get("mnt_ref_no"));
	 * func_ref_no.add(map.get("func_ref_no")); mnt_no.add(map.get("mnt_no"));
	 * mnt_item.add(map.get("mnt_item")); function_no.add(map.get("function_no"));
	 * 
	 * ver_item.add(map.get("ver_item")); ver_id.add(map.get("ver_id")); }
	 * 
	 * Element rootElement = new Element("nfvi"); rootElement.addContent(new
	 * Element("id").addContent(nfvid_id.get(0))); Element virtual_resource_list_el
	 * = new Element("virtual_resource_list");
	 * 
	 * int i = 0; for(String s : mnt_item){ if( ! "NULL".equals(s) &&
	 * "NULL".equals(nfvidc_id.get(i))){ Element virtual_resource_el = new
	 * Element("virtual_resource"); virtual_resource_el.addContent(new
	 * Element("key").addContent(mnt_no.get(i))); virtual_resource_el.addContent(new
	 * Element("item").addContent(s));
	 * virtual_resource_list_el.addContent(virtual_resource_el); }
	 * 
	 * i++; }
	 * 
	 * rootElement.addContent(virtual_resource_list_el); Element component_el = new
	 * Element("component");
	 * 
	 * i = 0; for(String s : nfvidc_id){ if( ! "NULL".equals(s) &&
	 * !nfvidc_id_temp.contains(s)){ Element component_info_el = new
	 * Element("component_info"); component_info_el.addContent(new
	 * Element("key").addContent(nfvidc_no.get(i)));
	 * component_info_el.addContent(new Element("id").addContent(s));
	 * 
	 * Element resource_el = new Element("resource");
	 * 
	 * int q = 0; for(String k : mnt_item){ if(! "NULL".equals(k) &&
	 * s.equals(nfvidc_id.get(q))){ resource_el.addContent(new
	 * Element("item").addContent(k)); }
	 * 
	 * q++; }
	 * 
	 * component_info_el.addContent(resource_el); Element vnf_el = new
	 * Element("vnf");
	 * 
	 * q = 0; for(String k : ver_id){ if(! "NULL".equals(k) &&
	 * s.equals(nfvidc_id.get(q))){ Element vnf_info_el = new Element("vnf_info");
	 * vnf_info_el.addContent(new Element("id").addContent(k));
	 * vnf_info_el.addContent(new Element("status").addContent(ver_item.get(q)));
	 * vnf_el.addContent(vnf_info_el); }
	 * 
	 * q++; }
	 * 
	 * component_info_el.addContent(vnf_el);
	 * component_el.addContent(component_info_el); nfvidc_id_temp.add(s); }
	 * 
	 * i++; }
	 * 
	 * rootElement.addContent(component_el);
	 * 
	 * return CommonXmlUtil.getElementToString(rootElement); }
	 * 
	 * @Transactional public int postNfvi(HashMap<String, Object> hm) {
	 * LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);
	 * 
	 * if(nfvi_result != null && nfvi_result.size() > 0) { return -1; }
	 * 
	 * //table_seq String nfvi_seq = nfvCommonDAO.getSeq("93"); String
	 * monitoring_ref_seq = nfvCommonDAO.getSeq("42"); String function_ref_seq =
	 * nfvCommonDAO.getSeq("11");
	 * 
	 * hm.put("nfvid_no", nfvi_seq); hm.put("mnt_ref_no", monitoring_ref_seq);
	 * hm.put("func_ref_no", function_ref_seq);
	 * 
	 * // 1. nfv_nfvid_tab insert atViewDAO.postNfvi(hm);
	 * 
	 * // 2. nfv_monitoring_ref insert String[] mnt_key_list =
	 * (String[])hm.get("mnt_key_list"); for(String s : mnt_key_list){
	 * hm.put("mnt_no", s); atViewDAO.postMntRef(hm); }
	 * 
	 * // 3. nfv_function_ref insert String[] func_key_list =
	 * (String[])hm.get("func_key_list"); for(String s : func_key_list){
	 * hm.put("func_no", s); atViewDAO.postFuncRef(hm); }
	 * 
	 * // 4. tmenu insert hm.put("tmenu_ref_no",
	 * nfvCommonDAO.getTmenuRefNo("NFVI")); hm.put("func_ref_no", nfvi_seq);
	 * atViewDAO.postTmenuRef(hm);
	 * 
	 * return 1; }
	 * 
	 * @Transactional public int putNfvi(HashMap<String, Object> hm) {
	 * LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);
	 * 
	 * // 1. nfv_monitoring_ref delete String mnt_ref_no =
	 * nfvi_result.get("mnt_ref_no"); hm.put("mnt_ref_no", mnt_ref_no);
	 * atViewDAO.deleteMntRef(hm);
	 * 
	 * // 2. nfv_function_ref delete String func_ref_no =
	 * nfvi_result.get("func_ref_no"); hm.put("func_ref_no", func_ref_no);
	 * atViewDAO.deleteFuncRef(hm);
	 * 
	 * // 3. nfv_monitoring_ref insert String[] mnt_key_list =
	 * (String[])hm.get("mnt_key_list"); for(String s : mnt_key_list){
	 * hm.put("mnt_no", s); atViewDAO.postMntRef(hm); }
	 * 
	 * // 4. nfv_function_ref insert String[] func_key_list =
	 * (String[])hm.get("func_key_list"); for(String s : func_key_list){
	 * hm.put("func_no", s); atViewDAO.postFuncRef(hm); }
	 * 
	 * return 1; }
	 * 
	 * @Transactional public int deleteNfvi(HashMap<String, Object> hm) {
	 * LinkedHashMap<String, String> nfvi_result = atViewDAO.getNfviOne(hm);
	 * 
	 * // 1. nfv_monitoring_ref delete String mnt_ref_no =
	 * nfvi_result.get("mnt_ref_no"); hm.put("mnt_ref_no", mnt_ref_no);
	 * atViewDAO.deleteMntRef(hm);
	 * 
	 * // 2. nfv_function_ref delete String func_ref_no =
	 * nfvi_result.get("func_ref_no"); hm.put("func_ref_no", func_ref_no);
	 * atViewDAO.deleteFuncRef(hm);
	 * 
	 * // 3. nfv_nfvidc_tab delete atViewDAO.deleteNfvi(hm);
	 * 
	 * // 4. tmenu delete String nfvid_no = nfvi_result.get("nfvid_no");
	 * hm.put("func_ref_no", nfvid_no); atViewDAO.deleteTmenuRef(hm);
	 * 
	 * return 1; }
	 */

	// vnfd
	@Transactional
	public String getVnfd(HashMap<String, Object> hm) {
		List<LinkedHashMap<String, String>> resultList = atViewDAO.getVnfd(hm);

		if (resultList == null || resultList.size() < 1) {

			Element rootElement = new Element("vnfd");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> vnfd_id = new ArrayList<String>(); // 1
		List<String> vnfd_vendor = new ArrayList<String>(); // 2
		List<String> vnfd_version = new ArrayList<String>(); // 2
		List<String> vnfd_no = new ArrayList<String>(); // 3
		List<String> version_no = new ArrayList<String>(); // 4
		List<String> descriptor_version = new ArrayList<String>(); // 5
		List<String> vdu_no = new ArrayList<String>(); // 6
		List<String> vdu_id = new ArrayList<String>(); // 6
		List<String> vdu_id_temp = new ArrayList<String>();
		List<String> vm_image = new ArrayList<String>(); // 7
		List<String> computation_requirement = new ArrayList<String>(); // 8
		List<String> vmre = new ArrayList<String>(); // 9
		List<String> vnbr = new ArrayList<String>(); // 10

		List<String> life_item = new ArrayList<String>(); // 11
		List<String> vdu_constraint = new ArrayList<String>(); // 12
		List<String> high_availability = new ArrayList<String>(); // 13
		List<String> scale_in_out = new ArrayList<String>(); // 14
		List<String> vnfc_id = new ArrayList<String>(); // 15
		List<String> vnfc_id_temp = new ArrayList<String>();
		List<String> cp_id = new ArrayList<String>(); // 16
		List<String> cp_vlr = new ArrayList<String>(); // 17
		List<String> cp_type = new ArrayList<String>(); // 18
		List<String> mnt_item = new ArrayList<String>(); // 19

		List<String> vld_version_no = new ArrayList<String>(); // 20
		List<String> vld_no = new ArrayList<String>(); // 20
		List<String> vld_id = new ArrayList<String>(); // 21
		List<String> vld_id_temp = new ArrayList<String>();
		List<String> vld_vendor = new ArrayList<String>(); // 22
		List<String> vld_version = new ArrayList<String>(); // 23
		List<String> number_of_endpoints = new ArrayList<String>(); // 24
		List<String> root_requirement = new ArrayList<String>(); // 25
		List<String> leaf_requirement = new ArrayList<String>(); // 26
		List<String> vld_qos = new ArrayList<String>(); // 27
		List<String> test_access = new ArrayList<String>(); // 28
		List<String> vld_con = new ArrayList<String>(); // 29
		List<String> vld_con_type = new ArrayList<String>(); // 30

		List<String> vld_security = new ArrayList<String>(); // 31
		List<String> vnfd_cp_no = new ArrayList<String>(); // 32
		List<String> vnfd_cp_id = new ArrayList<String>(); // 32
		List<String> vnfd_cp_id_temp = new ArrayList<String>();
		List<String> vnfd_cp_vlr = new ArrayList<String>(); // 33
		List<String> vnfd_cp_type = new ArrayList<String>(); // 34
		List<String> vnfd_life_no = new ArrayList<String>(); // 35
		List<String> vnfd_life_item = new ArrayList<String>(); // 35
		List<String> dep_no = new ArrayList<String>(); // 36
		List<String> dep_item = new ArrayList<String>(); // 36
		List<String> vnfd_mnt_no = new ArrayList<String>(); // 37
		List<String> vnfd_mnt_item = new ArrayList<String>(); // 37
		List<String> flavour_no = new ArrayList<String>(); // 38
		List<String> flavour_id = new ArrayList<String>(); // 38
		List<String> flavour_id_temp = new ArrayList<String>(); // 38
		List<String> flavour_key = new ArrayList<String>(); // 39
		List<String> cstr_item = new ArrayList<String>(); // 40

		List<String> cstr_vdu_id = new ArrayList<String>(); // 41
		List<String> cstr_vdu_id_temp = new ArrayList<String>();
		List<String> instance_cnt = new ArrayList<String>(); // 42
		List<String> cstr_vnfc_id = new ArrayList<String>(); // 43
		List<String> policy_no = new ArrayList<String>(); // 44
		List<String> policy_item = new ArrayList<String>(); // 44
		List<String> manifest_file = new ArrayList<String>(); // 45
		List<String> security_no = new ArrayList<String>(); // 46
		List<String> security_item = new ArrayList<String>(); // 46

		for (LinkedHashMap<String, String> map : resultList) {
			vnfd_id.add(map.get("vnfd_id"));
			vnfd_vendor.add(map.get("vnfd_vendor"));
			vnfd_version.add(map.get("vnfd_version"));
			vnfd_no.add(map.get("vnfd_no"));
			version_no.add(map.get("version_no"));
			descriptor_version.add(map.get("descriptor_version"));
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

			vld_version_no.add(map.get("vld_version_no"));
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
			vnfd_cp_no.add(map.get("vnfd_cp_no"));
			vnfd_cp_id.add(map.get("vnfd_cp_id"));
			vnfd_cp_vlr.add(map.get("vnfd_cp_vlr"));
			vnfd_cp_type.add(map.get("vnfd_cp_type"));
			vnfd_life_no.add(map.get("vnfd_life_no"));
			vnfd_life_item.add(map.get("vnfd_life_item"));
			dep_no.add(map.get("dep_no"));
			dep_item.add(map.get("dep_item"));
			vnfd_mnt_no.add(map.get("vnfd_mnt_no"));
			vnfd_mnt_item.add(map.get("vnfd_mnt_item"));
			flavour_no.add(map.get("flavour_no"));
			flavour_id.add(map.get("flavour_id"));
			flavour_key.add(map.get("flavour_key"));
			cstr_item.add(map.get("cstr_item"));

			cstr_vdu_id.add(map.get("cstr_vdu_id"));
			instance_cnt.add(map.get("instance_cnt"));
			cstr_vnfc_id.add(map.get("cstr_vnfc_id"));
			policy_no.add(map.get("policy_no"));
			policy_item.add(map.get("policy_item"));
			manifest_file.add(map.get("manifest_file"));
			security_no.add(map.get("security_no"));
			security_item.add(map.get("security_item"));

		}

		Element rootElement = new Element("vnfd");

		rootElement.addContent(new Element("id").addContent(vnfd_id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(vnfd_vendor.get(0)));
		rootElement.addContent(new Element("version").addContent(vnfd_version.get(0)));
		rootElement.addContent(new Element("descriptor_version").addContent(descriptor_version.get(0)));

		Element vdu_el = new Element("vdu");

		// vdu start
		int i = 0;
		for (String s : vdu_id) {
			if (!"NULL".equals(s) && !vdu_id_temp.contains(s)) {
				Element vdu_info_el = new Element("vdu_info");
				vdu_info_el.addContent(new Element("key").addContent(vdu_no.get(i)));
				vdu_info_el.addContent(new Element("id").addContent(s));
				vdu_info_el.addContent(new Element("vm_image").addContent(vm_image.get(i)));
				vdu_info_el
						.addContent(new Element("computation_requirement").addContent(computation_requirement.get(i)));
				vdu_info_el.addContent(new Element("virtual_memory_resource_element").addContent(vmre.get(i)));
				vdu_info_el.addContent(new Element("virtual_network_bandwidth_resource").addContent(vnbr.get(i)));

				Element lifecycle_event_el = new Element("lifecycle_event");

				int j = 0;
				for (String o : life_item) {
					if (!"NULL".equals(o) && s.equals(vdu_id.get(j))) {
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
				for (String o : vnfc_id) {
					if (!"NULL".equals(o) && s.equals(vdu_id.get(k))) {
						if (vnfc_id_temp.contains(o)) {
							Element connection_point_info_el = new Element("connection_point_info");

							connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(k)));
							connection_point_info_el
									.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(k)));
							connection_point_info_el.addContent(new Element("type").addContent(cp_type.get(k)));

							connection_point_el.addContent(connection_point_info_el);

						} else {
							Element vnfc_info_el = new Element("vnfc_info");
							vnfc_info_el.addContent(new Element("id").addContent(o));

							connection_point_el = new Element("connection_point");
							Element connection_point_info_el = new Element("connection_point_info");

							connection_point_info_el.addContent(new Element("id").addContent(cp_id.get(k)));
							connection_point_info_el
									.addContent(new Element("virtual_link_reference").addContent(cp_vlr.get(k)));
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
				for (String o : mnt_item) {
					if (!"NULL".equals(o) && s.equals(vdu_id.get(u))) {
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
		for (String s : vld_id) {
			if (!"NULL".equals(s) && !vld_id_temp.contains(s)) {
				Element virtual_link_info_el = new Element("virtual_link_info");
				virtual_link_info_el.addContent(new Element("key").addContent(vld_version_no.get(i)));
				virtual_link_info_el.addContent(new Element("id").addContent(s));
				virtual_link_info_el.addContent(new Element("connectivity_type").addContent(vld_con_type.get(i)));
				Element connection_point_reference_el = new Element("connection_point_reference");

				int k = 0;
				for (String q : vld_con) {
					if (!"NULL".equals(q) && s.equals(vld_id.get(k))) {
						connection_point_reference_el.addContent(new Element("ref").addContent(q));
					}
					k++;
				}
				virtual_link_info_el.addContent(connection_point_reference_el);
				virtual_link_info_el.addContent(new Element("root_requirement").addContent(root_requirement.get(i)));
				virtual_link_info_el.addContent(new Element("leaf_requirement").addContent(leaf_requirement.get(i)));

				Element qos_el = new Element("qos");

				k = 0;
				for (String q : vld_qos) {
					if (!"NULL".equals(q) && s.equals(vld_id.get(k))) {
						qos_el.addContent(new Element("item").addContent(q));
					}
					k++;
				}
				// virtual_link_info_el.addContent(new Element("qos").addContent(new
				// Element("item").addContent(vld_qos.get(i))));
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
		for (String s : vnfd_cp_id) {
			if (!"NULL".equals(s) && !vnfd_cp_id_temp.contains(s)) {
				Element connection_point_info_el = new Element("connection_point_info");
				connection_point_info_el.addContent(new Element("key").addContent(vnfd_cp_no.get(i)));
				connection_point_info_el.addContent(new Element("id").addContent(s));
				connection_point_info_el
						.addContent(new Element("virtual_link_reference").addContent(vnfd_cp_vlr.get(i)));
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

		int j = 0;
		for (String o : vnfd_life_item) {
			if (!"NULL".equals(o)) {
				Element lifecycle_event_info_el = new Element("lifecycle_event_info");
				lifecycle_event_info_el.addContent(new Element("key").addContent(vnfd_life_no.get(j)));
				lifecycle_event_info_el.addContent(new Element("item").addContent(o));
				lifecycle_event_el.addContent(lifecycle_event_info_el);
			}
			j++;
		}

		rootElement.addContent(lifecycle_event_el);

		// lifecycle_event end
		// dependency start

		Element dependency_el = new Element("dependency");

		j = 0;
		for (String o : dep_item) {
			if (!"NULL".equals(o)) {
				Element dependency_el_info_el = new Element("dependency_info");
				dependency_el_info_el.addContent(new Element("key").addContent(dep_no.get(j)));
				dependency_el_info_el.addContent(new Element("item").addContent(o));
				dependency_el.addContent(dependency_el_info_el);
			}
			j++;
		}

		rootElement.addContent(dependency_el);

		// dependency end
		// monitoring_parameter start

		Element monitoring_parameter_el = new Element("monitoring_parameter");

		j = 0;
		for (String o : vnfd_mnt_item) {
			if (!"NULL".equals(o)) {
				Element monitoring_parameter_info_el = new Element("monitoring_parameter_info");
				monitoring_parameter_info_el.addContent(new Element("key").addContent(vnfd_mnt_no.get(j)));
				monitoring_parameter_info_el.addContent(new Element("item").addContent(o));
				monitoring_parameter_el.addContent(monitoring_parameter_info_el);
			}
			j++;
		}

		rootElement.addContent(monitoring_parameter_el);

		// monitoring_parameter end
		// deployment_flavour start

		Element deployment_flavour_el = new Element("deployment_flavour");

		i = 0;
		for (String o : flavour_id) {
			if (!"NULL".equals(o) && !flavour_id_temp.contains(o)) {
				Element deployment_flavour_info_el = new Element("deployment_flavour_info");
				deployment_flavour_info_el.addContent(new Element("key").addContent(flavour_no.get(i)));
				deployment_flavour_info_el.addContent(new Element("id").addContent(o));
				deployment_flavour_info_el.addContent(new Element("flavour_key").addContent(flavour_key.get(i)));

				Element constraint_el = new Element("constraint");

				int q = 0;
				for (String k : cstr_item) {
					if (!"NULL".equals(k) && o.equals(flavour_id.get(q))) {
						constraint_el.addContent(new Element("item").addContent(k));
					}
					q++;
				}

				deployment_flavour_info_el.addContent(constraint_el);

				Element constituent_vdu_el = new Element("constituent_vdu");

				q = 0;
				for (String k : cstr_vdu_id) {
					if (!"NULL".equals(k) && o.equals(flavour_id.get(q)) && !cstr_vdu_id_temp.contains(o + k)) {
						Element constituent_vdu_info_el = new Element("constituent_vdu_info");

						constituent_vdu_info_el.addContent(new Element("vdu_reference").addContent(k));
						constituent_vdu_info_el
								.addContent(new Element("number_of_instances").addContent(instance_cnt.get(q)));

						Element constituent_vnfc_el = new Element("constituent_vnfc");

						int b = 0;
						for (String m : cstr_vnfc_id) {
							if (!"NULL".equals(m) && o.equals(flavour_id.get(b))) {
								constituent_vnfc_el.addContent(new Element("ref").addContent(m));
							}
							b++;
						}
						constituent_vdu_info_el.addContent(constituent_vnfc_el);

						constituent_vdu_el.addContent(constituent_vdu_info_el);
						cstr_vdu_id_temp.add(o + k);
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

		j = 0;
		for (String o : policy_item) {
			if (!"NULL".equals(o)) {
				Element auto_scale_policy_info_el = new Element("auto_scale_policy_info");
				auto_scale_policy_info_el.addContent(new Element("key").addContent(policy_no.get(j)));
				auto_scale_policy_info_el.addContent(new Element("item").addContent(o));
				auto_scale_policy_el.addContent(auto_scale_policy_info_el);
			}
			j++;
		}

		rootElement.addContent(auto_scale_policy_el);

		// auto_scale_policy end

		rootElement.addContent(new Element("manifest_file").addContent(manifest_file.get(0)));

		Element manifest_file_security_el = new Element("manifest_file_security");

		j = 0;
		for (String o : security_item) {
			if (!"NULL".equals(o)) {
				Element manifest_file_security_info_el = new Element("manifest_file_security_info");
				manifest_file_security_info_el.addContent(new Element("key").addContent(security_no.get(j)));
				manifest_file_security_info_el.addContent(new Element("item").addContent(o));
				manifest_file_security_el.addContent(manifest_file_security_info_el);
			}
			j++;
		}
		rootElement.addContent(manifest_file_security_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postVnfd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> vnfd_result = atViewDAO.getVnfdOne(hm);

		if (vnfd_result != null && vnfd_result.size() > 0) {
			return -1;
		}

		// table_seq
		String vnfd_seq = nfvCommonDAO.getSeq("32");
		String version_seq = nfvCommonDAO.getSeq("10");
		String vdu_ref_seq = nfvCommonDAO.getSeq("11");
		String vld_ref_seq = nfvCommonDAO.getSeq("12");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String life_ref_seq = nfvCommonDAO.getSeq("44");
		String dep_ref_seq = nfvCommonDAO.getSeq("12");
		String mnt_ref_seq = nfvCommonDAO.getSeq("42");
		String flavour_ref_seq = nfvCommonDAO.getSeq("72");
		String policy_ref_seq = nfvCommonDAO.getSeq("84");
		String security_ref_seq = nfvCommonDAO.getSeq("02");

		logger.info("--------------------------------------------vnfd_seq: " + vnfd_seq + ", version_seq: "
				+ version_seq + ", cp_ref_seq: " + cp_ref_seq);
		logger.info("--------------------------------------------vdu_ref_seq: " + vdu_ref_seq + ", vld_ref_seq: "
				+ vld_ref_seq + ", dep_ref_seq: " + dep_ref_seq);
		logger.info("--------------------------------------------life_ref_seq: " + life_ref_seq + ", mnt_ref_seq: "
				+ mnt_ref_seq + ", flavour_ref_seq: " + flavour_ref_seq);
		logger.info("--------------------------------------------policy_ref_seq: " + policy_ref_seq
				+ ", security_ref_seq: " + security_ref_seq);

		hm.put("vnfd_no", vnfd_seq);
		hm.put("version_no", version_seq);
		hm.put("vdu_ref_no", vdu_ref_seq);
		hm.put("vld_ref_no", vld_ref_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("life_ref_no", life_ref_seq);
		hm.put("dependency_ref_no", dep_ref_seq);
		hm.put("monitoring_no", mnt_ref_seq);
		hm.put("flavour_ref_no", flavour_ref_seq);
		hm.put("policy_ref_no", policy_ref_seq);
		hm.put("security_ref_no", security_ref_seq);

		// 1. nfv_vnfd_tab insert
		atViewDAO.postVnfd(hm);

		// 2. nfv_version insert
		atViewDAO.postVersion(hm);

		HashMap<String, Object> refTableMap = null;

		// 3. nfv_function_ref insert(vdu_ref_no, vld_ref_no, dependency_ref_no)
		// 3.1 vdu_ref_no
		String[] vdu_key_list = (String[]) hm.get("vdu_key_list");
		if (vdu_key_list != null) {
			for (String s : vdu_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vdu_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);

			}
		}
		// 3.2 vld_ref_no
		String[] vld_key_list = (String[]) hm.get("vld_key_list"); // vld_version_no_key_list
		if (vld_key_list != null) {
			for (String s : vld_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vld_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 3.3 dependency_ref_no
		String[] dep_key_list = (String[]) hm.get("dep_key_list");
		if (dep_key_list != null) {
			for (String s : dep_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", dep_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}

		// 4. nfv_cp_ref insert
		String[] vnfd_cp_key_list = (String[]) hm.get("vnfd_cp_key_list");
		if (vnfd_cp_key_list != null) {
			for (String s : vnfd_cp_key_list) {
				hm.put("cp_no", s);
				atViewDAO.postCpRef(hm);
			}
		}

		// 5. nfv_lifecycle_ref insert
		String[] lifecycle_key_list = (String[]) hm.get("lifecycle_key_list");
		if (lifecycle_key_list != null) {
			for (String s : lifecycle_key_list) {
				hm.put("life_no", s);
				atViewDAO.postLifecycleRef(hm);
			}
		}

		// 6. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		if (mnt_key_list != null) {
			for (String s : mnt_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("mnt_ref_no", mnt_ref_seq);
				refTableMap.put("mnt_no", s);
				atViewDAO.postMntRef(refTableMap);
			}
		}

		// 7. nfv_flavour_ref insert
		String[] flavour_key_list = (String[]) hm.get("flavour_key_list");
		if (flavour_key_list != null) {
			for (String s : flavour_key_list) {
				hm.put("flavour_no", s);
				atViewDAO.postFlavourRef(hm);
			}
		}

		// 8. nfv_policy_ref insert
		String[] policy_key_list = (String[]) hm.get("policy_key_list");
		if (policy_key_list != null) {
			for (String s : policy_key_list) {
				hm.put("policy_no", s);
				atViewDAO.postPolicyRef(hm);
			}
		}

		// 9. nfv_security_ref insert
		String[] security_key_list = (String[]) hm.get("security_key_list");
		if (security_key_list != null) {
			for (String s : security_key_list) {
				hm.put("security_no", s);
				atViewDAO.postSecurityRef(hm);
			}
		}

		// 10. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("VNFD"));
		hm.put("func_ref_no", version_seq);
		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putVnfd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> vnfd_result = atViewDAO.getVnfdOne(hm);

		String version_no = vnfd_result.get("version_no");
		String vnfd_no = vnfd_result.get("vnfd_no");
		String vdu_ref_no = vnfd_result.get("vdu_ref_no");
		String vld_ref_no = vnfd_result.get("vld_ref_no");
		String cp_ref_no = vnfd_result.get("cp_ref_no");
		String life_ref_no = vnfd_result.get("life_ref_no");
		String dependency_ref_no = vnfd_result.get("dependency_ref_no");
		String monitoring_no = vnfd_result.get("monitoring_no");
		String flavour_ref_no = vnfd_result.get("flavour_ref_no");
		String policy_ref_no = vnfd_result.get("policy_ref_no");
		String security_ref_no = vnfd_result.get("security_ref_no");

		hm.put("version_no", version_no);
		hm.put("vnfd_no", vnfd_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("life_ref_no", life_ref_no);
		hm.put("mnt_ref_no", monitoring_no);
		hm.put("flavour_ref_no", flavour_ref_no);
		hm.put("policy_ref_no", policy_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. nfv_vnfd_tab insert
		atViewDAO.putVnfd(hm);

		// 2. nfv_version insert
		atViewDAO.putVersion(hm);

		HashMap<String, Object> refTableMap = null;
		// 1. nfv_function_ref delete
		// 1.1 vdu_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vdu_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 1.2 vld_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vld_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 1.3 dependency_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", dependency_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 2. nfv_cp_ref delete
		atViewDAO.deleteCpRef(hm);

		// 3. nfv_lifecycle_ref delete
		atViewDAO.deleteLifecycleRef(hm);

		// 4. nfv_monitoring_ref delete
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("mnt_ref_no", monitoring_no);
		atViewDAO.deleteMntRef(refTableMap);

		// 5. nfv_flavour_ref delete
		atViewDAO.deleteFlavourRef(hm);

		// 6. nfv_policy_ref delete
		atViewDAO.deletePolicyRef(hm);

		// 7. nfv_security_ref delete
		atViewDAO.deleteSecurityRef(hm);

		// 8. nfv_function_ref insert(vdu_ref_no, vld_ref_no, dependency_ref_no)
		// 8.1 vdu_ref_no
		String[] vdu_key_list = (String[]) hm.get("vdu_key_list");
		if (vdu_key_list != null) {
			for (String s : vdu_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vdu_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);

			}
		}
		// 8.2 vld_ref_no
		String[] vld_key_list = (String[]) hm.get("vld_key_list");
		if (vld_key_list != null) {
			for (String s : vld_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vld_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 8.3 dependency_ref_no
		String[] dep_key_list = (String[]) hm.get("dep_key_list");
		if (dep_key_list != null) {
			for (String s : dep_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", dependency_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}

		// 9. nfv_cp_ref insert
		String[] vnfd_cp_key_list = (String[]) hm.get("vnfd_cp_key_list");
		if (vnfd_cp_key_list != null) {
			for (String s : vnfd_cp_key_list) {
				hm.put("cp_no", s);
				atViewDAO.postCpRef(hm);
			}
		}

		// 10. nfv_lifecycle_ref insert
		String[] lifecycle_key_list = (String[]) hm.get("lifecycle_key_list");
		if (lifecycle_key_list != null) {
			for (String s : lifecycle_key_list) {
				hm.put("life_no", s);
				atViewDAO.postLifecycleRef(hm);
			}
		}

		// 11. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		if (mnt_key_list != null) {
			for (String s : mnt_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("mnt_ref_no", monitoring_no);
				refTableMap.put("mnt_no", s);
				atViewDAO.postMntRef(refTableMap);
			}
		}

		// 12. nfv_flavour_ref insert
		String[] flavour_key_list = (String[]) hm.get("flavour_key_list");
		if (flavour_key_list != null) {
			for (String s : flavour_key_list) {
				hm.put("flavour_no", s);
				atViewDAO.postFlavourRef(hm);
			}
		}

		// 13. nfv_policy_ref insert
		String[] policy_key_list = (String[]) hm.get("policy_key_list");
		if (policy_key_list != null) {
			for (String s : policy_key_list) {
				hm.put("policy_no", s);
				atViewDAO.postPolicyRef(hm);
			}
		}

		// 14. nfv_security_ref insert
		String[] security_key_list = (String[]) hm.get("security_key_list");
		if (security_key_list != null) {
			for (String s : security_key_list) {
				hm.put("security_no", s);
				atViewDAO.postSecurityRef(hm);
			}
		}

		return 1;
	}

	@Transactional
	public int deleteVnfd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> vnfd_result = atViewDAO.getVnfdOne(hm);

		String version_no = vnfd_result.get("version_no");
		String vnfd_no = vnfd_result.get("vnfd_no");
		String vdu_ref_no = vnfd_result.get("vdu_ref_no");
		String vld_ref_no = vnfd_result.get("vld_ref_no");
		String cp_ref_no = vnfd_result.get("cp_ref_no");
		String life_ref_no = vnfd_result.get("life_ref_no");
		String dependency_ref_no = vnfd_result.get("dependency_ref_no");
		String monitoring_no = vnfd_result.get("monitoring_no");
		String flavour_ref_no = vnfd_result.get("flavour_ref_no");
		String policy_ref_no = vnfd_result.get("policy_ref_no");
		String security_ref_no = vnfd_result.get("security_ref_no");

		hm.put("cp_ref_no", cp_ref_no);
		hm.put("vnfd_no", vnfd_no);
		hm.put("life_ref_no", life_ref_no);
		hm.put("mnt_ref_no", monitoring_no);
		hm.put("flavour_ref_no", flavour_ref_no);
		hm.put("policy_ref_no", policy_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. nfv_vnfd_tab delete
		atViewDAO.deleteVnfd(hm);

		// 2. nfv_version delete
		atViewDAO.deleteVersion(hm);

		HashMap<String, Object> refTableMap = null;
		// 3. nfv_function_ref delete
		// 3.1 vdu_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vdu_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3.2 vld_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vld_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3.3 dependency_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", dependency_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 4. nfv_cp_ref delete
		atViewDAO.deleteCpRef(hm);

		// 5. nfv_lifecycle_ref delete
		atViewDAO.deleteLifecycleRef(hm);

		// 6. nfv_monitoring_ref delete
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("mnt_ref_no", monitoring_no);
		atViewDAO.deleteMntRef(refTableMap);

		// 7. nfv_flavour_ref delete
		atViewDAO.deleteFlavourRef(hm);

		// 8. nfv_policy_ref delete
		atViewDAO.deletePolicyRef(hm);

		// 9. nfv_security_ref delete
		atViewDAO.deleteSecurityRef(hm);

		// 10. tmenu delete
		hm.put("func_ref_no", version_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

	// nsd
	@Transactional
	public String getNsd(HashMap<String, Object> hm) {
		List<LinkedHashMap<String, String>> resultList = atViewDAO.getNsd(hm);

		if (resultList == null || resultList.size() < 1) {
			Element rootElement = new Element("nsd");
			return CommonXmlUtil.getElementToString(rootElement);
		}

		List<String> nsd_id = new ArrayList<String>();
		List<String> nsd_vendor = new ArrayList<String>();
		List<String> nsd_version = new ArrayList<String>();
		List<String> version_no = new ArrayList<String>();
		List<String> vnfd_no = new ArrayList<String>();
		List<String> vnfd_item = new ArrayList<String>();
		List<String> vnffgd_no = new ArrayList<String>();
		List<String> vnffgd_item = new ArrayList<String>();

		List<String> vld_no = new ArrayList<String>();
		List<String> vld_item = new ArrayList<String>();
		List<String> nsd_life_no = new ArrayList<String>();
		List<String> nsd_life_item = new ArrayList<String>();
		List<String> nsd_vnfdep_no = new ArrayList<String>();
		List<String> nsd_vnfdep_item = new ArrayList<String>();
		List<String> nsd_mnt_no = new ArrayList<String>();
		List<String> nsd_mnt_item = new ArrayList<String>();

		List<String> sdf_no = new ArrayList<String>(); // sdf_no
		List<String> sdf_id = new ArrayList<String>();
		List<String> sdf_id_temp = new ArrayList<String>();
		List<String> sdf_flavour_key = new ArrayList<String>();
		List<String> vnf_ref_id = new ArrayList<String>();
		List<String> vnf_flavour_id = new ArrayList<String>();
		List<String> rddc_model = new ArrayList<String>();

		List<String> affinity = new ArrayList<String>();
		List<String> vnf_cap = new ArrayList<String>();
		List<String> vnf_instance_cnt = new ArrayList<String>();
		List<String> nsd_policy_no = new ArrayList<String>();
		List<String> nsd_policy_item = new ArrayList<String>();

		List<String> nsd_cp_no = new ArrayList<String>();
		List<String> nsd_cp_id = new ArrayList<String>();
		List<String> nsd_cp_vlr = new ArrayList<String>();
		List<String> nsd_cp_type = new ArrayList<String>();
		List<String> pnfd_no = new ArrayList<String>();
		List<String> pnfd_item = new ArrayList<String>();
		List<String> nsd_security_no = new ArrayList<String>();
		List<String> nsd_security_item = new ArrayList<String>();

		for (LinkedHashMap<String, String> map : resultList) {
			nsd_id.add(map.get("nsd_id"));
			nsd_vendor.add(map.get("nsd_vendor"));
			nsd_version.add(map.get("nsd_version"));

			version_no.add(map.get("version_no"));
			vnfd_no.add(map.get("vnfd_no"));
			vnfd_item.add(map.get("vnfd_item"));
			vnffgd_no.add(map.get("vnffgd_no"));
			vnffgd_item.add(map.get("vnffgd_item"));

			vld_no.add(map.get("vld_no"));
			vld_item.add(map.get("vld_item"));
			nsd_life_no.add(map.get("nsd_life_no"));
			nsd_life_item.add(map.get("nsd_life_item"));
			nsd_vnfdep_no.add(map.get("nsd_vnfdep_no"));
			nsd_vnfdep_item.add(map.get("nsd_vnfdep_item"));
			nsd_mnt_no.add(map.get("nsd_mnt_no"));
			nsd_mnt_item.add(map.get("nsd_mnt_item"));

			sdf_no.add(map.get("sdf_no"));
			sdf_id.add(map.get("sdf_id"));
			sdf_flavour_key.add(map.get("sdf_flavour_key"));
			vnf_ref_id.add(map.get("vnf_ref_id"));
			vnf_flavour_id.add(map.get("vnf_flavour_id"));
			rddc_model.add(map.get("rddc_model"));

			affinity.add(map.get("affinity"));
			vnf_cap.add(map.get("vnf_cap"));
			vnf_instance_cnt.add(map.get("vnf_instance_cnt"));
			nsd_policy_no.add(map.get("nsd_policy_no"));
			nsd_policy_item.add(map.get("nsd_policy_item"));

			nsd_cp_no.add(map.get("nsd_cp_no"));
			nsd_cp_id.add(map.get("nsd_cp_id"));
			nsd_cp_vlr.add(map.get("nsd_cp_vlr"));
			nsd_cp_type.add(map.get("nsd_cp_type"));
			pnfd_no.add(map.get("pnfd_no"));
			pnfd_item.add(map.get("pnfd_item"));
			nsd_security_no.add(map.get("nsd_security_no"));
			nsd_security_item.add(map.get("nsd_security_item"));

		}

		Element rootElement = new Element("nsd");

		rootElement.addContent(new Element("id").addContent(nsd_id.get(0)));
		rootElement.addContent(new Element("vendor").addContent(nsd_vendor.get(0)));
		rootElement.addContent(new Element("version").addContent(nsd_version.get(0)));

		Element vnfd_item_el = new Element("vnfd");

		int i = 0;
		for (String s : vnfd_item) {
			if (!"NULL".equals(s)) {
				Element vnfd_item_info_el = new Element("vnfd_info");
				vnfd_item_info_el.addContent(new Element("key").addContent(vnfd_no.get(i)));
				vnfd_item_info_el.addContent(new Element("ref").addContent(s));
				vnfd_item_el.addContent(vnfd_item_info_el);
			}
			i++;
		}

		rootElement.addContent(vnfd_item_el);

		Element vnffgd_item_el = new Element("vnffgd");

		i = 0;
		for (String s : vnffgd_item) {
			if (!"NULL".equals(s)) {
				Element vnffgd_item_info_el = new Element("vnffgd_info");
				vnffgd_item_info_el.addContent(new Element("key").addContent(vnffgd_no.get(i)));
				vnffgd_item_info_el.addContent(new Element("ref").addContent(s));
				vnffgd_item_el.addContent(vnffgd_item_info_el);
			}
			i++;
		}

		rootElement.addContent(vnffgd_item_el);

		Element vld_item_el = new Element("vld");

		i = 0;
		for (String s : vld_item) {
			if (!"NULL".equals(s)) {
				Element vld_item_info_el = new Element("vld_info");
				vld_item_info_el.addContent(new Element("key").addContent(vld_no.get(i)));
				vld_item_info_el.addContent(new Element("ref").addContent(s));
				vld_item_el.addContent(vld_item_info_el);
			}
			i++;
		}

		rootElement.addContent(vld_item_el);

		Element nsd_life_item_el = new Element("lifecycle_event");

		i = 0;
		for (String s : nsd_life_item) {
			if (!"NULL".equals(s)) {
				Element nsd_life_item_info_el = new Element("lifecycle_event_info");
				nsd_life_item_info_el.addContent(new Element("key").addContent(nsd_life_no.get(i)));
				nsd_life_item_info_el.addContent(new Element("item").addContent(s));
				nsd_life_item_el.addContent(nsd_life_item_info_el);
			}
			i++;
		}

		rootElement.addContent(nsd_life_item_el);

		Element nsd_vnfdep_item_el = new Element("vnf_dependency");

		i = 0;
		for (String s : nsd_vnfdep_item) {
			if (!"NULL".equals(s)) {
				Element nsd_vnfdep_item_info_el = new Element("vnf_dependency_info");
				nsd_vnfdep_item_info_el.addContent(new Element("key").addContent(nsd_vnfdep_no.get(i)));
				nsd_vnfdep_item_info_el.addContent(new Element("item").addContent(s));
				nsd_vnfdep_item_el.addContent(nsd_vnfdep_item_info_el);
			}
			i++;
		}

		rootElement.addContent(nsd_vnfdep_item_el);

		Element nsd_mnt_item_el = new Element("monitoring_parameter");

		i = 0;
		for (String s : nsd_mnt_item) {
			if (!"NULL".equals(s)) {
				Element nsd_mnt_item_info_el = new Element("monitoring_parameter_info");
				nsd_mnt_item_info_el.addContent(new Element("key").addContent(nsd_mnt_no.get(i)));
				nsd_mnt_item_info_el.addContent(new Element("item").addContent(s));
				nsd_mnt_item_el.addContent(nsd_mnt_item_info_el);
			}
			i++;
		}

		rootElement.addContent(nsd_mnt_item_el);

		Element service_deployment_flavour_el = new Element("service_deployment_flavour");

		i = 0;
		for (String s : sdf_id) {
			if (!"NULL".equals(s) && !sdf_id_temp.contains(s)) {
				Element service_deployment_flavour_info_el = new Element("service_deployment_flavour_info");
				service_deployment_flavour_info_el.addContent(new Element("key").addContent(sdf_no.get(i)));
				service_deployment_flavour_info_el.addContent(new Element("id").addContent(s));
				service_deployment_flavour_info_el
						.addContent(new Element("flavour_key").addContent(sdf_flavour_key.get(i)));

				Element constituent_vnf_el = new Element("constituent_vnf");

				int k = 0;
				for (String o : vnf_ref_id) {
					if (!"NULL".equals(o) && s.equals(sdf_id.get(k))) {
						Element constituent_vnf_info_el = new Element("constituent_vnf_info");
						constituent_vnf_info_el.addContent(new Element("vnf_reference").addContent(o));
						constituent_vnf_info_el
								.addContent(new Element("vnf_flavour_id_reference").addContent(vnf_flavour_id.get(k)));
						constituent_vnf_info_el
								.addContent(new Element("redundancy_model").addContent(rddc_model.get(k)));
						constituent_vnf_info_el.addContent(new Element("affinity").addContent(affinity.get(k)));
						constituent_vnf_info_el.addContent(new Element("capability").addContent(vnf_cap.get(k)));
						constituent_vnf_info_el
								.addContent(new Element("number_of_instances").addContent(vnf_instance_cnt.get(k)));

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

		i = 0;
		for (String s : nsd_policy_item) {
			if (!"NULL".equals(s)) {
				Element auto_scale_policy_info_el = new Element("auto_scale_policy_info");
				auto_scale_policy_info_el.addContent(new Element("key").addContent(nsd_policy_no.get(i)));
				auto_scale_policy_info_el.addContent(new Element("item").addContent(s));
				auto_scale_policy_el.addContent(auto_scale_policy_info_el);
			}
			i++;
		}

		rootElement.addContent(auto_scale_policy_el);

		Element connection_point_el = new Element("connection_point");

		int q = 0;
		for (String s : nsd_cp_id) {
			if (!"NULL".equals(s)) {
				Element connection_point_info_el = new Element("connection_point_info");
				connection_point_info_el.addContent(new Element("key").addContent(nsd_cp_no.get(q)));
				connection_point_info_el.addContent(new Element("id").addContent(s));
				connection_point_info_el.addContent(new Element("type").addContent(nsd_cp_type.get(q)));

				connection_point_el.addContent(connection_point_info_el);
			}
			q++;
		}

		rootElement.addContent(connection_point_el);

		Element pnfd_el = new Element("pnfd");

		i = 0;
		for (String s : pnfd_item) {
			if (!"NULL".equals(s)) {
				Element pnfd_info_el = new Element("pnfd_info");
				pnfd_info_el.addContent(new Element("key").addContent(pnfd_no.get(i)));
				pnfd_info_el.addContent(new Element("ref").addContent(s));
				pnfd_el.addContent(pnfd_info_el);
			}
			i++;
		}

		rootElement.addContent(pnfd_el);

		Element nsd_security_el = new Element("nsd_security");

		i = 0;
		for (String s : nsd_security_item) {
			if (!"NULL".equals(s)) {
				Element nsd_security_info_el = new Element("nsd_security_info");
				nsd_security_info_el.addContent(new Element("key").addContent(nsd_security_no.get(i)));
				nsd_security_info_el.addContent(new Element("item").addContent(s));
				nsd_security_el.addContent(nsd_security_info_el);
			}
			i++;
		}
		rootElement.addContent(nsd_security_el);

		return CommonXmlUtil.getElementToString(rootElement);
	}

	@Transactional
	public int postNsd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nsd_result = atViewDAO.getNsdOne(hm);

		if (nsd_result != null && nsd_result.size() > 0) {
			return -1;
		}

		// table_seq
		String nsd_seq = nfvCommonDAO.getSeq("34");
		String version_seq = nfvCommonDAO.getSeq("10");
		String vnfd_ref_seq = nfvCommonDAO.getSeq("11");
		String vnffgd_ref_seq = nfvCommonDAO.getSeq("12");
		String vld_ref_seq = nfvCommonDAO.getSeq("13");
		String pnfd_ref_seq = nfvCommonDAO.getSeq("14");
		String life_ref_seq = nfvCommonDAO.getSeq("44");
		String vnfdep_ref_seq = nfvCommonDAO.getSeq("46");
		String mnt_ref_seq = nfvCommonDAO.getSeq("42");
		String sdf_ref_seq = nfvCommonDAO.getSeq("48");
		String policy_ref_seq = nfvCommonDAO.getSeq("84");
		String cp_ref_seq = nfvCommonDAO.getSeq("08");
		String security_ref_seq = nfvCommonDAO.getSeq("02");

		logger.info("--------------------------------------------nsd_seq: " + nsd_seq + ", version_seq: " + version_seq
				+ ", vnfd_ref_seq: " + vnfd_ref_seq);
		logger.info("--------------------------------------------vnffgd_ref_seq: " + vnffgd_ref_seq + ", vld_ref_seq: "
				+ vld_ref_seq + ", pnfd_ref_seq: " + pnfd_ref_seq);
		logger.info("--------------------------------------------life_ref_seq: " + life_ref_seq + ", vnfdep_ref_seq: "
				+ vnfdep_ref_seq + ", mnt_ref_seq: " + mnt_ref_seq);
		logger.info("--------------------------------------------sdf_ref_seq: " + sdf_ref_seq + ", policy_ref_seq: "
				+ policy_ref_seq + ", cp_ref_seq: " + cp_ref_seq + ", security_ref_seq: " + security_ref_seq);

		hm.put("nsd_no", nsd_seq);
		hm.put("version_no", version_seq);
		hm.put("vnfd_ref_no", vnfd_ref_seq);
		hm.put("vnffgd_ref_no", vnffgd_ref_seq);
		hm.put("vld_ref_no", vld_ref_seq);
		hm.put("pnfd_ref_no", pnfd_ref_seq);
		hm.put("life_ref_no", life_ref_seq);
		hm.put("vnf_ref_no", vnfdep_ref_seq);
		hm.put("monitoring_ref_no", mnt_ref_seq);
		hm.put("sdf_ref_no", sdf_ref_seq);
		hm.put("policy_ref_no", policy_ref_seq);
		hm.put("cp_ref_no", cp_ref_seq);
		hm.put("sec_ref_no", security_ref_seq);

		// 1. nfv_nsd_tab insert
		atViewDAO.postNsd(hm);

		// 2. nfv_version insert
		atViewDAO.postVersion(hm);

		HashMap<String, Object> refTableMap = null;

		// 3. nfv_function_ref insert(vdu_ref_no, vld_ref_no, dependency_ref_no)
		// 3.1 vnfd_ref_no
		String[] vnfd_key_list = (String[]) hm.get("vnfd_key_list");
		if (vnfd_key_list != null) {
			for (String s : vnfd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vnfd_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);

			}
		}
		// 3.2 vnffgd_ref_no
		String[] vnffgd_key_list = (String[]) hm.get("vnffgd_key_list");
		if (vnffgd_key_list != null) {
			for (String s : vnffgd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vnffgd_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 3.3 vld_ref_no
		String[] vld_key_list = (String[]) hm.get("vld_key_list");
		if (vld_key_list != null) {
			for (String s : vld_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vld_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 3.4 pnfd_ref_no
		String[] pnfd_key_list = (String[]) hm.get("pnfd_key_list");
		if (pnfd_key_list != null) {
			for (String s : pnfd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", pnfd_ref_seq);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}

		// 4. nfv_lifecycle_ref insert
		String[] lifecycle_key_list = (String[]) hm.get("lifecycle_key_list");
		if (lifecycle_key_list != null) {
			for (String s : lifecycle_key_list) {
				hm.put("life_no", s);
				atViewDAO.postLifecycleRef(hm);
			}
		}

		// 5. nfv_vnfdep_ref insert
		String[] vnfdep_key_list = (String[]) hm.get("vnfdep_key_list");
		if (vnfdep_key_list != null) {
			for (String s : vnfdep_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("vnfdep_ref_no", vnfdep_ref_seq);
				refTableMap.put("vnfdep_no", s);
				atViewDAO.postVnfdepRef(refTableMap);
			}
		}

		// 6. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		if (mnt_key_list != null) {
			for (String s : mnt_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("mnt_ref_no", mnt_ref_seq);
				refTableMap.put("mnt_no", s);
				atViewDAO.postMntRef(refTableMap);
			}
		}

		// 7. nfv_sdf_ref insert
		String[] sdf_key_list = (String[]) hm.get("sdf_key_list");
		if (sdf_key_list != null) {
			for (String s : sdf_key_list) {
				hm.put("sdf_no", s);
				atViewDAO.postSdfRef(hm);
			}
		}

		// 8. nfv_policy_ref insert
		String[] policy_key_list = (String[]) hm.get("policy_key_list");
		if (policy_key_list != null) {
			for (String s : policy_key_list) {
				hm.put("policy_no", s);
				atViewDAO.postPolicyRef(hm);
			}
		}

		// 9. nfv_cp_ref insert
		String[] cp_key_list = (String[]) hm.get("cp_key_list");
		if (cp_key_list != null) {
			for (String s : cp_key_list) {
				hm.put("cp_no", s);
				atViewDAO.postCpRef(hm);
			}
		}

		// 10. nfv_security_ref insert
		String[] security_key_list = (String[]) hm.get("security_key_list");
		if (security_key_list != null) {
			for (String s : security_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("security_ref_no", security_ref_seq);
				refTableMap.put("security_no", s);
				atViewDAO.postSecurityRef(refTableMap);
			}
		}

		// 11. tmenu insert
		hm.put("tmenu_ref_no", nfvCommonDAO.getTmenuRefNo("NSD"));
		hm.put("func_ref_no", version_seq);
		atViewDAO.postTmenuRef(hm);

		return 1;
	}

	@Transactional
	public int putNsd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nsd_result = atViewDAO.getNsdOne(hm);

		String version_no = nsd_result.get("version_no");
		String nsd_no = nsd_result.get("nsd_no");
		String vnfd_ref_no = nsd_result.get("vnfd_ref_no");
		String vnffgd_ref_no = nsd_result.get("vnffgd_ref_no");
		String vld_ref_no = nsd_result.get("vld_ref_no");
		String life_ref_no = nsd_result.get("life_ref_no");
		String vnfdep_ref_no = nsd_result.get("vnf_ref_no");
		String mnt_ref_no = nsd_result.get("monitoring_ref_no");
		String sdf_ref_no = nsd_result.get("sdf_ref_no");
		String policy_ref_no = nsd_result.get("policy_ref_no");
		String cp_ref_no = nsd_result.get("cp_ref_no");
		String pnfd_ref_no = nsd_result.get("pnfd_ref_no");
		String security_ref_no = nsd_result.get("sec_ref_no");

		hm.put("version_no", version_no);
		hm.put("nsd_no", nsd_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("vnfd_ref_no", vnfd_ref_no);
		hm.put("vnffgd_ref_no", vnffgd_ref_no);
		hm.put("vld_ref_no", vld_ref_no);
		hm.put("pnfd_ref_no", pnfd_ref_no);
		hm.put("life_ref_no", life_ref_no);
		hm.put("vnfdep_ref_no", vnfdep_ref_no);
		hm.put("mnt_ref_no", mnt_ref_no);
		hm.put("sdf_ref_no", sdf_ref_no);
		hm.put("policy_ref_no", policy_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. nfv_version_tab update
		atViewDAO.putVersion(hm);

		HashMap<String, Object> refTableMap = null;
		// 2. nfv_function_ref delete
		// 2.1 vnfd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vnfd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 2.2 vnffgd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vnffgd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 2.3 vld_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vld_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 2.4 pnfd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", pnfd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3. nfv_lifecycle_ref delete
		atViewDAO.deleteLifecycleRef(hm);

		// 4. nfv_vnfdep_ref delete
		atViewDAO.deleteVnfdepRef(hm);

		// 5. nfv_monitoring_ref delete
		atViewDAO.deleteMntRef(hm);

		// 6. nfv_sdf_ref delete
		atViewDAO.deleteSdfRef(hm);

		// 7. nfv_policy_ref delete
		atViewDAO.deletePolicyRef(hm);

		// 8. nfv_cp_ref delete
		atViewDAO.deleteCpRef(hm);

		// 9. nfv_security_ref delete
		atViewDAO.deleteSecurityRef(hm);

		// 10. nfv_function_ref insert(vdu_ref_no, vld_ref_no, dependency_ref_no)
		// 10.1 vnfd_ref_no
		String[] vnfd_key_list = (String[]) hm.get("vnfd_key_list");
		if (vnfd_key_list != null) {
			for (String s : vnfd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vnfd_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);

			}
		}
		// 10.2 vnffgd_ref_no
		String[] vnffgd_key_list = (String[]) hm.get("vnffgd_key_list");
		if (vnffgd_key_list != null) {
			for (String s : vnffgd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vnffgd_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 10.3 vld_ref_no
		String[] vld_key_list = (String[]) hm.get("vld_key_list");
		if (vld_key_list != null) {
			for (String s : vld_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", vld_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}
		// 10.4 pnfd_ref_no
		String[] pnfd_key_list = (String[]) hm.get("pnfd_key_list");
		if (pnfd_key_list != null) {
			for (String s : pnfd_key_list) {
				refTableMap = new HashMap<String, Object>();
				refTableMap.put("func_ref_no", pnfd_ref_no);
				refTableMap.put("func_no", s);
				atViewDAO.postFuncRef(refTableMap);
			}
		}

		// 11. nfv_lifecycle_ref insert
		String[] lifecycle_key_list = (String[]) hm.get("lifecycle_key_list");
		if (lifecycle_key_list != null) {
			for (String s : lifecycle_key_list) {
				hm.put("life_no", s);
				atViewDAO.postLifecycleRef(hm);
			}
		}

		// 12. nfv_vnfdep_ref insert
		String[] vnfdep_key_list = (String[]) hm.get("vnfdep_key_list");
		if (vnfdep_key_list != null) {
			for (String s : vnfdep_key_list) {
				hm.put("vnfdep_no", s);
				atViewDAO.postVnfdepRef(hm);
			}
		}

		// 13. nfv_monitoring_ref insert
		String[] mnt_key_list = (String[]) hm.get("mnt_key_list");
		if (mnt_key_list != null) {
			for (String s : mnt_key_list) {
				hm.put("mnt_no", s);
				atViewDAO.postMntRef(hm);
			}
		}

		// 14. nfv_sdf_ref insert
		String[] sdf_key_list = (String[]) hm.get("sdf_key_list");
		if (sdf_key_list != null) {
			for (String s : sdf_key_list) {
				hm.put("sdf_no", s);
				atViewDAO.postSdfRef(hm);
			}
		}

		// 15. nfv_policy_ref insert
		String[] policy_key_list = (String[]) hm.get("policy_key_list");
		if (policy_key_list != null) {
			for (String s : policy_key_list) {
				hm.put("policy_no", s);
				atViewDAO.postPolicyRef(hm);
			}
		}

		// 16. nfv_cp_ref insert
		String[] cp_key_list = (String[]) hm.get("cp_key_list");
		if (cp_key_list != null) {
			for (String s : cp_key_list) {
				hm.put("cp_no", s);
				atViewDAO.postCpRef(hm);
			}
		}

		// 17. nfv_security_ref insert
		String[] security_key_list = (String[]) hm.get("security_key_list");
		if (security_key_list != null) {
			for (String s : security_key_list) {
				hm.put("security_no", s);
				atViewDAO.postSecurityRef(hm);
			}
		}

		return 1;
	}

	@Transactional
	public int deleteNsd(HashMap<String, Object> hm) {
		LinkedHashMap<String, String> nsd_result = atViewDAO.getNsdOne(hm);

		String version_no = nsd_result.get("version_no");
		String nsd_no = nsd_result.get("nsd_no");
		String vnfd_ref_no = nsd_result.get("vnfd_ref_no");
		String vnffgd_ref_no = nsd_result.get("vnffgd_ref_no");
		String vld_ref_no = nsd_result.get("vld_ref_no");
		String life_ref_no = nsd_result.get("life_ref_no");
		String vnfdep_ref_no = nsd_result.get("vnf_ref_no");
		String mnt_ref_no = nsd_result.get("monitoring_ref_no");
		String sdf_ref_no = nsd_result.get("sdf_ref_no");
		String policy_ref_no = nsd_result.get("policy_ref_no");
		String cp_ref_no = nsd_result.get("cp_ref_no");
		String pnfd_ref_no = nsd_result.get("pnfd_ref_no");
		String security_ref_no = nsd_result.get("sec_ref_no");

		hm.put("version_no", version_no);
		hm.put("nsd_no", nsd_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("vnfd_ref_no", vnfd_ref_no);
		hm.put("vnffgd_ref_no", vnffgd_ref_no);
		hm.put("vld_ref_no", vld_ref_no);
		hm.put("pnfd_ref_no", pnfd_ref_no);
		hm.put("life_ref_no", life_ref_no);
		hm.put("vnfdep_ref_no", vnfdep_ref_no);
		hm.put("mnt_ref_no", mnt_ref_no);
		hm.put("sdf_ref_no", sdf_ref_no);
		hm.put("policy_ref_no", policy_ref_no);
		hm.put("cp_ref_no", cp_ref_no);
		hm.put("security_ref_no", security_ref_no);

		// 1. nfv_nsd_tab delete
		atViewDAO.deleteNsd(hm);

		// 2. nfv_version delete
		atViewDAO.deleteVersion(hm);

		HashMap<String, Object> refTableMap = null;
		// 3. nfv_function_ref delete
		// 3.1 vnfd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vnfd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3.2 vnffgd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vnffgd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3.3 vld_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", vld_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 3.4 pnfd_ref_no
		refTableMap = new HashMap<String, Object>();
		refTableMap.put("func_ref_no", pnfd_ref_no);
		atViewDAO.deleteFuncRef(refTableMap);

		// 4. nfv_lifecycle_ref delete
		atViewDAO.deleteLifecycleRef(hm);

		// 5. nfv_vnfdep_ref delete
		atViewDAO.deleteVnfdepRef(hm);

		// 6. nfv_monitoring_ref delete
		atViewDAO.deleteMntRef(hm);

		// 7. nfv_sdf_ref delete
		atViewDAO.deleteSdfRef(hm);

		// 8. nfv_policy_ref delete
		atViewDAO.deletePolicyRef(hm);

		// 9. nfv_cp_ref delete
		atViewDAO.deleteCpRef(hm);

		// 10. nfv_security_ref delete
		atViewDAO.deleteSecurityRef(hm);

		// 11. tmenu delete
		hm.put("func_ref_no", version_no);
		atViewDAO.deleteTmenuRef(hm);

		return 1;
	}

}
