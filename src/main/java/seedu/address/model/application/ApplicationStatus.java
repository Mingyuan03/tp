package seedu.address.model.application;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the status of a job application. The status represents which round
 * has been completed up to.
 */
public class ApplicationStatus implements Comparable<ApplicationStatus> {
    public static final String MESSAGE_CONSTRAINTS = "Application status should be a non-negative integer";
    /** Initialises dummy application in {@code AddApplicationCommandParser} below. */
    public static final ApplicationStatus DEFAULT_ADDAPPLICATIONSTATUS = new ApplicationStatus(0);
    public static final ApplicationStatus DEFAULT_ADVANCEAPPLICATIONSTATUS = new ApplicationStatus(1);

    /**
     * The status must be a non-negative integer. 0 means applied but no rounds
     * completed yet. A positive integer n means completed up to round n.
     */
    public static final String VALIDATION_REGEX = "^[0-9]\\d*$";
    public final int applicationStatus;

    /**
     * Constructs an {@code ApplicationStatus}.
     *
     * @param applicationStatus A valid application status.
     */
    public ApplicationStatus(int applicationStatus) {
        checkArgument(isValidApplicationStatus(applicationStatus), MESSAGE_CONSTRAINTS);
        this.applicationStatus = applicationStatus;
    }

    /**
     * Constructs an {@code ApplicationStatus} from a string.
     *
     * @param applicationStatus A valid application status as a string.
     */
    public ApplicationStatus(String applicationStatus) {
        requireNonNull(applicationStatus);
        checkArgument(isValidApplicationStatus(applicationStatus), MESSAGE_CONSTRAINTS);
        this.applicationStatus = Integer.parseInt(applicationStatus);
    }

    /**
     * Returns true if a given integer is a valid application status.
     *
     * @param test The integer to validate.
     * @return True if the given integer is a valid application status.
     */
    public static boolean isValidApplicationStatus(int test) {
        return test >= 0;
    }

    /**
     * Returns true if a given string is a valid application status.
     *
     * @param test The string to validate.
     * @return True if the given string is a valid application status.
     */
    public static boolean isValidApplicationStatus(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the string representation of the application status.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return String.valueOf(this.applicationStatus);
    }

    /**
     * Returns true if this application status is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ApplicationStatus otherApplicationStatus)) {
            return false;
        }
        return this.applicationStatus == otherApplicationStatus.applicationStatus;
    }

    /**
     * Returns the hash code of this application status.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(this.applicationStatus);
    }

    @Override
    public int compareTo(ApplicationStatus other) {
        return Integer.compare(this.applicationStatus, other.applicationStatus);
    }
}
