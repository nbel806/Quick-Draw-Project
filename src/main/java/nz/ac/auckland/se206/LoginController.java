package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import nz.ac.auckland.se206.speech.TextToSpeechBackground;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class LoginController {
	// Set name of the file to set user data
	private static final String fileName = "userdata.csv";

	@FXML
	private Button createButton;
	@FXML
	private Button loginButton;
	@FXML
	private Button backButton;
	@FXML
	private TextField usernameText;
	@FXML
	private ImageView penImageView;
	@FXML
	private ImageView userImageView;
	@FXML
	private Label outputLabel;

	@FXML
	private Label textToSpeechLabel;

	private String currentUsername = null; // The username currently logged in
	private Boolean textToSpeech;
	private TextToSpeechBackground textToSpeechBackground;

	/**
	 * This method creates a blank csv file
	 * 
	 * @throws IOException throws if there is no name
	 */
	public static void createDataBase() throws IOException {
		// file object creation
		File file = new File(fileName);

		// file creation
		file.createNewFile();

	}

	/**
	 * This method creates a new record/profile and appends into the csv file
	 * 
	 * @param username the name of the person using the app
	 */
	private void createProfile(String username) {
		String[] profile = { null, null, null, null, null, null };

		try {
			FileWriter csvwriter = new FileWriter(fileName, true);
			try (CSVWriter writer = new CSVWriter(csvwriter)) {
				// Check if username exists
				if (!searchUsername(username)) {
					this.currentUsername = username;
					usernameText.clear();
					usernameText.setPromptText("Hi, " + username);

					outputLabel.setText("Profile Created");
					outputLabel.setStyle("-fx-text-fill: green;");
					outputLabel.setOpacity(0.5);

					// create profile
					profile[0] = username;

					//adds all the easy words to the csv
					CategorySelector category = new CategorySelector();
					profile[1] = category.getCategory(Difficulty.E).toString();
					profile[2] = "0";//number of wins
					profile[3] = "0";//number of losses
					profile[4] = "100";//fastest time
					profile[5] = "none";//history words

					writer.writeNext(profile);

					// Set current username
					currentUsername = username;
				} else {
					System.out.println("USERNAME IS TAKEN");
					usernameText.clear();

					outputLabel.setText("Invalid Username");
					outputLabel.setStyle("-fx-text-fill: red;");
					outputLabel.setOpacity(0.5);
				}
			} catch (URISyntaxException | CsvException e) {
				throw new RuntimeException(e);
			}

			csvwriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method searches in the csv file for the username passed and returns a
	 * flag
	 * 
	 * @param username the name of the person using the app
	 * @return if name was found or not
	 * @throws IOException if file name was empty
	 */
	private static Boolean searchUsername(String username) throws IOException {
		boolean flag = false;
		String line;
		FileReader fr;

		fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);

		System.out.println(username);
		System.out.println();
		while ((line = br.readLine()) != null) {
			// Check if current line contains the username to be found
			String[] record = line.split(",");
			String tempUsername = record[0];
			tempUsername = tempUsername.substring(1, (tempUsername.length() - 1));

			if (username.equals(tempUsername)) {
				flag = true;
			}

		}

		br.close();
		return flag;
	}

	public void setUsername(String username) {
		// Set current username
		currentUsername = username;
	}

	@FXML
	private void onCreate(){
		String username = usernameText.getText();

		if (usernameText.getText().trim().isEmpty()) {
			outputLabel.setText("The textbox is empty");
			outputLabel.setStyle("-fx-text-fill: red;");
			outputLabel.setOpacity(0.5);
		} else {
			createProfile(username);
		}

	}

	@FXML
	private void onLogin() throws IOException {
		String username = usernameText.getText();

		if (usernameText.getText().trim().isEmpty()) {
			outputLabel.setText("The textbox is empty");
			outputLabel.setStyle("-fx-text-fill: red;");
			outputLabel.setOpacity(0.5);
		} else {
			if (!searchUsername(username)) {
				usernameText.clear();

				outputLabel.setText("Invalid Username");
				outputLabel.setStyle("-fx-text-fill: red;");
				outputLabel.setOpacity(0.5);

			} else {
				currentUsername = username;
				usernameText.clear();
				usernameText.setPromptText("Hi, " + username);

				outputLabel.setText("Login Success");
				outputLabel.setStyle("-fx-text-fill: green;");
				outputLabel.setOpacity(0.5);

			}
		}

	}
	
	@FXML
	private void onLogout() throws IOException{
		currentUsername = null;
		outputLabel.setText("Logout Success");
		outputLabel.setStyle("-fx-text-fill: green;");
	}

	/**
	 * This method goes to the previous page
	 *
	 * @throws IOException if name of file is not found
	 */
	@FXML
	private void onBack() throws IOException {
		// switch to the main menu after login
		Stage stage = (Stage) backButton.getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main_menu.fxml")); // creates a new instance of
																							// menu page
		Scene scene = new Scene(loader.load(), 1000, 680);
		MainMenuController ctrl = loader.getController(); // need controller to pass information
		ctrl.give(textToSpeechBackground, textToSpeech);

		// Pass username
		ctrl.getUsername(currentUsername);

		stage.setScene(scene);
		stage.show();
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

	public void onHoverTextToSpeechLabel() {
		textToSpeechBackground.backgroundSpeak("toggle text to speech", textToSpeech);
	}

	public void onHoverTextToSpeech() {
		textToSpeechBackground.backgroundSpeak("On", textToSpeech);
	}

	public void give(TextToSpeechBackground textToSpeechBackground, Boolean textToSpeech) {
		this.textToSpeech = textToSpeech;
		this.textToSpeechBackground = (textToSpeechBackground);
		if (textToSpeech) { // updates text to speech label to ensure it is up-to-date
			textToSpeechLabel.setText("ON");
		}
	}

	@FXML
	private void onHoverLogin() {
		textToSpeechBackground.backgroundSpeak("login Button", textToSpeech);
	}

	@FXML
	private void onHoverCreate() {
		textToSpeechBackground.backgroundSpeak("Create Button", textToSpeech);
	}

	@FXML
	private void onHoverBack() {
		textToSpeechBackground.backgroundSpeak("Back Button", textToSpeech);
	}
}