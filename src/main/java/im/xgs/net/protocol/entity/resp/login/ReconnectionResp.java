package im.xgs.net.protocol.entity.resp.login;
public class ReconnectionResp{
private int code;
//	private String text;
	private String userId;
	private String client;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
//	public String getText() {
//		return text;
//	}
//	public void setText(String text) {
//		this.text = text;
//	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
}