/**
 * 
 */
package application.physics;

import java.util.List;
import java.util.stream.IntStream;

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
	
	private static final float CORRECTION_PERCENT = 0.2f;
	private static final float CORRECTION_SLOP = 0.01f;
	
	private List<BasicPhysicsEntity> entities;
	
	/**
	 * @param entities
	 */
	public PhysicsEngine(List<BasicPhysicsEntity> entities) {
		super();
		this.entities = entities;
	}
	
	/**
	 * Performs collision checks and resolves them
	 */
	public void update(boolean parallel) {
		
		if (parallel) {
			IntStream intStream = IntStream.range(0, entities.size());
			
			intStream.parallel().forEach(i -> {
				BasicPhysicsEntity a = entities.get(i);
				for (int j = i + 1; j < entities.size(); j++) {
					BasicPhysicsEntity b = entities.get(j);
					
					
					if (checkCollision(a.getShape(), b.getShape())) {
						CollisionManifold m = new CollisionManifold(a, b);
						resolveCollision(m);
						positionCorrection(m);
					}
				}
				
			});
		} else {
			for (int i = 0; i < entities.size(); i++) {
				BasicPhysicsEntity a = entities.get(i);
				for (int j = i + 1; j < entities.size(); j++) {
					BasicPhysicsEntity b = entities.get(j);
					
					
					if (checkCollision(a.getShape(), b.getShape())) {
						CollisionManifold m = new CollisionManifold(a, b);
						resolveCollision(m);
						positionCorrection(m);
					}
				}
			}
		}
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
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean checkCollision(Shape shapeA, Shape shapeB) {
		// two circles
		if (shapeA instanceof CircleShape && shapeB instanceof CircleShape) {
			return checkCollision((CircleShape)shapeA, (CircleShape)shapeB);
		} 
		
		// two rectangles
		else if (shapeA instanceof RectShape && shapeB instanceof RectShape) {
			return checkCollision((RectShape)shapeA, (RectShape)shapeB);
		}
		
		return false;
	} 
	
	/**
	 * Checks if another circle entity has collided or clipped.
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean checkCollision(CircleShape a, CircleShape b) {
		float r = a.getRadius() + b.getRadius();
		r *= r;
		float distX = (a.getPosition().getX() - b.getPosition().getX());
		distX *= distX;
		float distY = (a.getPosition().getY() - b.getPosition().getY());
		distY *= distY;
		return r > distX + distY;
	}
	
	/**
	 * Checks if two rectangles had collided or clipped.
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean checkCollision(RectShape a, RectShape b) {
		if (a.getPointB().getX() < b.getPointA().getX() || a.getPointA().getX() > b.getPointB().getX()) {
			return false;
		}
		
		if (a.getPointB().getY() < b.getPointA().getY() || a.getPointA().getY() > b.getPointB().getY()) {
			return false;
		}
		
		return true;
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
