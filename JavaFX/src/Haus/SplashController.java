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


//@Author Fahd ;

public class SplashController  implements Initializable {

    @FXML
    public StackPane stack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new SplashScreen().start();
    }

    public static class SplashScreen extends Thread {

        @Override
        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Thread.sleep(5000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;

                            try {
                                root = FXMLLoader.load(getClass().getResource("AnimationPage.fxml"));


                            } catch (IOException ex) {
                                Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();


                        }
                    });


                } catch (InterruptedException e) {

                    Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, e);

                }
                Thread.currentThread().interrupt();


            }

        }


    }

    public void hideStack() {
        if (this.stack != null) {
            this.stack.getScene().getWindow().hide();
        }
    }


}
