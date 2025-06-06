package seedu.address.logic;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model.ViewState;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the AddressBook.
     *
     * @see seedu.address.model.Model#getAddressBook()
     */
    ReadOnlyAddressBook getAddressBook();

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of jobs */
    ObservableList<Job> getFilteredJobList();

    /** Returns an unmodifiable view of the filtered list of applications */
    ObservableList<Application> getFilteredApplicationList();

    /** Returns filtered list of applications for a job */
    List<Application> getApplicationsByJob(Job job);

    /** Returns filtered list of applications for a job that match current application filters */
    List<Application> getFilteredApplicationsByJob(Job job);

    /** Returns filtered list of applications for a person */
    List<Application> getApplicationsByPerson(Person person);

    /** Returns filtered list of applications for a person that match current application filters */
    List<Application> getFilteredApplicationsByPerson(Person person);

    /**
     * Updates the filter of the filtered application list to filter by the given
     * {@code predicate}.
     *
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredApplicationList(Predicate<Application> predicate);

    /**
     * Resets the filter of the filtered application list to show all applications
     */
    void resetFilteredApplicationList();

    /**
     * Resets the filter of the filtered job list to show all jobs
     */
    void resetFilteredJobList();

    /**
     * Returns the previous command relative to current pointer in the command history.
     */
    String getPrevCommand();

    /**
     * Returns the next command relative to current pointer in the command history.
     */
    String getNextCommand();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Sets the current view state.
     */
    void setViewState(ViewState viewState);

    /**
     * Gets the current view state.
     * @return The current view state.
     */
    ViewState getViewState();

    /**
     * Reapplies any active job filters based on current application filters.
     * This ensures that jobs without matching applications are filtered out properly.
     */
    void reapplyJobFilters();
}
