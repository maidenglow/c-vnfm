package com.eluon.vepc.mano.vo;

import java.sql.Date;



public class AlarmNeutronAgentVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String agent_type;
	public String binary;
	public String topic;
	public String host;
	public int admin_state_up; 
	public String created_at;
	public String started_at;
	public String heartbeat_timestamp;
	public String description;
	public String configurations;
	public int load;
	
	public AlarmNeutronAgentVO() {
		super();
	}
}
