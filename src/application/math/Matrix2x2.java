/**
 * 
 */
package application.math;

/**
 * @author Davis
 *
 */
public class Matrix2x2 {
	private float[][] values;
	
	public Matrix2x2(float radian) {
		values = new float[2][2];
		
		//     x  y
		values[0][0] = (float)Math.cos(radian);
		values[1][0] = -(float)Math.sin(radian);
		values[0][1] = -values[1][0];
		values[1][1] = values[0][0];
	}

	/**
	 * @return the values
	 */
	public float[][] getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(float[][] values) {
		this.values = values;
	}
	
	
}
