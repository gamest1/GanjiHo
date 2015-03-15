package comp6721;

public class AsyncExecutor {
 
	private static java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newCachedThreadPool();
 
	public synchronized static void asyncExecuteTask(final Runnable task, final long timeout, final AsyncExecutorCallback callback) {
		pool.execute(new Runnable() {
						@Override
						public void run() {
							java.util.concurrent.Future<AsyncExecutorCallback> marker = pool.submit(task, callback);
							try {
								marker.get(timeout, java.util.concurrent.TimeUnit.MILLISECONDS).taskCompleted();
							} catch (Exception e) {
			                    marker.cancel(true);
								callback.taskFailed();
							} 
						} 
					});
	}
 
	public interface AsyncExecutorCallback {
		void taskCompleted();
		void taskFailed();
	} 
}