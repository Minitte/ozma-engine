/**
 * 
 */
package application.physics;

import application.entity.CircleEntity;
import application.entity.Entity;
import application.entity.RectangleEntity;
import application.math.Vector2;

/**
 * @author Davis
 *
 */
public class CollisionManifold {
	private Entity entityA;
	private Entity entityB;
	private float penDepth;
	private Vector2 normal;

	/**
	 * @param entityA
	 * @param entityB
	 */
	public CollisionManifold(CircleEntity entityA, CircleEntity entityB) {
		super();
		this.entityA = entityA;
		this.entityB = entityB;

		Vector2 v = entityB.getPosition().clone();
		v.minus(entityA.getPosition());

		float sum = entityA.getRadius() + entityB.getRadius();
		penDepth = sum - v.getLength();

		v.Normalize();
		normal = v;
	}
	
	/**
	 * @param entityA
	 * @param entityB
	 */
	public CollisionManifold(RectangleEntity entityA, RectangleEntity entityB) {
		super();
		this.entityA = entityA;
		this.entityB = entityB;

		Vector2 v = entityB.getPosition().clone();
		v.minus(entityA.getPosition());
		v.Normalize();
		normal = v;
		
		float xPen = 0;
		float yPen = 0;
		
		// find depth based on smallest overlap on x or y
		
		if (entityA.getPointB().getX() > entityB.getPointA().getX()) {
			xPen = entityA.getPointB().getX() - entityB.getPointA().getX();
		} else {
			xPen = entityA.getPointA().getX() - entityB.getPointB().getX();
		}
		
		if (entityA.getPointB().getY() > entityB.getPointA().getY()) {
			yPen = entityA.getPointB().getY() - entityB.getPointA().getX();
		} else {
			yPen = entityA.getPointA().getY() - entityB.getPointB().getY();
		}
		
		// absolute value
		xPen = xPen > 0f ? xPen : -xPen;
		yPen = yPen > 0f ? yPen : -yPen;
		
		penDepth = xPen > yPen ? yPen : xPen;
	}

	/**
	 * @return the entityA
	 */
	public Entity getEntityA() {
		return entityA;
	}

	/**
	 * @return the entityB
	 */
	public Entity getEntityB() {
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
