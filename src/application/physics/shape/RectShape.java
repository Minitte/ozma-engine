package application.physics.shape;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class RectShape extends Shape {

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
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeRect(pointA.getX(), pointA.getY(), width, height);

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
		Vector2 best = pointA.clone();
		float bestScalar = best.dot(dir);
		
		Vector2 test;
		float testScalar;
		
		// try top right
		test = new Vector2(pointB.getX(), pointA.getY());
		testScalar = test.dot(dir);
		
		if (testScalar > bestScalar) {
			best = test;
		}
		
		// try bottom left
		test = new Vector2(pointA.getX(), pointB.getY());
		testScalar = test.dot(dir);
		
		if (testScalar > bestScalar) {
			best = test;
		}
				
		// try bottom right (pointB)
		test = pointA.clone();
		testScalar = test.dot(dir);
		
		if (testScalar > bestScalar) {
			best = test;
		}
		
		return best;
	}
	
	private void updatePoints() {
		pointA = new Vector2(position.getX() - (width / 2f), position.getY() - (height / 2f));
		pointB = new Vector2(position.getX() + (width / 2f), position.getY() + (height / 2f));
	}
	
	/* (non-Javadoc)
	 * @see application.entity.Entity#setPosition(application.math.Vector2)
	 */
	@Override
	public void setPosition(Vector2 position) {
		this.position = position;
		updatePoints();
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
		updatePoints();
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
		updatePoints();
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
