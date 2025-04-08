package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's degree in the address book.
 * Guarantees: immutable; is valid always.
 */
public class Degree {

    public static final String MESSAGE_CONSTRAINTS = "Degree should only contain alphanumeric "
            + "characters and spaces, and it should not be blank";

    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";

    public final String value;

    /**
     * Constructs an {@code Degree}.
     *
     * @param degree A valid degree.
     */
    public Degree(String degree) {
        requireNonNull(degree);
        checkArgument(isValidDegree(degree), MESSAGE_CONSTRAINTS);
        this.value = degree;
    }

    /**
     * Returns true if a given string is a valid degree.
     */
    public static boolean isValidDegree(String test) {
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
        if (!(other instanceof Degree otherDegree)) {
            return false;
        }

        return this.value.trim().equals(otherDegree.value.trim()); // Ignore leading or trailing whitespaces.
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
