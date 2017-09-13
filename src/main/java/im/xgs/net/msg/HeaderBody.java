package im.xgs.net.msg;
/**
 * 请求报文实体
 * @author TianW
 *
 */
public class HeaderBody {
	private int len;
	private String client;
	private String version;
	private String uuid;
	private short cmd;
	private short opera;
	private String msg;
	
	public HeaderBody(int len, String client, String version, String uuid, short cmd, short opera, String msg){
		this.len = len;
		this.client = client;
		this.version = version;
		this.uuid = uuid;
		this.cmd = cmd;
		this.opera = opera;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public short getCmd() {
		return cmd;
	}
	public void setCmd(short cmd) {
		this.cmd = cmd;
	}
	public short getOpera() {
		return opera;
	}
	public void setOpera(short opera) {
		this.opera = opera;
	}
	

}
