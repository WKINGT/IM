package im.xgs.net.protocol.entity.req.login;
public class ReconnectionReq{
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
	}