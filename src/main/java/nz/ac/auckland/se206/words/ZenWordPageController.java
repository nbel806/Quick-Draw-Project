package nz.ac.auckland.se206.words;

import java.io.IOException;
import java.net.URISyntaxException;

import com.opencsv.exceptions.CsvException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.ZenCanvasController;

public class ZenWordPageController {

	@FXML
	private Label wordLabel;
	@FXML
	private Button readyButton;
	@FXML
	private Button newWordButton;

	private String currentWord;

	
	
	public void initialize() throws IOException, URISyntaxException, CsvException {
		setWordToDraw();
	}
	
	
	@FXML
	public void onZenCanvas() throws IOException {

		Stage stage = (Stage) readyButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/zen_canvas.fxml")); // creates a new instance of
																							// zen canvas
		Scene scene = new Scene(loader.load(), 1000, 680);
		stage.setScene(scene);
		stage.show();
		ZenCanvasController ctrl = loader.getController();
		ctrl.setWordLabel(currentWord);
	}

	public void setWordToDraw() throws IOException, URISyntaxException, CsvException {
		CategorySelector categorySelector = new CategorySelector(); // picks random easy word
		currentWord = categorySelector.getRandomCategory(CategorySelector.Difficulty.E);
		wordLabel.setText(currentWord);
	}
	
	public void onNewWord() throws IOException, URISyntaxException, CsvException {
		setWordToDraw();
	}
	

}
