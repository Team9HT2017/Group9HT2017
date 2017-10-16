package Haus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;

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
    public ListView JsonList;

    @FXML
    public Button Animate;
    
    public static String toParse;


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
                Parent Root50 =  fxmlloader.load();
                Stage stage =new Stage();
                stage.setTitle("Animation phase");
                stage.setScene(new Scene(Root50));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    @FXML
    private void HandleUploadAction() throws IOException {

        FileChooser json = new FileChooser();
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File  SelectedFile = json.showOpenDialog(null);
        if (SelectedFile != null ) {
           // JsonList.getItems().add(SelectedFile.getCanonicalFile()); // replaced with my new function below
            toParse = new Scanner(SelectedFile).useDelimiter("\\Z").next();
          

        } else {
            System.out.println("File is not valid");
        }

    }

    @FXML
    public void HandleButton () {
        System.out.println("launch the server");


    }
    @FXML
    private void HandleAnimation() throws IOException {

        System.out.println("animation in progress");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}








