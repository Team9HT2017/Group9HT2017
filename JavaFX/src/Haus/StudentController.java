package Haus;

/**
 *
 * Created by LFigueroa on 04/11/17.
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    public Button animateButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void HandleAnimation() throws IOException {

        try {

            System.out.println("animation in progress");

            String teacherIp  = Controller.IP.getText();
            TCPClient.main("student", teacherIp);

            AnimationController.runAnim(Parser_v1.Parse2(Controller.toParse));
            //showstage();
            //yourProcess();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Class connection fails");
            alert.setHeaderText(null);
            alert.setContentText("Connect to a class to go to animation");
            alert.showAndWait();
            System.out.println(e);

        }

    }

}
