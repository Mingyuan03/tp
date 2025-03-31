package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPLICATION_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROUNDS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;

/**
 * Advances an application by a specified number of rounds.
 */
public class AdvanceApplicationCommand extends Command {
    public static final String COMMAND_WORD = "advapp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Advances an application by the specified number of rounds. "
            + "Parameters: "
            + PREFIX_JOB_INDEX + "JOB_INDEX "
            + PREFIX_APPLICATION_INDEX + "APPLICATION_INDEX "
            + PREFIX_ROUNDS + "ROUNDS\n"
            + PREFIX_APPLICATION_STATUS + "ROUNDS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_JOB_INDEX + "2 "
            + PREFIX_APPLICATION_STATUS + "1";
            + PREFIX_APPLICATION_INDEX + "1 "
            + PREFIX_ROUNDS + "1";

    public static final String MESSAGE_ADVANCE_APPLICATION_SUCCESS =
            "%1$s has completed %3$d/%4$d rounds for %2$s";
    public static final String MESSAGE_EXCEED_ROUNDS =
            "Cannot advance application beyond the maximum number of rounds for this job";
    public static final String MESSAGE_INVALID_ROUNDS =
            "Number of rounds must be a positive integer";
    public static final String MESSAGE_APPLICATION_NOT_FOUND =
            "No application found at the specified index";
    public static final String MESSAGE_JOB_INVALID_INDEX =
            "Job index is invalid";
    public static final String MESSAGE_APPLICATION_INVALID_INDEX =
            "Application index is invalid";

    private final Index jobIndex;
    private final Index applicationIndex;
    private final int rounds;

    /**
     * Creates an AdvanceApplicationCommand to advance the application for the specified job
     * at the specified application index by the specified number of rounds.
     */
    public AdvanceApplicationCommand(Index jobIndex, Index applicationIndex, int rounds) {
        requireNonNull(jobIndex);
        requireNonNull(applicationIndex);
        this.jobIndex = jobIndex;
        this.applicationIndex = applicationIndex;
        this.rounds = rounds;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (rounds <= 0) {
            throw new CommandException(MESSAGE_INVALID_ROUNDS);
        }

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

        try {
            Application advancedApplication = model.advanceApplication(targetApplication, rounds);

            String successMessage = String.format(MESSAGE_ADVANCE_APPLICATION_SUCCESS,
                    advancedApplication.getApplicant().getName(), job.getJobTitle(),
                    advancedApplication.getApplicationStatus().applicationStatus,
                    job.getJobRounds().jobRounds);

            // Return a CommandResult that signals applications need to be refreshed
            return CommandResult.withRefreshApplications(successMessage);
        } catch (InvalidApplicationStatusException e) {
            throw new CommandException(MESSAGE_EXCEED_ROUNDS);
        } catch (IllegalArgumentException e) {
            throw new CommandException(MESSAGE_INVALID_ROUNDS);
        }
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

        return jobIndex.equals(otherAdvanceApplicationCommand.jobIndex)
                && applicationIndex.equals(otherAdvanceApplicationCommand.applicationIndex)
                && rounds == otherAdvanceApplicationCommand.rounds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("jobIndex", jobIndex)
                .add("applicationIndex", applicationIndex)
                .add("rounds", rounds)
                .toString();
    }
}
