package com.eluon.vepc.mano.vo;



public class EndpointVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String legacy_endpoint_id;
	public String Interface;
	public String service_id;
	public String service_name;
	public String service_type;
	public String url; 
	public String extra;
	public String enabled;
	public String region_id;
	public EndpointVO() {
		super();
	}
}
