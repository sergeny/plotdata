1. Assuming you have Java installed, download and install Groovy from http://groovy.codehaus.org/Download.
Make sure you have PATH configure so you can just type 'groovy' from the command line.

2. Download and install Grails from http://grails.org/download.

3. It is a good idea to set the environment variables JAVA_HOME, GROOVY_HOME, and GRAILS_HOME, though the Grails Wrapper may be able to help you with that.

4. For everything that follows, use the grails wrapper "grailsw", if you are on unix, or "grailsw.bat", if you are on Windows.

5. Install a database such as MySQL with InnoDB, or anything else supported by Grails. You cannot use a default in-memory database as the backend won't be able to write to it. (You may be able to use the default H2 database configured to save data to a file on the hard drive. This wasn't tested yet, though.)

6. Configure connection to a database by manually editing grails-app/conf/DataSource.groovy.
Note that you can have different settings for "development", "test", and "production". 
If necessary, refer to the Grails documentation: http://grails.org/doc/2.1.0/guide/conf.html#dataSourcesAndEnvironments
Basically, you want to set url, driverClassName, username, and password. You may set it at the top, in the main dataSource { } clause,
and/or for a particular environment. 

7. If you are using something other than MySQL, which should be already configured, make sure that your JDBC driver is included as a dependency in grails-app/conf/BulidConfig.groovy. It should be much like the following:
  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
     runtime 'mysql:mysql-connector-java:5.1.22'
  }


8. Here you will see if your configuration is working.
Autoatically configure the backend by running "grailsw configure-backend" from the main grails directory.
You can also specify one of
```shell
./grailsw dev configure-backend
./grailsw prod configure-backend
./grailsw test-configure-backend
```
This will first of all test connection to the database, so you will know if your DataSource.groovy is correctly configured.
Then it will create a configuration script for the backend. By default this file will be created in the same (current) directory,
and if you run it as a groovy script, it will also test the connection.
You can also specify an alternative name for this file, e.g.
grailsw configure-backend /etc/BackendConfig.groovy

9. Use the script backend.sh to create tables in the SQL database:
```shell
./backend.sh --create-tables
```
Also check out
```shell
./backend.sh -h or ./backend.sh --help
```

10. Run the server:
./backend.sh --run
By default you will be using BackendConfig_production.groovy in the current directory that was perhaps created at step 6.
You can also specify a different file:
./backend.sh -c /etc/BackendConfig.groovy --run
DO NOT RUN MULTIPLE BACKEND SERVERS AT THE SAME TIME. 

11. Start the frontend with
```
grailsw run-app
```

12. Navigate to http://localhost:8080/plotdata/


