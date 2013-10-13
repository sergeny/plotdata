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
	private def connectToSql // Closure; when called, returns "sql", a new groovy.sql.Sql instance
	private int total, startTotal;
    private ScheduledExecutorService executor;
    private ScheduledFuture future;
 
    public BackendTask(def dataToUpdate) {
        this(dataToUpdate, 1000);
    }
 
    /**
     * Construct the task with the given total - useful to restore its state as needed.
     */
    public BackendTask(def dataToUpdate, int period, def connectToSql) {
		this.period = period
		update = dataToUpdate
		this.connectToSql = connectToSql 
        total = 0;
        startTotal = total;
        start();
    }

	// NB! When adding fields to BackendTask, remember to update here
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