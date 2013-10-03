includeTargets << grailsScript("_GrailsArgParsing") // grailsScript("_GrailsInit")

target(main: "The description of the script goes here!") {
    println argsMap
    for (p in argsMap['params']) 
      println p
}

setDefaultTarget(main)
