package application.ui;

import application.math.Vector2;
import javafx.scene.control.Button;

/**
 * This is a class for buttons that spawn entities.
 */
public class EntityButton extends Button {

    /**
     * Constructor for the button.
     * @param position the position of the button
     * @param buttonText the text of the button
     */
    public EntityButton(Vector2 position, String buttonText) {
        
        // Set the position of the button
        setLayoutX(position.getX());
        setLayoutY(position.getY());

        // Set the text of the button
        setText(buttonText);
    }
}
