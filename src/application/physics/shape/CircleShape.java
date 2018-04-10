package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class CircleShape extends Shape {

	public static final int TYPE_ID = 1;
	
	private static final int DEGREE = 16;
	
	private float radius;
	private float boxRadius;

	/**
	 * @param position
	 * @param angle
	 * @param radius
	 */
	public CircleShape(Vector2 position, float angle, float radius) {
		super(position, angle, TYPE_ID);
		this.radius = radius;
		boxRadius = radius * 2;
		
		calculateVertices();
	}
	
	/**
	 * Calculates the vertices based on center position and radius
	 */
	private void calculateVertices()  {
		vertices = new Vector2[DEGREE];

		double radInc = (Math.PI * 2.0) / DEGREE;

		double rad = 0;
		for (int i = 0 ; i < vertices.length; i++) {

			vertices[i] = new Vector2((float)Math.cos(rad), (float)Math.sin(rad));

			rad += radInc;

		}
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getLooseCheckRadius()
	 */
	@Override
	public float getLooseCheckRadius() {
		return radius;
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
	public void moveTo(Vector2 dest) {
		Vector2 diff = dest.clone();
		diff.minus(position);

		for (int i = 0 ; i < vertices.length; i++) {
			vertices[i].add(diff);
		}
		
		position = dest;
		//calculateVertices();
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getVertice(application.math.Vector2)
	 */
	@Override
	public Vector2 getVertice(Vector2 direction) {
		return direction.clone().Normalize().linearMutliply(radius);
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
