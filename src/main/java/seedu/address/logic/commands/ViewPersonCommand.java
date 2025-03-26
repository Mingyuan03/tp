package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_INDEX;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Shows detailed information about a person from a specific job.
 */
public class ViewPersonCommand extends Command {

    public static final String COMMAND_WORD = "viewperson";
    private static final Logger logger = LogsCenter.getLogger(ViewPersonCommand.class);

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views detailed information for the person identified by the job index and person index within that job.\n"
            + "Parameters: " + PREFIX_JOB_INDEX + "JOB_INDEX " + PREFIX_PERSON_INDEX + "PERSON_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_JOB_INDEX + "1 " + PREFIX_PERSON_INDEX + "2";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Viewing Person: %1$s (from Job: %2$s)";
    public static final String MESSAGE_NOT_IN_JOB_VIEW = "This command is only available in job-related views. Please switch to job view first using 'switchview' command.";
    public static final String MESSAGE_NO_SUCH_PERSON = "No person with index %1$d found for job with index %2$d.";

    private final Index jobIndex;
    private final Index personIndex;

    /**
     * @param jobIndex Index of the job in the filtered job list
     * @param personIndex Index of the person for that job
     */
    public ViewPersonCommand(Index jobIndex, Index personIndex) {
        requireNonNull(jobIndex);
        requireNonNull(personIndex);
        this.jobIndex = jobIndex;
        this.personIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Log the current view state for debugging
        logger.info("ViewPersonCommand executed. Current ViewState: " + model.getCurrentViewState());
        logger.info("isInJobView() returns: " + model.isInJobView());

        // Check if we're in job view
        if (!model.isInJobView()) {
            logger.warning("Command failed: Not in job view. Current state: " + model.getCurrentViewState());
            throw new CommandException(MESSAGE_NOT_IN_JOB_VIEW);
        }

        // Get the job
        List<Job> jobList = model.getFilteredJobList();
        if (jobIndex.getZeroBased() >= jobList.size()) {
            logger.warning("Command failed: Invalid job index: " + jobIndex.getOneBased());
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }
        Job targetJob = jobList.get(jobIndex.getZeroBased());
        logger.info("Found target job: " + targetJob.getJobTitle().jobTitle());

        // Get the application/person - use filtered applications to respect status filters
        List<Application> applications = model.getApplicationsByJob(targetJob);
        if (applications == null || personIndex.getZeroBased() >= applications.size()) {
            logger.warning("Command failed: Invalid person index: " + personIndex.getOneBased() 
                    + " for job index: " + jobIndex.getOneBased());
            throw new CommandException(
                    String.format(MESSAGE_NO_SUCH_PERSON, personIndex.getOneBased(), jobIndex.getOneBased()));
        }
        
        Application targetApplication = applications.get(personIndex.getZeroBased());
        Person targetPerson = targetApplication.applicant();
        logger.info("Found target person: " + targetPerson.getName());

        // Set the model state
        model.setViewState(Model.ViewState.PERSON_DETAIL_VIEW);
        logger.info("View state updated to PERSON_DETAIL_VIEW");

        return new CommandResult(
                String.format(MESSAGE_VIEW_PERSON_SUCCESS, targetPerson.getName(), targetJob.getJobTitle().jobTitle()),
                false,
                false,
                false,
                false,
                true, // Set viewing person flag
                jobIndex.getZeroBased(),
                personIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewPersonCommand)) {
            return false;
        }

        ViewPersonCommand otherCommand = (ViewPersonCommand) other;
        return jobIndex.equals(otherCommand.jobIndex)
                && personIndex.equals(otherCommand.personIndex);
    }
} 