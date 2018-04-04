package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.entity.CircleEntity;
import application.entity.Entity;
import application.math.Vector2;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

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
	
	/**
	 * Program entry point
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
		
		// javafx node stuff
        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene( theScene );
        Canvas canvas = new Canvas(START_WIDTH, START_HEIGHT);
        root.getChildren().add( canvas );
        
        // use this to draw
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // timeline loop
        Timeline renderLoop = new Timeline();
        renderLoop.setCycleCount( Timeline.INDEFINITE );
        
        Timeline updateLoop = new Timeline();
        updateLoop.setCycleCount( Timeline.INDEFINITE );
        
        // render keyframe loop
        KeyFrame kfRender = new KeyFrame(
            Duration.seconds(1.0/TARGET_FPS), // target FPS
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                	frameDelta = (System.nanoTime() - lastFrame) / NANO_SEC;
                    render(gc, frameDelta);
                    lastFrame = System.nanoTime();
                }
            });
        
        // update keyframe loop
        KeyFrame kfUpdate = new KeyFrame(
                Duration.seconds(1.0/UPDATE_RATE), // game loop update rate
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                    	updateDelta = (System.nanoTime() - lastUpdate) / NANO_SEC;
                        update(updateDelta);
                    	lastUpdate = System.nanoTime(); 
                    	
                    	primaryStage.setTitle(String.format("%s %f (%f)", APP_NAME, (1f / frameDelta), (1f / updateDelta)));
                    }
                });
        
        // start game loop
        updateLoop.getKeyFrames().add( kfUpdate );
        renderLoop.getKeyFrames().add( kfRender );
        
        renderLoop.play();
        updateLoop.play();
        
        // show window
        primaryStage.show();
	}
	
	private void initEntities() {
		entities = new ArrayList<>();
		
		Random rand = new Random();
		
		for (int i = 0; i < 1000; i++) {
			entities.add(new CircleEntity(new Vector2(rand.nextFloat() * START_WIDTH, rand.nextFloat() * START_HEIGHT), rand.nextFloat() * 100f));
		}
	}
	
	/**
	 * Update loop for updating data of objects
	 * @param delta milliseconds between last update
	 */
	public void update(float delta) {
		for (Entity e : entities) {
			e.update(delta);
		}
	}
	
	/**
	 * rendering loop for objects
	 * @param gc
	 * @param delta milliseconds between last render
	 */
	public void render(GraphicsContext gc, float delta) {
		// Clear the canvas
        gc.clearRect(0, 0, 512,512);
		
		// fill colour
		gc.setFill(Color.BLUE);
		
		// outline / stroke colour
		gc.setStroke(Color.BLACK);
		
		// outline / stroke width
		gc.setLineWidth(6);
		
		for (Entity e : entities) {
			e.render(gc, delta);
		}
		
	}
}
