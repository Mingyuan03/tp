package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Filters the list to show only applications with the specified status.
 * Can be used in both person view and job view.
 */
public class FindAppCommand extends Command {

    public static final String COMMAND_WORD = "findapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters the list to show only applications "
            + "with the specified status.\n"
            + "In job view: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "JOB_INDEX (optional) "
            + PREFIX_STATUS + "STATUS\n"
            + "In person view: " + COMMAND_WORD + " " + PREFIX_STATUS + "STATUS\n"
            + "Example: " + COMMAND_WORD + " j/1 st/Accepted";

    public static final String MESSAGE_SUCCESS = "Filtered applications by status: %1$s";
    public static final String MESSAGE_NO_MATCHES = "No applications found with status: %1$s. Filter cleared.";
    public static final String MESSAGE_JOB_NOT_FOUND = "The specified job index is invalid";
    public static final String MESSAGE_JOB_PARAM_IGNORED = "Job index parameter ignored in person view";

    private static final Logger logger = LogsCenter.getLogger(FindAppCommand.class);

    private final String status;
    private final Optional<Index> jobIndex;

    /**
     * Creates a FindAppCommand to filter by the specified status
     */
    public FindAppCommand(String status) {
        this.status = status;
        this.jobIndex = Optional.empty();
    }

    /**
     * Creates a FindAppCommand to filter by the specified job and status
     */
    public FindAppCommand(Index jobIndex, String status) {
        this.status = status;
        this.jobIndex = Optional.of(jobIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        logger.info("Executing FindAppCommand with status: " + status + ", jobIndex: " + jobIndex);
        logger.info("Current view state: " + model.getCurrentViewState());

        String feedbackMessage = String.format(MESSAGE_SUCCESS, status);
        boolean shouldClearView = false;
        boolean shouldRefreshJobView = model.isInJobView(); // Set to true if in job view

        // Check if we need to reset the view
        if (model.getCurrentViewState() == Model.ViewState.JOB_DETAIL_VIEW
            || model.getCurrentViewState() == Model.ViewState.PERSON_DETAIL_VIEW) {
            model.setViewState(Model.ViewState.JOB_VIEW);
            logger.info("Reset to JOB_VIEW from detail view");
            shouldClearView = true;
        }

        // Set the global status filter
        model.setApplicationStatusFilter(status);

        if (jobIndex.isPresent()) {
            // If job index is specified, we need to filter for a specific job
            if (!model.isInJobView()) {
                // Warn about job index being ignored in person view
                feedbackMessage = MESSAGE_JOB_PARAM_IGNORED + ". " + feedbackMessage;
                // Apply the global status filter
                model.applyStatusFilter();
            } else {
                // In job view with job index specified
                List<Job> lastShownList = model.getFilteredJobList();
                if (jobIndex.get().getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(MESSAGE_JOB_NOT_FOUND);
                }

                Job jobToFilter = lastShownList.get(jobIndex.get().getZeroBased());
                logger.info("Filtering applications for job: " + jobToFilter.getJobTitle().jobTitle());

                // Apply filter for this specific job AND with the specified status
                // First apply the global status filter
                model.applyStatusFilter();

                // Then get applications that are both for this job AND have the specified status
                List<Application> filteredJobApps = model.getApplicationsByJob(jobToFilter);

                // Filter these applications again to ensure they match both job AND status
                List<Application> statusFilteredJobApps = filteredJobApps.stream()
                        .filter(app -> Integer.toString(app.applicationStatus().applicationStatus).equals(status))
                        .toList();

                // Update the application list to only show these applications
                model.updateFilteredApplicationList(app -> statusFilteredJobApps.contains(app));

                // Update job list to show only this job if it has matching applications
                if (!statusFilteredJobApps.isEmpty()) {
                    model.updateFilteredJobList(job -> job.equals(jobToFilter));
                } else {
                    // No applications found with that status for that job
                    model.clearStatusFilter();
                    feedbackMessage = String.format(MESSAGE_NO_MATCHES, status);
                }
            }
        } else {
            // No job index specified, just apply the global status filter
            model.applyStatusFilter();

            // Check if we have any matches
            if (model.getFilteredApplicationList().isEmpty()) {
                // No applications found with that status
                model.clearStatusFilter();
                feedbackMessage = String.format(MESSAGE_NO_MATCHES, status);
            }
        }

        // Use the constructor with refreshJobView flag
        return new CommandResult(feedbackMessage, shouldClearView, shouldRefreshJobView);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindAppCommand)) {
            return false;
        }

        FindAppCommand otherCommand = (FindAppCommand) other;
        return status.equals(otherCommand.status)
                && jobIndex.equals(otherCommand.jobIndex);
    }
}
