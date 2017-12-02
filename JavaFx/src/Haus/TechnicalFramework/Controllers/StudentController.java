package Haus.TechnicalFramework.Controllers;

import Haus.NetworkHandlers.TCPClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 * Modification - Rema: Created the connection in the button actions with the error handling
 * 						Notifying the user in case of disconnecting with the server
 * 						Creating the userController object to be used in the dialogs for reducing duplications.
 *
 * @author Laiz Figueroa
 * @version 1.2
 * Modification - Integrated the old Splash class code into this class.
 *
 */

public class StudentController extends AnchorPane {


    public static String toMessageLog;

    @FXML
    public Button animateButton;

    @FXML
    private TextField classID1;

    @FXML
    AnchorPane studentPane;

    @FXML
    public Button backButton;

    @FXML
    private ProgressBar progressBarStudent;

    @FXML
    private Label IPServerStudent;

    UserController userController = new UserController();

    public static String[] topars;

    /**
     * method to inform the student whether the teacher uploaded the file or not In
     * other words whether a server to connect to is launched or not if it is the
     * case then the student should input the right ip address to connect to
     *
     * @throws IOException
     */

    @FXML
    private void HandleAnimation() throws IOException {

        progressBarStudent.setVisible(true);
        IPServerStudent.setVisible(true);
        inProgressBar();
        System.out.println(classID1);

        if (classID1.getText() == null || classID1.getText().isEmpty()) {
            try {
                userController.dialog("Missing Classroom ID",
                        "Type the class identification, " + "\n" + "provided by the teacher");
            } catch (Exception e) {
                e.printStackTrace();
                userController.dialog("Loading Error", "Something went wrong!" + "\n" + "Please try again ...");
            }

        } else {
            // changing pane into the animation
            System.out.println("animation in progress");

            try {
                //to request the information from the server
                topars = (TCPClient.main("student", classID1.getText(), "hi")).split("~");
                toMessageLog = topars[1];
                studentPane.getChildren().clear();
                studentPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/AnimationPage.fxml")));
                classID1.clear();

            } catch (Exception e) {
                System.out.println("No connection to the server");
                e.printStackTrace();
                // notification to the user in case of connection to server not completed
                userController.dialog("Loading Error",
                        "Connection to the class got corrupted" + "\n" + "Please try again ...");
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
            studentPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/UserSelection.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
            userController.dialog("Loading Error", "Something went wrong!" + "\n" + "Please try again ...");
        }
    }

    /**
     * This method for updating the progress bar contains gradually according the
     * given sleep time.
     */
    private void inProgressBar() {
        double p = progressBarStudent.getProgress();
        // Updating the progress in the bar
        for (double i = p; i <= 10; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            progressBarStudent.setProgress(i + 0.1);
        }
    }
}
