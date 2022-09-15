package nz.ac.auckland.se206;


import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class CanvasController {

  @FXML private Label wordLabel;

  @FXML private Label timerLabel;

  @FXML private Canvas canvas;

  @FXML private Label topTenLabel;
  @FXML private Label textToSpeechLabel;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  private String currentWord;

  private int seconds = 60;
  private boolean winLose = false;

  private boolean end = false;

  private boolean pen = true;
  private TextToSpeechBackground textToSpeechBackground;

  private boolean textToSpeech;

  // mouse coordinates
  private double currentX;
  private double currentY;
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
    model = new DoodlePrediction();
    setTimerLabel(seconds); // sets timer to specified number of seconds
    doTimer();
    doPredictions();
  }

  private void setTool() {

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          if (pen) {
            graphic.setFill(Color.BLACK);
            graphic.setLineWidth(size);
            graphic.strokeLine(currentX, currentY, x, y); // Create a line that goes from the point (currentX, currentY) and (x,y)
          } else { // eraser
            graphic.setFill(Color.TRANSPARENT); // sets colour so that black won't be there
            graphic.clearRect(
                e.getX() - 5,
                e.getY() - 5,
                11,
                11); // then will clear a rectangle of 5 either side of the pixel the user is on
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
                } catch (IOException e) {
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
            Duration.seconds(1), // new keyframe every second so lists will update every second
            actionEvent -> {
              BufferedImage snapshot =
                  getCurrentSnapshot(); // uses main thread to get a snapshot of users drawing
              Task<Void> backgroundTask =
                  new Task<>() { // will run the rest of the task in the background thread to ensure
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
                                  snapshot,
                                  10); // uses the model to get predictions based on current user
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
                                getTopThree(list);
                              } catch (IOException e) {
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

  private void getTopThree(List<Classifications.Classification> list) throws IOException {
    for (int i = 0; i < 3; i++) { // cycles through top 3
      String strNew =
          list.get(i)
              .getClassName()
              .replace("_", " "); // replaces _ with spaces to ensure a standard format
      if (strNew.equals(
          currentWord)) { // tests to see if the word the user is trying to draw is in the top 3
        winLose = true;
        whenTimerEnds(); // called early to end game
        end = true;
      }
    }
  }

  private void printTopTen(List<Classifications.Classification> list) {
    StringBuilder sb = new StringBuilder();
    sb.append(System.lineSeparator());
    int i = 1;
    for (Classifications.Classification classification :
        list) { // cycles through list and build string to print top 10
      sb.append(i)
          .append(" : ")
          .append(
              classification
                  .getClassName()
                  .replace("_", " ")) // replaces _ with spaces to ensure a standard format
          .append(System.lineSeparator());
      i++;
    }
    topTenLabel.setText(String.valueOf(sb)); // updates label to the new top 10
  }

  /** When timer reaches 60secs */
  private void whenTimerEnds() throws IOException {
    Stage stage =
        (Stage) wordLabel.getScene().getWindow(); // finds current stage from the word label
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/game_over.fxml"));
    Scene scene = new Scene(loader.load(), 1000, 680);
    stage.setScene(scene);
    stage.show();
    GameOverController gameOverController =
        loader.getController(); // gets controller from loader to pass through information
    gameOverController.timeLeft(seconds);
    gameOverController.setWinLoseLabel(
        winLose, this); // passes if user won or lost and current instance of canvas controller
    gameOverController.give(
        textToSpeechBackground, textToSpeech); // passes text to speech and boolean

  }

  public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
    this.textToSpeech = textToSpeech;
    this.textToSpeechBackground = (textToSpeechBackground);
    if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
      textToSpeechLabel.setText("ON");
    }
  }

  @FXML
  private void onHoverClear() {
    textToSpeechBackground.backgroundSpeak("Clear Canvas", textToSpeech);
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
    textToSpeechBackground.backgroundSpeak("Canvas, draw here", textToSpeech);
  }

  @FXML
  private void onHoverTitle() {
    textToSpeechBackground.backgroundSpeak("Speed Drawing", textToSpeech);
  }

  @FXML
  private void onHoverWord() {
    textToSpeechBackground.backgroundSpeak(currentWord, textToSpeech);
  }

  @FXML
  private void onHoverPen() {
    textToSpeechBackground.backgroundSpeak("pen tool", textToSpeech);
  }

  @FXML
  public void onHoverEraser() {
    textToSpeechBackground.backgroundSpeak("eraser tool", textToSpeech);
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
  }

  @FXML
  private void onSwitchToPen() { //"https://www.flaticon.com/free-icons/brush" title="brush icons">Brush icons created by Freepik - Flaticon
    pen = true;
    setTool();
  }

  //"https://www.flaticon.com/free-icons/eraser" title="eraser icons">Eraser icons created by Freepik - Flaticon
  @FXML
  private void onSwitchToEraser() {
    pen = false;
    setTool();
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  } //https://www.flaticon.com/free-icons/recycle-bin title="recycle bin icons">Recycle bin icons created by lakonicon - Flaticon
}


