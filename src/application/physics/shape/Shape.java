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
	 * List of vertices
	 */
	protected Vector2[] vertices;

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
	 * Move to the spot
	 * @param dest
	 */
	public abstract void moveTo(Vector2 dest);
	
	/**
	 * Gets the point that is mostly aligned with the direction
	 * @param direction a vector representing direction
	 */
	public abstract Vector2 getVertice(Vector2 direction);
	
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
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
