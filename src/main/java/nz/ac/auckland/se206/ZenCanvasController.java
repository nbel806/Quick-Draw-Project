package nz.ac.auckland.se206;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.ml.DoodlePrediction;

public class ZenCanvasController {

	@FXML private Button backButton;
	@FXML private Button onSaveButton;
	
    @FXML private Label wordLabel;
    @FXML private Label topTenLabel;
	
	@FXML private Canvas zenCanvas;
	
	@FXML private ImageView backImage;
	@FXML private ImageView blackPenImage;
	@FXML private ImageView redPenImage;
	@FXML private ImageView orangePenImage;
	@FXML private ImageView bluePenImage;
	@FXML private ImageView greenPenImage;
	@FXML private ImageView eraserImage;
	@FXML private ImageView clearImage;
	@FXML private ImageView saveImage;
	
	//@FXML private Image redNinja = ;
	
	private DoodlePrediction model;
	private GraphicsContext graphic;
	
	private boolean blackPen = true;
	private boolean redPen = false;
	private boolean orangePen = false;
	private boolean bluePen = false;
	private boolean greenPen = false;
	private boolean startedDrawing;
	
	// mouse coordinates for drawings
	private double currentX;
	private double currentY;
	
	
	public void initialize() throws ModelException, IOException {

		graphic = zenCanvas.getGraphicsContext2D();
		setTool();
		model = new DoodlePrediction();
	}
	
	private void setTool() {

	    // save coordinates when mouse is pressed on the canvas
	    zenCanvas.setOnMousePressed(
	        e -> {
	          currentX = e.getX();
	          currentY = e.getY();
	          
	          if (!startedDrawing) {
	              startedDrawing = true;
	              doPredictions();
	            }
	          
	        });

	    zenCanvas.setOnMouseDragged(
	        e -> {
	          // Brush size (you can change this, it should not be too small or too large).
	          final double size = 10;

	          final double x = e.getX() - size / 2;
	          final double y = e.getY() - size / 2;

	          // Default color: black
	          if (blackPen) {
	        	graphic.setFill(Color.BLACK);
	            graphic.fillOval(x, y, size, size);
	          }else if (redPen) {
	        	graphic.setFill(Color.RED);
	            graphic.fillOval(x, y, size, size);
	          }else if (orangePen) {
	        	graphic.setFill(Color.ORANGE);
		        graphic.fillOval(x, y, size, size);
	          }else if (bluePen) {
	        	graphic.setFill(Color.BLUE);
		        graphic.fillOval(x, y, size, size);
	          }else if (greenPen) {
	        	graphic.setFill(Color.GREEN);
		        graphic.fillOval(x, y, size, size);
	          }else {
	        	graphic.clearRect(x, y, size, size);
	          }
	          
	          
	          // update the coordinates
	          currentX = x;
	          currentY = y;
	        });
	  }
	
	@FXML
	public void onMainMenu() throws IOException {

		Stage stage = (Stage) backButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
																							// main menu
		Scene scene = new Scene(loader.load(), 1000, 680);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void onBlackPen() {
		blackPen = true;
		redPen = false;
		orangePen = false;
		bluePen = false;
		greenPen = false;
		setTool();
	}

	@FXML
	public void onRedPen() throws FileNotFoundException {
		blackPen = false;
		redPen = true;
		orangePen = false;
		bluePen = false;
		greenPen = false;
		setTool();
	}
	
	@FXML
	public void onOrangePen() {
		blackPen = false;
		redPen = false;
		orangePen = true;
		bluePen = false;
		greenPen = false;
		setTool();
	}
	
	@FXML
	public void onBluePen() {
		blackPen = false;
		redPen = false;
		orangePen = false;
		bluePen = true;
		greenPen = false;
		setTool();
	}
	
	@FXML
	public void onGreenPen() {
		blackPen = false;
		redPen = false;
		orangePen = false;
		bluePen = false;
		greenPen = true;
		setTool();
	}
	
	@FXML
	public void onEraser() {
		blackPen = false;
		redPen = false;
		orangePen = false;
		bluePen = false;
		greenPen = false;
		setTool();
	}

	@FXML
	public void onClear() {
		graphic.clearRect(0, 0, zenCanvas.getWidth(), zenCanvas.getHeight());
	}

	public void setWordLabel(String word) {
		wordLabel.setText(word);

	}
	
	@FXML
	public void onSave() {
		Stage stage = (Stage) onSaveButton.getScene().getWindow(); // gets the stage from the button
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Image");
	    File file =
	        fileChooser.showSaveDialog(
	            stage); // shows a popup to allow user to choose where to save file
	    if (file != null) {
	      try {
	        ImageIO.write(
	            getCurrentSnapshot(),
	            "bmp",
	            file); // creates a new image to save to the
	        // users location
	      } catch (IOException ex) {
	        System.out.println(ex.getMessage());
	      }
	    }
	}
	
	public BufferedImage getCurrentSnapshot() {
	    final Image snapshot =
	        zenCanvas.snapshot(null, null); // is the current image based on user drawing on the canvas
	    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

	    // Convert into a binary image.
	    final BufferedImage imageBinary =
	        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

	    final Graphics2D graphics = imageBinary.createGraphics();

	    graphics.drawImage(image, 0, 0, null);

	    // To release memory we dispose.
	    graphics.dispose();

	    return imageBinary;
	}
	
	private void doPredictions() {
	    Timeline time = new Timeline();
	    time.setCycleCount(Timeline.INDEFINITE);
	    time.stop();
	    KeyFrame keyFrame =
	        new KeyFrame(
	            Duration.seconds(1), // new keyframe every second so lists will update every
	            // second
	            actionEvent -> {
	              BufferedImage snapshot =
	                  getCurrentSnapshot(); // uses main thread to get a snapshot of users
	              // drawing
	              Task<Void> backgroundTask =
	                  new Task<>() { // will run the rest of the task in the background thread
	                    // to ensure
	                    // user can draw smoothly and no lag
	                    @Override
	                    protected Void call() {
	                      
	                        List<Classification> list;
	                        try {
	                          list =
	                              model.getPredictions(
	                                  snapshot, 10); // uses the model to get predictions
	                          // based on current user
	                          // drawing
	                        } catch (TranslateException e) {
	                          throw new RuntimeException(e);
	                        }
	                        Platform.runLater(
	                            () -> {
	                              printTopTen(
	                                  list); // will run these methods in the main thread as they deal
	                              // with updating javafx elements
	                        });
	                      
	                      return null;
	                    }
	                  };

	              Thread backgroundThread = new Thread(backgroundTask);
	              backgroundThread
	                  .start(); // all the ML modeling will happen in a background thread to reduce lag
	            });

	    time.getKeyFrames().add(keyFrame);
	    time.playFromStart();
	  }
	
	private void printTopTen(List<Classifications.Classification> list) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(System.lineSeparator());
	    int i = 1;
	    for (Classifications.Classification classification :
	        list) { // cycles through list and build string to print
	      // top 10
	      sb.append(i)
	          .append(" : ")
	          .append(classification.getClassName().replace("_", " ")) // replaces _ with spaces
	          // to ensure a standard
	          // format
	          .append(System.lineSeparator());
	      i++;
	    }
	    topTenLabel.setText(String.valueOf(sb)); // updates label to the new top 10
	}
	
	@FXML
	private void onHoverBack() {
		backImage.setFitHeight(79);
		backImage.setFitWidth(76);
	}
	
	@FXML
	private void onHoverBlack() {
		blackPenImage.setFitHeight(35);
		blackPenImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverRed() {
		redPenImage.setFitHeight(35);
		redPenImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverOrange() {
		orangePenImage.setFitHeight(35);
		orangePenImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverGreen() {
		greenPenImage.setFitHeight(35);
		greenPenImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverBlue() {
		bluePenImage.setFitHeight(35);
		bluePenImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverEraser() {
		eraserImage.setFitHeight(35);
		eraserImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverClear() {
		clearImage.setFitHeight(35);
		clearImage.setFitWidth(35);
	}
	
	@FXML
	private void onHoverSave() {
		saveImage.setFitHeight(35);
		saveImage.setFitWidth(35);
	}
	
	@FXML
	private void onBlackExit() {
		blackPenImage.setFitHeight(32);
		blackPenImage.setFitWidth(32);
	}
	
	@FXML
	private void onRedExit() {
		redPenImage.setFitHeight(32);
		redPenImage.setFitWidth(32);
	}
	
	@FXML
	private void onOrangeExit() {
		orangePenImage.setFitHeight(32);
		orangePenImage.setFitWidth(32);
	}
	
	@FXML
	private void onGreenExit() {
		greenPenImage.setFitHeight(32);
		greenPenImage.setFitWidth(32);
	}
	
	@FXML
	private void onBlueExit() {
		bluePenImage.setFitHeight(32);
		bluePenImage.setFitWidth(32);
	}
	
	@FXML
	private void onEraserExit() {
		eraserImage.setFitHeight(32);
		eraserImage.setFitWidth(32);
	}
	
	@FXML
	private void onClearExit() {
		clearImage.setFitHeight(32);
		clearImage.setFitWidth(32);
	}
	
	@FXML
	private void onSaveExit() {
		saveImage.setFitHeight(32);
		saveImage.setFitWidth(32);
	}
	
	@FXML
	private void onBackExit() {
		backImage.setFitHeight(76);
		backImage.setFitWidth(73);
	}

}
