#!/bin/bash

groovy -classpath src/groovy/ src/groovy/Backend.groovy -c BackendConfig_development.groovy $*
