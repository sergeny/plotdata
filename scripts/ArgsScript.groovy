includeTargets << grailsScript("_GrailsArgParsing") // grailsScript("_GrailsInit")

target(main: "The description of the script goes here!") {
    def list=argsMap['params']
    def host=list[0] ? list[0] : 'localhost'  
    def port=list[1] ? list[1] : '3306'       
    def db=list[2] ? list[2] : ''             // can leave empty
    def user=list[3] ? list[3] : 'root'      
    def pswd=list[4] ? list[4] : 'root'

    println host
    println port
    println db
    println user
    println pswd
    println argsMap
    println argsMap['params'][2]
    for (p in argsMap['params']) 
      println p
}

setDefaultTarget(main)
