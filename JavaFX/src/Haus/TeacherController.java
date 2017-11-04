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
import java.net.Inet4Address;
import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {

    @FXML
    public Button selectDiagramButton;

    @FXML
    public Button animateDiagramButton;

    @FXML
    private void HandleAnimation() throws IOException {

        try {

            System.out.println("animation in progress");

            String ip = Inet4Address.getLocalHost().getHostAddress();
            TCPClient.main("teacher", ip);

            AnimationController.runAnim(Parser_v1.Parse2(Controller.toParse));
            //showstage();
            //yourProcess();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Load a file ");
            alert.setHeaderText(null);
            alert.setContentText("Load the file to animate");
            alert.showAndWait();
            System.out.println(e);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
