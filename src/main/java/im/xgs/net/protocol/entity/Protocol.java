package im.xgs.net.protocol.entity;
public class Protocol {
	/*** 网页客户端***/
	public final static String WEB = "0";
	/*** pc客户端***/
	public final static String PC = "1";
	/*** 移动客户端***/
	public final static String MOBILE = "2";
	
	/*** 版本***/
	public final static String VERSION = "010";

	/*** 登入命令 ***/
	public final static short LOGINC = 10;

	public final static class Login {
		/*** 登入，请求和响应是一样的 ***/
		public static final short LOGIN = 10;
		/*** 登出，请求和响应是一样的 ***/
		public static final short LOGOUT = 11;
		/*** 重连，请求和响应是一样的 ***/
		public static final short RECONNECT = 12;
		
	}

	/*** 请求数据命令 ***/
	public final static short REQUESTC = 20;

	public final static class Request {
		/*** 好友列表，请求和响应是一样的 ***/
		public final static short FRIEND_LIST = 10;
		/*** 用户概要，请求和响应是一样的 ***/
		public final static short ORG_INFO = 11;
		/*** 用户详细，请求和响应是一样的 ***/
		public final static short USER_DETAIL = 12;
		/*** 用户在线状态，请求和响应是一样的 ***/
		public final static short ONLINE_STATUS = 13;
		/*** 群组列表，请求和响应是一样的 ***/
		public final static short GROUP_LIST = 20;
		/*** 群组信息，请求和响应是一样的 ***/
		public final static short GROUP_INFO = 21;
		/*** 群组的成员，请求和响应是一样的 ***/
		public final static short GROUP_MEMBERS = 22;
		/*** 好友查找，请求和响应是一样的 ***/
		public final static short SEARCH = 30;
		/*** 查看最近联系人，请求和响应是一样的 ***/
		public final static short FIND_RECENT_CONTACT = 31;
		/*** 保存最近联系人，请求和响应是一样的 ***/
		public final static short SAVE_RECENT_CONTACT = 32;
		/*** 删除最近联系人，请求和响应是一样的 ***/
		public final static short DELETE_RECENT_CONTACT = 33;
		/*** 待办，请求和响应是一样的 ***/
		public final static short BACKLOG = 40;
		/*** 获取离线消息数量，请求和响应是一样的 ***/
		public final static short COUNT_OFFLINE_MSG = 41;
		/*** 获取离线消息，请求和响应是一样的 ***/
		public final static short FIND_OFFLINE_MSG = 42;
		/*** 删除离线消息，请求和响应是一样的 ***/
		public final static short DELETE_OFFLINE_MSG = 43;
		
		public final static short HEART_BEAT = 88;
	}

	/*** 群组操作命令 ***/
	public final static short GROUPC = 30;

	public final static class Group {
		/*** 创建群组，请求和响应是一样的 ***/
		public final static short CREATE = 10;
		/*** 解散群组，请求和响应是一样的 ***/
		public final static short DISBAND = 11;
		/*** 更改群成员身份，请求和响应是一样的 ***/
		public final static short CHANGE_IDENTITY = 12;
		
		public final static short KICK_OUT_GROUP = 13;
		/*** 加入群组，请求和响应是一样的 ***/
		public final static short JOIN = 20;
		/*** 退出群组，请求和响应是一样的 ***/
		public final static short EXIT = 21;
		/*** 拉好友进群，请求和响应是一样的 ***/
		public final static short INVITE_TO_GROUP=22;
		/*** 发布公告，请求和响应是一样的 ***/
		public final static short PUBLIC_NOTICE = 30;
		/*** 删除公告，请求和响应是一样的 ***/
		public final static short REMOVE_NOTICE = 31;
		/*** 验证消息，请求和响应是一样的 ***/
		public final static short VERIFY_MSG = 32;
		/*** 更改设定，请求和响应是一样的 ***/
		public final static short CHANGE_SETTING = 33;
		/*** 获取公告，请求和响应是一样的 ***/
		public final static short GET_NOTICE = 40;
		
		public final static short UPLOAD_FILE = 50;
		public final static short VIEW_FILE = 51;
		public final static short DELETE_FILE = 52;
	}

	/*** 聊天消息命令 ***/
	public final static short CHATC = 40;

	public final static class Chat {
		/*** 文本，请求和响应是一样的 ***/
		public final static short TEXT = 10;
		/*** 表情，请求和响应是一样的 ***/
		public final static short EXPRESSION = 11;
		/*** 图片，请求和响应是一样的 ***/
		public final static short IMAGE = 20;
		/*** 语音，请求和响应是一样的 ***/
		public final static short VOICE = 21;
		/*** 视频，请求和响应是一样的 ***/
		public final static short VIDEO = 22;
		/*** 截屏，请求和响应是一样的 ***/
		public final static short SCREENSHOT = 23;
		/*** 文件，请求和响应是一样的 ***/
		public final static short FILE = 24;
		/*** 查看历史记录，请求和响应是一样的 ***/
		public final static short FIND_HISTORY_MSG = 30;
	}

	/*** 更新数据命令***/
	public final static short UPDATE_DATAC = 50;

	public final static class UpdateData {
		/*** 更新资料，请求和响应是一样的 ***/
		public final static short INFO = 10;
		/*** 更新个人签名，请求和响应是一样的 ***/
		public final static short STATE_MSG = 11;
		/*** 更改个人设置，请求和响应是一样的 ***/
		public final static short PERSONAL_SETTING = 20;
		/*** 修改密码，请求和响应是一样的 ***/
		public final static short CHANGE_PWD = 21;
	}

	/*** 系统消息命令 ***/
	public final static short SYSTEMC = 60;

	public final static class System {
		/*** 上线通知，响应 ***/
		public final static short UP_LINE_NOTICE = 10;
		/*** 下线通知，响应 ***/
		public final static short DOWN_LINE_NOTICE = 11;
		/*** 系统通告，响应 ***/
		public final static short NOTICE = 20;
		/*** 待办，响应 ***/
		public final static short BACKLOG = 30;
		/*** 挤下线通知，响应 ***/
		public final static short KICKED_OFF = 40;
		
		public final static short VIEW_HISTORY_SYS_NOTICE = 50;
	}

}
