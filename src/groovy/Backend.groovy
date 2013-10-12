import groovy.util.CliBuilder

println "Welcome to the backend"



def cli = new CliBuilder(usage:"groovy ${this.class.name}.groovy [options]")
cli.h(longOpt:'help', 'Print this message')
cli.c(longOpt: 'config', args:1, argName:'config', 'Configuration file, e.g. BackendConfig.groovy')
cli(longOpt: 'create-tables', 'Create tables in the database')
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
	println "Create tables"
	script.createTables(sql)
	return
}

                sql.eachRow("SHOW DATABASES") {

                println it

            }