package seedu.address.model.application;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that an {@code Application}'s {@code ApplicationStatus} matches the target status.
 */
public class ApplicationStatusPredicate implements Predicate<Application> {
    private final ApplicationStatus targetStatus;

    public ApplicationStatusPredicate(ApplicationStatus targetStatus) {
        this.targetStatus = targetStatus;
    }

    @Override
    public boolean test(Application application) {
        return application.getApplicationStatus().equals(targetStatus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ApplicationStatusPredicate)) {
            return false;
        }

        ApplicationStatusPredicate otherApplicationStatusPredicate = (ApplicationStatusPredicate) other;
        return targetStatus.equals(otherApplicationStatusPredicate.targetStatus);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("targetStatus", targetStatus).toString();
    }
}
