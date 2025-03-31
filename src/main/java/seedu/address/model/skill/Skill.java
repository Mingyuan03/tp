package seedu.address.model.skill;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Skill in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidSkillName(String)}
 */
public record Skill(String skillName) {

    public static final String MESSAGE_CONSTRAINTS = "Skill names should be alphanumeric with '.' and '/' allowed";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}./]+";

    /**
     * Constructs a {@code Skill}.
     *
     * @param skillName A valid skill name.
     */
    public Skill {
        requireNonNull(skillName);
        checkArgument(isValidSkillName(skillName), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid skill name.
     */
    public static boolean isValidSkillName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Skill otherSkill)) {
            return false;
        }
        return this.skillName.equals(otherSkill.skillName);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return this.skillName;
    }

}
