#!/usr/bin/env bash

# Waking up herokuapp
curl $BACKOFFICE_URL > /dev/null
printenv
gradle integrationtest
gradle clean bintrayUpload
# ARTIFACTS:
# ARTIFACT build/libs
#
# ENVIRONMENT VARIABLES:
#   BACKOFFICE_URL https://backoffice-qa.kushkipagos.com/