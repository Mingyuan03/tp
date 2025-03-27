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
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.job.Job;


public class DeleteJobCommandTest {
    private final Model model = new ModelManager(
            getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Job jobToDelete = model.getFilteredJobList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteJobCommand.MESSAGE_DELETE_JOB_SUCCESS,
                Messages.format(jobToDelete));

        ModelManager expectedModel = new ModelManager(new AddressBook(this.model.getAddressBook()),
                new ApplicationsManager(model.getApplicationsManager()), new UserPrefs());
        expectedModel.deleteJob(jobToDelete);

        assertCommandSuccess(deleteJobCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredJobList().size() + 1);
        DeleteJobCommand deleteJobCommand = new DeleteJobCommand(outOfBoundIndex);

        assertCommandFailure(deleteJobCommand, model, Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteJobCommand deleteJobFirstCommand = new DeleteJobCommand(INDEX_FIRST_PERSON);
        DeleteJobCommand deleteJobSecondCommand = new DeleteJobCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertEquals(deleteJobFirstCommand, deleteJobFirstCommand);

        // same values -> returns true
        DeleteJobCommand deleteJobFirstCommandCopy = new DeleteJobCommand(INDEX_FIRST_PERSON);
        assertEquals(deleteJobFirstCommand, deleteJobFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(new Object(), deleteJobFirstCommand);

        // null -> returns false
        assertNotEquals(null, deleteJobFirstCommand);

        // different person -> returns false
        assertNotEquals(deleteJobFirstCommand, deleteJobSecondCommand);
    }
}
