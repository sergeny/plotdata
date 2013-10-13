import static.java.util.concurrent.TimeUnit.*
import groovy.util.CliBuilder


println "Welcome to the backend"



def cli = new CliBuilder(usage:"groovy ${this.class.name}.groovy [options]")
cli.h(longOpt:'help', 'Print this message')
cli.c(longOpt: 'config', args:1, argName:'config', 'Configuration file, e.g. BackendConfig.groovy')
cli.f(longOpt: 'freq', args:1, argName:'freq', 'Frequency: will post updates each that many ms' )
cli._(longOpt: 'create-tables', 'Create tables in the database')
cli._(longOpt: 'run', 'Run the backend server')
def options = cli.parse(args)
assert options // would be null (false) on failure
if (options.h) {
	cli.usage()
	return
}
if (!options.c) {
	System.err <<"Error: Need to specify a configuration file\n"
	cli.usage()
	System.exit(1)
}
println "Config: ${options.config}"

GroovyShell shell = new GroovyShell()
def script = shell.parse(new File(options.config))
def sql = script.connectToSql()

if (options.'create-tables') {
	if (options.run) {
		System.err << "Error: Cannot combine --create-tables with --run\n"
		System.exit(2)
	}
	println "Create tables"
	script.createTables(sql)
	// Exit the script after creating tables.
	// This is simpler and more reliable. Do not try to start the server at the same time.
	return
}

            sql.eachRow("SHOW DATABASES") {

                println it

            }
sql.close()

if (options.'run') {
	println 'Starting the backend server...'
	int freq
	try {
		freq=options.f ? Integer.parseInt(options.f) : 1000
	} catch (java.lang.NumberFormatException e) {
		freq = -1 // invalidate	
	}
	if (freq <= 0) {
		System.err <<"Error: The option -f requires a positive integer, the number of milliseconds\n"
		System.exit(3)
	}
	println "Will publish updates every ${freq} ms"
	def update = script.dataToUpdate()

    if (update["local"]) {
        update["local"].each { 
            k, v -> println "${k}, ${v()}"
        }
    }

	println update["stock"]
	
	BackendTask task = new BackendTask(update, freq, script.&connectToSql)
	
	// watch the execution in case there is an exception that kills the thread
	BackendWatchdog watchdog = new BackendWatchdog(task)
	println "${task} ${watchdog}"
	while (true) {
		Thread.sleep(500)
		println "Total: ${watchdog.getTask().getTotal()}"
	}
}