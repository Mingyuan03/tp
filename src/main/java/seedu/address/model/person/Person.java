package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.skill.Skill;

/**
 * Represents a Person in the address book. Guarantees: details are present and
 * not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Degree degree;

    // Data fields
    private final Address address;
    private final School school;
    private final Set<Skill> skills = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, School school, Degree degree, Set<Skill> skills) {
        requireAllNonNull(name, phone, email, address, school, skills);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.school = school;
        this.degree = degree;
        this.skills.addAll(skills);
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
        return school;
    }

    public Degree getDegree() {
        return degree;
    }

    /**
     * Returns an immutable skill set, which throws
     * {@code UnsupportedOperationException} if modification is attempted.
     */
    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(this.skills);
    }

    /**
     * Returns true if both persons have the same name. This defines a weaker notion
     * of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        return otherPerson != null && otherPerson.getName().equals(this.getName());
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
                && school.equals(otherPerson.school) && degree.equals(otherPerson.degree)
                && this.skills.equals(otherPerson.skills);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, skills, school, degree);
    }

    /**
     * Order the different attributes of Person to be optimally displayed in either
     * CLI or GUI mode.
     *
     * @return String representation of Person's application data.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).add("name", name).add("phone", phone).add("email", email)
                .add("address", address).add("school", school).add("degree", degree).add("skills", skills).toString();
    }

}
