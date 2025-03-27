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
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Adds an {@code Application} to the address book.
 */
public class AddApplicationCommand extends Command {
    public static final String COMMAND_WORD = "addapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an application to the model manager. There exists"
            + " 2 ways to enter inputs for adding an application.\nThe first way comprises these parameters: "
            + PREFIX_PHONE + "CANDIDATE'S PHONE NUMBER "
            + PREFIX_JOB_TITLE + "JOB TITLE "
            + PREFIX_APPLICATION_STATUS + "OPTIONAL APPLICATION STATUS DEFAULTING TO 0.\nThe second way comprises these"
            + " parameters: " + PREFIX_PERSON_INDEX + "CANDIDATE'S LAST SEEN INDEX IN GUI "
            + PREFIX_JOB_INDEX + "JOB'S LAST SEEN INDEX IN GUI "
            + PREFIX_APPLICATION_STATUS + "OPTIONAL APPLICATION STATUS DEFAULTING TO 0.";
    public static final String MESSAGE_SUCCESS = "New application added as follows:\n" + "Application added: {%1$s}";
    public static final String MESSAGE_DUPLICATE_APPLICATION =
            "This application already exists in the address book. Try using the AdvanceApplicationCommand instead!";
    public static final String MESSAGE_INVALID_PERSON = "This application's person does not exist in the address book";
    public static final String MESSAGE_INVALID_JOB = "This application's job does not exist in the address book";
    public static final String MESSAGE_NONUNIQUE_PERSON = "This application's person is non-unique in the address book";
    public static final String MESSAGE_NONUNIQUE_JOB = "This application's job is non-unique in the address book";

    private Index personIndex = null;
    private Index jobIndex = null;
    private ApplicationStatus applicationStatus = null;
    private Application application = null;
    private final boolean parseByIndex;

    /**
     * Creates an {@code AddApplicationCommand} to add the specified {@code Application}.
     * @param personIndex index of person in last {@code FilteredPersonList} via
     *                    {@code AddApplicationCommandParser::parse}.
     * @param jobIndex index of job in last {@code FilteredJobList} via {@code AddApplicationCommandParser::parse}.
     * @param applicationStatus optional application status to manually set round index of new application if present.
     */
    public AddApplicationCommand(Index personIndex, Index jobIndex, ApplicationStatus applicationStatus) {
        requireAllNonNull(personIndex, jobIndex, applicationStatus);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
        this.applicationStatus = applicationStatus; // Logic completed in AddApplicationCommandParser.
        this.parseByIndex = true;
    }

    /**
     * Creates an {@code AddApplicationCommand} to add the specified {@code Application}.
     * @param application Intermediate application from {@code AddApplicationCommandParser::parse}.
     */
    public AddApplicationCommand(Application application) {
        requireNonNull(application);
        this.application = application;
        this.parseByIndex = false;
    }

    /**
     * Executes {@code AddApplicationCommand} and returns the resulting success or failure message.
     * @param model {@code Model} which the command must operate on, BEWARE dependencies on {@code FilteredPersonList},
     *                           {@code FilteredJobList}, and {@code FilteredApplicationList} exists here only to check
     *                           given {@code AddApplicationCommandParser} lacks {@code Model}'s subclass's import.
     * @return feedback message of the operation result for display.
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (this.parseByIndex) {
            ObservableList<Person> matchingPersons = model.getFilteredPersonList();
            ObservableList<Job> matchingJobs = model.getFilteredJobList();
            // 1st guard condition below: Invalid person index. Index bypasses AddApplicationCommandParser's dummy.
            if (this.personIndex.getZeroBased() >= matchingPersons.size() || this.personIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_PERSON);
            }
            Person matchingPerson = matchingPersons.get(this.personIndex.getZeroBased());
            // 2nd guard condition below: Invalid job index. Index bypasses AddApplicationCommandParser's dummy.
            if (this.jobIndex.getZeroBased() >= matchingJobs.size() || this.jobIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_JOB);
            }
            Job matchingJob = matchingJobs.get(this.jobIndex.getZeroBased());
            this.application = new Application(matchingPerson, matchingJob, this.applicationStatus);
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
        // 3rd guard condition below: No existing duplicate application, duplicate defined as having same Person and Job
        // free of application status so max 1 Application instance per (Person, Job) exists in UniqueApplicationList.
        if (model.hasApplication(this.application)) { // Code ultimately traces to {Application::equals}.
            throw new CommandException(MESSAGE_DUPLICATE_APPLICATION);
        }
        // Finally apply main logic of adding new valid application to model.
        model.addApplication(this.application);
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.application));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof AddApplicationCommand otherAddApplicationCommand)) {
            return false;
        }
        // distinguish between parse by indices and parse by more concrete Person and Job's unique identifiers.
        if (this.parseByIndex != otherAddApplicationCommand.parseByIndex) {
            return false;
        }
        if (this.parseByIndex) {
            return this.personIndex.equals(otherAddApplicationCommand.personIndex)
                    && this.jobIndex.equals(otherAddApplicationCommand.jobIndex)
                    && this.applicationStatus.equals(otherAddApplicationCommand.applicationStatus);
        } else {
            return this.application.equals(otherAddApplicationCommand.application);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Application added", this.application).toString();
    }
}
