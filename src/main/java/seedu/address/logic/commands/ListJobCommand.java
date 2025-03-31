package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all jobs in the address book to the user.
 */
public class ListJobCommand extends Command {

    public static final String COMMAND_WORD = "listjob";

    public static final String MESSAGE_SUCCESS = "Listed all jobs";
    public static final String MESSAGE_WRONG_VIEW = "This command is only available in job view. "
            + "Please switch to job view first using 'switchview' command.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check that we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }

        model.resetFilteredJobList();
        // Reset application filters instead of applying a predicate
        model.resetFilteredApplicationList();
        return CommandResult.withRefreshJobView(MESSAGE_SUCCESS);
    }
}
