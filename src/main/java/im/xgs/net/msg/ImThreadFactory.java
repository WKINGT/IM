package im.xgs.net.msg;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImThreadFactory implements ThreadFactory {

	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	
	private final ThreadGroup group;
	private final String namePrefix;
	
	public ImThreadFactory(){
		SecurityManager s = System.getSecurityManager();
		group = (s != null)? s.getThreadGroup():Thread.currentThread().getThreadGroup();
		namePrefix = "im-pool-"+poolNumber.getAndIncrement()+"-thread-";
	}
	
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(group,r,namePrefix+threadNumber.getAndIncrement(),0);
		if(thread.isDaemon()){
			thread.setDaemon(false);
		}
		if(thread.getPriority() != Thread.NORM_PRIORITY){
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		thread.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		return thread;
	}

	static class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
		private static Logger logger = LoggerFactory.getLogger(ThreadUncaughtExceptionHandler.class);
		public void uncaughtException(Thread t, Throwable e) {
			logger.error(e.getMessage(),e);
			
		}
		
	}
}
