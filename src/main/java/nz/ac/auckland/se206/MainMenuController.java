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

  private int accuracy = 3;
  private int time = 60;
  private int words = 1;
  private int confidence = 1;

  /**
   * pass the text to speech functionality
   * 
   * @param textToSpeechBackground generates tts functionality from tts class
   * @param textToSpeech activates tts functionality if is true
   */
  public void give(TextToSpeechBackground tts, Boolean textToSpeech) {
    textToSpeechBackground = tts; // passes through the text to speech instance
    this.textToSpeech = textToSpeech;
    if (textToSpeech) {
      textToSpeechLabel.setText("ON");
    }
  }

  /**
   * create the current username and select a profile picture
   * 
   * @param username current username
   * @param profilePic profile picture selected
   */
  public void getUsername(String username, String profilePic) throws IOException, CsvException {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      currentProfilePic = profilePic;
      userLabel.setText(currentUsername);
      setDifficulty(currentUsername);

    } else {
      userLabel.setText("Guest");
    }
  }

  /**
   * get the difficulties that user chose from last time
   * 
   * @param currentUsername curren user name that is passed into
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  private void setDifficulty(String currentUsername) throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    accuracy = sheetReaderWriter.getUsersAccuracy(currentUsername);
    confidence = sheetReaderWriter.getUsersConfidence(currentUsername);
    time = sheetReaderWriter.getUsersTime(currentUsername);
    words = sheetReaderWriter.getUsersWords(currentUsername);
  }

  /**
   * switch to zen mode word page
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
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

  /**
   * switch to normal and hidden word page
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException If URI does not exist
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onPlay() throws IOException, URISyntaxException, CsvException {

    Stage stage = (Stage) playButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/word_page.fxml")); // creates a new instance of
    // word page
    Scene scene = new Scene(loader.load(), 1000, 680);
    WordPageController ctrl = loader.getController(); // need controller to pass information
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.setDifficulty(accuracy, confidence, words, time);
    ctrl.getUsername(currentUsername, currentProfilePic); // passes username
    stage.setScene(scene);
    stage.show();
  }

  /**
   * switch to profile page
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onProfile() throws IOException, CsvException {
    Stage stage = (Stage) profileButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedProfile(
        textToSpeechBackground, textToSpeech, currentUsername, currentProfilePic, stage);
  }

  /**
   * switch to login page
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvValidationException If the file is invalid
   */
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

  /**
   * initialize or disconnect the tts feature
   */
  @FXML
  private void onTextToSpeech() {
    textToSpeech = !textToSpeech; // inverts boolean
    if (textToSpeech) { // then sets label accordingly
      textToSpeechLabel.setText("ON");
    } else {
      textToSpeechLabel.setText("OFF");
    }
  }

  /**
   * label speaks out when mouser hovers on
   */
  @FXML
  private void onHoverCreators() {
    textToSpeechBackground.backgroundSpeak(
        "Bought to you by speedy sketcher and Team 15", textToSpeech);
  }

  /**
   * label speaks out when mouser hovers on
   */
  @FXML
  private void onHoverLogo() {
    textToSpeechBackground.backgroundSpeak("Speedy Sketchers logo", textToSpeech);
  }

  /**
   * label speaks out and images becomes slightly larger when mouser hovers on
   */
  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  /**
   * label speaks out when mouser hovers on
   */
  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  /**
   * label speaks out and button style changes when mouse hovers on
   */
  @FXML
  private void onHoverPlay() {
    textToSpeechBackground.backgroundSpeak("Start", textToSpeech);
    playButton.setStyle(
        "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  /**
   * label speaks out and image gets slightly when mouse hovers on 
   */
  @FXML
  private void onHoverProfile() {
    textToSpeechBackground.backgroundSpeak("Profile", textToSpeech);
    userImage.setFitHeight(69);
    userImage.setFitWidth(63);
  }

  /**
   * label speaks out and image gets slightly when mouse hovers on 
   */
  @FXML
  private void onHoverLogin() {
    textToSpeechBackground.backgroundSpeak("Login", textToSpeech);
    loginImage.setFitHeight(73);
    loginImage.setFitWidth(62);
  }

  /**
   * label speaks out when mouse hovers on
   */
  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  /**
   * label speaks out and image gets slightly when mouse hovers on 
   */
  @FXML
  private void onHoverZen() {
    textToSpeechBackground.backgroundSpeak("Zen Mode", textToSpeech);
    zenImage.setFitHeight(64);
    zenImage.setFitWidth(62);
  }

  /**
   * button style stores when mouse is away
   */
  @FXML
  private void onPlayExit() {
    playButton.setStyle(
        "-fx-background-radius: 25px; -fx-border-radius: 25px; -fx-background-color: transparent; -fx-border-color: white;");
  }

  /**
   * image restores when mouse is away
   */
  @FXML
  private void onProfileExit() {
    userImage.setFitHeight(66);
    userImage.setFitWidth(60);
  }

  /**
   * image restores when mouse is away
   */
  @FXML
  private void onLoginExit() {
    loginImage.setFitHeight(70);
    loginImage.setFitWidth(60);
  }

  /**
   * image restores when mouse is away
   */
  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }

  /**
   * image restores when mouse is away
   */
  @FXML
  private void onZenExit() {
    zenImage.setFitHeight(61);
    zenImage.setFitWidth(59);
  }

  /**
   * set prediction result within top 3
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetAccuracyTop3() throws IOException, CsvException {
    updateUserAccuracy(3);
  }

  /**
   * set prediction result within top 2
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetAccuracyTop2() throws IOException, CsvException {
    updateUserAccuracy(2);
  }

  /**
   * set prediction result within top 1
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetAccuracyTop1() throws IOException, CsvException {
    updateUserAccuracy(1);
  }

  /**
   * update current accuracy for current user
   * 
   * @param accuracy current accuracy
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  private void updateUserAccuracy(int accuracy) throws IOException, CsvException {
    this.accuracy = accuracy;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersAccuracy(accuracy, currentUsername);
  }

  /**
   * set the current confidence to be 1%
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetConfidence1() throws IOException, CsvException {
    updateUserConfidence(1);
  }

  /**
   * set the current confidence to be 10%
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetConfidence10() throws IOException, CsvException {
    updateUserConfidence(10);
  }

  /**
   * set the current confidence to be 25%
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetConfidence25() throws IOException, CsvException {
    updateUserConfidence(25);
  }

  /**
   * set the current confidence to be 50%
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetConfidence50() throws IOException, CsvException {
    updateUserConfidence(50);
  }

  /**
   * update confidence for current user
   * 
   * @param confidence current confidence percentage
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  private void updateUserConfidence(int confidence) throws IOException, CsvException {
    this.confidence = confidence;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersConfidence(confidence, currentUsername);
  }

  /**
   * set word category easy
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetWordsE() throws IOException, CsvException {
    updateUserWords(1);
  }

  /**
   * set word category easy and medium
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetWordsEM() throws IOException, CsvException {
    updateUserWords(2);
  }

  /**
   * set word category easy, medium, and hard
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetWordsEMH() throws IOException, CsvException {
    updateUserWords(3);
  }

  /**
   * set word category hard
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetWordsH() throws IOException, CsvException {
    updateUserWords(4);
  }

  /**
   * update word category for the current user
   * 
   * @param words current word category
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  private void updateUserWords(int words) throws IOException, CsvException {
    this.words = words;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersWords(words, currentUsername);
  }

  /**
   * set time limit 60s
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetTime60() throws IOException, CsvException {
    updateUserTime(60);
  }

  /**
   * set time limit 45s
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetTime45() throws IOException, CsvException {
    updateUserTime(45);
  }
  
  /**
   * set time limit 30s
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetTime30() throws IOException, CsvException {
    updateUserTime(30);
  }

  /**
   * set time limit 15s
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onSetTime15() throws IOException, CsvException {
    updateUserTime(15);
  }

  /**
   * update time limit for current user
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  private void updateUserTime(int time) throws IOException, CsvException {
    this.time = time;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersTime(time, currentUsername);
  }
}
