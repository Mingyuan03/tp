package seedu.address.model.company;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Company's workplace address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCompanyAddress(String)}
 */
public class CompanyAddress {

    public static final String MESSAGE_CONSTRAINTS = "Company addresses can take any values,"
            + "and it should not be blank";
    /** The first character of the address must not be a whitespace, else " " (a blank string) becomes a valid input. */
    public static final String VALIDATION_REGEX = "\\S.*";
    /** Initialises dummy Company in {@code AddApplicationCommandParser} below. */
    public static final CompanyAddress DEFAULT_COMPANYADDRESS = new CompanyAddress("DEFAULT_COMPANY_ADDRESS");
    public final String value;

    /**
     * Constructs a {@code companyAddress}.
     *
     * @param companyAddress A valid company address.
     */
    public CompanyAddress(String companyAddress) {
        requireNonNull(companyAddress);
        checkArgument(isValidCompanyAddress(companyAddress), MESSAGE_CONSTRAINTS);
        this.value = companyAddress;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidCompanyAddress(String test) {
        return test.matches(VALIDATION_REGEX);
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
        if (!(other instanceof CompanyAddress otherCompanyAddress)) {
            return false;
        }
        return this.value.equals(otherCompanyAddress.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
