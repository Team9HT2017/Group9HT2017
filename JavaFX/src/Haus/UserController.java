package Haus;

/**
 * Class to give the user a specific interface for his/her needs.
 * The user can create a class or join one by clicking the buttons,
 * then he/she will be redirected to a new interface.
 *
 * @author Laiz Figueroa
 * @version 1.0
 * 
 * @editor Rema Salman
 * @version 1.1
 * Modification: Created the connection in the button actions with the error handling.
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class UserController {

	@FXML
	public Button teacherButton;

	@FXML
	public Button studentButton;

	@FXML
	AnchorPane first;

	/**
	 * Method to give action to the Create class button, which change the UserSelection anchorPane into
	 * the teacher's anchorPane.
	 * 
	 */
	@FXML
	private void changeToTeacherMain() {
		try {
			// adding TeacherMain anchorPane instead of the UserSelection anchorPane
			first.getChildren().add(FXMLLoader.load(getClass().getResource("TeacherMain.fxml")));

		} catch (IOException e) {
			loadingAlert();
			e.printStackTrace();
		}
	}
	/**
	 * Method to give action to the Join class button on the UserSelection interface,
	 * which by pressing it the user will be redirected to the student page.
	 *
	 */
	@FXML
	private void changeToStudentMain() {
		try {
			// adding StudentMain anchorPane instead of the UserSelection anchorPane
			first.getChildren().add(FXMLLoader.load(getClass().getResource("StudentMain.fxml")));

		} catch (IOException e) {
			loadingAlert();
			e.printStackTrace();
		}
	}

	/**
	 * Method to load a pop up a dialog to warn the user about loading problems.
	 *
	 */
	private void loadingAlert(){
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Loading Error");
		alert.setHeaderText(null);
		alert.setContentText("Something went wrong!" + "\n" + "Please try again ...");
		alert.showAndWait();
	}
}
