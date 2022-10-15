package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;
import nz.ac.auckland.se206.words.WordPageController;
import nz.ac.auckland.se206.words.ZenWordPageController;

public class MainMenuController {

  @FXML private Button playButton;
  @FXML private Button profileButton;
  @FXML private Button loginButton;
  @FXML private Label textToSpeechLabel;
  @FXML private Label userLabel;
  @FXML private ImageView userImage;
  @FXML private ImageView loginImage;
  @FXML private ImageView volumeImage;
  @FXML private ImageView zenImage;

  private Boolean textToSpeech = false;
  private TextToSpeechBackground textToSpeechBackground;
  private String currentUsername = null;
  private String currentProfilePic = null;
  private int words;

  public void give(TextToSpeechBackground tts, Boolean textToSpeech) {
    textToSpeechBackground = tts; // passes through the text to speech instance
    this.textToSpeech = textToSpeech;
    if (textToSpeech) {
      textToSpeechLabel.setText("ON");
    }
  }

  public void getUsername(String username, String profilePic) throws IOException, CsvException {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      currentProfilePic = profilePic;
      userLabel.setText(currentUsername);

    } else {
      userLabel.setText("Guest");
    }
  }

  @FXML
  private void onZenModeCanvas() throws IOException, CsvException {
    Stage stage = (Stage) playButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/zen_word_page.fxml")); // creates a new instance
    // of
    // word page
    Scene scene = new Scene(loader.load(), 1000, 680);
    ZenWordPageController ctrl = loader.getController();
    ctrl.getUsername(currentUsername, currentProfilePic);
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void onPlay() throws IOException, URISyntaxException, CsvException {

    Stage stage = (Stage) playButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/word_page.fxml")); // creates a new instance of
    // word page
    Scene scene = new Scene(loader.load(), 1000, 680);
    WordPageController ctrl = loader.getController(); // need controller to pass information
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.getUsername(currentUsername, currentProfilePic); // passes username
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void onProfile() throws IOException, CsvException {
    Stage stage = (Stage) profileButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedProfile(
        textToSpeechBackground, textToSpeech, currentUsername, currentProfilePic, stage);
  }

  @FXML
  private void onLogin() throws IOException, CsvValidationException {
    Stage stage = (Stage) loginButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/login_page.fxml")); // creates a new instance
    // of word page
    Scene scene = new Scene(loader.load(), 1000, 680);
    LoginController ctrl = loader.getController(); // need controller to pass information
    ctrl.give(textToSpeechBackground, textToSpeech);

    // Pass current username
    ctrl.setUsername(currentUsername, currentProfilePic);
    ctrl.displayUsers();
    stage.setScene(scene);
    stage.show();
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
  private void onHoverCreators() {
    textToSpeechBackground.backgroundSpeak(
        "Bought to you by speedy sketcher and Team 15", textToSpeech);
  }

  @FXML
  private void onHoverLogo() {
    textToSpeechBackground.backgroundSpeak("Speedy Sketchers logo", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  // Below is list of methods for when mouse hovers a button
  @FXML
  private void onHoverPlay() {
    textToSpeechBackground.backgroundSpeak("Start", textToSpeech);
    playButton.setStyle(
        "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  @FXML
  private void onHoverProfile() {
    textToSpeechBackground.backgroundSpeak("Profile", textToSpeech);
    userImage.setFitHeight(69);
    userImage.setFitWidth(63);
  }

  @FXML
  private void onHoverLogin() {
    textToSpeechBackground.backgroundSpeak("Login", textToSpeech);
    loginImage.setFitHeight(73);
    loginImage.setFitWidth(62);
  }

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  @FXML
  private void onHoverZen() {
    textToSpeechBackground.backgroundSpeak("Zen Mode", textToSpeech);
    zenImage.setFitHeight(64);
    zenImage.setFitWidth(62);
  }

  // Below is list of methods for when mouse exits a button
  @FXML
  private void onPlayExit() {
    playButton.setStyle(
        "-fx-background-radius: 25px; -fx-border-radius: 25px; -fx-background-color: transparent; -fx-border-color: white;");
  }

  @FXML
  private void onProfileExit() {
    userImage.setFitHeight(66);
    userImage.setFitWidth(60);
  }

  @FXML
  private void onLoginExit() {
    loginImage.setFitHeight(70);
    loginImage.setFitWidth(60);
  }

  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }

  @FXML
  private void onZenExit() {
    zenImage.setFitHeight(61);
    zenImage.setFitWidth(59);
  }

  @FXML
  private void onClickWordsUp() throws IOException, CsvException {
    if (words != 4) {
      words++;
    }
    updateUserWords(words);
  }

  @FXML
  private void onClickWordsDown() throws IOException, CsvException {
    if (words != 1) {
      words--;
    }
    updateUserWords(words);
  }

  private void updateUserWords(int words) throws IOException, CsvException {
    this.words = words;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersWords(words, currentUsername);
    // TODO: 16/10/22 add label change
  }
}
