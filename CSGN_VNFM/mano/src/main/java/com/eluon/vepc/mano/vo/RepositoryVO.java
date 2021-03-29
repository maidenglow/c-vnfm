package com.eluon.vepc.mano.vo;

import java.util.List;


/**
 * VNFDescriptor VO
 *
 */
public class RepositoryVO extends BaseVO {
	
	
	private static final long serialVersionUID = 1L;
	public String id;
	public String title;
	public String parent;
	public String type;
	public List info; // vnfd_id_vnfc_name;
	public RepositoryVO() {
		super();
	}

}
