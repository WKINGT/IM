package im.xgs.net.msg;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.JsonKit;

import im.xgs.net.service.ImUserService;

/**
 * 解决当群成员比较大 的时候，发送群消息慢的情况
 * @author TianW
 *
 */
class ThreadPool{
	ImUserService imservice = Enhancer.enhance(ImUserService.class);
	Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService singlExecutorService;
	private static ThreadPool pool;
	
	private ThreadPool(){
		singlExecutorService = Executors.newSingleThreadExecutor(new ImThreadFactory());
	}
	
	public static ThreadPool instance(){
		if(pool == null){
			pool = new ThreadPool();
		}
		return pool;
	}
	
	public ExecutorService get(){
		return this.singlExecutorService;
	}
	/**
	 * 批量保存离线消息
	 * @param list
	 */
	public void batchExecOfflines(final List<Map<String,Object>> list){
		this.singlExecutorService.execute(new Runnable() {
			public void run() {
				if(logger.isDebugEnabled()){
					logger.debug("send msgBody:{}",JsonKit.toJson(list));
				}
				imservice.saveOfflines(list);
			}
		});
	}
	/**
	 * 单线程保存离线消息
	 * @param userId
	 * @param fromId
	 * @param toType
	 * @param msgBody
	 */
	public void ExecOfflines(final String userId, final String fromId, final int toType, final String msgBody){
		this.singlExecutorService.execute(new Runnable() {
			public void run() {
				if(logger.isDebugEnabled()){
					logger.debug("send msgBody:{}",JsonKit.toJson(msgBody));
				}
				imservice.saveOffline(userId, fromId, toType, msgBody);
			}
		});
	}
	
}