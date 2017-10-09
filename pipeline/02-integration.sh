#!/usr/bin/env bash

# Waking up herokuapp
curl $BACKOFFICE_URL > /dev/null

gradle integrationtest
gradle bintrayUpload
# ARTIFACTS:
# ARTIFACT build/libs
#
# ENVIRONMENT VARIABLES:
#   BACKOFFICE_URL https://backoffice-qa.kushkipagos.com/