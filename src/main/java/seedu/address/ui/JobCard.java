package seedu.address.ui;

import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.tag.Tag;
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
        
        // Set card style
        cardPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #303030, #252525); " +
            "-fx-background-radius: 8; " + 
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);"
        );
        
        // Setup job header
        id.setText(displayedIndex + ". ");
        id.setStyle("-fx-text-fill: #f39c12;");
        jobTitle.setText(job.getJobTitle().jobTitle());
        jobTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Add header icons
        jobHeaderContainer.setStyle(
            "-fx-background-color: rgba(43, 43, 43, 0.7); " +
            "-fx-background-radius: 4; " +
            "-fx-padding: 12 15 12 15;"
        );
        
        // Job rounds with icon and modern styling
        jobRoundsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.TASKS, "white"));
        jobRounds.setText("Rounds: " + job.getJobRounds().jobRounds);
        jobRounds.setStyle("-fx-text-fill: white;");
        jobRoundsBox.setStyle(
            "-fx-background-color: rgba(62, 123, 145, 0.15); " +
            "-fx-background-radius: 4; " +
            "-fx-padding: 5 10 5 10;"
        );
        
        // Applications count with icon and modern styling
        applicationsBox.getChildren().add(0, IconUtil.createIcon(FontAwesomeIcon.USERS, "white"));
        this.applications.setText("Applicants: " + applications.size());
        this.applications.setStyle("-fx-text-fill: white;");
        applicationsBox.setStyle(
            "-fx-background-color: rgba(249, 105, 14, 0.15); " +
            "-fx-background-radius: 4; " +
            "-fx-padding: 5 10 5 10;"
        );
        
        // Add application count badge with Balenciaga-inspired styling
        if (applications.size() > 0) {
            Label badgeLabel = new Label(Integer.toString(applications.size()));
            badgeLabel.setStyle(
                "-fx-background-color: #3e7b91; " +
                "-fx-background-radius: 50%; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 3 7 3 7; " +
                "-fx-font-size: 10pt; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 3, 0, 0, 1);"
            );
            jobTitleBox.getChildren().add(badgeLabel);
        }

        // Configure TilePane for adaptive grid layout with modern styling
        applicantsContainer.setStyle(
            "-fx-background-color: rgba(43, 43, 43, 0.5); " +
            "-fx-padding: 15; " +
            "-fx-background-radius: 4; " +
            "-fx-hgap: 15; " +
            "-fx-vgap: 15;"
        );
        applicantsContainer.setPrefTileWidth(320);
        applicantsContainer.setPrefTileHeight(Region.USE_COMPUTED_SIZE);
        applicantsContainer.setPrefColumns(2);
        applicantsContainer.setTileAlignment(Pos.TOP_LEFT);
        
        if (!applications.isEmpty()) {
            // Add each applicant card to the TilePane
            applications.stream()
                    .map(application -> createDetailedMiniPersonCard(application))
                    .forEach(miniCard -> applicantsContainer.getChildren().add(miniCard));
        } else {
            Label noApplicantsLabel = new Label("No applications yet");
            noApplicantsLabel.setStyle(
                "-fx-font-style: italic; " +
                "-fx-text-fill: #aaaaaa; " +
                "-fx-padding: 15; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;"
            );
            applicantsContainer.getChildren().add(noApplicantsLabel);
        }
    }
    
    /**
     * Creates a detailed mini person card for each applicant
     */
    private VBox createDetailedMiniPersonCard(Application application) {
        // Container for the mini card - using VBox for TilePane layout
        VBox miniCard = new VBox(10);
        miniCard.setStyle(
            "-fx-background-color: #333336; " +
            "-fx-border-color: #3e7b91; " +
            "-fx-border-width: 0 0 0 3; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 12; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 3, 0, 0, 1);"
        );
        miniCard.setPrefWidth(320);
        
        // Header with name - Balenciaga styling
        HBox nameBox = new HBox(5);
        nameBox.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.USER, "white"));
        Label nameLabel = new Label(application.applicant().getName().fullName);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        nameBox.getChildren().add(nameLabel);
        
        // Address with icon - Consistent styling
        HBox addressBox = new HBox(5);
        addressBox.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.HOME, "white"));
        Label addressLabel = new Label(application.applicant().getAddress().value);
        addressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #bdc3c7;");
        addressBox.getChildren().add(addressLabel);
        addressBox.setStyle(
            "-fx-background-color: rgba(62, 123, 145, 0.15); " +
            "-fx-background-radius: 4; " +
            "-fx-padding: 5 10 5 10; " +
            "-fx-margin: 5 0 5 0;"
        );
        
        // Skills section with modern styling
        VBox skillsSection = new VBox(5);
        HBox skillsHeader = new HBox(5);
        skillsHeader.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.TAGS, "white"));
        Label skillsLabel = new Label("Skills:");
        skillsLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");
        skillsHeader.getChildren().add(skillsLabel);
        skillsHeader.setStyle(
            "-fx-background-color: rgba(43, 43, 43, 0.7); " +
            "-fx-background-radius: 4 4 0 0; " +
            "-fx-padding: 5 10 5 10;"
        );
        
        FlowPane skillsPane = new FlowPane();
        skillsPane.setStyle("-fx-spacing: 5; -fx-alignment: center-left; -fx-padding: 5 0 5 10;");
        skillsPane.setHgap(5);
        skillsPane.setVgap(5);
        application.applicant().getTags().stream()
                .map(Tag::tagName)
                .map(tagName -> {
                    Label label = new Label(tagName);
                    label.setStyle(
                        "-fx-background-color: #3e7b91; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 3 8 3 8; " +
                        "-fx-background-radius: 3; " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 0, 1);"
                    );
                    return label;
                })
                .forEach(label -> skillsPane.getChildren().add(label));
        
        skillsSection.getChildren().addAll(skillsHeader, skillsPane);
        
        // Progress section with Balenciaga styling
        HBox progressHeader = new HBox(5);
        progressHeader.getChildren().add(IconUtil.createIcon(FontAwesomeIcon.BAR_CHART, "white"));
        Label progressLabel = new Label("Application Progress");
        progressLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white;");
        progressHeader.getChildren().add(progressLabel);
        progressHeader.setStyle(
            "-fx-background-color: rgba(43, 43, 43, 0.7); " +
            "-fx-background-radius: 4 4 0 0; " +
            "-fx-padding: 5 10 5 10;"
        );
        
        // Progress bar with modern styling
        int currentRound = application.applicationStatus().applicationStatus;
        int maxRound = job.getJobRounds().jobRounds;
        double progress = (double) currentRound / maxRound;
        
        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefHeight(12);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        
        // White-on-black progress bar styling with stronger specificity
        progressBar.setStyle(
            "-fx-accent: white !important; " +
            "-fx-control-inner-background: #1a1a1a !important; " +
            "-fx-background-radius: 3 !important; " +
            "-fx-background-color: #1a1a1a !important; " +
            "-fx-border-radius: 3 !important; " +
            "-fx-border-color: #333333 !important; " +
            "-fx-border-width: 1 !important; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(255,255,255,0.1), 1, 0, 0, 1) !important;"
        );
        
        // Give the progress bar a unique ID to target it with CSS
        progressBar.setId("custom-progress-bar-" + currentRound + "-" + maxRound);
        
        // Status label with minimalist badge style
        Label statusLabel = new Label("Round " + currentRound + " of " + maxRound);
        statusLabel.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-size: 11px; " +
            "-fx-padding: 3 8 3 8; " +
            "-fx-background-color: rgba(255, 255, 255, 0.1); " +
            "-fx-background-radius: 10; " +
            "-fx-alignment: center-right;"
        );
        
        // Add all sections to the card
        miniCard.getChildren().addAll(nameBox, addressBox, skillsSection, progressHeader, progressBar, statusLabel);
        
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
        return id.getText().equals(card.id.getText())
                && job.equals(card.job);
    }
}
