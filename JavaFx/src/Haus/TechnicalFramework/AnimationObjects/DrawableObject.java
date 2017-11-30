package Haus.TechnicalFramework.AnimationObjects;

import Haus.TechnicalFramework.Controllers.TeacherController;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class handles ...
 *
 * @author
 * @version 1.0
 *
 */

public class DrawableObject {

	public String name;
	public Image image;
	public int x, y;
	public int[] nodeDistances;

	public DrawableObject(Object obj, int x, int y) {
		Random rand = new Random();
		if (TeacherController.user == "teacher") {
			this.x = rand.nextInt(x - 2) + 1;
			this.y = rand.nextInt(y - 2) + 1;
		}
		else {
			this.x = x;
			this.y = y;
		}

		name = obj.toString();
		image = new Image("/Haus/DataStorage/img/house.png");
		connections = new ArrayList<DrawableObject>();
	}

	// math equation to calculate the distance between 2 nodes
	public double distance(DrawableObject drawableObject) {
		double res = 0;
		res = Math.sqrt(Math.pow(this.x - drawableObject.x, 2) + Math.pow(this.y - drawableObject.y, 2));
		return res;
	}

	public boolean isConnected = false;
	ArrayList<DrawableObject> connections; // an array for saving the connections

	// method for adding the connection to the list
	public void addConnection(DrawableObject d) {
		isConnected = true;
		connections.add(d);
	}

	// To check if the object is connected to the node in the argument or not
	public boolean isConnectedTo(DrawableObject d) {
		if (connections.size() == 0)
			return false;
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).name.equals(d.name)) {
				return true;
			}
		}
		return false;
	}
}
