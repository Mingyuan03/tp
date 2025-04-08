package seedu.address.ui;

import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import seedu.address.commons.util.StringUtil;
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
    private Label jobRounds;
    @FXML
    private Label applications;

    // Attribute Containers
    @FXML
    private HBox jobTitleBox;
    @FXML
    private HBox jobRoundsBox;
    @FXML
    private HBox applicationsBox;
    @FXML
    private TilePane applicantsContainer;
    @FXML
    private VBox jobHeaderContainer;

    /**
     * Creates a {@code JobCard} with the given {@code Job} and index to display.
     */
    public JobCard(Job job, List<Application> applications, int displayedIndex) {
        super(FXML);
        this.job = job;

        // Setup job header with CSS classes (styling in DarkTheme.css)
        id.setText(displayedIndex + ". ");
        jobTitle.setText(StringUtil.toTitleCase(job.getJobTitle().jobTitle()));
        jobTitle.setWrapText(false);
        jobTitle.setMaxWidth(350);
        jobTitle.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);

        // Add header icons
        // Job rounds with icon
        jobRoundsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.TASKS, "white"));
        jobRounds.setText("Rounds: " + job.getJobRounds().jobRounds);

        // Applications count with icon
        applicationsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.USERS, "white"));

        // Ensure applications list is not null
        List<Application> applicationsList = (applications != null) ? applications : List.of();
        this.applications.setText("Applicants: " + applicationsList.size());

        // Add application count badge
        if (applicationsList.size() > 0) {
            Label badgeLabel = new Label(Integer.toString(applicationsList.size()));
            badgeLabel.getStyleClass().add("applications-badge");
            jobTitleBox.getChildren().add(badgeLabel);
        }

        // Configure TilePane for adaptive grid layout with styling from CSS
        applicantsContainer.setPrefTileWidth(320);
        applicantsContainer.setPrefTileHeight(Region.USE_COMPUTED_SIZE);
        applicantsContainer.setPrefColumns(2);
        applicantsContainer.setTileAlignment(Pos.TOP_LEFT);

        if (applicationsList.isEmpty()) {
            Label noApplicantsLabel = new Label("No applications yet");
            noApplicantsLabel.getStyleClass().add("no-applicants-label");
            applicantsContainer.getChildren().add(noApplicantsLabel);
        } else {
            // Add each applicant card to the TilePane
            int[] index = { 0 }; // Use array to allow modification in lambda
            applicationsList.stream()
                    .map(application -> createDetailedMiniPersonCard(application, index[0]++))
                    .forEach(miniCard -> applicantsContainer.getChildren().add(miniCard));
        }
    }

    /**
     * Creates a detailed mini person card for each applicant
     *
     * @param application  The application to display
     * @param displayIndex The index to display (0-based)
     * @return A VBox containing the mini person card
     */
    private VBox createDetailedMiniPersonCard(Application application, int displayIndex) {
        // Container for the mini card - using VBox for TilePane layout
        VBox miniCard = new VBox(10);
        miniCard.getStyleClass().add("mini-card");
        miniCard.setPrefWidth(320);

        // Header with index and name
        HBox nameBox = new HBox(8);

        // Add the index label
        Label indexLabel = new Label(displayIndex + 1 + ""); // Convert to 1-based index
        indexLabel.getStyleClass().add("mini-card-index");
        nameBox.getChildren().add(indexLabel);

        nameBox.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.USER, "white"));
        Label nameLabel = new Label(StringUtil.toTitleCase(application.getApplicant().getName().fullName));
        nameLabel.getStyleClass().add("mini-card-name");
        nameLabel.setWrapText(false);
        nameLabel.setMaxWidth(150);
        nameLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
        nameBox.getChildren().add(nameLabel);

        // Progress section
        HBox progressHeader = new HBox(5);
        progressHeader.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.BAR_CHART, "white"));
        Label progressLabel = new Label("Application Progress");
        progressLabel.getStyleClass().add("progress-header-label");
        progressHeader.getChildren().add(progressLabel);
        progressHeader.getStyleClass().add("progress-header");

        // Progress bar
        int completedRounds = application.getApplicationStatus().applicationStatus;
        int totalRounds = job.getJobRounds().jobRounds;

        // Prevent division by zero
        double progress = totalRounds > 0 ? (double) completedRounds / totalRounds : 0.0;

        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefHeight(12);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.getStyleClass().add("custom-progress-bar");

        // Set a unique ID for the progress bar
        progressBar.setId("progress-bar-application-" + displayIndex);

        // Status label
        Label statusLabel = new Label("Rounds completed: " + completedRounds + " of " + totalRounds);
        statusLabel.getStyleClass().add("status-label");

        // Add all sections to the card
        miniCard.getChildren().addAll(nameBox, progressHeader, progressBar, statusLabel);

        return miniCard;
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
        return id.getText().equals(card.id.getText()) && job.equals(card.job);
    }
}
