package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Deletes an application between a person and a job.
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

    private final Index jobIndex;
    private final Index applicationIndex;

    /**
     * Creates a DeleteApplicationCommand to delete the application at the specified index
     * for the specified job.
     */
    public DeleteApplicationCommand(Index jobIndex, Index applicationIndex) {
        requireNonNull(jobIndex);
        requireNonNull(applicationIndex);
        this.jobIndex = jobIndex;
        this.applicationIndex = applicationIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Job> lastShownJobList = model.getFilteredJobList();

        if (jobIndex.getZeroBased() >= lastShownJobList.size()) {
            throw new CommandException(MESSAGE_JOB_INVALID_INDEX);
        }

        Job job = lastShownJobList.get(jobIndex.getZeroBased());

        // Get applications for this job
        List<Application> jobApplications = model.getFilteredApplicationsByJob(job);

        if (applicationIndex.getZeroBased() >= jobApplications.size()) {
            throw new CommandException(MESSAGE_APPLICATION_INVALID_INDEX);
        }

        Application targetApplication = jobApplications.get(applicationIndex.getZeroBased());

        model.deleteApplication(targetApplication);

        String successMessage = String.format(MESSAGE_DELETE_APPLICATION_SUCCESS,
                targetApplication.getApplicant().getName(), job.getJobTitle());

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
