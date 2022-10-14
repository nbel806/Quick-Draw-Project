package nz.ac.auckland.se206.words;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SpreadSheetReaderWriter;
import nz.ac.auckland.se206.ZenCanvasController;

public class ZenWordPageController {

  @FXML private Label lostWord1;
  @FXML private Label lostWord2;
  @FXML private Label lostWord3;
  @FXML private Label lostWord4;
  @FXML private Label lostWord5;

  @FXML private Button readyButton;

  @FXML private Text wordLabel;

  @FXML private ImageView newImage;

  private String currentWord;
  private String currentUsername;

  public void initialize() throws IOException, URISyntaxException, CsvException {
    setWordToDraw();
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

  @FXML
  public void onZenCanvas() throws IOException {
    Stage stage = (Stage) readyButton.getScene().getWindow();
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/zen_canvas.fxml")); // creates a new instance of
    // zen canvas
    Scene scene = new Scene(loader.load(), 999, 625);
    stage.setScene(scene);
    stage.show();
    ZenCanvasController ctrl = loader.getController();
    ctrl.setWordLabel("chosen word: " + currentWord);
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

  public void onNewWord() throws IOException, URISyntaxException, CsvException {
    setWordToDraw();
  }

  public void getUsername(String currentUsername) throws IOException, CsvException {
    this.currentUsername = currentUsername;
    setPreviousWords();
  }

  @FXML
  private void onHoverNew() {
    newImage.setFitHeight(57);
    newImage.setFitWidth(57);
    newImage.setLayoutX(20);
  }

  @FXML
  private void onNewExit() {
    newImage.setFitHeight(55);
    newImage.setFitWidth(55);
  }

  @FXML
  private void onHoverReady() {
    readyButton.setStyle("-fx-background-radius: 25; -fx-background-color: #99F4B3");
  }

  @FXML
  private void onReadyExit() {
    readyButton.setStyle("-fx-background-radius: 50; -fx-background-color: white");
  }

  @FXML
  private void onClickPlay1() {
    if (currentUsername == null) {
      return;
    }
    currentWord = lostWord1.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay2() {
    if (currentUsername == null) {
      return;
    }
    currentWord = lostWord2.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay3() {
    if (currentUsername == null) {
      return;
    }
    currentWord = lostWord3.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay4() {
    if (currentUsername == null) {
      return;
    }
    currentWord = lostWord4.getText();
    wordLabel.setText(currentWord);
  }

  @FXML
  private void onClickPlay5() {
    if (currentUsername == null) {
      return;
    }
    currentWord = lostWord5.getText();
    wordLabel.setText(currentWord);
  }
}
