package seedu.address.model.application;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.application.exceptions.InvalidApplicationStatusException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Represents a job application with an applicant, job, and status.
 */
public class Application {
    public static final String EXCEED_ROUNDS_MESSAGE = "Application status cannot exceed the number of job rounds";

    private final Person applicant;
    private final Job job;
    private final ApplicationStatus applicationStatus;

    /**
     * Constructs an Application with the specified applicant, job, and status.
     *
     * @param applicant         The person applying for the job.
     * @param job               The job being applied for.
     * @param applicationStatus The current status of the application.
     * @throws InvalidApplicationStatusException if status exceeds job rounds.
     */
    public Application(Person applicant, Job job, ApplicationStatus applicationStatus) {
        requireAllNonNull(applicant, job, applicationStatus);

        // Validate status against job rounds
        if (applicationStatus.applicationStatus > job.getJobRounds().jobRounds) {
            throw new InvalidApplicationStatusException();
        }

        this.applicant = applicant;
        this.job = job;
        this.applicationStatus = applicationStatus;
    }

    /**
     * Returns the applicant of this application.
     *
     * @return The applicant.
     */
    public Person getApplicant() {
        return this.applicant;
    }

    /**
     * Returns the job of this application.
     *
     * @return The job.
     */
    public Job getJob() {
        return this.job;
    }

    /**
     * Returns the status of this application.
     *
     * @return The application status.
     */
    public ApplicationStatus getApplicationStatus() {
        return this.applicationStatus;
    }

    /**
     * Advances the application status by the specified number of rounds. Returns a
     * new Application with the updated status.
     *
     * @param rounds The number of rounds to advance.
     * @return A new Application with the updated status.
     * @throws IllegalArgumentException          if rounds is negative.
     * @throws InvalidApplicationStatusException if the new status would exceed job
     *                                           rounds.
     */
    public Application advance(int rounds) {
        if (rounds < 0) {
            throw new IllegalArgumentException("Cannot advance by a negative number of rounds");
        }

        int newStatus = this.applicationStatus.applicationStatus + rounds;

        // Validate that the new status doesn't exceed job rounds
        if (newStatus > this.job.getJobRounds().jobRounds) {
            throw new InvalidApplicationStatusException();
        }

        // Create new application with updated status
        return new Application(this.applicant, this.job, new ApplicationStatus(newStatus));
    }

    /**
     * Returns true if this application is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Application)) {
            return false;
        }
        Application otherApplication = (Application) other;
        return applicant.equals(otherApplication.applicant) && job.equals(otherApplication.job)
                && applicationStatus.equals(otherApplication.applicationStatus);
    }

    /**
     * Returns a string representation of the application.
     *
     * @return A string representation.
     */
    @Override
    public String toString() {
        return String.format("Application: %s (Status: %d/%d)", job.getJobTitle(), applicationStatus.applicationStatus,
                job.getJobRounds().jobRounds);
    }

    /**
     * Returns a hash code for this application.
     *
     * @return A hash code value for this application.
     */
    @Override
    public int hashCode() {
        return Objects.hash(applicant, job, applicationStatus);
    }
}
