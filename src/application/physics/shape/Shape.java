package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public abstract class Shape {

	/**
	 * The xy representing the center of the shape
	 */
	protected Vector2 position;

	/**
	 * The angle or rotation of the shape in radians
	 */
	protected float angle;

	/**
	 * @param position
	 * @param angle
	 */
	public Shape(Vector2 position, float angle) {
		super();
		this.position = position;
		this.angle = angle;
	}

	/**
	 * Renders the outline of the shape
	 * 
	 * @param gc
	 * @param delta
	 */
	public abstract void render(GraphicsContext gc, float delta);

	/**
	 * Checks if a point is within shape
	 * @param point
	 * @return
	 */
	public abstract boolean pointWithin(Vector2 point);
	
	/**
	 * Gets the vertex that is farthest away in the given direction
	 * @param dir a unit vector 
	 * @return
	 */
	public abstract Vector2 GetSupport(Vector2 dir);
	
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

}
