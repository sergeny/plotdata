import org.codehaus.groovy.grails.commons.ConfigurationHolder
import com.sergeyorshanskiy.domain.DataPoint
import java.util.timer.*

class TimerTaskExample extends TimerTask {
      public void run() {
      	     println "Timer"
      }
}

println "hi"
def _url      = ConfigurationHolder.config.dataSource.url 
def _username = ConfigurationHolder.config.dataSource.username 
def _password = ConfigurationHolder.config.dataSource.password 
def _driver   = ConfigurationHolder.config.dataSource.driverClassName 
def sql = groovy.sql.Sql.newInstance(_url, _username, _password, _driver) 
println "" + _url + " " + _username + " " +  _password + " " + _driver
println sql

//int delay=5000
//int period=1000
//Timer timer = new Timer()
//timer.scheduleAtFixedRate(new TimerTaskExample(), delay, period)

//def line = System.console().readLine 'What is your name?'
//println "Hello ${line}"

System.console().readLine()

//int inChar = System.in.read()
//println "You entered ${inChar}"

/*
for (i in 0..<1000) { 
  DataPoint dp = new DataPoint()
  dp.x = i
  dp.y = i*i
  println "saving .. ${dp} ${dp.x} ${dp.y} ${dp.id}"
  println dp.validate()
  dp.save()
  println "flushing .. ${dp} ${dp.x} ${dp.y} ${dp.id}"
  if (dp.save(flush: true)) {
    println "saved ${i}"
  } else {
    println "not saved ${i}"
  }
  sleep 1000
}
println DataPoint.list()
*/