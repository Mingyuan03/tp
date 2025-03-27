package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code AddApplicationCommand}, knowing
 * AMY, BOB are persons for manual additions thus lacking any application even in {@code getTypicalApplicationsManager}.
 * Also note that accessing 1st application of {@code UniqueApplicationList} implicitly sets the person index of that
 * application's applicant to {@code INDEX_FIRST_PERSON} and job index of that application's job to
 * {@code INDEX_FIRST_PERSON}. This doesn't necessarily hold from 2nd application onwards.
 */
public class AddApplicationCommandTest {
    private final Model model = new ModelManager(
            getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
    private final ApplicationStatus applicationStatusAdded = ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS;
    private final Application applicationToAdd = new Application(
            this.model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
            this.model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased()),
            this.applicationStatusAdded
    );

    @BeforeEach
    public void setUp() {
        if (this.model.hasApplication(this.applicationToAdd)) {
            // This code avoids duplicate application. It is necessary in order to test adding application by indices
            // wherein the person at first index and job at first index MUST NOT have an application.
            // No cyclic dependency exists since DeleteApplicationCommandTest doesn't rely on AddApplicationCommandTest.
            this.model.deleteApplication(this.applicationToAdd);
        }
    }

    /**
     * Tests constructing via valid person index and job index parsed as such in {@code AddApplicationCommandParser} in
     * multiple dispatch.
     */
    @Test
    public void execute_validIndexesUnfilteredList_success() {
        // Test command instantiated from indices.
        AddApplicationCommand addApplicationCommand = new AddApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, this.applicationStatusAdded); // CRUX
        // Construct expected model to base off the test. Beware Application lacks multiple dispatch vs AddApplication.
        Person applicant = this.model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Job job = this.model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased());
        Application expectedApplication = new Application(applicant, job, this.applicationStatusAdded);
        assertEquals(expectedApplication, this.applicationToAdd);
        String expectedMessage = String.format(AddApplicationCommand.MESSAGE_SUCCESS, expectedApplication);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        assert !expectedModel.getApplicationsManager().getApplicationList().contains(expectedApplication)
                : "Application should not already exist before addition.";
        expectedModel.addApplication(expectedApplication);
        assertCommandSuccess(addApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    /**
     * Tests constructing via valid person phone number and job title executes successfully in multiple dispatch.
     */
    @Test
    public void execute_validApplicationUnfilteredList_success() {
        // Test command instantiated from unique identifiers.
        AddApplicationCommand addApplicationCommand = new AddApplicationCommand(this.applicationToAdd); // CRUX
        // Construct expected model to base off the test. Application lacks multiple dispatch vs AddApplication.
        String expectedMessage = String.format(AddApplicationCommand.MESSAGE_SUCCESS, this.applicationToAdd);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        assert !expectedModel.hasApplication(this.applicationToAdd)
                : "Application should not already exist before addition.";
        expectedModel.addApplication(this.applicationToAdd);
        assertCommandSuccess(addApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundPersonIndex = Index.fromOneBased(this.model.getFilteredPersonList().size() + 1);
        AddApplicationCommand addApplicationCommand = new AddApplicationCommand(
                outOfBoundPersonIndex, INDEX_FIRST_PERSON, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        assertCommandFailure(addApplicationCommand, this.model, AddApplicationCommand.MESSAGE_INVALID_PERSON);
    }

    @Test
    public void execute_invalidJobIndex_throwsCommandException() {
        Index outOfBoundJobIndex = Index.fromOneBased(model.getFilteredJobList().size() + 1);
        AddApplicationCommand addApplicationCommand = new AddApplicationCommand(
                INDEX_FIRST_PERSON, outOfBoundJobIndex, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        assertCommandFailure(addApplicationCommand, this.model, AddApplicationCommand.MESSAGE_INVALID_JOB);
    }

    @Test
    public void execute_duplicateApplication_throwsCommandException() {
        assert !this.model.getApplicationsManager().getApplicationList().contains(this.applicationToAdd)
                : "Application should not already exist before addition.";
        this.model.addApplication(this.applicationToAdd); // Adds 1 application first successfully.
        AddApplicationCommand addApplicationCommand = new AddApplicationCommand(this.applicationToAdd); // Test repeat.
        assertCommandFailure(addApplicationCommand, this.model, AddApplicationCommand.MESSAGE_DUPLICATE_APPLICATION);
    }

    /**
     * Both applications may exist as it tests solely for equality. Equality in multiple dispatch is tested above.
     */
    @Test
    public void equals() {
        AddApplicationCommand addFirstApplicationCommand = new AddApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        AddApplicationCommand addSecondApplicationCommand = new AddApplicationCommand(
                INDEX_SECOND_PERSON, INDEX_SECOND_PERSON, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        // Test 1: Same object -> returns true.
        assertEquals(addFirstApplicationCommand, addFirstApplicationCommand);
        // Test 2: Same values -> returns true.
        AddApplicationCommand addFirstApplicationCommandCopy = new AddApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        assertEquals(addFirstApplicationCommand, addFirstApplicationCommandCopy);
        // Test 3: Different application status -> returns false. This highlights the transitivity of dummy applications
        // which must not be exposed (i.e. encapsulation must not leak).
        AddApplicationCommand addFirstDummyApplicationCommand = new AddApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, ApplicationStatus.DEFAULT_ADVANCEAPPLICATIONSTATUS);
        assertNotEquals(addFirstApplicationCommand, addFirstDummyApplicationCommand);
        // Test 4: Different types -> returns false.
        assertNotEquals(new Object(), addFirstApplicationCommand);
        assertNotEquals(null, addFirstApplicationCommand);
        // Test 5: Different commands -> returns false.
        assertNotEquals(addFirstApplicationCommand, addSecondApplicationCommand);
    }
}
