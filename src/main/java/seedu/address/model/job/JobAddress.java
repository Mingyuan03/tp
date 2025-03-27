package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a job's workplace address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidJobAddress(String)}
 */
public class JobAddress {
    public static final String MESSAGE_CONSTRAINTS = "Job addresses can take any values, and it should not be blank";
    /** The first character of the address must not be a whitespace, else " " (a blank string) becomes a valid input. */
    public static final String VALIDATION_REGEX = "\\S.*";
    /** Initialises dummy job in {@code AddApplicationCommandParser} below. */
    public static final JobAddress DEFAULT_JOBADDRESS = new JobAddress("DEFAULT_JOB_ADDRESS");
    public final String value;

    /**
     * Constructs a {@code jobAddress}.
     *
     * @param jobAddress A valid job address.
     */
    public JobAddress(String jobAddress) {
        requireNonNull(jobAddress);
        checkArgument(isValidJobAddress(jobAddress), MESSAGE_CONSTRAINTS);
        this.value = jobAddress;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidJobAddress(String test) {
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
        if (!(other instanceof JobAddress otherJobAddress)) {
            return false;
        }
        return this.value.equals(otherJobAddress.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
