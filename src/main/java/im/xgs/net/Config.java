package im.xgs.net;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class Config {
	public static final String systemId;
	public static final Prop prop ;
	
	static{
		prop = PropKit.use("cnf.txt");
		systemId = prop.get("system.id","system");
	}
}
