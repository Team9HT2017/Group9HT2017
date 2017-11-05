package Haus;

/**
 * Class to give the student a specific interface for his/her to connect to the
 * animation by inputting the class identification.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @author Laiz Figueroa
 * @version 1.1
 * Modification: Created this new class from the previous version Controller by Fahd.
 *
 * @editor Rema Salman
 * @version 1.2
 * Modification: Created the connection in the button actions with the error handling.
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
    private void HandleAnimation() throws IOException {

        if (classID.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Class");
            alert.setHeaderText(null);
            alert.setContentText("Type the class identification, provided by the teacher!");
            alert.showAndWait();

        } else {
            try {
                if (TeacherController.uploaded)
                    showStage();
                    classID.clear();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showStage() throws IOException {
        FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent root =  fxmlloader.load();
        Stage stage = new Stage();
        stage.setTitle("Loading Animation ...");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
