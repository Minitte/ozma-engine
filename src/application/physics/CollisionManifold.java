/**
 * 
 */
package application.physics;

import application.entity.CircleEntity;
import application.math.Vector2;

/**
 * @author Davis
 *
 */
public class CollisionManifold {
	private CircleEntity entityA;
	private CircleEntity entityB;
	private float penDepth;
	private Vector2 normal;

	/**
	 * @param entityA
	 * @param entityB
	 * @param depth
	 */
	public CollisionManifold(CircleEntity entityA, CircleEntity entityB) {
		super();
		this.entityA = entityA;
		this.entityB = entityB;

		Vector2 v = entityB.getPosition().clone();
		v.minus(entityA.getPosition());

		float sum = entityA.getRadius() + entityB.getRadius();
		penDepth = sum - v.getLength();

		normal = v.getNormal();
	}

	/**
	 * @return the entityA
	 */
	public CircleEntity getEntityA() {
		return entityA;
	}

	/**
	 * @return the entityB
	 */
	public CircleEntity getEntityB() {
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
