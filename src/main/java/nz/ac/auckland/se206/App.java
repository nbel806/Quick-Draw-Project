package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;

/**
 * This is the entry point of the JavaFX application, while you can change this
 * class, it should remain as the class that runs the JavaFX application.
 */
public class App extends Application {
	public static void main(final String[] args) {
		launch();
	}

	private final TextToSpeech tts = new TextToSpeech();

	/**
	 * This method is invoked when the application starts. It loads and shows the
	 * "Canvas" scene.
	 *
	 * @param stage The primary stage of the application.
	 * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
	 */
	@Override
	public void start(final Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml"));
		Scene scene = new Scene(loader.load(), 1000, 680);
		stage.setScene(scene);
		MainMenuController ctrl = loader.getController(); // need controller to pass information
		ctrl.give(new TextToSpeechBackground(tts), false);
		stage.show();
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			tts.terminate(); // ensures that upon close the tts will terminate
		});
		LoginController.createDataBase(); // Create csv file
	}
}
