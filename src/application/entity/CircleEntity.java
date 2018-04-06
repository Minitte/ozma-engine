package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleEntity extends Entity {
	
	private float radius;
	private float boxRadius;
	
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
	 * Check if theres a collision with the given pos
	 * @param pos
	 * @return
	 */
	public boolean isWithin(Vector2 pos) {
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

}
