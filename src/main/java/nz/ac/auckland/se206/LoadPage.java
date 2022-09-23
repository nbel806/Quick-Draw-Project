package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class LoadPage {

  void extracted(
      TextToSpeechBackground textToSpeechBackground,
      Boolean textToSpeech,
      String currentUsername,
      Stage stage)
      throws IOException {
    FXMLLoader loader =
        new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
    // menu page
    Scene scene = new Scene(loader.load(), 1000, 680);
    MainMenuController ctrl = loader.getController(); // need controller to pass information
    // may need to add code to pass though tts here
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.getUsername(currentUsername);
    stage.setScene(scene);
    stage.show();
  }
}
