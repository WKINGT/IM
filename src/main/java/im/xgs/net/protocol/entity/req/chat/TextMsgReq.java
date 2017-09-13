package im.xgs.net.protocol.entity.req.chat;


/**
 * 文本消息请求实体
 * @author TianW
 *
 */
public class TextMsgReq extends MsgReq{
	
	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}