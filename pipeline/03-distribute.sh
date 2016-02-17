#!/usr/bin/env bash
cp build/libs/Kushki.jar out/artifacts/Kushki_jar/Kushki-$SNAP_PIPELINE_COUNTER.jar
git add out/artifacts/Kushki_jar/
git commit -m '[snap-ci] adding latest version of jar, pipeline '$SNAP_PIPELINE_COUNTER
git config --global push.default simple
git pull --rebase
git push