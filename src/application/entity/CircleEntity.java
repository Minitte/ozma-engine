package application.entity;

import application.math.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class CircleEntity extends Entity {

	private float radius;
	
	public CircleEntity(Vector2 position, float radius) {
		super(position);
		this.radius = radius;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GraphicsContext gc, float delta) {
		gc.fillOval(position.getX() - radius, position.getY() - radius, radius, radius);
	}

}
