package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.job.exceptions.DuplicateSkillException;

/**
 * Represents a job's requisite list of skills in the address book.
 * Guarantees: immutable; is valid as declared in {@link #areValidIndividualJobSkills(ObservableList)}}
 */
public class JobSkills {
    public static final String MESSAGE_CONSTRAINTS = "Each job skill should only contain alphanumeric characters, "
            + "spaces, and the symbols '/', '+', '-', or '.'. It should not be blank";
    /*
     * The first character of the name must not be a whitespace, otherwise " " (a
     * blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\p{Alnum}[\\p{Alnum} /+\\-.]*";
    /** Initialises dummy job in {@code AddApplicationCommandParser} below. */
    public static final JobSkills DEFAULT_JOBSKILLS =
            new JobSkills(FXCollections.observableArrayList("DEFAULT-SKILL"));
    public final ObservableList<String> value = FXCollections.observableArrayList();

    /**
     * Constructs a {@Code JobSkills}.
     *
     * @param newJobSkills  The list of job skills to populate.
     */
    public JobSkills(ObservableList<String> newJobSkills) {
        for (String skill : newJobSkills) {
            requireNonNull(skill);
            checkArgument(skill.matches(VALIDATION_REGEX), MESSAGE_CONSTRAINTS);
            this.value.add(skill);
        }
        if (!this.areSkillsUnique()) {
            throw new DuplicateSkillException();
        }
    }

    public static boolean areValidIndividualJobSkills(ObservableList<String> jobSkills) {
        return jobSkills.stream().allMatch(jobSkill -> jobSkill.matches(VALIDATION_REGEX));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof JobSkills otherJobSkills) || this.value.size() != otherJobSkills.value.size()) {
            return false;
        }
        for (int i = 0; i < this.value.size(); i++) {
            if (!this.value.get(i).equals(otherJobSkills.value.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * Returns true if {@code jobSkills} contains only unique skills.
     */
    private boolean areSkillsUnique() {
        for (int i = 0; i < value.size() - 1; i++) {
            for (int j = i + 1; j < value.size(); j++) {
                if (value.get(i).equals(value.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
