package application.math;

public class Mat22 {
	private float[][] values;

	/**
	 * default all zeros 2x2 matrix
	 */
	public Mat22() {
		values = new float[2][2];
	}

	/**
	 * Rotation matrix
	 * 
	 * @param radians
	 */
	public Mat22(float radians) {
		float cos = (float) Math.cos((double) radians);
		float sin = (float) Math.sin((double) radians);

		values = new float[2][2];
		values[0][0] = cos;
		values[0][1] = -sin;
		values[1][0] = cos;
		values[1][1] = sin;
	}

	/**
	 * Sets a value in the x y spot
	 * 
	 * @param value
	 * @param x
	 *            zero based
	 * @param y
	 *            zero based
	 */
	public void setValue(float value, int x, int y) {
		values[x][y] = value;
	}

	/**
	 * Gets a row and stores it in a new vector
	 * 
	 * @param i
	 *            zero based
	 * @return
	 */
	public Vector2 getRow(int i) {
		return new Vector2(values[0][i], values[1][i]);
	}

	/**
	 * Gets a column and stores it in a new vector
	 * 
	 * @param i
	 *            zero based
	 * @return
	 */
	public Vector2 getCol(int i) {
		return new Vector2(values[i][0], values[i][1]);
	}

	/**
	 * @return the values
	 */
	public float[][] getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(float[][] values) {
		this.values = values;
	}

}
