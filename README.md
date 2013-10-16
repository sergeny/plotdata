<h2>About</h2>
Plotdata is a pet project for displaying various real-time graphs in a web application. It was built using the <a href="http://grails.org/">Groovy/Grails</a> web framework and MySQL as a backend, though theoretically another database can be substituted (with some adjustments). The backend server is also implemented in Groovy and works with the database directly. The frontend is not using any domain classes, and Hibernate is disabled. The Javascript library <a href="http://www.highcharts.com/">Highcharts/Highstock</a> is used to draw the graphs.
Lots of things still to be done; check out the <a href="https://github.com/Commentor/plotdata/wiki">Wiki</a>.

The code in this repository is availabe under the <a href="http://www.gnu.org/licenses/lgpl.html">GNU Lesser General Public License</a>, except possibly where stated otherwise.

<h2>Installation (short version)</h2>
1. Install Groovy and MySQL (or, theoretically, another SQL RDBMS). 
2. Install Grails or let the Grails wrapper, ```grailsw```, do it for you. You can always use ```grailsw``` instead of ```grails```.
3. Edit ```DataSource.groovy``` and maybe ```BuildConfig.groovy``` to configure connection to MySQL.
4. Generate backend config with  ```./grailsw [prod|dev|test|] configure-backend [<config-file>]```. For instructions on editing it, see <a href="https://github.com/Commentor/plotdata/wiki/Configuring-the-backend">Wiki: configuring the backend</a>.
5. Create tables with ```./backend.sh [-c <config-file>] --create-tables```. 
6. Run the backend, ```./backend.sh [-c <config-file>] --run```. Do not run multiple copies at the same time!
7. Run the frontend, ```grailsw run-app```.

<h2>Installation (long version)</h2>

<h3> Dependencies</h3>

1. Install Groovy from http://groovy.codehaus.org/Download. 

2. No need to install Grails, just use the Grails Wrapper, ```grailsw``` instead of ```grails```, and it will download Grails for you.

3. Or download and install Grails from http://grails.org/download.

4. Try to run ```grailsw``` (or ```grails```) and ```groovy``` from the command line. If necessary, set ```JAVA_HOME```, ```GROOVY_HOME```, ```GRAILS_HOME```.

5. Install a database server. MySQL is recommended, though in theory you should be able to substitute any other database supported by Grails. (Some manual reconfiguration may be involved.)
Run the database servers, create users; make sure that everything works.

<h3> Configuration </h3>

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
Automatically configure the backend by running "grailsw configure-backend" from the main grails directory.
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
```
grailsw configure-backend /etc/BackendConfig.groovy
```
After that you can freely change the username/password in DataSource.groovy if you want the backend and the frontend to use different usernames for connecting to the database. (Perhaps so the frontend would have read-only rights.)

9. It is important to understand that this "configuration" contains arbitrary Groovy code! It also has all the information about the data that the backend will collect and the frontend will publish. Feel free to edit this file to add other sources of data. Anything that can be put in a Groovy closure can be used, any Groovy code whatsoever! For instructions on editing the config, see <a href="https://github.co\
m/Commentor/plotdata/wiki/Configuring-the-backend">Wiki: configuring the backend</a>.


10. Use the script backend.sh to create tables in the SQL database:
```shell
./backend.sh --create-tables
```
Also check out
```shell
./backend.sh -h or ./backend.sh --help
```

11. Run the server:
```
./backend.sh --run
```
By default you will be using BackendConfig_production.groovy in the current directory that was perhaps created at step 6.
You can also specify a different file:
```
./backend.sh -c /etc/BackendConfig.groovy --run
```
DO NOT RUN MULTIPLE BACKEND SERVERS AT THE SAME TIME. 

12. Start the frontend with
```
grailsw run-app
```

13. Navigate to http://localhost:8080/plotdata/


