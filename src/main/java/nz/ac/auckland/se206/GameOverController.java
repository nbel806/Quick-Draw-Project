package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;
import nz.ac.auckland.se206.words.WordPageController;

public class GameOverController {
  @FXML
  private Label winLoseLabel2;
  @FXML private Label winLoseLabel;
  @FXML private Button playAgainButton;
  @FXML private Label textToSpeechLabel;

  private CanvasController canvasController;
  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;
  private boolean win;

  private String winLoseString;

  private int timeLeft;

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeechBackground = textToSpeechBackground;
    this.textToSpeech = textToSpeech;
    if (textToSpeech) { // updates label to ensure it is correct
      textToSpeechLabel.setText("ON");
    }
  }

  public void setWinLoseLabel(boolean winLose, CanvasController ctrl) {
    this.win = winLose;
    if(win){
      winLoseLabel.setText("You won with " + timeLeft);
      winLoseLabel2.setText("seconds left!");

      winLoseString = "You won with " + timeLeft + "Seconds left!";
    } else {
      winLoseLabel.setText("You lost!");
      winLoseLabel2.setText("");
      winLoseString = "You lost!";
    }
    canvasController = ctrl;
  }

  public void timeLeft(int sec) {
    timeLeft = sec;
  }

  @FXML
  private void onSave() {
    Stage stage = (Stage) playAgainButton.getScene().getWindow(); // gets the stage from the button
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Image");
    File file =
        fileChooser.showSaveDialog(
            stage); // shows a popup to allow user to choose where to save file
    if (file != null) {
      try {
        ImageIO.write(
            canvasController.getCurrentSnapshot(),
            "bmp",
            file); // creates a new image to save to the users location
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  @FXML
  private void onPlayAgain() throws IOException {
    Stage stage = (Stage) playAgainButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(
            App.class.getResource(
                "/fxml/word_page.fxml")); // reset to a new word_page where a new word will be
    // generated
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();

    WordPageController ctrl =
        loader.getController(); // gets controller of new page to pass text to speech
    ctrl.give(
        textToSpeechBackground,
        textToSpeech); // passes text to speech instance and boolean to next page
  }

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Game over", textToSpeech);
  }

  @FXML
  private void onHoverWinLose() {
    textToSpeechBackground.backgroundSpeak("You " + winLoseString, textToSpeech);
  }

  @FXML
  private void onHoverSave() {
    textToSpeechBackground.backgroundSpeak("Save Button", textToSpeech);
  }

  @FXML
  private void onHoverPlayAgain() {
    textToSpeechBackground.backgroundSpeak("Play Again Button", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  @FXML
  private void onTextToSpeech() {
    textToSpeech = !textToSpeech; // inverts boolean
    if (textToSpeech) { // then sets label accordingly
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
  private void onHoverLogo() {
    textToSpeechBackground.backgroundSpeak("Speedy Sketchers logo", textToSpeech);
  }

}
