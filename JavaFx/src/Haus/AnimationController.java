package Haus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

//@Author Fahd;
public class AnimationController implements Initializable {


    @FXML
    public Button Kill;
	@FXML
	Canvas canvas;
	
	GraphicsContext gc;
	static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();

    Controller controller = new Controller();

    @FXML
    private void GetScene1() throws IOException {

        controller.HideWindow(Kill);

    }

    
	public static void runAnim(Map<?, ?> map)
	{
		System.out.println("Creating DrawableObjects");
		for(Object obj : map.keySet()) 
		{	
			nodes.add(new DrawableObject(obj));
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("Initializing anim");
		gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font("Consolas", 10));
		for(DrawableObject node : nodes)
		{
			gc.drawImage(node.image, node.x, node.y);
			gc.fillText(node.name, node.x, node.y);
		}
	}
	
}
