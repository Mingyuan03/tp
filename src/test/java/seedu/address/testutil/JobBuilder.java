package seedu.address.testutil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobSkills;
import seedu.address.model.job.JobTitle;
import seedu.address.model.job.JobType;

/**
 * A utility class to help with building Job objects.
 */
public class JobBuilder {

    public static final String DEFAULT_JOB_TITLE = "Software Engineering";
    public static final int DEFAULT_JOB_ROUNDS = 5;
    public static final ObservableList<String> DEFAULT_JOB_SKILLS = FXCollections.observableArrayList("Python",
            "JavaScript", "React");
    public static final String DEFAULT_JOB_TYPE = "Intern";

    private JobTitle jobTitle;
    private JobRounds jobRounds;
    private JobSkills jobSkills;
    private JobType jobType;

    /**
     * Creates a {@code JobBuilder} with the default details.
     */
    public JobBuilder() {
        jobTitle = new JobTitle(DEFAULT_JOB_TITLE);
        jobRounds = new JobRounds(DEFAULT_JOB_ROUNDS);
        jobSkills = new JobSkills(DEFAULT_JOB_SKILLS);
        jobType = JobType.fromDisplayType(DEFAULT_JOB_TYPE);
    }

    /**
     * Initializes the JobBuilder with the data of {@code jobToCopy}.
     */
    public JobBuilder(Job jobToCopy) {
        jobTitle = jobToCopy.getJobTitle();
        jobRounds = jobToCopy.getJobRounds();
        jobSkills = jobToCopy.getJobSkills();
        jobType = jobToCopy.getJobType();
    }

    /**
     * Sets the {@code JobTitle} of the {@code Job} that we are building.
     */
    public JobBuilder withJobTitle(String jobTitle) {
        this.jobTitle = new JobTitle(jobTitle);
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
     * Sets the {@code JobSkills} of the {@code Job} that we are building.
     */
    public JobBuilder withJobSkills(ObservableList<String> jobSkills) {
        this.jobSkills = new JobSkills(jobSkills);
        return this;
    }

    /**
     * Sets the {@code JobType} of the {@code Job} that we are building.
     */
    public JobBuilder withJobType(String jobType) {
        this.jobType = JobType.fromDisplayType(jobType);
        return this;
    }

    public Job build() {
        return new Job(jobTitle, jobRounds, jobSkills, jobType);
    }
}
