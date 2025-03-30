package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROUNDS;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.application.ApplicationStatusPredicate;
import seedu.address.model.job.Job;

/**
 * Filters the list to show only applications with the specified status.
 * Can only be used in job view.
 */
public class FindAppCommand extends Command {

    public static final String COMMAND_WORD = "findapp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters the list to show only applications "
            + "with the specified status in job view.\n"
            + "Parameters: " + PREFIX_JOB_INDEX + "JOB_INDEX (optional) "
            + PREFIX_ROUNDS + "ROUNDS\n"
            + "Example: " + COMMAND_WORD + " j/1 r/2";

    public static final String MESSAGE_SUCCESS = "Filtered applications by status: %1$s";
    public static final String MESSAGE_NO_MATCHES = "No applications found with status: %1$s";
    public static final String MESSAGE_JOB_NOT_FOUND = "The specified job index is invalid";
    public static final String MESSAGE_WRONG_VIEW = "This command can only be used in job view";

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

        // Check if we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_WRONG_VIEW);
        }

        // Check if we need to reset the view
        if (model.getCurrentViewState() == Model.ViewState.JOB_DETAIL_VIEW
            || model.getCurrentViewState() == Model.ViewState.PERSON_DETAIL_VIEW) {
            model.setViewState(Model.ViewState.JOB_VIEW);
            logger.info("Reset to JOB_VIEW from detail view");
        }

        // Create the status predicate
        ApplicationStatus targetStatus = new ApplicationStatus(Integer.parseInt(status));
        ApplicationStatusPredicate statusPredicate = new ApplicationStatusPredicate(targetStatus);

        if (jobIndex.isPresent()) {
            // If job index is specified, first filter the job list
            List<Job> lastShownList = model.getFilteredJobList();
            if (jobIndex.get().getZeroBased() >= lastShownList.size()) {
                throw new CommandException(MESSAGE_JOB_NOT_FOUND);
            }

            Job jobToFilter = lastShownList.get(jobIndex.get().getZeroBased());
            logger.info("Filtering for job: " + jobToFilter.getJobTitle().jobTitle());

            // Update the job list to only show this job
            model.updateFilteredJobList(job -> job.equals(jobToFilter));

            // Get all applications for this job
            List<Application> jobApplications = model.getApplicationsByJob(jobToFilter);
            
            // Filter these applications by the status predicate
            List<Application> filteredJobApps = jobApplications.stream()
                    .filter(app -> statusPredicate.test(app))
                    .toList();
            
            // Update application list to show only these filtered applications
            model.updateFilteredApplicationList(app -> filteredJobApps.contains(app));
        } else {
            // No job index specified, apply the status filter to all applications
            model.updateFilteredApplicationList(statusPredicate);
        }

        return CommandResult.withRefreshApplications(String.format(MESSAGE_SUCCESS, status));
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
