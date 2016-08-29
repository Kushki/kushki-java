#!/usr/bin/env bash

set -e

artifact_version=$(gradle --quiet printVersion)
target_dir="docs/$artifact_version"

gradle clean javadoc
git checkout -b gh-pages
mkdir -p "$target_dir"
cp -r build/docs/javadoc/* "$target_dir"
git commit -m "Update docs for v$artifact_version"
git push origin gh-pages
