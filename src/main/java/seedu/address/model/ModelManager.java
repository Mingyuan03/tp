package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final ApplicationsManager applicationsManager;
    private final UserPrefs userPrefs;
    private final StackableFilteredList<Person> filteredPersons;
    private final StackableFilteredList<Job> filteredJobs;
    private final StackableFilteredList<Application> filteredApplications;
    private ViewState currentViewState = ViewState.JOB_VIEW; // Default to person view
    private final DoublyLinkedList commandHistory;

    /**
     * Initializes a ModelManager with the given addressBook, applicationsManager,
     * and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyApplicationsManager applicationsManager,
            ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, applicationsManager, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + ", applications manager: " + applicationsManager
                + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.applicationsManager = new ApplicationsManager(applicationsManager);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredPersons = new StackableFilteredList<>(this.addressBook.getPersonList());
        this.filteredJobs = new StackableFilteredList<>(this.addressBook.getJobList());
        this.filteredApplications = new StackableFilteredList<>(this.applicationsManager.getApplicationList());
        commandHistory = new DoublyLinkedList();
    }

    public ModelManager() {
        this(new AddressBook(), new ApplicationsManager(), new UserPrefs());
    }

    // =========== UserPrefs
    // ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    @Override
    public Path getApplicationsManagerFilePath() {
        return userPrefs.getApplicationsManagerFilePath();
    }

    @Override
    public void setApplicationsManagerFilePath(Path applicationsManagerFilePath) {
        requireNonNull(applicationsManagerFilePath);
        userPrefs.setApplicationsManagerFilePath(applicationsManagerFilePath);
    }

    // =========== Command History
    // ================================================================================

    @Override
    public void addCommand(String command) {
        commandHistory.add(command);
        commandHistory.reset();
    }

    @Override
    public String getPrevCommand() {
        commandHistory.moveBack();
        return commandHistory.getCommand();
    }

    @Override
    public String getNextCommand() {
        commandHistory.moveForward();
        return commandHistory.getCommand();
    }

    // =========== AddressBook
    // ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    // =========== Person Operations
    // ==========================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        // First remove any applications associated with this person
        applicationsManager.removePersonApplications(target);
        // Then remove the person
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        resetFilteredPersonList();
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        // Update the person in address book
        addressBook.setPerson(target, editedPerson);

        // Update any applications that contain this person
        applicationsManager.updatePerson(target, editedPerson);
    }

    // =========== Job Operations
    // =============================================================================

    @Override
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return addressBook.hasJob(job);
    }

    @Override
    public void deleteJob(Job target) {
        // First remove any applications associated with this job
        applicationsManager.removeJobApplications(target);
        // Then remove the job
        addressBook.removeJob(target);
    }

    @Override
    public void addJob(Job job) {
        addressBook.addJob(job);
        resetFilteredJobList();
    }

    @Override
    public void setJob(Job target, Job editedJob) {
        requireAllNonNull(target, editedJob);

        // Update the job in address book
        addressBook.setJob(target, editedJob);

        // Update any applications that contain this job
        applicationsManager.updateJob(target, editedJob);
    }

    // =========== Filtered Person List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons.getFilteredList();
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.addPredicate(predicate);
    }

    public void resetFilteredPersonList() {
        filteredPersons.clearFilters();
    }

    // =========== Filtered Job List Accessors
    // ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the
     * internal list of {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return filteredJobs.getFilteredList();
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        filteredJobs.addPredicate(predicate);
    }

    public void resetFilteredJobList() {
        filteredJobs.clearFilters();
    }

    // =========== ApplicationsManager Methods
    // =================================================================

    @Override
    public ReadOnlyApplicationsManager getApplicationsManager() {
        return applicationsManager;
    }

    @Override
    public void setApplicationsManager(ReadOnlyApplicationsManager applicationsManager) {
        this.applicationsManager.resetData(applicationsManager);
    }

    @Override
    public boolean hasApplication(Application application) {
        requireNonNull(application);
        return applicationsManager.hasApplication(application);
    }

    @Override
    public void deleteApplication(Application target) {
        applicationsManager.deleteApplication(target);
    }

    @Override
    public void addApplication(Application application) {
        applicationsManager.addApplication(application);
        updateFilteredApplicationList(PREDICATE_SHOW_ALL_APPLICATIONS);
    }

    @Override
    public void setApplication(Application target, Application editedApplication) {
        requireAllNonNull(target, editedApplication);
        applicationsManager.setApplication(target, editedApplication);
    }

    @Override
    public Application advanceApplication(Application application, int rounds) {
        requireNonNull(application);
        return applicationsManager.advanceApplication(application, rounds);
    }

    @Override
    public ObservableList<Application> getFilteredApplicationList() {
        return filteredApplications.getFilteredList();
    }

    @Override
    public void updateFilteredApplicationList(Predicate<Application> predicate) {
        requireNonNull(predicate);
        filteredApplications.addPredicate(predicate);
    }

    @Override
    public void resetFilteredApplicationList() {
        filteredApplications.clearFilters();
    }

    @Override
    public boolean isInJobView() {
        return currentViewState == ViewState.JOB_VIEW
                || currentViewState == ViewState.JOB_DETAIL_VIEW
                || currentViewState == ViewState.PERSON_DETAIL_VIEW;
    }

    @Override
    public ViewState getCurrentViewState() {
        return currentViewState;
    }

    @Override
    public void setViewState(ViewState viewState) {
        this.currentViewState = viewState;
    }

    @Override
    public ViewState getViewState() {
        return this.currentViewState;
    }

    /**
     * Toggles between person view and job view.
     * If in any job view, switches to person view.
     * If in person view, switches to job view.
     */
    public void toggleJobView() {
        if (isInJobView()) {
            setViewState(ViewState.PERSON_VIEW);
        } else {
            setViewState(ViewState.JOB_VIEW);
        }
    }

    @Override
    public List<Application> getApplicationsByJob(Job job) {
        requireNonNull(job);
        return applicationsManager.getApplicationsByJob(job);
    }

    @Override
    public List<Application> getFilteredApplicationsByJob(Job job) {
        requireNonNull(job);
        // Get all applications for the job
        List<Application> allJobApplications = applicationsManager.getApplicationsByJob(job);

        // Filter using the current application filters by testing against the filtered
        // list
        return allJobApplications.stream()
                .filter(app -> filteredApplications.getFilteredList().contains(app))
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> getApplicationsByPerson(Person person) {
        requireNonNull(person);
        return applicationsManager.getApplicationsByPerson(person);
    }

    @Override
    public List<Application> getFilteredApplicationsByPerson(Person person) {
        requireNonNull(person);
        // Get all applications for the person
        List<Application> allPersonApplications = applicationsManager.getApplicationsByPerson(person);

        // Filter using the current application filters by testing against the filtered
        // list
        return allPersonApplications.stream()
                .filter(app -> filteredApplications.getFilteredList().contains(app))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Application> getApplicationByPersonAndJob(Person person, Job job) {
        requireAllNonNull(person, job);
        return applicationsManager.getApplicationByPersonAndJob(person, job);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }
        return addressBook.equals(otherModelManager.addressBook)
                && applicationsManager.equals(otherModelManager.applicationsManager)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && filteredJobs.equals(otherModelManager.filteredJobs)
                && filteredApplications.equals(otherModelManager.filteredApplications);
    }

    @Override
    public void reapplyJobFilters() {
        logger.info("Reapplying job filters based on application filters");

        // Get the current filtered applications list
        ObservableList<Application> currentFilteredApps = filteredApplications.getFilteredList();

        // If application filters are active (list size is different from total)
        if (currentFilteredApps.size() < applicationsManager.getApplicationList().size()) {
            // Find jobs that have at least one application in the filtered application list
            List<Job> jobsWithMatchingApplications = addressBook.getJobList().stream()
                    .filter(job -> {
                        List<Application> jobApps = applicationsManager.getApplicationsByJob(job);
                        return jobApps.stream().anyMatch(currentFilteredApps::contains);
                    })
                    .collect(Collectors.toList());

            // Clear existing job filters and apply the new filter
            filteredJobs.clearFilters();
            updateFilteredJobList(jobsWithMatchingApplications::contains);

            logger.info("After reapplying filters, job list size: " + filteredJobs.getFilteredList().size());
        }
    }
}
