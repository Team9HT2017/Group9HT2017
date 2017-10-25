package Haus;

<<<<<<< HEAD
=======

>>>>>>> ddbbaaabfc2e7b04cbed6083e0124be9d8361bdf
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.InetAddress;
import java.net.UnknownHostException;
<<<<<<< HEAD
import java.io.IOException;

=======
import java.util.Map;
import java.io.IOException;


>>>>>>> ddbbaaabfc2e7b04cbed6083e0124be9d8361bdf
public class Main extends Application {


    public static void main(String[] args) {


        launch(args);

        Map<?, ?> t1 = Parser_v1.Parse2(Controller.toParse);
        System.out.println(t1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent Root = FXMLLoader.load(getClass().getResource("FrontPage.fxml"));
        primaryStage.setTitle("Sample");
        Scene scene = new Scene(Root, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
    public void getIP(TextField text) throws UnknownHostException {
        text.setText(String.valueOf(InetAddress.getLocalHost()));

    }


}
