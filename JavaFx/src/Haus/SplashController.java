package Haus;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will handle loading page. This will be called to give time to
 * the application to start the server, receive the data and animate it.
 *
 * @author Fahd Debbiche
 * @version 1.0
 *
 */

public class SplashController implements Initializable {

    @FXML
    private StackPane Stack;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new SplashScreen().start();
    }

    class SplashScreen extends Thread {

        @Override
        public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                try {

                    Thread.sleep(2000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;

                            try {
                                //root = FXMLLoader.load(getClass().getResource("AnimationPage.fxml"));
                                Stack.getChildren().clear();
                                Stack.getChildren().add(FXMLLoader.load(getClass().getResource("AnimationPage.fxml")));

                            } catch (IOException ex) {
                                Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                            }
//                            Scene scene = new Scene(root);
//                            Stage stage = new Stage();
//                            stage.setScene(scene);
//                            stage.show();
//                            Stack.getScene().getWindow().hide();
                        }
                    });

                } catch (InterruptedException e) {

                    Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, e);

                }
                Thread.currentThread().interrupt();
            }
        }
    }
}
