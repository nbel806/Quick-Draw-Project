package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;
import nz.ac.auckland.se206.words.WordPageController;

public class GameOverController {

  @FXML private Label timeLabel;
  @FXML private Label winLoseLabel;
  @FXML private Button playAgainButton;
  @FXML private Button menuButton;
  @FXML private Button saveButton;
  @FXML private Label textToSpeechLabel;
  @FXML private ImageView volumeImage;

  private CanvasController canvasController;
  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;

  private String winLoseString;

  private int timeLeft;
  private String currentUsername;
  private int accuracy;
  private int time;
  private int confidence;
  private int words;

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeechBackground = textToSpeechBackground;
    this.textToSpeech = textToSpeech;
    if (textToSpeech) { // updates label to ensure it is correct
      textToSpeechLabel.setText("ON");
    }
  }

  public void setWinLoseLabel(boolean winLose, CanvasController ctrl)
      throws IOException, CsvException {
    if (winLose) { // if user won display message and time
      winLoseLabel.setText("YOU WON");
      timeLabel.setText("TIME LEFT: " + timeLeft + " seconds");
      winLoseString = "You won with " + timeLeft + "Seconds left!";
    } else { // if user looses display message
      winLoseLabel.setText("YOU LOST");
      timeLabel.setText("TIME LIMIT REACHED");
      winLoseString = "You lost!";
    }
    canvasController = ctrl;
    SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
    spreadSheetReaderWriter.updateResult(
        winLose, currentUsername); // writes over old file to update win/loss record
  }

  public void timeLeft(int sec) throws IOException, CsvException {
    timeLeft = sec;
    SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
    spreadSheetReaderWriter.updateTime(60 - timeLeft, currentUsername);
  }

  public void getUsername(String username) {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
    }
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
            file); // creates a new image to save to the
        // users location
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  @FXML
  private void onClickMenu() throws IOException, CsvException {
    Stage stage = (Stage) menuButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // reset to a new word_page
    // where a new word will be
    // generated
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();

    MainMenuController ctrl =
        loader.getController(); // gets controller of new page to pass text to speech
    ctrl.give(
        textToSpeechBackground,
        textToSpeech); // passes text to speech instance and boolean to next page
    ctrl.getUsername(currentUsername);
  }

  @FXML
  private void onPlayAgain() throws IOException, URISyntaxException, CsvException {
    Stage stage = (Stage) playAgainButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/word_page.fxml")); // reset to a new word_page
    // where a new word will be
    // generated
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();

    WordPageController ctrl =
        loader.getController(); // gets controller of new page to pass text to speech
    ctrl.give(
        textToSpeechBackground,
        textToSpeech); // passes text to speech instance and boolean to next page
    ctrl.setDifficulty(accuracy, confidence, words, time);
    ctrl.getUsername(currentUsername);
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
  private void onHoverWinLose() {
    textToSpeechBackground.backgroundSpeak(winLoseString, textToSpeech);
  }

  // Below is a list of methods when mouse hovers a button
  @FXML
  private void onHoverSave() {
    textToSpeechBackground.backgroundSpeak("Save", textToSpeech);
    saveButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  @FXML
  private void onHoverPlayAgain() {
    textToSpeechBackground.backgroundSpeak("Play Again", textToSpeech);
    playAgainButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  @FXML
  private void onHoverMenu() {
    textToSpeechBackground.backgroundSpeak("Main Menu", textToSpeech);
    menuButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  // Below is list of methods for when mouse exits a button
  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }

  @FXML
  private void onSaveExit() {
    saveButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2");
  }

  @FXML
  private void onMenuExit() {
    menuButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2");
  }

  @FXML
  private void onPlayAgainExit() {
    playAgainButton.setStyle(
        "-fx-background-radius: 10px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2");
  }

  @FXML
  public void onHoverTimeLeft() {
    textToSpeechBackground.backgroundSpeak(timeLeft + "seconds left", textToSpeech);
  }

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  public void setTimeAccuracy(int time, int userAccuracy, int confidence, int words) {
    this.time = time;
    this.accuracy = userAccuracy;
    this.confidence = confidence;
    this.words = words;
  }
}
