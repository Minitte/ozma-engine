package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class RectShape extends Shape {

	public static final int TYPE_ID = 2;
	
	private float width;
	private float height;
	private float looseCheckRadius;
	
	/**
	 * @param position
	 * @param angle
	 * @param width
	 * @param height
	 */
	public RectShape(Vector2 position, float angle, float width, float height) {
		super(position, angle, TYPE_ID);
		this.width = width;
		this.height = height;
		looseCheckRadius = (float)Math.sqrt((width * width) + (height * height)) / 2f;
		
		calculateVertices();
		calulateNormals();
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeRect(vertices[0].getX(), vertices[0].getY(), width, height);
		
		for (int i = 0; i < faceNormals.length; i++) {
			Vector2 v = position.clone().add(faceNormals[i].clone().linearMutliply(5f));
			gc.strokeLine(position.getX(), position.getY(), v.getX(), v.getY());
		}

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
		position = dest;
		calculateVertices();
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getVertice(application.math.Vector2)
	 */
	@Override
	public Vector2 getVertice(Vector2 direction) {
		Vector2 best = null;
		float min = Float.MAX_VALUE;
		Vector2 nTarget = direction.clone().linearMutliply(width + height);
		
		for (int i = 0; i < vertices.length; i++) {
			Vector2 diff = nTarget.clone().minus(vertices[i]);
			float dist = diff.getLengthSquared();
			
			if (dist < min) {
				best = vertices[i];
				min = dist;
			}
		}
		
		return best;
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getVertice(application.physics.shape.Shape)
	 */
	@Override
	public Vector2 getVertice(Shape pos) {
		return getVertice(pos.getPosition().clone().minus(position).Normalize());
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#getLooseCheckRadius()
	 */
	@Override
	public float getLooseCheckRadius() {
		return looseCheckRadius;
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
