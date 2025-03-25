package seedu.address.ui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Panel that displays detailed information about a person.
 */
public class PersonDetailPanel {
    
    private final Logic logic;
    private final VBox container;
    private Label nameLabel;
    private Label phoneLabel;
    private Label emailLabel;
    private Label addressLabel;
    private Label schoolLabel;
    private Label degreeLabel;
    private Label tagsLabel;
    private Label jobTitleLabel;
    private Label applicationStatusLabel;
    private ProgressBar progressBar;
    
    /**
     * Creates a {@code PersonDetailPanel} with the given {@code Logic}.
     */
    public PersonDetailPanel(Logic logic) {
        this.logic = logic;
        this.container = createContainer();
    }
    
    /**
     * Returns the root container for this panel.
     */
    public VBox getRoot() {
        return container;
    }
    
    /**
     * Updates the panel to show information about the given person.
     * 
     * @param person The person to show
     * @param job The job the person applied to
     * @param application The person's application
     */
    public void updateForPerson(Person person, Job job, Application application) {
        if (person == null) {
            return;
        }
        
        // Update person information
        nameLabel.setText(person.getName().toString());
        phoneLabel.setText(person.getPhone().toString());
        emailLabel.setText(person.getEmail().toString());
        addressLabel.setText(person.getAddress().toString());
        schoolLabel.setText(person.getSchool() != null ? person.getSchool().toString() : "N/A");
        degreeLabel.setText(person.getDegree() != null ? person.getDegree().toString() : "N/A");
        
        // Update tags
        StringBuilder tagBuilder = new StringBuilder();
        person.getTags().forEach(tag -> tagBuilder.append(tag.toString()).append(", "));
        String tagText = tagBuilder.length() > 0 
                ? tagBuilder.substring(0, tagBuilder.length() - 2) // Remove last comma
                : "No tags";
        tagsLabel.setText(tagText);
        
        // Update application information
        jobTitleLabel.setText(job.getJobTitle().jobTitle());
        int currentRound = application.applicationStatus().applicationStatus;
        int maxRound = job.getJobRounds().jobRounds;
        String applicationStatus = "Round " + currentRound + " of " + maxRound;
        applicationStatusLabel.setText(applicationStatus);
        
        // Update progress bar
        double progress = (double) currentRound / maxRound;
        progressBar.setProgress(progress);
    }
    
    /**
     * Creates a container for the person details.
     */
    private VBox createContainer() {
        VBox container = new VBox();
        container.setStyle("-fx-background-color: #2d2d30; -fx-padding: 20;");
        container.setSpacing(16);
        container.setMinWidth(350);
        container.setPrefWidth(400);
        
        // Add title - Person details
        Label titleLabel = new Label("Person Details");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        container.getChildren().add(titleLabel);
        
        // Add person name with nice styling
        nameLabel = new Label("Person Name");
        nameLabel.setStyle(
            "-fx-text-fill: linear-gradient(to right, #ff9966, #ff5e62); " +
            "-fx-font-size: 24px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 5 0 10 0;"
        );
        container.getChildren().add(nameLabel);
        
        // Add application header
        VBox applicationBox = createInfoBox("Application Details");
        
        jobTitleLabel = new Label("Job Title");
        jobTitleLabel.setStyle("-fx-text-fill: #4facfe; -fx-font-size: 18px; -fx-font-weight: bold;");
        applicationBox.getChildren().add(jobTitleLabel);
        
        applicationStatusLabel = new Label("Application Status");
        applicationStatusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        applicationBox.getChildren().add(applicationStatusLabel);
        
        // Add progress bar with white-on-black styling
        Label progressLabel = new Label("Application Progress");
        progressLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px; -fx-padding: 10 0 5 0;");
        applicationBox.getChildren().add(progressLabel);
        
        progressBar = new ProgressBar(0);
        progressBar.setPrefHeight(12);
        progressBar.setMaxWidth(Double.MAX_VALUE);
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
        
        // Give the progress bar a unique ID for CSS targeting
        progressBar.setId("person-detail-progress-bar");
        
        applicationBox.getChildren().add(progressBar);
        
        container.getChildren().add(applicationBox);
        
        // Add contact information
        VBox contactBox = createInfoBox("Contact Information");
        
        Label phoneTitle = new Label("Phone:");
        phoneTitle.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");
        
        phoneLabel = new Label("Phone Number");
        phoneLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        
        HBox phoneBox = new HBox(10);
        phoneBox.getChildren().addAll(phoneTitle, phoneLabel);
        contactBox.getChildren().add(phoneBox);
        
        Label emailTitle = new Label("Email:");
        emailTitle.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");
        
        emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        
        HBox emailBox = new HBox(10);
        emailBox.getChildren().addAll(emailTitle, emailLabel);
        contactBox.getChildren().add(emailBox);
        
        Label addressTitle = new Label("Address:");
        addressTitle.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");
        
        addressLabel = new Label("Physical Address");
        addressLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        
        HBox addressBox = new HBox(10);
        addressBox.getChildren().addAll(addressTitle, addressLabel);
        contactBox.getChildren().add(addressBox);
        
        container.getChildren().add(contactBox);
        
        // Add education information
        VBox educationBox = createInfoBox("Education");
        
        Label schoolTitle = new Label("School:");
        schoolTitle.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");
        
        schoolLabel = new Label("School Name");
        schoolLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        
        HBox schoolBox = new HBox(10);
        schoolBox.getChildren().addAll(schoolTitle, schoolLabel);
        educationBox.getChildren().add(schoolBox);
        
        Label degreeTitle = new Label("Degree:");
        degreeTitle.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");
        
        degreeLabel = new Label("Degree Name");
        degreeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        
        HBox degreeBox = new HBox(10);
        degreeBox.getChildren().addAll(degreeTitle, degreeLabel);
        educationBox.getChildren().add(degreeBox);
        
        container.getChildren().add(educationBox);
        
        // Add tags
        VBox tagsBox = createInfoBox("Tags");
        
        tagsLabel = new Label("Tags");
        tagsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        tagsLabel.setWrapText(true);
        
        tagsBox.getChildren().add(tagsLabel);
        container.getChildren().add(tagsBox);
        
        return container;
    }
    
    /**
     * Creates a styled info box with a title.
     */
    private VBox createInfoBox(String title) {
        VBox box = new VBox(8);
        box.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #3a3a3a, #2a2a2a); " +
            "-fx-padding: 15; " +
            "-fx-background-radius: 8; " + 
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);"
        );
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-text-fill: #66ccff; " +
            "-fx-font-size: 16px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 0 0 5 0;"
        );
        
        box.getChildren().add(titleLabel);
        return box;
    }
} 