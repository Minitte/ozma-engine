package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleShape extends Shape {

	public static final int TYPE_ID = 1;
	
	private static final int DEGREE = 4;
	
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
		calulateNormals();
	}
	
	/**
	 * Calculates the vertices based on center position and radius
	 */
	private void calculateVertices()  {
		vertices = new Vector2[DEGREE];

		double radInc = (Math.PI * 2.0) / DEGREE;

		double rad = 0;
		for (int i = 0 ; i < vertices.length; i++) {

			vertices[i] = new Vector2((float)Math.cos(rad), (float)Math.sin(rad)).linearMutliply(radius).add(position);

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
		gc.setStroke(colour);
		gc.strokeOval(position.getX() - radius, position.getY() - radius, boxRadius, boxRadius);
		
		for (int i = 0; i < faceNormals.length; i++) {
			Vector2 v = position.clone().add(faceNormals[i].clone().linearMutliply(5f));
			gc.strokeLine(position.getX(), position.getY(), v.getX(), v.getY());
			gc.fillOval(vertices[i].getX() - 2f, vertices[i].getY() - 2f, 4f, 4f);
		}
		
		colour = Color.BLACK;
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
		position = dest;
		calculateVertices();
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getVertice(application.math.Vector2)
	 */
	@Override
	public Vector2 getVertice(Vector2 direction) {
		return direction.clone().Normalize().linearMutliply(radius).add(position);
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getVertice(application.physics.shape.Shape)
	 */
	@Override
	public Vector2 getVertice(Shape pos) {
		return getVertice(pos.getPosition().clone().minus(position).Normalize());
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getFaceNormal(application.math.Vector2)
	 */
	@Override
	public Vector2 getFaceNormalTowards(Vector2 dir) {
		return dir.clone().Normalize();
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
