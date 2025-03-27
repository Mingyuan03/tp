package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple
 * commands
 */
public class CliSyntax {
    // Person-specific Prefixes below, all being single-character.
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_SCHOOL = new Prefix("s/");
    public static final Prefix PREFIX_DEGREE = new Prefix("d/");
    // Job-specific Prefixes, all being double-character and starting with 'j' barring JobType.
    public static final Prefix PREFIX_JOB_TITLE = new Prefix("jt/");
    public static final Prefix PREFIX_JOB_COMPANY = new Prefix("jc/");
    public static final Prefix PREFIX_JOB_ROUNDS = new Prefix("jr/");
    public static final Prefix PREFIX_JOB_SKILLS = new Prefix("js/");
    public static final Prefix PREFIX_JOB_ADDRESS = new Prefix("ja/");
    public static final Prefix PREFIX_EMPLOYMENT_TYPE = new Prefix("em/"); // Correspond to job type.
    // Application-specific Prefix below, all being double-character and starting with 'a'.
    public static final Prefix PREFIX_APPLICATION_STATUS = new Prefix("as/");
    // Index-based Prefix below from corresponding lists, all being double-character and starting with 'i'.
    public static final Prefix PREFIX_PERSON_INDEX = new Prefix("ip/");
    public static final Prefix PREFIX_JOB_INDEX = new Prefix("ij/");
}
