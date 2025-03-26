package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import seedu.address.model.job.Job;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private Model model;
    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private JobListPanel jobListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
    private HelpWindow helpWindow;
    private ViewStateIndicator viewStateIndicator;
    private boolean isJobView = false;
    private int selectedJobIndex = -1; // -1 means no job selected

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane jobListPanelPlaceholder;

    @FXML
    private StackPane viewStateIndicatorPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // Initialize status bar first (only once)
        if (statusBarFooter == null) {
            statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
            statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
        }
        
        // Initialize view state indicator
        if (viewStateIndicator == null) {
            viewStateIndicator = new ViewStateIndicator();
            viewStateIndicatorPlaceholder.getChildren().add(viewStateIndicator.getRoot());
        }
        
        // Update the view state indicator
        updateViewStateIndicator();
        
        // Initialize the appropriate view
        if (isJobView) {
            initJobView();
        } else {
            initPersonView();
        }

        // Initialize components that are always present
        if (resultDisplay == null) {
            resultDisplay = new ResultDisplay();
            resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
        }

        if (commandBoxPlaceholder.getChildren().isEmpty()) {
            CommandBox commandBox = new CommandBox(this::executeCommand);
            commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
        }
    }
    
    /**
     * Initializes the job view components.
     */
    private void initJobView() {
        // Clear person list placeholder
        personListPanelPlaceholder.getChildren().clear();
        
        // Create job list panel if needed
        jobListPanel = new JobListPanel(logic.getFilteredJobList(), logic);
        jobListPanelPlaceholder.getChildren().clear();
        jobListPanelPlaceholder.getChildren().add(jobListPanel.getRoot());
        
        // Hide person list completely
        personListPanelPlaceholder.setVisible(false);
        personListPanelPlaceholder.setManaged(false);
        
        // Show job list
        jobListPanelPlaceholder.setVisible(true);
        jobListPanelPlaceholder.setManaged(true);
        
        // If a job is selected, show its specific statistics
        if (selectedJobIndex >= 0 && selectedJobIndex < logic.getFilteredJobList().size()) {
            viewJobStatistics(selectedJobIndex);
        }
    }
    
    /**
     * Initializes the person view components.
     */
    private void initPersonView() {
        // Clear job list placeholder
        jobListPanelPlaceholder.getChildren().clear();
        
        // Create person list panel if needed
        personListPanel = new PersonListPanel(logic.getFilteredPersonList(), logic);
        personListPanelPlaceholder.getChildren().clear();
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());
        
        // Hide job list completely
        jobListPanelPlaceholder.setVisible(false);
        jobListPanelPlaceholder.setManaged(false);
        
        // Show person list
        personListPanelPlaceholder.setVisible(true);
        personListPanelPlaceholder.setManaged(true);
        
        // Reset job selection when switching to person view
        selectedJobIndex = -1;
    }

    /**
     * Updates the view state indicator with the current view state.
     */
    private void updateViewStateIndicator() {
        if (viewStateIndicator != null) {
            if (logic instanceof Model) {
                Model model = (Model) logic;
                viewStateIndicator.updateViewState(model.getCurrentViewState());
            } else {
                // If logic is not a Model, use isJobView to determine the state
                viewStateIndicator.updateViewState(isJobView ? 
                    Model.ViewState.JOB_VIEW : Model.ViewState.PERSON_VIEW);
            }
        }
    }

    /**
     * Displays statistics for a specific job.
     * 
     * @param jobIndex the index of the job to view
     */
    public void viewJobStatistics(int jobIndex) {
        if (!isJobView) {
            toggleJobView(); // Ensure we're in job view
        }
        
        if (jobIndex >= 0 && jobIndex < logic.getFilteredJobList().size()) {
            selectedJobIndex = jobIndex;
            Job selectedJob = logic.getFilteredJobList().get(jobIndex);
            
            // Set the model to job detail view
            if (logic instanceof Model) {
                ((Model) logic).setViewState(Model.ViewState.JOB_DETAIL_VIEW);
                updateViewStateIndicator();
            }
            
            if (jobListPanel != null) {
                // Tell the job list panel to show job-specific statistics
                jobListPanel.showJobSpecificStatistics(selectedJob);
                
                // Optionally select the job in the list view
                jobListPanel.selectJob(jobIndex);
                
                logger.info("Viewing statistics for job: " + selectedJob.getJobTitle().jobTitle());
            }
        } else {
            logger.warning("Invalid job index for statistics: " + jobIndex);
            selectedJobIndex = -1;
        }
    }

    /**
     * Resets the job selection and shows general statistics.
     */
    public void resetJobSelection() {
        selectedJobIndex = -1;
        
        // Set the model to regular job view
        if (logic instanceof Model) {
            ((Model) logic).setViewState(Model.ViewState.JOB_VIEW);
            updateViewStateIndicator();
        }
        
        if (isJobView && jobListPanel != null) {
            jobListPanel.showGeneralStatistics();
        }
    }

    /**
     * Clears the current view and returns to the general overview.
     */
    public void clearView() {
        // Return to overview mode
        if (isJobView) {
            // Ensure model state is synchronized
            if (logic instanceof Model) {
                Model model = (Model) logic;
                model.setViewState(Model.ViewState.JOB_VIEW);
            }
            
            resetJobSelection();
            
            // Refresh the panel with general statistics
            if (jobListPanel != null) {
                jobListPanel.showGeneralStatistics();
            }
            
            logger.info("View cleared, returned to job overview");
        }
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Toggles the view between person and job.
     */
    public void toggleJobView() {
        this.isJobView = !this.isJobView;
        
        // Update the UI for the new view
        if (isJobView) {
            initJobView();
        } else {
            initPersonView();
        }
        
        // Update view state indicator
        updateViewStateIndicator();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    public JobListPanel getJobListPanel() {
        return jobListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.setToggleView()) {
                toggleJobView();
            }

            if (commandResult.isViewJob()) {
                viewJobStatistics(commandResult.getJobIndex());
            }
            
            if (commandResult.isViewPerson()) {
                viewPersonDetails(commandResult.getJobIndex(), commandResult.getPersonIndex());
            }

            if (commandResult.isExit()) {
                handleExit();
            }
            
            // Update view state indicator after command execution
            updateViewStateIndicator();

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Displays the details of a person for a specific job.
     * 
     * @param jobIndex The index of the job
     * @param personIndex The index of the person
     */
    private void viewPersonDetails(int jobIndex, int personIndex) {
        if (!isJobView) {
            toggleJobView(); // Ensure we're in job view
        }
        
        if (jobIndex >= 0 && jobIndex < logic.getFilteredJobList().size()) {
            selectedJobIndex = jobIndex;
            Job selectedJob = logic.getFilteredJobList().get(jobIndex);
            
            // Set the model to person detail view
            if (logic instanceof Model) {
                ((Model) logic).setViewState(Model.ViewState.PERSON_DETAIL_VIEW);
            }
            
            if (jobListPanel != null) {
                // Tell the job list panel to show the person details
                jobListPanel.showPersonDetails(selectedJob, personIndex);
                
                // Optionally select the job in the list view
                jobListPanel.selectJob(jobIndex);
                
                logger.info("Viewing person details for job: " + selectedJob.getJobTitle().jobTitle() 
                        + ", person index: " + personIndex);
            }
        } else {
            logger.warning("Invalid job index for person details: " + jobIndex);
            selectedJobIndex = -1;
        }
    }
}
