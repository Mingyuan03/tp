package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalApplicationsManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;


/**
 * Contains integration tests (interaction with the Model) for
 * {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), getTypicalApplicationsManager(), new UserPrefs());
        // Set the view state to PERSON_VIEW since AddCommand can only be executed in person view
        model.setViewState(Model.ViewState.PERSON_VIEW);
    }

    @Test
    public void execute_newPerson_success() throws CommandException {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), model.getApplicationsManager(), new UserPrefs());
        expectedModel.setViewState(Model.ViewState.PERSON_VIEW);
        expectedModel.addPerson(validPerson);

        // Execute the command
        AddCommand addCommand = new AddCommand(validPerson);
        CommandResult result = addCommand.execute(model);

        // Verify the result directly
        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS,
            Messages.format(validPerson)), result.getFeedbackToUser());
        assertEquals(expectedModel.getAddressBook().getPersonList(),
            model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model, AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
