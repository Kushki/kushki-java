#!/usr/bin/env bash

gradle integrationtest
if [ "$CI_BRANCH" != "master" ] && [ "$CI_BRANCH" != "develop" ]  && [ "$CI_PULL_REQUEST" != "" ] ; then
gradlew sonarqube -Dsonar.host.url=https://sonarqube.kushkipagos.com -Dsonar.login=376f1aab337d57217e07b5e703b08f1e549340f7 -Dsonar.github.pullRequest=$CI_PULL_REQUEST -Dsonar.github.repository=$CI_REPO_NAME -Dsonar.github.oauth=1e82a599deb3f8895fc7441bfdb6d5732907f510 -Dsonar.analysis.mode=preview  -Dsonar.branch.name=$CI_BRANCH  -Dsonar.branch.target=develope
fi
