package com.eluon.vepc.mano.vo;


/**
 * OpenstackService VO (OpenstackServiceVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OpenstackServiceVO.java,v 1.0 2015/03/31 00:00:00 SimByungChul Express $
 */
public class OpenstackServiceVO extends BaseVO {
	private String id;
	private String enabled;
	private String type;
	private String name;
	private String description;
	private String host;
	
	public OpenstackServiceVO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
