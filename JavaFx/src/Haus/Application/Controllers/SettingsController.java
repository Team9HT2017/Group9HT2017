package Haus.Application.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import java.io.IOException;
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
    public Slider colorblindSlider;

    @FXML
    public Button applyButton;

    public static int names;

    Stage stage = (Stage) applyButton.getScene().getWindow();

    @FXML
    private void applySettingsSelection() throws IOException {

        if (houseNameSlider.getValue() == 1 || colorblindSlider.getValue() == 1) {
            AnimationController.cleanAnimationCanvas();
            names = 1;
            //AnimationController.runAnim();

        } else
            stage.close();
    }

}
