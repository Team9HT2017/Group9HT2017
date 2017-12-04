package Haus.PresentationUI;

import Haus.NetworkHandlers.TCPListener;
import Haus.TechnicalFramework.Controllers.TeacherController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.transform.Result;

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
 *@editor Rema Salman
 * @version 1.2 Modifications: hidewindow for handling LeaveAnimation button's action and restarting the application  
 */

public class Main extends Application {

    Stage stage = new Stage();
    public static Scene scene;

    public static void main(String[] args) {
   
    	ExecutorService concurrent = Executors.newCachedThreadPool();

    			Future<Void> past = concurrent.submit(new Callable<Void>() {
    			    public Void call() throws Exception {
    			        TCPListener.listen();
    			        return null;
    			    }});
    			   launch(args);
    			try {
					past.get();
				} catch (Exception e) {
					System.out.println("Concurrency failed");
					e.printStackTrace();
				} 
     

/*
        Map<?, ?> t1 = Parser.Parse2(TeacherController.toParse);
        System.out.println(t1);

        ArrayList <ArrayList<Object>> result = Parser.ParseInorder(TeacherController.toParse) ;

        // for testing purposes
        for (int j = 0; j < result.size(); j++) {
            System.out.println(result.get(j));

        }
        // for testing purposes
        for (int j = 0; j < t1.keySet().toArray().length; j++) {
            System.out.println(t1.get(t1.keySet().toArray()[j]));

        }
        System.out.println(t1.keySet());
        System.out.println(t1.entrySet());
*/
    }

    /**
     * Method to start the front page.
     *
     * @param primaryStage
     *
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent Root = FXMLLoader.load(getClass().getResource("../PresentationUI/FXML/UserSelection.fxml"));
        // To get the application user's screen size and pass it to the set the
        // application size
        getScene(Root, primaryStage);
    }

    /**
     * Method to define the application window's characteristics. And to close the window when asked.
     *
     * @param root
     * @param primaryStage
     *
     */
    public static void getScene(Parent root, Stage primaryStage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        scene = new Scene(root, screenBounds.getHeight() + 200, screenBounds.getHeight());
        primaryStage.setTitle("Haus Diagram Simulator");
        primaryStage.setX(0);
        primaryStage.setY(10);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(primaryStage);
            //TeacherController.alert.close();
        });
    }

//    /**
//     * Method to get the teacher's IP to display to the users in order to connect to
//     * the server.
//     *
//     * @param text
//     *
//     */
//    public void getIP(TextField text) throws UnknownHostException {
//        text.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
//    }

    /**
     * Method to display a dialog when the user try to close the application, then
     * it asks if they user is aware of what he is doing. This will close safely the
     * application.
     *
     * @param stage
     *
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
            TeacherController.alert.close();
            // ... user chose Yes
        } else {
            // ... user chose No or closed the dialog
        }
    }

    /**
     * Method to hide the animation window and restarting the application from the
     * user selection page
     *
     * @param leaveAnimation
     *            the button is passed as an argument to be handled
     *            as its action
     * @throws Exception
     *
     */
    public void hideWindow(Button leaveAnimation) throws Exception {
        this.stage = (Stage) leaveAnimation.getScene().getWindow();
        // for restarting the application
        stage.close();
        start(stage);

    }
}