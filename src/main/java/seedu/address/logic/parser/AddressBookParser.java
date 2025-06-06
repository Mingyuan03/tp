package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddApplicationCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.commands.AdvanceApplicationCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteApplicationCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteJobCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditJobCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindAppCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindJobCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListJobCommand;
import seedu.address.logic.commands.SwitchViewCommand;
import seedu.address.logic.commands.ViewJobCommand;
import seedu.address.logic.commands.ViewPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        return switch (commandWord) {
        case AddCommand.COMMAND_WORD -> new AddCommandParser().parse(arguments);
        case EditCommand.COMMAND_WORD -> new EditCommandParser().parse(arguments);
        case DeleteCommand.COMMAND_WORD -> new DeleteCommandParser().parse(arguments);
        case ClearCommand.COMMAND_WORD -> new ClearCommand();
        case FindCommand.COMMAND_WORD -> new FindCommandParser().parse(arguments);
        case ListCommand.COMMAND_WORD -> new ListCommand();
        case ExitCommand.COMMAND_WORD -> new ExitCommand();
        case HelpCommand.COMMAND_WORD -> new HelpCommand();
        case AddJobCommand.COMMAND_WORD -> new AddJobCommandParser().parse(arguments);
        case DeleteJobCommand.COMMAND_WORD -> new DeleteJobCommandParser().parse(arguments);
        case SwitchViewCommand.COMMAND_WORD -> new SwitchViewCommand();
        case EditJobCommand.COMMAND_WORD -> new EditJobCommandParser().parse(arguments);
        case FindJobCommand.COMMAND_WORD -> new FindJobCommandParser().parse(arguments);
        case ListJobCommand.COMMAND_WORD -> new ListJobCommand();
        case AddApplicationCommand.COMMAND_WORD -> new AddApplicationCommandParser().parse(arguments);
        case DeleteApplicationCommand.COMMAND_WORD -> new DeleteApplicationCommandParser().parse(arguments);
        case AdvanceApplicationCommand.COMMAND_WORD -> new AdvanceApplicationCommandParser().parse(arguments);
        case ViewJobCommand.COMMAND_WORD -> new ViewJobCommandParser().parse(arguments);
        case ViewPersonCommand.COMMAND_WORD -> new ViewPersonCommandParser().parse(arguments);
        case FindAppCommand.COMMAND_WORD -> new FindAppCommandParser().parse(arguments);
        default -> {
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
        };
    }
}
