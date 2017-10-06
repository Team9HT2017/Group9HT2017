package Haus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent Root = FXMLLoader.load(getClass().getResource("FrontPage.fxml"));
        primaryStage.setTitle("Haus");
        primaryStage.setScene(new Scene(Root, 970, 500));
        primaryStage.show();
    }


}