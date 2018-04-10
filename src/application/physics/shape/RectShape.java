package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class RectShape extends Shape {

	private float width;
	private float height;
	/**
	 * @param position
	 * @param angle
	 * @param width
	 * @param height
	 */
	public RectShape(Vector2 position, float angle, float width, float height) {
		super(position, angle);
		this.width = width;
		this.height = height;
		
		calculateVertices();
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeRect(vertices[0].getX(), vertices[0].getY(), width, height);

	}
	
	@Override
	public boolean pointWithin(Vector2 point) {
		if (vertices[0].getX() > point.getX() || point.getX() > vertices[2].getX()) {
			return false;
		}
		
		if (vertices[0].getY() > point.getY() || point.getY() > vertices[2].getY()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Calulcates the vertices based on center, width and height
	 */
	private void calculateVertices() {
		
		vertices = new Vector2[4];
		vertices[0] = new Vector2(position.getX() - (width / 2f), position.getY() - (height / 2f));
		vertices[2] = vertices[0].clone();
		vertices[2].add(new Vector2(width, height));
		vertices[1] = new Vector2(vertices[2].getX(), vertices[0].getY());
		vertices[3] = new Vector2(vertices[0].getX(), vertices[2].getY());
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#moveTo(application.math.Vector2)
	 */
	@Override
	public void moveTo(Vector2 dest) {
//		Vector2 diff = dest.clone();
//		diff.minus(position);
//
//		for (int i = 0 ; i < vertices.length; i++) {
//			vertices[i].add(diff);
//		}
		
		position = dest;
		calculateVertices();
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
		calculateVertices();
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
		calculateVertices();
	}

	/**
	 * @return the pointA
	 */
	public Vector2 getPointA() {
		return vertices[0];
	}

	/**
	 * @return the pointB
	 */
	public Vector2 getPointB() {
		return vertices[2];
	}

	
}
