/**
 * 
 */
package application.entity;

import application.math.Vector2;
import application.physics.PhysicsProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Davis
 *
 */
public class RectangleEntity extends Entity {

	private Vector2 pointA; // top left corner
	private Vector2 pointB; // bottom right corner
	private float width;
	private float height;
	
	/**
	 * @param position
	 * @param width
	 * @param height
	 */
	public RectangleEntity(Vector2 position, float width, float height) {
		super(position);
		this.width = width;
		this.height = height;
		pointA = new Vector2(position.getX() - (width / 2f), position.getY() - (height / 2f));
		pointB = new Vector2(position.getX() + (width / 2f), position.getY() + (height / 2f));
		
		float area = (pointB.getX() - pointA.getX()) * (pointB.getY() - pointA.getY());
		area = area > 0 ? area : -area;
		
		phyProperties = new PhysicsProperties(area * density, 0.5f);
		phyProperties.setVelocityDamping(1.0f);
	}
	
	/**
	 * Checks if the point is in the rectangle
	 * @param p
	 * @return
	 */
	public boolean isWithin(Vector2 p) {
		if (pointA.getX() > p.getX() || p.getX() > pointB.getX()) {
			return false;
		}
		
		if (pointA.getY() > p.getY() || p.getY() > pointB.getY()) {
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see application.entity.Entity#update(float)
	 */
	@Override
	public void update(float delta) {
		Vector2 scaledVelocity = velocity.clone();
		scaledVelocity.linearMutliply(delta);
		position.add(scaledVelocity);
		setPosition(position);
		
		// apply damping effects
		Vector2 dampingVector = new Vector2(phyProperties.getVelocityDamping(), phyProperties.getVelocityDamping());
		dampingVector.linearMutliply(delta);
		velocity.reduce(dampingVector);
	}

	/* (non-Javadoc)
	 * @see application.entity.Entity#render(javafx.scene.canvas.GraphicsContext, float)
	 */
	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.setFill(Color.DARKGREEN);
		gc.fillRect(pointA.getX(), pointA.getY(), width, height);
	}
	
	/* (non-Javadoc)
	 * @see application.entity.Entity#setPosition(application.math.Vector2)
	 */
	@Override
	public void setPosition(Vector2 position) {
		this.position = position;
		
		pointA = new Vector2(position.getX() - (width / 2f), position.getY() - (height / 2f));
		pointB = new Vector2(position.getX() + (width / 2f), position.getY() + (height / 2f));
	}

	/**
	 * @return the pointA
	 */
	public Vector2 getPointA() {
		return pointA;
	}

	/**
	 * @param pointA the pointA to set
	 */
	public void setPointA(Vector2 pointA) {
		this.pointA = pointA;
	}

	/**
	 * @return the pointB
	 */
	public Vector2 getPointB() {
		return pointB;
	}

	/**
	 * @param pointB the pointB to set
	 */
	public void setPointB(Vector2 pointB) {
		this.pointB = pointB;
	}
}
