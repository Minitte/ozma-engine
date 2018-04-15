/**
 * 
 */
package application.physics;

import java.util.ArrayList;
import java.util.List;

import application.entity.BasicPhysicsEntity;
import application.math.Projection;
import application.math.Vector2;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;
import javafx.scene.paint.Color;

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
	 * Clears the engine, removing all references to current entities and collisions.
	 */
	public void clearEngine() {
		entities.clear();
		potentialCollisions.clear();
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
					//positionCorrection(cm);
				}
			}
		}
		
		potentialCollisions.clear();
	}

	/**
	 * Resolves collision between two entities
	 * @param cm
	 */
	private void resolveCollision(CollisionManifold cm) {
		
//		applyCollisionImpluses(cm.getEntityA(), cm.getEntityB());
		applyContactImpulse(cm);
		
		
	}
	
	/**
	 * Applies basic impluse 
	 */
	private void applyCollisionImpluses(BasicPhysicsEntity a, BasicPhysicsEntity b) {		
		PhysicsProperties propA = a.getProperties();
		PhysicsProperties propB = b.getProperties();
		
		Vector2 a2bDir = b.getPosition().clone().minus(a.getPosition()).Normalize();
		Vector2 b2aDir = a2bDir.clone().linearMutliply(-1f);
		
		Vector2 rVelocity = b.getVelocity().clone().add(a.getVelocity());
		
		/*
		 * Push away from each other
		 */
		
		// vector based on the face pointing away from the center of the shape
		Vector2 faceA = a.getShape().getFaceNormalTowards(a2bDir).clone();
		Vector2 faceB = b.getShape().getFaceNormalTowards(b2aDir).clone();
		
		// use smaller restitution value
		float e = propA.getRestitution() < propB.getRestitution() ? propA.getRestitution() : propB.getRestitution();
		
		// scalar along normal
		float dirScalar = rVelocity.dot(a2bDir.getNormal());
		dirScalar = dirScalar > 0 ? dirScalar : -dirScalar;
		
		// impluse
		float j = (-(1 + e)) * dirScalar;
		j /= propA.getInvMass() + propB.getInvMass();
		
		j = j > 0 ? j : -j;
		
		// apply force
		a.applyForce(faceB.linearMutliply(j * propA.getInvMass()));
		b.applyForce(faceA.linearMutliply(j * propB.getInvMass()));
		
		
		/*
		 * Friction
		 */
		
		Vector2 tangent = rVelocity.clone().minus(a2bDir.clone().linearMutliply(rVelocity.dot(a2bDir)));
		tangent.Normalize();
		
		float jt = -rVelocity.dot(a2bDir);
		jt = jt / (propA.getInvMass() + propB.getInvMass());
		
		float jtAbs = jt > 0 ? jt : -jt;
		
		// skip if very small.
		if (jtAbs < 0.0001f) {
			return;
		}
		
		float fricA = propA.getStaticFriction();
		float fricB = propB.getStaticFriction();
		float staticFriction = (float)Math.abs(fricA * fricA + fricB * fricB);
		
		Vector2 frictionImpulse;
		if (jtAbs < j * staticFriction) {
			frictionImpulse = a2bDir.getNormal().linearMutliply(jt);
			
		} else {
			fricA = propA.getDynamicFriction();
			fricB = propB.getDynamicFriction();
			float dynamicFriction = (float)Math.abs(fricA * fricA + fricB * fricB);
			frictionImpulse = a2bDir.getNormal().linearMutliply(-jt * dynamicFriction);
		}
		
		a.applyForce(frictionImpulse);
		b.applyForce(frictionImpulse.linearMutliply(-1f));
	}
	
	/**
	 * applies impulse with contact points
	 * @param cm
	 */
	private void applyContactImpulse(CollisionManifold cm) {
		BasicPhysicsEntity a = cm.getEntityA();
		BasicPhysicsEntity b = cm.getEntityB();
		
		PhysicsProperties propA = a.getProperties();
		PhysicsProperties propB = b.getProperties();
		
		Vector2 a2bDir = b.getPosition().clone().minus(a.getPosition()).Normalize();
		Vector2 b2aDir = a2bDir.clone().linearMutliply(-1f);
		
		Vector2[] contacts = cm.getContacts();
		
		for (int i = 0; i < cm.getNumContact(); i++) {
			
			// radii from center of mass to contact point
			Vector2 ra = contacts[i].clone().minus(a.getPosition());
			Vector2 rb = contacts[i].clone().minus(b.getPosition());
			
			// relative velocity
			Vector2 rv = b.getVelocity().clone()
					.add(Vector2.crossProduct(b.getAngularVelocity(), rb))
					.minus(a.getVelocity())
					.minus(Vector2.crossProduct(a.getAngularVelocity(), ra));
			
			// relative velocity along normal
			float contactVel = Math.abs(a2bDir.dot(rv));
			
			// radii center to contact cross normal
			float raCrossNorm = ra.dot(a2bDir);
			float rbCrossNorm = rb.dot(a2bDir);
			
			// inverse mass plus magic
			float invMassSum = propA.getInvMass() + propB.getInvMass() 
			+ (raCrossNorm * raCrossNorm) * propA.getInvInteria() 
			+ (rbCrossNorm * rbCrossNorm) * propB.getInvInteria();
			
			// take smaller e
			float e = propA.getRestitution() < propB.getRestitution() ? propA.getRestitution() : propB.getRestitution();
			
			// j value for impulse
			float j = -(1.0f + e) * contactVel;
			j /= invMassSum;
			j /= cm.getNumContact();
			
			// apply force
			a.applyForce(a2bDir.clone().linearMutliply(j), ra);
			b.applyForce(b2aDir.clone().linearMutliply(j), rb);
			
			/*
			 * Friction
			 */
			
			// friction rv
//			rv = b.getVelocity().clone()
//					.add(Vector2.crossProduct(b.getAngularVelocity(), rb))
//					.minus(a.getVelocity())
//					.minus(Vector2.crossProduct(a.getAngularVelocity(), ra));
//			
//			// tangent
//			Vector2 t = rv.clone().minus(a2bDir.clone().linearMutliply(rv.dot(a2bDir)));
//			
//			// tangent magnitude
//			float jt = -rv.dot(t);
//			jt /= invMassSum;
//			jt /= cm.getNumContact();
//			
//			// skip small impulses
//			if (Math.abs(jt) < 0.0001f) {
//				return;
//			}
//			
//			float fricA = propA.getStaticFriction();
//			float fricB = propB.getStaticFriction();
//			float staticFriction = (float)Math.abs(fricA * fricA + fricB * fricB);
//			
//			Vector2 frictionImpulse;
//			if (Math.abs(jt) < j * staticFriction) {
//				frictionImpulse = a2bDir.getNormal().linearMutliply(jt);
//				
//			} else {
//				fricA = propA.getDynamicFriction();
//				fricB = propB.getDynamicFriction();
//				float dynamicFriction = (float)Math.abs(fricA * fricA + fricB * fricB);
//				frictionImpulse = a2bDir.getNormal().linearMutliply(-jt * dynamicFriction);
//			}
//			
//			a.applyForce(frictionImpulse, ra);
//			b.applyForce(frictionImpulse.linearMutliply(-1f), rb);
			
		}
	}
	
	/**
	 * Counter acts object sinking due to floating point errors
	 * @param cm
	 */
	private void positionCorrection(CollisionManifold cm) {
		BasicPhysicsEntity a = cm.getEntityA();
		BasicPhysicsEntity b = cm.getEntityB();
		
		PhysicsProperties propA = a.getProperties();
		PhysicsProperties propB = b.getProperties();
		
		Vector2 a2bDir = b.getPosition().clone().minus(a.getPosition()).Normalize();
		
		float correctionMult = max(cm.getPenDepth() - CORRECTION_SLOP, 0.0f);
		correctionMult /= propA.getInvMass() + propB.getInvMass();
		correctionMult *= CORRECTION_PERCENT;
		
		Vector2 correctionVel = a2bDir.linearMutliply(correctionMult);
		
		a.applyForce(correctionVel.clone().linearMutliply(propA.getInvMass()));
		b.applyForce(correctionVel.linearMutliply(-1f * propB.getInvMass()));
	}
	
	/**
	 * Check if two shapes are overlapping / clipping / colliding 
	 * @param entA
	 * @param entB
	 * @return
	 */
	public boolean checkCollision(Shape a, Shape b) {
		boolean collided = false;
		// circle vs circle
		if (a.getShapeType() == CircleShape.TYPE_ID && b.getShapeType() == CircleShape.TYPE_ID) {
			collided = radiusCheck((CircleShape)a, (CircleShape)b);
		}
		
		// rect vs rect
		else if (a.getShapeType() == RectShape.TYPE_ID && b.getShapeType() == RectShape.TYPE_ID) {
			collided = axisProjectionCheck((RectShape)a, (RectShape)b);
		}
		
		// circle vs other shapes
		else if (a.getShapeType() == CircleShape.TYPE_ID) {
			collided = outerVertexCheck((CircleShape)a, b);
		}
		
		else if (b.getShapeType() == CircleShape.TYPE_ID) {
			collided = outerVertexCheck((CircleShape)b, a);
		}
		
		// colour
		if (collided) {
			a.setColour(Color.RED);
			b.setColour(Color.RED);
		} else {
			a.setColour(Color.BLUE);
			b.setColour(Color.BLUE);
		}
		
		return collided;
	}
	
	/**
	 * projection based collision checking
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean axisProjectionCheck(RectShape a, RectShape b) {
		Vector2[] axisA = a.getFaceNormals();
		Vector2[] axisB = b.getFaceNormals();
		
		for (int i = 0; i < axisA.length; i++) {
			Projection projA = a.projectOnAxis(axisA[i]);
			Projection projB = b.projectOnAxis(axisA[i]);
			if (!Projection.overlap(projA, projB)) {
				return false;
			}
		}
		
		for (int i = 0; i < axisB.length; i++) {
			Projection projA = a.projectOnAxis(axisB[i]);
			Projection projB = b.projectOnAxis(axisB[i]);
			if (!Projection.overlap(projA, projB)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * radius + position based collision checking
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean radiusCheck(CircleShape a, CircleShape b) {
		Vector2 dist = a.getPosition().clone().minus(b.getPosition());
		float distSq = dist.getLengthSquared();
		float radABSq = a.getRadius() + b.getRadius();
		radABSq *= radABSq;
		return distSq < radABSq;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean outerVertexCheck(CircleShape a, Shape b) {
		Vector2 vertB = b.getVertice(a);
		return a.pointWithin(vertB);
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
