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
	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.strokeRect(position.getX() - (width * 0.5f), position.getY() - (height * 0.5f), width, height);

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

}
