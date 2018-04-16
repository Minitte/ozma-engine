package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import application.physics.shape.Shape;
import javafx.scene.canvas.GraphicsContext;

public class BasicPhysicsEntity extends Entity {
	private Shape shape;
	private PhysicsProperties properties;
	private boolean frozen;
	private float angularVelocity;

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
	}
	
	public void physicsUpdate(float delta) {
		velocity.add(new Vector2(0f, 2f));
		Vector2 scaledVelocity = velocity.clone();
		scaledVelocity.linearMutliply(delta);

		if (Math.abs(scaledVelocity.getX()) < 0.0001f) {
			scaledVelocity.setX(0f);
		}

		if (Math.abs(scaledVelocity.getY()) < 0.0001f) {
			scaledVelocity.setY(0f);
		}

		position.add(scaledVelocity);
		shape.moveTo(position);

		// rotate
		shape.setAngle(shape.getAngle() + (angularVelocity * delta));
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		shape.render(gc, delta);
		gc.strokeText(
				String.format("P: (%d, %d)%nA: %2f%nVl: (%d, %d)%nVa: %d", (int) position.getX(), (int) position.getY(),
						shape.getAngle(), (int) velocity.getX(), (int) velocity.getY(), (int) angularVelocity),
				position.getX() + 10f, position.getY() - 10f);
	}

	@Override
	public boolean pointWithin(Vector2 point) {
		return shape.pointWithin(point);
	}

	/**
	 * Applies a force
	 * 
	 * @param force
	 */
	public void applyForce(Vector2 force) {
		velocity.add(force);
	}

	/**
	 * Applies a force at a point in the entity caussing a rotation
	 * 
	 * @param force
	 * @param point
	 */
	public void applyForce(Vector2 force, Vector2 point) {
		velocity.add(force.clone().linearMutliply(properties.getInvMass()));
		angularVelocity += properties.getInvInteria() * point.crossProduct(force);
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

	/**
	 * @param frozen
	 *            the frozen to set
	 */
	public void setFrozen() {
		properties.setMass(0f);
		properties.setInteria(0f);
		frozen = true;
	}

	/**
	 * @return the frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}


	/**
	 * @return the angularVelocity
	 */
	public float getAngularVelocity() {
		return angularVelocity;
	}

	/**
	 * @param angularVelocity
	 *            the angularVelocity to set
	 */
	public void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

}
