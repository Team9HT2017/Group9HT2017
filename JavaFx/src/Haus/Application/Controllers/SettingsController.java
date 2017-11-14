package Haus.Application.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    /**
     * Method to add functionality to the Apply button, which will repaint the
     * canvas depending on the selection done by the user in the sliders.
     *
     * @throws Exception
     *
     */
    @FXML
    private void applySettingsSelection() throws Exception {
        Stage stage = (Stage) applyButton.getScene().getWindow();

        if (houseNameSlider.getValue() == 1) {
            names = 1;
        }

        else {
            //To close the new window
            stage.close();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/AnimationPage.fxml"));
        Parent root = (Parent) loader.load();
        stage.setTitle("Settings");
        stage.setScene(new Scene(root));
        stage.setX(0);
        stage.setY(10);
        stage.show();
        
        TeacherController.uploaded = false;
        AnimationController controller = loader.getController();

        controller.cleanAnimationCanvas();
        controller.animate();


        names = 0;

    }
}