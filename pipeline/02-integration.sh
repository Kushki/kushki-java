#!/usr/bin/env bash

# Waking up herokuapp
curl $BACKOFFICE_URL > /dev/null

gradle integrationtest
gradle -Puser="{$BINTRAY_USER}" -Pkey="{$BINTRAY_API_KEY}" clean bintrayUpload
# ARTIFACTS:
# ARTIFACT build/libs
#
# ENVIRONMENT VARIABLES:
#   BACKOFFICE_URL https://backoffice-qa.kushkipagos.com/