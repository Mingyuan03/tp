package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Displays detailed information about a specific job.
 */
public class ViewJobCommand extends Command {

    public static final String COMMAND_WORD = "viewjob";

    public static final String MESSAGE_SUCCESS = "Viewing job at index: %1$d";
    public static final String MESSAGE_INVALID_JOB_INDEX = "The job index provided is invalid";
    public static final String MESSAGE_NOT_IN_JOB_VIEW = "Please switch to job view first using 'switchview' command.";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Views the detailed information of a job. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index targetIndex;

    public ViewJobCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Check if in an appropriate view that has job listings
        if (model.getCurrentViewState() == Model.ViewState.PERSON_VIEW) {
            throw new CommandException(MESSAGE_NOT_IN_JOB_VIEW);
        }

        if (targetIndex.getZeroBased() >= model.getFilteredJobList().size()) {
            throw new CommandException(MESSAGE_INVALID_JOB_INDEX);
        }

        model.setViewState(Model.ViewState.JOB_DETAIL_VIEW);

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, targetIndex.getOneBased()),
                false,
                false,
                false,
                true,
                false,
                targetIndex.getZeroBased(),
                -1);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewJobCommand // instanceof handles nulls
                && targetIndex.equals(((ViewJobCommand) other).targetIndex)); // state check
    }
}
