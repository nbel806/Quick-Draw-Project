package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ProfilePageController {

	@FXML private Button back;
	@FXML private Label username;
	@FXML private Label win;
	@FXML private Label loss;
	
	@FXML
	private void onMainMenu () throws IOException {
		
		Stage stage = (Stage) back.getScene().getWindow();
	    FXMLLoader loader =
	        new FXMLLoader(
	            App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of menu page
	    Scene scene = new Scene(loader.load(), 1000, 680);
	    MainMenuController ctrl = loader.getController(); // need controller to pass information
	    //may need to add code to pass though tts here
	    stage.setScene(scene);
	    stage.show();
	    
	}
	
	

}
