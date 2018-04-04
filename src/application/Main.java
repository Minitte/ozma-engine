package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Main extends Application {
	
	public static final int START_WIDTH = 400;
	public static final int START_HEIGHT = 400;
	public static final int TARGET_FPS = 60;
	public static final int UPDATE_RATE = 60;
	public static final String APP_NAME = "ozma engine";
	
	private long updateDelta, frameDelta, lastFrame, lastUpdate;
	
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

		// javafx node stuff
        Group root = new Group();
        Scene theScene = new Scene( root );
        primaryStage.setScene( theScene );
        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );
        
        // use this to draw
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // timeline loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount( Timeline.INDEFINITE );
        
        // render keyframe loop
        lastFrame = System.currentTimeMillis();
        KeyFrame kfRender = new KeyFrame(
            Duration.seconds(1.0/TARGET_FPS), // target FPS
            new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent ae)
                {
                	frameDelta = System.currentTimeMillis() - lastFrame;
                    render(gc, frameDelta);
                    lastFrame = System.currentTimeMillis();
                }
            });
        
        // update keyframe loop
        lastUpdate = System.currentTimeMillis(); 
        KeyFrame kfUpdate = new KeyFrame(
                Duration.seconds(1.0/UPDATE_RATE), // game loop update rate
                new EventHandler<ActionEvent>()
                {
                    public void handle(ActionEvent ae)
                    {
                    	updateDelta =  - lastUpdate;
                        update(updateDelta);
                    	lastUpdate = System.currentTimeMillis(); 
                    }
                });
        
        // start game loop
        gameLoop.getKeyFrames().add( kfRender );
        gameLoop.getKeyFrames().add( kfUpdate );
        gameLoop.play();
        
        // show window
        primaryStage.show();
	}
	
	/**
	 * Update loop for updating data of objects
	 * @param delta milliseconds between last update
	 */
	public void update(long delta)
	{
		
		
	}
	
	/**
	 * rendering loop for objects
	 * @param gc
	 * @param delta milliseconds between last render
	 */
	public void render(GraphicsContext gc, long delta)
	{
		// Clear the canvas
        gc.clearRect(0, 0, 512,512);
		
		// fill colour
		gc.setFill(Color.RED);
		
		// outline / stroke colour
		gc.setStroke(Color.BLACK);
		
		// outline / stroke width
		gc.setLineWidth(5);
		
		gc.fillRect(10, 10, 30, 30);
	}
}
