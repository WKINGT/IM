package im.xgs.net.protocol.entity.req.updateData;


/**
 * 更改个人签名的请求实体
 * @author TianW
 *
 */
public class UpdateSignReq{
	private String newSign;

	public String getNewSign() {
		return newSign;
	}

	public void setNewSign(String newSign) {
		this.newSign = newSign;
	}
//	public static void main(String[] args){
//		UpdateSignReq up = new UpdateSignReq();
//		up.setNewSign(null);
//		System.out.println(JsonKit.toJson(up));	
//	}
}