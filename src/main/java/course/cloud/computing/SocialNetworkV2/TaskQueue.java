package course.cloud.computing.SocialNetworkV2;

import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskQueue {

	ConcurrentLinkedQueue<WorkItem> workQueue = new ConcurrentLinkedQueue<WorkItem>();
	
	ExecutorService executor = null;
	private int depth = -1;

	private TaskQueue(int queueDepth) {
		depth = queueDepth;
	}

	public void destroy() {
		executor.shutdown();
		while (executor.isTerminated()) {
		}
		workQueue.clear();
	}

	public static TaskQueue create(Properties properties) {
		int queueDepth = properties
				.getProperty("course.cloud.computing.queuedepth") != null ? Integer
				.parseInt(properties
						.getProperty("course.cloud.computing.queuedepth"))
				: 10000;

		TaskQueue queue = new TaskQueue(queueDepth);
		int numWorkers = properties
				.getProperty("course.cloud.computing.workers") != null ? Integer
				.parseInt(properties
						.getProperty("course.cloud.computing.workers")) : 1;

		queue.executor = Executors.newFixedThreadPool(numWorkers);
		for (int i = 0; i < numWorkers; i++) {
			Runnable worker = new Worker(queue.workQueue);
			queue.executor.execute(worker);
		}
		
		return queue;
	}

	public long add(WorkItem item) {
		if (workQueue.add(item)) {
			return workQueue.size();
		} else {
			return -1;
		}
	}

	public long getDepth() {
		return workQueue.size();
	}

}
