import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


// BackendTask.groovy and BackendWatchdog.groovy
// This code was essentially taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
// Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
// This code's copyright status is unknown (which is why I am reusing the simpler version --- it is so simple that it would be almost certainly considered common knowledge.)


public class BackendWatchdog {
 
    private BackendTask task;


    public BackendWatchdog(BackendTask theTask) {
        task = theTask;
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Watcher(), 0, 200, TimeUnit.MILLISECONDS);
    }
 
    public BackendTask getTask() {
        return task;
    }
 
    private class Watcher implements Runnable {
 
        public void run() {
 
            while (true) {
                if (task.isDone()) {
                    System.out.println("Task stopped - restarting...");
                    //  cancel the task and shutdown the executor
                    task.cancel(true);
                    task.shutdown();
                    // restart from where we left off - state gets restored:
                    int period = task.getPeriod();
                    task = new BackendTask(period);
                }
            }
        }
    }
}