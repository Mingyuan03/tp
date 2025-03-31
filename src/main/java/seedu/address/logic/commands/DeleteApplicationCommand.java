package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Deletes an {@code Application} from the address book.
 */
public class DeleteApplicationCommand extends Command {

    public static final String COMMAND_WORD = "deleteapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the application between the specified person and job. "
            + "Parameters: "
            + PREFIX_JOB_INDEX + "JOB_INDEX "
            + PREFIX_APPLICATION_INDEX + "APPLICATION_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_JOB_INDEX + "2 "
            + PREFIX_APPLICATION_INDEX + "1";

    public static final String MESSAGE_DELETE_APPLICATION_SUCCESS =
            "Deleted application: %1$s's application for %2$s";
    public static final String MESSAGE_APPLICATION_NOT_FOUND =
            "No application found at the specified index";
    public static final String MESSAGE_JOB_INVALID_INDEX =
            "Job index is invalid";
    public static final String MESSAGE_APPLICATION_INVALID_INDEX =
            "Application index is invalid";
    public static final String COMMAND_WORD = "delapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an application from the application manager. "
            + "Parameters: " + PREFIX_PERSON_INDEX + "CANDIDATE'S LAST SEEN INDEX IN GUI "
            + PREFIX_JOB_INDEX + "JOB's LAST SEEN INDEX IN GUI.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PERSON_INDEX + "1 " + PREFIX_JOB_INDEX + "2";
    public static final String MESSAGE_SUCCESS = "Deleted application as follows:\nApplication deleted: {%1$s}";
    public static final String MESSAGE_INVALID_APPLICATION = "This application does not exist in the address book. "
            + "Try using the AddApplicationCommand to add an application first!";
    public static final String MESSAGE_INVALID_PERSON = "This application's person does not exist in the address book.";
    public static final String MESSAGE_INVALID_JOB = "This application's job does not exist in the address book.";

    private final Index jobIndex;
    private final Index applicationIndex;

    /**
     * Creates a DeleteApplicationCommand to delete the application at the specified index
     * for the specified job.
     * Creates a {@code DeleteApplicationCommand} to delete the {@code application} associated with the person and job.
     */
    public DeleteApplicationCommand(Index jobIndex, Index applicationIndex) {
        requireNonNull(jobIndex);
        requireNonNull(applicationIndex);
    public DeleteApplicationCommand(Index personIndex, Index jobIndex) {
        requireAllNonNull(personIndex, jobIndex);
        this.personIndex = personIndex;
        this.jobIndex = jobIndex;
        this.applicationIndex = applicationIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        ObservableList<Person> lastShownPersonList = model.getFilteredPersonList();
        ObservableList<Job> lastShownJobList = model.getFilteredJobList();
        // 1st guard condition below: Invalid person index.
        if (this.personIndex.getZeroBased() >= lastShownPersonList.size() || this.personIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_PERSON);
        }
        Person matchingPerson = lastShownPersonList.get(this.personIndex.getZeroBased());
        // 2nd guard condition below: Invalid job index.
        if (this.jobIndex.getZeroBased() >= lastShownJobList.size() || this.jobIndex.getZeroBased() < 0) {
            throw new CommandException(MESSAGE_INVALID_JOB);

        List<Job> lastShownJobList = model.getFilteredJobList();

        if (jobIndex.getZeroBased() >= lastShownJobList.size()) {
            throw new CommandException(MESSAGE_JOB_INVALID_INDEX);
        }

        Job job = lastShownJobList.get(jobIndex.getZeroBased());

        // Get applications for this job
        List<Application> jobApplications = model.getFilteredApplicationsByJob(job);
        Job matchingJob = lastShownJobList.get(this.jobIndex.getZeroBased());
        // Find the application for this person and job
        List<Application> personApplications = model.getApplicationsByPerson(matchingPerson);
        Application targetApplication = null;

        if (applicationIndex.getZeroBased() >= jobApplications.size()) {
            throw new CommandException(MESSAGE_APPLICATION_INVALID_INDEX);
        for (Application app : personApplications) {
            if (app.getJob().equals(matchingJob)) {
                targetApplication = app;
                break;
            }
        }

        Application targetApplication = jobApplications.get(applicationIndex.getZeroBased());
        if (targetApplication == null) {
            throw new CommandException(MESSAGE_INVALID_APPLICATION);
        }

        model.deleteApplication(targetApplication);

        String successMessage = String.format(MESSAGE_DELETE_APPLICATION_SUCCESS,
                targetApplication.getApplicant().getName(), job.getJobTitle());
        String successMessage = String.format(MESSAGE_SUCCESS,
                matchingPerson.getName(), matchingJob.getJobTitle());

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

        return jobIndex.equals(otherDeleteApplicationCommand.jobIndex)
                && applicationIndex.equals(otherDeleteApplicationCommand.applicationIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("jobIndex", jobIndex)
                .add("applicationIndex", applicationIndex)
                .toString();
    }
}
