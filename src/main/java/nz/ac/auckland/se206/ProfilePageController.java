package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class ProfilePageController {

	@FXML
	private Button back;
	@FXML
	private Label username;
	@FXML
	private Label win;
	@FXML
	private Label loss;

	@FXML
	private Label textToSpeechLabel;
	private Boolean textToSpeech;
	private TextToSpeechBackground textToSpeechBackground;

	@FXML
	private void onMainMenu() throws IOException {

		Stage stage = (Stage) back.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
																							// menu page
		Scene scene = new Scene(loader.load(), 1000, 680);
		MainMenuController ctrl = loader.getController(); // need controller to pass information
		// may need to add code to pass though tts here
		ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
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

}
