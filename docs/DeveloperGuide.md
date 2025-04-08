---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TalentMatch Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

### Base Project
* TalentMatch is a brownfield project built upon the foundation provided by [AddressBook Level-3](https://se-education.org/addressbook-level3/).
* The base code, user guide, and developer guides were adapted from the original AB3 project.

### AI Assistance Tools
During the development of this application, the following AI tools were utilized:
* **Cursor** (running Claude Sonnet 3.7) - Assisted with JavaFX UI component development and styling
* **GitHub Copilot** - Provided code completion and suggestions throughout the application

These AI tools primarily contributed to:
* UI components development in JavaFX
* Styling and design improvements
* Code auto-completion across the application

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `del 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("del 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object), all `Job` objects (which are contained in a `UniqueJobList` object) and all `Application` objects (which are contained in a `UniqueApplicationList`).
* stores the currently 'selected' `Person`, `Job`, `Application` objects (e.g., results of a search query) as a separate `stackable filtered_ list` which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Skill` list in the `TalentMatch`, which `Person` references. This allows `TalentMatch` to only require one `Skill` object per unique skill, instead of each `Person` needing their own `Skill` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S2-CS2103T-T08-4/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data, applications manager data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage`, `ApplicationsManagerStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedTalentMatch`. It extends `TalentMatch` with an undo/redo history, stored internally as an `TalentMatchStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedTalentMatch#commit()` — Saves the current address book state in its history.
* `VersionedTalentMatch#undo()` — Restores the previous address book state from its history.
* `VersionedTalentMatch#redo()` — Restores a previously undone address book state from its history.


These operations are exposed in the `Model` interface as `Model#commitTalentMatch()`, `Model#undoTalentMatch()` and `Model#redoTalentMatch()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedTalentMatch` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `del 5` command to delete the 5th person in the address book. The `del` command calls `Model#commitTalentMatch()`, causing the modified state of the address book after the `del 5` command executes to be saved in the `TalentMatchStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitTalentMatch()`, causing another modified address book state to be saved into the `TalentMatchStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitTalentMatch()`, so the address book state will not be saved into the `TalentMatchStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoTalentMatch()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial TalentMatch state, then there are no previous TalentMatch states to restore. The `undo` command uses `Model#canUndoTalentMatch()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoTalentMatch()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `TalentMatchStateList.size() - 1`, pointing to the latest address book state, then there are no undone TalentMatch states to restore. The `redo` command uses `Model#canRedoTalentMatch()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitTalentMatch()`, `Model#undoTalentMatch()` or `Model#redoTalentMatch()`. Thus, the `TalentMatchStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitTalentMatch()`. Since the `currentStatePointer` is not pointing at the end of the `TalentMatchStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* TalentMatch is specifically tailored for recruitment professionals in Small-Medium Enterprises (SMEs) who:
* Manage multiple university candidate pipelines simultaneously
* Need to efficiently track skills, availability, and application statuses
* Value speed and efficiency in their workflow
* Are comfortable with typing-based interfaces
* Prefer lightweight, standalone solutions over complex enterprise HR systems
* Work for SMEs with budget and infrastructure constraints

**Value proposition**:

TalentMatch enables HR recruiters to manage the full recruitment lifecycle more efficiently than typical mouse/GUI driven applications by:

- Managing applicants, jobs, and applications with a streamlined command-line interface that's faster than traditional GUI applications
- Providing specialized views for different recruiting scenarios (Person View, Job View, Application View)
- Supporting multi-dimensional search capabilities across skills, application status, and other candidate attributes
- Enabling efficient tracking of candidates through interview rounds with performance feedback
- Maintaining clear relationships between applicants and the positions they've applied for
- Offering command history functionality to speed up repetitive tasks
- Providing visual indicators of application progress through status tracking
- Allowing focused management of internship applications with appropriate attributes
- Supporting skill-based matching between candidates and job requirements
- Enabling view-specific commands that improve recruiter workflow efficiency

These features combine to provide HR recruiters with a comprehensive tool for managing the entire recruitment process from job creation to final candidate selection.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                 | So that I can…​                                               |
|----------|--------------------------------------------|------------------------------|------------------------------------------------------------------------|
| `* * *`  | HR Recruiter   | add a new applicant          | track his/her application status easily                 |
| `* * *`  | HR Recruiter   | delete a person        | remove his/her entry once their application is rejected       |
| `* * *`  | HR Recruiter   | view all applicants as a list| view a summary of their applications                                   |
| `* * *`  | HR Recruiter   | find an applicant by details | locate details of an applicant without having to go through the entire list |
| `* * *`  | HR recruiter     | search applications by job and person | quickly locate specific applications without browsing through all records |
| `* * *`  | HR recruiter     | have a separate view for jobs | focus on job-specific data when needed |
| `* * *`  | HR recruiter     | search by application status | identify applications at specific stages in the recruitment process |
| `* * *`  | HR recruiter     | search across multiple fields simultaneously | find candidates that match complex criteria |
| `* * *`  | HR recruiter     | view the applications for each person | see all positions a candidate has applied for at once |
| `* *`    | HR Recruiter     | filter applicants            | simplify my search for those who are more suitable for this role       |
| `* *`    | Recruiter        | create an application to link people with roles applied | keep track of the applicants for a specific role |
| `* *`    | HR recruiter     | create role openings  | eventually assign them to applicants and search open roles |
| `* *`    | HR recruiter     | see the role(s) a candidate is applying for | more quickly evaluate if their qualifications align or if such roles are currently available |
| `* *`    | Recruiter        | see an applicant's education background | ensure legitimacy of their application |
| `* *`    | HR recruiter     | find candidates and jobs by skills | match people to positions based on skill requirements |
| `* *`    | HR recruiter     | access my command history | reuse or modify previous commands without retyping them |
| `* *`    | HR recruiter     | track a candidate's progress via interview rounds | record performance feedback and interview dates for each stage |
| `* *`    | HR recruiter     | specify required skills for job postings | clearly communicate position requirements to potential applicants |
| `* *`    | HR recruiter     | edit job fields after creation | update job details as requirements change |
| `* *`    | HR recruiter     | see a visual progress indicator for applications | quickly gauge where each application stands in the process |
| `* *`    | HR recruiter     | view applicants from the job view | see all candidates for a specific position at once |
| `* *`    | HR recruiter     | use view-specific commands | have a more intuitive workflow depending on my current context |
| `* *`    | HR recruiter     | have datetime support for applications | schedule and track interview appointments |
| `* *`    | HR recruiter     | clearly distinguish between applications | avoid confusion when dealing with multiple applications |
| `* *`    | HR recruiter     | focus on internship applications | manage the specific needs of intern recruitment |
| `*`      | HR Recruiter     | sort applicants        | view the top most suitable applicants for the role I'm hiring                 |
| `*`      | HR recruiter     | have a more user-friendly help command | quickly learn how to use the system |
| `*`      | HR recruiter     | preview applicants for a job | get a quick overview without changing views |
| `*`      | HR recruiter     | make JobView the primary view | streamline my workflow as I primarily work with job openings |
| `*`      | HR recruiter     | have applications follow standard behaviors | ensure consistent interaction patterns throughout the system |

### Use cases

(For all use cases below, the **System** is `TalentMatch` and the **Actor** is the `HR manager (HR)`, unless specified otherwise)

**Use case: Remove a person**

**MSS**

1. User requests to list all persons.
2. TalentMatch shows a list of all persons with their indices.
3. User requests to delete a specific person in the list by their index.
4. TalentMatch deletes the person.

     Use case ends.

**Extensions**

* 1a. User is in Job View.
  * 1a1. TalentMatch prompts user to switch to Person View.<br>
  Use case resumes at step 2.

* 2a. The given index is invalid.
  * 2a1. TalentConnect shows an error message.<br>
  Use case resumes at step 3.

---

**Use case: Add a job**

**MSS**

1. User requests to add a job.
2. TalentMatch prompts for details of the job (title, rounds, required skills).
3. User fills in the relevant details.
4. TalentMatch adds the job to the system.
  Use case ends.

**Extensions**

* 3a. User provides invalid job details (missing required fields/ wrong format).
  * 3a1. TalentMatch shows an error message.
  * 3a2. User corrects the details.<br>
  Use case resumes at step 4.

* 3b. TalentMatch finds an existing job with identical job title.
  * 3b1. TalentMatch informs HR of the duplicate.
  * 3b2. HR modifies job title to make it unique.<br>
  Use case resumes at step 4.

---

**Use case: Create an application**

**MSS**

1. User requests to create an application.
2. TalentMatch prompts for the person and job to link.
3. User selects the person index and job index.
4. TalentMatch creates the application linking the person and job
  Use case ends.

**Extensions**

* 3a. The person doesn't exist in the system.
  * 3a1. User adds the person first.<br>
  Use case resumes at step 3.

* 3b. The job doesn't exist in the system.
  * 3b1. User adds the job first.<br>
  Use case resumes at step 3.

* 4a. TalentMatch detects an existing application for the same person and job.
  * 4a1. TalentMatch raises an error to inform user of the duplicate application.<br>
  Use case ends.

---

**Use case: List all jobs**

**MSS**

1. User requests to list all jobs
2. TalentMatch shows a list of job openings<br>
  Use case ends.

**Extensions**

* 1a. User is in Person View.
    * 1a1. TalentMatch prompts user to switch to Job View.<br>
    Use case resumes at step 2.

---

**Use case: Delete an application**

**MSS**

1.  User requests to delete an application.
2.  TalentMatch prompts user for job index and application index.
3.  User selects the job index and application index in the list.
4.  TalentMatch deletes the application.

    Use case ends.

**Extensions**

* 1a. User is in Person View.
    * 1a1. TalentMatch prompts user to switch to Job View.<br>
      Use case resumes at step 2.

* 3a. User provides invalid job index or application index.
    * 3a1. TalentMatch shows an error message.
    * 3a2. User corrects the index.<br>
      Use case resumes at step 4.

---

**Use case: Find a job**

**MSS**

1. User requests to list jobs.
2. TalentMatch shows a list of jobs.
3. User requests to find a job by a keyword.
4. TalentMatch filters and list any jobs that contain the full keyword.

   Use case ends

**Extensions**

* 1a. User is in Person View.
    * 1a1. TalentMatch prompts user to switch to Job View.<br>
      Use case resumes at step 2.

* 3a. User mistypes the keyword.
  * 3a1. TalentMatch returns the wrong list/ empty list of jobs.
  * 3a2. User refreshes the list of jobs.<br>
  Use case resumes at step 3.

---

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 500 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should respond to any user command within 2 seconds.
5.  Should be usable by a person who has never used a contact management system before with minimal training.
6.  User data should be stored in a human-editable json file for easy backup and transfer.
7.  The system should be able to recover from unexpected shutdowns without data loss.

### Glossary

* **Mainstream OS**: Windows, Linux, MacOS
* **HR Recruiter**: Main user of TalentMatch, someone who manages the application timeline of applicants
* **Applicant**: A prospective university student who is applying for a job opening.

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder
    1. Double-click the jar file<br>
       Expected: Shows the GUI with a set of sample data. The window size is adjustable.

2. Saving window preferences

    1. Resize the window to an optimum height and width. Move the window to a different location. Close the window.
    1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Shutdown
    1. Close the application window<br>
       Expected: Application closes and all data is saved.

### Person Management

1. Adding a person

    1. Prerequisites: List all persons using the `list` command.
    1. Test case: `add n/John Doe p/98765432 e/john@example.com a/123 Jurong Street s/NUS d/CS`<br>
       Expected: Person is added to the list. Details of the added person shown in the status message.
    1. Test case: `add n/John Doe`<br>
       Expected: No person is added. Error details shown in the status message.
    1. Other incorrect add commands to try: `add`, `add n/`, `add p/12345678`<br>
       Expected: Similar to previous.

2. Deleting a person

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.
    1. Test case: `del 1`<br>
       Expected: First person is deleted from the list. Details of the deleted person shown in the status message.
    1. Test case: `del 0`<br>
       Expected: No person is deleted. Error details shown in the status message.
    1. Other incorrect delete commands to try: `delete`, `del x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

3. Finding a person

    1. Prerequisites: List all persons using the `list` command.
    1. Test case: `find John`<br>
       Expected: Shows a list of persons with names containing "John".
    1. Test case: `find 98765432`<br>
       Expected: Shows a list of persons with phone numbers containing "98765432".
    1. Test case: `find john@example.com`<br>
       Expected: Shows a list of persons with emails containing "john@example.com".

### Job Management

1. Adding a job

    1. Prerequisites: List all jobs using the `listjob` command.
    1. Test case: `addjob jt/Software Engineer jr/3 k/Java k/Python`<br>
       Expected: Job is added to the list. Details of the added job shown in the status message.
    1. Test case: `addjob t/Software Engineer`<br>
       Expected: No job is added. Error details shown in the status message.
    1. Other incorrect addjob commands to try: `addjob`, `addjob t/`, `addjob s/Java`<br>
       Expected: Similar to previous.

2. Deleting a job

    1. Prerequisites: List all jobs using the `listjob` command. Multiple jobs in the list.
    1. Test case: `deljob 1`<br>
       Expected: First job is deleted from the list. Details of the deleted job shown in the status message.
    1. Test case: `deljob 0`<br>
       Expected: No job is deleted. Error details shown in the status message.
    1. Other incorrect deletejob commands to try: `deletejob`, `deljob x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

3. Finding a job

    1. Prerequisites: List all jobs using the `listjob` command.
    1. Test case: `findjob Software`<br>
       Expected: Shows a list of jobs with titles containing "Software".
    1. Test case: `findjob Java`<br>
       Expected: Shows a list of jobs requiring the skill "Java".

### Application Management

1. Creating an application

    1. Prerequisites: List all persons and jobs using the `list` and `listjob` commands.
    1. Test case: `addapp ij/1 ip/1`<br>
       Expected: Application is created linking the first person and first job. Details shown in the status message.
    1. Test case: `addapp ij/0 ip/1`<br>
       Expected: No application is created. Error details shown in the status message.
    1. Other incorrect apply commands to try: `addapp`, `apply ij/x ip/y`, `...` (where x or y is larger than the list size)<br>
       Expected: Similar to previous.

2. Deleting an application

    1. Prerequisites: List all jobs using the `listjob` command. Each job card contains multiple applications in the list.
    1. Test case: `delapp ij/1 ia/1`<br>
       Expected: First application for the first job is deleted from the list. Details of the deleted application shown in the status message.
    1. Test case: `delapp ij/0 ia/1`<br>
       Expected: No application is deleted. Error details shown in the status message.
    1. Other incorrect deleteapp commands to try: `delapp`, `delapp ij/x ia/y`, `...` (where x is larger than the list size and y is larger than the number of applications for the said job)<br>
       Expected: Similar to previous.

3. Finding applications

    1. Prerequisites: List all jobs using the `listjob` command.
    1. Test case: `findapp as/3`<br>
       Expected: Shows a list of job cards applications corresponding to the application status queried.
    1. Test case: `findapp as/1`<br>
       Expected: Shows a list of applications with application status of 1.
    1. Test case: `findapp ij/1 as/1`<br>
       Expected: Shows a list of applications with application status of 1 for job at index 1.

### View Management

1. Switching views

    1. Test case: `switch`<br>
       Expected: Switches to person view when in job view, showing the person list panel.
    1. Test case: `switch`<br>
       Expected: Switches to job view when in person view, showing the job list panel.

### Saving data

1. Dealing with missing/corrupted data files

    1. Delete the `data/addressbook.json` file<br>
       Expected: Application starts with a new default address book and default applications manager.
    1. Delete the `data/applicationsmanager.json` file<br>
       Expected: Application starts with a new empty applications manager and address book stays the same.
    1. Corrupt the `data/addressbook.json` file by adding invalid JSON<br>
       Expected: Application starts with a new default address book and default applications manager.
    1. Corrupt the `data/applicationsmanager.json` file by adding invalid JSON<br>
       Expected: Application starts with a new empty applications manager and address book stays the same.

### Help Command

1. Accessing help

    1. Test case: `help`<br>
       Expected: Opens the help window showing command usage information.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team Size: 4**

1. **Improve duplicate person and job detection:**
   Currently, duplicate detection only uses the `Person.name` and `Job.jobTitle` fields for comparison in the `Person` and `Job` classes respectively, which can inconvenience users wanting to add multiple contacts with the same name. It also results in issues like multiple people being able to be added to the address book with different names but the same contact details, which may not reflect a real-world scenario faithfully. We plan to extend duplicate detection to become more robust, comparing multiple fields (such as names, phone numbers, emails etc.), only considering a contact duplicate if multiple fields match. This logic is non-trivial.

2. **Fix multiple window display issue:**
   When using multiple screens, if users move the application to a secondary screen, and later switch to using only the primary screen, the GUI opens off-screen. We plan to implement logic to detect and remedy this scenario by ensuring the application window always appears within the bounds of available screens, eliminating the need for users to manually delete the `preferences.json` file.

3. **Implement intelligent case sensitivity handling:**
   Currently, the application has rigid case sensitivity rules that don't always align with user expectations. We plan to improve this by:
   - Making non-critical fields (like descriptive text) case-insensitive for better usability
   - Maintaining case sensitivity where it provides important distinctions (e.g., usernames, passwords)
   - Adding warning prompts when users attempt actions that might create confusion due to case similarity
   - Providing users with options to resolve potential conflicts caused by case differences
   - Offering clearer feedback about case requirements for different fields
   This approach will reduce friction while allowing users to make informed decisions about potential ambiguities.

4. **Enhance viewport management:**
   Parts of the application may be cut off at smaller window sizes, including the default size at first startup. We plan to improve dynamic UI resizing to properly handle different window sizes and ensure all UI elements remain accessible.

5. **Improve message display formatting:**
   Some success and error messages are too long and become hard to read in the current interface. We plan to implement better message formatting with line breaks, improved layout, and possibly expandable/collapsible message areas for detailed information.

6. **Improve data recovery for corrupted storage files:**
   Currently, when storage files (`applicationsmanager.json` and `addressbook.json`) are detected to have corruption or incorrect formatting, the application wipes all data and starts with a clean slate, resulting in complete data loss even from minor formatting errors. We plan to implement a more robust data recovery mechanism that attempts to salvage uncorrupted portions of the files, creates automatic backups before wiping data, and provides users with options to restore from previous states rather than immediately discarding all data.

7. **Improve command parsing robustness:**
   The current command parsing is sensitive to case and strict validation rules, causing unexpected errors when these rules aren't followed. We plan to make the command parser more robust by implementing flexible case handling, improved error detection, and more intuitive validation with better feedback to users.
