package Haus.Application.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

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

    public static int names = 0;

    public static int color = 0;

    /**
     * Method to add functionality to the Apply button, which will repaint the
     * canvas depending on the selection done by the user in the sliders.
     *
     * @throws IOException
     *
     */
    @FXML
    private void applySettingsSelection() throws IOException {
        Stage stage = (Stage) applyButton.getScene().getWindow();

        if (houseNameSlider.getValue() == 1) {
            names = 1;
        }
        if(colorblindSlider.getValue() == 1){
            color = 1;
        }

        else {
            //To close the new window
            stage.close();
        }

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../FXML/AnimationPage.fxml"));
        Parent root = (Parent) loader.load();
        AnimationController controller = loader.getController();
        //Just to test
        System.out.println(names + " N " + color + " C " + " parent");
        controller.cleanAnimationCanvas();
        controller.animate();
        //Just to test
        System.out.println(names + " N " + color + " C " + " parent");

        names = 0;
        color = 0;
        //To close the new window
        stage.close();


    }
}