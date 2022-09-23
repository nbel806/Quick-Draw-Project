package nz.ac.auckland.se206;

import java.io.IOException;
import java.text.DecimalFormat;

import com.opencsv.exceptions.CsvException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class ProfilePageController {
	@FXML
	private Button backButton;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label winLabel;
	@FXML
	private Label gameLabel;
	@FXML
	private Label winrateLabel;
	@FXML
	private Label fastestLabel;
	@FXML
	private TextArea historyWordsText;
	@FXML
	private Label textToSpeechLabel;
	@FXML
	private ImageView volumeImage;
	@FXML
	private ListView<String> historyListView;

	private Boolean textToSpeech;
	private TextToSpeechBackground textToSpeechBackground;
	private String currentUsername;
	private String currentWord;
	private int usersLosses;
	private int usersWins;
	private int totalGames;
	private int fastestTime;
	private double winRate;
	DecimalFormat df = new DecimalFormat("#.#");
	String[] historyWords;

	public void initialize() {

	}

	public void onHoverTextToSpeechLabel() {
		textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
	}

	public void onHoverTextToSpeech() {
		textToSpeechBackground.backgroundSpeak("On", textToSpeech);
		volumeImage.setFitHeight(48);
		volumeImage.setFitWidth(48);
	}

	public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
		this.textToSpeech = textToSpeech;
		this.textToSpeechBackground = (textToSpeechBackground);
		if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
			textToSpeechLabel.setText("ON");
		}
	}

	public void setUsername(String username) throws IOException, CsvException {
		// Check if username is not null
		if (username != null) {
			// If not null, update label as current username
			currentUsername = username;
			this.usernameLabel.setText(currentUsername);
			CSVReaderWriter csvReaderWriter = new CSVReaderWriter();

			// Assign wins
			usersWins = csvReaderWriter.getWins(currentUsername);
			usersLosses = csvReaderWriter.getLosses(currentUsername);
			fastestTime = csvReaderWriter.getFastest(currentUsername);
			historyWords = csvReaderWriter.getHistory(currentUsername).split(",");
			currentWord = csvReaderWriter.getHistory(currentUsername);

			// Calculate games
			totalGames = usersWins + usersLosses;

			if (totalGames > 0) {
				winRate = (((double) usersWins * 100) / (double) totalGames);
			}

			// Update Labels
			winLabel.setText(Integer.toString(usersWins));
			gameLabel.setText(Integer.toString(totalGames));
			winrateLabel.setText(df.format(winRate) + "%");

			if (fastestTime == 100) { // value will be 100 by default eg they must play a game
				fastestLabel.setText("-");
			} else {
				fastestLabel.setText(Integer.toString(fastestTime) + " seconds");
			}

			// Add current word to history of words
			if (!currentWord.equals("none")) {
				historyListView.getItems().addAll(historyWords);
			}

		} else {
			// If user is not signed in
			this.usernameLabel.setText("Guest");
			winLabel.setText("-");
			fastestLabel.setText("-");
			gameLabel.setText("-");
			winrateLabel.setText("-");

		}

	}

	@FXML
	private void onBack() throws IOException {
		Stage stage = (Stage) backButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
																							// menu page
		Scene scene = new Scene(loader.load(), 1000, 680);
		MainMenuController ctrl = loader.getController(); // need controller to pass information
		// may need to add code to pass though tts here
		ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
		ctrl.getUsername(currentUsername);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private void onTextToSpeech() {
		textToSpeech = !textToSpeech; // inverts boolean of text to speech
		if (textToSpeech) { // sets label accordingly
			textToSpeechLabel.setText("ON");
		} else {
			textToSpeechLabel.setText("OFF");
		}
	}

	// Below is list of methods for when mouse hovers a button
	@FXML
	private void onHoverBack() {
		textToSpeechBackground.backgroundSpeak("Back Button", textToSpeech);
		backButton.setStyle(
				"-fx-background-radius: 100px; -fx-text-fill: white; -fx-border-radius: 100px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
	}

	// Below is list of methods for when mouse exits a button
	@FXML
	private void onBackExit() {
		backButton.setStyle(
				"-fx-background-radius: 100px; -fx-text-fill: white; -fx-background-color: #EB4A5A; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 100px;");
	}

	@FXML
	private void onVolumeExit() {
		volumeImage.setFitHeight(45);
		volumeImage.setFitWidth(45);
	}
}
