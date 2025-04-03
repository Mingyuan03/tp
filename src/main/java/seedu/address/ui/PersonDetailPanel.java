package seedu.address.ui;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final ScrollPane scrollPane;
    private Label nameLabel;
    private Label phoneLabel;
    private Label emailLabel;
    private Label addressLabel;
    private Label schoolLabel;
    private Label degreeLabel;
    private FlowPane skillsContainer;
    private Label jobTitleLabel;
    private Label applicationStatusLabel;
    private ProgressBar progressBar;

    /**
     * Creates a {@code PersonDetailPanel} with the given {@code Logic}.
     */
    public PersonDetailPanel(Logic logic) {
        this.logic = logic;
        this.container = createContainer();
        this.scrollPane = createScrollPane();
    }

    /**
     * Returns the root container for this panel.
     */
    public ScrollPane getRoot() {
        return scrollPane;
    }

    /**
     * Creates a ScrollPane to wrap the content container.
     */
    private ScrollPane createScrollPane() {
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle("-fx-background-color: #2d2d30; -fx-background: #2d2d30;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        return scrollPane;
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

        // Update skill tags - use stylish tags instead of comma-separated text
        skillsContainer.getChildren().clear();
        if (person.getSkills().isEmpty()) {
            Label noSkillsLabel = new Label("No Skills");
            noSkillsLabel.getStyleClass().add("person-detail-label");
            skillsContainer.getChildren().add(noSkillsLabel);
        } else {
            person.getSkills().forEach(skill -> {
                // Use skillName() method since Skill is a record
                String skillText = skill.skillName();
                Label skillLabel = new Label(skillText);
                skillLabel.getStyleClass().add("person-detail-tag");
                
                // Limit width and add ellipsis for long skill names
                skillLabel.setMaxWidth(150);
                skillLabel.setMinWidth(30);
                skillLabel.setWrapText(false);
                skillLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
                
                // Create a wrapper HBox with padding to add spacing around each skill
                HBox wrapper = new HBox(skillLabel);
                wrapper.setStyle("-fx-padding: 3 5 3 5;");
                
                skillsContainer.getChildren().add(wrapper);
            });
        }

        // Update application information
        if (job != null) {
            jobTitleLabel.setText(job.getJobTitle().jobTitle());

            if (application != null) {
                int currentRound = application.getApplicationStatus().applicationStatus;
                int maxRound = job.getJobRounds().jobRounds;
                String applicationStatus = "Round " + currentRound + " of " + maxRound;
                applicationStatusLabel.setText(applicationStatus);

                // Update progress bar with safety check for division by zero
                double progress = maxRound > 0 ? (double) currentRound / maxRound : 0.0;
                progressBar.setProgress(progress);
            } else {
                applicationStatusLabel.setText("No application found");
                progressBar.setProgress(0);
            }
        } else {
            jobTitleLabel.setText("No job information");
            applicationStatusLabel.setText("No application found");
            progressBar.setProgress(0);
        }
    }

    /**
     * Creates a container for the person details.
     */
    private VBox createContainer() {
        VBox container = new VBox();
        container.getStyleClass().add("person-detail-container");
        container.setMinWidth(350);
        container.setPrefWidth(400);

        // Add title - Person details
        Label titleLabel = new Label("Person Details");
        titleLabel.getStyleClass().add("person-detail-title");
        container.getChildren().add(titleLabel);

        // Add person name with nice styling
        nameLabel = new Label("Person Name");
        nameLabel.getStyleClass().add("person-detail-name");
        container.getChildren().add(nameLabel);

        // Add application header
        VBox applicationBox = createInfoBox("Application Details");

        jobTitleLabel = new Label("Job Title");
        jobTitleLabel.getStyleClass().add("person-detail-job-title");
        applicationBox.getChildren().add(jobTitleLabel);

        applicationStatusLabel = new Label("Application Status");
        applicationStatusLabel.getStyleClass().add("person-detail-app-status");
        applicationBox.getChildren().add(applicationStatusLabel);

        // Add progress bar with white-on-black styling
        Label progressLabel = new Label("Application Progress");
        progressLabel.getStyleClass().add("person-detail-label");
        applicationBox.getChildren().add(progressLabel);

        progressBar = new ProgressBar(0);
        progressBar.setPrefHeight(12);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setId("person-detail-progress-bar");

        applicationBox.getChildren().add(progressBar);

        container.getChildren().add(applicationBox);

        // Add contact information
        VBox contactBox = createInfoBox("Contact Information");

        Label phoneTitle = new Label("Phone:");
        phoneTitle.getStyleClass().add("person-detail-label");

        phoneLabel = new Label("Phone Number");
        phoneLabel.getStyleClass().add("person-detail-value");

        HBox phoneBox = new HBox(10);
        phoneBox.getChildren().addAll(phoneTitle, phoneLabel);
        contactBox.getChildren().add(phoneBox);

        Label emailTitle = new Label("Email:");
        emailTitle.getStyleClass().add("person-detail-label");

        emailLabel = new Label("Email Address");
        emailLabel.getStyleClass().add("person-detail-value");

        HBox emailBox = new HBox(10);
        emailBox.getChildren().addAll(emailTitle, emailLabel);
        contactBox.getChildren().add(emailBox);

        Label addressTitle = new Label("Address:");
        addressTitle.getStyleClass().add("person-detail-label");

        addressLabel = new Label("Physical Address");
        addressLabel.getStyleClass().add("person-detail-value");

        HBox addressBox = new HBox(10);
        addressBox.getChildren().addAll(addressTitle, addressLabel);
        contactBox.getChildren().add(addressBox);

        container.getChildren().add(contactBox);

        // Add education information
        VBox educationBox = createInfoBox("Education");

        Label schoolTitle = new Label("School:");
        schoolTitle.getStyleClass().add("person-detail-label");

        schoolLabel = new Label("School Name");
        schoolLabel.getStyleClass().add("person-detail-value");

        HBox schoolBox = new HBox(10);
        schoolBox.getChildren().addAll(schoolTitle, schoolLabel);
        educationBox.getChildren().add(schoolBox);

        Label degreeTitle = new Label("Degree:");
        degreeTitle.getStyleClass().add("person-detail-label");

        degreeLabel = new Label("Degree Name");
        degreeLabel.getStyleClass().add("person-detail-value");

        HBox degreeBox = new HBox(10);
        degreeBox.getChildren().addAll(degreeTitle, degreeLabel);
        educationBox.getChildren().add(degreeBox);

        container.getChildren().add(educationBox);

        // Add skill tags
        VBox skillsBox = createInfoBox("Skills");

        // Use FlowPane for skill tags to allow wrapping
        skillsContainer = new FlowPane();
        skillsContainer.getStyleClass().add("person-detail-tags-container");
        skillsContainer.setPrefWrapLength(350);
        skillsContainer.setMinHeight(80);
        skillsContainer.setHgap(15);
        skillsContainer.setVgap(12);

        skillsBox.getChildren().add(skillsContainer);
        container.getChildren().add(skillsBox);

        return container;
    }

    /**
     * Creates a styled info box with a title.
     */
    private VBox createInfoBox(String title) {
        VBox box = new VBox();
        box.getStyleClass().add("person-detail-info-box");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("person-detail-info-title");

        box.getChildren().add(titleLabel);
        return box;
    }
}
