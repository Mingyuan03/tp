package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.job.Job;
import seedu.address.model.job.JobRounds;
import seedu.address.model.job.JobTitle;
import seedu.address.model.skill.Skill;

/**
 * Jackson-friendly version of {@link Job}.
 */
class JsonAdaptedJob {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Job's %s field is missing!";

    private final String jobTitle;
    private final Integer jobRounds;
    private final List<JsonAdaptedSkill> skills = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedJob} with the given job details.
     */
    @JsonCreator
    public JsonAdaptedJob(@JsonProperty("jobTitle") String jobTitle, @JsonProperty("jobRounds") Integer jobRounds,
                          @JsonProperty("skills") List<JsonAdaptedSkill> skills) {
        this.jobTitle = jobTitle;
        this.jobRounds = jobRounds;
        if (skills != null) {
            this.skills.addAll(skills);
        }
    }

    /**
     * Converts a given {@code Job} into this class for Jackson use.
     */
    public JsonAdaptedJob(Job source) {
        jobTitle = source.getJobTitle().jobTitle(); // JobTitle record class has implicit accessor.
        jobRounds = source.getJobRounds().jobRounds;
        skills.addAll(source.getSkills().stream().map(JsonAdaptedSkill::new).toList());
    }

    /**
     * Converts this Jackson-friendly adapted job object into the model's
     * {@code Job} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted job.
     */
    public Job toModelType() throws IllegalValueException {
        final List<Skill> jobSkills = new ArrayList<>();
        for (JsonAdaptedSkill skill : skills) {
            jobSkills.add(skill.toModelType());
        }

        if (jobTitle == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    JobTitle.class.getSimpleName()));
        }
        if (!JobTitle.isValidJobTitle(jobTitle)) {
            throw new IllegalValueException(JobTitle.MESSAGE_CONSTRAINTS);
        }
        final JobTitle modelJobTitle = new JobTitle(this.jobTitle);
        // Check valid max job rounds below.
        if (this.jobRounds == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, JobRounds.class.getSimpleName()));
        }
        final JobRounds modelJobRounds = new JobRounds(jobRounds);

        final Set<Skill> modelSkills = new HashSet<>(jobSkills);
        return new Job(modelJobTitle, modelJobRounds, modelSkills);
    }
}
