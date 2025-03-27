package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.application.ApplicationStatus;
import seedu.address.model.job.JobAddress;
import seedu.address.model.job.JobCompany;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various Parser classes.
 */
public class ParserUtil {
    public static final String INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index}. Leading and trailing whitespaces will be trimmed.
     * @param oneBasedIndex raw 1-based index String by user.
     * @throws ParseException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */

    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}. Leading and trailing whitespaces will be trimmed.
     * @param name Raw name String by user.
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}. Leading and trailing whitespaces will be trimmed.
     * @param phone Raw phone String by user.
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}. Leading and trailing whitespaces will be trimmed.
     * @param email Raw email String by user.
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String address} into an {@code Address}. Leading and trailing whitespaces will be trimmed.
     * @param address Raw address String by user.
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String school} into a {@code School}. Leading and trailing whitespaces will be trimmed.
     * @param school Raw school String by user.
     * @return trimmed remark without leading and trailing whitespaces for more efficient processing.
     */
    public static School parseSchool(String school) {
        requireNonNull(school);
        String trimmedSchool = school.trim(); // School yet has format constraints.
        return new School(trimmedSchool);
    }

    /**
     * Parses a {@code String degree} into a {@code Degree}. Leading and trailing whitespaces will be trimmed.
     * @param degree Raw degree String by user.
     * @return trimmed remark without leading and trailing whitespaces for more efficient processing.
     */
    public static Degree parseDegree(String degree) {
        requireNonNull(degree);
        String trimmedDegree = degree.trim(); // School yet has format constraints.
        return new Degree(trimmedDegree);
    }

    /**
     * Parses a {@code String jobTitle} into a {@code JobTitle}. Leading and trailing whitespaces will be trimmed.
     * @param jobTitle Raw jobTitle String by user.
     * @return trimmed remark without leading and trailing whitespaces for more efficient processing.
     * @throws ParseException if the given {@code jobTitle} is invalid.
     */
    public static JobTitle parseJobTitle(String jobTitle) throws ParseException {
        jobTitle = jobTitle.trim();
        requireNonNull(jobTitle);
        return new JobTitle(jobTitle);
    }

    /**
     * Parses a {@code String jobCompany} into a {@code JobCompany}. Leading and trailing whitespaces will be trimmed.
     * @param jobCompany Raw JobCompany String by user.
     * @return trimmed remark without leading and trailing whitespaces for more efficient processing.
     * @throws ParseException if the given {@code JobCompany} is invalid.
     */
    public static JobCompany parseJobCompany(String jobCompany) throws ParseException {
        String trimmedJobCompany = jobCompany.trim();
        requireNonNull(jobCompany);
        return new JobCompany(trimmedJobCompany);
    }

    /**
     * Parses a {@code String jobRounds} into a {@code JobRounds}. Leading and trailing whitespaces will be trimmed.
     * @param jobRounds Raw jobRounds String by user.
     * @return trimmed remark without leading and trailing whitespaces for more efficient processing.
     * @throws ParseException if the given {@code jobRounds} is invalid.
     */
    public static JobRounds parseJobRounds(String jobRounds) throws ParseException {
        jobRounds = jobRounds.trim();
        requireNonNull(jobRounds);
        int jobRoundsCount = Integer.parseInt(jobRounds);
        return new JobRounds(jobRoundsCount);
    }

    /**
     * Parses a {@code String jobSkills} into {@code JobSkills} list. Leading and trailing whitespaces will be trimmed.
     * @param jobSkills Raw jobSkills String in the form of a single String input delimited by whitespace by user.
     * @return {@code FXCollections.observableArrayList} collection of jobSkill Strings.
     * @throws ParseException if any given {@code jobSkill} is invalid.
     */
    public static JobSkills parseJobSkills(String jobSkills) throws ParseException {
        jobSkills = jobSkills.trim();
        requireNonNull(jobSkills);
        String[] jobSkillsArray = jobSkills.split("\\s+");
        return new JobSkills(FXCollections.observableArrayList(jobSkillsArray));
    }

    /**
     * Parses a {@code String jobAddress} into a {@code JobAddress}. Leading and trailing whitespaces will be trimmed.
     * @param jobAddress Raw jobAddress String by user.
     * @return trimmed jobAddress.
     * @throws ParseException if the given {@code jobAddress} is invalid.
     */
    public static JobAddress parseJobAddress(String jobAddress) throws ParseException {
        jobAddress = jobAddress.trim();
        requireNonNull(jobAddress);
        return new JobAddress(jobAddress);
    }

    /**
     * Parses a {@code String jobType} into a {@code JobType} key. Leading and trailing whitespaces will be trimmed.
     * @param jobType Raw jobType String value by user.
     * @return trimmed jobType key.
     * @throws ParseException if the given {@code jobType} value is invalid.
     */
    public static JobType parseJobType(String jobType) throws ParseException {
        jobType = jobType.trim();
        requireNonNull(jobType);
        return JobType.fromDisplayType(jobType);
    }

    /**
     * Parses an {@code String applicationStatus} into an {@code ApplicationStatus}.
     * Leading and trailing whitespaces will be trimmed.
     * @param applicationStatus Raw applicationStatus String value by user.
     * @return trimmed applicationStatus key.
     * @throws ParseException if the given {@code applicationStatus} is invalid.
     */
    public static ApplicationStatus parseApplicationStatus(String applicationStatus) throws ParseException {
        applicationStatus = applicationStatus.trim();
        requireNonNull(applicationStatus);
        if (!ApplicationStatus.isValidApplicationStatus(applicationStatus)) {
            throw new ParseException(ApplicationStatus.MESSAGE_CONSTRAINTS);
        }
        return new ApplicationStatus(applicationStatus);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}. Leading and trailing
     * whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
