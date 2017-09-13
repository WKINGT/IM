package im.xgs.net.protocol.entity.resp.request;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.kit.JsonKit;

public class OnlineStatusResp {
	private int code;
	private List<String> onlineIds;
	
	
	public List<String> getOnlineIds() {
		return onlineIds;
	}

	public void setOnlineIds(List<String> onlineIds) {
		this.onlineIds = onlineIds;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
}
