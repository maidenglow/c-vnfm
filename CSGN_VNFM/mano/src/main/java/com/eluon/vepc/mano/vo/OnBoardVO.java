package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Disk VO (DiskVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: DiskVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class OnBoardVO extends BaseVO {
	
	
	private String Id;
	private String name;
	private String path;
	private String pathAndName;
	private String contents;
	
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPathAndName() {
		return pathAndName;
	}

	public void setPathAndName(String pathAndName) {
		this.pathAndName = pathAndName;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public OnBoardVO() {
		super();
	}

	

}
