package im.xgs.net.protocol.entity.resp.chat;

/**
 * 文本消息响应实体
 * @author TianW
 *
 */
public class TextMsgResp extends MsgResp {
	
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}