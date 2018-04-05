package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleEntity extends Entity {
	
	private static float density = 1;
	
	private float radius;
	private float boxRadius;
	
	private PhysicsProperties phyProperties;
	
	public CircleEntity(Vector2 position, float radius) {
		super(position);
		this.radius = radius;
		boxRadius = radius * 2f;
		
		float area = radius;
		area *= area;
		area *= (float)Math.PI;
		phyProperties = new PhysicsProperties(area * density, 1);
		phyProperties.setVelocityDamping(1.0f);
	}
	
	/**
	 * Checks if another circle entity has collided or clipped.
	 * @param other
	 * @return
	 */
	public boolean checkCollision(CircleEntity other) {
		float r = radius + other.radius;
		r *= r;
		float distX = (position.getX() - other.position.getX());
		distX *= distX;
		float distY = (position.getY() - other.position.getY());
		distY *= distY;
		return r > distX + distY;
	}
	
	/**
	 * Check if theres a collision with the given pos
	 * @param pos
	 * @return
	 */
	public boolean checkCollision(Vector2 pos) {
		float r = radius;
		r *= r;
		float distX = (position.getX() - pos.getX());
		distX *= distX;
		float distY = (position.getY() - pos.getY());
		distY *= distY;
		return r > distX + distY;
	}

	@Override
	public void update(float delta) {
		Vector2 scaledVelocity = velocity.clone();
		scaledVelocity.linearMutliply(delta);
		position.add(scaledVelocity);
		
		// apply damping effects
		Vector2 dampingVector = new Vector2(phyProperties.getVelocityDamping(), phyProperties.getVelocityDamping());
		dampingVector.linearMutliply(delta);
		velocity.reduce(dampingVector);
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.setFill(Color.BLUE);
		gc.fillOval(position.getX() - radius, position.getY() - radius, boxRadius, boxRadius);
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
