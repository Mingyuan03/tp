---
  layout: default.md
  title: "TalentMatch DevOps Guide"
  pageNav: 3
---

# TalentMatch DevOps Guide

<!-- * Table of Contents -->
<page-nav-print />

<!-- -------------------------------------------------------------------------------------------------------------------- -->

## Build Automation

TalentMatch uses Gradle for **build automation and dependency management**. **You are recommended to read [this Gradle Tutorial from the se-edu/guides](https://se-education.org/guides/tutorials/gradle.html)**.

### Common Gradle Commands

| Command | Description |
|---------|-------------|
| `./gradlew clean` | Deletes the files created during previous build tasks (e.g., files in the `build` folder) |
| `./gradlew shadowJar` | Creates a fat JAR file in the `build/lib` folder that contains all dependencies |
| `./gradlew run` | Builds and runs the application |
| `./gradlew runShadow` | Builds the application as a fat JAR, and then runs it |
| `./gradlew checkstyleMain` | Runs the code style check for the main code base |
| `./gradlew checkstyleTest` | Runs the code style check for the test code base |
| `./gradlew test` | Runs all tests |
| `./gradlew clean test` | Cleans the project and runs tests |

### TalentMatch-Specific Build Tasks

When developing TalentMatch, the following build sequence is recommended:

1. Run `./gradlew clean` to ensure a clean slate
2. Run `./gradlew checkstyleMain` to verify code style compliance
3. Run `./gradlew test` to ensure all tests pass
4. Run `./gradlew run` to start the application for manual testing

--------------------------------------------------------------------------------------------------------------------

## Continuous Integration (CI)

TalentMatch uses GitHub Actions for CI. The project comes with the necessary GitHub Actions configuration files (in the `.github/workflows` folder). No further setup is required.

### CI Pipeline Overview

![CI Pipeline](images/CIPipeline.png)

The TalentMatch CI pipeline includes:

1. **Compilation check**: Verifies that the code compiles without errors
2. **Test execution**: Runs all tests to ensure functionality
3. **Code style verification**: Ensures adherence to the project's coding standards
4. **Coverage analysis**: Generates code coverage reports

### Code Coverage

As part of CI, TalentMatch uses Codecov to generate coverage reports. When CI runs, it will:

1. Generate code coverage data based on test execution
2. Upload that data to the Codecov website
3. Provide detailed insights into test coverage across the codebase

> **Note**: Because Codecov is known to experience intermittent issues (e.g., report upload failures), the CI is configured to pass even if the Codecov task fails. Therefore, developers should check coverage levels periodically and take corrective actions if coverage falls below desired thresholds.

To enable Codecov for forks of TalentMatch, follow the steps given in [this se-edu guide](https://se-education.org/guides/tutorials/codecov.html).

### Repository-Wide Checks

In addition to running Gradle checks, CI includes repository-wide checks that cover all files in the repository. These checks enforce repository rules such as line ending requirements that are difficult to enforce on development machines.

These checks are implemented as POSIX shell scripts and can only be run on POSIX-compliant operating systems such as macOS and Linux. To run all checks locally on these operating systems, execute the following in the repository root directory:

```bash
./config/travis/run-checks.sh
```

Any warnings or errors will be printed to the console.

#### Adding New Checks

* Checks are implemented as executable `check-*` scripts within the `.github` directory.
* Check scripts should print errors in the format:
  ```
  SEVERITY:FILENAME:LINE: MESSAGE
  ```
  Where:
  * SEVERITY is either ERROR or WARN
  * FILENAME is the path to the file relative to the current directory
  * LINE is the line where the error occurred
  * MESSAGE explains the error
* Check scripts must exit with a non-zero exit code if any errors occur

--------------------------------------------------------------------------------------------------------------------

## Making a Release

Follow these steps to create a new release of TalentMatch:

### Release Checklist

1. Update the version number in [`MainApp.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)
2. Update the user guide to reflect any changes in functionality
3. Generate a fat JAR file using Gradle: `./gradlew shadowJar`
4. Test the JAR file to ensure it works as expected: `java -jar build/libs/talentmatch-x.x.jar`
5. Tag the repo with the version number (e.g., `v0.1`)
6. [Create a new release using GitHub](https://help.github.com/articles/creating-releases/) and upload the JAR file

### Release Notes Guidelines

For each release, include:

* **New Features**: List new functionality added in this release
* **Enhancements**: List improvements to existing features
* **Bug Fixes**: List resolved issues
* **Breaking Changes**: Highlight any changes that break backward compatibility
* **Dependencies**: Note any updates to dependencies

### Deployment Considerations

When deploying TalentMatch to production environments:

1. Configure appropriate logging levels in `config.json` (see the [Configuration Guide](Configuration.md))
2. Ensure data files are backed up before upgrading
3. Test the application with a sample of real user data
4. Consider providing a data migration path if the data format has changed
