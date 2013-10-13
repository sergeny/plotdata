import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


// BackendTask.groovy and BackendWatchdog.groovy
// This code was originally taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
// Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
// This code's copyright status is unknown (which is why I am reusing the simpler version --- it is so simple that it would be almost certainly considered common knowledge.)




public class BackendTask {
	def SERIES_TABLE='series'
	def POINTS_TABLE='points' 

	private int period // Execute every that many milliseconds
    private def update // Data to update - probably a dictionary with dictionaries of groovy closures
	private int total, startTotal;
    private ScheduledExecutorService executor;
    private ScheduledFuture future;
 
    public BackendTask(def dataToUpdate) {
        this(dataToUpdate, 1000);
    }
 
    /**
     * Construct the task with the given total - useful to restore its state as needed.
     */
    public BackendTask(def dataToUpdate, int period) {
		this.period = period
		update = dataToUpdate
        total = 0;
        startTotal = total;
        start();
    }

	public BackendTask restartItself() {
		return new BackendTask(this.update, this.period)
	}
 
    private void start() {
        // start immediately, repeat every ${period} ms
        executor = Executors.newSingleThreadScheduledExecutor();
        future = executor.scheduleAtFixedRate(new TheTask(), 0, period, TimeUnit.MILLISECONDS);
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
  
	// e.g. type is "local", name is "cpu" or 
	// type is "stock", name is "AAPL"
	private int seriesId(String seriesType, String seriesName) {
		return 1
	}

    private class TheTask implements Runnable {	
        public void run() {
        	println "Running... "
    	
			
			// First compute all the new values, then put all of them to a database
			// Currently using a separate timestamp for each value
			// May be a waste. But then, if one update takes forever (network connection, etc.),
			// the other timestamps will be more accurate.
			def new_values=[:]
		    if (update["local"]) {
				new_values["local"] = [:]
				def l = new_values["local"]
		        update["local"].each { 
		            name, v_closure -> //println "${k}, ${v()}"
						l[name] = [new Date().getTime(), v_closure()] // Execute the closure simultaneously with taking the timestamp
		        }
		    }
		
			println "${new_values}"
			
			new_values.each {
				s_type, d -> d.each {
					name, p ->  // p[0] - timestamp, p[1] - value
						println "INSERT ${SERIES_TABLE}_id=${seriesId(s_type, name)} " +			
						"${p}"//	"time=${l[0]} value=${l[1]}"
				}
			}
		
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