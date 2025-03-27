package seedu.address.model.application;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Represents a job application with an applicant, job, and status.
 */
public class Application {
    private Person applicant;
    private Job job;
    private final ApplicationStatus applicationStatus;

    /**
     * Constructs an Application with the specified applicant, job, and status.
     * @param applicant         The person applying for the job.
     * @param job               The job being applied for.
     * @param applicationStatus The current status of the application.
     * @throws InvalidApplicationStatusException if status exceeds job rounds.
     */
    public Application(Person applicant, Job job, ApplicationStatus applicationStatus) {
        requireAllNonNull(applicant, job, applicationStatus);
        this.applicant = applicant;
        this.job = job;
        // Validate status against job rounds
        if (applicationStatus.applicationStatus > job.jobRounds().jobRounds) {
            throw new InvalidApplicationStatusException();
        }
        this.applicationStatus = applicationStatus;
    }

    /**
     * Returns the applicant of this application.
     *
     * @return The applicant.
     */
    public Person applicant() {
        return this.applicant;
    }

    /**
     * Returns the job of this application.
     * @return The job.
     */
    public Job job() {
        return this.job;
    }

    /**
     * Returns the status of this application.
     * @return The application status.
     */
    public ApplicationStatus applicationStatus() {
        return this.applicationStatus;
    }

    /**
     * Advances the application status by the specified number of rounds. Returns a
     * new Application with the updated status.
     * @param rounds The number of rounds to advance.
     * @return A new Application with the updated status.
     * @throws IllegalArgumentException          if rounds is negative.
     * @throws InvalidApplicationStatusException if the new status would exceed job rounds.
     */
    public Application advance(int rounds) {
        if (rounds < 0) {
            throw new IllegalArgumentException("Cannot advance by a negative number of rounds");
        }
        int newStatus = this.applicationStatus.applicationStatus + rounds;
        // Validate that the new status doesn't exceed job rounds
        if (newStatus > this.job.jobRounds().jobRounds) {
            throw new InvalidApplicationStatusException();
        }
        // Create new application with updated status
        return new Application(this.applicant, this.job, new ApplicationStatus(newStatus));
    }

    /**
     * Replaces application's dummy Person instance from {@code AddApplicationCommandParser}.
     * @param newApplicant new Person to be updated within the application.
     */
    public void setApplicant(Person newApplicant) {
        requireAllNonNull(applicant);
        this.applicant = newApplicant;
    }

    /**
     * Replaces application's dummy Job instance from {@code AddApplicationCommandParser}.
     * @param newJob new Job to be updated within the application.
     */
    public void setJob(Job newJob) {
        requireAllNonNull(newJob);
        this.job = newJob;
    }

    /**
     * Returns true if this application is the same as the specified object.
     * Given {@code Application} is an association class between {@code Person} and {@code Job}, and at most 1 unique
     * instance per person and job exists in {@code UniqueApplicationList} exists to hold latest candidate's evaluation,
     * this should NOT check for equality of any contained {@code Application}'s fields.
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Application otherApplication)) {
            return false;
        }
        return this.applicant.equals(otherApplication.applicant)
                && this.job.equals(otherApplication.job);
    }

    /**
     * Returns a string representation of the application.
     *
     * @return A string representation.
     */
    @Override
    public String toString() {
        return String.format("Application: %s at %s (Status: %d/%d)",
                this.job.jobTitle(),
                this.job.jobCompany(),
                this.applicationStatus.applicationStatus,
                this.job.jobRounds().jobRounds);
    }
}
