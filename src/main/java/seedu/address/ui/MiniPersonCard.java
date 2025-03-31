package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A mini UI component that displays basic information of a {@code Person}.
 */
public class MiniPersonCard extends UiPart<Region> {
    private static final String FXML = "MiniPersonCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label school;
    @FXML
    private FlowPane skills;

    /**
     * Creates a {@code MiniPersonCard} with the given {@code Person}.
     */
    public MiniPersonCard(Person person) {
        super(FXML);
        name.setText(person.getName().toString());
        school.setText(person.getSchool().toString());
        person.getSkills().stream()
                .forEach(skill -> {
                    Label skillLabel = new Label(skill.skillName());
                    skillLabel.getStyleClass().add("skill-label-small");
                    skills.getChildren().add(skillLabel);
                });
    }
}
