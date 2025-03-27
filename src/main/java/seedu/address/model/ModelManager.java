package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.application.Application;
import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobTitle;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

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
    private final FilteredList<Application> filteredApplications;

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
        this.filteredApplications = new FilteredList<>(this.applicationsManager.getApplicationList());
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

    /**
     * Checks if there exists a Person of the same phone number only in {@code UniquePersonList}, where code ultimately
     * traces to {@code Person::equals}.
     * @param person Person candidate whose existence is checked, wherein all fields other than phone number may differ.
     * @return boolean value indicating success or failure in finding the Person candidate.
     */
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
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
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

    /**
     * Checks if there exists a Job of the same title and company name only in {@code UniqueJobList}, where code
     * ultimately traces to {@code Job::equals}.
     * @param job Job whose existence is checked, wherein all fields other than title and company name may differ.
     * @return boolean value indicating success or failure in finding the Job.
     */
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
        updateFilteredJobList(PREDICATE_SHOW_ALL_JOBS);
    }

    @Override
    public void setJob(Job target, Job editedJob) {
        requireAllNonNull(target, editedJob);

        // Update the job in address book
        addressBook.setJob(target, editedJob);

        // Update any applications that contain this job
        applicationsManager.updateJob(target, editedJob);
    }

    // =========== Application Operations via ApplicationsManager's method invocations
    // =================================================================

    @Override
    public ReadOnlyApplicationsManager getApplicationsManager() {
        return applicationsManager;
    }

    @Override
    public void setApplicationsManager(ReadOnlyApplicationsManager applicationsManager) {
        this.applicationsManager.resetData(applicationsManager);
    }

    /**
     * Checks if there exists an application of the same person via name, same job via title and company name, and same
     * application via application status only in {@code UniquePersonList}, {@code UniqueJobList} and
     * {@code UniqueApplicationList} where code ultimately traces to {@code Application::equals}.
     * @param application Job whose existence is checked, wherein all fields other than person's name, job's title and
     *                   company name, and application's application status may differ.
     * @return boolean value indicating success or failure in finding the Application.
     */
    @Override
    public boolean hasApplication(Application application) {
        requireNonNull(application);
        return applicationsManager.hasApplication(application);
    }

    @Override
    public void deleteApplication(Application target) {
        applicationsManager.removeApplication(target);
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

    /**
     * Advances the given application in-place by modifying contained {@code ApplicationStatus} out-of-place with rounds
     * where code ultimately traces to {@code Application::advance(int)}.
     * @param application Preexisting application to advance its {@code ApplicationStatus}.
     * @param round Specified integer number of rounds to check advancing for {@code Application}.
     * @return Same {@code Application} object albeit with distinct new contained {@code ApplicationStatus}.
     */
    @Override
    public Application advanceApplication(Application application, int round) throws InvalidApplicationStatusException {
        requireNonNull(application);
        return applicationsManager.advanceApplication(application, round);
    }

    // =========== Filtered Person List Accessors
    // =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the
     * internal list of {@code versionedAddressBook}
     * @return Unmodifiable view of persons' list via {@code UniquePersonList::asUnmodifiableObservableList}.
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return this.filteredPersons.getFilteredList();
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        this.filteredPersons.addPredicate(predicate);
    }

    public void resetFilteredPersonList() {
        this.filteredPersons.clearFilters();
    }

    @Override
    public List<Person> getPersonsByPhone(Phone phone) {
        return this.addressBook.getPersonsByPhone(phone);
    }

    // =========== Filtered Job List Accessors
    // ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Job} backed by the
     * internal list of {@code versionedAddressBook}
     * @return Unmodifiable view of jobs' list via {@code UniqueJobList::asUnmodifiableJobList}.
     */
    @Override
    public ObservableList<Job> getFilteredJobList() {
        return this.filteredJobs.getFilteredList();
    }

    @Override
    public void updateFilteredJobList(Predicate<Job> predicate) {
        requireNonNull(predicate);
        this.filteredJobs.addPredicate(predicate);
    }

    public void resetFilteredJobList() {
        filteredJobs.clearFilters();
    }

    @Override
    public List<Job> getJobsByTitle(JobTitle jobTitle) {
        return this.addressBook.getJobsByTitle(jobTitle);
    }

    // =========== Filtered Application List Accessors
    // ================================================================

    /**
     * Returns an unmodifiable view of the list of {@code Application} backed by the
     * internal list of {@code versionedAddressBook}.
     * @return Unmodifiable view of applications' list via {@code UniqueApplicationList::asUnmodifiableApplicationList}.
     */
    @Override
    public ObservableList<Application> getFilteredApplicationList() {
        return this.filteredApplications;
    }

    @Override
    public void updateFilteredApplicationList(Predicate<Application> predicate) {
        requireNonNull(predicate);
        filteredApplications.setPredicate(predicate);
    }

    @Override
    public List<Application> getApplicationsByPerson(Person person) {
        requireNonNull(person);
        return applicationsManager.getApplicationsByPerson(person);
    }

    @Override
    public List<Application> getApplicationsByJob(Job job) {
        requireNonNull(job);
        return applicationsManager.getApplicationsByJob(job);
    }

    @Override
    public List<Application> getApplicationsByPersonAndJob(Person person, Job job) {
        requireAllNonNull(person, job);
        return this.getApplicationsByPerson(person).stream()
                .filter(application ->
                        application.applicant().isSamePerson(person) && application.job().isSameJob(job))
                .collect(Collectors.toList());
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
        return this.addressBook.equals(otherModelManager.addressBook)
                && this.applicationsManager.equals(otherModelManager.applicationsManager)
                && this.userPrefs.equals(otherModelManager.userPrefs)
                && this.filteredPersons.equals(otherModelManager.filteredPersons)
                && this.filteredJobs.equals(otherModelManager.filteredJobs)
                && this.filteredApplications.equals(otherModelManager.filteredApplications);
    }
}
