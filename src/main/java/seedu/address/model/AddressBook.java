package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.UniqueJobList;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by
 * Person::isSamePerson comparison and Job::equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {
    private final UniquePersonList persons;
    private final UniqueJobList jobs;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication between constructors. See
     * https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other
     * ways to avoid duplication among constructors.
     */
    {
        persons = new UniquePersonList();
        jobs = new UniqueJobList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the job list with {@code jobs}. {@code jobs} must
     * not contain duplicate jobs.
     */
    public void setJobs(List<Job> jobs) {
        this.jobs.setJobs(jobs);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setJobs(newData.getJobList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in
     * the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return this.persons.contains(person);
    }

    /**
     * Adds a person to the address book. The person must not already exist in the
     * address book.
     */
    public void addPerson(Person p) {
        this.persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with
     * {@code editedPerson}. {@code target} must exist in the address book. The
     * person identity of {@code editedPerson} must not be the same as another
     * existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Gets all persons associated with a specific phone number.
     * @param phone The person whose name to retrieve.
     * @return A List of persons associated with the phone number.
     */
    public List<Person> getPersonsByPhone(Phone phone) {
        requireNonNull(phone);
        return this.persons.asUnmodifiableObservableList().stream()
                .filter(person -> person.getPhone().equals(phone))
                .collect(Collectors.toList());
    }

    //// job-level operations

    /**
     * Returns true if a job with the same identity as {@code job} exists in the
     * address book.
     */
    public boolean hasJob(Job job) {
        requireNonNull(job);
        return jobs.contains(job);
    }

    /**
     * Adds a job to the address book. The job must not already exist in the address
     * book.
     */
    public void addJob(Job job) {
        jobs.add(job);
    }

    /**
     * Replaces the given job {@code target} in the list with {@code editedJob}.
     * {@code target} must exist in the address book. The job identity of
     * {@code editedJob} must not be the same as another existing job in the address
     * book.
     */
    public void setJob(Job target, Job editedJob) {
        requireNonNull(editedJob);

        jobs.setJob(target, editedJob);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in
     * the address book.
     */
    public void removeJob(Job key) {
        jobs.remove(key);
    }

    /**
     * Gets all jobs associated with a specific title and company name.
     * @param jobTitle The title whose jobs to retrieve.
     * @return A list of jobs associated with the title and company name.
     */
    public List<Job> getJobsByTitle(JobTitle jobTitle) {
        requireNonNull(jobTitle);
        return this.jobs.asUnmodifiableObservableList().stream()
                .filter(job -> job.jobTitle().equals(jobTitle))
                .collect(Collectors.toList());
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("persons", persons).toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Job> getJobList() {
        return jobs.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }
        return this.persons.equals(otherAddressBook.persons) && this.jobs.equals(otherAddressBook.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(persons, jobs);
    }
}
