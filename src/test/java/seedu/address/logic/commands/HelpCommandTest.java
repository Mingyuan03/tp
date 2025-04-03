package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class HelpCommandTest {
    private final Model model = new ModelManager();
    private final Model expectedModel = new ModelManager();

    @Test
    public void execute_helpDefault_success() {
        CommandResult expectedCommandResult = CommandResult.withHelp(HelpCommand.getGeneralHelpMessage());
        assertCommandSuccess(new HelpCommand(""), model, expectedCommandResult, expectedModel);
        assertCommandSuccess(new HelpCommand("  "), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_helpHelp_success() {
        CommandResult expectedCommandResult = CommandResult.withHelp(
                HelpCommand.COMMAND_WORD + ": " + HelpCommand.MESSAGE_USAGE);
        assertCommandSuccess(new HelpCommand("HELP"), model, expectedCommandResult, expectedModel);
    }
}
