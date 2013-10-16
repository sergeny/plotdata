import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/*
 * The files BackendTask.groovy and BackendWatchdog.groovy implement a design pattern: a "Watchdog" watching over a "Task" run by an Executor.
 * The implementation of the design pattern was taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
 * Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
 *
 */


public class BackendWatchdog {
 
    private BackendTask task;


    public BackendWatchdog(BackendTask theTask) {
        task = theTask;
		// Watching every 2 seconds. TODO: do we need to watch at all, and what would be the best period?
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Watcher(), 0, 2000, TimeUnit.MILLISECONDS);
    }
 
    public BackendTask getTask() {
        return task;
    }
 
    private class Watcher implements Runnable {
 
        public void run() {
            //while (true) {
                if (task.isDone()) {
                    System.out.println("Task stopped - restarting...");
                    //  cancel the task and shutdown the executor
                    task.cancel(true);
                    task.shutdown();
					try {
                    	// restart from where we left off - state gets restored:
						task = task.restartItself()
					} catch (Exception e) {
						System.err <<"ERROR. TASK RESTART FAILED " << e
					}
                }
            //}
        }
    }
}