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
 * Contains integration tests (interaction with the Model) and unit tests for {@code AddApplicationCommand}, noting that
 * accessing 1st application of {@code UniqueApplicationList} implicitly sets the person index of that application's
 * applicant to {@code INDEX_FIRST_PERSON} and job index of that application's job to {@code INDEX_FIRST_PERSON}. This
 * doesn't necessarily hold from 2nd application onwards.
 */
public class AdvanceApplicationCommandTest {
    private final Model model = new ModelManager(
            getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
    private final ApplicationStatus applicationStatusAdvanced = ApplicationStatus.DEFAULT_ADVANCEAPPLICATIONSTATUS;
    private final Application applicationToAdvance = new Application(
            this.model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
            this.model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased()),
            ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS
    );

    /**
     * Ensure {@code applicationToAdvance} starts off on a clean slate at round index 0, especially given
     * {@code Application::contains} doesn't check by any {@code Application}'s fields, potentially failing equality.
     * Rationale for above is contained in {@code Application} class.
     */
    @BeforeEach
    public void setUp() {
        // Ensure applicationToAdvance starts off on a clean slate at round index 0, since Application
        if (this.model.hasApplication(this.applicationToAdvance)) {
            this.model.deleteApplication(this.applicationToAdvance);
        }
        this.model.addApplication(this.applicationToAdvance);
        assertEquals(this.applicationToAdvance, new Application(
                this.model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()),
                this.model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased()),
                ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS));
        assert this.applicationToAdvance.job().jobRounds().jobRounds
                > ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS.applicationStatus
                : "The application to be advanced already is at its final round.";
    }

    @Test
    public void execute_validIndicesUnfilteredList_success() { // CRUX below.
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, this.applicationStatusAdvanced);
        // Construct expected model to base off the test. Application lacks multiple dispatch vs AdvanceApplication.
        Person applicant = this.model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Job job = this.model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased());
        Application expectedApplication = new Application(applicant, job,
                ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        assertEquals(expectedApplication, this.applicationToAdvance);
        String expectedMessage = String.format(AdvanceApplicationCommand.MESSAGE_SUCCESS, expectedApplication,
                this.applicationStatusAdvanced);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        assert expectedModel.hasApplication(expectedApplication) : "Application should exist before advancement.";
        expectedModel.advanceApplication(expectedApplication, this.applicationStatusAdvanced.applicationStatus);
        assertCommandSuccess(advanceApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validApplicationUnfilteredList_success() { // CRUX below.
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(this.applicationToAdvance,
                this.applicationStatusAdvanced);
        // Construct expected model to base off the test. Application lacks multiple dispatch vs AdvanceApplication.
        String expectedMessage = String.format(AdvanceApplicationCommand.MESSAGE_SUCCESS, this.applicationToAdvance,
                this.applicationStatusAdvanced);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        assert expectedModel.hasApplication(this.applicationToAdvance) : "Application should exist before advancement.";
        expectedModel.advanceApplication(this.applicationToAdvance, this.applicationStatusAdvanced.applicationStatus);
        assertCommandSuccess(advanceApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(
                outOfBoundPersonIndex, INDEX_FIRST_PERSON, this.applicationStatusAdvanced);
        assertCommandFailure(advanceApplicationCommand, this.model, AdvanceApplicationCommand.MESSAGE_INVALID_PERSON);
    }

    @Test
    public void execute_invalidJobIndex_throwsCommandException() {
        Index outOfBoundJobIndex = Index.fromOneBased(model.getFilteredJobList().size() + 1);
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, outOfBoundJobIndex, this.applicationStatusAdvanced);
        assertCommandFailure(advanceApplicationCommand, this.model, AdvanceApplicationCommand.MESSAGE_INVALID_JOB);
    }

    @Test
    public void execute_excessApplicationStatusAdvanced_throwsCommandException() {
        ApplicationStatus outOfBoundApplicationStatusAdvanced = new ApplicationStatus(
                this.applicationToAdvance.job().jobRounds().jobRounds + 1);
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, outOfBoundApplicationStatusAdvanced);
        assertCommandFailure(advanceApplicationCommand, this.model, AdvanceApplicationCommand.MESSAGE_FULL_APPLICATION);
    }

    @Test
    public void execute_zeroApplicationStatusAdvanced_throwsCommandException() {
        ApplicationStatus zeroApplicationStatusAdvanced = ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS;
        AdvanceApplicationCommand advanceApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, zeroApplicationStatusAdvanced);
        assertCommandFailure(advanceApplicationCommand, this.model,
                AdvanceApplicationCommand.MESSAGE_INVALID_APPLICATION_STATUS_ADVANCED);
    }

    /**
     * Both applications need not exist as it tests only for equality. Equality in multiple dispatch is tested above.
     */
    @Test
    public void equals() {
        AdvanceApplicationCommand advanceFirstApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, this.applicationStatusAdvanced);
        AdvanceApplicationCommand advanceSecondApplicationCommand = new AdvanceApplicationCommand(
                INDEX_SECOND_PERSON, INDEX_SECOND_PERSON, this.applicationStatusAdvanced);
        // Test 1: Same object -> returns true.
        assertEquals(advanceFirstApplicationCommand, advanceFirstApplicationCommand);
        // Test 2: Same values -> returns true.
        AdvanceApplicationCommand advanceFirstApplicationCommandCopy = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, this.applicationStatusAdvanced);
        assertEquals(advanceFirstApplicationCommand, advanceFirstApplicationCommandCopy);
        // Test 3: Different application status advanced -> returns false.
        AdvanceApplicationCommand advanceFirstDummyApplicationCommand = new AdvanceApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS);
        assertNotEquals(advanceFirstApplicationCommand, advanceFirstDummyApplicationCommand);
        // Test 4: Different types -> returns false.
        assertNotEquals(new Object(), advanceFirstApplicationCommand);
        assertNotEquals(null, advanceFirstApplicationCommand);
        // Test 5: Different commands -> returns false.
        assertNotEquals(advanceFirstApplicationCommand, advanceSecondApplicationCommand);
    }
}
