/**
 * 
 */
package application.physics;

import application.entity.BasicPhysicsEntity;
import application.math.Vector2;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;

/**
 * @author Davis
 *
 */
public class CollisionManifold {
	private BasicPhysicsEntity entityA;
	private BasicPhysicsEntity entityB;
	private float penDepth;
	private Vector2 normal;

	/**
	 * @param entityA
	 * @param entityB
	 */
	public CollisionManifold(BasicPhysicsEntity a, BasicPhysicsEntity b) {
		super();
		entityA = a;
		entityB = b;

		normal = calulateNormal();
		
		penDepth = calulateDepth(entityA.getShape(), entityB.getShape());
	}
	
	/**
	 * Calculates the normal
	 */
	private Vector2 calulateNormal() {
		Vector2 v = entityB.getPosition().clone();
		v.minus(entityA.getPosition());
		v.Normalize();
		return v;
	}
	
	/**
	 * Calculates the depth between two shapes
	 * @param shapeA
	 * @param shapeB
	 * @return
	 */
	private float calulateDepth(Shape shapeA, Shape shapeB) {
		// two circles
		if (shapeA instanceof CircleShape && shapeB instanceof CircleShape) {
			return calculateCircleDepth((CircleShape)shapeA, (CircleShape)shapeB);
		} 
		
		// two rectangles
		else if (shapeA instanceof RectShape && shapeB instanceof RectShape) {
			return calculateRectDepth((RectShape)shapeA, (RectShape)shapeB);
		}
		
		return 0;
		
	}
	
	/**
	 * Calculates the penetration depth between two circles
	 * @param a
	 * @param b
	 * @return
	 */
	private float calculateCircleDepth(CircleShape a, CircleShape b) {
		Vector2 v = b.getPosition().clone();
		v.minus(a.getPosition());
		
		float sum = a.getRadius() + b.getRadius();
		return sum - v.getLength();
	}
	
	/**
	 * Calculates the penetration depth between two rectangles
	 * @param a
	 * @param b
	 * @return
	 */
	private float calculateRectDepth(RectShape a, RectShape b) {
		float xPen = 0;
		float yPen = 0;
		
		if (a.getPointB().getX() > b.getPointA().getX()) {
			xPen = a.getPointB().getX() - b.getPointA().getX();
		} else {
			xPen = a.getPointA().getX() - b.getPointB().getX();
		}
		
		if (a.getPointB().getY() > b.getPointA().getY()) {
			yPen = a.getPointB().getY() - b.getPointA().getX();
		} else {
			yPen = a.getPointA().getY() - b.getPointB().getY();
		}
		
		// absolute value
		xPen = xPen > 0f ? xPen : -xPen;
		yPen = yPen > 0f ? yPen : -yPen;
		
		return xPen > yPen ? yPen : xPen;
	}

	/**
	 * @return the entityA
	 */
	public BasicPhysicsEntity getEntityA() {
		return entityA;
	}

	/**
	 * @return the entityB
	 */
	public BasicPhysicsEntity getEntityB() {
		return entityB;
	}

	/**
	 * @return the penDepth
	 */
	public float getPenDepth() {
		return penDepth;
	}

	/**
	 * @return the normal
	 */
	public Vector2 getNormal() {
		return normal;
	}

}
