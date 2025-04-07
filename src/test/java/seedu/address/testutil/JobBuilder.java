package seedu.address.testutil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Job objects.
 */
public class JobBuilder {

    public static final String DEFAULT_JOB_TITLE = "Software Engineering";
    public static final int DEFAULT_JOB_ROUNDS = 5;

    private JobTitle jobTitle;
    private JobRounds jobRounds;
    private Set<Skill> skills;

    /**
     * Creates a {@code JobBuilder} with the default details.
     */
    public JobBuilder() {
        jobTitle = new JobTitle(DEFAULT_JOB_TITLE.toLowerCase());
        jobRounds = new JobRounds(DEFAULT_JOB_ROUNDS);
        skills = new HashSet<>();
    }

    /**
     * Initializes the JobBuilder with the data of {@code jobToCopy}.
     */
    public JobBuilder(Job jobToCopy) {
        jobTitle = jobToCopy.getJobTitle();
        jobRounds = jobToCopy.getJobRounds();
        skills = new HashSet<>(jobToCopy.getSkills());
    }

    /**
     * Sets the {@code JobTitle} of the {@code Job} that we are building.
     */
    public JobBuilder withJobTitle(String jobTitle) {
        this.jobTitle = new JobTitle(jobTitle.toLowerCase());
        return this;
    }

    /**
     * Sets the {@code JobRounds} of the {@code Job} that we are building.
     */
    public JobBuilder withJobRounds(int jobRounds) {
        this.jobRounds = new JobRounds(jobRounds);
        return this;
    }

    /**
     * Parses the {@code skills} into a {@code Set<Skill>} and set it to the
     * {@code Job} that we are building.
     */
    public JobBuilder withSkills(String... skills) {
        this.skills = SampleDataUtil.getSkillSet(Arrays.stream(skills).map(String::toLowerCase).toArray(String[]::new));
        return this;
    }

    public Job build() {
        return new Job(jobTitle, jobRounds, skills);
    }
}
