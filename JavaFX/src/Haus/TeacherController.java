package Haus;

/**
 * This class will handle the teacher's interface, where he/she can
 * upload a diagram into the system, get the Class identification
 * and open a class for his/hers students.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @author Laiz Figueroa
 * @version 1.1
 * Modification: Created this new class from the previous version Controller by Fahd.
 *
 */

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


public class TeacherController extends AnchorPane {

    @FXML
    public Button selectDiagramButton;

    @FXML
    public Button animateDiagramButton;

    @FXML
    public ListView<File> diagramPath;

    public static String toParse;

    private Stage stage = new Stage();

    public static boolean uploaded = false;

    /**
     * Method to give action to the Select Diagram button on the TeacherMain interface,
     * which by pressing it the user will have a system browser to look for the file
     * in .json and .txt format.
     *
     * @throws IOException
     */
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

    /**
     * Method to give action to the Animate Diagram button on the TeacherMain interface,
     * which by pressing it the user will be starting the server, sending the diagram
     * to it, and being redirected to the animation page.
     *
     * @throws IOException
     */
    @FXML
    private void createAnimation() throws IOException {

        try {

            System.out.println("Animation in progress");

            String ip = Inet4Address.getLocalHost().getHostAddress();
            TCPClient.main("teacher", ip);

            AnimationController.runAnim(Parser_v1.Parse2(toParse));
            showStage();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Loading Error");
            alert.setHeaderText(null);
            alert.setContentText("Please load the file to start the animation..");
            alert.showAndWait();
            System.out.println(e);

        }
    }
    
    private void showStage() throws IOException {

        FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent root =  fxmlloader.load();
        stage.setTitle("Loading Animation ...");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
