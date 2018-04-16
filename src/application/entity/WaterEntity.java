package application.entity;

import application.Main;
import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class WaterEntity extends Entity {

    // Spring constant
    public static float k = 0.025f;

    // Dampening factor
    public static float dampening = 0.025f;

    // The natural height of the water
    public static float waterSurface = Main.START_HEIGHT - 200;

    private float positionY, velocityY;

    /**
     * Constructor.
     * @param position the starting position of the water
     */
    public WaterEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void update(float delta) {
        // Springs cannot move horizontally, so only use vertical components
        positionY = position.getY();
        velocityY = velocity.getY();

        // Difference between this height and the natural height
        float heightDiff = positionY - waterSurface;

        // Acceleration = -(k/m)x - dv, ignoring mass
        float acceleration = -k * heightDiff - dampening * velocityY;

        // Update position and velocity
        position.add(velocity);
        velocity.add(new Vector2(0, acceleration));
    }

    @Override
    public void render(GraphicsContext gc, float delta) {

    }

    @Override
    public boolean pointWithin(Vector2 point) {
        return false;
    }
}
