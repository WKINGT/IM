package im.xgs.net.protocol.entity.req.group;


/**
 * 获取公告的请求实体
 * @author TianW
 *
 */
public class GetNoticeReq{
	private String groupId;
	private int pageNumber;
	private int pageSize;
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
//	public static void main(String[] args){
//		GetNoticeReq ge = new GetNoticeReq();
//		ge.setGroupId(null);
//		ge.setPageNumber(1);
//		ge.setPageSize(10);
//		System.out.println(JsonKit.toJson(ge));	
//	}
}