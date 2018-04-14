package application.math;

public class Projection {
	private float min;
	private float max;

	/**
	 * @param min
	 * @param max
	 */
	public Projection(float min, float max) {
		super();
		this.min = min;
		this.max = max;
	}
	
	public static boolean overlap(Projection p1, Projection p2) {
		if (p2.min < p1.max && p1.max < p2.max) {
			return true;
		}
		
		if (p2.min < p1.min && p1.min < p2.max) {
			return true;
		}
		
		return false;
	}

	/**
	 * Stores the min as x and max as y in a vector2
	 * 
	 * @return
	 */
	public Vector2 toVector2() {
		return new Vector2(min, max);
	}

	/**
	 * @return the min
	 */
	public float getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(float min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public float getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(float max) {
		this.max = max;
	}

}
