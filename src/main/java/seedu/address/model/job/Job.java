package seedu.address.model.job;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a job with a job title and company name.
 */
public record Job(JobTitle jobTitle, JobCompany jobCompany, JobRounds jobRounds, JobSkills jobSkills,
                  JobAddress jobAddress, JobType jobType) {
    /**
     * Constructs a Job with the specified job title and company name.
     *
     * @param jobTitle   The title of the job.
     * @param jobCompany The name of the company offering the job.
     * @param jobRounds  The rounds of the job.
     * @param jobSkills  The requisite skills for the job.
     * @param jobAddress The address of the job.
     * @param jobType    The employment type of the job.
     */
    public Job {
        requireAllNonNull(jobTitle, jobCompany, jobRounds, jobSkills, jobAddress, jobType);
    }

    /**
     * Returns the job title of this job.
     *
     * @return The job title.
     */
    @Override
    public JobTitle jobTitle() {
        return this.jobTitle;
    }

    /**
     * Returns the company name of this job.
     *
     * @return The company name.
     */
    @Override
    public JobCompany jobCompany() {
        return this.jobCompany;
    }

    /**
     * Returns the job rounds of this job.
     *
     * @return The job rounds.
     */
    @Override
    public JobRounds jobRounds() {
        return this.jobRounds;
    }

    /**
     * Returns the job skills of this job.
     *
     * @return The job skills.
     */
    @Override
    public JobSkills jobSkills() {
        return this.jobSkills;
    }

    /**
     * Returns the job address of this job.
     *
     * @return The job address.
     */
    @Override
    public JobAddress jobAddress() {
        return this.jobAddress;
    }

    /**
     * Returns the employment type of this job, be it intern, part-time or full-time.
     *
     * @return The employment type of the job.
     */
    @Override
    public JobType jobType() {
        return this.jobType;
    }

    /**
     * Returns true if both jobs have the same title and company. This defines a weaker notion
     * of equality between two jobs.
     */
    public boolean isSameJob(Job otherJob) {
        return otherJob != null && otherJob.jobTitle().equals(this.jobTitle)
                && otherJob.jobCompany().equals(this.jobCompany);
    }

    /**
     * Returns true if this job is the same as the specified object.
     * @param other The object to compare to.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(other instanceof Job otherJob)) {
            return false;
        }
        return this.jobTitle.equals(otherJob.jobTitle);
    }
}
