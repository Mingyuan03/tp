package seedu.address.testutil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;

/**
 * A utility class to help with building Job objects for FX tests.
 */
public class JobBuilderFX {
    public static final String DEFAULT_TITLE = "Software Engineer";
    public static final int DEFAULT_ROUNDS = 3;
    public static final ObservableList<String> DEFAULT_SKILLS = FXCollections.observableArrayList("Java", "Python");

    private JobTitle jobTitle;
    private JobRounds jobRounds;
    private JobSkills jobSkills;

    /**
     * Creates a {@code JobBuilderFX} with default details.
     */
    public JobBuilderFX() {
        jobTitle = new JobTitle(DEFAULT_TITLE);
        jobRounds = new JobRounds(DEFAULT_ROUNDS);
        jobSkills = new JobSkills(DEFAULT_SKILLS);
    }

    /**
     * Sets the {@code JobTitle} of the {@code Job} that we are building.
     */
    public JobBuilderFX withTitle(String title) {
        this.jobTitle = new JobTitle(title);
        return this;
    }

    /**
     * Sets the {@code JobRounds} of the {@code Job} that we are building.
     */
    public JobBuilderFX withRounds(int rounds) {
        this.jobRounds = new JobRounds(rounds);
        return this;
    }

    /**
     * Sets the {@code JobSkills} of the {@code Job} that we are building.
     */
    public JobBuilderFX withSkills(ObservableList<String> skills) {
        this.jobSkills = new JobSkills(skills);
        return this;
    }

    /**
     * Builds a Job object with the current attributes.
     */
    public Job build() {
        return new Job(jobTitle, jobRounds, jobSkills);
    }
}
