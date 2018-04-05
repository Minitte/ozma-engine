/**
 * 
 */
package application.physics;

import java.util.List;

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
	
	public void update() {
		for (int i = 0; i < entities.size(); i++) {
			CircleEntity a = (CircleEntity)entities.get(i);
			for (int j = i + 1; j < entities.size(); j++) {
				CircleEntity b = (CircleEntity)entities.get(j);
				
				if (a.checkCollision(b)) {
					CollisionManifold m = new CollisionManifold(a, b);
					resolveCollision(m);
					correctPosition(m);
				}
			}
		}
	}

	public void resolveCollision(CollisionManifold cm) {
		
		CircleEntity a = cm.getEntityA();
		CircleEntity b = cm.getEntityB();
		
		// relative velocity
		Vector2 rv = b.getVelocity().clone();
		rv.minus(a.getVelocity());
		
		// relative velocity in normal direction
		Vector2 normalRV = rv.getNormal();
		float velAlongNormal = rv.dot(normalRV);
		
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
		j /= 1f / aProperties.getMass() + 1f / bProperties.getMass();
		
		// apply impulse
		Vector2 impulseA = normalRV.clone();
		impulseA.linearMutliply(j);
		
		Vector2 impulseB = impulseA.clone();
		
		impulseA.linearMutliply(aProperties.getInvMass());
		impulseB.linearMutliply(bProperties.getInvMass());
		
		a.getVelocity().minus(impulseA);
		b.getVelocity().add(impulseB);
	}
	
	private void correctPosition(CollisionManifold cm) {
		// TODO
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
