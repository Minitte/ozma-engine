package application.math;

public class Vector2 {
	
	private float x, y;

	/**
	 * Default constructor
	 */
	public Vector2() {
		
	}
	
	/**
	 * Constructor
	 * @param x
	 * @param y
	 */
	public Vector2(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Dot product operation between this and the other vector
	 * @param other
	 * @return
	 */
	public float dot(Vector2 other) {
		return (x * other.x) + (y * other.y);
	}

	/**
	 * Adds another vector to this vector
	 * @param other
	 */
	public void add(Vector2 other) {
		x += other.x;
		y += other.y;
	}
	
	/**
	 * Minus another vector to this vector
	 * @param other
	 */
	public void minus(Vector2 other) {
		x -= other.x;
		y -= other.y;
	}
	
	/**
	 * Reduces x and y by the amount of x and y of other. 
	 * The reduction is in the direction of zero if values are positive.
	 * Negatives will have an opposite effect of getting further away from zero
	 * @param other 
	 */
	public void reduce(Vector2 other) {
		if (x > 0) {
			x -= other.x;
		} else if (x < 0) {
			x += other.x;
		}
		
		if (y > 0) {
			y -= other.y;
		} else if (y < 0) {
			y += other.y;
		}
	}
	
	/**
	 * multiplies both x and y with the given scale factor
	 * @param scale
	 * @return
	 */
	public void linearMutliply(float scale) {
		x *= scale;
		y *= scale;
	}
	
	/**
	 * multiplies both x and y with the x and y in other
	 * @param other
	 */
	public void multiply(Vector2 other) {
		x *= other.x;
		y *= other.y;
	}
	
	/**
	 * divdes both x and y by the given denominator
	 * @param denominator
	 */
	public void linearDivide(float denominator) {
		x /= denominator;
		y /= denominator;
	}
	
	/**
	 * divides both x and y by the x and y in other
	 * @param other
	 */
	public void divide(Vector2 other) {
		x /= other.x;
		y /= other.y;
	}
	
	/**
	 * Creates a new vector2 with the same values
	 */
	public Vector2 clone() {
		return new Vector2(x, y);
	}
	
	/**
	 * Creates a new vector representing the normal
	 * @return
	 */
	public Vector2 getNormal() {
		return new Vector2(-y, x);
	}
	
	/**
	 * makes the length to 1
	 * @return
	 */
	public void Normalize() {
		float len = getLength();
		
		linearDivide(len);
	}
	
	/**
	 * the length of the vector by calculating the hypotenuse
	 * @return
	 */
	public float getLength() {
		return (float)Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

}
