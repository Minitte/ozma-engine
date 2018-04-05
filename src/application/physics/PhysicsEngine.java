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
public class PhysicsEngine {
	
	
	public void resolveCollision(CircleEntity a, CircleEntity b) {
		// relative velocity
		Vector2 rv = b.getVelocity().clone();
		rv.minus(a.getVelocity());
		
		// relative velocity in normal direction
		Vector2 normalRV = new Vector2(-rv.getY(), rv.getX());
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
		
		impulseA.linearMutliply(1f / aProperties.getMass());
		impulseB.linearMutliply(1f / bProperties.getMass());
		
		a.getVelocity().minus(impulseA);
		b.getVelocity().add(impulseB);
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
}
