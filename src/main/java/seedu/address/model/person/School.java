package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is valid always.
 */
public class School {

    public static final String MESSAGE_CONSTRAINTS = "School should only contain alphanumeric "
            + "characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs an {@code School}.
     *
     * @param school A valid school.
     */
    public School(String school) {
        requireNonNull(school);
        checkArgument(isValidSchool(school), MESSAGE_CONSTRAINTS);
        this.value = school;
    }

    /**
     * Returns true if a given string is a valid school.
     */
    public static boolean isValidSchool(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value.toUpperCase();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof School otherSchool)) {
            return false;
        }

        return this.value.trim().equals(otherSchool.value.trim()); // Ignore leading or trailing whitespaces.
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
