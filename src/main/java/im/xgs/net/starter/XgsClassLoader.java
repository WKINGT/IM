package im.xgs.net.starter;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XgsClassLoader extends URLClassLoader {

	private static Logger logger = LoggerFactory.getLogger("xgs.net.start");

	XgsClassLoader(ClassLoader parent, File libDir) throws IOException, MalformedURLException {

		super(new URL[] { libDir.toURI().toURL() }, parent);

		File[] jars = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				boolean accept = false;
				String smallName = name.toLowerCase();
				if (smallName.endsWith(".jar")) {
					accept = true;
				} else if (smallName.endsWith(".zip")) {
					accept = true;
				}
				return accept;
			}
		});
		if (jars == null) {
			return;
		}
		for (int i = 0; i < jars.length; i++) {
			if (jars[i].isFile()) {
				logger.info("loading " + jars[i].getName() + " ...");
				addURL(jars[i].toURI().toURL());
			}
		}
	}
	
}
