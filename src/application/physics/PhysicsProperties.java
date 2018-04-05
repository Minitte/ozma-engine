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

	/**
	 * @param mass
	 * @param restitutionl
	 */
	public PhysicsProperties(float mass, float restitutionl) {
		super();
		this.mass = mass;
		invMass = 1f / mass;
		this.restitution = restitutionl;
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		return mass;
	}

	/**
	 * @param mass the mass to set
	 */
	public void setMass(float mass) {
		this.mass = mass;
		invMass = 1f / mass;
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
	 * @param restitution the restitution to set
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
	 * @param velocityDamping the velocityDamping to set
	 */
	public void setVelocityDamping(float velocityDamping) {
		this.velocityDamping = velocityDamping;
	}
	
	

}
