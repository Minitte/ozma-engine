package application.entity;

import java.util.Vector;

import application.math.Vector2;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
	
	private Vector2 position;

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

	public void setPosition(float x, float y) {
		position.setX(x);
		position.setY(y);
	}
	
}
