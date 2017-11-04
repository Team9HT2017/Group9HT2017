package Haus;

/**
 * Class to give the user a specific interface for his/her needs.
 * The user can create a class or join one by clicking the buttons,
 * then he/she will be redirected to a new interface.
 *
 * @author Laiz Figueroa
 * @version 1.0
 * 
 * 
 * @author Rema Salman
 * @version 1.1
 * Modification: Created the connection in the button actions with the error handling.
 *
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController {
	Main main = new Main();
	@FXML
	public Button teacherButton;

	@FXML
	public Button studentButton;

	@FXML
	AnchorPane first;

	/**
	 * Description: button action, which change the UserSelection anchorPane into
	 * the teacher's anchorePane.
	 * 
	 */
	@FXML
	private void ChangeToTeacherMain() {
		try {
			// adding TeacherMain anchorPane instead of the UserSelection anchorPane
			first.getChildren().add(FXMLLoader.load(getClass().getResource("TeacherMain.fxml")));
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Loading Error");
			alert.setHeaderText(null);
			alert.setContentText("Something went wrong! please try again ..");
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	@FXML
	private void ChangeToStudentMain() {
		try {
			first.getChildren().add(FXMLLoader.load(getClass().getResource("StudentMain.fxml")));
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Loading Error");
			alert.setHeaderText(null);
			alert.setContentText("Something went wrong! please try again ..");
			alert.showAndWait();
			e.printStackTrace();
		}

	}
}
