import com.sergeyorshanskiy.domain.DataPoint
import java.text.*

int x=2
int y=3
double z=1.5
println "Hello, world!" + z

println args

for (p in args) {
    println p
}

def cli = new CliBuilder(usage: 'Test.groovy -[abcde] [date] [prefix]')
cli.with {
  h longOpt: 'help', 'Show usage information'
  f longOpt: 'frequency', args: 1, argName: 'freq', 'Frequency of data'
}

def options = 
  cli.parse(args)
assert options // would be null on failure
//if (!options) {
//  return 
//}

if (options.h) {
  cli.usage()
  return
}

println 'options.frequency: '+ options.frequency
println 'extra arguments: ' + options.arguments()