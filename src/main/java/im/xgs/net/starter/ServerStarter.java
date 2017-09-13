package im.xgs.net.starter;

import im.xgs.net.WChatServer;
import im.xgs.net.logs.LogPrintStream;
import im.xgs.net.util.TextUtil;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerStarter {
	private static Logger logger = LoggerFactory.getLogger("xgs.net.start");
	private static final String DEFAULT_LIB_DIR = "/lib";

	public static void main(String[] args) {
		new ServerStarter().start();
	}

	private void start() {
		logger.info(TextUtil.use("banner.txt"));
		System.setOut(new LogPrintStream(false));
		System.setErr(new LogPrintStream(true));
		try {
			ClassLoader parent = findParentClassLoader();
			String libDirString = System.getProperty("user.dir")
					+ DEFAULT_LIB_DIR;
			File libDir;
			libDir = new File(libDirString);
			if (!libDir.exists()) {
				logger.warn("Lib directory " + libDirString
						+ " does not exist. Using default " + "../lib");

				libDir = new File("../lib");
			}

			ClassLoader loader = new XgsClassLoader(parent, libDir);
			//loader.loadClass(name)
			Thread.currentThread().setContextClassLoader(loader);
			
		

			new WChatServer().start();
			// Class containerClass =
			// loader.loadClass("org.jivesoftware.openfire.XMPPServer");
			//
			// containerClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ClassLoader findParentClassLoader() {
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = getClass().getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return parent;
	}
	
}
