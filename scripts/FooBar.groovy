includeTargets << grailsScript("_GrailsInit")

target(main: "Runs a generic script and passes parameters") {
  def myclass = classLoader.loadClass('GenericRunScript')
  myclass.execute(args);
}

setDefaultTarget(main)