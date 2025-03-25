package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

/**
 * UI component for the collapsible sidebar.
 */
public class Sidebar extends UiPart<Region> {

    private static final String FXML = "Sidebar.fxml";
    private static final String ICON_CHEVRON_LEFT = "CHEVRON_LEFT";
    private static final String ICON_CHEVRON_RIGHT = "CHEVRON_RIGHT";

    @FXML
    private VBox sidebarContainer;

    @FXML
    private HBox sidebarHeader;

    @FXML
    private VBox sidebarContent;

    @FXML
    private Button toggleButton;

    @FXML
    private FontAwesomeIconView toggleIcon;

    private boolean isVisible;
    private List<SidebarToggleListener> listeners;

    /**
     * Interface for components that need to be notified when the sidebar is toggled.
     */
    public interface SidebarToggleListener {
        void onSidebarToggle(boolean isVisible);
    }

    /**
     * Creates a new Sidebar.
     */
    public Sidebar() {
        super(FXML);
        this.isVisible = true;
        this.listeners = new ArrayList<>();
        
        // Set up the toggle button action
        toggleButton.setOnAction(event -> toggle());
    }

    /**
     * Toggles the sidebar visibility.
     */
    @FXML
    private void toggle() {
        setVisible(!isVisible);
    }

    /**
     * Sets the visibility of the sidebar.
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
        updateToggleIcon();
        notifyListeners();
    }

    /**
     * Updates the toggle icon based on the current visibility state.
     */
    private void updateToggleIcon() {
        toggleIcon.setGlyphName(isVisible ? ICON_CHEVRON_LEFT : ICON_CHEVRON_RIGHT);
    }

    /**
     * Notifies all registered listeners of the sidebar toggle.
     */
    private void notifyListeners() {
        for (SidebarToggleListener listener : listeners) {
            listener.onSidebarToggle(isVisible);
        }
    }

    /**
     * Registers a listener for sidebar toggle events.
     */
    public void addToggleListener(SidebarToggleListener listener) {
        listeners.add(listener);
    }

    /**
     * Adds a filter section to the sidebar.
     */
    public void addSection(String title, Node content) {
        VBox section = new VBox();
        section.setSpacing(5);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("sidebar-section-label");
        
        section.getChildren().addAll(titleLabel, content);
        sidebarContent.getChildren().add(section);
    }

    /**
     * Removes all filter sections from the sidebar.
     */
    public void clearSections() {
        sidebarContent.getChildren().clear();
    }

    /**
     * Returns whether the sidebar is currently visible.
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Returns the root container of the sidebar.
     */
    public VBox getRoot() {
        return sidebarContainer;
    }
} 