package Haus;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.io.IOException;


public class Main extends Application {



    public static void main(String[] args) {
        launch(args);
        Parser_v1.Parse2(Controller.toParse);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent Root = FXMLLoader.load(getClass().getResource("FrontPage.fxml"));
        primaryStage.setTitle("Haus");
        Scene scene =new Scene(Root, 970, 500);
        primaryStage.setScene(scene);
        primaryStage.show();



    }
    public void getIP(TextField text) throws UnknownHostException {
        text.setText(String.valueOf(InetAddress.getLocalHost()));

    }




}