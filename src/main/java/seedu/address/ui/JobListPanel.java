package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.application.Application;
import seedu.address.model.job.Job;
import seedu.address.ui.Sidebar.SidebarToggleListener;

/**
 * Panel containing the list of jobs with a collapsible sidebar.
 */
public class JobListPanel extends UiPart<Region> implements SidebarToggleListener {
    
    private static final String FXML = "JobListPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(JobListPanel.class);
    private static final double DEFAULT_DIVIDER_POSITION = 0.25;
    private static final double HIDDEN_DIVIDER_POSITION = 0.0;

    private Logic logic;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private Sidebar sidebarController;
    
    @FXML
    private ListView<Job> jobListView;
    
    private double lastDividerPosition = DEFAULT_DIVIDER_POSITION;

    /**
     * Creates a {@code JobListPanel} with the given {@code ObservableList}.
     */
    public JobListPanel(ObservableList<Job> jobList, Logic logic) {
        super(FXML);
        this.logic = logic;
        
        // Set cell factory
        jobListView.setCellFactory(listView -> new JobListViewCell());
        
        // Set items 
        jobListView.setItems(jobList);
        
        // Add listener to observable list to refresh UI when data changes
        jobList.addListener((javafx.collections.ListChangeListener.Change<? extends Job> c) -> {
            logger.info("Job list changed, refreshing UI components");
            refreshJobView();
        });
        
        // Create and initialize sidebar
        sidebarController = new Sidebar();
        initSidebar();
    }
    
    /**
     * Initializes the sidebar component.
     */
    private void initSidebar() {
        sidebarController.addToggleListener(this);
        
        // Set initial divider position
        splitPane.setDividerPositions(DEFAULT_DIVIDER_POSITION);
    }
    
    /**
     * Sets the visibility of the sidebar.
     */
    public void setSidebarVisible(boolean visible) {
        sidebarController.setVisible(visible);
    }
    
    @Override
    public void onSidebarToggle(boolean isVisible) {
        if (isVisible) {
            // Restore the sidebar
            splitPane.setDividerPositions(lastDividerPosition);
        } else {
            // Remember divider position before collapsing
            lastDividerPosition = splitPane.getDividerPositions()[0];
            // Collapse the sidebar
            splitPane.setDividerPositions(HIDDEN_DIVIDER_POSITION);
        }
    }
    
    /**
     * Adds a filter section to the sidebar.
     */
    public void addFilterSection(String title, Region content) {
        sidebarController.addSection(title, content);
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

    public List<Job> getJobList() {
        return jobListView.getItems();
    }
}