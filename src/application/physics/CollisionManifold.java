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
		
		Vector2 faceNormal = new Vector2(0f, -1f);

		// Calculate half extents along x axis for each object
		float extentAx = (entityA.getPointB().getX() - entityA.getPointA().getX()) / 2f;
		float extentBx = (entityB.getPointB().getX() - entityB.getPointA().getX()) / 2f;
		
		// overlap on x
		float overlapX = extentAx + extentBx - Math.abs(faceNormal.getX());
		
		// test x axis
		if (overlapX > 0) {
			float extentAy = (entityA.getPointB().getY() - entityA.getPointA().getY()) / 2f;
			float extentBy = (entityB.getPointB().getY() - entityB.getPointA().getY()) / 2f;
			
			float overlapY = extentAy + extentBy - Math.abs(faceNormal.getY());
			
			// test y axis
			if (overlapY > 0) {
				
				// which is more
				if (overlapX > overlapY) {
					if (faceNormal.getX() < 0) {
						normal = new Vector2(-1f, 0f);
					} else {
						normal = new Vector2(0f, 0f);
					}
					
					penDepth = overlapX;
					
					return;
				} else {
					if (faceNormal.getY() < 0) {
						normal = new Vector2(0, -1);
					} else {
						normal = new Vector2(0, 1);
					}
					
					penDepth = overlapY;
					
					return;
				}
			}
		}
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
