package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliCommands.isValidCommandWord;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code HelpCommand} object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {
    @Override
    public HelpCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new HelpCommand(null); // Show all commands by default.
        }
        // 1st guard condition below: Exists >1 potential command words.
        String[] words = trimmedArgs.split("\\s+");
        if (words.length > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        String commandWord = words[0];
        // 2nd guard condition below: Invalid command word.
        if (!isValidCommandWord(commandWord)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }
        return new HelpCommand(commandWord);
    }
}
