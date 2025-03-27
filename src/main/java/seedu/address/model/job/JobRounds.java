package seedu.address.model.job;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the max number of interview rounds in a {@Code Job}, serving as an upper limit for {@Code Application}.
 * Guarantees: immutable, is natural number (>0) always.
 */
public class JobRounds {
    public static final String MESSAGE_CONSTRAINTS = "Job rounds should be a positive integer";
    /** Initialises dummy job in {@code AddApplicationCommandParser} below. */
    public static final JobRounds DEFAULT_JOBROUNDS = new JobRounds(Integer.MAX_VALUE);
    public final int jobRounds; // Primitive int eliminates necessity of this being a normal class.

    /**
     * Constructs a {@code JobRounds}.
     *
     * @param rounds A valid number of job rounds.
     */
    public JobRounds(int rounds) {
        checkArgument(isValidJobRounds(rounds), MESSAGE_CONSTRAINTS);
        this.jobRounds = rounds;
    }

    /**
     * Returns true if a given integer is a valid number of job rounds.
     *
     * @param test The integer to validate.
     * @return True if the given integer is a valid number of job rounds.
     */
    public static boolean isValidJobRounds(int test) {
        return test > 0;
    }

    /**
     * Returns the string representation of the number of job rounds.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return String.valueOf(jobRounds);
    }

    /**
     * Returns true if this job rounds is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JobRounds otherJobRounds)) {
            return false;
        }
        return this.jobRounds == otherJobRounds.jobRounds;
    }

    /**
     * Returns the hash code of this job rounds.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(jobRounds);
    }
}
