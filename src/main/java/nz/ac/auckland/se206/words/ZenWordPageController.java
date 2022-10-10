package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ZenCanvasController;

public class ZenWordPageController {
	@FXML private Button readyButton;
	@FXML private Button newWordButton;
	
	@FXML private Text wordLabel;
	
	@FXML private ImageView newImage;

	private String currentWord;

	
	public void initialize() throws IOException, URISyntaxException, CsvException {
		setWordToDraw();
	}
	
	
	@FXML
	public void onZenCanvas() throws IOException {

		Stage stage = (Stage) readyButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/zen_canvas.fxml")); // creates a new instance of
																							// zen canvas
		Scene scene = new Scene(loader.load(), 999, 625);
		stage.setScene(scene);
		stage.show();
		ZenCanvasController ctrl = loader.getController();
		ctrl.setWordLabel("chosen word: " + currentWord);
	}

	public void setWordToDraw() throws IOException, URISyntaxException, CsvException {
		CategorySelector categorySelector = new CategorySelector(); // picks random easy word
		currentWord = categorySelector.getRandomCategory(CategorySelector.Difficulty.E);
		wordLabel.setText(currentWord);
	}
	
	public void onNewWord() throws IOException, URISyntaxException, CsvException {
		setWordToDraw();
	}
	
	@FXML
	private void onHoverNew() {
	   newImage.setFitHeight(57);
	   newImage.setFitWidth(57);
	   newImage.setLayoutX(20);
	}
	
	@FXML
	private void onNewExit() {
	   newImage.setFitHeight(55);
	   newImage.setFitWidth(55);
	}
	
	@FXML
	private void onHoverReady() {
		readyButton.setStyle("-fx-background-radius: 25; -fx-background-color: #99F4B3");
	}
	
	@FXML
	private void onReadyExit() {
		readyButton.setStyle("-fx-background-radius: 50; -fx-background-color: white");
	}

}
