package com.eluon.vepc.mano.vo;


/**
 * Alarm VO (AlarmVO)
 *
 */
public class EventLogVO extends PaginationVO {
	public String idx;
	public String log_id;
	public String name;
	public String descriptor;
	public String create_at;
	
	public EventLogVO() {
		super();
	}
}
