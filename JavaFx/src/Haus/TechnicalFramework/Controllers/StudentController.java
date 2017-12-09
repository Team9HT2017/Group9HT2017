package Haus.TechnicalFramework.Controllers;

import Haus.NetworkHandlers.TCPClient;
import Haus.TechnicalFramework.DataHandler.Parser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static String toMessageLog;


    /**
     * Method to input the classroom id (IP) by pressing Enter on the keyboard
     */
    @FXML
    private void textAction() {
        classID1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    handleAnimation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Method to input the classroom id (IP) by pressing the button
     */
    @FXML
    private void buttonAction() {
            try {
                handleAnimation();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Method to inform the student whether the teacher uploaded the file or not In
     * other words whether a server to connect to is launched or not if it is the
     * case then the student should input the right ip address to connect to
     *
     * @throws IOException
     */
    private void handleAnimation() throws IOException {

        System.out.println(classID1);

        if (classID1.getText() == null || classID1.getText().isEmpty()) {
            try {
                userController.dialog("Missing Classroom ID",
                        "Type the classroom ID provided by the teacher");
            } catch (Exception e) {
                e.printStackTrace();
                userController.dialog("Loading Error", "Something went wrong!" + "\n" + "Please try again ...");
            }

        } else {
            System.out.println("animation in progress");

            try {
                progressBarStudent.setVisible(true);
                IPServerStudent.setVisible(true);
                inProgressBar();
                //to request the information from the server
                topars = (TCPClient.main("student", classID1.getText(), "hi")).split("~");

                System.out.println("Topars= "+Arrays.toString(topars));
                toMessageLog = topars[1];
                String flows = topars[3];
                String [] tomap = flows.replaceAll("\\{", "").replaceAll("\\}", "").split(",");
                System.out.println("flow= "+Arrays.toString(tomap));
                
                Map<Integer,Integer> flow1 = new HashMap<Integer,Integer>();
                for (int i=0;i<tomap.length;i++){
                	String [] elem = tomap[i].split("=");
                	int one = Integer.parseInt(elem[0].trim());
                	int two = Integer.parseInt(elem[1].trim());
                	flow1.put(one, two);
                }
                System.out.println("flow1= "+flow1.toString());
                Parser.flows=flow1;

                //toMessageLog = topars[1];

                //to change to the animation page
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            studentPane.getChildren().clear();
                            studentPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/AnimationPage.fxml")));

                        } catch (IOException ex) {
                            Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                classID1.clear();
            } catch (Exception e) {
                System.out.println("No connection to the server");
                e.printStackTrace();
                // notification to the user in case of connection to server not completed
                userController.dialog("Loading Error",
                        "Connection to the classroom got corrupted" + "\n" + "Please try again ...");
            }
        }
    }

    /**
     * Method for going back to the first page
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
            progressBarStudent.setProgress(i + 0.1);
        }
    }
}
