package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book. Guarantees: immutable; is
 * valid as declared in {@link #isValidName(String)}
 */
public class Name {
    public static final String MESSAGE_CONSTRAINTS = "Names should only contain alphanumeric "
            + "characters and spaces, and it should not be blank";
    /* The first character of the name must not be a whitespace, else " " (a blank string) becomes a valid input. */
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";
    /** Initialises dummy person in {@code AddApplicationCommandParser} below. */
    public static final Name DEFAULT_NAME = new Name("DEFAULT NAME");
    public final String fullName;

    /**
     * Constructs a {@code Name}.
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        this.fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof Name otherName)) {
            return false;
        }
        return this.fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return this.fullName.hashCode();
    }
}
