package im.xgs.net.protocol.entity.resp.group;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 获取公告的响应实体
 * @author TianW
 *
 */
public class GetNoticeResp{
	private int code;
	private Page<Record> notices;

	public Page<Record> getNotices() {
		return notices;
	}

	public void setNotices(Page<Record> notices) {
		this.notices = notices;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}