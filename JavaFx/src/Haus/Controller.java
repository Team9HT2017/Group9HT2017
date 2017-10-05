package Haus;

import com.sun.tools.hat.internal.model.Root;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button uploadButton;
    @FXML
    public TextField IP;
    @FXML
    public Button connect;
    @FXML
    private GridPane Board;

    @FXML
    private void HandleButtonAction() throws IOException {

        if (IP.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate IP ");
            alert.setHeaderText(null);
            alert.setContentText("type the server IP address");
            alert.showAndWait();
        } else {
            try {
                FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource("AnimationPage.fxml"));
                Parent Root50 = (Parent) fxmlloader.load();
                Stage stage =new Stage();
                stage.setTitle("Animation phase");
                stage.setScene(new Scene(Root50));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
        public void HandleButton () {
            System.out.println("launch the server");


        }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
