import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// Thanks to http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html

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
                    int lastKnownState = task.getTotal();
                    task = new BackendTask(lastKnownState);
                }
            }
        }
    }
}