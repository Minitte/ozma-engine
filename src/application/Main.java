package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.entity.BasicPhysicsEntity;
import application.entity.Entity;
import application.math.Vector2;
import application.physics.PhysicsEngine;
import application.physics.PhysicsProperties;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;
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
	public static final double UPDATE_RATE = 60;
	public static final double FRAME_DISPLAY_RATE = 10;
	public static final String APP_NAME = "ozma engine";

	private float updateDelta, frameDelta;
	private long lastFrame, lastUpdate;
	
	private List<Entity> entities;
	private List<BasicPhysicsEntity> phyEntities;
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

		entities = new ArrayList<>();
		phyEntities = new ArrayList<>();
		initEntities();

		phyEngine = new PhysicsEngine(phyEntities);

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
					
					if (ent.pointWithin(mouseVector)) {
						selected = ent;
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
//					selected.setVelocity(mouseVelocity);
					selected.setVelocity(new Vector2(0f, 0f));
				}
				
			}

		});
	}

	private void initEntities() {
		Random rand = new Random();
		 
		 for (int i = 0; i < 6; i++) {
			 Vector2 pos = new Vector2(rand.nextFloat() * START_WIDTH, rand.nextFloat() * START_HEIGHT);
			 
			 Shape shape = null;
			 
			 if (i % 2 == 0) {
				 shape = new CircleShape(pos, 0f, 50f);
			 } else {
				 shape = new RectShape(pos, (float)Math.PI / rand.nextFloat(), 50f, 50f);
//				 continue;
			 }
			 
			 PhysicsProperties prop = new PhysicsProperties(5f, 0.5f);
			 prop.setVelocityDamping(1.0f);
			 
			 addEntity(new BasicPhysicsEntity(pos, shape, prop));
		 }
		 
		 Vector2 floorPos = new Vector2(START_WIDTH/2f, START_HEIGHT - 50f);
		 Shape floorShape = new RectShape(floorPos, 0f, 60f, 40f);
		 BasicPhysicsEntity floorEnt = new BasicPhysicsEntity(floorPos, floorShape, new PhysicsProperties(30f, 0.2f));
		 //floorEnt.setFrozen(true);
		 addEntity(floorEnt);
	}
	
	/**
	 * Adds an entity to the correct list
	 * @param e
	 */
	public void addEntity(Entity e) {
		entities.add(e);
		
		if (e instanceof BasicPhysicsEntity) {
			phyEntities.add((BasicPhysicsEntity)e);
		}
	}

	/**
	 * Update loop for updating data of objects
	 * 
	 * @param delta
	 *            milliseconds between last update
	 */
	public void update(float delta) {
		phyEngine.update(true);
		
		for (Entity e : entities) {
			e.update(delta);
		}
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
