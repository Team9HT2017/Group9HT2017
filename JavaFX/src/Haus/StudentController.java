package Haus;

/**
 *
 * Created by LFigueroa on 04/11/17.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    public Button animateButton;

    @FXML
    public static TextField classID;

    private Stage stage = new Stage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void HandleAnimation() throws IOException {

        if (classID.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate IP ");
            alert.setHeaderText(null);
            alert.setContentText("type the server IP address");
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
        stage = new Stage();
        stage.setTitle("Loading Animation ...");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
