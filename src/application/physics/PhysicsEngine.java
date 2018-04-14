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
					resolveCollision(cm.getEntityA(), cm.getEntityB());
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
	private void resolveCollision(BasicPhysicsEntity a, BasicPhysicsEntity b) {
		
//		BasicPhysicsEntity a = cm.getEntityA();
//		BasicPhysicsEntity b = cm.getEntityB();
		
		PhysicsProperties propA = a.getProperties();
		PhysicsProperties propB = b.getProperties();
		
		Vector2 a2bDir = b.getPosition().clone().minus(a.getPosition()).Normalize();
		Vector2 b2aDir = a2bDir.clone().linearMutliply(-1f);
		
		// vector based on the face pointing away from the center of the shape
		Vector2 faceA = a.getShape().getFaceNormalTowards(a2bDir).clone();
		Vector2 faceB = b.getShape().getFaceNormalTowards(b2aDir).clone();
		
		// Calculate impulse amt
		
		// use smaller restitution value
		float e = propA.getRestitution() < propB.getRestitution() ? propA.getRestitution() : propB.getRestitution();
		
		// scalar along normal
		Vector2 rVelocity = b.getVelocity().clone().add(a.getVelocity());
		float dirScalar = rVelocity.dot(a2bDir);
		dirScalar = dirScalar > 0 ? dirScalar : -dirScalar;
		
		// impluse
		float j = (-(1 + e)) * dirScalar;
		j /= propA.getInvMass() + propB.getInvMass();
		
		j = j > 0 ? j : -j;
		
		// apply force
		a.applyForce(faceB.linearMutliply(j * propA.getInvMass()));
		b.applyForce(faceA.linearMutliply(j * propB.getInvMass()));
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
