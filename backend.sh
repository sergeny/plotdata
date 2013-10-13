#!/bin/bash

# Note that $* is before -c because so that the user can pass another config file to this script
# Backend.groovy will use the first -c argument specified, not the last one
groovy -classpath src/groovy/ src/groovy/Backend.groovy $* -c BackendConfig_development.groovy
