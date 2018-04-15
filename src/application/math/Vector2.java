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
	 * Cross product between two vector2
	 * @param other
	 * @return
	 */
	public float crossProduct(Vector2 other) {
		return (x * other.y) - (y * other.x);
	}
	
	/**
	 * Crossproduct with a single float, returns a new vector with result
	 * @param s
	 * @return
	 */
	public static Vector2 crossProduct(Vector2 v, float s) {
		return new Vector2(s * v.y, -s * v.x);
	}
	
	/**
	 * Crossproduct with a single float, returns a new vector with result
	 * @param s
	 * @return
	 */
	public static Vector2 crossProduct(float s, Vector2 v) {
		return new Vector2(-s * v.y, s * v.x);
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
	 * multiplies both x and y with the matrix
	 * @param other
	 * @return returns itself to allow chains of operations
	 */
	public Vector2 multiply(Matrix2x2 mat) {
		float[][] m = mat.getValues();
		float xOld = x;
		float yOld = y;
		x = xOld * m[0][0] + yOld * m[0][1];
		y = xOld * m[1][0] + yOld * m[1][1];
		
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
	 * Copies the values from source vector
	 * @param src
	 * @return returns itself to allow chains of operations 
	 */
	public Vector2 copy(Vector2 src) {
		x = src.x;
		y = src.y;
		
		return this;
	}
	
	/**
	 * if both are within 0.0001 of each other..
	 * @param other
	 * @return
	 */
	public boolean equals(Vector2 other) {
		Vector2 diff = clone().minus(other);
		return Math.abs(diff.x) > 0.001f && Math.abs(diff.y) > 0.001f;
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
