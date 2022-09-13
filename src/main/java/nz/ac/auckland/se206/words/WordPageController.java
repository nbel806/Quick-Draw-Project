package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class WordPageController {

  @FXML private Text wordToDraw;

  @FXML private Button readyButton;
  @FXML private Label textToSpeechLabel;

  private String currentWord;
  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;

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
  private void onReady(){
  }
}
