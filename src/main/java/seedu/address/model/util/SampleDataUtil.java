package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ApplicationsManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyApplicationsManager;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.skill.Skill;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
        public static Person[] getSamplePersons() {
                return new Person[] { // Initialisation order: Name, Phone, Email, Address, School, Degree[, Skills]
                                new Person(new Name("ALEX YEOH"), new Phone("87438807"),
                                                new Email("alexyeoh@example.com"),
                                                new Address("BLK 30 GEYLANG STREET 29, #06-40"), new School("NUS"),
                                                new Degree("COMPUTER SCIENCE"), getSkillSet("datascience", "python")),
                                new Person(new Name("BERNICE YU"), new Phone("99272758"),
                                                new Email("berniceyu@example.com"),
                                                new Address("BLK 30 LORONG 3 SERANGOON GARDENS, #07-18"),
                                                new School("NUS"), new Degree("INFORMATION SYSTEMS"),
                                                getSkillSet("flutter", "javascript")),
                                new Person(new Name("CHARLOTTE OLIVEIRO"), new Phone("93210283"),
                                                new Email("charlotte@example.com"),
                                                new Address("BLK 11 ANG MO KIO STREET 74, #11-04"), new School("NTU"),
                                                new Degree("BUSINESS ANALYTICS"), getSkillSet("python")),
                                new Person(new Name("DAVID LI"), new Phone("91031282"),
                                                new Email("lidavid@example.com"),
                                                new Address("BLK 436 SERANGOON GARDENS STREET 26, #16-43"),
                                                new School("NTU"), new Degree("MATHEMATICS"), getSkillSet("java")),
                                new Person(new Name("IRFAN IBRAHIM"), new Phone("92492021"),
                                                new Email("irfan@example.com"),
                                                new Address("BLK 47 TAMPINES STREET 20, #17-35"), new School("SMU"),
                                                new Degree("BUSINESS"), getSkillSet("clang")),
                                new Person(new Name("ROY BALAKRISHNAN"), new Phone("92624417"),
                                                new Email("royb@example.com"),
                                                new Address("BLK 45 ALJUNIED STREET 85, #11-31"), new School("SMU"),
                                                new Degree("POLITICAL SCIENCE"), getSkillSet("awscertified")) };
        }

        public static Job[] getSampleJobs() {
                return new Job[] { // Initialisation order: JobTitle, JobRounds, Skills
                                new Job(new JobTitle("SOFTWARE ENGINEER"), new JobRounds(5),
                                                getSkillSet("java", "springboot",
                                                                "microservices")),
                                new Job(new JobTitle("DATA SCIENTIST"), new JobRounds(4),
                                                getSkillSet("python", "ml",
                                                                "tensorflow")),
                                new Job(new JobTitle("PRODUCT MANAGER"), new JobRounds(3),
                                                getSkillSet("agile",
                                                                "planning")),
                                new Job(new JobTitle("UX DESIGNER"), new JobRounds(3),
                                                getSkillSet("figma",
                                                                "research", "framing")),
                                new Job(new JobTitle("DEVOPS ENGINEER"), new JobRounds(4),
                                                getSkillSet("aws", "kubernetes",
                                                                "ci/cd")),
                                new Job(new JobTitle("FULL STACK DEVELOPER"), new JobRounds(4),
                                        getSkillSet("react", "node.js", "mongodb"))
                };
        }

        public static ReadOnlyAddressBook getSampleAddressBook() {
                AddressBook sampleAb = new AddressBook();
                for (Person samplePerson : getSamplePersons()) {
                        sampleAb.addPerson(samplePerson);
                }
                for (Job sampleJob : getSampleJobs()) {
                        sampleAb.addJob(sampleJob);
                }
                return sampleAb;
        }

        public static ReadOnlyApplicationsManager getSampleApplicationsManager() {
                ApplicationsManager sampleAm = new ApplicationsManager();
                Person[] samplePersons = getSamplePersons();
                Job[] sampleJobs = getSampleJobs();

                // Create some sample applications
                Application[] sampleApplications = new Application[] {
                                // Alex applied to Google and is in round 2
                                new Application(samplePersons[0], sampleJobs[0], new ApplicationStatus(2)),

                                // Bernice applied to Microsoft and is in round 3
                                new Application(samplePersons[1], sampleJobs[1], new ApplicationStatus(3)),

                                // Charlotte applied to Apple and is in round 1
                                new Application(samplePersons[2], sampleJobs[2], new ApplicationStatus(1)),

                                // David applied to Meta and is in round 2
                                new Application(samplePersons[3], sampleJobs[3], new ApplicationStatus(2)),

                                // Irfan applied to Amazon and is in round 1
                                new Application(samplePersons[4], sampleJobs[4], new ApplicationStatus(1)),

                                // Roy applied to Netflix and is in round 3
                                new Application(samplePersons[5], sampleJobs[5], new ApplicationStatus(3)),

                                // Alex also applied to Microsoft and is in round 1
                                new Application(samplePersons[0], sampleJobs[1], new ApplicationStatus(1)),

                                // Bernice also applied to Google and is in round 4
                                new Application(samplePersons[1], sampleJobs[0], new ApplicationStatus(4)) };

                // Add all sample applications
                for (Application sampleApplication : sampleApplications) {
                        sampleAm.addApplication(sampleApplication);
                }

                return sampleAm;
        }

        /**
         * Returns a skill set containing the list of strings given.
         */
        public static Set<Skill> getSkillSet(String... strings) {
                return Arrays.stream(strings).map(Skill::new).collect(Collectors.toSet());
        }
}
