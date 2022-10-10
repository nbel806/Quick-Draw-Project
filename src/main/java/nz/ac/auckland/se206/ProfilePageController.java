package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.text.DecimalFormat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class ProfilePageController {

  @FXML private ImageView userImage;
  @FXML private Button badgeButton;
  @FXML private Button backButton;
  @FXML private Label usernameLabel;
  @FXML private Label winLabel;
  @FXML private Label gameLabel;
  @FXML private Label winrateLabel;
  @FXML private Label fastestLabel;
  @FXML private Label textToSpeechLabel;
  @FXML private ImageView volumeImage;
  @FXML private ListView<String> historyListView;

  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;
  private String currentUsername;
  private String currentWord;
  private int usersLosses;
  private int usersWins;
  private int totalGames;
  private int fastestTime;
  private double winRate;
  private DecimalFormat df = new DecimalFormat("#.#");
  private String[] historyWords;

  public void initialize() {}

  public void onHoverTextToSpeechLabel() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
  }

  public void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("On", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
      textToSpeechLabel.setText("ON");
    }
  }

  public void setUsername(String username) throws IOException, CsvException {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      this.usernameLabel.setText(currentUsername);
      SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();

      // Assign wins
      usersWins = spreadSheetReaderWriter.getWins(currentUsername);
      usersLosses = spreadSheetReaderWriter.getLosses(currentUsername);
      fastestTime = spreadSheetReaderWriter.getFastest(currentUsername);
      historyWords = spreadSheetReaderWriter.getHistory(currentUsername).split(",");
      currentWord = spreadSheetReaderWriter.getHistory(currentUsername);

      // Calculate games
      totalGames = usersWins + usersLosses;

      if (totalGames > 0) {
        winRate = (((double) usersWins * 100) / (double) totalGames);
      }

      // Update Labels
      winLabel.setText(Integer.toString(usersWins));
      gameLabel.setText(Integer.toString(totalGames));
      winrateLabel.setText(df.format(winRate) + "%");

      if (fastestTime == 100) { // value will be 100 by default eg they must play a game
        fastestLabel.setText("-");
      } else {
        fastestLabel.setText(fastestTime + " seconds");
      }

      // Add current word to history of words
      if (!currentWord.equals("none")) {
        historyListView.getItems().addAll(historyWords);
      }

    } else {
      // If user is not signed in
      this.usernameLabel.setText("Guest");
      winLabel.setText("-");
      fastestLabel.setText("-");
      gameLabel.setText("-");
      winrateLabel.setText("-");
    }
  }

  @FXML
  private void onBack() throws IOException, CsvException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    LoadPage loadPage = new LoadPage();
    loadPage.extractedMainMenu(textToSpeechBackground, textToSpeech, currentUsername, stage);
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
  private void onHoverUser() {
    textToSpeechBackground.backgroundSpeak(currentUsername, textToSpeech);
  }

  @FXML
  private void onHoverWinRate() {
    textToSpeechBackground.backgroundSpeak(winRate + "win rate", textToSpeech);
  }

  @FXML
  private void onHoverHistory() {
    textToSpeechBackground.backgroundSpeak("History of words played", textToSpeech);
  }

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Just Draw", textToSpeech);
  }

  @FXML
  private void onHoverGamesPlayed() {
    textToSpeechBackground.backgroundSpeak("Played" + totalGames + "Games", textToSpeech);
  }

  @FXML
  private void onHoverGamesWon() {
    textToSpeechBackground.backgroundSpeak("you have won" + usersWins, textToSpeech);
  }

  @FXML
  private void onHoverFastest() {
    textToSpeechBackground.backgroundSpeak(
        "fastest game was " + fastestTime + "seconds", textToSpeech);
  }

  public void onClickBadge() throws IOException, CsvException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(
            App.class.getResource("/fxml/badge_page.fxml")); // creates a new instance of page
    Scene scene = new Scene(loader.load(), 1000, 680);
    BadgePageController ctrl = loader.getController(); // need controller to pass information
    // may need to add code to pass though tts here
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.setUsername(currentUsername);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void onHoverBadge() {
    textToSpeechBackground.backgroundSpeak("Badge", textToSpeech);
    badgeButton.setStyle(
        "-fx-background-radius: 100px; -fx-text-fill: white; -fx-border-radius: 100px; -fx-background-color: #99DAF4; -fx-border-color: #99DAF4;");
  }

  @FXML
  private void onBadgeExit() {
    badgeButton.setStyle(
        "-fx-background-radius: 100px; -fx-text-fill: white; -fx-background-color: #EB4A5A; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 100px;");
  }
}
