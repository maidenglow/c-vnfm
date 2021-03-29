package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Veryfy VO (VeryfyVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: VeryfyVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class VeryfyVO extends BaseVO {
	private String id;
	private String vendor;
	private String descriptor_version;
	private String number_of_endpoints;
	private String root_requirement;
	private String leaf_requirement;
	private String qos;
	private String test_access;
	private String connection;
	private String connectivity_type;
	private String vld_security;
	private String security_no;
	private String item;
	private String cp_no;
	private String qos_no;
	private String qos_ref_no;
	private String connection_ref_no;
	private String vld_security_ref_no;
	private String vld_no;
	private String version_no;
	
	
	

	public String getCp_no() {
		return cp_no;
	}

	public void setCp_no(String cp_no) {
		this.cp_no = cp_no;
	}

	public String getQos_ref_no() {
		return qos_ref_no;
	}

	public void setQos_ref_no(String qos_ref_no) {
		this.qos_ref_no = qos_ref_no;
	}

	public String getConnection_ref_no() {
		return connection_ref_no;
	}

	public void setConnection_ref_no(String connection_ref_no) {
		this.connection_ref_no = connection_ref_no;
	}

	public String getVld_security_ref_no() {
		return vld_security_ref_no;
	}

	public void setVld_security_ref_no(String vld_security_ref_no) {
		this.vld_security_ref_no = vld_security_ref_no;
	}

	public String getVld_no() {
		return vld_no;
	}

	public void setVld_no(String vld_no) {
		this.vld_no = vld_no;
	}

	public String getVersion_no() {
		return version_no;
	}

	public void setVersion_no(String version_no) {
		this.version_no = version_no;
	}

	public void setQos_no(String qos_no) {
		this.qos_no = qos_no;
	}

	public String getQos_no() {
		return qos_no;
	}

	public void getQos_no(String qos_no) {
		this.qos_no = qos_no;
	}
	
	public String getSecurity_no() {
		return security_no;
	}
	
	public void setSecurity_no(String security_no) {
		this.security_no = security_no;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public VeryfyVO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDescriptor_version() {
		return descriptor_version;
	}

	public void setDescriptor_version(String descriptor_version) {
		this.descriptor_version = descriptor_version;
	}

	public String getNumber_of_endpoints() {
		return number_of_endpoints;
	}

	public void setNumber_of_endpoints(String number_of_endpoints) {
		this.number_of_endpoints = number_of_endpoints;
	}

	public String getRoot_requirement() {
		return root_requirement;
	}

	public void setRoot_requirement(String root_requirement) {
		this.root_requirement = root_requirement;
	}

	public String getLeaf_requirement() {
		return leaf_requirement;
	}

	public void setLeaf_requirement(String leaf_requirement) {
		this.leaf_requirement = leaf_requirement;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	public String getTest_access() {
		return test_access;
	}

	public void setTest_access(String test_access) {
		this.test_access = test_access;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getConnectivity_type() {
		return connectivity_type;
	}

	public void setConnectivity_type(String connectivity_type) {
		this.connectivity_type = connectivity_type;
	}

	public String getVld_security() {
		return vld_security;
	}

	public void setVld_security(String vld_security) {
		this.vld_security = vld_security;
	}

	

}