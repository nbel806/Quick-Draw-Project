package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class BadgePageController {

  @FXML
  private ImageView volumeImage;
  @FXML
  private Label usernameLabel;

  @FXML
  private Button backButton;
  @FXML
  private Button menuButton;
  @FXML
  private Label textToSpeechLabel;
  private TextToSpeechBackground textToSpeechBackground;
  private String currentUsername;
  private boolean textToSpeech;
  private String numberBadges;

  @FXML
  private void onHoverUser() {
    textToSpeechBackground.backgroundSpeak(currentUsername, textToSpeech);
  }

  @FXML
  private void onHoverNumBadges() {
    textToSpeechBackground.backgroundSpeak("You have " + numberBadges + "badges", textToSpeech);
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

  public void onHoverTextToSpeechLabel(MouseEvent mouseEvent) {
  }

  public void onHoverTextToSpeech(MouseDragEvent mouseDragEvent) {
  }

  public void onBack() throws IOException, CsvException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedProfile(textToSpeechBackground, textToSpeech, currentUsername, stage);
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

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  @FXML
  private void onClickMenu() throws IOException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedMainMenu(textToSpeechBackground, textToSpeech, currentUsername, stage);
  }

  // Below is list of methods for when mouse hovers a button
  @FXML
  private void onHoverMenu() {
    textToSpeechBackground.backgroundSpeak("Menu Button", textToSpeech);
    menuButton.setStyle(
        "-fx-background-radius: 100px; -fx-text-fill: white; -fx-border-radius: 100px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  // Below is list of methods for when mouse exits a button
  @FXML
  private void onMenuExit() {
    menuButton.setStyle(
        "-fx-background-radius: 100px; -fx-text-fill: white; -fx-background-color: #EB4A5A; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 100px;");
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
      textToSpeechLabel.setText("ON");
    }
  }

  public void setUsername(String username) {
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      this.usernameLabel.setText(currentUsername);
    }
  }
}
