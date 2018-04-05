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

	public static final int START_WIDTH = 512;
	public static final int START_HEIGHT = 512;
	public static final float NANO_SEC = 1000000000f;
	public static final double TARGET_FPS = 60;
	public static final double UPDATE_RATE = 30;
	public static final double FRAME_DISPLAY_RATE = 10;
	public static final String APP_NAME = "ozma engine";

	private float updateDelta, frameDelta;
	private long lastFrame, lastUpdate;
	private List<Entity> entities;
	private Entity selected;
	private PhysicsEngine phyEngine;

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
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
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

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if (selected != null) {
					selected.setPosition(new Vector2((float) e.getX(), (float) e.getY()));
					selected.setVelocity(new Vector2(0, 0));
				}
				
			}

		});
	}

	private void initEntities() {
		entities = new ArrayList<>();

		Random rand = new Random();

		 for (int i = 0; i < 2; i++) {
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

		phyEngine.update();
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
		gc.clearRect(0, 0, 512, 512);

		for (Entity e : entities) {
			e.render(gc, delta);
		}

	}
}
