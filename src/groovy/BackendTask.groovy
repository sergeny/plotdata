import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.codehaus.groovy.runtime.MethodClosure


/*
 * The files BackendTask.groovy and BackendWatchdog.groovy implement a design pattern: a "Watchdog" watching over a "Task" run by an Executor.
 * The implementation of the design pattern was taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
 * Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
 *
 * However, this file also contains the core logic of the backend.
 * 
 */


// BackendTask.groovy and BackendWatchdog.groovy
// This code was originally taken from the blog http://blog.temposwc.com/2011/01/schedule-periodic-task-with-watchdog.html
// Also take a look at the follow-up post, periodic task+watchdog+generics, http://blog.temposwc.com/2011/01/periodic-task-and-watchdog-reusable-and.html
// This code's copyright status is unknown (which is why I am reusing the simpler version --- it is so simple that it would be almost certainly considered common knowledge.)




public class BackendTask {
	def SERIES_TABLE='series'
	def POINTS_TABLE='points' 

	private int period // Execute every that many milliseconds
    private def update // Data to update - probably a dictionary with dictionaries of groovy closures
	private def connectToSql // Closure; when called, returns "sql", a new groovy.sql.Sql instance
	private int total, startTotal;
    private ScheduledExecutorService executor;
    private ScheduledFuture future;
 
  


    /**
     * Construct the task with the given total - useful to restore its state as needed.
     */
    public BackendTask(def dataToUpdate, int period, MethodClosure connectToSql) {
		this.period = period
		update = dataToUpdate
		this.connectToSql = connectToSql 
		
        total = 0; // Temporary development/testing code
        startTotal = total; // Temporary development/testing code
        start();
    }

	
	public BackendTask restartItself() {
		// Note: If for whatever reason there is no matching constructor, a real-time exception will be thrown, to be caught by BackendWatchdog
		return new BackendTask(this.update, this.period, this.connectToSql)
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
	private long seriesId(def sql, String seriesType, String seriesName) {
		
		// Here we want to catch problems right away and to fix asap. So print any exceptions before they go to the ExecutorService.
		try {
			def rows
			for (attempt in [0, 1]) { // Select. If not there --- insert, then select again.
				rows = sql.rows("SELECT id from `$SERIES_TABLE` WHERE type=? AND name=?", [seriesType, seriesName]) 
		
				assert rows.size() <= 1, "At most one row: (type, series) should be unique, and neither can be NULL"
				assert (rows.size() == 1) || (attempt == 0), "If this is our second attempt, we should have already inserted"
			
				if (rows.size() == 0) { // No such series; insert
					println "Adding new series: type: ${seriesType}, name: ${seriesName}"
					def result = sql.execute "INSERT INTO ${SERIES_TABLE} (type, name) VALUE (?, ?)", [seriesType, seriesName]
					assert (sql.updateCount == 1)   // Inserted one row
				}
			}
			
			assert rows.size() == 1
			assert "id" in rows[0]
			
			return rows[0]["id"]
		} catch (Throwable e) { 
			println "$e"
			throw e
		}
	}

	/*
	 * TheTask.run() is the function that is actually doing the work. That is, it generates new data using the closures in the "config"
	 * and then populates the database.
	 */
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
			
			try {
			def sql = this.connectToSql()
			new_values.each {
				s_type, d -> d.each {
					name, p ->  // p[0] - timestamp, p[1] - value
						// The JDBC '?'-syntax protects against SQL injection
						def params = [seriesId(sql, s_type, name)] + p // Important to call seriesId, which does SELECT and/or INSERT, before doing INSERT
						println "READY TO INSERT $params"
						sql.execute "INSERT INTO ${POINTS_TABLE} (${SERIES_TABLE}_id, time, value) VALUE (?, ?, ?)", params
						println "inserted $sql.updateCount"
				}
			}
			sql.close()
			} catch (Throwable t) {
				println "$t"
				throw t
			}
		
			// NOTE: this is Temporary Debug/Test/Development code
			// Kill the thread every once in a while and see if it recovers :-)
            total++;
            if (total > startTotal + 10) {
             
                throw new IllegalStateException();
            }
        }
    }
 
	// Temporary debug/development code
    public int getTotal() {
        return total;
    }
}