package Haus;

/**
 * Class to give the student a specific interface for his/her to connect to the
 * animation by inputting the class identification.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @author Laiz Figueroa and Rema Salman
 * @version 1.1
 * Modification - Laiz: Created this new class from the previous version Controller by Fahd.
 * Modification - Rema: Created the connection in the button actions with the error handling.
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class StudentController extends AnchorPane {

    @FXML
    public Button animateButton;

    @FXML
    public static TextField classID;

    @FXML
    AnchorPane studentPane;

    @FXML
    public Button backButton;

    /**
     * method to inform the student whether the teacher uploaded the file or not
     * In other words whether a server to connect to is launched or not if it is the case
     * then the student should input the right ip address to connect to
     *
     * @throws IOException
     */

	@FXML
	private void HandleAnimation() throws IOException {


		if ( !TeacherController.uploaded) {
			try {

				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Validate Class");
				alert.setHeaderText(null);
				alert.setContentText("No Server to join");
				alert.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else  {
			if  (classID.getText().isEmpty()) {

				try {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Validate Class");
					alert.setHeaderText(null);
					alert.setContentText("Type the class identification, provided by the teacher");
					alert.showAndWait();
				} catch (Exception e) {
					e.printStackTrace();

				}

			}  else {
					showStage();
					classID.clear();
				}
		}
	}

    /**
     * Method for going back to the first page, in case no
     *
     * @throws IOException
     */
    @FXML
    private void backButton() throws IOException {
        try {
            studentPane.getChildren().clear();
            studentPane.getChildren().add(FXMLLoader.load(getClass().getResource("UserSelection.fxml")));
        } catch (Exception e) {
            loadingAlert("You have already chosen a file to be animated");
            System.out.println(e);
        }
    }

    private void showStage() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent root = fxmlloader.load();
        Stage stage = new Stage();
        stage.setTitle("Loading Animation ...");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Method to load a pop up a dialog to warn the user about loading problems.
     *
     * @param msg
     *            represents the message displayed to the user
     *
     */
    private void loadingAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Loading Error");
        alert.setHeaderText(null);
        alert.setContentText(msg + "\n" + "Please try again ...");
        alert.showAndWait();
    }

}
