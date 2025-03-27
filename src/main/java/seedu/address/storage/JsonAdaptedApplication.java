package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.application.Application;
import seedu.address.model.application.ApplicationStatus;

/**
 * Jackson-friendly version of {@link Application}.
 */
public class JsonAdaptedApplication {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Application's %s field is missing!";

    private final JsonAdaptedPerson applicant;
    private final JsonAdaptedJob job;
    private final Integer applicationStatus;

    /**
     * Constructs a {@code JsonAdaptedApplication} with the given application
     * details.
     */
    @JsonCreator
    public JsonAdaptedApplication(@JsonProperty("applicant") JsonAdaptedPerson applicant,
            @JsonProperty("job") JsonAdaptedJob job, @JsonProperty("applicationStatus") Integer applicationStatus) {
        this.applicant = applicant;
        this.job = job;
        this.applicationStatus = applicationStatus;
    }

    /**
     * Converts a given {@code Application} into this class for Jackson use.
     */
    public JsonAdaptedApplication(Application source) {
        applicant = new JsonAdaptedPerson(source.getApplicant());
        job = new JsonAdaptedJob(source.getJob());
        applicationStatus = source.getApplicationStatus().applicationStatus;
    }

    /**
     * Converts this Jackson-friendly adapted application object into the model's
     * {@code Application} object.
     */
    public Application toModelType() throws IllegalValueException {
        if (applicationStatus == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ApplicationStatus.class.getSimpleName()));
        }
        if (!ApplicationStatus.isValidApplicationStatus(applicationStatus)) {
            throw new IllegalValueException(ApplicationStatus.MESSAGE_CONSTRAINTS);
        }
        final ApplicationStatus modelApplicationStatus = new ApplicationStatus(applicationStatus);
        return new Application(applicant.toModelType(), job.toModelType(), modelApplicationStatus);
    }
}
