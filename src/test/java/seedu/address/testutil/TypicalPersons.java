package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEGREE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEGREE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCHOOL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCHOOL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests, synchronised with JSON file in
 * {@code typicalPersonsAddressBook}.
 * Standardise Person and Job initialisation orders in {@code SampleDataUtil} for clarity even if order matters not.
 * Person Initialisation order: Name, Phone, Email, Address, School, Degree[, Tags]
 * Job Initialisation order: JobTitle, JobCompany, JobRounds, JobSkills, JobAddress, JobType
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withPhone("94351253").withPhone("94351253").withPhone("94351253") // Duplicate Phones test robustness.
            .withEmail("alice@example.com")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withSchool("NUS")
            .withDegree("Computer Science")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withPhone("98765432")
            .withEmail("johnd@example.com")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withSchool("NTU")
            .withDegree("Business Analytics").withDegree("Business Analytics") // Duplicate Degrees test robustness.
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .withAddress("wall street")
            .withSchool("SMU")
            .withDegree("Mathematics").build(); // Absent optional Tags tests robustness.
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withPhone("87652533")
            .withEmail("cornelia@example.com")
            .withAddress("10th street")
            .withSchool("SIM")
            .withDegree("Physics")
            .withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withPhone("9482224")
            .withEmail("werner@example.com")
            .withAddress("michegan ave")
            .withSchool("SIT")
            .withDegree("Accounting").build(); // Absent optional Tags tests robustness.
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withPhone("9482427")
            .withEmail("lydia@example.com")
            .withAddress("little tokyo")
            .withSchool("NTU")
            .withDegree("Civil Engineering").build(); // Absent optional Tags tests robustness.
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withPhone("9482442")
            .withEmail("anna@example.com")
            .withAddress("4th street")
            .withSchool("NUS")
            .withDegree("Computer Engineering").build(); // Absent optional Tags tests robustness.

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withSchool("NUS")
            .withDegree("Computer Science").withPhone("8482424").withEmail("stefan@example.com")
            .withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withSchool("NUS")
            .withDegree("Computer Science").withPhone("8482131").withEmail("hans@example.com")
            .withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY)
            .withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withSchool(VALID_SCHOOL_AMY).withSchool(VALID_SCHOOL_AMY) // Duplicate Schools test robustness.
            .withDegree(VALID_DEGREE_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withSchool(VALID_SCHOOL_BOB).withSchool(VALID_SCHOOL_BOB) // Duplicate Schools test robustness.
            .withDegree(VALID_DEGREE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final Job SOFTWARE_ENGINEER_GOOGLE = new JobBuilder().withJobTitle("Software Engineer")
            .withJobCompany("Google")
            .withJobRounds(5)
            .withJobSkills(FXCollections.observableArrayList("Java", "Spring Boot", "Microservices"))
            .withJobAddress("70 Pasir Panjang Rd, Singapore 117371")
            .withJobType("Intern")
            .build();

    public static final Job DATA_SCIENTIST_MICROSOFT = new JobBuilder().withJobTitle("Data Scientist")
            .withJobCompany("Microsoft")
            .withJobRounds(4)
            .withJobSkills(FXCollections.observableArrayList("Python", "ML", "TensorFlow"))
            .withJobAddress("1 Marina Boulevard, #22-01, Singapore 018989")
            .withJobType("Full Time")
            .build();

    public static final Job PRODUCT_MANAGER_APPLE = new JobBuilder().withJobTitle("Product Manager")
            .withJobCompany("Apple")
            .withJobRounds(3)
            .withJobSkills(FXCollections.observableArrayList("Agile", "Roadmap Planning"))
            .withJobAddress("7 Ang Mo Kio Street 64, Singapore 569086")
            .withJobType("Contract")
            .build();

    public static final Job UX_DESIGNER_META = new JobBuilder().withJobTitle("UX Designer")
            .withJobCompany("Meta")
            .withJobRounds(3)
            .withJobSkills(FXCollections.observableArrayList("Figma", "User Research", "Wire Framing"))
            .withJobAddress("9 Straits View, Marina One, Singapore 018937")
            .withJobType("Freelance")
            .build();

    public static final Job DEVOPS_ENGINEER_AMAZON = new JobBuilder().withJobTitle("DevOps Engineer")
            .withJobCompany("Amazon")
            .withJobRounds(4)
            .withJobSkills(FXCollections.observableArrayList("AWS", "Kubernetes", "CI/CD"))
            .withJobAddress("23 Church Street, Capital Square, Singapore 049481")
            .withJobType("Part Time")
            .build();

    public static final Job FULLSTACK_DEVELOPER_NETFLIX = new JobBuilder().withJobTitle("Full Stack Developer")
            .withJobCompany("Netflix")
            .withJobRounds(4)
            .withJobSkills(FXCollections.observableArrayList("React", "Node.js", "MongoDB"))
            .withJobAddress("1 Fusionopolis Place, Galaxis, Singapore 138522")
            .withJobType("Full Time")
            .build();

    // Typical applications
    public static final Application ALICE_GOOGLE_APPLICATION = new Application(ALICE, // Person
            SOFTWARE_ENGINEER_GOOGLE, // Job
            new ApplicationStatus(2));

    public static final Application BENSON_MICROSOFT_APPLICATION = new Application(BENSON, // Person
            DATA_SCIENTIST_MICROSOFT, // Job
            new ApplicationStatus(3));

    public static final Application CARL_APPLE_APPLICATION = new Application(CARL, // Person
            PRODUCT_MANAGER_APPLE, // Job
            new ApplicationStatus(1));

    public static final Application DANIEL_META_APPLICATION = new Application(DANIEL, // Person
            UX_DESIGNER_META, // Job
            new ApplicationStatus(2));

    public static final Application ELLE_AMAZON_APPLICATION = new Application(ELLE, // Person
            DEVOPS_ENGINEER_AMAZON, // Job
            new ApplicationStatus(1));

    public static final Application FIONA_NETFLIX_APPLICATION = new Application(FIONA, // Person
            FULLSTACK_DEVELOPER_NETFLIX, // Job
            new ApplicationStatus(3));

    public static final Application ALICE_MICROSOFT_APPLICATION = new Application(ALICE, // Person
            DATA_SCIENTIST_MICROSOFT, // Job
            new ApplicationStatus(1));

    public static final Application BENSON_GOOGLE_APPLICATION = new Application(BENSON, // Person
            SOFTWARE_ENGINEER_GOOGLE, // Job
            new ApplicationStatus(4));

    private TypicalPersons() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();

        // Add all typical persons
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }

        // Add all typical jobs
        for (Job job : getTypicalJobs()) {
            ab.addJob(job);
        }

        return ab;
    }

    /**
     * Returns an {@code ApplicationsManager} with all the typical applications.
     */
    public static ApplicationsManager getTypicalApplicationsManager() {
        ApplicationsManager am = new ApplicationsManager();

        // Add all typical applications
        for (Application application : getTypicalApplications()) {
            am.addApplication(application);
        }

        return am;
    }

    /**
     * Returns a list of typical persons.
     */
    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    /**
     * Returns a list of typical jobs.
     */
    public static List<Job> getTypicalJobs() {
        return new ArrayList<>(Arrays.asList(SOFTWARE_ENGINEER_GOOGLE, DATA_SCIENTIST_MICROSOFT, PRODUCT_MANAGER_APPLE,
                UX_DESIGNER_META, DEVOPS_ENGINEER_AMAZON, FULLSTACK_DEVELOPER_NETFLIX));
    }

    /**
     * Returns a list of typical applications.
     */
    public static List<Application> getTypicalApplications() {
        return new ArrayList<>(Arrays.asList(ALICE_GOOGLE_APPLICATION, ALICE_MICROSOFT_APPLICATION,
                BENSON_GOOGLE_APPLICATION, BENSON_MICROSOFT_APPLICATION, CARL_APPLE_APPLICATION,
                DANIEL_META_APPLICATION, ELLE_AMAZON_APPLICATION, FIONA_NETFLIX_APPLICATION));
    }
}
