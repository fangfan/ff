package com.adchina.dbutil.task;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adchina.dbutil.datahandle.DataExecutor;
import com.adchina.dbutil.datahandle.DataModel;
import com.adchina.dbutil.datasource.CustomerContextHolder;

/**
 * 任务.
 * @author F.Fang
 * 
 */
public class RunnableTask implements Runnable {
    
    private static final Log LOG = LogFactory.getLog(RunnableTask.class);

	private DataExecutor dataExecutor;
	private Map<String, DataModel> dataContainer;
	private String dbTag;
	/**
	 * 计时器
	 */
	private CountDownLatch begin;
	private CountDownLatch end;

	public RunnableTask(DataExecutor newDataExecutor,
			Map<String, DataModel> refDataContainer, CountDownLatch begin,
			CountDownLatch end, String dbTag) {
		this.dataExecutor = newDataExecutor;
		this.dataContainer = refDataContainer;
		this.dbTag = dbTag;
		this.begin = begin;
		this.end = end;
	}

	@Override
	public void run() {
		CustomerContextHolder.setDbTag(dbTag);
		try {
			begin.await();
			DataModel dataModel = dataExecutor.execute();
			dataContainer.put(dbTag, dataModel);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			LOG.error("当前任务("+dbTag+")监视计数器发生异常!", e);
		} finally {
			end.countDown();
		}
	}

}
