package seedu.address.testutil;

import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;
import seedu.address.model.util.SampleDataUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class to help with building Job objects for FX tests.
 */
public class JobBuilderFX {
    public static final String DEFAULT_TITLE = "Software Engineer";
    public static final int DEFAULT_ROUNDS = 3;
    public static final List<String> DEFAULT_SKILLS = List.of("Java", "Python");

    private JobTitle jobTitle;
    private JobRounds jobRounds;
    private Set<Skill> skills;

    /**
     * Creates a {@code JobBuilderFX} with default details.
     */
    public JobBuilderFX() {
        jobTitle = new JobTitle(DEFAULT_TITLE);
        jobRounds = new JobRounds(DEFAULT_ROUNDS);
        skills = new HashSet<>(DEFAULT_SKILLS.stream().map(Skill::new).collect(Collectors.toSet()));
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
     * Sets the {@code skills} of the {@code Job} that we are building.
     */
    public JobBuilderFX withSkills(String... skills) {
        this.skills = SampleDataUtil.getSkillSet(skills);
        return this;
    }

    /**
     * Builds a Job object with the current attributes.
     */
    public Job build() {
        return new Job(jobTitle, jobRounds, skills);
    }
}
