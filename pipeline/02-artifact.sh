#!/usr/bin/env bash
gradle fatJar
gradle integrationtest

# ARTIFACTS:
# ARTIFACT build/libs