package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.JobBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class FindAppCommandTest {

    private Model model;
    private Model expectedModel;
    private Job job;
    private Person person;
    private Application application;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBookBuilder().withPerson(TypicalPersons.ALICE)
                .withPerson(TypicalPersons.BENSON).build(),
                new ApplicationsManager(),
                new UserPrefs());

        expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());

        // Setup model with some applications
        job = new JobBuilder().build();
        person = new PersonBuilder().build();
        application = new Application(person, job, new ApplicationStatus(1));

        model.addJob(job);
        model.addPerson(person);
        model.addApplication(application);

        expectedModel.addJob(job);
        expectedModel.addPerson(person);
        expectedModel.addApplication(application);
    }

    @Test
    public void constructor_withStatus_createsCommand() {
        FindAppCommand command = new FindAppCommand("1");
        assertTrue(command instanceof FindAppCommand);
    }

    @Test
    public void constructor_withJobIndexAndStatus_createsCommand() {
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(1), "1");
        assertTrue(command instanceof FindAppCommand);
    }

    @Test
    public void execute_withStatusInPersonView_throwsCommandException() {
        // Set view states
        model.setViewState(Model.ViewState.PERSON_VIEW);

        // Execute command
        FindAppCommand command = new FindAppCommand("1");

        // The command should throw CommandException when executed in PERSON_VIEW
        assertThrows(CommandException.class, FindAppCommand.MESSAGE_WRONG_VIEW, () -> command.execute(model));
    }

    @Test
    public void execute_inDetailView_clearsView() throws CommandException {
        // Set view states
        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);

        // Configure expected model
        expectedModel.updateFilteredApplicationList(app ->
            app.getApplicationStatus().equals(new ApplicationStatus(1)));

        // Execute command
        FindAppCommand command = new FindAppCommand("1");
        String expectedMessage = String.format(FindAppCommand.MESSAGE_SUCCESS, "1");

        // Execute directly since we need to check multiple result properties
        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertTrue(result.isClearView());
        assertEquals(Model.ViewState.JOB_VIEW, model.getCurrentViewState());
    }


    @Test
    public void execute_withStatusInJobView_success() throws CommandException {
        // Set view states
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);

        // Configure expected model
        expectedModel.updateFilteredApplicationList(app ->
            app.getApplicationStatus().equals(new ApplicationStatus(1)));

        // Execute command
        FindAppCommand command = new FindAppCommand("1");
        String expectedMessage = String.format(FindAppCommand.MESSAGE_SUCCESS, "1");

        // Execute directly since we need to check multiple result properties
        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertTrue(result.isClearView());
    }

    @Test
    public void execute_withJobIndexAndStatus_success() throws CommandException {
        // Set view states
        model.setViewState(Model.ViewState.JOB_VIEW);
        expectedModel.setViewState(Model.ViewState.JOB_VIEW);

        // Configure expected model
        expectedModel.updateFilteredApplicationList(app ->
            app.getApplicationStatus().equals(new ApplicationStatus(1))
            && app.getJob().equals(job));

        // Execute command with job index and status
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(1), "1");
        String expectedMessage = String.format(FindAppCommand.MESSAGE_SUCCESS, "1");

        // Execute directly since we need to check multiple result properties
        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertTrue(result.isRefreshJobView());
        assertTrue(result.isClearView());
    }

    @Test
    public void execute_withInvalidJobIndex_throwsCommandException() {
        // Set view state
        model.setViewState(Model.ViewState.JOB_VIEW);

        // Execute command with invalid job index
        FindAppCommand command = new FindAppCommand(Index.fromOneBased(999), "1");
        assertThrows(CommandException.class, FindAppCommand.MESSAGE_JOB_NOT_FOUND, () -> command.execute(model));
    }

    @Test
    public void equals() {
        FindAppCommand findAppCommand1 = new FindAppCommand("1");
        FindAppCommand findAppCommand2 = new FindAppCommand("1");
        FindAppCommand findAppCommand3 = new FindAppCommand("2");
        FindAppCommand findAppCommand4 = new FindAppCommand(Index.fromOneBased(1), "1");

        // same object -> returns true
        assertTrue(findAppCommand1.equals(findAppCommand1));

        // same values -> returns true
        assertTrue(findAppCommand1.equals(findAppCommand2));

        // different values -> returns false
        assertFalse(findAppCommand1.equals(findAppCommand3));

        // different types -> returns false
        assertFalse(findAppCommand1.equals(findAppCommand4));
    }
}
