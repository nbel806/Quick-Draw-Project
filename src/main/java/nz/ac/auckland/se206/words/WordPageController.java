package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CanvasController;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class WordPageController {

  @FXML private Text wordToDraw;

  @FXML private Button readyButton;
  @FXML private Label textToSpeechLabel;

  private String currentWord;
  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;

  private String currentUsername = null;

  public void initialize() throws IOException, URISyntaxException, CsvException {
    setWordToDraw();
  }

  /** Picks a random word from the easy category using category selector */
  private void setWordToDraw() throws IOException, URISyntaxException, CsvException {
    CategorySelector categorySelector = new CategorySelector();
    currentWord = categorySelector.getRandomCategory(CategorySelector.Difficulty.E);
    wordToDraw.setText(currentWord);
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeechBackground = textToSpeechBackground;
    this.textToSpeech = textToSpeech;
    if (textToSpeech) { // checks if text to speech was previously enabled then will make this page
      // mirror
      textToSpeechLabel.setText("ON");
    }
  }

  public void getUsername(String username) {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
    }
  }


  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("You have 60 seconds to draw a ...", textToSpeech);
  }

  @FXML
  private void onHoverWord() {
    textToSpeechBackground.backgroundSpeak(currentWord, textToSpeech);
  }

  @FXML
  private void onHoverReady() {
    textToSpeechBackground.backgroundSpeak("Ready Button", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  @FXML
  private void onTextToSpeech() {
    textToSpeech = !textToSpeech; // toggles text to speech
    if (textToSpeech) { // sets label to correct value
      textToSpeechLabel.setText("ON");
    } else {
      textToSpeechLabel.setText("OFF");
    }
  }

  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
  }

  @FXML
  private void onReady() throws IOException {
    Stage stage =
        (Stage) readyButton.getScene().getWindow(); // uses the ready button to fine the stage
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml"));
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();
    CanvasController canvasController =
        loader.getController(); // gets the newly created controller for next page
    canvasController.setWordLabel(
        currentWord); // passes the current word so that the next screen can display it
    canvasController.give(
        textToSpeechBackground,
        textToSpeech); // passes the background threaded text to speech and whether it is on or not
  }
  }

