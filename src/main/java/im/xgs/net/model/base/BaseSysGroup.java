package im.xgs.net.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysGroup<M extends BaseSysGroup<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setGroupName(java.lang.String groupName) {
		set("group_name", groupName);
	}

	public java.lang.String getGroupName() {
		return get("group_name");
	}

	public void setHeader(java.lang.String header) {
		set("header", header);
	}

	public java.lang.String getHeader() {
		return get("header");
	}

	public void setDecription(java.lang.String decription) {
		set("decription", decription);
	}

	public java.lang.String getDecription() {
		return get("decription");
	}

	public void setOwner(java.lang.String owner) {
		set("owner", owner);
	}

	public java.lang.String getOwner() {
		return get("owner");
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

	public void setVerify(java.lang.Boolean verify) {
		set("verify", verify);
	}

	public java.lang.Boolean getVerify() {
		return get("verify");
	}

}
