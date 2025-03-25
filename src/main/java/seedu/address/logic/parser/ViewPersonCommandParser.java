package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ViewPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ViewPersonCommand object
 */
public class ViewPersonCommandParser implements Parser<ViewPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewPersonCommand
     * and returns a ViewPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewPersonCommand parse(String args) throws ParseException {
        try {
            String trimmedArgs = args.trim();
            String[] indices = trimmedArgs.split("\\s+");
            
            if (indices.length != 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                        ViewPersonCommand.MESSAGE_USAGE));
            }
            
            Index jobIndex = ParserUtil.parseIndex(indices[0]);
            Index personIndex = ParserUtil.parseIndex(indices[1]);
            
            return new ViewPersonCommand(jobIndex, personIndex);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewPersonCommand.MESSAGE_USAGE), pe);
        }
    }
} 