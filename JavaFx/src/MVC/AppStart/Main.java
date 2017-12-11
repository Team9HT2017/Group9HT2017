package MVC.AppStart;

import MVC.Controllers.TeacherController;
import MVC.Models.NetworkHandlers.TCPListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class to start the application, sets its basic characteristics.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 * @editor Laiz Figueroa
 * @version 1.1 Modifications: Added comments, Dialog when closing the
 *          application more personalised, different size management for the
 *          application.
 *
 * @editor Rema Salman
 * @version 1.2 Modifications: hidewindow for handling LeaveAnimation
 *          button's action and restarting the application
 */

public class Main extends Application {

    public static Scene scene;

    public static void main(String[] args) {

        ExecutorService concurrent = Executors.newCachedThreadPool(); // Executor to concurrently run the main flow of app AND listen to server

        Future<Void> past = concurrent.submit(new Callable<Void>() { // Listen to server in one thread
            public Void call() throws Exception {
                TCPListener.listen();
                return null;
            }
        });
        launch(args); // Run the main app in another
        try {
            past.get();
        } catch (Exception e) {
            System.out.println("Concurrency failed");
            e.printStackTrace();
        }
    }

    /**
     * Method to start the front page.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent Root = FXMLLoader.load(getClass().getResource("../Views/UserSelection.fxml"));
        getScene(Root, primaryStage);
    }

    /**
     * Method to define the application window's characteristics. And to close the window when asked.
     *
     * @param root
     * @param primaryStage
     */
    public static void getScene(Parent root, Stage primaryStage) {
        // To get the application user's screen size and pass it to the set the
        // application size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        scene = new Scene(root, screenBounds.getHeight() + 200, screenBounds.getHeight() * 0.94);
        primaryStage.setTitle("Haus Diagram Simulator");
        //primaryStage.getIcons().add(new Image("/Content/img/HAUSIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(primaryStage);
        });
    }

    /**
     * Method to display a dialog when the user try to close the application, then
     * it asks if they user is aware of what he is doing. This will close safely the
     * application.
     *
     * @param stage
     */
    private static void closeProgram(Stage stage) {

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
        if (result.get() == buttonTypeYes) {
            stage.close();
            TeacherController.alert.close(); // To close the pop-up window that gives the classroom id
            // ... user chose Yes
        } else {
            // ... user chose No or closed the dialog
        }
    }
}