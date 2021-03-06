/**
 * 
 */
package application.physics;

/**
 * @author Davis
 *
 */
public class PhysicsProperties {
	private float invMass;
	private float mass;
	private float restitution;
	private float velocityDamping;

	// friction
	private float staticFriction;
	private float dynamicFriction;

	/**
	 * @param mass
	 * @param restitutionl
	 */
	public PhysicsProperties(float mass, float restitutionl) {
		super();
		this.mass = mass;
		if (mass != 0) {
			invMass = 1f / mass;
		} else {
			invMass = 0;
		}
		this.restitution = restitutionl;
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * @param mass
	 *            the mass to set
	 */
	public void setMass(float mass) {
		this.mass = mass;
		if (mass != 0) {
			invMass = 1f / mass;
		} else {
			invMass = 0;
		}
	}

	/**
	 * @return the invMass
	 */
	public float getInvMass() {
		return invMass;
	}

	/**
	 * @return the restitution
	 */
	public float getRestitution() {
		return restitution;
	}

	/**
	 * @param restitution
	 *            the restitution to set
	 */
	public void setRestitution(float restitution) {
		this.restitution = restitution;
	}

	/**
	 * @return the velocityDamping
	 */
	public float getVelocityDamping() {
		return velocityDamping;
	}

	/**
	 * @param velocityDamping
	 *            the velocityDamping to set
	 */
	public void setVelocityDamping(float velocityDamping) {
		this.velocityDamping = velocityDamping;
	}

	/**
	 * @return the staticFriction
	 */
	public float getStaticFriction() {
		return staticFriction;
	}

	/**
	 * @param staticFriction
	 *            the staticFriction to set
	 */
	public void setStaticFriction(float staticFriction) {
		this.staticFriction = staticFriction;
	}

	/**
	 * @return the dynamicFriction
	 */
	public float getDynamicFriction() {
		return dynamicFriction;
	}

	/**
	 * @param dynamicFriction
	 *            the dynamicFriction to set
	 */
	public void setDynamicFriction(float dynamicFriction) {
		this.dynamicFriction = dynamicFriction;
	}
}
