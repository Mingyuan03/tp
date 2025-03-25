package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;

/**
 * Panel containing the list of jobs with a sidepane for additional information.
 */
public class JobListPanel extends UiPart<Region> {
    
    private static final String FXML = "JobListPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(JobListPanel.class);
    private static final double HIDDEN_DIVIDER_POSITION = 1.0;
    private static final double DEFAULT_DIVIDER_POSITION = 0.6;

    private Logic logic;
    private StatisticsChartPanel statisticsPanel;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private ListView<Job> jobListView;
    
    private double lastDividerPosition = DEFAULT_DIVIDER_POSITION;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Job> jobList, Logic logic) {
        super(FXML);
        this.logic = logic;
        logger.info("Creating JobListPanel with logic: " + (logic != null ? "valid" : "null"));
        
        // Set cell factory
        jobListView.setCellFactory(listView -> new JobListViewCell());
        
        // Set items 
        jobListView.setItems(jobList);
        
        // Log all FXML components
        logger.info("FXML components initialized: " + 
                   "splitPane=" + (splitPane != null) + 
                   ", jobListView=" + (jobListView != null));
        
        // Initialize the sidepane with statistics by default
        initSidepane();
        
        // Force divider position to make sidebar visible
        if (splitPane != null) {
            splitPane.setDividerPosition(0, DEFAULT_DIVIDER_POSITION);
            logger.info("Set initial divider position to: " + DEFAULT_DIVIDER_POSITION);
        }
        
        // Add listener to observable list to refresh UI when data changes
        jobList.addListener((javafx.collections.ListChangeListener.Change<? extends Job> c) -> {
            logger.info("Job list changed, refreshing UI components");
            refreshJobView();
            refreshSidepane();
        });
        
        // Add listener to job selection to update sidepane content if needed in the future
        jobListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Job selection changed: " + (newValue != null ? newValue.getJobTitle().jobTitle() : "null"));
            // In the future, this could update the sidepane content based on the selected job
            // For now, we'll just refresh the statistics
            refreshSidepane();
        });
    }
    
    /**
     * Initializes the sidepane content.
     */
    private void initSidepane() {
        // Create statistics panel
        statisticsPanel = new StatisticsChartPanel(logic);
        
        // Add it to the split pane
        if (splitPane != null && splitPane.getItems().size() > 1) {
            logger.info("Setting statistics panel as sidepane content");
            splitPane.getItems().set(1, statisticsPanel.getRoot());
        } else {
            logger.severe("SplitPane doesn't have enough items for sidepane!");
        }
    }
    
    /**
     * Changes the sidepane content to a different panel.
     * This is a placeholder for future extensibility.
     * 
     * @param contentType The type of content to display
     */
    public void setSidepaneContent(SidepaneContentType contentType) {
        // This method would allow changing what's displayed in the sidepane
        // For now, we only have statistics, but in the future this could display
        // different types of content based on user selection or context
        
        if (splitPane == null || splitPane.getItems().size() <= 1) {
            logger.severe("Cannot set sidepane content - SplitPane not properly initialized!");
            return;
        }
        
        switch (contentType) {
            case STATISTICS:
                // We're already using statistics panel, create if needed
                if (statisticsPanel == null) {
                    statisticsPanel = new StatisticsChartPanel(logic);
                }
                splitPane.getItems().set(1, statisticsPanel.getRoot());
                break;
                
            // Future content types would be added here as new cases
            // case JOB_DETAILS:
            //     JobDetailsPanel detailsPanel = new JobDetailsPanel(selectedJob);
            //     splitPane.getItems().set(1, detailsPanel.getRoot());
            //     break;
                
            default:
                logger.warning("Unknown sidepane content type: " + contentType);
                break;
        }
        
        // Make sure the sidepane is visible
        setSidepaneVisible(true);
    }
    
    /**
     * Refreshes the current sidepane content.
     */
    private void refreshSidepane() {
        if (statisticsPanel != null) {
            statisticsPanel.refresh();
        }
    }
    
    /**
     * Sets the visibility of the sidepane.
     */
    public void setSidepaneVisible(boolean visible) {
        if (visible) {
            splitPane.setDividerPosition(0, lastDividerPosition);
        } else {
            lastDividerPosition = splitPane.getDividerPositions()[0];
            splitPane.setDividerPosition(0, HIDDEN_DIVIDER_POSITION);
        }
    }
    
    /**
     * Toggles the visibility of the sidepane.
     */
    public void toggleSidepane() {
        boolean isCurrentlyVisible = splitPane.getDividerPositions()[0] < HIDDEN_DIVIDER_POSITION;
        setSidepaneVisible(!isCurrentlyVisible);
    }
    
    /**
     * Refreshes the job view by forcing an update of visible cells
     */
    private void refreshJobView() {
        int size = jobListView.getItems().size();
        if (size > 0) {
            jobListView.refresh();
        }
    }
    
    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Job} using a {@code JobCard}.
     */
    class JobListViewCell extends ListCell<Job> {
        @Override
        protected void updateItem(Job job, boolean empty) {
            super.updateItem(job, empty);

            if (empty || job == null) {
                setGraphic(null);
                setText(null);
            } else {
                List<Application> applications = logic.getApplicationsByJob(job);
                setGraphic(new JobCard(job, applications, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Enum to represent different types of content that can be displayed in the sidepane.
     * This is for future extensibility.
     */
    public enum SidepaneContentType {
        STATISTICS,
        // Add more types in the future as needed
        // JOB_DETAILS,
        // APPLICANT_DETAILS,
        // etc.
    }
    
    public List<Job> getJobList() {
        return jobListView.getItems();
    }
}