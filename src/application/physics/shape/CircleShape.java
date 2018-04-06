package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class CircleShape extends Shape {

	private float radius;
	private float boxRadius;

	/**
	 * @param position
	 * @param angle
	 * @param radius
	 */
	public CircleShape(Vector2 position, float angle, float radius) {
		super(position, angle);
		this.radius = radius;
		boxRadius = radius * 2;
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeOval(position.getX() - radius, position.getY() - radius, boxRadius, boxRadius);
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

}
