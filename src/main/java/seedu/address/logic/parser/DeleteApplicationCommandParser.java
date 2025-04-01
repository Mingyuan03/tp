package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DeleteApplicationCommand} object, enabling multiple dispatch by
 * parsing exactly 1 of (jobIndex, applicationIndex) or (personIndex, jobIndex), but not both or neither.
 */
public class DeleteApplicationCommandParser implements Parser<DeleteApplicationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code DeleteApplicationCommand}
     * and returns a {@code DeleteApplicationCommand} object for execution.
     * @throws ParseException If the user input does not conform the expected format.
     */
    public DeleteApplicationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);
        // 1st guard condition below: empty job index or trailing whitespace exists before 1st valid prefix
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_INDEX) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteApplicationCommand.MESSAGE_USAGE));
        }
        // 2nd guard condition below: exactly one of (application index, person index) must be present
        if (arePrefixesPresent(argMultimap, PREFIX_PERSON_INDEX)
            == arePrefixesPresent(argMultimap, PREFIX_APPLICATION_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteApplicationCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX, PREFIX_APPLICATION_INDEX);
        try {
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            if (arePrefixesPresent(argMultimap, PREFIX_APPLICATION_INDEX)) {
                // Parsing by (jobIndex, applicationIndex). Boolean isOnlyJobView set to true.
                Index appByJobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPLICATION_INDEX).get());
                return new DeleteApplicationCommand(jobIndex, appByJobIndex, true);
            } else {
                // Parsing by (personIndex, jobIndex). Boolean isOnlyJobView set to false.
                Index personIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PERSON_INDEX).get());
                return new DeleteApplicationCommand(jobIndex, personIndex, false);
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteApplicationCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values
     * in the given {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
