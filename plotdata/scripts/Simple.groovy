import org.codehaus.groovy.grails.commons.ConfigurationHolder
import com.sergeyorshanskiy.domain.DataPoint

println "hi"
def _url      = ConfigurationHolder.config.dataSource.url 
def _username = ConfigurationHolder.config.dataSource.username 
def _password = ConfigurationHolder.config.dataSource.password 
def _driver   = ConfigurationHolder.config.dataSource.driverClassName 
def sql = groovy.sql.Sql.newInstance(_url, _username, _password, _driver) 
println "" + _url + " " + _username + " " +  _password + " " + _driver
println sql

DataPoint dp = new DataPoint()
dp.x = 1
dp.y = 2
dp.save(flush: true)
println DataPoint.list()
