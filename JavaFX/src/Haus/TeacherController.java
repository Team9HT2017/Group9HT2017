package Haus;

/**
 * This class will handle the teacher's interface, where he/she can
 * upload a diagram into the system, get the Class identification
 * and open a class for his/hers students.
 *
 * @author
 * @version 1.0
 *
 * @author Laiz Figueroa
 * @version 1.1
 * Modification: Created two new classes from the previous version.
 *
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class TeacherController extends AnchorPane {

    @FXML
    public Button selectDiagramButton;

    @FXML
    public Button animateDiagramButton;

    @FXML
    public ListView<File> diagramPath;

    public static String toParse;

   // private Stage stage = new Stage();

    public static boolean uploaded = false;

    @FXML
    private void selectDiagram() throws Exception {

        FileChooser json = new FileChooser();
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File SelectedFile = json.showOpenDialog(null);
        if (SelectedFile != null) {
            diagramPath.getItems().add(SelectedFile.getCanonicalFile());
            toParse = new Scanner(SelectedFile).useDelimiter("\\Z").next();
            uploaded = true;

        } else {
            System.out.println("File is not valid");
        }
    }

    @FXML
    private void createAnimation() throws IOException {

        try {

            System.out.println("Animation in progress");

            String ip = Inet4Address.getLocalHost().getHostAddress();
            TCPClient.main("teacher", ip);

            AnimationController.runAnim(Parser_v1.Parse2(toParse));
          //  showStage();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Loading Error");
            alert.setHeaderText(null);
            alert.setContentText("Please load the file to start the animation..");
            alert.showAndWait();
            System.out.println(e);

        }
    }
    
    
//    private void showStage() throws IOException {
//
//        FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource("Splash.fxml"));
//        Parent root =  fxmlloader.load();
//        stage.setTitle("Loading Animation ...");
//        stage.setScene(new Scene(root));
//        stage.show();
//    }

}
