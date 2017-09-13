package im.xgs.net.channel;

public enum ChannelType {
	WEB(0,"WEB"),MOBILE(1,"PC"),PC(2,"MOBILE");
	
	private int i;
	private String name;
	
	private ChannelType(int i,String name){
		this.i = i;
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getCode(){
		return this.i;
	}
	
	public static ChannelType get(int code){
		switch (code) {
		case 0:
			return ChannelType.WEB;
		case 1:
			return ChannelType.PC;
		case 2:
			return ChannelType.MOBILE;
		default:
			return ChannelType.WEB;
		}
	}
}
