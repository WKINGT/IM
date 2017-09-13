package im.xgs.net.protocol.entity.req.updateData;


/**
 * 更改密码的请求实体
 * @author TianW
 *
 */
public class ChangePwdReq{
	
	private String oldPwd;
	private String newPwd;

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
//	public static void main(String[] args){
//		ChangePwdReq ch = new ChangePwdReq();
//		ch.setOldPwd(null);
//		ch.setNewPwd(null);
//		System.out.println(JsonKit.toJson(ch));	
//	}
}