package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class ProfilePageController {
	@FXML
  private Label fastestGame;
  @FXML
	private Button back;
	@FXML
	private Label username;
	@FXML
	private Label win;
	@FXML
	private Label loss;
	@FXML
	private Label history;
	@FXML
	private TextArea historyWordsText;

	@FXML
	private Label textToSpeechLabel;
	private Boolean textToSpeech;
	private TextToSpeechBackground textToSpeechBackground;
	private String currentUsername;
	private int usersLosses;
	private int usersWins;
	private int fastestTime;
	private String historyWords;

	@FXML
	private void onMainMenu() throws IOException {

		Stage stage = (Stage) back.getScene().getWindow();
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
	
	public void initialize(){
		
	}

	public void onHoverTextToSpeechLabel() {
		textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
	}

	public void onHoverTextToSpeech() {
		textToSpeechBackground.backgroundSpeak("On", textToSpeech);
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
			this.username.setText(currentUsername);
			CSVReaderWriter csvReaderWriter = new CSVReaderWriter();
			usersWins = csvReaderWriter.getWins(currentUsername);
			usersLosses = csvReaderWriter.getLosses(currentUsername);
			fastestTime = csvReaderWriter.getFastest(currentUsername);
			historyWords = csvReaderWriter.getHistory(currentUsername);

			win.setText("Number of Wins: " + usersWins);
			loss.setText("Number of Losses: " + usersLosses);
			if(fastestTime == 100){ //value will be 100 by default eg they must play a game
				fastestGame.setText("You must win a game to set a time");
			} else {
				fastestGame.setText("Your fastest game was " + fastestTime + " seconds");
			}
			history.setText("History words:");
			historyWordsText.setText(historyWords);

		} else {
			this.username.setText("Guest");
			win.setText("Guests dont have saved stats");
			loss.setText("Login to save stats");
			fastestGame.setText("");
			history.setText("Guests dont have history words");
		}
		
		
		
	}
}
