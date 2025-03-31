package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.skill.Skill;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_SCHOOL = "NUS";
    public static final String DEFAULT_DEGREE = "Computer Science";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private School school;
    private Degree degree;
    private Set<Skill> skills;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        school = new School(DEFAULT_SCHOOL);
        degree = new Degree(DEFAULT_DEGREE);
        skills = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        school = personToCopy.getSchool();
        degree = personToCopy.getDegree();
        skills = new HashSet<>(personToCopy.getSkills());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code School} of the {@code Person} that we are building.
     *
     * @param school school String associated with the Person in the contact.
     * @return Person with the associated school.
     */
    public PersonBuilder withSchool(String school) {
        this.school = new School(school);
        return this;
    }

    /**
     * Sets the {@code Degree} of the {@code Person} that we are building.
     */
    public PersonBuilder withDegree(String degree) {
        this.degree = new Degree(degree);
        return this;
    }

    /**
     * Parses the {@code skills} into a {@code Set<Skill>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withSkills(String... skills) {
        this.skills = SampleDataUtil.getSkillSet(skills);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address, school, degree, skills);
    }
}
