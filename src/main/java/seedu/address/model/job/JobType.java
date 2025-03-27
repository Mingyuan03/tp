package seedu.address.model.job;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a job's employment type in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDisplayType(String)}
 */
public enum JobType {
    INTERN("Intern"),
    PART_TIME("Part Time"),
    FULL_TIME("Full Time"),
    FREELANCE("Freelance"),
    CONTRACT("Contract");
    public static final String MESSAGE_CONSTRAINTS = "Job type should be one of the valid job types: "
            + Arrays.stream(JobType.values()).map(JobType::toString).collect(Collectors.joining(", "));
    /** Initialises dummy job in {@code AddApplicationCommandParser} below. */
    public static final JobType DEFAULT_JOBTYPE = JobType.fromDisplayType("Intern");
    private final String displayType;

    JobType(String newDisplayType) { // Implicitly private JobType enum constructor.
        this.displayType = newDisplayType;
    }

    /**
     * Constructs a {@code JobType} from {@code displayType} accounting for case differences.
     * @param displayType  The employment type of candidates this job is seeking.
     */
    public static JobType fromDisplayType(String displayType) {
        return Arrays.stream(JobType.values())
                .filter(jobType -> jobType.toString().equalsIgnoreCase(displayType.trim()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(MESSAGE_CONSTRAINTS));
    }

    public static boolean isValidDisplayType(String displayType) {
        return Arrays.stream(JobType.values()).anyMatch(jobType -> jobType.displayType.equals(displayType));
    }

    @Override
    public String toString() {
        return this.displayType;
    }
}
