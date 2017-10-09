#!/usr/bin/env bash

artifact_version=$(gradle --quiet printVersion)

#git tag --annotate "v$artifact_version" -m "Release for version $artifact_version"
#git push --tags
#CI_BRANCH

#if [ "$CI_BRANCH" == "DEVELOPE" ]; then
gradle  bintrayUpload
#fi

# ENVIRONMENT VARIABLES:
# STAGE
#  BINTRAY_USER
#  BINTRAY_API_KEY
#  MAVEN_CENTRAL_USER
#  MAVEN_CENTRAL_TOKEN
