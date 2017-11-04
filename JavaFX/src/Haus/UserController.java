package Haus;

/**
 * Class to give the user an specific interface for his/her needs.
 * The user can create a class or join one by clicking the buttons,
 * then he/seh will be redirected to a new interface.
 *
 * @author Laiz Figueroa
 * @version 1.0
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    public Button teacherButton;

    @FXML
    public Button studentButton;

    private Stage stage = new Stage();

    private void showStage(String fxml) throws IOException {

        FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource(fxml));
        Parent root =  fxmlloader.load();
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }
    @FXML
    private void ChangeToTeacherMain() throws IOException {
        showStage("TeacherMain.fxml");

    }
    @FXML
    private void ChangeToStudentMain() throws IOException {
        showStage("StudentMain.fxml");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
