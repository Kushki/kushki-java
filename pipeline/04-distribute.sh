#!/usr/bin/env bash

#CI_BRANCH

#if [ "$CI_BRANCH" == "DEVELOPE" ]; then
gradle  clean bintrayUpload
#fi

# ENVIRONMENT VARIABLES:
# STAGE
#  BINTRAY_USER
#  BINTRAY_API_KEY
#  MAVEN_CENTRAL_USER
#  MAVEN_CENTRAL_TOKEN
