package im.xgs.net.msg;

public final class ImMsgType {

	public static final int msg = 0x1000;
	public static final int cmd = 0x2000;
	public static final int ntf = 0x3000;//通知
	
	private ImMsgType(){}

	public static final class msgc{
		private msgc(){}
		public static final int text = 0x1001;
		public static final int sound= 0x1002;
		public static final int file = 0x1003;
		public static final int image= 0x1004;
		public static final int vedio= 0x1005;
	}
	public static final class cmdc{
		private cmdc(){}
		public static final class req{
			private req(){}
			public static final int login = 0x2101;
			public static final int logout= 0x2103;
			//添加好友
			public static final int add_friends = 0x2106;
			//创建用户
			public static final int add_users = 0x2108;
		}
		public static final class rep{
			private rep(){}
			//登陆响应
			public static final int login_succ = 0x2201;
			public static final int login_fail = 0x2202;
			//登出响应
			public static final int logout_succ = 0x2203;
			public static final int logout_fial = 0x2204;
			//用户在其它地方登陆被挤下线
			public static final int login_down = 0x2205;
			
			//添加好友成功，
			public static final int add_friends_succ = 0x2206;
			//添加好友失败
			public static final int add_friends_fail = 0x2207;
			
			public static final int add_users_succ = 0x2208;
			public static final int add_users_fail = 0x2209;
		}
	}
	
	public static final class ntfc{
		private ntfc(){};
		public static final class rep{
			private rep(){}
			//用户（同一个）在其它地方上线
			public static final int user_up = 0x3001;
			//用户（同一个）在其它地方下线
			public static final int user_down= 0x3002;
			//好友（或组织机构）上线
			public static final int friend_up = 0x3003;
			//好友（或组织机构）下线
			public static final int friend_down = 0x3004;
			//群组上线通知  //FIXME 待定，如果这里作通知，如果当群成员比较大的时候，将会很消耗资源
			public static final int group_up = 0x3005;
			//群组下线通知  //FIXME 同  #link{group_up}
			public static final int group_down = 0x3006;
		}
		public static final class req{
			private req(){}
		}
	}
}
