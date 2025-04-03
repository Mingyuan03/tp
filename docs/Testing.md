---
  layout: default.md
  title: "TalentMatch Testing Guide"
  pageNav: 3
---

# TalentMatch Testing Guide

<!-- * Table of Contents -->
<page-nav-print />

<!-- -------------------------------------------------------------------------------------------------------------------- -->

## Running tests

There are two ways to run tests for TalentMatch.

* **Method 1: Using IntelliJ JUnit test runner**
  * To run all tests, right-click on the `src/test/java` folder and choose `Run 'All Tests'`
  * To run a subset of tests, you can right-click on a test package,
    test class, or a test and choose `Run 'ABC'`
* **Method 2: Using Gradle**
  * Open a console and run the command `gradlew clean test` (Mac/Linux: `./gradlew clean test`)

<box type="info" seamless>

**Link**: Read [this Gradle Tutorial from the se-edu/guides](https://se-education.org/guides/tutorials/gradle.html) to learn more about using Gradle.
</box>

--------------------------------------------------------------------------------------------------------------------

## Types of tests

TalentMatch has three types of tests:

1. *Unit tests* targeting the lowest level methods/classes.<br>
   e.g. `seedu.address.commons.util.StringUtilTest`
1. *Integration tests* that are checking the integration of multiple code units (those code units are assumed to be working).<br>
   e.g. `seedu.address.storage.StorageManagerTest`
1. Hybrids of unit and integration tests. These test are checking multiple code units as well as how they are connected together.<br>
   e.g. `seedu.address.logic.LogicManagerTest`

## Testing TalentMatch Components

### Testing Model Components

When testing TalentMatch's model components, focus on:

* **Person Entity Tests**: Ensure validation for person attributes (name, phone, email, address, school, degree, skills)
* **Job Entity Tests**: Validate job attributes (title, rounds, skills)
* **Application Entity Tests**: Verify application status progression and constraints
* **Model Manager Tests**: Test filtering, command history, and view state management

### Testing Logic Components

For TalentMatch's logic layer, focus on:

* **Command Tests**: Verify each command (add, edit, delete, find, etc.) for all entity types (Person, Job, Application)
* **Parser Tests**: Test command parsing including validation and error handling
* **Integration with Model**: Ensure commands modify the model correctly

### Testing Storage Components

When testing TalentMatch's persistence:

* **Storage Manager Tests**: Test saving and loading both AddressBook and ApplicationsManager data
* **JSON Adapters**: Verify correct serialization/deserialization of all entities
* **Data Integrity**: Ensure relationships between entities are preserved across save/load cycles

### Testing UI Components

UI testing should focus on:

* **Person/Job Card Tests**: Verify correct display of entities
* **List Panel Tests**: Test proper display of lists and filtering
* **Stats Panel Tests**: Validate visualization of job-specific statistics

## Tips for Writing Good Tests

* Write tests for both normal and boundary conditions
* Focus on TalentMatch's core HR recruitment workflows:
  * Adding and filtering candidates
  * Managing job openings
  * Tracking application progress through interview rounds
* Test the interplay between Persons, Jobs, and Applications to ensure proper relationship management
* Structure tests to match TalentMatch's MVC architecture
* Mock dependent components when testing a specific layer
