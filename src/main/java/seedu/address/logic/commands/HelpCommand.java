package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliCommands;
import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display by default, or a specific help instruction.
 */
public class HelpCommand extends Command {
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = "Displays all command words and formats by default, or "
            + "the specific valid command word and format.\n"
            + "Default example: " + COMMAND_WORD + "\nSpecific example: " + COMMAND_WORD + " " + COMMAND_WORD + "\n";
    public static final String MESSAGE_INVALID_COMMAND = "This command is invalid. Try displaying all help instructions"
            + " to see the list of valid commands!";
    private final String targetCommandWord;

    public HelpCommand(String targetCommandWord) {
        this.targetCommandWord = targetCommandWord.trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // 1st guard condition below: No specific command word parsed, default to displaying all help instructions.
        if (Objects.equals(this.targetCommandWord, "")) {
            return CommandResult.withHelp(getGeneralHelpMessage());
        }
        // 2nd guard condition below: Invalid specific command word parsed.
        CliCommands targetCommand = CliCommands.fromCommandWord(this.targetCommandWord);
        if (targetCommand == null) {
            throw new CommandException(MESSAGE_INVALID_COMMAND);
        }
        // Apply main logic of displaying the help instruction for the specific valid command word parsed.
        return CommandResult.withHelp(
                targetCommand.getCommandWord() + ": " + targetCommand.getMessageUsage());
    }

    public static String getGeneralHelpMessage() {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        for (CliCommands command : CliCommands.values()) {
            if (command.equals(CliCommands.ADD)) {
                sb.append("Person-specific Commands below:\n");
            } else if (command.equals(CliCommands.ADDJOB)) {
                sb.append("\nJob-specific Commands below:\n");
            } else if (command.equals(CliCommands.ADDAPP)) {
                sb.append("\nApplication-specific Commands below:\n");
            } else if (command.equals(CliCommands.VIEWPERSON)) {
                sb.append("\nView-toggling Commands below:\n");
            } else if (command.equals(CliCommands.CLEAR)) {
                sb.append("\nMiscellaneous Commands below:\n");
            }
            sb.append(command.getCommandWord()).append(": ").append(command.getMessageUsage()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls. Simplify if-else as all HelpCommand instances lack state thus they are equal.
        return other instanceof HelpCommand;
    }
}
