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

  @FXML private Button plusWords;
  @FXML private Button minusWords;
  @FXML private Label wordDifLabel;
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
  private int words = 1;


  
  /**
   * pass the text to speech functionality
   * 
   * @param textToSpeechBackground generates tts functionality from tts class
   * @param textToSpeech activates tts functionality if is true
   */
  public void give(TextToSpeechBackground tts, Boolean textToSpeech)
      throws IOException, CsvException {
    textToSpeechBackground = tts; // passes through the text to speech instance
    this.textToSpeech = textToSpeech;
    if (textToSpeech) {
      textToSpeechLabel.setText("ON");
    }
    updateUserWords(words);
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
      SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
      words = sheetReaderWriter.getUsersWords(currentUsername);
    } else {
      userLabel.setText("Guest");
    }
    updateUserWords(words);
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

  private void onClickWordsUp() throws IOException, CsvException {
    if (words != 4) {
      words++;
    }
    updateUserWords(words);
  }

  /**
   * set word category hard
   * 
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onClickWordsDown() throws IOException, CsvException {
    if (words != 1) {
      words--;
    }
    updateUserWords(words);
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
    if (words == 1) {
      wordDifLabel.setText("E");
      minusWords.setOpacity(0.2);
    } else if (words == 2) {
      wordDifLabel.setText("E,M");
      minusWords.setOpacity(1);
    } else if (words == 3) {
      wordDifLabel.setText("E,M,H");
      plusWords.setOpacity(1);
    } else if (words == 4) {
      wordDifLabel.setText("H");
      plusWords.setOpacity(0.2);
    } else {
      wordDifLabel.setText("ERROR");
    }
  }
}
