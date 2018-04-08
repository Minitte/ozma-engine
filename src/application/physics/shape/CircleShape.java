package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class CircleShape extends Shape {

	private static final int DEGREE = 16;
	
	private float radius;
	private float boxRadius;

	/**
	 * @param position
	 * @param angle
	 * @param radius
	 */
	public CircleShape(Vector2 position, float angle, float radius) {
		super(position, angle);
		this.radius = radius;
		boxRadius = radius * 2;
	}
	
	@Override
	protected void initVertices() {
		vertices = new Vector2[DEGREE];
		
		double radInc = (Math.PI * 2.0) / DEGREE;
		double rad = 0;
		
		for (int i = 0 ; i < vertices.length; i++) {
			vertices[i] = new Vector2((float)Math.cos(rad), (float)Math.sin(rad));
			rad += radInc;
		}
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeOval(position.getX() - radius, position.getY() - radius, boxRadius, boxRadius);
	}
	
	@Override
	public boolean pointWithin(Vector2 point) {
		float r = radius;
		r *= r;
		float distX = (position.getX() - point.getX());
		distX *= distX;
		float distY = (position.getY() - point.getY());
		distY *= distY;
		return r > distX + distY;
	}
	
	@Override
	public Vector2 GetSupport(Vector2 dir) {
		Vector2 v = dir.clone();
		v.linearMutliply(radius);
		return v;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 *            the radius to set
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

}
