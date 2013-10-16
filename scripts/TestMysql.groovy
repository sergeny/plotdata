/*
 *  This little program/GAnt script TestMySql.groovy is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This little program/GAnt script TestMySql.groovy is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import groovy.sql.Sql

/* 
 * This GAnt script allows you to test Grails' (2.2.3) connection to MySQL.
 * Call it "scripts/TestMysql.groovy" relative to the root of your project.
 * Usage: "grails test-mysql" or, to run in another environment, e.g. "grails test test-mysql"
 * (This can be helpful after you edit DataSource.groovy and nothing is working.)
 *
 * Up to five parameters:
 * grails test test-mysql <host> <port> <database> <user> <password>
 * e.g. grails test test-mysql localhost 3306 test root rootpassword.
 * The database name can also be empty.
 *
 * @author	Sergey Orshanskiy
 */

includeTargets << grailsScript("_GrailsInit") << grailsScript("_GrailsArgParsing")

target(main: "GAnt script for Grails to test MySQL connection") {

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

    def sql
    try {
        sql = Sql.newInstance(jdbc_string, user, pswd, "com.mysql.jdbc.Driver")
    } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
        println "ERROR! Cannot connect to " + host + ":" + port
        println "Check host, port, your open ports and other firewall settings; try to connect with some other program"
        println ""
        println e
        return
    } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
        println "MySQL ERROR, perhaps wrong database name!"
        println ""
        println e
        return
    } catch (java.sql.SQLException e) {
        println "MySQL ERROR, perhaps wrong login/password!"
        println ""
        println e
        return
    }

    println "SUCCESS! Connected to MySQL"
    def query = "SHOW DATABASES"

    println "Executing query " + query + "..."
    sql.eachRow(query) {
      println it
    }

    println "OK, Done!"

    println """
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://${host}:${port}/${db}"
            username="${user}"
            password="${pswd}"
        }
        """

}

setDefaultTarget(main)