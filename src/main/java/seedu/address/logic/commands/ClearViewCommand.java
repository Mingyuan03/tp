package seedu.address.logic.commands;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Model;

/**
 * Clears the current detailed view and returns to the general overview.
 */
public class ClearViewCommand extends Command {

    public static final String COMMAND_WORD = "clearview";
    public static final String MESSAGE_SUCCESS = "View cleared";
    public static final String MESSAGE_NOT_IN_DETAIL_VIEW = "Not in a detailed view, nothing to clear";

    private static final Logger logger = LogsCenter.getLogger(ClearViewCommand.class);

    @Override
    public CommandResult execute(Model model) {
        // Get current view state
        Model.ViewState currentViewState = model.getCurrentViewState();
        logger.info("Current view state: " + currentViewState);

        // Check if we're in a detailed view
        if (Model.ViewState.JOB_DETAIL_VIEW.equals(currentViewState)
                || Model.ViewState.PERSON_DETAIL_VIEW.equals(currentViewState)) {
            // Reset to regular job view
            model.setViewState(Model.ViewState.JOB_VIEW);
            logger.info("View cleared, resetting to JOB_VIEW");
            return new CommandResult(MESSAGE_SUCCESS, true);
        }

        // If already in general view, just acknowledge
        logger.info("Not in a detailed view, no action taken");
        return new CommandResult(MESSAGE_NOT_IN_DETAIL_VIEW);
    }
}
