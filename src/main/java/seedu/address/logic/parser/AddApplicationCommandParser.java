package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddApplicationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobAddress;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;

/**
 * Parses input arguments and creates a new {@code AddApplicationCommand} object either with dummy Person and Job later
 * updated xor with indices in last-accessed {@code FilteredPersonList} and {@code FilteredJobList} in GUI, both in
 * {@code AddApplicationCommand::execute}.
 */
public class AddApplicationCommandParser implements Parser<AddApplicationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddApplicationCommand}
     * and returns an {@code AddApplicationCommand} object for execution.
     * @param args Raw user input comprising 2 mandatory unique prefix identifiers, either {@code PREFIX_PHONE} and
     *            {@code PREFIX_JOB_TITLE} or {@code PREFIX_PERSON_INDEX} and {@code PREFIX_JOB_INDEX} to tokenize.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public AddApplicationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX,
                PREFIX_PHONE, PREFIX_JOB_TITLE, PREFIX_APPLICATION_STATUS);
        // Require min 1 pair of (person index, job index) or (phone, job title) to be present, PRIORITISING the latter.
        if (!(arePrefixesPresent(argMultimap, PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX)
                || arePrefixesPresent(argMultimap, PREFIX_PHONE, PREFIX_JOB_TITLE))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApplicationCommand.MESSAGE_USAGE));
        }
        // Initialise application status below.
        ApplicationStatus applicationStatus;
        if (argMultimap.getValue(PREFIX_APPLICATION_STATUS).isPresent()) {
            try {
                applicationStatus = ParserUtil.parseApplicationStatus(
                        argMultimap.getValue(PREFIX_APPLICATION_STATUS).get());
            } catch (ParseException pe) {
                throw new ParseException(pe.getMessage());
            }
        } else {
            applicationStatus = ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS;
        }
        // Prefix-based polymorphism below.
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PERSON_INDEX, PREFIX_JOB_INDEX, PREFIX_PHONE, PREFIX_JOB_TITLE,
                PREFIX_APPLICATION_STATUS);
        if (arePrefixesPresent(argMultimap, PREFIX_PHONE, PREFIX_JOB_TITLE)) {
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            JobTitle jobTitle = ParserUtil.parseJobTitle(argMultimap.getValue(PREFIX_JOB_TITLE).get());
            Person dummyPerson = new Person(Name.DEFAULT_NAME, phone, Email.DEFAULT_EMAIL, Address.DEFAULT_ADDRESS,
                    School.DEFAULT_SCHOOL, Degree.DEFAULT_DEGREE, Person.DEFAULT_TAGS);
            Job dummyJob = new Job(jobTitle, JobCompany.DEFAULT_JOBCOMPANY, JobRounds.DEFAULT_JOBROUNDS,
                    JobSkills.DEFAULT_JOBSKILLS, JobAddress.DEFAULT_JOBADDRESS, JobType.DEFAULT_JOBTYPE);
            Application application = new Application(dummyPerson, dummyJob, applicationStatus);
            return new AddApplicationCommand(application);
        } else {
            Index personIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PERSON_INDEX).get());
            Index jobIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_JOB_INDEX).get());
            return new AddApplicationCommand(personIndex, jobIndex, applicationStatus);
        }
    }

    /**
     * Returns true if none of the mandatory prefixes maps to empty {@code optional} values in {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
