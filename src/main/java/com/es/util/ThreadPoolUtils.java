package com.es.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;


@Component
public class ThreadPoolUtils {

	public static final Logger logger = LogManager.getLogger(ThreadPoolUtils.class);

	private ThreadPoolExecutor threadPoolExecutor;

	/** 线程池维护线程的最少数量 */
	private int corePoolSize = 8;

	/** 线程池维护线程的最大数量 */
	private int maximumPoolSize = 32;

	/** 线程池维护线程所允许的空闲时间 */
	private int keepAliveTime = 1 * 60;

	/** 线程池所使用的缓冲队列大小 */
	private int queueMaxSize = 1024;

	/** 0:Array 1:Linked */
	private int queueType = 0;

	private BlockingQueue<Runnable> workQueue;


	@PostConstruct
	public void init() {
		if (this.workQueue == null) {
			if (queueType == 0) {
				this.workQueue = new ArrayBlockingQueue<Runnable>(queueMaxSize);
			} else {
				this.workQueue = new LinkedBlockingQueue<Runnable>(queueMaxSize);
			}
		}
		threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				workQueue, new RejectedExecutionHandler() {

					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						logger.warn("rejectedExecution - ThreadName:{}, activeCount:{}", r.getClass().getName(),
								executor.getActiveCount());
					}
				});
		logger.info("threadPoolExecutor init over.");
		
	}

	public void execute(Runnable runnable) {
		threadPoolExecutor.execute(runnable);
	}

	public void submit(Runnable runnable) {
		threadPoolExecutor.submit(runnable);
	}

	public Future<Object> submit(Callable<Object> callable) {
		return threadPoolExecutor.submit(callable);
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return threadPoolExecutor;
	}
}
