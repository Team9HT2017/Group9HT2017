package Haus.Application.Controllers;

import Haus.Application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * Class to add functionality to the Settings page.
 *
 * @author Laiz Figueroa
 * @version 1.0
 *
 */
public class SettingsController {

    @FXML
    public Slider houseNameSlider;

    @FXML
    public Button applyButton;

    public static int names = 0;

    @FXML
    private void applySettingsSelection() throws Exception {
        Stage stage = (Stage) applyButton.getScene().getWindow();

        if (houseNameSlider.getValue() == 1) {
            names = 1;
        } else {
            names =0;
            //To close the new window
            stage.close();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/AnimationPage.fxml"));
        Parent root = (Parent) loader.load();
        Main.getScene(root, stage);

        TeacherController.uploaded = false;
        names = 0;

    }
}