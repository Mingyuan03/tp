package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.skill.Skill;

/**
 * Represents a job with a job title and additional properties.
 */
public class Job {
    private final JobTitle jobTitle;
    private final JobRounds jobRounds;
    private final Set<Skill> skills = new HashSet<>();

    /**
     * Constructs a Job with the specified job title and properties.
     *
     * @param jobTitle  The title of the job.
     * @param jobRounds The rounds of the job.
     * @param skills The requisite skills for the job.
     */
    public Job(JobTitle jobTitle, JobRounds jobRounds, Set<Skill> skills) {
        requireAllNonNull(jobTitle, jobRounds, skills);
        this.jobTitle = jobTitle;
        this.jobRounds = jobRounds;
        this.skills.addAll(skills);
    }

    /**
     * Returns the job title of this job.
     *
     * @return The job title.
     */
    public JobTitle getJobTitle() {
        return this.jobTitle;
    }

    /**
     * Returns the job rounds of this job.
     *
     * @return The job rounds.
     */
    public JobRounds getJobRounds() {
        return this.jobRounds;
    }

    /**
     * Returns an immutable skill set, which throws
     * {@code UnsupportedOperationException} if modification is attempted.
     */
    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(this.skills);
    }

    /**
     * Returns true if both jobs have the same title and company. This defines a
     * weaker notion of equality between two jobs.
     */
    public boolean isSameJob(Job otherJob) {
        return otherJob != null && otherJob.getJobTitle().equals(this.jobTitle);
    }

    /**
     * Returns true if this job is the same as the specified object.
     *
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Job otherJob)) {
            return false;
        }
        return this.jobTitle.equals(otherJob.jobTitle) && this.jobRounds.equals(otherJob.jobRounds)
                && this.skills.equals(otherJob.skills);
    }

    /**
     * Returns the hash code of this job.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.jobTitle, this.jobRounds, this.skills);
    }
}
