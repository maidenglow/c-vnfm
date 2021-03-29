package com.eluon.vepc.mano.vo;



public class AlarmDashboardVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String type;
	public String host;
	public String enabled; 
	public String description;
	public String flavor_vdisk;
	public AlarmDashboardVO() {
		super();
	}
}
