package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_WRONG_VIEW = "This command is only available in person view. "
            + "Please switch to person view first using 'switchview' command.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        
        // Check that we're in person view
        if (model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }
        
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return CommandResult.withFeedback(MESSAGE_SUCCESS);
    }
}
