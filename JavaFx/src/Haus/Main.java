package Haus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * Class to start the application, sets its basic characteristics.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @editor Laiz Figueroa
 * @version 1.1
 * Modifications: Added comments, Dialog when closing the application more personalised,
 * different size management for the application.
 *
 */

public class Main extends Application {


    public static void main(String[] args) {


        launch(args);

        Map<?, ?> t1 = Parser_v1.Parse2(TeacherController.toParse);
        System.out.println(t1);

        ArrayList<Object> result =Parser_v1.ParseInorder(TeacherController.toParse) ;

        // for testing purposes
        for (int j = 0; j <result.size(); j++) {
            System.out.println(result.get(j));

        }
        // for testing purposes
        for (int j = 0; j < t1.keySet().toArray().length; j++) {
            System.out.println(t1.get(t1.keySet().toArray()[j]));

        }
        System.out.println(t1.keySet());
        System.out.println(t1.entrySet());
        
    }
    /**
     * Method to start the front page and define its size characteristics.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent Root = FXMLLoader.load(getClass().getResource("UserSelection.fxml"));
        primaryStage.setTitle("Haus Diagram Simulator");
        //To get the application user's screen size and pass it to the set the application size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(Root, screenBounds.getHeight() + 200, screenBounds.getHeight());
        primaryStage.setX(0);
        primaryStage.setY(10);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e ->  {
            e.consume();
            closeProgram(primaryStage);
        });
    }

    /**
     * Method to get the teacher's IP to display to the users in order to
     * connect to the server.
     *
     * @param text
     *
     */
    public void getIP(TextField text) throws UnknownHostException {
        text.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
    }

    /**
     * Method to display a dialog when the user try to close the application,
     * then it asks if they user is aware of what he is doing. This will close
     * safely the application.
     *
     * @param stage
     *
     */
    private void closeProgram(Stage stage) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Leaving so soon!");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to leave the application?");

        // To modify the kind of buttons we have on the dialog
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("No");
        // To add the buttons defined above to the dialog
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        // To perform the close application when yes is selected
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes){
            stage.close();
            // ... user chose Yes
        } else {
            // ... user chose No or closed the dialog
        }
    }
}
