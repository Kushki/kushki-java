## CI Pipeline
The pipeline is in SnapCI, the steps are defined in `.sh` files in the `pipeline` folder in the main repository.
These files are executed by each of the steps defined in SnapCI, if environment variables or artifacts have to be
configured in the pipeline, it is mentioned as a comment at the end of the file.

These are the defined steps:

#### `01-unit.sh`
In this step the unit tests are run.

#### `02-artifact.sh`
In this step the `jar` is created and integration tests are run. Here an artifact is defined in SnapCI in the folder
`build/libs`

#### `03-distribute.sh`
**This is a manual step, triggered only after the new features have been approved.**

The `jar` from the previous step is copied to the `out/artifacts/Kushki_jar/` folder using the pipeline number to
identify it. The latest version, `Kushki.jar`, is overwritten with the newly created one. Then these files are
added to Git and pushed.