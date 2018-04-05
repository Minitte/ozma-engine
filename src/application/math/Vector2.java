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
	 * Creates a new vector2 with the same values
	 */
	public Vector2 clone() {
		return new Vector2(x, y);
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
