package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class BadgePageController {

  public Label numBadgeLabel;
  public ImageView userImage;
  public Circle fiveGames;
  public Circle tenGames;
  public Circle fiftyGames;
  public Circle hundredGames;
  public ImageView fiveStreak;
  public ImageView fiftyStreak;
  public ImageView hundredStreak;
  public ImageView tenStreak;
  @FXML private ImageView secten;
  @FXML private ImageView secthirty;
  @FXML private ImageView volumeImage;
  @FXML private Label usernameLabel;

  @FXML private Button backButton;
  @FXML private Button menuButton;
  @FXML private Label textToSpeechLabel;
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

  @FXML
  private void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("On", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
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

  public void setUsername(String username) throws IOException, CsvException {
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      this.usernameLabel.setText(currentUsername);
      setBadges();
    } else {
      // If user is not signed in
      this.usernameLabel.setText("Guest");
      numberBadges = "0";
      numBadgeLabel.setText("0");
      setAllBadgesClear();
    }
  }

  private void setAllBadgesClear() {
    // streak badges set to transparent
    fiveStreak.setOpacity(0.2);
    tenStreak.setOpacity(0.2);
    fiftyStreak.setOpacity(0.2);
    hundredStreak.setOpacity(0.2);
    // games played badges set to transparent
    fiveGames.setOpacity(0.2);
    tenGames.setOpacity(0.2);
    fiftyGames.setOpacity(0.2);
    hundredGames.setOpacity(0.2);
    // fastest game badges set to transparent
    secthirty.setOpacity(0.2);
    secten.setOpacity(0.2);
  }

  private void setBadges() throws IOException, CsvException {
    setTimeBadges();
    setGamesPlayedBadges();
    setWinStreakBadges();
    setDifficultWinBadges();
    setExtraBadges();
  }

  private void setExtraBadges() {
    // TODO: once difficulties are added
  }

  private void setDifficultWinBadges() {
    // TODO: once difficulties are added
  }

  private void setWinStreakBadges() throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    int streak = sheetReaderWriter.getStreak(currentUsername);
    // set all transparent
    fiveStreak.setOpacity(0.2);
    tenStreak.setOpacity(0.2);
    fiftyStreak.setOpacity(0.2);
    hundredStreak.setOpacity(0.2);
    if (streak >= 5) { // 5 win streak
      fiveStreak.setOpacity(1);
    }
    if (streak >= 10) { // 10 win streak
      tenStreak.setOpacity(1);
    }
    if (streak >= 50) { // 50 win streak
      fiftyStreak.setOpacity(1);
    }
    if (streak >= 100) {
      hundredStreak.setOpacity(1);
    }
  }

  private void setGamesPlayedBadges() throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    int games =
        sheetReaderWriter.getWins(currentUsername) + sheetReaderWriter.getLosses(currentUsername);
    // set all transparent
    fiveGames.setOpacity(0.2);
    tenGames.setOpacity(0.2);
    fiftyGames.setOpacity(0.2);
    hundredGames.setOpacity(0.2);

    if (games >= 5) { // 5 games played badge
      fiveGames.setOpacity(1);
    }
    if (games >= 10) { // 10 games played badge
      tenGames.setOpacity(1);
    }
    if (games >= 50) { // 50 games played badge
      fiftyGames.setOpacity(1);
    }
    if (games >= 100) { // 100 games played badge
      hundredGames.setOpacity(1);
    }
  }

  private void setTimeBadges() throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    int fastest = sheetReaderWriter.getFastest(currentUsername);
    // set all transparent
    secthirty.setOpacity(0.2);
    secten.setOpacity(0.2);
    if (fastest <= 30) {
      secthirty.setOpacity(1);
      // 30 second badge
    }
    if (fastest <= 10) {
      secten.setOpacity(1);
      // 10 second badge
    }
  }
}
