1. Download and install Groovy from http://groovy.codehaus.org/Download.
Make sure you have PATH configure so you can just type 'groovy' from the command line.

2. Download and install Grails from http://grails.org/download.

3. It is a good idea to set the environment variables JAVA_HOME, GROOVY_HOME, and GRAILS_HOME, though the Grails Wrapper may be able to help you with that.

4. For everything that follows, use the grails wrapper "grailsw", if you are on unix, or "grailsw.bat", if you are on Windows.

5. Configure connection to a database by manually editing grails-app/conf/DataSource.groovy.
Note that you can have different settings for "development", "test", and "production".

6. Configure the backend by running "grailsw configure-backend" from the main grails directory.
You can also specify one of
./grailsw dev configure-backend
./grailsw prod configure-backend
./grailsw test-configure-backend

This will first of all test connection to the database, so you will know if your DataSource.groovy is correctly configured.
Then it will create a configuration script for the backend. By default this file will be created in the same (current) directory,
and if you run it as a groovy script, it will also test the connection.
You can also specify an alternative name for this file, e.g.
grailsw configure-backend /etc/BackendConfig.groovy

7. Use the script backend.sh to create tables in the SQL database:
./backend.sh --create-tables
Also check out
./backend.sh -h or ./backend.sh --help

8. Run the server:
./backend.sh --run
By default you will be using BackendConfig_production.groovy in the current directory that was perhaps created at step 6.
You can also specify a different file:
./backend.sh -c /etc/BackendConfig.groovy --run

9. Stop the server with Ctrl+C (better option?)