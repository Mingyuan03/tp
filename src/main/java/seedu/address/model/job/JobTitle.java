package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents a job title in a {@Code Job}.
 */
public record JobTitle(String jobTitle) {
    public static final String MESSAGE_CONSTRAINTS = "Job title should only contain alphanumeric characters and spaces,"
            + " and it should not be blank";

    /**
     * The first character of the job title must not be a whitespace, otherwise " "
     * (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";

    /**
     * Constructs a {@code JobTitle}.
     *
     * @param jobTitle A valid job title.
     */
    public JobTitle {
        requireNonNull(jobTitle);
        checkArgument(isValidJobTitle(jobTitle), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid job title.
     *
     * @param test The string to validate.
     * @return True if the given string is a valid job title.
     */
    public static boolean isValidJobTitle(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the string representation of this job title.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return StringUtil.toTitleCase(this.jobTitle);
    }

    /**
     * Returns true if this job title is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof JobTitle otherJobTitle)) {
            return false;
        }
        return this.jobTitle.equals(otherJobTitle.jobTitle);
    }

}
