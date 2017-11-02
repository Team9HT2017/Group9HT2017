package Haus;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller  implements Initializable {

    @FXML
    public Button uploadButton;

    @FXML
    public TextField IP;

    @FXML
    public Button Connect;

    @FXML
    public ListView<File> JsonList;

    @FXML
    public Button Animation;

    @FXML
    private TextField IPlocal;

    public static String toParse;

    public static boolean uploaded = false;


    public static String client;

    private Stage stage =new Stage();
    private Main main = new Main();

        @FXML
        private void HandleConnection() throws IOException {


            if (IP.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Validate IP ");
                alert.setHeaderText(null);
                alert.setContentText("type the server IP address");
                alert.showAndWait();
            } else {
                try {
                    if (uploaded)
                        showstage();
                        IP.clear();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    private void showstage() throws IOException {

        FXMLLoader fxmlloader =  new FXMLLoader(getClass().getResource("Splash.fxml"));
        Parent root =  fxmlloader.load();
        stage =new Stage();
        stage.setTitle("Animation phase");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void HideWindow(Button kill) {

        this.stage = (Stage) kill.getScene().getWindow();
        // do what you have to do
        stage.close();



    }

    @FXML
    private void HandleUploadAction() throws Exception {

        FileChooser json = new FileChooser();
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json Files", "*.json"));
        json.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        File  SelectedFile = json.showOpenDialog(null);
        if (SelectedFile != null ) {
            JsonList.getItems().add(SelectedFile.getCanonicalFile());
            toParse = new Scanner(SelectedFile).useDelimiter("\\Z").next();


            main.getIP(IPlocal);
            JsonList.getItems().add(SelectedFile.getCanonicalFile()); // replaced with my new function below
            toParse = new Scanner(SelectedFile).useDelimiter("\\Z").next();
            main.getIP(IPlocal);
            uploaded=true;

            String ip  = Inet4Address.getLocalHost().getHostAddress();
            TCPClient.main("teacher", ip);

        } else {
            System.out.println("File is not valid");
        }

    }

    @FXML
    private void HandleAnimation() throws IOException {

        try {

            System.out.println("animation in progress");

            client = "student";
            String teacherIp  = IP.getText();
            TCPClient.main("student", teacherIp);

            AnimationController.runAnim(Parser_v1.Parse2(toParse));
            showstage();
            yourProcess();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Load a file ");
            alert.setHeaderText(null);
            alert.setContentText("Load the file to animate");
            alert.showAndWait();
            System.out.println(e);

        }

    }


     public void yourProcess() {
     String scriptName = "/usr/bin/open -a Terminal  /Users/fahddebbiche/Desktop/Group9HT2017/JavaFX/src/runserver.sh";
     try {

     Runtime rt = Runtime.getRuntime();
     Process process = rt.exec(scriptName);

     BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

     String line=null;
     while((line=input.readLine()) != null) {
     System.out.println(line);
     }

     int exitVal = process.waitFor();
     System.out.println("Exited with error code "+exitVal);

     } catch(Exception e) {
     System.out.println(e.toString());
     e.printStackTrace();
     }
     }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("UPLOAD THE FILE");
    }
}



