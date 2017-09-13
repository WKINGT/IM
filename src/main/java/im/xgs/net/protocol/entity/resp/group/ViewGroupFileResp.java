package im.xgs.net.protocol.entity.resp.group;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ViewGroupFileResp {
	private int code;
	private Page<Record> groupFiles;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Page<Record> getGroupFiles() {
		return groupFiles;
	}

	public void setGroupFiles(Page<Record> groupFiles) {
		this.groupFiles = groupFiles;
	}
}
