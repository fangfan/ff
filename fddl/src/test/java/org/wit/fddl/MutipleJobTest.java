package org.wit.fddl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class MutipleJobTest {

	@Test
	public void testMutipleJobSubmit() throws InterruptedException {
		
		ExecutorService exec = Executors.newFixedThreadPool(2);
		List<String> container = new ArrayList<String>();

		final CountDownLatch begin = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(2);
		Job job1 = new Job(container,"job1",begin,end);
		Job job2 = new Job(container,"job2",begin,end);
		
		exec.submit(job1);
		exec.submit(job2);
		
		begin.countDown();
		end.await();
		exec.shutdown();
		
		assertTrue(container.contains("job1"));
		assertTrue(container.contains("job2"));
		assertEquals(2, container.size());
	}
	
	private static class Job implements Runnable{
		
		private List<String> container;
		private String jobTag;
		private CountDownLatch begin;
		private CountDownLatch end;

		public Job(List<String> container,String jobTag,CountDownLatch begin,CountDownLatch end) {
			super();
			this.container = container;
			this.jobTag = jobTag;
			this.begin = begin;
			this.end = end;
		}

		@Override
		public void run() {
			//System.out.println("i am "+jobTag+" !");
			try {
				begin.await();
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				container.add(jobTag);
				end.countDown();
			}
		}
				
	}

}
