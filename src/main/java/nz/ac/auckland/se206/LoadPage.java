package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

public class LoadPage {

  public void extractedMainMenu(
      TextToSpeechBackground textToSpeechBackground,
      Boolean textToSpeech,
      String currentUsername,
      Stage stage)
      throws IOException, CsvException {
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

  public void extractedProfile(
      TextToSpeechBackground textToSpeechBackground,
      boolean textToSpeech,
      String currentUsername,
      Stage stage)
      throws IOException, CsvException {
    FXMLLoader loader =
        new FXMLLoader(
            App.class.getResource("/fxml/profile_page.fxml")); // creates a new instance of
    // menu page
    Scene scene = new Scene(loader.load(), 1000, 680);
    ProfilePageController ctrl = loader.getController(); // need controller to pass information
    // may need to add code to pass though tts here
    ctrl.give(textToSpeechBackground, textToSpeech); // passes text to speech instance and boolean
    ctrl.setUsername(currentUsername);
    stage.setScene(scene);
    stage.show();
  }
}
