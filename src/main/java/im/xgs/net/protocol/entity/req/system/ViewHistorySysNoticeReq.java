package im.xgs.net.protocol.entity.req.system;


public class ViewHistorySysNoticeReq {
	private String userId;
	private int pageNumber;
	private int pageSize;
	private String searchYm;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSearchYm() {
		return searchYm;
	}
	public void setSearchYm(String searchYm) {
		this.searchYm = searchYm;
	}
}
