package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all applications regardless of status.
 * Used to clear the status filter set by FindAppCommand.
 */
public class ListAppCommand extends Command {

    public static final String COMMAND_WORD = "listapp";

    public static final String MESSAGE_SUCCESS = "Listed all applications";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.clearStatusFilter();
        return new CommandResult(MESSAGE_SUCCESS);
    }
} 