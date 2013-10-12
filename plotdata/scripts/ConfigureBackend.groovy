

import groovy.sql.Sql
import grails.util.Environment
import groovy.json.StringEscapeUtils
// import org.codehaus.groovy.grails.commons.ConfigurationHolder


includeTargets << grailsScript("_GrailsInit") << grailsScript("_GrailsArgParsing") 

target(main: "Configure database access for the backend") {

def env = Environment.current.getName()
println "Current environment: ${env}"
println "Parsing DataSource.groovy for database connectivity settings"
def config
try {
config = new ConfigSlurper(env).parse(new File("grails-app/conf/DataSource.groovy").toURL())
} catch (java.io.FileNotFoundException e) {
	System.err << "Cannot parse DataSource.groovy: " + e
	System.exit(1)
}

_url = config.dataSource.url
_username = config.dataSource.username
_password = config.dataSource.password
_driver = config.dataSource.driverClassName

println "Testing database connection..."
println "url: ${_url}"
println "username: ${_username}"
println "password: ${_password}"
println "driver: ${_driver}"


/*
Need to run the script with run-script for this to work. Then other things get broken (though it can be done either way)
def _url      = ConfigurationHolder.config.dataSource.url 
def _username = ConfigurationHolder.config.dataSource.username 
def _password = ConfigurationHolder.config.dataSource.password 
def _driver   = ConfigurationHolder.config.dataSource.driverClassName 
*/

    // def list=argsMap['params']

    def sql
	// Let's try to catch exceptions to explain what's wrong
    try {
        sql = Sql.newInstance(_url, _username, _password, _driver)
    } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
		System.err << "Cannot connect to ${_url}: " + e
		System.exit(2)
    } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) { // Of course, this is just for MySQL
        System.err <<"Wrong MySQL database name? " + e
		System.exit(3)
    } catch (java.sql.SQLException e) {
        System.err <<"Wrong database login/password? " + e
		System.exit(4)
    }

	println "OK. Connected to the database"
    println "SUCCESS! Connected to MySQL"
    def query = "SHOW DATABASES"

    println "Issuing query " + query + "..."
    sql.eachRow(query) {
      println it
    }

    println ""
	println "OK, Database test successful"

	// getProtectionDomain() may sometimes fail because of security permissions, etc.
	def codeSource = Class.forName(_driver).getProtectionDomain().getCodeSource()
	println "JDBC driver was loaded from: ${codeSource}"
	
	// File jarFile = new File(codeSource.getLocation().toURI().getPath())
	// this.class.classLoader.rootLoader.addURL( new URL(\"file://${jarFile}\" ) )"
	
	println "this.class.classLoader.rootLoader.addURL(\"${StringEscapeUtils.escapeJava("" + codeSource.getLocation())}\")"
	println "def sql = Sql.newInstance(\"${_url}\", \"${_username}\", \"${_password}\", \"${_driver}\")"
	println "sql.eachRow(\"SHOW DATABASES\") {"
	println "  println it"
	println "}"
	

}


setDefaultTarget(main)