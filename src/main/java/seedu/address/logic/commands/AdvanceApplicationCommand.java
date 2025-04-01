package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Advances the {@code ApplicationStatus} of an {@code Application} by 1 round.
 */
public class AdvanceApplicationCommand extends Command {
    public static final String COMMAND_WORD = "advapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Advances an application in the model manager.\nThere "
            + "exists 2 ways to to enter inputs for advancing an application.\nThe first way has these parameters: "
            + PREFIX_JOB_INDEX + "JOB INDEX IN JOB VIEW "
            + PREFIX_APPLICATION_INDEX + "APPLICATION INDEX BY JOB IN JOB VIEW\nThe second way has these parameters: "
            + PREFIX_PERSON_INDEX + "PERSON INDEX IN PERSON VIEW " + PREFIX_JOB_INDEX + "JOB INDEX IN JOB VIEW\n"
            + "Example for 1st way: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_APPLICATION_INDEX + "2\n"
            + "Example for 2nd way: " + COMMAND_WORD + " " + PREFIX_PERSON_INDEX + "1 " + PREFIX_JOB_INDEX + "2\n";
    public static final String MESSAGE_SUCCESS =
            "Application advanced as follows:\nInitial application advanced: {%1$s}\nNumber of rounds advanced: 1";
    public static final String MESSAGE_EXCEED_ROUNDS = "This application is already at the last round and cannot be "
            + "advanced in the address book. Try using DeleteApplicationCommand to delete the application instead!";
    public static final String MESSAGE_INVALID_PERSON = "This person does not exist in the address book. "
            + "Try using AddCommand to add a person first, then use AddApplicationCommand to add an application!";
    public static final String MESSAGE_INVALID_JOB = "This job does not exist in the address book. "
            + "Try using AddJobCommand to add a job first, then use AddApplicationCommand to add an application!";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using AddApplicationCommand to add an application first!";

    private Index personIndex = null;
    private final Index jobIndex;
    private Index applicationByJobIndex = null;
    private Application applicationToAdvance = null;
    private final boolean isOnlyJobView;

    /**
     * Creates an AdvanceApplicationCommand to advance the application for the specified job
     * at the specified application index by the specified number of rounds.
     */
    public AdvanceApplicationCommand(Index jobIndex, Index otherIndex, boolean isOnlyJobView) {
        requireAllNonNull(jobIndex, otherIndex);
        this.jobIndex = jobIndex;
        this.isOnlyJobView = isOnlyJobView;
        if (isOnlyJobView) {
            this.applicationByJobIndex = otherIndex;
        } else {
            this.personIndex = otherIndex;
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Job> lastShownJobList = model.getFilteredJobList();
        // 1st guard condition below: Invalid job index.
        if (this.jobIndex.getZeroBased() >= lastShownJobList.size() || this.jobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_JOB);
        }
        Job matchingJob = lastShownJobList.get(jobIndex.getZeroBased());
        if (this.isOnlyJobView) {
            // Get applications for this job.
            List<Application> jobApplications = model.getFilteredApplicationsByJob(matchingJob);
            // 2nd guard condition below: Invalid application by job index.
            if (this.applicationByJobIndex.getZeroBased() >= jobApplications.size()
                    || this.applicationByJobIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_APPLICATION);
            }
            this.applicationToAdvance = jobApplications.get(this.applicationByJobIndex.getZeroBased());
        } else {
            ObservableList<Person> lastShownPersonList = model.getFilteredPersonList();
            // 1st guard condition (continued) below: Invalid person index.
            if (this.personIndex.getZeroBased() >= lastShownJobList.size() || this.personIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_PERSON);
            }
            Person matchingPerson = lastShownPersonList.get(this.personIndex.getZeroBased());
            // 2nd guard condition below: No existing valid application.
            List<Application> matchingApplications = model.getApplicationsByPersonAndJob(matchingPerson, matchingJob);
            if (matchingApplications.isEmpty()) {
                throw new CommandException(MESSAGE_INVALID_APPLICATION);
            }
            this.applicationToAdvance = matchingApplications.get(0);
        }
        // 3rd guard condition below: Application is at its final round.
        if (this.applicationToAdvance.getApplicationStatus().applicationStatus
                >= this.applicationToAdvance.getJob().getJobRounds().jobRounds) {
            throw new CommandException(MESSAGE_EXCEED_ROUNDS);
        }
        // Apply main logic of advancing valid existing application in model.
        this.applicationToAdvance = model.advanceApplication(this.applicationToAdvance, 1);
        String successMessage = String.format(MESSAGE_SUCCESS, this.applicationToAdvance);
        // Return a CommandResult that signals applications need to be refreshed
        return CommandResult.withRefreshApplications(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof AdvanceApplicationCommand otherAdvanceApplicationCommand)) {
            return false;
        }
        // Distinguish between different dispatches.
        if (this.isOnlyJobView != otherAdvanceApplicationCommand.isOnlyJobView) {
            return false;
        }
        if (this.isOnlyJobView) {
            return this.jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                    && this.applicationByJobIndex.equals(otherAdvanceApplicationCommand.applicationByJobIndex)
                    && this.applicationToAdvance.equals(otherAdvanceApplicationCommand.applicationToAdvance)
                    && this.personIndex == null
                    && otherAdvanceApplicationCommand.personIndex == null;
        } else {
            return this.personIndex.equals(otherAdvanceApplicationCommand.personIndex)
                    && this.jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                    && this.applicationToAdvance.equals(otherAdvanceApplicationCommand.applicationToAdvance)
                    && this.applicationByJobIndex == null
                    && otherAdvanceApplicationCommand.applicationByJobIndex == null;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Initial application advanced", this.applicationToAdvance)
                .toString();
    }
}
