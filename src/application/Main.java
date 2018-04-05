package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.entity.CircleEntity;
import application.entity.Entity;
import application.math.Vector2;
import application.physics.PhysicsEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static final int START_WIDTH = 1280;
	public static final int START_HEIGHT = 720;
	public static final float NANO_SEC = 1000000000f;
	public static final double TARGET_FPS = 60;
	public static final double UPDATE_RATE = 30;
	public static final double FRAME_DISPLAY_RATE = 10;
	public static final String APP_NAME = "ozma engine";

	private float updateDelta, frameDelta;
	private long lastFrame, lastUpdate;
	
	private List<Entity> entities;
	private PhysicsEngine phyEngine;
	
	private Entity selected;
	private boolean mouseDown;

	/**
	 * Program entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(APP_NAME);

		lastFrame = System.nanoTime();
		lastUpdate = System.nanoTime();

		initEntities();

		phyEngine = new PhysicsEngine(entities);

		// javafx node stuff
		primaryStage.setResizable(false);
		Group root = new Group();
		Scene theScene = new Scene(root);
		primaryStage.setScene(theScene);
		Canvas canvas = new Canvas(START_WIDTH, START_HEIGHT);
		root.getChildren().add(canvas);

		// use this to draw
		GraphicsContext gc = canvas.getGraphicsContext2D();

		// timeline loop
		Timeline renderLoop = new Timeline();
		renderLoop.setCycleCount(Timeline.INDEFINITE);

		Timeline updateLoop = new Timeline();
		updateLoop.setCycleCount(Timeline.INDEFINITE);

		// render keyframe loop
		KeyFrame kfRender = new KeyFrame(Duration.seconds(1.0 / TARGET_FPS), // target FPS
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						frameDelta = (System.nanoTime() - lastFrame) / NANO_SEC;
						render(gc, frameDelta);
						lastFrame = System.nanoTime();
					}
				});

		// update keyframe loop
		KeyFrame kfUpdate = new KeyFrame(Duration.seconds(1.0 / UPDATE_RATE), // game loop update rate
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent ae) {
						updateDelta = (System.nanoTime() - lastUpdate) / NANO_SEC;
						update(updateDelta);
						lastUpdate = System.nanoTime();

						primaryStage
								.setTitle(String.format("%s %f (%f)", APP_NAME, (1f / frameDelta), (1f / updateDelta)));
					}
				});

		// start game loop
		updateLoop.getKeyFrames().add(kfUpdate);
		renderLoop.getKeyFrames().add(kfRender);

		renderLoop.play();
		updateLoop.play();

		// mouse input handler
		initDragListener(theScene);

		// show window
		primaryStage.show();
	}

	private void initDragListener(Scene scene) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent e) {
				if (mouseDown) {
					return;
				}
				
				mouseDown = true;
				
				Vector2 mouseVector = new Vector2((float) e.getX(), (float) e.getY());

				for (Entity ent : entities) {
					
					CircleEntity circle = (CircleEntity) ent;
					if (circle.checkCollision(mouseVector)) {
						selected = circle;
						return;
					}
				}

				selected = null;
			}
		});
		
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				mouseDown = false;
				selected = null;
				
			}
		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if (selected != null) {
					Vector2 mouseVector = new Vector2((float) e.getX(), (float) e.getY());
					Vector2 mouseVelocity = mouseVector.clone();
					mouseVelocity.minus(selected.getPosition());
					mouseVelocity.linearMutliply(10f);
					selected.setPosition(mouseVector);
					selected.setVelocity(mouseVelocity);
				}
				
			}

		});
	}

	private void initEntities() {
		entities = new ArrayList<>();

		Random rand = new Random();

		 for (int i = 0; i < 300; i++) {
		 entities.add(new CircleEntity(new Vector2(rand.nextFloat() * START_WIDTH,
		 rand.nextFloat() * START_HEIGHT), rand.nextFloat() * 100f));
		 }

//		entities.add(new CircleEntity(new Vector2(START_WIDTH / 2f, START_HEIGHT / 2f), 50f));
	}

	/**
	 * Update loop for updating data of objects
	 * 
	 * @param delta
	 *            milliseconds between last update
	 */
	public void update(float delta) {
		for (Entity e : entities) {
			e.update(delta);
		}

//		phyEngine.update();
		phyEngine.updateParallel();
	}

	/**
	 * rendering loop for objects
	 * 
	 * @param gc
	 * @param delta
	 *            milliseconds between last render
	 */
	public void render(GraphicsContext gc, float delta) {
		// Clear the canvas
		gc.clearRect(0, 0, START_WIDTH, START_HEIGHT);

		for (Entity e : entities) {
			e.render(gc, delta);
		}

	}
}
