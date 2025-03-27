package seedu.address.model.company;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the company name in a {@Code Company}.
 */
public record CompanyName(String name) {
    public static final String MESSAGE_CONSTRAINTS = "Company name should only contain alphanumeric characters and"
            + "spaces, and it should not be blank";

    /**
     * The first character of the company name must not be a whitespace, otherwise "
     * " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} ]*";

    /**
     * Constructs a {@code CompanyName}.
     *
     * @param name A valid company name.
     */
    public CompanyName {
        requireNonNull(name);
        checkArgument(isValidCompanyName(name), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid company name.
     *
     * @param test The string to validate.
     * @return True if the given string is a valid company name.
     */
    public static boolean isValidCompanyName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns the string representation of this company name.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return this.name;
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
        if (!(other instanceof CompanyName otherCompanyName)) {
            return false;
        }
        return this.name.equals(otherCompanyName.name);
    }
}
