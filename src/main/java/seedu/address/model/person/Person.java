package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book. Guarantees: details are present and
 * not null, field values are validated, immutable.
 */
public class Person {
    public static final Set<Tag> DEFAULT_TAGS = new HashSet<>();
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Degree degree;
    private final Address address;
    private final School school;
    private final Set<Tag> tags = new HashSet<>();

    static {
        DEFAULT_TAGS.add(Tag.DEFAULT_TAG);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, School school, Degree degree, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, school, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.school = school;
        this.degree = degree;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return this.name;
    }

    public Phone getPhone() {
        return this.phone;
    }

    public Email getEmail() {
        return this.email;
    }

    public Address getAddress() {
        return this.address;
    }

    public School getSchool() {
        return this.school;
    }

    public Degree getDegree() {
        return this.degree;
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException} if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(this.tags);
    }

    /**
     * Returns true if both persons have the same name. This defines a weaker notion
     * of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        return otherPerson != null && otherPerson.getPhone().equals(this.getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields. This
     * defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Person otherPerson)) {
            return false;
        }
        return this.name.equals(otherPerson.name) && this.phone.equals(otherPerson.phone)
                && this.email.equals(otherPerson.email) && this.address.equals(otherPerson.address)
                && this.school.equals(otherPerson.school) && this.degree.equals(otherPerson.degree)
                && this.tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.name, this.phone, this.email, this.address, this.school, this.degree, this.tags);
    }

    /**
     * Order the different attributes of Person to be optimally displayed in either
     * CLI or GUI mode.
     *
     * @return String representation of Person's application data.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).add("name", this.name)
                .add("phone", this.phone)
                .add("email", this.email)
                .add("address", this.address)
                .add("school", this.school)
                .add("degree", this.degree)
                .add("tags", this.tags).toString();
    }
}
