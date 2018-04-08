package application.entity;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
	
	/**
	 * A xy vector representing the position of the center of the entity 
	 */
	protected Vector2 position;
	
	/**
	 * A xy vector representing the velocity that this object has
	 */
	protected Vector2 velocity;
	
	/**
	 * Constructor for an entity
	 * @param position
	 */
	public Entity(Vector2 position) {
		super();
		this.position = position;
		velocity = new Vector2();
	}

	/**
	 * Updates the data of the entity
	 * @param delta
	 */
	public abstract void update(float delta);
	
	/**
	 * Renders the entity
	 * @param gc
	 * @param delta
	 */
	public abstract void render(GraphicsContext gc, float delta);
	
	/**
	 * Checks if a point is within entity
	 * @param point
	 * @return
	 */
	public abstract boolean pointWithin(Vector2 point);
	
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * @return the velocity
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
}
