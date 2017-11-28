package Haus.TechnicalFramework.Controllers;

import Haus.NetworkHandlers.TCPClient;
import Haus.TechnicalFramework.DataHandler.Parser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.*;
import java.net.Inet4Address;
import java.util.Arrays;
import java.util.Scanner;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

	public static String toParse;
	private Stage stage = new Stage();
	public static boolean uploaded = false;
    UserController userController = new UserController();
    public static Alert alert;
    
    public static String user;

    public static String map;


	/**
	 * Method to give action to the Select Diagram button on the TeacherMain
	 * interface, which by pressing it the user will have a system browser to look
	 * for the file in .json and .txt format.
	 *
	 * @throws IOException
	 *
	 */
	@FXML
	private void selectDiagram() throws IOException {
		try {
			FileChooser json = new FileChooser();
			json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
			json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));
			File SelectedFile = json.showOpenDialog(null);

			if (SelectedFile != null) {
				diagramPath.getItems().add(SelectedFile.getCanonicalFile());
				toParse = new Scanner(SelectedFile).useDelimiter("\\Z").next();
				uploaded = true;
				classId();

			} else {
				System.out.println("File is not valid");
				
				userController.dialog("File missing","You have not chosen a file"+"\n" + "Please try again ...");
			}
		} catch (Exception e) {
			System.out.println(e);

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
        String OS = System.getProperty("os.name").toLowerCase();
        String mac= "./runserver.sh";
        String windows="./runwindows.sh";
		if (uploaded) {
//		    if (OS.contains("mac"))
//                runScript(mac);
//
//            else if (OS.contains("wind")) {
//                runScript(windows);
//
//            }


			try {
                map = Arrays.deepToString(AnimationController.runAnim(Parser.Parse2(TeacherController.toParse, false)))+ "~"+ Parser.Parse2(TeacherController.toParse,false).toString()+"~"+Parser.ParseInorder(TeacherController.toParse).toString();
                progressBarTeacher.setVisible(true);
                IPServerTeacher.setVisible(true);
			    inProgressBar();
				System.out.println("Animation in progress");
				String ip = Inet4Address.getLocalHost().getHostAddress();
                user = "teacher";
                TCPClient.main(user, ip, map);
				//AnimationController.runAnim(Parser.Parse2(toParse,false));

				//showStage();
                diagramPath.getItems().clear();
            	
				// Showing the Splash(loading page)
				teacherPane.getChildren().clear();
                System.out.println(" this is executed");
				teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/AnimationPage.fxml")));
                System.out.println("Not executed");
			} catch (Exception e) {
				userController.dialog("ERROR HANDELING", "Animation got corrupted!");
				e.printStackTrace();
			}
			// if the file is not already uploaded
		} else
			userController.dialog("FILE MISSING", "File not uploaded!");
	}

	private void showStage() throws IOException {


		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../../PresentationUI/FXML/Splash.fxml"));

		Parent root = fxmlloader.load();
		stage.setTitle("Loading Animation ...");
		stage.setScene(new Scene(root));
		stage.show();

	}

	/**
	 * Method for going back to the first page, in case no file has been uploaded
	 * 
	 * @throws IOException
	 *
	 */
	@FXML
	private void backButton() throws IOException {
		if (uploaded ) {
			backButton.disabledProperty();
			userController.dialog("FILE UPLOADED", "You have already chosen a file to be animated");
		} else {
			try {
				// backButton.disabledProperty();
				teacherPane.getChildren().clear();
				teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("../../PresentationUI/UserSelection.fxml")));

			} catch (Exception e) {				
				System.out.println(e);
			}
		}
	}

	/**
	 * Method to load a pop up dialog to warn the user about loading problems.
	 *
	 * @param title
	 *            string represents the dialog title
	 * @param msg
	 *            string represents the message of the error or a notification for
	 *            the user
	 */
	private void dialog(String title, String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
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
		alert.setContentText("Your class number is: " + ip);
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
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
		System.out.println(fileNames);
		Thread one = new Thread(() -> {

                try {

                    ProcessBuilder pb = new ProcessBuilder(server, "arg1", "arg2");
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
                    System.out.println(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }

		});

		one.start();

	}
}
