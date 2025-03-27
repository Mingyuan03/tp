package seedu.address.ui;

import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.ui.util.IconUtil;

/**
 * UI component that displays information of a {@code Job}.
 */
public class JobCard extends UiPart<Region> {

    private static final String FXML = "JobListCard.fxml";

    public final Job job;
    // Graphic Components
    @FXML
    private HBox cardPane;
    @FXML
    private Label id;

    // Attribute Labels
    @FXML
    private Label jobTitle;
    @FXML
    private Label company;
    @FXML
    private Label jobRounds;
    @FXML
    private Label applications;

    // Attribute Containers
    @FXML
    private HBox jobTitleBox;
    @FXML
    private HBox companyBox;
    @FXML
    private HBox jobRoundsBox;
    @FXML
    private HBox applicationsBox;
    @FXML
    private HBox applicantsPreview;

    /**
     * Creates a {@code JobCard} with the given {@code Job} and index to display.
     */
    public JobCard(Job job, List<Application> applications, int displayedIndex) {
        super(FXML);
        this.job = job;
        id.setText(displayedIndex + ". ");
        jobTitle.setText(job.jobTitle().jobTitle());
        // Company with icon
        companyBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.BUILDING, "white"));
        company.setText(job.jobCompany().jobCompany());
        // Job rounds with icon
        jobRoundsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.TASKS, "white"));
        jobRounds.setText("Rounds: " + job.jobRounds().jobRounds);
        // Applications preview with icon
        applicationsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.USERS, "white"));
        this.applications.setText("Applications: " + applications.size());

        // Add mini person cards for first 3 applicants
        if (!applications.isEmpty()) {
            applications.stream()
                    .limit(3)
                    .map(application -> new MiniPersonCard(application.applicant()))
                    .forEach(miniCard -> applicantsPreview.getChildren().add(miniCard.getRoot()));
            if (applications.size() > 3) {
                Label moreLabel = new Label("+" + (applications.size() - 3) + " more");
                moreLabel.getStyleClass().add("more-applications-label");
                applicantsPreview.getChildren().add(moreLabel);
            }
        } else {
            Label noApplicantsLabel = new Label("No applications yet");
            noApplicantsLabel.getStyleClass().add("no-applications-label");
            applicantsPreview.getChildren().add(noApplicantsLabel);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JobCard)) {
            return false;
        }

        // state check
        JobCard card = (JobCard) other;
        return id.getText().equals(card.id.getText())
                && job.equals(card.job);
    }
}
