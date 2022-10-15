package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
import nz.ac.auckland.se206.LoadPage;
import nz.ac.auckland.se206.SpreadSheetReaderWriter;
import nz.ac.auckland.se206.ZenCanvasController;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class ZenWordPageController {

  @FXML private Label lostWord1;
  @FXML private Label lostWord2;
  @FXML private Label lostWord3;
  @FXML private Label lostWord4;
  @FXML private Label lostWord5;
  @FXML private Label textToSpeechLabel;
  @FXML private Label userLabel;
  @FXML private Button readyButton;
  @FXML private Button backButton;
  @FXML private Text wordLabel;
  @FXML private ImageView newImage;
  @FXML private ImageView saveImage;
  @FXML private ImageView userImage;
  @FXML private ImageView volumeImage;

  private String currentWord;
  private String currentUsername = null;
  private String currentProfilePic;
  private Boolean textToSpeech = false;
  private TextToSpeechBackground textToSpeechBackground;

  public void initialize() throws IOException, URISyntaxException, CsvException {
    setWordToDraw();
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
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
      // Set profile pic
      File file = new File(profilePic);
      Image image = new Image(file.toURI().toString());
      userImage.setImage(image);
    } else {
      userLabel.setText("Guest");
      // Set guest pic
      File file = new File("src/main/resources/images/ProfilePics/GuestPic.png");
      Image image = new Image(file.toURI().toString());
      userImage.setImage(image);
    }
    setPreviousWords();
  }

  private void setPreviousWords() throws IOException, CsvException {
    SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
    String[] lastWords = spreadSheetReaderWriter.getUsersLostWords(currentUsername);
    if (currentUsername == null) {
      lostWord1.setOpacity(0.2);
      lostWord2.setOpacity(0.2);
      lostWord3.setOpacity(0.2);
      lostWord4.setOpacity(0.2);
      lostWord5.setOpacity(0.2);
    }
    lostWord1.setText("1. " + lastWords[0]);
    lostWord2.setText("2. " + lastWords[1]);
    lostWord3.setText("3. " + lastWords[2]);
    lostWord4.setText("4. " + lastWords[3]);
    lostWord5.setText("5. " + lastWords[4]);
  }
  
  public void setWordToDraw() throws IOException, URISyntaxException, CsvException {
    CategorySelector categorySelector = new CategorySelector(); // picks random easy word
    String[] words = new String[3];
    words[0] = categorySelector.getRandomCategory(CategorySelector.Difficulty.E);
    words[1] = categorySelector.getRandomCategory(CategorySelector.Difficulty.M);
    words[2] = categorySelector.getRandomCategory(CategorySelector.Difficulty.H);
    currentWord = words[new Random().nextInt(words.length)];
    wordLabel.setText(currentWord);
  }

  @FXML
  public void onZenCanvas() throws IOException {
    Stage stage = (Stage) readyButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/zen_canvas.fxml")); // creates a new instance of
    // zen canvas
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();
    ZenCanvasController ctrl = loader.getController();
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.getUsername(currentUsername, currentProfilePic);
    ctrl.setWordLabel(currentWord);
  }

  public void onNewWord() throws IOException, URISyntaxException, CsvException {
    setWordToDraw();
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
  private void onClickPlay1() {
    if (currentUsername == null || lostWord1.getText().equals("1. Play normal mode first")) {
      return;
    }
    currentWord = lostWord1.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay2() {
    if (currentUsername == null || lostWord2.getText().equals("2. Play normal mode first")) {
      return;
    }
    currentWord = lostWord2.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay3() {
    if (currentUsername == null || lostWord3.getText().equals("3. Play normal mode first")) {
      return;
    }
    currentWord = lostWord3.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay4() {
    if (currentUsername == null || lostWord4.getText().equals("4. Play normal mode first")) {
      return;
    }
    currentWord = lostWord4.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay5() {
    if (currentUsername == null || lostWord5.getText().equals("5. Play normal mode first")) {
      return;
    }
    currentWord = lostWord5.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onBack() throws IOException, CsvException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedMainMenu(
        textToSpeechBackground, textToSpeech, currentUsername, currentProfilePic, stage);
  }

  // Below is a list of methods when mouse hovers a button
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

  @FXML
  private void onHoverNew() {
    newImage.setFitHeight(57);
    newImage.setFitWidth(57);
    newImage.setLayoutX(20);
  }

  @FXML
  private void onHoverReady() {
    readyButton.setStyle(
        "-fx-border-radius: 10; fx-background-border: 10; -fx-background-color: #99F4B3; -fx-border-color: #99F4B3;");
  }

  @FXML
  private void onHoverBack() {
    textToSpeechBackground.backgroundSpeak("Back Button", textToSpeech);
    backButton.setStyle(
        "-fx-background-radius: 100px; -fx-text-fill: white; -fx-border-radius: 100px; "
            + "-fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  // Below is a list of methods when mouse exits a button
  @FXML
  private void onNewExit() {
    newImage.setFitHeight(55);
    newImage.setFitWidth(55);
  }

  @FXML
  private void onReadyExit() {
    readyButton.setStyle(
        "-fx-border-radius: 10; fx-background-border: 10; -fx-background-color: transparent; -fx-border-color: white");
  }

  @FXML
  private void onBackExit() {
    backButton.setStyle(
        "-fx-background-radius: 100px; -fx-background-color: #EB4A5A; "
            + "-fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 100px;");
  }

  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }
}
