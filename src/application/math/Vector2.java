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
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 add(Vector2 other) {
		x += other.x;
		y += other.y;
		
		return this;
	}
	
	/**
	 * Minus another vector to this vector
	 * @param other
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 minus(Vector2 other) {
		x -= other.x;
		y -= other.y;
		
		return this;
	}
	
	/**
	 * Reduces x and y by the amount of x and y of other. 
	 * The reduction is in the direction of zero if values are positive.
	 * Negatives will have an opposite effect of getting further away from zero
	 * @param other 
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 reduce(Vector2 other) {
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
		
		return this;
	}
	
	/**
	 * multiplies both x and y with the given scale factor
	 * @param scale
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 linearMutliply(float scale) {
		x *= scale;
		y *= scale;
		
		return this;
	}
	
	/**
	 * multiplies both x and y with the x and y in other
	 * @param other
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 multiply(Vector2 other) {
		x *= other.x;
		y *= other.y;
		
		return this;
	}
	
	/**
	 * divdes both x and y by the given denominator
	 * @param denominator
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 linearDivide(float denominator) {
		x /= denominator;
		y /= denominator;
		
		return this;
	}
	
	/**
	 * divides both x and y by the x and y in other
	 * @param other
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 divide(Vector2 other) {
		x /= other.x;
		y /= other.y;
		
		return this;
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
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 Normalize() {
		float len = getLength();
		
		linearDivide(len);
		
		return this;
	}
	
	/**
	 * the length of the vector by calculating the hypotenuse
	 * @return
	 */
	public float getLength() {
		return (float)Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * the length of the vector by calculating the hypotenuse without doing the sqrt step
	 * @return
	 */
	public float getLengthSquared() {
		return (x * x) + (y * y);
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
	
	@Override
	public String toString() {
		return String.format("Vector2[%f, %f]", x, y);
	}

}
