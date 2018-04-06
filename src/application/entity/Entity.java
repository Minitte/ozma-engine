package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
	
	protected static float density = 0.0001f;
	
	/**
	 * A xy vector representing the position of the center of the entity 
	 */
	protected Vector2 position;
	
	/**
	 * A xy vector representing the velocity that this object has
	 */
	protected Vector2 velocity;

	/**
	 * Physics properties for the entity
	 */
	protected PhysicsProperties phyProperties;
	
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
	
	/**
	 * @return the phyProperties
	 */
	public PhysicsProperties getPhyProperties() {
		return phyProperties;
	}

	/**
	 * @param phyProperties the phyProperties to set
	 */
	public void setPhyProperties(PhysicsProperties phyProperties) {
		this.phyProperties = phyProperties;
	}
	
}
