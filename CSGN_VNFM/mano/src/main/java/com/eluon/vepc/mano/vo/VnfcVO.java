package com.eluon.vepc.mano.vo;

import java.util.Date;


/**
 * VNFDescriptor VO
 *
 */
public class VnfcVO extends BaseVO {
	
	
	private static final long serialVersionUID = 1L;
	
	public String vnfd_id;
	public String vnfc_id;
	public String vnfc_id_ref;
	public String vnfc_name; // vnfd_id_vnfc_name;
	public String vnfc_name_ref;
	public String vnfc_real_name; //vnfc_name
	public String status;
//	public String high_availability; //active, standby
	public String port_id;
	public String network;
	public String num_instances;
	public String max_instances;
	public String priority;
	public String scaling_flag; //0,1
	public String in_policy;
	public String out_policy;
	public long scale_in_time;
	public long scale_out_time;
	public long guard_time;
	public String break_policy;
	
	public String flavor_id;
	public String flavor_name; // vnfd_id_vnfc_name
	public String flavor_real_name; //vnfc_name
	public String flavor_vmem;
	public String flavor_vcpu;
	public String flavor_vdisk;
	public String affinity;
	public String snapshot_id;
	public Date create_at;
	
	public VnfcVO() {
		super();
	}

}
