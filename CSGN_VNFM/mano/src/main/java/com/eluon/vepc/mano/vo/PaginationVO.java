package com.eluon.vepc.mano.vo;

/**
 * Paging VO(PaginationVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: PaginationVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class PaginationVO extends BaseVO {
	private int currentPage;
	private int pageSize = 15;
	private int totalCount;
	private int maxPage;
	private int nStartPageList;
	private int nEndPageList;
	private String pageDivideForm;

	public PaginationVO() {
		super();
	}

	public PaginationVO(int totalCount, int currentPage, int pageSize) {
		super();
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.maxPage = pageSize == 0 ? this.totalCount : (totalCount - 1) / pageSize + 1;
		this.currentPage = currentPage > maxPage ? maxPage : currentPage;
		this.nStartPageList = ( ( currentPage - 1 ) / pageSize ) * pageSize + 1;
		this.nEndPageList = nStartPageList + pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public int getNStartPageList() {
		return nStartPageList;
	}

	public int getNEndPageList() {
		return nEndPageList;
	}

	public String getPageDivideForm() {
		return pageDivideForm;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public void setNStartPageList(int startPageList) {
		nStartPageList = startPageList;
	}

	public void setNEndPageList(int endPageList) {
		nEndPageList = endPageList;
	}

	public void setPageDivideForm(String pageDivideForm) {
		this.pageDivideForm = pageDivideForm;
	}

}