import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


// BackendTask.groovy and BackendWatchdog.groovy
// This code was essentially taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
// Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
// This code's copyright status is unknown (which is why I am reusing the simpler version --- it is so simple that it would be almost certainly considered common knowledge.)


public class BackendTask {
 
    private int total, startTotal;
    private ScheduledExecutorService executor;
    private ScheduledFuture future;
 
    public BackendTask() {
        this(0);
    }
 
    /**
     * Construct the task with the given total - useful to restore its state as needed.
     */
    public BackendTask(int theTotal) {
        total = theTotal;
        startTotal = total;
        start();
    }
 
    private void start() {
        // start immediately, repeat every 300 ms
        executor = Executors.newSingleThreadScheduledExecutor();
        future = executor.scheduleAtFixedRate(new TheTask(), 0, 300, TimeUnit.MILLISECONDS);
    }
 
    /**
     * Allow the watchdog to cancel the task
     */
    public boolean cancel(boolean b) {
        return future.cancel(b);
    }
 
    /**
     * Allow the watchdog to determine if the task has completed (which it never should unless
     * it has encountered an exception)
     */
    public boolean isDone() {
        return future.isDone();
    }
 
    /**
     * Allow the watchdog to shutdown the executor
     */
    public void shutdown() {
        executor.shutdown();
    }
 
    private class TheTask implements Runnable {	
        public void run() {
        	println "Running..."
    		// update object state on each execution
            total++;
            if (total > startTotal + 10) {
                // generate an exception to simulate what might happen
                throw new IllegalStateException();
            }
        }
    }
 
    public int getTotal() {
        return total;
    }
}