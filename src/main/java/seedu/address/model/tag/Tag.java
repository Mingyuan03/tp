package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public record Tag(String tagName) {
    public static final String MESSAGE_CONSTRAINTS = "Tag names should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";
    /** Initialises dummy person in {@code AddApplicationCommandParser} below. */
    public static final Tag DEFAULT_TAG = new Tag("DEFAULTTAG");

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tag otherTag)) {
            return false;
        }
        return this.tagName.equals(otherTag.tagName);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + this.tagName + ']';
    }

}
