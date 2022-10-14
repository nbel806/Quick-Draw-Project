package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;
import nz.ac.auckland.se206.words.DictionaryLookup;
import nz.ac.auckland.se206.words.WordInfo;
import nz.ac.auckland.se206.words.WordNotFoundException;
import nz.ac.auckland.se206.words.WordPane;

public class HiddenWordCanvasController {

  @FXML private ImageView downArrow;
  @FXML private ImageView upArrow;
  @FXML private ImageView userImage;
  @FXML private ImageView penImage;
  @FXML private ImageView eraseImage;
  @FXML private ImageView clearImage;
  @FXML private ImageView volumeImage;

  @FXML private Canvas canvas;

  @FXML private Label wordLabel;
  @FXML private Label timerLabel;
  @FXML private Label userLabel;
  @FXML private Label topTenLabel;
  @FXML private Label textToSpeechLabel;

  @FXML private Text hintText1;
  @FXML private Text hintText2;
  @FXML private Text hintText3;

  @FXML private Circle upArrowCircle;
  @FXML private Circle downArrowCircle;

  @FXML private Button penButton;
  @FXML private Button eraseButton;
  @FXML private Button profileButton;

  @FXML private Accordion resultsAccordion;

  private GraphicsContext graphic;
  private DoodlePrediction model;
  private String currentWord;

  private boolean winLose = false;
  private boolean end = false;
  private boolean pen = true;
  private boolean textToSpeech;
  private boolean startedDrawing;

  private TextToSpeechBackground textToSpeechBackground;

  private String currentUsername;

  private int userAccuracy;
  private int confidence;
  private int words;
  private int time;
  private int overallDif;
  private int seconds;

  private double currentX;
  private double currentY;
  private double confidenceUser;
  private double lastWordPred = 0;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException {
    graphic = canvas.getGraphicsContext2D();
    setTool(); // calls method to set pen/eraser and size
    // Set pen button
    penButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: #99F4B3;");
    penImage.setFitHeight(71);
    penImage.setFitWidth(71);
    model = new DoodlePrediction();
    setTimerLabel(seconds); // sets timer to specified number of seconds
    doTimer();
    upArrow.setOpacity(0.1);
    upArrowCircle.setOpacity(0.1);
    downArrow.setOpacity(0.1);
    downArrowCircle.setOpacity(0.1);
  }

  private void setTool() {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
          if (!startedDrawing) {
            startedDrawing = true;
            doPredictions();
          }
        });
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 10;
          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;
          // This is the colour of the brush.
          if (pen) {
            graphic.setFill(Color.BLACK);
            graphic.setLineWidth(size);
            graphic.strokeLine(
                currentX, currentY, x, y); // Create a line that goes from the point (currentX,
            // currentY) and (x,y)
          } else { // eraser
            graphic.setFill(Color.TRANSPARENT); // sets colour so that black won't be there
            graphic.clearRect(
                e.getX() - 10,
                e.getY() - 10,
                16,
                16); // then will clear a rectangle of 5 either side
            // of the pixel the user is on
          }
          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  public BufferedImage getCurrentSnapshot() {
    final Image snapshot =
        canvas.snapshot(null, null); // is the current image based on user drawing on the canvas
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    // To release memory we dispose.
    graphics.dispose();
    return imageBinary;
  }

  /**
   * Sets word label from word passed
   *
   * @param wordToDraw word passed from previous screen
   */
  public void setWordLabel(String wordToDraw) {
    currentWord = wordToDraw;
    wordLabel.setText(currentWord);
  }

  /**
   * Timer label is set through
   *
   * @param time value of timer
   */
  private void setTimerLabel(int time) {
    timerLabel.setText(String.valueOf(time));
  }

  /** runs timer through timeline for 60secs until seconds = 0 */
  private void doTimer() {
    Timeline time = new Timeline();
    time.setCycleCount(Timeline.INDEFINITE);
    time.stop();
    KeyFrame keyFrame =
        new KeyFrame(
            Duration.seconds(1),
            actionEvent -> {
              seconds--;
              setTimerLabel(seconds); // decrements the timer and updates label
              if (end) {
                time.stop(); // if the game is over or time is up the timer stops
              }
              if (seconds <= 0) { // timer is over then end timer
                time.stop();
                end = true;
                try {
                  whenTimerEnds(); // runs to progress to next page
                } catch (IOException | CsvException e) {
                  throw new RuntimeException(e);
                }
              }
            });
    time.getKeyFrames().add(keyFrame);
    time.playFromStart();
  }

  /** Still needs work to not make application lag */
  private void doPredictions() {
    Timeline time = new Timeline();
    time.setCycleCount(Timeline.INDEFINITE);
    time.stop();
    KeyFrame keyFrame =
        new KeyFrame(
            Duration.seconds(1), // new keyframe every second so lists will update every
            // second
            actionEvent -> {
              BufferedImage snapshot =
                  getCurrentSnapshot(); // uses main thread to get a snapshot of users
              // drawing
              Task<Void> backgroundTask =
                  new Task<>() { // will run the rest of the task in the background thread
                    // to ensure
                    // user can draw smoothly and no lag
                    @Override
                    protected Void call() {
                      if (end) {
                        time.stop(); // if the timer is up ends the predictions
                      } else {
                        List<Classification> list;
                        try {
                          list =
                              model.getPredictions(
                                  snapshot, 345); // uses the model to get predictions
                          // based on current user
                          // drawing
                        } catch (TranslateException e) {
                          throw new RuntimeException(e);
                        }
                        Platform.runLater(
                            () -> {
                              printTopTen(
                                  list); // will run these methods in the main thread as they deal
                              // wil updating javafx elements
                              try {
                                getTopX(list);
                              } catch (IOException | CsvException e) {
                                throw new RuntimeException(e);
                              }
                            });
                      }
                      return null;
                    }
                  };

              Thread backgroundThread = new Thread(backgroundTask);
              backgroundThread
                  .start(); // all the ML modeling will happen in a background thread to reduce lag
            });

    time.getKeyFrames().add(keyFrame);
    time.playFromStart();
  }

  private void getTopX(List<Classifications.Classification> list) throws IOException, CsvException {
    for (int i = 0; i < userAccuracy; i++) { // cycles through top 3
      String strNew =
          list.get(i)
              .getClassName()
              .replace("_", " "); // replaces _ with spaces to ensure a standard
      // format
      if (strNew.equals(currentWord)) {
        // tests to see if the word the user is trying to draw is in the top 3
        if (list.get(i).getProbability() >= confidenceUser) {
          winLose = true;
          whenTimerEnds(); // called early to end game
          end = true;
        }
      }
    }
  }

  private void printTopTen(List<Classifications.Classification> list) {
    StringBuilder sb = new StringBuilder();
    sb.append(System.lineSeparator());
    int i = 1;
    for (Classifications.Classification classification :
        list) { // cycles through list and build string to print
      // top 10
      sb.append(i)
          .append(" : ")
          .append(classification.getClassName().replace("_", " ")) // replaces _ with spaces
          // to ensure a standard
          // format
          .append(System.lineSeparator());
      i++;
      if (i == 11) {
        break;
      }
    }
    topTenLabel.setText(String.valueOf(sb)); // updates label to the new top 10
    updateWordPrediction(list);
  }

  private void updateWordPrediction(List<Classification> list) {
    double wordPred = 0;
    for (Classifications.Classification classification : list) {
      if (classification.getClassName().equals(currentWord)) {
        wordPred = classification.getProbability();
        break;
      }
    }

    if (wordPred > lastWordPred) { // increase
      upArrow.setOpacity(1);
      upArrowCircle.setOpacity(1);
      downArrow.setOpacity(0.1);
      downArrowCircle.setOpacity(0.1);
    } else if (wordPred == lastWordPred) {
      upArrow.setOpacity(0.1);
      upArrowCircle.setOpacity(0.1);
      downArrow.setOpacity(0.1);
      downArrowCircle.setOpacity(0.1);
    } else { // decrease
      upArrow.setOpacity(0.1);
      upArrowCircle.setOpacity(0.1);
      downArrow.setOpacity(1);
      downArrowCircle.setOpacity(1);
    }
    lastWordPred = wordPred;
  }

  /** When timer reaches 0secs */
  private void whenTimerEnds() throws IOException, CsvException {
    BufferedImage bufferedImage = getCurrentSnapshot();
    Stage stage =
        (Stage) wordLabel.getScene().getWindow(); // finds current stage from the word label
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/game_over.fxml"));
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();
    GameOverController gameOverController =
        loader.getController(); // gets controller from loader to pass through
    // information
    gameOverController.getUsername(currentUsername);
    gameOverController.give(
        textToSpeechBackground, textToSpeech, bufferedImage); // passes text to speech and boolean
    gameOverController.timeLeft(seconds);
    gameOverController.setHiddenWinLoseLabel(winLose, this, overallDif);
    gameOverController.setTimeAccuracy(time, userAccuracy, confidence, words);
  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
      textToSpeechLabel.setText("ON");
    }
  }

  public void getUsername(String username) {
    // Check if username is not null
    if (username != null) {
      // If not null, update label as current username
      currentUsername = username;
      userLabel.setText(currentUsername);
    } else {
      userLabel.setText("Guest");
    }
  }

  @FXML
  private void onHoverClear() {
    textToSpeechBackground.backgroundSpeak("Clear Canvas", textToSpeech);
    clearImage.setFitHeight(83);
    clearImage.setFitWidth(83);
  }

  @FXML
  private void onHoverTimer() {
    textToSpeechBackground.backgroundSpeak(String.valueOf(seconds), textToSpeech);
  }

  @FXML
  private void onHoverTop10() {
    textToSpeechBackground.backgroundSpeak("List of Top 10 guesses", textToSpeech);
  }

  @FXML
  private void onHoverCanvas() {
    textToSpeechBackground.backgroundSpeak("draw here", textToSpeech);
  }

  @FXML
  private void onHoverPen() {
    textToSpeechBackground.backgroundSpeak(
        "pen tool", textToSpeech); // uses background task to read name
    penButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: #99F4B3;");
    penImage.setFitHeight(73); // enlarges button to make reactive
    penImage.setFitWidth(73);
  }

  @FXML
  public void onHoverEraser() {
    textToSpeechBackground.backgroundSpeak(
        "eraser tool", textToSpeech); // uses background thread to read name
    eraseButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: #99F4B3;");
    eraseImage.setFitHeight(73); // makes button reactive
    eraseImage.setFitWidth(73);
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
    textToSpeechBackground.backgroundSpeak("ON", textToSpeech);
  }

  @FXML
  private void onHoverTextToSpeech() {
    textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
    volumeImage.setFitHeight(48);
    volumeImage.setFitWidth(48);
  }

  @FXML
  private void
      onSwitchToPen() { // "https://www.flaticon.com/free-icons/brush" title="brush icons">Brush
    // icons
    // created by Freepik - Flaticon
    pen = true;
    setTool();

    // Change button
    penButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: #99F4B3;");
    penImage.setFitHeight(71); // reactive
    penImage.setFitWidth(71);
    eraseButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: white");
    eraseImage.setFitHeight(71); // reactive
    eraseImage.setFitWidth(71);
  }

  // "https://www.flaticon.com/free-icons/eraser" title="eraser icons">Eraser
  // icons created by Freepik - Flaticon
  @FXML
  private void onSwitchToEraser() {
    pen = false;
    setTool();

    // Changes button to show it is clicked
    eraseButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: #99F4B3;");
    eraseImage.setFitHeight(71); // enlarges button
    eraseImage.setFitWidth(71);
    penButton.setStyle(
        "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: white");
    penImage.setFitHeight(71); // enlarges button
    penImage.setFitWidth(71);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  } // https://www.flaticon.com/free-icons/recycle-bin title="recycle bin
  // icons">Recycle bin icons created by lakonicon - Flaticon

  // Below is list of methods for when mouse exits a button
  @FXML
  private void exitPen() {
    if (!pen) { // if eraser is curently active
      penButton.setStyle(
          "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: white");
      penImage.setFitHeight(71);
      penImage.setFitWidth(71);
    }
  }

  @FXML
  private void exitEraser() {
    if (pen) { // if pen too is active
      eraseButton.setStyle(
          "-fx-background-radius: 100px; -fx-border-radius: 100px; -fx-background-color: white");
      eraseImage.setFitHeight(71);
      eraseImage.setFitWidth(71);
    }
  }

  @FXML
  private void exitClear() {
    clearImage.setFitHeight(80);
    clearImage.setFitWidth(80);
  }

  @FXML
  private void onVolumeExit() {
    volumeImage.setFitHeight(45);
    volumeImage.setFitWidth(45);
  }

  @FXML
  private void onHoverPredictions() {
    textToSpeechBackground.backgroundSpeak("Predictions", textToSpeech);
  }

  public void setTimeAccuracy(int time, int accuracy, int confidence, int words, int overallDif) {
    seconds = time;
    this.time = time;
    userAccuracy = accuracy;
    this.confidence = confidence;
    switch (confidence) {
      case 1 -> this.confidenceUser = 0.01;
      case 10 -> this.confidenceUser = 0.1;
      case 25 -> this.confidenceUser = 0.25;
      case 50 -> this.confidenceUser = 0.5;
    }
    this.words = words;
    this.overallDif = overallDif;
  }

  public void setDefinitionList(String wordToDraw) throws IOException, WordNotFoundException {
    currentWord = wordToDraw;
    resultsAccordion.getPanes().clear();
    WordInfo wordResult = DictionaryLookup.searchWordInfo(wordToDraw);
    TitledPane pane = WordPane.generateWordPane(wordToDraw, wordResult);
    resultsAccordion.getPanes().add(pane);

    // setting the hints
    hintText1.setText(wordToDraw.length() + " characters");
    hintText2.setText("first character: " + wordToDraw.charAt(0));
    hintText3.setText("last character: " + wordToDraw.charAt(wordToDraw.length() - 1));
  }
}