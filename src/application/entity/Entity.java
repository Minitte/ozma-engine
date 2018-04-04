package application.entity;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
	
	/**
	 * A xy vector representing the position of the center of the entity 
	 */
	protected Vector2 position;

	/**
	 * Constructor for an entity
	 * @param position
	 */
	public Entity(Vector2 position) {
		super();
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
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
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
}
