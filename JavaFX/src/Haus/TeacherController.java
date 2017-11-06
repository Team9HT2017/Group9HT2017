package Haus;

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
 */

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
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
	public Button backButton;

	@FXML
	public ListView<File> diagramPath;

	@FXML
	AnchorPane teacherPane;

	public static String toParse;
	private Stage stage = new Stage();
	public static boolean uploaded = false;

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
			}
		} catch (Exception e) {
			loadingAlert("You have already selected a file!");
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
		if (uploaded) {
			try {
				System.out.println("Animation in progress");

				String ip = Inet4Address.getLocalHost().getHostAddress();
				TCPClient.main("teacher", ip);

				AnimationController.runAnim(Parser_v1.Parse2(toParse));
				showStage();

			} catch (Exception e) {
				loadingAlert("Animation got corrupted!");
				System.out.println(e);
			}
			// if the file is not already uploaded
		} else
			loadingAlert("File not uploaded!");
	}

	private void showStage() throws IOException {

		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("Splash.fxml"));
		Parent root = fxmlloader.load();
		stage.setTitle("Loading Animation ...");
		stage.setScene(new Scene(root));
		stage.show();

	}

	/**
	 * Method for going back to the first page, in case no
	 * 
	 * @throws IOException
	 *
	 */
	@FXML
	private void backButton() throws IOException {
		if (uploaded) {
			backButton.disabledProperty();
			loadingAlert("You have already chosen a file to be animated");
		} else {
			try {
				// backButton.disabledProperty();
				teacherPane.getChildren().clear();
				teacherPane.getChildren().add(FXMLLoader.load(getClass().getResource("UserSelection.fxml")));

			} catch (Exception e) {
				loadingAlert("You have already chosen a file to be animated");
				System.out.println(e);
			}
		}
	}

	/**
	 * Method to load a pop up a dialog to warn the user about loading problems.
	 *
	 * @param msg
	 *
	 */
	private void loadingAlert(String msg) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Loading Error");
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}

    private void classId() throws Exception {

        String ip  = Inet4Address.getLocalHost().getHostAddress();
        //TCPClient.main("teacher", ip);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WELCOME");
        alert.setHeaderText("Your class number is: ");
        alert.setContentText(ip);
        alert.showAndWait();
    }

}
