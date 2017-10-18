package Haus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//@Author Fahd;
public class AnimationController implements Initializable {


    @FXML
    public Button Kill;

    Controller controller = new Controller();


    @FXML
    private void GetScene1()  throws IOException {

        controller.HideWindow(Kill);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Animation Phase");

    }

}
