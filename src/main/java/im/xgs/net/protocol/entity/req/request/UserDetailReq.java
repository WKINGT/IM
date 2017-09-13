package im.xgs.net.protocol.entity.req.request;


public class UserDetailReq{
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
//	public static void main(String[] args){
//		UserDetailReq udr = new UserDetailReq();
//		udr.setUserId(null);
//		System.out.println(JsonKit.toJson(udr));	
//	}
}