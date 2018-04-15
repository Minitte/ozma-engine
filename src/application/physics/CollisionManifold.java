/**
 * 
 */
package application.physics;

import application.entity.BasicPhysicsEntity;
import application.math.Vector2;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;

/**
 * @author Davis
 *
 */
public class CollisionManifold {
	private BasicPhysicsEntity entityA;
	private BasicPhysicsEntity entityB;
	private float penDepth;
	private Vector2 normal;
	private Vector2[] contacts = {new Vector2(), new Vector2()};
	private int numContact;

	/**
	 * @param entityA
	 * @param entityB
	 */
	public CollisionManifold(BasicPhysicsEntity a, BasicPhysicsEntity b) {
		super();
		entityA = a;
		entityB = b;

		normal = calulateNormal();
		
		penDepth = calulateDepth(entityA.getShape(), entityB.getShape());
	}
	
	/**
	 * Calculates the normal
	 */
	private Vector2 calulateNormal() {
		Vector2 v = entityB.getPosition().clone();
		v.minus(entityA.getPosition());
		v.Normalize();
		return v;
	}
	
	/**
	 * Calculates the depth between two shapes
	 * @param shapeA
	 * @param shapeB
	 * @return
	 */
	private float calulateDepth(Shape shapeA, Shape shapeB) {
		// two circles
		if (shapeA instanceof CircleShape && shapeB instanceof CircleShape) {
			return calculateCircleDepth((CircleShape)shapeA, (CircleShape)shapeB);
		} 
		
		// two rectangles
		else if (shapeA instanceof RectShape && shapeB instanceof RectShape) {
			return calculateRectDepth((RectShape)shapeA, (RectShape)shapeB);
		}
		
		// one circle and one rect
		else if (shapeA instanceof CircleShape && shapeB instanceof RectShape) {
			return calculateMixDepth((RectShape)shapeB, (CircleShape)shapeA);
		}
		
		else if (shapeB instanceof CircleShape && shapeA instanceof RectShape) {
			return calculateMixDepth((RectShape)shapeA, (CircleShape)shapeB);
		}
		
		return 0;
		
	}
	
	/**
	 * Calculates the penetration depth between two circles
	 * @param a
	 * @param b
	 * @return
	 */
	private float calculateCircleDepth(CircleShape a, CircleShape b) {
		Vector2 v = b.getPosition().clone();
		v.minus(a.getPosition());
		
		float sum = a.getRadius() + b.getRadius();
		
		contacts[0].copy(normal).linearMutliply(a.getRadius()).add(a.getPosition());
		numContact = 1;
		
		return sum - v.getLength();
	}
	
	/**
	 * Calculates the penetration depth between two rectangles
	 * @param a
	 * @param b
	 * @return
	 */
	private float calculateRectDepth(RectShape a, RectShape b) {
		float xPen = 0;
		float yPen = 0;
		
		if (a.getPointB().getX() > b.getPointA().getX()) {
			xPen = a.getPointB().getX() - b.getPointA().getX();
		} else {
			xPen = a.getPointA().getX() - b.getPointB().getX();
		}
		
		if (a.getPointB().getY() > b.getPointA().getY()) {
			yPen = a.getPointB().getY() - b.getPointA().getX();
		} else {
			yPen = a.getPointA().getY() - b.getPointB().getY();
		}
		
		// absolute value
		xPen = xPen > 0f ? xPen : -xPen;
		yPen = yPen > 0f ? yPen : -yPen;
		
		// contact points
		Vector2 a2bDir = b.getPosition().clone().minus(a.getPosition()).Normalize();
		Vector2 b2aDir = a2bDir.clone().linearMutliply(-1f);
		
		Vector2[] verticesA = a.getVertices(a.getFaceIndexTowards(a2bDir));
		Vector2[] verticesB = b.getVertices(b.getFaceIndexTowards(b2aDir));
		
		// get the best 1 or 2
		float best = -Float.MAX_VALUE;
		float second = -Float.MAX_VALUE;
		
		for (int i = 0; i < verticesA.length; i++) {
			float dotResult = a2bDir.dot(verticesA[i]);
			
			if (dotResult > best) {
				best = dotResult;
				contacts[1] = contacts[0]; // move old first to second
				contacts[0].copy(verticesA[i]);
			} else if (dotResult > second) {
				best = dotResult;
				contacts[1].copy(verticesA[i]);
			}
		}
		
		for (int i = 0; i < verticesB.length; i++) {
			float dotResult = a2bDir.dot(verticesB[i]);
			
			if (dotResult > best) {
				best = dotResult;
				contacts[1] = contacts[0]; // move old first to second
				contacts[0].copy(verticesB[i]);
			} else if (dotResult > second) {
				best = dotResult;
				contacts[1].copy(verticesB[i]);
			}
		}
		
		numContact = 2;
		if (contacts[0].equals(contacts[1])) { // same point!
			numContact = 1;
		}
		
		return xPen > yPen ? yPen : xPen;
	}
	
	/**
	 * Calculates the penetration depth between a rect and circle
	 * @param a
	 * @param b
	 * @return
	 */
	private float calculateMixDepth(RectShape a, CircleShape b) {
		
		float pxA = a.getPointA().getX() - b.getPosition().getX();
		pxA = pxA > 0f ? pxA : -pxA;
		
		float pxB = a.getPointB().getX() - b.getPosition().getX();
		pxB = pxB > 0f ? pxB : -pxB;
		
		float pyA = a.getPointA().getY() - b.getPosition().getY();
		pyA = pyA > 0f ? pyA : -pyA;
		
		float pyB = a.getPointB().getY() - b.getPosition().getY();
		pyB = pyB > 0f ? pyB : -pyB;
		
		float px = pxA < pxB ? a.getPointA().getX() : a.getPointB().getX();
		float py = pyA < pyB ? a.getPointA().getY() : a.getPointB().getY();
		
		Vector2 p = new Vector2(px, py);
		Vector2 n = b.getPosition().clone();
		n.minus(p);
		if (a.pointWithin(b.getPosition())) {
			n.linearMutliply(-1f);
		}
		
		if (b.getRadius() * b.getRadius() < n.getLengthSquared()) {
			return 0f;
		}
		
		float depth = n.getLength();
		
//		n.Normalize();
		
		contacts[0].copy(normal).linearMutliply(b.getRadius()).add(a.getPosition());
		numContact = 1;
		
		normal = a.getFaceNormalTowards(b.getPosition().clone().minus(a.getPosition())).clone().Normalize();
		
		return depth;
	}

	/**
	 * @return the entityA
	 */
	public BasicPhysicsEntity getEntityA() {
		return entityA;
	}

	/**
	 * @return the entityB
	 */
	public BasicPhysicsEntity getEntityB() {
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

	/**
	 * @return the contacts
	 */
	public Vector2[] getContacts() {
		return contacts;
	}

	/**
	 * @return the numContact
	 */
	public int getNumContact() {
		return numContact;
	}
	
	

}
