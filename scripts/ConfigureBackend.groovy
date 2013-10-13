

import groovy.sql.Sql
import grails.util.Environment
import groovy.json.StringEscapeUtils
// import org.codehaus.groovy.grails.commons.ConfigurationHolder


def SERIES_TABLE='series'
def POINTS_TABLE='points'

includeTargets << grailsScript("_GrailsInit") << grailsScript("_GrailsArgParsing") 

target(main: "Configure database access for the backend") {

def env = Environment.current.getName()

// This script will create a config and put it in the file name ${CONFIG_FILENAME}
// The user can specify a different config file name as the first command-line parameter
String CONFIG_FILENAME = "BackendConfig_${env}.groovy"
if (argsMap['params']) {
    CONFIG_FILENAME=argsMap['params'][0]
}

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
	
	
	
	println "Writing config to ${CONFIG_FILENAME}..."
	File file = new File(CONFIG_FILENAME)
	if (file.exists()) {
		System.err << " A file or a directory ${CONFIG_FILENAME} already exists, aborted!" 
		System.exit(10)
	}



	
	file <<"""
import groovy.sql.Sql

def connectToSql() {
	this.class.classLoader.rootLoader.addURL(new URL(\"${StringEscapeUtils.escapeJava("" + codeSource.getLocation())}\") )
	sql = Sql.newInstance(\"${_url}\", \"${_username}\", \"${_password}\", \"${_driver}\")
	return sql
}

def createTables(sql) {
    sql.execute \"\"\" CREATE TABLE `${SERIES_TABLE}` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,   /* name and type have to be not null */
    `type` varchar(255) NOT NULL,   /* so that (name, type) is a truly unique pair */
    PRIMARY KEY (`id`),
    UNIQUE KEY  (`name`, `type`)
    ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1\"\"\"

    sql.execute \"\"\" CREATE TABLE `${POINTS_TABLE}` (
    `${SERIES_TABLE}_id` bigint(20) NOT NULL,
    `time` bigint(20) NOT NULL,
    `value` double NOT NULL,
    PRIMARY KEY (`time`),
    KEY  (`${SERIES_TABLE}_id`),
    CONSTRAINT FOREIGN KEY (`${SERIES_TABLE}_id`) REFERENCES `${SERIES_TABLE}` (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=19535 DEFAULT CHARSET=latin1\"\"\"
}

def dataToUpdate() {
    [
        "local": 
            ["always five": {return 5},
             "timestamp":   {new Date().getTime()},
             "freemem":     {Runtime.getRuntime().freeMemory()},
             "random":      {new Random().nextInt(10)} ],
        "stock":
            null
    ]
}

			
def sql = connectToSql()			
sql.eachRow(\"SHOW DATABASES\") {
    println it
}"""

println "Success! Test the result by running: groovy ${CONFIG_FILENAME}"
	

}


setDefaultTarget(main)