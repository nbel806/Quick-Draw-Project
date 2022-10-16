package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.CanvasController;
import nz.ac.auckland.se206.HiddenWordCanvasController;
import nz.ac.auckland.se206.SpreadSheetReaderWriter;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class WordPageController {

  @FXML private Button minusConfidence;
  @FXML private Button minusAccuracy;
  @FXML private Button minusTime;
  @FXML private Button plusTime;
  @FXML private Button plusConfidence;
  @FXML private Button plusAccuracy;

  @FXML private Text confidenceLabel;
  @FXML private Text wordsLabel;
  @FXML private Text accuracyLabel;
  @FXML private Text timeLabel;
  @FXML private Text wordToDraw;

  @FXML private Button readyButton;

  @FXML private Label textToSpeechLabel;
  @FXML private Label userLabel;

  @FXML private ImageView volumeImage;
  @FXML private ImageView newImage;
  @FXML private ImageView userImage;
  @FXML private ImageView hiddenWordModeImage;

  private String currentWord;
  private Boolean isHiddenWordMode = false;
  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;
  private String currentUsername = null;
  private String currentProfilePic = null;
  private int time = 60;
  private int accuracy = 3;
  private int confidence = 1;
  private int words = 1;
  private int overallDif = 1;

  /**
   * Picks a random word from the easy category using category selector
   *
   * @throws IOException If the model cannot be found on the file system.
   * @throws CsvException If file does not exist
   * @throws URISyntaxException If URI does not exist
   */
  private void setWordToDraw() throws IOException, URISyntaxException, CsvException {
    if (currentUsername == null) { // if guest
      CategorySelector categorySelector = new CategorySelector(); // picks random word
      ArrayList<Object> randomWords = new ArrayList<>();
      switch (words) {
        case 1 -> // easy
        randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.E));
        case 2 -> { // easy and medium
          randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.E));
          randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.M));
        }
        case 3 -> { // easy medium and hard
          randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.E));
          randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.M));
          randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.H));
        }
        case 4 -> // just hard
        randomWords.add(categorySelector.getRandomCategory(CategorySelector.Difficulty.H));
      }
      currentWord = (String) randomWords.get(new Random().nextInt(randomWords.size()));
    } else { // if user chosen from their pool of words left
      SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
      String[] historyArray = spreadSheetReaderWriter.getHistory(currentUsername).split(",");

      CategorySelector categorySelector = new CategorySelector(); // picks random word
      currentWord = categorySelector.getRandomCategory(words, historyArray, currentUsername);
    }
    SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
    spreadSheetReaderWriter.updateWords(currentWord, currentUsername);
    wordToDraw.setText(currentWord);
  }

  /**
   * pass the text to speech functionality
   *
   * @param textToSpeechBackground generates tts functionality from tts class
   * @param textToSpeech activates tts functionality if is true
   */
  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeechBackground = textToSpeechBackground;
    this.textToSpeech = textToSpeech;
    if (textToSpeech) { // checks if text to speech was previously enabled then will make this page
      // mirror
      textToSpeechLabel.setText("ON");
    }
  }

  /**
   * create the current username and select a profile picture
   *
   * @param username current username
   * @param profilePic profile picture selected
   */
  public void getUsername(String username, String profilePic)
      throws IOException, URISyntaxException, CsvException {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      currentProfilePic = profilePic;
      userLabel.setText(currentUsername);
      // Set profile pic
      File file = new File(profilePic);
      Image image = new Image(file.toURI().toString());
      userImage.setImage(image);
      currentProfilePic = profilePic;
      setFirstDifficulty(currentUsername);
    } else {
      userLabel.setText("Guest");
      // Set guest pic
      File file = new File("src/main/resources/images/ProfilePics/GuestPic.png");
      Image image = new Image(file.toURI().toString());
      userImage.setImage(image);
      setDifficulty(accuracy, confidence, words, time);
    }
    setWordToDraw();
  }

  /**
   * get a new hidden word
   *
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException If the file cannot be found locally
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onNewWord() throws IOException, URISyntaxException, CsvException {
    // Get new word
    isHiddenWordMode = false;
    setWordToDraw();
  }

  /**
   * switch to hidden mode
   *
   * @throws IOException If the model cannot be found on the file system.
   * @throws URISyntaxException If the file cannot be found locally
   * @throws CsvException If the user info cannot be found locally
   */
  @FXML
  private void onHiddenWordMode()
      throws IOException, URISyntaxException, CsvException, WordNotFoundException {
    isHiddenWordMode = true;
    setWordToDraw();
    wordToDraw.setText("?????");
  }

  /** image gets larger when mouse hovers on */
  @FXML
  private void onHoverNew() {
    newImage.setFitHeight(57);
    newImage.setFitWidth(57);
  }

  /** label speaks out when mouse hovers on */
  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("you have 60 seconds on easy mode", textToSpeech);
  }

  /** label speaks out when mouse hovers on */
  @FXML
  private void onHoverWord() {
    textToSpeechBackground.backgroundSpeak(currentWord, textToSpeech);
  }

  /** label speaks out and button style changes when mouse hovers on */
  @FXML
  private void onHoverReady() {
    textToSpeechBackground.backgroundSpeak("Ready", textToSpeech);
    readyButton.setStyle(
        "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-background-color: #99F4B3;");
  }

  /** label speaks out and image gets larger when mouse hovers on */
  @FXML
  private void onHoverHidden() {
    textToSpeechBackground.backgroundSpeak("Hidden word mode", textToSpeech);
    hiddenWordModeImage.setFitWidth(56);
    hiddenWordModeImage.setFitHeight(66);
  }

  /** image gets larger when mouse is away */
  @FXML
  private void onHiddenExit() {
    hiddenWordModeImage.setFitWidth(53);
    hiddenWordModeImage.setFitHeight(63);
  }

  /** button style restores when mouse is away */
  @FXML
  private void onExitReady() {
    readyButton.setStyle(
        "-fx-background-radius: 25px; -fx-border-radius: 25px; -fx-background-color: white;");
  }

  /** label speaks out when mouse hovers on */
  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  /** initialize or disconnect the tts feature */
  @FXML
  private void onTextToSpeech() {
    textToSpeech = !textToSpeech; // toggles text to speech
    if (textToSpeech) { // sets label to correct value
      textToSpeechLabel.setText("ON");
    } else {
      textToSpeechLabel.setText("OFF");
    }
  }

  /** label speaks out and image gets larger when mouse hovers on */
  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  /**
   * switch to canvas page or hidden word canvas page
   *
   * @throws IOException If the model cannot be found on the file system.
   * @throws WordNotFoundException word does not exist
   */
  @FXML
  private void onReady() throws IOException, WordNotFoundException {
    Stage stage =
        (Stage) readyButton.getScene().getWindow(); // uses the ready button to fine the stage

    FXMLLoader loader; // uses the ready button to fine the stage
    if (!isHiddenWordMode) {
      loader = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml"));
      Scene scene = new Scene(loader.load(), 1000, 680);
      stage.setScene(scene);
      CanvasController canvasController =
          loader.getController(); // gets the newly created controller for next
      // page
      canvasController.setTimeAccuracy(time, accuracy, confidence, words, overallDif);
      canvasController.setWordLabel(
          currentWord); // passes the current word so that the next screen can display
      // it
      canvasController.give(
          textToSpeechBackground, textToSpeech); // passes the background threaded text to
      // speech
      // and whether it is on or not
      canvasController.getUsername(currentUsername, currentProfilePic);
    } else {
      loader = new FXMLLoader(App.class.getResource("/fxml/hidden_word_canvas.fxml"));
      Scene scene = new Scene(loader.load(), 1000, 680);
      stage.setScene(scene);
      HiddenWordCanvasController hiddenWordCanvasController =
          loader.getController(); // gets the newly created
      // controller for next page
      hiddenWordCanvasController.setTimeAccuracy(time, accuracy, confidence, words, overallDif);
      hiddenWordCanvasController.give(
          textToSpeechBackground, textToSpeech); // passes the background threaded
      // text to speech
      // and whether it is on or not
      hiddenWordCanvasController.setDefinitionList(currentWord);
      hiddenWordCanvasController.getUsername(currentUsername, currentProfilePic);
    }
    stage.show();
  }

  /** image restores its size when mouse is away */
  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }

  /** image restores its size when mouse is away */
  @FXML
  private void onNewExit() {
    newImage.setFitHeight(55);
    newImage.setFitWidth(55);
  }

  /** label speaks out when mouse hovers on */
  @FXML
  private void onHoverJustDraw() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  /**
   * set the difficulty selection
   *
   * @param accuracy is the result within top 1/2/3 prediction
   * @param confidence confidence percentage
   * @param words word category
   * @param time time limit
   */
  public void setDifficulty(int accuracy, int confidence, int words, int time) {
    this.time = time;
    timeLabel.setText(time + "secs"); // adds secs for readability
    this.accuracy = accuracy;
    accuracyLabel.setText("Top " + accuracy);

    confidenceLabel.setText(confidence + "%"); // adds % for readability
    this.confidence = confidence;

    if (words == 1) { // sets text corosponding to number of words
      wordsLabel.setText("E");
    } else if (words == 2) {
      wordsLabel.setText("E,M");
    } else if (words == 3) {
      wordsLabel.setText("E,M,H");
    } else if (words == 4) {
      wordsLabel.setText("H");
    } else {
      wordsLabel.setText("ERROR"); // error output shouldnt be reached
    }
    this.words = words;
    setPlusMinusLabels(); // sets opacity
    overallDifficulty(accuracy, confidence, words, time); // writes to csv
  }

  /** sets the opacity of the + - labeles */
  private void setPlusMinusLabels() {
    if (time == 60) { // disables plus by greying out
      plusTime.setOpacity(0.2);
    }
    if (time == 15) { // disables minus by greying out button
      minusTime.setOpacity(0.2);
    }
    if (confidence == 50) { // disables plus by greying out
      plusConfidence.setOpacity(0.2);
    }
    if (confidence == 1) { // disables minus by greying outbutton
      minusConfidence.setOpacity(0.2);
    }
    if (accuracy == 1) { // disables minus by greying out
      minusAccuracy.setOpacity(0.2);
      plusAccuracy.setOpacity(1); // enables plus
    }
    if (accuracy == 3) { // disables plus by greying out
      plusAccuracy.setOpacity(0.2);
      minusAccuracy.setOpacity(1); // enables minus
    }
    if (accuracy == 2) { // enables both
      minusAccuracy.setOpacity(1);
      plusAccuracy.setOpacity(1);
    }
  }

  /**
   * intitaly called to gather users past settings
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  private void setFirstDifficulty(String currentUsername) throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    accuracy = sheetReaderWriter.getUsersAccuracy(currentUsername);
    confidence =
        sheetReaderWriter.getUsersConfidence(currentUsername); // gathers conf from prev settings
    time = sheetReaderWriter.getUsersTime(currentUsername);
    words = sheetReaderWriter.getUsersWords(currentUsername);
    setDifficulty(accuracy, confidence, words, time); // sets opacitys
  }

  /**
   * pass the difficulty selection and get the level under different combination
   *
   * @param accuracy is the result within top 1/2/3 prediction
   * @param confidence confidence percentage
   * @param words word category
   * @param time time limit
   */
  private void overallDifficulty(int accuracy, int confidence, int words, int time) {
    if (words == 4 && confidence == 50 && accuracy == 1 && time == 15) { // master level
      overallDif = 4;
    } else if (words >= 3 && confidence >= 25 && accuracy == 1 && time <= 30) { // hard level
      overallDif = 3;
    } else if (words >= 2 && confidence >= 10 && accuracy <= 2 && time <= 45) { // medium level
      overallDif = 2;
    } else { // easy level
      overallDif = 1;
    }
  }

  /**
   * increments time
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickTimeUp() throws IOException, CsvException {
    if (time == 15) { // increase time dif
      time = 30;
      minusTime.setOpacity(1);
    } else if (time == 30) {
      time = 45;
    } else if (time == 45) {
      time = 60; // if 60 wont increase as limit
    }
    setDifficulty(accuracy, confidence, words, time);
    updateUserTime(time); // writes to csv
  }

  /**
   * increments accuracy
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickAccuracyUp() throws IOException, CsvException {
    if (accuracy != 3) { // cant increase if 3
      accuracy++;
    }
    setDifficulty(accuracy, confidence, words, time);
    updateUserAccuracy(accuracy); // writes to csv
  }

  /**
   * increments confidence
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickConfidenceUp() throws IOException, CsvException {
    if (confidence == 25) {
      confidence = 50;
    } else if (confidence == 10) {
      confidence = 25;
    } else if (confidence == 1) {
      confidence = 10;
      minusConfidence.setOpacity(1); // cant decrease if 1
    }
    setDifficulty(accuracy, confidence, words, time); // sets opacity for buttons
    updateUserConfidence(confidence); // writes to csv
  }

  /**
   * Decreaments time
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickTimeDown() throws IOException, CsvException {
    if (time == 60) { // decreases time setting
      time = 45;
      plusTime.setOpacity(1);
    } else if (time == 45) {
      time = 30;
    } else if (time == 30) {
      time = 15;
    } // 15 cant be decreased
    setDifficulty(accuracy, confidence, words, time); // updates opacity
    updateUserTime(time); // writes to csv
  }

  /**
   * Decreaments accuracy
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickAccuracyDown() throws IOException, CsvException {
    if (accuracy != 1) { // unless accuray is one will decrement
      accuracy--;
    }
    setDifficulty(accuracy, confidence, words, time);
    updateUserAccuracy(accuracy); // writes to csv
  }

  /**
   * Decreaments confidence
   *
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  @FXML
  private void onClickConfidenceDown() throws IOException, CsvException {
    if (confidence == 50) { // decrements confidence
      confidence = 25;
      plusConfidence.setOpacity(1);
    } else if (confidence == 25) {
      confidence = 10;
    } else if (confidence == 10) {
      confidence = 1;
    }
    setDifficulty(accuracy, confidence, words, time); // changes opacity
    updateUserConfidence(confidence); // writes to csv
  }

  /**
   * updates the users time therough the csv
   *
   * @param time the new input to update on csv
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  private void updateUserTime(int time) throws IOException, CsvException {
    this.time = time;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersTime(time, currentUsername);
  }

  /**
   * updates the users confidence therough the csv
   *
   * @param confidence the new input to update on csv
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  private void updateUserConfidence(int confidence) throws IOException, CsvException {
    this.confidence = confidence;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersConfidence(confidence, currentUsername);
  }

  /**
   * updates the users accuracy therough the csv
   *
   * @param accuracy the new input to update on csv
   * @throws IOException if file is not found
   * @throws CsvException if out of the file range
   */
  private void updateUserAccuracy(int accuracy) throws IOException, CsvException {
    this.accuracy = accuracy;
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    sheetReaderWriter.updateUsersAccuracy(accuracy, currentUsername);
  }
}
