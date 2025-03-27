package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the company name in a {@Code Job}.
 */
public record JobCompany(String jobCompany) {
    public static final String MESSAGE_CONSTRAINTS = "Company name should only contain alphanumeric characters and "
            + "spaces, and it should not be blank";
    /** The 1st character of company name must not be a whitespace, else " " (a blank string) becomes a valid input. */
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";
    /** Initialises dummy job in {@code AddApplicationCommandParser} below. */
    public static final JobCompany DEFAULT_JOBCOMPANY = new JobCompany("DEFAULT JOB COMPANY");

    /**
     * Constructs a {@code JobCompany}.
     * @param jobCompany A valid company name.
     */
    public JobCompany {
        requireNonNull(jobCompany);
        checkArgument(isValidJobCompany(jobCompany), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid company name.
     *
     * @param test The string to validate.
     * @return True if the given string is a valid company name.
     */
    public static boolean isValidJobCompany(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the string representation of this company name.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return this.jobCompany;
    }

    /**
     * Returns true if this company name is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JobCompany otherJobCompany)) {
            return false;
        }
        return this.jobCompany.equals(otherJobCompany.jobCompany);
    }

}
