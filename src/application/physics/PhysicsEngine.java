/**
 * 
 */
package application.physics;

import java.util.ArrayList;
import java.util.List;

import application.entity.BasicPhysicsEntity;
import application.math.Vector2;
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
		findActualCollisions();
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
			
			if (checkCollision(pot.a.getShape(), pot.b.getShape())) {
				CollisionManifold cm = new CollisionManifold(pot.a, pot.b);
				
				if (cm.getPenDepth() > 0) {
					resolveCollision(cm);
					positionCorrection(cm);
				}
			}
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
		float velAlongNormal = normal.dot(rv);
		
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
		
//		Vector2 colVector = b.getPosition().clone().minus(a.getPosition()).Normalize();
	
		Vector2[] checkAxis = a.getFaceNormals();
		
		for (int i = 0; i < checkAxis.length; i++) {
			if (!projectionCheck(b, a, checkAxis[i]) && !projectionCheck(a, b, checkAxis[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * projection based collision checking
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean projectionCheck(Shape a, Shape b, Vector2 collisionVector) {
		Vector2 contactB = b.getVertice(a);
		Vector2 contactA = a.getVertice(b);
		
		float distA = collisionVector.dot(contactA);
		float distB = collisionVector.dot(contactB);
		
//		distA = distA > 0 ? distA : -distA;
//		distB = distB > 0 ? distB : -distB;
		
		return distB - distA > 0;
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
