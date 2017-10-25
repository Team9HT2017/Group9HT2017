package Haus;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import java.util.logging.Level;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;


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

                //Thread.sleep(5000);
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
