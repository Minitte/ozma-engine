package application.entity;

import application.Main;
import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class WaterEntity extends Entity {

    // Spring constant
    public static float k = 0.03f;

    // Dampening factor
    public static float dampening = 0.05f;

    // The natural height of the water
    public static float waterSurface = Main.START_HEIGHT - 200;

    // Current speed of the water
    public float speed;

    /**
     * Constructor.
     * @param position the starting position of the water
     */
    public WaterEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void update(float delta) {

        // Difference in current height and natural height
        float y = waterSurface - position.getY();

        // Multiply by spring constant
        y *= k;

        // Apply dampening based on direction to go
        if(speed > 0) {
            y -= dampening;
        } else {
            y += dampening;
        }

        // Apply speed
        speed += y;
        position.add(new Vector2(0, speed));
    }

    @Override
    public void render(GraphicsContext gc, float delta) {
        // Do nothing
    }

    @Override
    public boolean pointWithin(Vector2 point) {
        return false;
    }
}
