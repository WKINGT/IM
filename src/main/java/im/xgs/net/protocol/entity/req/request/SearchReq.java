package im.xgs.net.protocol.entity.req.request;


/**
 * 查找好友或群组的请求实体
 * @author TianW
 *
 */
public class SearchReq{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
//	public static void main(String[] args){
//		SearchReq sr = new SearchReq();
//		sr.setName(null);
//		System.out.println(JsonKit.toJson(sr));	
//	}
}