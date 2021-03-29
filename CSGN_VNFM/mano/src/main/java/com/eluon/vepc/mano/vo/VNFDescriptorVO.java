package com.eluon.vepc.mano.vo;

import java.util.Date;


/**
 * VNFDescriptor VO
 *
 */
public class VNFDescriptorVO extends BaseVO {
	
	
	private static final long serialVersionUID = 1L;
	public String vnfd_id;
	public String vnfd_name;
	
	public String image_id;
	public String old_image_id;
	
	public String zone;
	public Date created;
	
	public VNFDescriptorVO() {
		super();
	}

}
