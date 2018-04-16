package application;

import application.entity.BasicPhysicsEntity;
import application.entity.Entity;
import application.entity.WaterEntity;
import application.math.Vector2;
import application.physics.PhysicsEngine;
import application.physics.PhysicsProperties;
import application.physics.shape.CircleShape;
import application.physics.shape.RectShape;
import application.physics.shape.Shape;
import application.ui.EntityButton;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

	public static final int START_WIDTH = 1280;
	public static final int START_HEIGHT = 720;
	public static final float NANO_SEC = 1000000000f;
	public static final double TARGET_FPS = 60;
	public static final double UPDATE_RATE = 60;
	public static final double FRAME_DISPLAY_RATE = 10;
	public static final String APP_NAME = "ozma engine";

	public static final int totalSprings = 50;

	private float updateDelta, frameDelta;
	private long lastFrame, lastUpdate;
	
	private List<Entity> entities;
	private List<BasicPhysicsEntity> phyEntities;
	private List<WaterEntity> waterEntities;
	private List<BasicPhysicsEntity> alreadySplashed;
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
		waterEntities = new ArrayList<>();
		alreadySplashed = new ArrayList<>();
		initEntities();
		initWater();

		phyEngine = new PhysicsEngine(phyEntities);

		// javafx node stuff
		primaryStage.setResizable(false);
		Group root = new Group();
		Scene theScene = new Scene(root);
		primaryStage.setScene(theScene);
		Canvas canvas = new Canvas(START_WIDTH, START_HEIGHT);
		root.getChildren().add(canvas);

		// Initialize the user interface
		initUserInterface(root);

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

	/**
	 * Initializes the game's user interface.
	 * @param group the parent root to add the buttons to
	 */
	private void initUserInterface(Group group) {

		// Location of the buttons
		Vector2 circleButtonLocation = new Vector2(20f, 50f);
		Vector2 rectangleButtonLocation = new Vector2(20f, 80f);
		Vector2 clearButtonLocation = new Vector2(20f, 110f);
		Vector2 initializeButtonLocation = new Vector2(20f, 140f);

		// Create the buttons
		EntityButton circleSpawner = new EntityButton(circleButtonLocation, "Spawn circle");
		EntityButton rectangleSpawner = new EntityButton(rectangleButtonLocation, "Spawn rectangle");
		EntityButton clearButton = new EntityButton(clearButtonLocation, "Clear entities");
		EntityButton initializeButton = new EntityButton(initializeButtonLocation, "Initialize entities");

		// Physics properties
		PhysicsProperties prop = new PhysicsProperties(5f, 0.5f);
		prop.setVelocityDamping(1.0f);

		// Onclick listener for the circle spawner button
		circleSpawner.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				Random rand = new Random();

				// Shape position (center)
				Vector2 shapePosition = new Vector2(START_WIDTH / 2, START_HEIGHT / 2);

				//Shape type
				Shape circleShape = new CircleShape(shapePosition, 0f, 50f);

				// Entity for spawning
				BasicPhysicsEntity circle = new BasicPhysicsEntity(shapePosition, circleShape, prop);

				// Apply some random force to offset its position
				circle.setVelocity(new Vector2(rand.nextFloat() * 50, rand.nextFloat() * 50));

				// Add the new entity to the game
				addEntity(circle);
			}
		});

		// Onclick listener for the rectangle spawner button
		rectangleSpawner.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				Random rand = new Random();

				// Shape position (center)
				Vector2 shapePosition = new Vector2(START_WIDTH / 2, START_HEIGHT / 2);

				// Shape type
				Shape rectangleShape = new RectShape(shapePosition, (float)Math.PI / rand.nextFloat(), 50f, 50f);

				// Entities for spawning
				BasicPhysicsEntity rectangle = new BasicPhysicsEntity(shapePosition, rectangleShape, prop);

				// Apply some random force to offset its position
				rectangle.setVelocity(new Vector2(rand.nextFloat() * 50, rand.nextFloat() * 50));

				// Add the new entity to the game
				addEntity(rectangle);
			}
		});

		// Onclick listener for the clear entities button.
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				clearEntities();
			}
		});

		// Onclick listener for the initialize entities button.
		initializeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				initEntities();
			}
		});


		// Add the buttons
		group.getChildren().add(circleSpawner);
		group.getChildren().add(rectangleSpawner);
		group.getChildren().add(clearButton);
		group.getChildren().add(initializeButton);

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
					selected.setVelocity(mouseVelocity);
//					selected.setVelocity(new Vector2(0f, 0f));
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
			 
			 PhysicsProperties prop = new PhysicsProperties(5f, 0.1f);
			 prop.setDynamicFriction(0.2f);
			 prop.setStaticFriction(0.4f);
			 
			 addEntity(new BasicPhysicsEntity(pos, shape, prop));
		 }
		 
		 Vector2 floorPos = new Vector2(START_WIDTH/2f, START_HEIGHT - 50f);
		 Shape floorShape = new RectShape(floorPos, 0f, 50f, 50f);
		 PhysicsProperties floorProp = new PhysicsProperties(0, 0f);
		 floorProp.setDynamicFriction(0f);
		 floorProp.setStaticFriction(0f);
		 BasicPhysicsEntity floorEnt = new BasicPhysicsEntity(floorPos, floorShape, floorProp);
		 floorEnt.setFrozen(true);
		 addEntity(floorEnt);
	}

	/**
	 * Initializes the water entities.
	 */
	private void initWater() {
		for(int n = 0; n <= totalSprings; n++) {

			// Factor over total number of springs to get screen width position
			float t = (float) n / (float) totalSprings;

			// Add the entity
			waterEntities.add( new WaterEntity(new Vector2(t * START_WIDTH, WaterEntity.waterSurface)));
		}
	}

	/**
	 * Updates the water entities.
	 * @param delta time since last frame
	 */
	private void updateWater(float delta) {

		// Call update function on each water entity
		for (WaterEntity we : waterEntities) {
			we.update(delta);
		}

		// For storing the height deltas
		float[] leftDeltas = new float[totalSprings];
		float[] rightDeltas = new float[totalSprings];

		// How fast the waves spread, larger values = faster spread
		float spread = 0.5f;

		// Number of iterations to simulate the splashes, larger values = more smooth
		for(int j = 0; j < 8; j++) {

			// Calculate deltas between each spring and its neighbours
			for(int i = 0; i < totalSprings; i++) {

				if(i > 0)
				{
					leftDeltas[i] = spread * (waterEntities.get(i).getPosition().getY() - waterEntities.get(i - 1).getPosition().getY());
					waterEntities.get(i - 1).speed += leftDeltas[i] * delta;
				}

				if(i < totalSprings - 1) {

					rightDeltas[i] = spread * (waterEntities.get(i).getPosition().getY() - waterEntities.get(i + 1).getPosition().getY());
					waterEntities.get(i + 1).speed += rightDeltas[i] * delta;
				}
			}

			// Set the positions
			for(int i = 0; i < totalSprings; i++) {

				if(i > 0)
					waterEntities.get(i - 1).getPosition().add(new Vector2(0, leftDeltas[i] * delta));
				if(i < totalSprings - 1)
					waterEntities.get(i + 1).getPosition().add(new Vector2(0, rightDeltas[i] * delta));

			}
		}
	}

	/**
	 * Removes all entities from the game.
	 */
	public void clearEntities() {
		entities.clear();
		phyEntities.clear();
		phyEngine.clearEngine();
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

	public void splash(WaterEntity spring, BasicPhysicsEntity entity) {
		spring.speed += entity.getProperties().getMass() * entity.getVelocity().getY() / 50;
	}

	public boolean roughlyEqual(float first, float second, float epsilon) {
		return Math.abs(first - second) < epsilon;
	}


	/**
	 * Update loop for updating data of objects
	 * 
	 * @param delta
	 *            milliseconds between last update
	 */
	public void update(float delta) {

		//phyEngine.update(true);

		for (Entity e : entities) {
			e.update(delta);
		}

		for (BasicPhysicsEntity bpe : phyEntities) {
			if (alreadySplashed.contains(bpe)) {
				continue;
			}
			float xPos = bpe.getPosition().getX();
			float yPos = bpe.getPosition().getY();
			float width = 0, height = 0;

			if (bpe.getShape().getShapeType() == CircleShape.TYPE_ID) {
				width = ((CircleShape)bpe.getShape()).getRadius();
				height = ((CircleShape)bpe.getShape()).getRadius();
			} else if (bpe.getShape().getShapeType() == RectShape.TYPE_ID) {
				width = ((RectShape)bpe.getShape()).getWidth();
				height = ((RectShape)bpe.getShape()).getHeight();
			}

			for (WaterEntity we : waterEntities) {
				if (roughlyEqual(we.getPosition().getX(), xPos, width) && roughlyEqual(we.getPosition().getY(), yPos, height / 2)) {
					splash(we, bpe);
					alreadySplashed.add(bpe);
				}
			}
		}

		updateWater(delta);
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

		renderWater(gc);
	}

	/**
	 * Renders the water.
	 * @param gc the graphics context
	 */
	public void renderWater(GraphicsContext gc) {
		for (int i = 0; i < waterEntities.size() - 1; i++) {
			double[] xPoints = new double[]{(int) waterEntities.get(i).getPosition().getX(), (int) waterEntities.get(i + 1).getPosition().getX(),
					(int) waterEntities.get(i + 1).getPosition().getX(), (int) waterEntities.get(i).getPosition().getX()};
			double[] yPoints = new double[]{(int) waterEntities.get(i).getPosition().getY(), (int) waterEntities.get(i + 1).getPosition().getY(), START_HEIGHT, START_HEIGHT};

			gc.fillPolygon(xPoints, yPoints, 4);
		}
	}
}
