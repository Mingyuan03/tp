package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Switches the view between person and job.
 */
public class SwitchViewCommand extends Command {
    public static final String COMMAND_WORD = "switchview";

    @Override
    public CommandResult execute(Model model) {
        // Toggle the model's view state
        model.toggleJobView();

        return new CommandResult("Switched view", false, false, true);
    }
}
