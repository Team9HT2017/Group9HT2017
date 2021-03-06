package MVC.Controllers;

import MVC.Models.NetworkHandlers.TCPClient;
import MVC.Models.AnimationObject.DrawableObject;
import MVC.Controllers.AnimationController;
import MVC.Controllers.UserController;
import MVC.Models.Parser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Inet4Address;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.util.Pair;

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
    public Button backButton;

    @FXML
    public ListView<File> diagramPath;

    @FXML
    AnchorPane teacherPane;

    @FXML
    private ProgressBar progressBarTeacher;

    @FXML
    private Label IPServerTeacher;

    UserController userController = new UserController();
    public static String toParse;
    public static String toParseC;
    public static String toParseD;
    public static boolean uploaded = false;
    public static Alert alert;
    public static String user;
    public static String map;
    private Map classDiag = new HashMap<>();
    private Map deploymentDiag = new HashMap<>();
    private Map sequenceDiag = new HashMap<>();

    /**
     * Method to give action to the Select Diagram button on the TeacherMain
     * interface, which by pressing it the user will have a system browser to look
     * for the file in .json and .txt format.
     *
     * @throws IOException
     */
    @FXML
    private void selectDiagram() throws IOException {
        // checking if the file is uploaded before animation starts
        String OS = System.getProperty("os.name").toLowerCase();
        String mac = "./runserver.sh";
        String windows = "./runwindows.sh";

        if (OS.contains("mac")) {
            runScript(mac);
        } else if (OS.contains("wind")) {
            runScript(windows);
        }

        // Section for: File chooser implementation
        FileChooser json = new FileChooser();
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));

        // creating an array for all selecting multiple files
        List<File> files = json.showOpenMultipleDialog(null);
        if (files != null) {
            try {
                // looping through the list for invoking the diagram's file
                for (File selectedFile : files) {
                    if (selectedFile != null) {
                        diagramPath.getItems().add(selectedFile.getCanonicalFile());
                        String identifier = new Scanner(selectedFile).useDelimiter("\\Z").next();

                        if (identifier.contains("sequence_diagram")) {
                            toParse = new Scanner(selectedFile).useDelimiter("\\Z").next();
                            sequenceDiag = Parser.Parse2(TeacherController.toParse, false);
                            classId();

                        } else if (identifier.contains("class_diagram")) {
                            toParseC = new Scanner(selectedFile).useDelimiter("\\Z").next();
                            classDiag = Parser.Parse2(TeacherController.toParseC, false);

                        } else {
                            toParseD = new Scanner(selectedFile).useDelimiter("\\Z").next();
                            deploymentDiag = Parser.Parse2(TeacherController.toParseD, false);
                        }
                        uploaded = true;

                    } else {
                        System.out.println("File is not valid");
                        userController.dialog("File missing", "You have not chosen a file" + "\n" + "Please try again ...", Alert.AlertType.WARNING);
                    }
                }

            } catch (Exception e) {
                System.out.println("File not chosen");
                e.printStackTrace();
            }
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

        if (uploaded) {

            try {
                user = "teacher";
                map = Arrays.deepToString(AnimationController.generateMap(sequenceDiag)) + "~" + getHouses() + "~" + Parser.parParsing(TeacherController.toParse).toString() + "~" + Parser.flows; //parParsing
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
                            teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/MVC/Views/AnimationPage.fxml")));

                        } catch (IOException ex) {
                            Logger.getLogger(AnimationController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            } catch (Exception e) {

                userController.dialog("ERROR HANDELING", "Animation got corrupted!", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
            // if the file is not already uploaded
        } else
            userController.dialog("FILE MISSING", "File not uploaded!", Alert.AlertType.WARNING);
    }

    /**
     * This method is get the class and deployment diagram for each house and for draw different houses,
     * based on that information.
     */
    private String getHouses() {
        String houses = "";
        for (DrawableObject node : AnimationController.nodes) {
            for (int e = 0; e < classDiag.keySet().size(); e++) { // add information from class diagram to each house

                if (node.name.split("\\|")[1].equals((classDiag).keySet().toArray()[e].toString())) {
                    node.name = node.name + classDiag.get((classDiag).keySet().toArray()[e]);
                }
            }
            for (int e = 0; e < deploymentDiag.keySet().size(); e++) { // add information from deployment diagram to each house
                System.out.println("here " + (deploymentDiag).keySet().toArray()[e].toString());

                if (node.name.split("\\|")[0].equals((deploymentDiag).keySet().toArray()[e].toString())) {
                    node.name = node.name + deploymentDiag.get((deploymentDiag).keySet().toArray()[e]);
                }
            }
            houses = houses + "{" + node.name.replaceAll(",", ";") + "," + node.x + "," + node.y + "}";
        }

        // Code to set the houses to be different based on the deployment diagram
        Image building;

        if (!deploymentDiag.isEmpty()) {
            Set<String> set = new HashSet<>();

            for (int i = 0; i < AnimationController.nodes.size(); i++) {
                set.add(AnimationController.nodes.get(i).name.split(Pattern.quote("Device: "))[1]);
            }
            AnimationController.deviceImages = new ArrayList<Pair<String, Image>>(set.size());

            int counter = 0;
            while (counter < set.size()) {

                if (counter == 0) {
                    building = new Image("MVC/Content/img/apartmentbuilding.png");
                } else if (counter == 1) {
                    building = new Image("MVC/Content/img/school.png");
                } else {
                    building = new Image("MVC/Content/img/house.png");
                }

                AnimationController.deviceImages.add(new Pair<String, Image>(set.toArray()[counter].toString(), building));
                counter++;
            }
            for (DrawableObject node : AnimationController.nodes) {
                node.checkDevice();
            }
        } else {
            for (DrawableObject node : AnimationController.nodes) {
                node.image = new Image("MVC/Content/img/house.png");
            }
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
            backButton.disableProperty();
            backButton.disabledProperty();
            userController.dialog("FILE UPLOADED", "You have already chosen a file to be animated", Alert.AlertType.WARNING);
        } else {
            try {
                teacherPane.getChildren().clear();
                teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/MVC/Views/UserSelection.fxml")));

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

    public static void runScript(String server) {

        File file = new File(".");
        for (String fileNames : file.list())
            System.out.println(fileNames);
        Thread one = new Thread(() -> {

            try {

                ProcessBuilder pb = new ProcessBuilder(server, "arg1", "arg2");
                pb.inheritIO();
                Process process = pb.start();

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
            } catch (InterruptedException v) {
                System.out.println(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        one.start();
    }
}