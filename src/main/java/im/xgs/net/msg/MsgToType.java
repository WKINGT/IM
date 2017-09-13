package im.xgs.net.msg;



public enum MsgToType {
	TOUSER,TOGROUP,TOSYSTEM;
	public static MsgToType get(int code){
		switch (code) {
		case 0:
			return MsgToType.TOUSER;
		case 1:
			return MsgToType.TOGROUP;
		case 2:
			return MsgToType.TOSYSTEM;
		default:
			return MsgToType.TOUSER;
		}
	}
}
