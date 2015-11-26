package com.adchina.dbutil.datahandle;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.adchina.dbutil.task.RunnableTask;

/**
 * 多库数据合并处理工具.
 * 
 * @author F.Fang
 * 
 */
public class CustomDataHandler {
    private static final Log LOG = LogFactory.getLog(CustomDataHandler.class);

	private List<String> dbs;

	public CustomDataHandler() {

	}

	public CustomDataHandler(List<String> newDbs) {
		this.dbs = newDbs;
	}

	/**
	 * 业务查询.
	 * 
	 * @param executor
	 *            查询器.
	 * @param dbTag
	 *            数据库代号.
	 * @return 多库合并后或单库的数据对象列表.
	 */
	public Map<String, DataModel> query(DataExecutor executor) {
		// 若无业务执行 返回空
		if (executor == null) {
			return null;
		}

		Map<String, DataModel> dataContainer = new ConcurrentHashMap<String, DataModel>();
		// 如果db有多个, 并行处理
		doExecute(executor, dataContainer);
		return dataContainer;
	}

	private void doExecute(DataExecutor executor, Map<String, DataModel> dataContainer) {
		ExecutorService exec = Executors.newFixedThreadPool(dbs.size());
		final CountDownLatch begin = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(dbs.size());
		for (String tag : dbs) {
			// 逻辑处理时还可能需要唯一性确定的相关参数.
			// 只使用循环处理时不能达到数据源切换的效果，因为只是单线程处理.
			RunnableTask task = new RunnableTask(executor, dataContainer,
					begin, end, tag);
			exec.submit(task);
		}
		begin.countDown();
		try {
			end.await();
		} catch (InterruptedException e) {
		    //
			//e.printStackTrace();
			LOG.error("线程计数器执行异常!", e);
		}finally{
		    exec.shutdown();
		}
	}
	
	public boolean isSingleDB(){
		return dbs.size()==1;
	}

}
