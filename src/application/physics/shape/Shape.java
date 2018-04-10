package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public abstract class Shape {
	
	/**
	 * The xy representing the center of the shape
	 */
	protected Vector2 position;

	/**
	 * The angle or rotation of the shape in radians
	 */
	protected float angle;

	/**
	 * List of vertices
	 */
	protected Vector2[] vertices;

	/**
	 * Normals to each face
	 */
	protected Vector2[] faceNormals;
	
	/**
	 * Type id used for fast type checking
	 */
	protected final int shapeType;

	/**
	 * @param position
	 * @param angle
	 */
	public Shape(Vector2 position, float angle, int shapeType) {
		super();
		this.position = position;
		this.angle = angle;
		this.shapeType = shapeType;
	}

	/**
	 * Renders the outline of the shape
	 * 
	 * @param gc
	 * @param delta
	 */
	public abstract void render(GraphicsContext gc, float delta);

	/**
	 * Checks if a point is within shape
	 * 
	 * @param point
	 * @return
	 */
	public abstract boolean pointWithin(Vector2 point);

	/**
	 * Move to the spot
	 * 
	 * @param dest
	 */
	public abstract void moveTo(Vector2 dest);

	/**
	 * Gets the point that is mostly aligned with the direction
	 * 
	 * @param direction
	 *            a vector representing direction
	 */
	public abstract Vector2 getVertice(Vector2 direction);
	
	/**
	 * Gets the vertex that is towards the shape's position
	 * 
	 * @param position towards this shape's position
	 */
	public abstract Vector2 getVertice(Shape pos);

	/**
	 * the radius used for a quick check for potential collisions
	 * 
	 * @return
	 */
	public abstract float getLooseCheckRadius();

	/**
	 * Calulates all of the face normals
	 */
	protected void calulateNormals() {
		faceNormals = new Vector2[vertices.length];

		for (int i = 0; i < faceNormals.length; i++) {

			Vector2 v = vertices[(i + 1) % vertices.length].clone();

			v.minus(vertices[i]);

			faceNormals[i] = v.getNormal();

			faceNormals[i].Normalize();
		}
	}
	
	/**
	 * Finds the face normal closest to the direction
	 * @param dir
	 * @return
	 */
	public Vector2 getFaceNormal(Vector2 dir) {
		Vector2 best = null;
		float max = Float.MIN_VALUE;
		
		for (int i = 0; i < faceNormals.length; i++) { 
			if (dir.dot(faceNormals[i]) > max) {
				best = faceNormals[i];
			}
		}
		
		return best;
	}

	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * @return the vertices
	 */
	public Vector2[] getVertices() {
		return vertices;
	}

	/**
	 * @return the faceNormals
	 */
	public Vector2[] getFaceNormals() {
		return faceNormals;
	}

	/**
	 * @return the shapeType
	 */
	public int getShapeType() {
		return shapeType;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	

}
