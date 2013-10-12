
1. For everything that follows, use the grails wrapper "grailsw", if you are on unix, or "grailsw.bat", if you are on Windows.

2. Configure connection to a database by manually editing grails-app/conf/DataSource.groovy.
Note that you can have different settings for "development", "test", and "production".

3. Configure the backend by running "grailsw configure-backend" from the main grails directory.
You can also specify one of
grailsw dev configure-backend
grailsw prod configure-backend
grailsw test-configure-backend

This will first of all test connection to the database, so you will know if your DataSource.groovy is correctly configured.
Then it will create a configuration script for the backend. By default this file will be created in the same (current) directory,
and if you run it as a groovy script, it will also test the connection.

