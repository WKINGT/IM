package im.xgs.net.protocol.entity.req.login;


public class LoginReq{
		private String loginAccount;
		private String loginPwd;
		public String getLoginAccount() {
			return loginAccount;
		}
		public void setLoginAccount(String loginAccount) {
			this.loginAccount = loginAccount;
		}
		public String getLoginPwd() {
			return loginPwd;
		}
		public void setLoginPwd(String loginPwd) {
			this.loginPwd = loginPwd;
		}
//		public static void main(String[] args){
//			LoginReq in = new LoginReq();
//			in.setLoginAccount(null);
//			in.setLoginPwd(null);
//			System.out.println(JsonKit.toJson(in));
//		}
	}