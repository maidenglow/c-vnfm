package com.eluon.vepc.mano.vo;

/**
 * Images VO (ImagesVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ImagesVO.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
public class ImageVO extends BaseVO {
	private String url;
	private String name;
	private boolean isPublic;
	private String containerFormat;
	private String diskFormat;
	private String filePath;
	private int minDisk;

	public ImageVO() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getContainerFormat() {
		return containerFormat;
	}

	public void setContainerFormat(String containerFormat) {
		this.containerFormat = containerFormat;
	}

	public String getDiskFormat() {
		return diskFormat;
	}

	public void setDiskFormat(String diskFormat) {
		this.diskFormat = diskFormat;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getMinDisk() {
		return minDisk;
	}

	public void setMinDisk(int minDisk) {
		this.minDisk = minDisk;
	}

}
