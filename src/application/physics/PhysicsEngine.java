/**
 * 
 */
package application.physics;

import java.util.ArrayList;
import java.util.List;

import application.entity.BasicPhysicsEntity;
import application.math.Vector2;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;

/**
 * @author Davis
 *
 */
public class PhysicsEngine {
	
	private static final float CORRECTION_PERCENT = 0.1f;
	private static final float CORRECTION_SLOP = 0.01f;
	
	private List<BasicPhysicsEntity> entities;
	private List<Potential> potentialCollisions;
	
	/**
	 * @param entities
	 */
	public PhysicsEngine(List<BasicPhysicsEntity> entities) {
		super();
		this.entities = entities;
		potentialCollisions = new ArrayList<>();
	}
	
	/**
	 * Performs collision checks and resolves them
	 */
	public void update(boolean parallel) {
		
		findPotentialCollisions();
	}
	
	/**
	 * Finds potential collisions based on the radius of the shapes
	 */
	private void findPotentialCollisions() {
		for (int i = 0; i < entities.size(); i++) {
			BasicPhysicsEntity a = entities.get(i);
			for (int j = i + 1; j < entities.size(); j++) {
				BasicPhysicsEntity b = entities.get(j);
				
				float dist = b.getPosition().clone().minus(a.getPosition()).getLengthSquared();
				float rsq = a.getShape().getLooseCheckRadius() + b.getShape().getLooseCheckRadius();
				rsq *= rsq;
				if (dist <= rsq) {
					potentialCollisions.add(new Potential(a, b));
				}
			}
		}
		
	}
	
	/**
	 * Checks if any of the potential collisions are actually collisions
	 */
	private void findActualCollisions() {
		for (int i = 0; i < potentialCollisions.size(); i++) {
			Potential pot = potentialCollisions.get(i);
		}
		
		potentialCollisions.clear();
	}

	/**
	 * Resolves collision between two entities
	 * @param cm
	 */
	public void resolveCollision(CollisionManifold cm) {
		
		BasicPhysicsEntity a = cm.getEntityA();
		BasicPhysicsEntity b = cm.getEntityB();
		
		// relative velocity
		Vector2 rv = b.getVelocity().clone();
		rv.minus(a.getVelocity());
		
		// relative velocity in normal direction
		Vector2 normal = cm.getNormal();
		float velAlongNormal = rv.dot(normal);
		
		// do nothing for separating directions
		if (velAlongNormal > 0f) {
			return;
		}
		
		PhysicsProperties aProperties = a.getProperties();
		PhysicsProperties bProperties = b.getProperties();
		
		// pick smaller restitution
		float e = min(aProperties.getRestitution(), bProperties.getRestitution());
		
		// impulse scalar
		float j = -(1f + e) * velAlongNormal;
		j /= aProperties.getInvMass() + bProperties.getInvMass();
		
		// apply impulse
		Vector2 impulseA = normal.clone();
		impulseA.linearMutliply(j);
		
		Vector2 impulseB = impulseA.clone();
		
		impulseA.linearMutliply(aProperties.getInvMass());
		impulseB.linearMutliply(bProperties.getInvMass());
		
		a.getVelocity().minus(impulseA);
		b.getVelocity().add(impulseB);
	}
	
	/**
	 * Counter acts object sinking due to floating point errors
	 * @param cm
	 */
	private void positionCorrection(CollisionManifold cm) {
		float correctionMult = max(cm.getPenDepth() - CORRECTION_SLOP, 0.0f) * CORRECTION_PERCENT;
		Vector2 correctionVectorA = cm.getNormal().clone();
		correctionVectorA.linearMutliply(correctionMult);
		
		Vector2 correctionVectorB = correctionVectorA.clone();
		
		correctionVectorA.linearMutliply(cm.getEntityA().getProperties().getInvMass());
		correctionVectorB.linearMutliply(cm.getEntityB().getProperties().getInvMass());
		
		cm.getEntityA().getPosition().minus(correctionVectorA);
		cm.getEntityB().getPosition().add(correctionVectorB);
	}
	
	/**
	 * Check if two shapes are overlapping / clipping / colliding 
	 * @param entA
	 * @param entB
	 * @return
	 */
	public boolean checkCollision(Shape a, Shape b) {
		int typeA = a.getShapeType();
		int typeB = b.getShapeType();
		
		if (typeA == CircleShape.TYPE_ID && typeA == CircleShape.TYPE_ID) {
			return circleVsCircle((CircleShape)a, (CircleShape)b);
		}
		
		return false;
	}
	
	/**
	 * collision check between circles
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean circleVsCircle(CircleShape a, CircleShape b) {
		float r = a.getRadius() + b.getRadius();

		r *= r;

		float distX = (a.getPosition().getX() - b.getPosition().getX());

		distX *= distX;

		float distY = (a.getPosition().getY() - b.getPosition().getY());

		distY *= distY;

		return r > distX + distY;
	}
	
	/**
	 * collision check between circle and rect
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean circleVsRect(CircleShape a, RectShape b) {
		Vector2 collisionVector = b.getPosition().clone().minus(a.getPosition()).Normalize();
		Vector2 rectContact = b.getVertice(collisionVector);
		Vector2 circContact = a.getVertice(collisionVector.clone().linearMutliply(-1f));
		
		float rectDist = collisionVector.dot(rectContact);
		float circDist = collisionVector.dot(circContact);
		
		return circDist - rectDist <= 0;
	}
	
	
	
	/**
	 * Returns the smaller value of the given
	 * @param a
	 * @param b
	 * @return
	 */
	private float min(float a, float b) {
		return a <= b ? a : b;
	}
	
	/**
	 * Returns the larger value of the given
	 * @param a
	 * @param b
	 * @return
	 */
	private float max(float a, float b) {
		return a >= b ? a : b;
	}
	
	private class Potential {
		BasicPhysicsEntity a, b;

		/**
		 * @param a
		 * @param b
		 */
		public Potential(BasicPhysicsEntity a, BasicPhysicsEntity b) {
			super();
			this.a = a;
			this.b = b;
		}
		
		
	}
}
