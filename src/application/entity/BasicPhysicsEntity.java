package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import application.physics.shape.Shape;
import javafx.scene.canvas.GraphicsContext;

public class BasicPhysicsEntity extends Entity {
	private Shape shape;
	private PhysicsProperties properties;

	/**
	 * @param position
	 * @param shape
	 * @param properties
	 */
	public BasicPhysicsEntity(Vector2 position, Shape shape, PhysicsProperties properties) {
		super(position);
		this.shape = shape;
		this.properties = properties;
	}

	@Override
	public void update(float delta) {
		Vector2 scaledVelocity = velocity.clone();
		scaledVelocity.linearMutliply(delta);
		position.add(scaledVelocity);
		shape.setPosition(position);

		// apply damping effects
		Vector2 dampingVector = new Vector2(properties.getVelocityDamping(), properties.getVelocityDamping());
		dampingVector.linearMutliply(delta);
		velocity.reduce(dampingVector);

	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		shape.render(gc, delta);
	}
	
	@Override
	public boolean pointWithin(Vector2 point) {
		return shape.pointWithin(point);
	}

	/**
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * @param shape
	 *            the shape to set
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * @return the properties
	 */
	public PhysicsProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(PhysicsProperties properties) {
		this.properties = properties;
	}

}
