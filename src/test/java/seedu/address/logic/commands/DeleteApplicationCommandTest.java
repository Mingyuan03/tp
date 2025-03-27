package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteApplicationCommand}, noting
 * that accessing 1st application of {@code UniqueApplicationList} implicitly sets the person index of that
 * application's applicant to {@code INDEX_FIRST_PERSON} and job index of that application's job to
 * {@code INDEX_FIRST_PERSON}. This doesn't necessarily hold from 2nd application onwards.
 */
public class DeleteApplicationCommandTest {
    private final Model model = new ModelManager(
            getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());

    /**
     * Tests constructing via valid application directly in multiple dispatch.
     */
    @Test
    public void execute_validApplicationUnfilteredList_success() {
        Application applicationToDelete = this.model.getFilteredApplicationList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteApplicationCommand deleteApplicationCommand = new DeleteApplicationCommand(applicationToDelete); // CRUX
        String expectedMessage = String.format(DeleteApplicationCommand.MESSAGE_SUCCESS, applicationToDelete);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        expectedModel.deleteApplication(applicationToDelete);
        assertCommandSuccess(deleteApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    /**
     * Tests constructing via valid person index and job index parsed as such in {@code DeleteApplicationCommandParser}
     * in multiple dispatch.
     */
    @Test
    public void execute_validIndicesUnfilteredList_success() {
        Application applicationToDelete = this.model.getFilteredApplicationList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteApplicationCommand deleteApplicationCommand = new DeleteApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON); // CRUX
        String expectedMessage = String.format(DeleteApplicationCommand.MESSAGE_SUCCESS, applicationToDelete);
        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(this.model.getApplicationsManager()), new UserPrefs());
        expectedModel.deleteApplication(applicationToDelete);
        assertCommandSuccess(deleteApplicationCommand, this.model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundPersonIndex = Index.fromOneBased(this.model.getFilteredPersonList().size() + 1);
        DeleteApplicationCommand deleteApplicationCommand = new DeleteApplicationCommand(
                outOfBoundPersonIndex, INDEX_FIRST_PERSON);
        assertCommandFailure(deleteApplicationCommand, this.model, DeleteApplicationCommand.MESSAGE_INVALID_PERSON);
    }

    @Test
    public void execute_invalidJobIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundJobIndex = Index.fromOneBased(this.model.getFilteredJobList().size() + 1);
        DeleteApplicationCommand deleteApplicationCommand = new DeleteApplicationCommand(
                INDEX_FIRST_PERSON, outOfBoundJobIndex);
        assertCommandFailure(deleteApplicationCommand, this.model, DeleteApplicationCommand.MESSAGE_INVALID_JOB);
    }

    /**
     * Both applications need not exist as it tests only for equality. Equality in multiple dispatch is tested above.
     */
    @Test
    public void equals() {
        DeleteApplicationCommand deleteFirstApplicationCommand = new DeleteApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        DeleteApplicationCommand deleteSecondApplicationCommand = new DeleteApplicationCommand(
                INDEX_SECOND_PERSON, INDEX_SECOND_PERSON);
        // Test 1: Same object -> returns true.
        assertEquals(deleteFirstApplicationCommand, deleteFirstApplicationCommand);
        // Test 2: Same values -> returns true.
        DeleteApplicationCommand deleteFirstApplicationCommandCopy = new DeleteApplicationCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        assertEquals(deleteFirstApplicationCommand, deleteFirstApplicationCommandCopy);
        // Test 3: Different types -> returns false.
        assertNotEquals(new Object(), deleteFirstApplicationCommand);
        assertNotEquals(null, deleteFirstApplicationCommand);
        // Test 4: Different commands -> returns false.
        assertNotEquals(deleteFirstApplicationCommand, deleteSecondApplicationCommand);
    }
}
