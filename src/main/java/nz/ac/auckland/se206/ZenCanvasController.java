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
	
    @FXML private Label wordLabel;
	
	@FXML private Canvas zenCanvas;
	
	private DoodlePrediction model;
	private GraphicsContext graphic;
	
	private double currentX;
	private double currentY;
	
	private boolean blackPen = true;
	private boolean redPen = false;
	private boolean orangePen = false;
	private boolean bluePen = false;
	private boolean greenPen = false;
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
	public void onRedPen() {
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

}
