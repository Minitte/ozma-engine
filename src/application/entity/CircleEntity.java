package application.entity;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class CircleEntity extends Entity {

	private static float speed = 20;
	
	private float radius;
	
	public CircleEntity(Vector2 position, float radius) {
		super(position);
		this.radius = radius;
	}
	
	/**
	 * Checks if another circle entity has collided or clipped.
	 * @param other
	 * @return
	 */
	public boolean checkCollision(CircleEntity other) {
		float r = radius + other.radius;
		r *= r;
		float distX = (position.getX() + other.position.getX());
		distX *= distX;
		float distY = (position.getY() + other.position.getY());
		distY *= distY;
		return r < distX + distY;
	}

	@Override
	public void update(float delta) {
		position.setX(position.getX() + speed * delta);
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.fillOval(position.getX() - radius, position.getY() - radius, radius, radius);
	}

}
