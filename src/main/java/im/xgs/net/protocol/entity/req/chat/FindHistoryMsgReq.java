package im.xgs.net.protocol.entity.req.chat;

import im.xgs.net.msg.MsgToType;

public class FindHistoryMsgReq {
	private String id;
	private MsgToType toType;
	private int pageNumber;
	private int pageSize;
	private String searchYm;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getToType() {
		return toType.ordinal();
	}
	public void setToType(int code) {
		this.toType = MsgToType.get(code);
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
	
//	public static void main(String[] args){
//		FindHistoryReq fh = new FindHistoryReq();
//		fh.setId(null);
//		fh.setToType(1);
//		fh.setPageNumber(1);
//		fh.setPageSize(10);
//		System.out.println(JsonKit.toJson(fh));	
//	}
}
