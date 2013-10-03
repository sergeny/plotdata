/* Testing grails/GORM connection to mysql
 * Usage: "grails test-mysql" or, to run in another environment, e.g. "grails test test-mysql"
 * (This is in case you have changed DataSource.groovy and nothing is working.)
 *
 * Up to five parameters:
 * grails test test-mysql <host> <port> <database> <user> <password>
 * e.g. grails test test-mysql localhost 3306 test root rootpassword.
 * The database name can also be empty.
 */

includeTargets << grailsScript("_GrailsInit") << grailsScript("_GrailsArgParsing")

target(main: "The description of the script goes here!") {

    def list=argsMap['params']
    def host=list[0] ? list[0] : 'localhost'  
    def port=list[1] ? list[1] : '3306'       
    def db=list[2] ? list[2] : ''             // can leave empty
    def user=list[3] ? list[3] : 'grails'      
    def pswd=list[4] ? list[4] : 'grails'

    println "Connecting to " + host + ":" + port
    println "Database:" + db
    println "User: " + user
    println "Password: " + pswd
    
    def jdbc_string='jdbc:mysql://' + host + ':' + port + '/' + db

    def sql = Sql.newInstance(jdbc_string, user, pswd, "com.mysql.jdbc.Driver")
}

setDefaultTarget(main)
