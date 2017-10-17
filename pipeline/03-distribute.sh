#!/usr/bin/env bash

# Waking up herokuapp
curl $BACKOFFICE_URL > /dev/null
if [ "$CI_BRANCH" == "master" ]; then
gradle clean bintrayUpload
fi
# ARTIFACTS:
# ARTIFACT build/libs
#
# ENVIRONMENT VARIABLES:
#   BACKOFFICE_URL https://backoffice   -qa.kushkipagos.com/