package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** Whether the job view should be shown. */
    private final boolean toggleView;

    /** Whether the job view should be shown. */
    private final boolean viewJob;

    /** Whether the person view should be shown. */
    private final boolean viewPerson;

    /** Whether to clear the current view. */
    private final boolean clearView;

    /** Whether to refresh the job view. */
    private final boolean refreshJobView;

    /** Job index. */
    private final int jobIndex;

    /** Person index. */
    private final int personIndex;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean toggleView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.toggleView = toggleView;
        this.viewJob = false;
        this.viewPerson = false;
        this.clearView = false;
        this.refreshJobView = false;
        this.jobIndex = -1;
        this.personIndex = -1;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields including job viewing.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean toggleView,
                         boolean viewJob, int jobIndex) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.toggleView = toggleView;
        this.viewJob = viewJob;
        this.viewPerson = false;
        this.clearView = false;
        this.refreshJobView = false;
        this.jobIndex = jobIndex;
        this.personIndex = -1;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields including person viewing.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, boolean toggleView,
                         boolean viewJob, boolean viewPerson, int jobIndex, int personIndex) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.toggleView = toggleView;
        this.viewJob = viewJob;
        this.viewPerson = viewPerson;
        this.clearView = false;
        this.refreshJobView = false;
        this.jobIndex = jobIndex;
        this.personIndex = personIndex;
    }

    /**
     * Constructs a {@code CommandResult} with the clear view flag set.
     */
    public CommandResult(String feedbackToUser, boolean clearView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.toggleView = false;
        this.viewJob = false;
        this.viewPerson = false;
        this.clearView = clearView;
        this.refreshJobView = false;
        this.jobIndex = -1;
        this.personIndex = -1;
    }

    /**
     * Constructs a {@code CommandResult} with the clear view and refresh job view flags set.
     */
    public CommandResult(String feedbackToUser, boolean clearView, boolean refreshJobView) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = false;
        this.exit = false;
        this.toggleView = false;
        this.viewJob = false;
        this.viewPerson = false;
        this.clearView = clearView;
        this.refreshJobView = refreshJobView;
        this.jobIndex = -1;
        this.personIndex = -1;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean setToggleView() {
        return toggleView;
    }

    public boolean isViewJob() {
        return viewJob;
    }

    public boolean isViewPerson() {
        return viewPerson;
    }

    public boolean isClearView() {
        return clearView;
    }

    public boolean isRefreshJobView() {
        return refreshJobView;
    }

    public int getJobIndex() {
        return jobIndex;
    }

    public int getPersonIndex() {
        return personIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && toggleView == otherCommandResult.toggleView
                && viewJob == otherCommandResult.viewJob
                && viewPerson == otherCommandResult.viewPerson
                && clearView == otherCommandResult.clearView
                && refreshJobView == otherCommandResult.refreshJobView
                && jobIndex == otherCommandResult.jobIndex
                && personIndex == otherCommandResult.personIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, toggleView, viewJob, viewPerson, clearView,
                refreshJobView, jobIndex, personIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("toggleView", toggleView)
                .add("viewJob", viewJob)
                .add("viewPerson", viewPerson)
                .add("clearView", clearView)
                .add("refreshJobView", refreshJobView)
                .add("jobIndex", jobIndex)
                .add("personIndex", personIndex)
                .toString();
    }
}
