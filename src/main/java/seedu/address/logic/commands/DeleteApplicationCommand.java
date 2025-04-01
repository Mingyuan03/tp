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
 * Deletes an {@code Application} from the address book.
 */
public class DeleteApplicationCommand extends Command {
    public static final String COMMAND_WORD = "delapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an application from the address book.\nThere "
            + "exists 2 ways to enter inputs for deleting an application.\nThe first way has these parameters: "
            + PREFIX_JOB_INDEX + "JOB INDEX IN JOB VIEW "
            + PREFIX_APPLICATION_INDEX + "APPLICATION INDEX BY JOB IN JOB VIEW\nThe second way has these parameters: "
            + PREFIX_PERSON_INDEX + "PERSON INDEX IN PERSON VIEW " + PREFIX_JOB_INDEX + "JOB INDEX IN JOB VIEW\n"
            + "Example for 1st way: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_APPLICATION_INDEX + "2\n"
            + "Example for 2nd way: " + COMMAND_WORD + " " + PREFIX_PERSON_INDEX + "1 " + PREFIX_JOB_INDEX + "2\n";
    public static final String MESSAGE_SUCCESS = "Deleted application as follows:\nApplication deleted: {%1$s}";
    public static final String MESSAGE_INVALID_PERSON = "This person does not exist in the address book. "
            + "Try using " + AddCommand.COMMAND_WORD + " to add a person first, then use "
            + AddApplicationCommand.COMMAND_WORD + " to add an application!";
    public static final String MESSAGE_INVALID_JOB = "This job does not exist in the address book. "
            + "Try using " + AddJobCommand.COMMAND_WORD + " to add a job first, then use "
            + AddApplicationCommand.COMMAND_WORD + " to add an application!";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using " + AddApplicationCommand.COMMAND_WORD + " to add an application first!";

    private Index personIndex = null;
    private final Index jobIndex;
    private Index applicationByJobIndex = null;
    private Application applicationToDelete = null;
    private final boolean isOnlyJobView;

    /**
     * Creates a {@code DeleteApplicationCommand} to delete the {@code application} associated with the person and job
     * by toggling between person view and job view.
     */
    public DeleteApplicationCommand(Index jobIndex, Index otherIndex, boolean isOnlyJobView) {
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
        Job matchingJob = lastShownJobList.get(this.jobIndex.getZeroBased());
        if (this.isOnlyJobView) {
            // Get applications for this job
            List<Application> jobApplications = model.getFilteredApplicationsByJob(matchingJob);
            // 2nd guard condition below: Invalid application by job index.
            if (this.applicationByJobIndex.getZeroBased() >= jobApplications.size()
                    || this.applicationByJobIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_APPLICATION);
            }
            this.applicationToDelete = jobApplications.get(this.applicationByJobIndex.getZeroBased());
        } else {
            ObservableList<Person> lastShownPersonList = model.getFilteredPersonList();
            // 1st guard condition (continued) below: Invalid person index.
            if (this.personIndex.getZeroBased() >= lastShownPersonList.size() || this.personIndex.getZeroBased() < 0) {
                throw new CommandException(MESSAGE_INVALID_PERSON);
            }
            Person matchingPerson = lastShownPersonList.get(this.personIndex.getZeroBased());
            // 2nd guard condition below: No existing valid application.
            List<Application> matchingApplications = model.getApplicationsByPersonAndJob(matchingPerson, matchingJob);
            if (matchingApplications.isEmpty()) {
                throw new CommandException(MESSAGE_INVALID_APPLICATION);
            }
            this.applicationToDelete = matchingApplications.get(0);
        }
        // Apply main logic of deleting valid existing application from model in-place.
        model.deleteApplication(this.applicationToDelete);
        String successMessage = String.format(MESSAGE_SUCCESS, this.applicationToDelete);
        // Return a CommandResult that signals applications need to be refreshed
        return CommandResult.withRefreshApplications(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof DeleteApplicationCommand otherDeleteApplicationCommand)) {
            return false;
        }
        // Distinguish between different dispatches.
        if (this.isOnlyJobView != otherDeleteApplicationCommand.isOnlyJobView) {
            return false;
        }
        if (this.isOnlyJobView) {
            return this.jobIndex.equals(otherDeleteApplicationCommand.jobIndex)
                    && this.applicationByJobIndex.equals(otherDeleteApplicationCommand.applicationByJobIndex)
                    && this.applicationToDelete.equals(otherDeleteApplicationCommand.applicationToDelete)
                    && this.personIndex == null
                    && otherDeleteApplicationCommand.personIndex == null;
        } else {
            return this.personIndex.equals(otherDeleteApplicationCommand.personIndex)
                    && this.jobIndex.equals(otherDeleteApplicationCommand.jobIndex)
                    && this.applicationToDelete.equals(otherDeleteApplicationCommand.applicationToDelete)
                    && this.applicationByJobIndex == null
                    && otherDeleteApplicationCommand.applicationByJobIndex == null;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("Application deleted", this.applicationToDelete)
                .toString();
    }
}
