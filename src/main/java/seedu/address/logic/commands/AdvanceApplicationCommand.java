package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_TITLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Advances the {@code ApplicationStatus} of an {@code Application} in the address book.
 */
public class AdvanceApplicationCommand extends Command {
    public static final String COMMAND_WORD = "advapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Advances an application in the model manager. There "
            + "exists 2 ways to enter inputs for advancing an application.\nThe first way comprises these parameters: "
            + PREFIX_PHONE + "CANDIDATE'S PHONE NUMBER "
            + PREFIX_JOB_TITLE + "JOB TITLE "
            + PREFIX_APPLICATION_STATUS + "OPTIONAL INCREMENT IN APPLICATION STATUS DEFAULTING TO 1.\nThe second way "
            + "comprises these parameters: " + PREFIX_PERSON_INDEX + "CANDIDATE'S LAST SEEN INDEX IN GUI "
            + PREFIX_JOB_INDEX + "JOB'S LAST SEEN INDEX IN GUI "
            + PREFIX_APPLICATION_STATUS + "OPTIONAL INCREMENT IN APPLICATION STATUS DEFAULTING TO 1.";
    public static final String MESSAGE_SUCCESS = """
            Application advanced as follows:
            Initial application advanced: {%1$s}
            Number of rounds advanced: %2$s""";
    public static final String MESSAGE_FULL_APPLICATION = "This application is already at the last round and cannot be "
            + "advanced in the address book. Try using the DeleteApplicationCommand to delete the application instead!";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using the AddApplicationCommand to add an application first!";
    public static final String MESSAGE_INVALID_PERSON = "This application's person does not exist in the address book.";
    public static final String MESSAGE_INVALID_JOB = "This application's job does not exist in the address book.";
    public static final String MESSAGE_INVALID_APPLICATION_STATUS_ADVANCED =
            "Advancing this application by 0 rounds has no effect.";
    public static final String MESSAGE_NONUNIQUE_PERSON = "This application's person is non-unique in the address book";
    public static final String MESSAGE_NONUNIQUE_JOB = "This application's job is non-unique in the address book";
    public static final String MESSAGE_NONUNIQUE_APPLICATION = "This application is non-unique in the address book";

    private Index personIndex = null;
    private Index jobIndex = null;
    private final ApplicationStatus applicationStatusAdvanced;
    private Application application = null;
    private final boolean parseByIndex;

    /**
     * Creates an {@code AdvanceApplicationCommand} to advance the specified {@code Application} by
     * {@code applicationStatusAdvanced} integer number of rounds if possible, NOT DEFAULTING if limit exceeded.
     * @param personIndex index of person in last {@code FilteredPersonList} via
     *                    {@code AdvanceApplicationCommandParser::parse}
     * @param jobIndex index of job in last {@code FilteredJobList} via {@code AdvanceApplicationCommandParser::parse}.
     * @param applicationStatusAdvanced optional increase in application status if present.
     */
    public AdvanceApplicationCommand(Index personIndex, Index jobIndex, ApplicationStatus applicationStatusAdvanced) {
        requireAllNonNull(personIndex, jobIndex, applicationStatusAdvanced);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
        this.applicationStatusAdvanced = applicationStatusAdvanced; // Logic done in AdvanceApplicationCommandParser.
        this.parseByIndex = true;
    }

    /**
     * Creates an {@code AdvanceApplicationCommand} to advance the specified {@code Application},
     * {@code applicationStatusAdvanced} contained within for easier parsing in {@code AdvanceApplicationCommandParser}.
     * @param application Intermediate application from {@code AdvanceApplicationCommandParser::parse}.
     * @param applicationStatusAdvanced optional increase in application status if present.
     */
    public AdvanceApplicationCommand(Application application, ApplicationStatus applicationStatusAdvanced) {
        requireNonNull(application);
        this.application = application;
        this.applicationStatusAdvanced = applicationStatusAdvanced;
        this.parseByIndex = false;
    }

    /**
     * Executes {@code AdvanceApplicationCommand} and returns the resulting success or failure message.
     * @param model {@code Model} which the command must operate on, BEWARE dependencies on {@code FilteredPersonList},
     *                           {@code FilteredJobList}, and {@code FilteredApplicationList} exists here only to check
     *                           given {@code AdvanceApplicationCommandParser} lacks {@code Model}'s subclass's import.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // 0th guard condition: Invalid application status advanced being 0. Ensure advancement is meaningful.
        if (this.applicationStatusAdvanced.equals(ApplicationStatus.DEFAULT_ADDAPPLICATIONSTATUS)) {
            throw new CommandException(MESSAGE_INVALID_APPLICATION_STATUS_ADVANCED);
        }
        if (this.parseByIndex) {
            ObservableList<Person> matchingPersons = model.getFilteredPersonList();
            ObservableList<Job> matchingJobs = model.getFilteredJobList();
            // 1st guard condition below: Invalid person index. Index bypasses AdvanceApplicationCommandParser's dummy.
            if (this.personIndex.getZeroBased() >= matchingPersons.size() || this.personIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_PERSON);
            }
            Person matchingPerson = matchingPersons.get(this.personIndex.getZeroBased());
            // 2nd guard condition below: Invalid job index. Index bypasses AdvanceApplicationCommandParser's dummy.
            if (this.jobIndex.getZeroBased() >= matchingJobs.size() || this.jobIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_JOB);
            }
            Job matchingJob = matchingJobs.get(this.jobIndex.getZeroBased());
            // 4th guard condition applies too: Existing application can't be advanced by round without exceeding limit.
            try {
                this.application = new Application(matchingPerson, matchingJob, this.applicationStatusAdvanced);
            } catch (InvalidApplicationStatusException e) {
                throw new CommandException(MESSAGE_FULL_APPLICATION);
            }
        } else {
            // 1st guard condition below: Invalid person by person's identifier i.e. Phone.
            if (!model.hasPerson(this.application.applicant())) {
                throw new CommandException(MESSAGE_INVALID_PERSON);
            }
            List<Person> matchingPersons = model.getPersonsByPhone(this.application.applicant().getPhone());
            assert matchingPersons.size() == 1 : MESSAGE_NONUNIQUE_PERSON;
            this.application.setApplicant(matchingPersons.get(0)); // Update dummy person to valid person in-place.
            // 2nd guard condition below: Invalid job by job's identifier i.e. JobTitle.
            if (!model.hasJob(this.application.job())) {
                throw new CommandException(MESSAGE_INVALID_JOB);
            }
            List<Job> matchingJobs = model.getJobsByTitle(this.application.job().jobTitle());
            assert matchingJobs.size() == 1 : MESSAGE_NONUNIQUE_JOB;
            this.application.setJob(matchingJobs.get(0)); // Update dummy job to valid job in-place.
        }
        // 3rd guard condition below: No existing application to advance.
        if (!model.hasApplication(this.application)) { // Code ultimately traces to {Application::equals}.
            throw new CommandException(MESSAGE_INVALID_APPLICATION);
        }
        List<Application> matchingApplications = model.getApplicationsByPersonAndJob(
                this.application.applicant(), this.application.job());
        assert matchingApplications.size() == 1 : MESSAGE_NONUNIQUE_APPLICATION;
        // Safely update this.application below since this.applicationStatusAdvanced has been preemptively initialised.
        this.application = matchingApplications.get(0);
        // 4th guard condition with main logic: Existing application can't be advanced by round without exceeding limit.
        try {
            model.advanceApplication(this.application, this.applicationStatusAdvanced.applicationStatus);
            return new CommandResult(String.format(MESSAGE_SUCCESS, this.application, this.applicationStatusAdvanced));
        } catch (InvalidApplicationStatusException ie) {
            throw new CommandException(MESSAGE_FULL_APPLICATION);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof AdvanceApplicationCommand otherAdvanceApplicationCommand)) {
            return false;
        }
        // distinguish between parse by indices and parse by more concrete Person and Job's unique identifiers.
        if (this.parseByIndex != otherAdvanceApplicationCommand.parseByIndex) {
            return false;
        }
        if (this.parseByIndex) {
            return this.personIndex.equals(otherAdvanceApplicationCommand.personIndex)
                    && this.jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                    && this.applicationStatusAdvanced.equals(otherAdvanceApplicationCommand.applicationStatusAdvanced);
        } else {
            return this.application.equals(otherAdvanceApplicationCommand.application)
                    && this.applicationStatusAdvanced.equals(otherAdvanceApplicationCommand.applicationStatusAdvanced);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Initial application advanced", this.application)
                .add("Number of rounds advanced", this.applicationStatusAdvanced).toString();
    }
}
