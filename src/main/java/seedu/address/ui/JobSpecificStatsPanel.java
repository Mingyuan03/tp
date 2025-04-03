package seedu.address.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.skill.Skill;

/**
 * Panel that displays statistics for a specific job.
 */
public class JobSpecificStatsPanel {

    private final Logic logic;
    private final ScrollPane scrollPane;
    private final VBox container;
    private Label jobTitleLabel;
    private Label applicantCountLabel;
    private BarChart<String, Number> roundDistributionChart;
    private NumberAxis yAxis;
    private FlowPane skillsSummaryPane;

    /**
     * Creates a {@code JobSpecificStatsPanel} with the given {@code Logic}.
     */
    public JobSpecificStatsPanel(Logic logic) {
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
        scrollPane.getStyleClass().add("stats-scroll-pane");
        scrollPane.setStyle("-fx-background-color: #2d2d30; -fx-background: #2d2d30;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);

        container.setPadding(new javafx.geometry.Insets(20, 30, 20, 20));

        return scrollPane;
    }

    /**
     * Updates the panel to show statistics for the given job.
     *
     * @param job The job to show statistics for
     */
    public void updateForJob(Job job) {
        if (job == null) {
            return;
        }

        // Update job title
        jobTitleLabel.setText(job.getJobTitle().jobTitle());

        // Update statistics
        updateApplicantCount(job);
        updateRoundDistribution(job);
        updateSkillsSummary(job);
    }

    /**
     * Creates the container for the job-specific statistics.
     */
    private VBox createContainer() {
        VBox container = new VBox();
        container.setStyle("-fx-background-color: #2d2d30; -fx-padding: 20;");
        container.setSpacing(20);
        container.setMinWidth(350);
        container.setPrefWidth(400);

        // Add job title label
        jobTitleLabel = new Label("Job Title");
        jobTitleLabel.getStyleClass().add("job-stats-title");
        jobTitleLabel.setWrapText(true);
        jobTitleLabel.setMaxWidth(Double.MAX_VALUE);
        jobTitleLabel.setMinHeight(60);
        jobTitleLabel.setAlignment(javafx.geometry.Pos.CENTER);
        jobTitleLabel.setStyle("-fx-background-color: linear-gradient(to right, #3a3a3a, #2a2a2a); "
                + "-fx-text-fill: white; "
                + "-fx-font-size: 22px; "
                + "-fx-font-weight: bold; "
                + "-fx-padding: 15; "
                + "-fx-background-radius: 8; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 3);");
        container.getChildren().add(jobTitleLabel);

        // Add summary panel with nice styling
        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a3a3a, #2a2a2a); "
                           + "-fx-padding: 20; "
                           + "-fx-background-radius: 8; "
                           + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");

        // Styled applicant count label
        applicantCountLabel = new Label("Total Applicants: 0");
        applicantCountLabel.setStyle("-fx-text-fill: linear-gradient(to right, #ff9966, #ff5e62); "
                                    + "-fx-font-size: 18px; "
                                    + "-fx-font-weight: bold; "
                                    + "-fx-padding: 5 0; "
                                    + "-fx-effect: dropshadow(one-pass-box, rgba(255,255,255,0.2), 1, 0, 0, 1); "
                                    + "-fx-background-color: rgba(255,255,255,0.05); "
                                    + "-fx-background-radius: 4; "
                                    + "-fx-padding: 8 12;");

        summaryBox.getChildren().add(applicantCountLabel);
        container.getChildren().add(summaryBox);

        // Create round distribution chart
        Label roundChartTitle = new Label("Applicants by Round");
        roundChartTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 20 0 5 0;");
        container.getChildren().add(roundChartTitle);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Application Stage");
        yAxis = new NumberAxis(0, 10, 1); // Will be adjusted dynamically
        yAxis.setLabel("Number of Applicants");
        roundDistributionChart = new BarChart<>(xAxis, yAxis);
        roundDistributionChart.setMinHeight(300);
        roundDistributionChart.setPrefHeight(300);

        // Completely disable the legend
        roundDistributionChart.setLegendVisible(false);

        // Set chart ID for CSS
        roundDistributionChart.setId("roundDistributionChart");

        roundDistributionChart.setAnimated(false);
        roundDistributionChart.setCategoryGap(50); // Increase space between bars
        roundDistributionChart.setBarGap(2);

        // Add more space at the bottom of the chart for labels
        roundDistributionChart.setPadding(new javafx.geometry.Insets(10, 10, 40, 10));

        // Style the bar chart
        String barChartCss =
            ".chart-bar-label { -fx-fill: white; }"
            + ".axis-label { -fx-text-fill: white; }"
            + ".axis { -fx-tick-label-fill: white; }"
            + ".chart-series-bar { -fx-bar-fill: white; }"
            + ".chart-plot-background { -fx-background-color: #2d2d30; }"
            + ".chart-vertical-grid-lines { -fx-stroke: transparent; }"
            + ".chart-horizontal-grid-lines { -fx-stroke: transparent; }"
            + ".chart-alternative-row-fill { -fx-fill: transparent; }"
            + ".chart-alternative-column-fill { -fx-fill: transparent; }"
            + ".chart-vertical-zero-line { -fx-stroke: transparent; }"
            + ".chart-horizontal-zero-line { -fx-stroke: transparent; }"
            + ".default-color0.chart-bar { -fx-background-radius: 0; }"
            + ".chart-line-symbol, .chart-symbol "
            + "{ -fx-background-color: transparent, transparent; "
            + "-fx-background-radius: 0px; "
            + "-fx-padding: 0px; }"
            + ".chart-series-line { -fx-stroke: transparent; }"
            + ".chart-legend-item-symbol { -fx-background-color: transparent; }"
            + ".chart-plot-background > * { -fx-background-color: transparent; }"
            + ".data0.chart-bar { -fx-bar-fill: white; }"
            + ".series0.chart-bar { -fx-bar-fill: white; }"
            + ".chart-legend { -fx-background-color: transparent; "
            + "visibility: hidden; -fx-padding: 0px; "
            + "-fx-border-width: 0px; -fx-max-width: 0px; "
            + "-fx-max-height: 0px; -fx-opacity: 0; "
            + "display: none; }"
            + ".chart-legend-item { visibility: hidden; "
            + "-fx-padding: 0px; -fx-opacity: 0; display: none; }";

        // Apply only basic style properties directly
        roundDistributionChart.setStyle("-fx-background-color: #2d2d30; -fx-padding: 10;");

        // Add the chart to the container first
        container.getChildren().add(roundDistributionChart);

        // CSS styling will be applied via applyCssToChartNodes() after data is added

        // Add Skills Summary Section
        Label skillsSummaryTitle = new Label("Required Job Skills");
        skillsSummaryTitle.getStyleClass().add("skills-summary-title");
        container.getChildren().add(skillsSummaryTitle);

        // Create a box for the skills summary with styled background
        VBox skillsBox = new VBox(10);
        skillsBox.getStyleClass().add("skills-summary-container");

        // Create a flow pane for the skills
        skillsSummaryPane = new FlowPane();
        skillsSummaryPane.getStyleClass().add("skills-summary-pane");
        skillsSummaryPane.setPrefWrapLength(350);

        skillsBox.getChildren().add(skillsSummaryPane);
        container.getChildren().add(skillsBox);

        return container;
    }

    /**
     * Updates the applicant count for the given job.
     */
    private void updateApplicantCount(Job job) {
        List<Application> applications = logic.getFilteredApplicationsByJob(job);
        int count = applications != null ? applications.size() : 0;

        // Update the label with a fun icon
        applicantCountLabel.setText("☺ Total Applicants: " + count);
    }

    /**
     * Updates the round distribution chart for the given job.
     */
    private void updateRoundDistribution(Job job) {
        List<Application> applications = logic.getFilteredApplicationsByJob(job);
        int totalRounds = job.getJobRounds().jobRounds;

        // Update the y-axis
        yAxis.setAutoRanging(false);
        
        // Calculate appropriate upper bound based on max count and total rounds
        int maxCount = 0;
        
        // Count applications by round
        Map<Integer, Integer> roundCounts = new HashMap<>();
        
        // Initialize all rounds with 0
        for (int i = 0; i <= totalRounds; i++) {
            roundCounts.put(i, 0);
        }
        
        // Count applicants in each round
        if (applications != null) {
            for (Application application : applications) {
                int round = application.getApplicationStatus().applicationStatus;
                int currentCount = roundCounts.getOrDefault(round, 0) + 1;
                roundCounts.put(round, currentCount);
                maxCount = Math.max(maxCount, currentCount);
            }
        }
        
        // Set y-axis bounds with some padding and ensure minimum of 5
        int upperBound = Math.max(maxCount + 2, 5);
        int tickUnit = Math.max(1, upperBound / 5);
        
        // Make sure x-axis has enough width for all rounds
        CategoryAxis xAxis = (CategoryAxis) roundDistributionChart.getXAxis();
        int barCount = totalRounds + 1; // +1 for Not Started (round 0)
        // Set category gap proportionally smaller when there are more rounds
        double categoryGap = Math.max(10, 50 - (barCount * 3));
        roundDistributionChart.setCategoryGap(categoryGap);
        
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(upperBound);
        yAxis.setTickUnit(tickUnit);

        // Clear existing data
        roundDistributionChart.getData().clear();

        // If no applications, show default data
        if (applications == null || applications.isEmpty()) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("");
            series.getData().add(new XYChart.Data<>("No applicants", 0));
            roundDistributionChart.getData().add(series);
            roundDistributionChart.setLegendVisible(false);

            // Force layout refresh
            roundDistributionChart.layout();

            // Apply CSS to ensure legend is hidden
            applyCssToChartNodes();
            return;
        }

        // Create series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(""); // Empty name to prevent legend issues

        for (int i = 0; i <= totalRounds; i++) {
            String roundLabel = i == 0 ? "Not Started"
                                : i == totalRounds ? "Offered"
                                : "Round " + i;

            // Truncate round label if too long
            if (roundLabel.length() > 15) {
                roundLabel = roundLabel.substring(0, 12) + "...";
            }

            XYChart.Data<String, Number> data = new XYChart.Data<>(roundLabel, roundCounts.get(i));
            series.getData().add(data);
        }

        // Force no legend
        series.setName(null);
        roundDistributionChart.setLegendVisible(false);
        roundDistributionChart.getData().add(series);
        
        // Force layout refresh to ensure chart updates
        roundDistributionChart.layout();

        // Apply CSS to remove symbols after adding data
        applyCssToChartNodes();
    }

    /**
     * Updates the skills summary for the given job.
     */
    private void updateSkillsSummary(Job job) {
        skillsSummaryPane.getChildren().clear();

        // Directly get skills from the job
        Set<Skill> jobSkills = job.getSkills();

        // If no skills found
        if (jobSkills.isEmpty()) {
            Label noSkillsLabel = new Label("No skills required for this job");
            noSkillsLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            skillsSummaryPane.getChildren().add(noSkillsLabel);
            return;
        }

        // Create a skill tag for each job skill
        for (Skill skill : jobSkills) {
            Label skillLabel = new Label(skill.skillName());
            skillLabel.getStyleClass().add("skill-summary-tag");
            skillLabel.setWrapText(false);
            skillLabel.setMaxWidth(120);
            skillLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);
            skillsSummaryPane.getChildren().add(skillLabel);
        }
    }

    /**
     * Applies CSS to chart nodes to ensure no symbols are visible
     */
    private void applyCssToChartNodes() {
        // Apply styles to chart bars
        for (XYChart.Series<String, Number> series : roundDistributionChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    // Apply style directly to the node
                    data.getNode().setStyle("-fx-background-color: white;"
                        + "-fx-background-insets: 0;"
                        + "-fx-background-radius: 0;");
                }
            }
        }

        // Hide any potential legend
        if (roundDistributionChart.lookup(".chart-legend") != null) {
            roundDistributionChart.lookup(".chart-legend").setVisible(false);
            roundDistributionChart.lookup(".chart-legend").setManaged(false);
            roundDistributionChart.lookup(".chart-legend").setOpacity(0);
        }

        if (roundDistributionChart.lookup(".chart-legend-item") != null) {
            roundDistributionChart.lookup(".chart-legend-item").setVisible(false);
            roundDistributionChart.lookup(".chart-legend-item").setManaged(false);
        }

        if (roundDistributionChart.lookup(".chart-legend-item-symbol") != null) {
            roundDistributionChart.lookup(".chart-legend-item-symbol").setVisible(false);
        }

        // Apply styles to other elements
        roundDistributionChart.lookupAll(".chart-bar-label").forEach(node ->
            node.setStyle("-fx-fill: white;"));

        roundDistributionChart.lookupAll(".axis-label").forEach(node ->
            node.setStyle("-fx-text-fill: white;"));

        roundDistributionChart.lookupAll(".axis").forEach(node ->
            node.setStyle("-fx-tick-label-fill: white;"));

        roundDistributionChart.lookupAll(".chart-plot-background").forEach(node ->
            node.setStyle("-fx-background-color: #2d2d30;"));

        roundDistributionChart.lookupAll(".chart-vertical-grid-lines").forEach(node ->
            node.setStyle("-fx-stroke: transparent;"));

        roundDistributionChart.lookupAll(".chart-horizontal-grid-lines").forEach(node ->
            node.setStyle("-fx-stroke: transparent;"));

        roundDistributionChart.lookupAll(".chart-vertical-zero-line").forEach(node ->
            node.setStyle("-fx-stroke: transparent;"));

        roundDistributionChart.lookupAll(".chart-horizontal-zero-line").forEach(node ->
            node.setStyle("-fx-stroke: transparent;"));
    }
}
