package Haus.TechnicalFramework.Controllers;

import Haus.PresentationUI.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    public CheckBox namesCheck;

    @FXML
    public Button applyButton;

    public static int names = 0;

    @FXML
    private void applySettingsSelection() throws Exception {
        Stage stage = (Stage) applyButton.getScene().getWindow();

        if (namesCheck.isSelected()) {
            names = 1;
        } else {
            names =0;
            //To close the new window
            stage.close();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../PresentationUI/FXML/AnimationPage.fxml"));
        Parent root = (Parent) loader.load();
        Main.getScene(root, stage);

        TeacherController.uploaded = false;
        names = 0;

    }
}