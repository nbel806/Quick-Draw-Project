package nz.ac.auckland.se206.speech;

import javafx.concurrent.Task;

public class TextToSpeechBackground {

  private final TextToSpeech tts;

  public TextToSpeechBackground(TextToSpeech tts) {
    this.tts = tts;
  }

  public void backgroundSpeak(String toRead, boolean textToSpeech) {
    if (textToSpeech) { // if text to speech is enabled then will create a background task
      Task<Void> backgroundTask =
          new Task<>() {
            @Override
            protected Void call() {
              tts.speak(
                  toRead); // uses googles text to speech to speak but will do it in background to
              // ensure no lag
              return null;
            }
          };
      Thread backgroundThread = new Thread(backgroundTask);
      backgroundThread.start();
    }
  }
}
