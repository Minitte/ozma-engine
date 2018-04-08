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
	 * List of all the vertices of the shape
	 */
	protected Vector2[] vertices;
	
	/**
	 * List of all of the normals for each face
	 */
	protected Vector2[] faceNormals;

	/**
	 * @param position
	 * @param angle
	 */
	public Shape(Vector2 position, float angle) {
		super();
		this.position = position;
		this.angle = angle;
	}
	
	/**
	 * initalizes all of the vertices of the shape
	 */
	protected abstract void initVertices();
	
	/**
	 * initalizes all of the faces of the shape;
	 */
	protected void initFaceNormals() {
		faceNormals = new Vector2[vertices.length];
		
		for (int i = 0 ; i < faceNormals.length; i++) {
			Vector2 v = vertices[(i + 1) % vertices.length].clone();
			v.minus(vertices[i]);
			faceNormals[i] = v.getNormal();
			faceNormals[i].Normalize();
		}
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
	 * @param point
	 * @return
	 */
	public abstract boolean pointWithin(Vector2 point);
	
	/**
	 * Gets the vertex that is farthest away in the given direction
	 * @param dir a unit vector 
	 * @return
	 */
	public abstract Vector2 GetSupport(Vector2 dir);
	
	public double[][] verticesToDoubleArr() {
		double[][] vert = new double[2][vertices.length];
		
		for (int i = 0; i < vertices.length; i++) {
			vert[0][i] = vertices[i].getX();
			vert[1][i] = vertices[i].getY();
		}
		
		return vert;
	}
	
	/**
	 * @return the position
	 */
	public Vector2 getPosition() {
		return position;
	}

	/**
	 * Moves the shape and it's vertices to another location
	 * @param pos
	 */
	public void moveTo(Vector2 pos) {
		Vector2 diff = pos.clone();
		diff.minus(position);
		
		position = pos;
		
		initVertices();
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
	 * @param vertices the vertices to set
	 */
	public void setVertices(Vector2[] vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the faceNormals
	 */
	public Vector2[] getFaceNormals() {
		return faceNormals;
	}

	/**
	 * @param faceNormals the faceNormals to set
	 */
	public void setFaceNormals(Vector2[] faceNormals) {
		this.faceNormals = faceNormals;
	}
	
	
	
	

}
