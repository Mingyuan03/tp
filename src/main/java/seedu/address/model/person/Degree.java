package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's degree in the address book.
 * Guarantees: immutable; is valid always.
 */
public class Degree {

    public static final String MESSAGE_CONSTRAINTS = "Degree can take any values, and it should not be blank";
    /** Initialises dummy person in {@code AddApplicationCommandParser} below. */
    public static final Degree DEFAULT_DEGREE = new Degree("DEFAULT_DEGREE");
    public final String value;

    /**
     * Constructs an {@code Degree}.
     * @param degree A valid degree.
     */
    public Degree(String degree) {
        requireNonNull(degree);
        this.value = degree;
    }

    @Override
    public String toString() {
        return this.value;
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
