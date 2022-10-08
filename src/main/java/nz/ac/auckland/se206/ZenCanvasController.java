package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import com.opencsv.exceptions.CsvException;
import ai.djl.ModelException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nz.ac.auckland.se206.ml.DoodlePrediction;

public class ZenCanvasController {

	@FXML private Button backButton;
	@FXML private Button redButton;
	@FXML private Button blackPenButton;
	@FXML private Button eraserButton;
	@FXML private Button clearButton;
	
    @FXML private Label wordLabel;
	
	@FXML private Canvas zenCanvas;
	
	private DoodlePrediction model;
	private GraphicsContext graphic;
	
	private double currentX;
	private double currentY;
	private boolean pen = true;
	private boolean startedDrawing;

	public void initialize() throws ModelException, IOException, CsvException, URISyntaxException {

		graphic = zenCanvas.getGraphicsContext2D();
		model = new DoodlePrediction();
		setTool();
	}
	
	
	private void setTool() {

	    // save coordinates when mouse is pressed on the canvas
	    zenCanvas.setOnMousePressed(
	        e -> {
	          currentX = e.getX();
	          currentY = e.getY();
	          
	        });

	    zenCanvas.setOnMouseDragged(
	        e -> {
	          // Brush size (you can change this, it should not be too small or too large).
	          final double size = 10;

	          final double x = e.getX() - size / 2;
	          final double y = e.getY() - size / 2;

	          // This is the colour of the brush.
	          if (pen) {
	            graphic.setFill(Color.BLACK);
	            graphic.setLineWidth(size);
	            graphic.strokeLine(
	                currentX, currentY, x, y); // Create a line that goes from the point (currentX,
	            // currentY) and (x,y)
	          } else { // eraser
	            graphic.setFill(Color.TRANSPARENT); // sets colour so that black won't be there
	            graphic.clearRect(
	                e.getX() - 10,
	                e.getY() - 10,
	                16,
	                16); // then will clear a rectangle of 5 either side
	            // of the pixel the user is on
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

	}

	@FXML
	public void onRedPen() {

	}

	@FXML
	public void onEraser() {

	}

	@FXML
	public void onClear() {

	}

	public void setWordLabel(String word) {
		wordLabel.setText(word);

	}

}
