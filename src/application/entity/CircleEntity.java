package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import javafx.scene.canvas.GraphicsContext;

public class CircleEntity extends Entity {

	private static float speed = 20;
	private static float density = 1;
	
	private float radius;
	
	private PhysicsProperties phyProperties;
	
	public CircleEntity(Vector2 position, float radius) {
		super(position);
		this.radius = radius;
		
		float area = radius;
		area *= area;
		area *= (float)Math.PI;
		phyProperties = new PhysicsProperties(area * density, 1);
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

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
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
