package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.person.Address;
import seedu.address.model.person.Degree;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.School;
import seedu.address.model.skill.Skill;

/**
 * Contains utility methods used for parsing strings in the various Parser
 * classes.
 */
public class ParserUtil {

    public static final String INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be trimmed.
     *
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
     * Parses a {@code String name} into a {@code Name}. Leading and trailing
     * whitespaces will be trimmed.
     *
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
     * Parses a {@code String phone} into a {@code Phone}. Leading and trailing
     * whitespaces will be trimmed.
     *
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
     * Parses a {@code String email} into an {@code Email}. Leading and trailing
     * whitespaces will be trimmed.
     *
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
     * Parses a {@code String address} into an {@code Address}. Leading and trailing
     * whitespaces will be trimmed.
     *
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
     * @param remark Raw remark by user.
     * @return trimmed remark without leading and trailing whitespaces for more
     *         efficient processing.
     */
    public static School parseRemark(String remark) {
        requireNonNull(remark);
        String trimmedRemark = remark.trim(); // School yet has format constraints.
        return new School(trimmedRemark);
    }

    /**
     * @param degree Raw degree by user.
     * @return trimmed remark without leading and trailing whitespaces for more
     *         efficient processing.
     */
    public static Degree parseDegree(String degree) {
        requireNonNull(degree);
        String trimmedDegree = degree.trim(); // School yet has format constraints.
        return new Degree(trimmedDegree);
    }

    /**
     * @param jobTitle Raw jobTitle by user.
     * @return trimmed remark without leading and trailing whitespaces for more
     *         efficient processing.
     * @throws ParseException if the given {@code jobTitle} is invalid.
     */
    public static JobTitle parseJobTitle(String jobTitle) throws ParseException {
        jobTitle = jobTitle.trim();
        requireNonNull(jobTitle);
        return new JobTitle(jobTitle);
    }

    /**
     * Parses a {@code String jobRounds} into a {@code JobRounds}. Leading and
     * trailing whitespaces will be trimmed.
     *
     * @param jobRounds Raw jobRounds String by user.
     * @return trimmed remark without leading and trailing whitespaces for more
     *         efficient processing.
     * @throws ParseException if the given {@code jobRounds} is invalid.
     */
    public static JobRounds parseJobRounds(String jobRounds) throws ParseException {
        jobRounds = jobRounds.trim();
        requireNonNull(jobRounds);
        int jobRoundsCount = Integer.parseInt(jobRounds);
        return new JobRounds(jobRoundsCount);
    }

    /**
     * @param jobSkills Raw jobSkills in the form of a single String input delimited
     *                  by whitespace by user.
     * @return {@code FXCollections.observableArrayList} collection of jobSkill
     *         Strings.
     * @throws ParseException if any given {@code jobSkill} is invalid.
     */
    public static JobSkills parseJobSkills(String jobSkills) throws ParseException {
        jobSkills = jobSkills.trim();
        requireNonNull(jobSkills);
        String[] jobSkillsArray = jobSkills.split("\\s+");
        return new JobSkills(FXCollections.observableArrayList(jobSkillsArray));
    }

    /**
     * Parses a {@code String skill} into a {@code Skill}. Leading and trailing
     * whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code skill} is invalid.
     */
    public static Skill parseSkill(String skill) throws ParseException {
        requireNonNull(skill);
        String trimmedSkill = skill.trim().toLowerCase();
        if (!Skill.isValidSkillName(trimmedSkill)) {
            throw new ParseException(Skill.MESSAGE_CONSTRAINTS);
        }
        return new Skill(trimmedSkill);
    }

    /**
     * Parses {@code Collection<String> skills} into a {@code Set<Skill>}.
     */
    public static Set<Skill> parseSkills(Collection<String> skills) throws ParseException {
        requireNonNull(skills);
        final Set<Skill> skillSet = new HashSet<>();
        for (String skillName : skills) {
            skillSet.add(parseSkill(skillName));
        }
        return skillSet;
    }
}
