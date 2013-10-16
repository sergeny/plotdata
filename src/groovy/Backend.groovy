import static.java.util.concurrent.TimeUnit.*
import groovy.util.CliBuilder


def cli = new CliBuilder(usage:"groovy ${this.class.name}.groovy [options]")
cli.h(longOpt:'help', 'Print this message')
cli.c(longOpt: 'config', args:1, argName:'config', 'Configuration file, e.g. BackendConfig.groovy')
cli.f(longOpt: 'freq', args:1, argName:'freq', 'Frequency: will post updates each that many ms' )
cli._(longOpt: 'create-tables', 'Create tables in the database')
cli._(longOpt: 'test-database', 'Test connection to the database')
cli._(longOpt: 'run', 'Run the backend server')
options = cli.parse(args)   // Omitting the type declaration so it is scoped to the script and seen from other methods

assert options // would be null (false) on failure
if (options.h) {
	cli.usage()
	return
}
if (!options.c) {
	System.err <<getClass() <<": You need to specify a configuration file\n"
	cli.usage()
	System.exit(2)
}


def script   // The config file will be loaded as a groovy script.

try {
	GroovyShell shell = new GroovyShell()
	script = shell.parse(new File(options.config))
} catch (java.io.FileNotFoundException e) {
	System.err <<getClass() << ": Cannot find config file ${options.config}\n";
	System.exit(3);
} 

def requiresMethod(script, method, code) {
	if (!script.metaClass.respondsTo(script, method)) {
		System.err <<getClass() << ": Broken config file ${options.config}. Cannot find function ${method}()\n";
		System.exit(code);
	}	
}

requiresMethod(script, "connectToSql", 4)

if (options.'test-database') {
	println "${getClass()}: --test-database:"
	println "${getClass()}: calling ${options.config}.connectToSql() to connect to the database..."
	groovy.sql.Sql sql = script.connectToSql()

	String query = "SHOW DATABASES"
	println query
	sql.eachRow(query) {
		println it
	}
	println "OK"
	sql.close()
}

if (options.'create-tables') {
	if (options.run) {
		System.err <<getClass() << ": Cannot combine --create-tables with --run\n"
		System.exit(5)
	}
	println "${getClass()}: --create-tables:"
	println "${getClass()}: calling ${options.config}.connectToSql() to connect to the database..."
	groovy.sql.Sql sql = script.connectToSql()
		
	requiresMethod(script, "createTables", 6)
			
	script.createTables(sql)
	sql.close()
	// Exit the script after creating tables.
	// This is simpler and more reliable. Do not try to start the server at the same time.
	println "OK"
	System.exit(0)
}



if (options.'run') {

	requiresMethod(script, "dataToUpdate", 7)

	int freq
	try {
		freq=options.f ? Integer.parseInt(options.f) : 1000
	} catch (java.lang.NumberFormatException e) {
		freq = -1 // invalidate	
	}
	if (freq <= 0) {
		System.err <<getClass() << ": The option -f requires a positive integer, the number of milliseconds\n"
		System.exit(8)
	}
	println "Will publish updates every ${freq} ms"
	def update = script.dataToUpdate()

   /* if (update["local"]) {
        update["local"].each { 
            k, v -> println "${k}, ${v()}"
        }
    }

	println update["stock"]
	*/
	
	BackendTask task = new BackendTask(update, freq, script.&connectToSql)
	
	// watch the execution in case there is an exception that kills the thread
	BackendWatchdog watchdog = new BackendWatchdog(task)
	println "${task} ${watchdog}"
	while (true) {
		Thread.sleep(1000)
		println "main thread..."
	}
}