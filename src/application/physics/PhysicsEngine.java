/**
 * 
 */
package application.physics;

import java.util.List;
import java.util.stream.IntStream;

import application.entity.CircleEntity;
import application.entity.Entity;
import application.math.Vector2;

/**
 * @author Davis
 *
 */
public class PhysicsEngine {
	
	private static final float CORRECTION_PERCENT = 0.2f;
	private static final float CORRECTION_SLOP = 0.01f;
	
	private List<Entity> entities;
	
	/**
	 * @param entities
	 */
	public PhysicsEngine(List<Entity> entities) {
		super();
		this.entities = entities;
	}
	
	/**
	 * Performs collision checks and resolves them
	 */
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			CircleEntity a = (CircleEntity)entities.get(i);
			for (int j = i + 1; j < entities.size(); j++) {
				CircleEntity b = (CircleEntity)entities.get(j);
				
				if (checkCollision(a, b)) {
					CollisionManifold m = new CollisionManifold(a, b);
					resolveCollision(m);
					positionCorrection(m);
				}
			}
		}
	}
	
	/**
	 * Performs collision checks and resolves them
	 */
	public void updateParallel() {
		
		IntStream intStream = IntStream.range(0, entities.size());
		
		intStream.parallel().forEach(i -> {
			CircleEntity a = (CircleEntity)entities.get(i);
			for (int j = i + 1; j < entities.size(); j++) {
				CircleEntity b = (CircleEntity)entities.get(j);
				
				if (checkCollision(a, b)) {
					CollisionManifold m = new CollisionManifold(a, b);
					resolveCollision(m);
					positionCorrection(m);
				}
			}
			
		});
	}

	/**
	 * Resolves collision between two entities
	 * @param cm
	 */
	public void resolveCollision(CollisionManifold cm) {
		
		CircleEntity a = cm.getEntityA();
		CircleEntity b = cm.getEntityB();
		
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
		
		PhysicsProperties aProperties = a.getPhyProperties();
		PhysicsProperties bProperties = b.getPhyProperties();
		
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
		
		correctionVectorA.linearMutliply(cm.getEntityA().getPhyProperties().getInvMass());
		correctionVectorB.linearMutliply(cm.getEntityB().getPhyProperties().getInvMass());
		
		cm.getEntityA().getPosition().minus(correctionVectorA);
		cm.getEntityB().getPosition().add(correctionVectorB);
	}
	
	/**
	 * Checks if another circle entity has collided or clipped.
	 * @param other
	 * @return
	 */
	public boolean checkCollision(CircleEntity a, CircleEntity b) {
		float r = a.getRadius() + b.getRadius();
		r *= r;
		float distX = (a.getPosition().getX() - b.getPosition().getX());
		distX *= distX;
		float distY = (a.getPosition().getY() - b.getPosition().getY());
		distY *= distY;
		return r > distX + distY;
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
}
