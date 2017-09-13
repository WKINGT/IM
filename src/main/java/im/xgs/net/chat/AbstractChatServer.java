package im.xgs.net.chat;

public abstract class AbstractChatServer {


	protected int port;
	
	public AbstractChatServer(int port){
		this.port = port;
	}
	public void run() throws Exception{}
}
