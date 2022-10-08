package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ZenCanvasController {

	@FXML private Button backButton;
	
	
	
	
	
	@FXML
	public void onMainMenu() throws IOException {
		
		Stage stage = (Stage) backButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
																							// main menu
		
		
		Scene scene = new Scene(loader.load(), 1000, 680);
		stage.setScene(scene);
		stage.show();
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
