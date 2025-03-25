package seedu.address.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.model.person.Person;

/**
 * Panel that displays statistics for a specific job.
 */
public class JobSpecificStatsPanel {
    
    private final Logic logic;
    private final VBox container;
    private Label jobTitleLabel;
    private Label applicantCountLabel;
    private PieChart schoolDistributionChart;
    
    /**
     * Creates a {@code JobSpecificStatsPanel} with the given {@code Logic}.
     */
    public JobSpecificStatsPanel(Logic logic) {
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
        updateSchoolDistribution(job);
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
        jobTitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        container.getChildren().add(jobTitleLabel);
        
        // Add summary panel with nice styling
        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: linear-gradient(to bottom right, #3a3a3a, #2a2a2a); " + 
                           "-fx-padding: 20; " +
                           "-fx-background-radius: 8; " + 
                           "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 10, 0, 0, 5);");
        
        // Styled applicant count label
        applicantCountLabel = new Label("Total Applicants: 0");
        applicantCountLabel.setStyle("-fx-text-fill: linear-gradient(to right, #ff9966, #ff5e62); " + 
                                    "-fx-font-size: 18px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-padding: 5 0; " +
                                    "-fx-effect: dropshadow(one-pass-box, rgba(255,255,255,0.2), 1, 0, 0, 1); " +
                                    "-fx-background-color: rgba(255,255,255,0.05); " +
                                    "-fx-background-radius: 4; " +
                                    "-fx-padding: 8 12;");
        
        summaryBox.getChildren().add(applicantCountLabel);
        container.getChildren().add(summaryBox);
        
        // Create school distribution chart
        Label schoolChartTitle = new Label("Applicants by School");
        schoolChartTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 20 0 5 0;");
        container.getChildren().add(schoolChartTitle);
        
        schoolDistributionChart = new PieChart();
        schoolDistributionChart.setMinHeight(300);
        schoolDistributionChart.setPrefHeight(300);
        schoolDistributionChart.setLegendVisible(true);
        schoolDistributionChart.setLegendSide(Side.RIGHT);
        schoolDistributionChart.setLabelsVisible(true);
        schoolDistributionChart.setStartAngle(90);
        schoolDistributionChart.setClockwise(true);
        
        // Style the pie chart
        String pieChartCss = 
            ".chart-pie-label { -fx-fill: white; }" +
            ".chart-pie-label-line { -fx-stroke: white; }" +
            ".chart-legend { -fx-background-color: transparent; }" +
            ".chart-legend-item { -fx-text-fill: white; }";
        schoolDistributionChart.setStyle("-fx-background-color: #2d2d30; -fx-padding: 10; " + pieChartCss);
        
        container.getChildren().add(schoolDistributionChart);
        
        return container;
    }
    
    /**
     * Updates the applicant count for the given job.
     */
    private void updateApplicantCount(Job job) {
        List<Application> applications = logic.getApplicationsByJob(job);
        int count = applications != null ? applications.size() : 0;
        
        // Update the label with a fun icon
        applicantCountLabel.setText("☺ Total Applicants: " + count);
    }
    
    /**
     * Updates the school distribution chart for the given job.
     */
    private void updateSchoolDistribution(Job job) {
        List<Application> applications = logic.getApplicationsByJob(job);
        
        // Create data for pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        // If no applications, show default data
        if (applications == null || applications.isEmpty()) {
            pieChartData.add(new PieChart.Data("No applicants", 1));
            schoolDistributionChart.setData(pieChartData);
            return;
        }
        
        // Count applicants by school
        Map<String, Integer> schoolCounts = new HashMap<>();
        for (Application application : applications) {
            Person person = application.applicant();
            if (person != null && person.getSchool() != null) {
                String school = person.getSchool().value;
                // Clean up school name if needed
                if (school.contains("@")) {
                    school = "Unknown School";
                }
                schoolCounts.put(school, schoolCounts.getOrDefault(school, 0) + 1);
            }
        }
        
        // Add data to pie chart
        for (Map.Entry<String, Integer> entry : schoolCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        // If no schools found
        if (pieChartData.isEmpty()) {
            pieChartData.add(new PieChart.Data("No school data", 1));
        }
        
        // Set the data
        schoolDistributionChart.setData(pieChartData);
    }
} 