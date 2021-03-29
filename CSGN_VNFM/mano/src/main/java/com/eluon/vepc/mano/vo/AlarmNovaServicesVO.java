package com.eluon.vepc.mano.vo;

import java.sql.Date;



public class AlarmNovaServicesVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String created_at;
	public String updated_at;
	public String deleted_at;
	public int id;
	public String host;
	public String binary;
	public String topic;
	public int report_count;
	public int disabled; 
	public int deleted;
	public String disabled_reason;
	public String last_seen_up;
	public int forced_down;
	public int version;
	
	public AlarmNovaServicesVO() {
		super();
	}
}
