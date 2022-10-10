package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.text.DecimalFormat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
  @FXML private Label badgeLabel, badgePercentage;
  @FXML private Label historyLabel;
  @FXML private Label winstreakLabel;
  @FXML private ImageView volumeImage;
  // Lists
  @FXML private ListView<String> historyListView;
  // Badges
  @FXML private ImageView secTen, secThirty;
  @FXML private ImageView fiveGames, tenGames, fiftyGames, hundredGames;
  @FXML public ImageView fiveStreak, tenStreak, fiftyStreak, hundredStreak;
  @FXML public ImageView easyWins, mediumWins, hardWins, masterWins;
  @FXML private ImageView godArtist;
  @FXML private ProgressBar badgeProgress;

  private Boolean textToSpeech;
  private TextToSpeechBackground textToSpeechBackground;
  private String currentUsername;
  private String currentWord;
  private int usersLosses;
  private int usersWins;
  private int totalGames;
  private int fastestTime;
  private double badgeRatio;
  private double winRate;
  private DecimalFormat df = new DecimalFormat("#.#");
  private int numberBadges = 0;
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

  @FXML
  private void onHoverNumBadges() {
    textToSpeechBackground.backgroundSpeak("You have " + numberBadges + "badges", textToSpeech);
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
      textToSpeechLabel.setText("ON");
    }
  }

  // Set username, stats, and badges
  public void setUsername(String username) throws IOException, CsvException {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      this.usernameLabel.setText(currentUsername);
      SpreadSheetReaderWriter spreadSheetReaderWriter = new SpreadSheetReaderWriter();
      int streak = spreadSheetReaderWriter.getStreak(currentUsername);

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
      winstreakLabel.setText(Integer.toString(streak));
      winrateLabel.setText(df.format(winRate) + "%");

      if (fastestTime == 100) { // value will be 100 by default eg they must play a game
        fastestLabel.setText("-");
      } else {
        fastestLabel.setText(fastestTime + "s");
      }

      // Add current word to history of words
      if (!currentWord.equals("none")) {
        historyListView.getItems().addAll(historyWords);
      }

      // Set badges
      setBadges();
      badgeLabel.setText("You've unlocked " + numberBadges + "/15" + " Badges");

      // Calculate percentage of badges completed
      badgeRatio = (((double) numberBadges * 100) / (double) 15);
      badgePercentage.setText("(" + String.format("%.0f", badgeRatio) + "%)");
      badgeProgress.setProgress(badgeRatio / 100);

    } else {
      // If user is not signed in
      this.usernameLabel.setText("Guest");
      winLabel.setText("-");
      fastestLabel.setText("-");
      gameLabel.setText("-");
      winrateLabel.setText("-");
      winstreakLabel.setText("-");

      // Set badges
      badgeLabel.setText("You've unlocked 0/15 Badges");
      badgePercentage.setText("(0%)");
      setAllBadgesClear();
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

  // Method to display selected word in list of words
  @FXML
  private void onSelectWord(MouseEvent event) {
    String word = historyListView.getSelectionModel().getSelectedItem();

    if (word == null || word.isEmpty()) {
      historyLabel.setText("Nothing Selected");
    } else {
      historyLabel.setText(word);
    }
  }

  // Badge Methods
  private void setAllBadgesClear() {
    // streak badges set to transparent
    fiveStreak.setOpacity(0.2);
    tenStreak.setOpacity(0.2);
    fiftyStreak.setOpacity(0.2);
    hundredStreak.setOpacity(0.2);

    // Games played to be added

    // difficulty badges set to transparent
    easyWins.setOpacity(0.2);
    mediumWins.setOpacity(0.2);
    hardWins.setOpacity(0.2);
    masterWins.setOpacity(0.2);

    // fastest game badges set to transparent
    secThirty.setOpacity(0.2);
    secTen.setOpacity(0.2);

    // Special badges set to transparent
    godArtist.setOpacity(0.2);
  }

  private void setBadges() throws IOException, CsvException {
    setTimeBadges();
    setGamesPlayedBadges();
    setWinStreakBadges();
    setDifficultWinBadges();
    setExtraBadges();
  }

  private void setExtraBadges() {
    godArtist.setOpacity(0.2);
    // TODO: once difficulties are added
  }

  private void setDifficultWinBadges() {
    easyWins.setOpacity(0.2);
    mediumWins.setOpacity(0.2);
    hardWins.setOpacity(0.2);
    masterWins.setOpacity(0.2);
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
      numberBadges++;
    }
    if (streak >= 10) { // 10 win streak
      tenStreak.setOpacity(1);
      numberBadges++;
    }
    if (streak >= 50) { // 50 win streak
      fiftyStreak.setOpacity(1);
      numberBadges++;
    }
    if (streak >= 100) {
      hundredStreak.setOpacity(1);
      numberBadges++;
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
      numberBadges++;
    }
    if (games >= 10) { // 10 games played badge
      tenGames.setOpacity(1);
      numberBadges++;
    }
    if (games >= 50) { // 50 games played badge
      fiftyGames.setOpacity(1);
      numberBadges++;
    }
    if (games >= 100) { // 100 games played badge
      hundredGames.setOpacity(1);
      numberBadges++;
    }
  }

  private void setTimeBadges() throws IOException, CsvException {
    SpreadSheetReaderWriter sheetReaderWriter = new SpreadSheetReaderWriter();
    int fastest = sheetReaderWriter.getFastest(currentUsername);
    // set all transparent
    secThirty.setOpacity(0.2);
    secTen.setOpacity(0.2);
    if (fastest <= 30) {
      secThirty.setOpacity(1);
      numberBadges++;
      // 30 second badge
    }
    if (fastest <= 10) {
      secTen.setOpacity(1);
      numberBadges++;
      // 10 second badge
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
        "-fx-background-radius: 100px; -fx-background-color: #EB4A5A; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 100px;");
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
}
