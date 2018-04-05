/**
 * 
 */
package application.physics;

/**
 * @author Davis
 *
 */
public class PhysicsProperties {
	private float mass;
	private float restitution;

	/**
	 * @param mass
	 * @param restitutionl
	 */
	public PhysicsProperties(float mass, float restitutionl) {
		super();
		this.mass = mass;
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

}
