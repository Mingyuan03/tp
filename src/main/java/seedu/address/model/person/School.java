package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is valid always.
 */
public class School {
    public static final String MESSAGE_CONSTRAINTS = "Remarks can take any values, and it should not be blank";
    /** Initialises dummy person in {@code AddApplicationCommandParser} below. */
    public static final School DEFAULT_SCHOOL = new School("DEFAULT_SCHOOL");
    public final String value;

    /**
     * Constructs an {@code Address}.
     * @param school A valid address.
     */
    public School(String school) {
        requireNonNull(school);
        this.value = school;
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
