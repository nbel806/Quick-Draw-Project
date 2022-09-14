package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {
	// Set name of the file to set user data
	private static String fileName = "userdata.csv";

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

	private String currentUsername = null;

	/**
	 * This method creates a blank csv file
	 * 
	 * @throws IOException
	 */
	public static void createDataBase() throws IOException {
		// file object creation
		File file = new File(fileName);

		// file creation
		file.createNewFile();

	}

	/**
	 * This method appends the statistics of the current user into the csv file
	 * 
	 * @param history
	 * @param results
	 * @param timeDuration
	 * @throws IOException
	 */
	public void appendData(String history, String results, int timeDuration) throws IOException {

	}

	/**
	 * This method creates a new record/profile and appends into the csv file
	 * 
	 * @param username
	 */
	private void createProfile(String username) {
		String[] profile = { null, null, null, null };

		try {
			FileWriter csvwriter = new FileWriter(fileName, true);
			try (CSVWriter writer = new CSVWriter(csvwriter)) {
				// Check if username exists
				if (searchUsername(username) == false) {
					this.currentUsername = username;
					usernameText.clear();
					usernameText.setPromptText("Hi, " + username);

					outputLabel.setText("Profile Created");
					outputLabel.setStyle("-fx-text-fill: green;");
					outputLabel.setOpacity(0.5);

					// create profile
					profile[0] = username;
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
			}

			csvwriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Boolean searchUsername(String username) throws IOException {
		Boolean flag = false;
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

			if ((username.equals(tempUsername) == true) && (username.equals(null)) == false) {
				flag = true;
			}

		}

		br.close();
		return flag;
	}

	private static void accessProfile(String[] currentRecord) throws IOException {
		String line;
		String username = currentRecord[0];

		// Check if username is valid
		if (searchUsername(username) == true) {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {
				// Check if current line contains the username to be found
				if (line.contains(username)) {
					currentRecord = line.split(",");
				}
			}

			br.close();
		} else {
			System.out.println("INVALID USERNAME");
		}

	}

	@FXML
	private void onCreate(ActionEvent event) throws IOException {
		String username = null;
		username = usernameText.getText();

		System.out.println(usernameText.getText().trim().isEmpty());

		if (usernameText.getText().trim().isEmpty()) {
			outputLabel.setText("The textbox is empty");
			outputLabel.setStyle("-fx-text-fill: red;");
			outputLabel.setOpacity(0.5);
		} else {
			createProfile(username);
		}

	}

	@FXML
	private void onLogin(ActionEvent event) throws IOException {
		String username = null;
		username = usernameText.getText();

		if (usernameText.getText().trim().isEmpty()) {
			outputLabel.setText("The textbox is empty");
			outputLabel.setStyle("-fx-text-fill: red;");
			outputLabel.setOpacity(0.5);
		} else {
			if (searchUsername(username) == false) {
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
	private void onBack(ActionEvent event) throws IOException {

	}
}