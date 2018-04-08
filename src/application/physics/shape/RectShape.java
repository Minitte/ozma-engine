package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class RectShape extends Shape {

	private static final int NUM_SIDES = 4;
	
	private float width;
	private float height;
	private Vector2 pointA;
	private Vector2 pointB;

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
		
		updatePoints();
		
		initVertices();
		initFaceNormals();
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#initVertices()
	 */
	@Override
	protected void initVertices() {
		vertices = new Vector2[NUM_SIDES];
		
		vertices[0] = pointA; // top left
		vertices[1] = new Vector2(pointB.getX(), pointA.getY()); // top right
		vertices[2] = pointB; // bottom right
		vertices[3] = new Vector2(pointA.getX(), pointB.getY()); // bottom left
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
//		gc.strokeRect(pointA.getX(), pointA.getY(), width, height);
		double[][] vert = verticesToDoubleArr();
		gc.strokePolygon(vert[0], vert[1], NUM_SIDES);

	}
	
	@Override
	public boolean pointWithin(Vector2 point) {
		if (pointA.getX() > point.getX() || point.getX() > pointB.getX()) {
			return false;
		}
		
		if (pointA.getY() > point.getY() || point.getY() > pointB.getY()) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public Vector2 GetSupport(Vector2 dir) {
		Vector2 best = null;
		float bestDistance = Float.MIN_VALUE;
		
		for (int i = 0; i < vertices.length; i++) {
			float d = vertices[i].dot(dir);
			d = d > 0 ? d : -d;
			
			if (d > bestDistance) {
				d = bestDistance;
				best = vertices[i];
			}
		}
		
		return best;
	}
	
	private void updatePoints() {
		pointA = new Vector2(position.getX() - (width / 2f), position.getY() - (height / 2f));
		pointB = new Vector2(position.getX() + (width / 2f), position.getY() + (height / 2f));
	}
	
	/* (non-Javadoc)
	 * @see application.physics.shape.Shape#moveTo(application.math.Vector2)
	 */
	@Override
	public void moveTo(Vector2 pos) {
		updatePoints();
		super.moveTo(pos);
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
	}

	/**
	 * @return the pointA
	 */
	public Vector2 getPointA() {
		return pointA;
	}

	/**
	 * @return the pointB
	 */
	public Vector2 getPointB() {
		return pointB;
	}

	
}
