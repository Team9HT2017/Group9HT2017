package Haus.TechnicalFramework.Controllers;

import Haus.NetworkHandlers.TCPClient;
import Haus.TechnicalFramework.AnimationObjects.DrawableObject;
import Haus.TechnicalFramework.DataHandler.Parser;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.*;
import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class will handle the teacher's interface, where he/she can
 * upload a diagram into the system, get the Class identification
 * and open a class for his/hers students.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @author Laiz Figueroa and Rema Salman
 * @version 1.1
 * Modification - Laiz: Created this new class from the previous version Controller by Fahd.
 * Modification - Rema: Handling the alerts in case of errors existence;
 * 				 		Checking if the file uploaded before pressing (starting) animate;
 *				 		Adding the backButton with an icon and its functionalities.
 *
 * @author Laiz Figueroa
 * @version 1.2
 * Modification - Integrated the old Splash class code into this class.
 *
 */

public class TeacherController extends AnchorPane {

    @FXML
    public Button selectDiagramButton;

    @FXML
    public Button animateDiagramButton;

    @FXML
    private Button backButton;

    @FXML
    private ListView<File> diagramPath;

    @FXML
    private AnchorPane teacherPane;

    @FXML
    private ProgressBar progressBarTeacher;

    @FXML
    private Label IPServerTeacher;

    UserController userController = new UserController();
    public static String toParse;
    public static boolean uploaded = false;
    public static Alert alert;
    public static String user;
    public static String map;

    /**
     * Method to give action to the Select Diagram button on the TeacherMain
     * interface, which by pressing it the user will have a system browser to look
     * for the file in .json and .txt format.
     *
     * @throws IOException
     */
    @FXML
    private void selectDiagram() throws IOException {
        String OS = System.getProperty("os.name").toLowerCase();
        String mac = "./runserver.sh";
        String windows = "./runwindows.sh";

        // detecting which operational system the user has to run the script for the server
//		    if (OS.contains("mac"))
//                runScript(mac);
//
//            else  (OS.contains("wind")) {
//               runScript(windows);
//
//            }

        try {
            FileChooser json = new FileChooser();
            json.setTitle("Open Resource File");
            json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text and Json Files", "*.json", "*.txt"));
            File selectedFile = json.showOpenDialog(null);

            if (selectedFile != null) {
                diagramPath.getItems().add(selectedFile.getCanonicalFile());
                toParse = new Scanner(selectedFile).useDelimiter("\\Z").next();
                uploaded = true;
                classId();

            } else {
                System.out.println("File is not valid");

                userController.dialog("File missing", "You have not chosen a file" + "\n" + "Please try again ...");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Method to give action to the Animate Diagram button on the TeacherMain
     * interface, which by pressing it the user will be starting the server, sending
     * the diagram to it, and being redirected to the animation page. but the file
     * should be chosen otherwise an alert message will pop-up for choosing the file
     * first
     *
     * @throws IOException
     */
    @FXML
    private void createAnimation() throws IOException {


        // checking if the file is uploaded before animation starts
        if (uploaded) {

                try {
                    user = "teacher";
                    map = Arrays.deepToString(AnimationController.generateMap(Parser.Parse2(TeacherController.toParse, false))) + "~" + getHouses() + "~" + Parser.ParseInorder(TeacherController.toParse).toString();
                    progressBarTeacher.setVisible(true);
                    IPServerTeacher.setVisible(true);
                    inProgressBar();
                    System.out.println("Animation in progress");
                    String ip = Inet4Address.getLocalHost().getHostAddress();
                    TCPClient.main(user, ip, map);

                    diagramPath.getItems().clear();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                teacherPane.getChildren().clear();
                                teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/AnimationPage.fxml")));

                            } catch (IOException ex) {
                                Logger.getLogger(AnimationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });

                } catch (Exception e) {

                    userController.dialog("ERROR HANDELING", "Animation got corrupted!");
                    e.printStackTrace();
                }
                // if the file is not already uploaded display a message to the user
        } else
            userController.dialog("FILE MISSING", "File not uploaded!");
    }

    /**
     * Method for set the houses with the username and its position on the grid
     *
     */
    private String getHouses() {
        String houses = "";
        for (DrawableObject node : AnimationController.nodes) {
            houses = houses + "{" + node.name + "," + node.x + "," + node.y + "}";
        }
        return houses;
    }

    /**
     * Method for going back to the first page, in case no file has been uploaded
     *
     * @throws IOException
     */
    @FXML
    private void backButton() throws IOException {
        if (uploaded) {
            backButton.disabledProperty();
            userController.dialog("FILE UPLOADED", "You have already chosen a file to be animated");
        } else {
            try {
                teacherPane.getChildren().clear();
                teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/UserSelection.fxml")));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to load a pop up dialog to provide the class number (IP) to the teacher.
     *
     * @throws Exception
     */
    private void classId() throws Exception {

        String ip = Inet4Address.getLocalHost().getHostAddress();
        TCPClient.main("teacher", ip, "");

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WELCOME");
        alert.setHeaderText(null);
        alert.setContentText("Your classroom ID is: " + ip);
        alert.initModality(Modality.NONE); // To enable the user to navigate to other windows
        alert.setX(900);
        alert.setY(20);
        alert.setResizable(false);
        alert.show();

    }

    /**
     * This method for updating the progress bar contains gradually according the
     * given sleep time.
     */
    private void inProgressBar() {
        double p = progressBarTeacher.getProgress();
        // Updating the progress in the bar
        for (double i = p; i <= 10; i++) {
            progressBarTeacher.setProgress(i + 0.1);
        }
    }

    /**
     * Method to run the Script responsible for running the server in a separated process.
     **/

    public void runScript(String server) {
        // String scriptName = "/usr/bin/open -a Terminal
        File file = new File(".");
        for (String fileNames : file.list())
            if (fileNames.startsWith("ehaus-0.1.0")) {
                server="./runServerfolder.sh";
                System.out.println(fileNames);

            }

        String finalServer = server;
        Thread one = new Thread(() -> {

            try {

                ProcessBuilder pb = new ProcessBuilder(finalServer, "arg1", "arg2");
                pb.inheritIO();
                Process process = pb.start();

                // InputStream input = process.getInputStream();
                // System.out.println(input);
                int exitValue = process.waitFor();

                if (exitValue != 0) {
                    // check for errors
                    new BufferedInputStream(process.getErrorStream());
                    throw new RuntimeException("execution of script failed!");

                }

                File log = new File("log");
                pb.redirectErrorStream(true);
                pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));

                OutputStream output = process.getOutputStream();
                System.out.println(output.toString());
                System.out.println("Nope, it doesnt...again.");
            } catch (InterruptedException v) {
                v.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        one.start();

    }
}
