package im.xgs.net.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysUser<M extends BaseSysUser<M>> extends Model<M> implements IBean {

	public void setUserId(java.lang.String userId) {
		set("user_id", userId);
	}

	public java.lang.String getUserId() {
		return get("user_id");
	}

	public void setNickname(java.lang.String nickname) {
		set("nickname", nickname);
	}

	public java.lang.String getNickname() {
		return get("nickname");
	}

	public void setHeader(java.lang.String header) {
		set("header", header);
	}

	public java.lang.String getHeader() {
		return get("header");
	}

	public void setLoginAccount(java.lang.String loginAccount) {
		set("login_account", loginAccount);
	}

	public java.lang.String getLoginAccount() {
		return get("login_account");
	}

	public void setLoginPwd(java.lang.String loginPwd) {
		set("login_pwd", loginPwd);
	}

	public java.lang.String getLoginPwd() {
		return get("login_pwd");
	}

	public void setLoginSalt(java.lang.String loginSalt) {
		set("login_salt", loginSalt);
	}

	public java.lang.String getLoginSalt() {
		return get("login_salt");
	}

	public void setSign(java.lang.String sign) {
		set("sign", sign);
	}

	public java.lang.String getSign() {
		return get("sign");
	}

	public void setFullPinyin(java.lang.String fullPinyin) {
		set("full_pinyin", fullPinyin);
	}

	public java.lang.String getFullPinyin() {
		return get("full_pinyin");
	}

	public void setBriefPinyin(java.lang.String briefPinyin) {
		set("brief_pinyin", briefPinyin);
	}

	public java.lang.String getBriefPinyin() {
		return get("brief_pinyin");
	}

}
