package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_ROUNDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Set;

import seedu.address.logic.commands.AddJobCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;

/**
 * Parses input arguments and creates a new AddJobCommand object
 */
public class AddJobCommandParser implements Parser<AddJobCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * AddJobCommand and returns an AddJobCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddJobCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS,
                PREFIX_SKILL);
        if (!arePrefixesPresent(argMultimap, PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_JOB_TITLE, PREFIX_JOB_ROUNDS);
        JobTitle title = ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get());
        JobRounds jobRounds = ParserUtil.parseJobRounds(argMultimap.getValue(PREFIX_JOB_ROUNDS).get());
        Set<Skill> skillList = ParserUtil.parseSkills(argMultimap.getAllValues(PREFIX_SKILL));

        Job job = new Job(title, jobRounds, skillList);
        return new AddJobCommand(job);
    }

}
