package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

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

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views detailed information for the person identified by the job index and person index within that job.\n"
            + "Parameters: JOB_INDEX PERSON_INDEX (both must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_VIEW_PERSON_SUCCESS = "Viewing Person: %1$s (from Job: %2$s)";
    public static final String MESSAGE_NOT_IN_JOB_VIEW = "Please switch to job view first using 'switchview' command.";
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

        // Check if we're in job view
        if (!model.isInJobView()) {
            throw new CommandException(MESSAGE_NOT_IN_JOB_VIEW);
        }

        // Get the job
        List<Job> jobList = model.getFilteredJobList();
        if (jobIndex.getZeroBased() >= jobList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOB_DISPLAYED_INDEX);
        }
        Job targetJob = jobList.get(jobIndex.getZeroBased());

        // Get the application/person
        List<Application> applications = model.getApplicationsByJob(targetJob);
        if (applications == null || personIndex.getZeroBased() >= applications.size()) {
            throw new CommandException(
                    String.format(MESSAGE_NO_SUCH_PERSON, personIndex.getOneBased(), jobIndex.getOneBased()));
        }
        
        Application targetApplication = applications.get(personIndex.getZeroBased());
        Person targetPerson = targetApplication.applicant();

        // Set the model state
        model.setViewState(Model.ViewState.PERSON_DETAIL_VIEW);

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