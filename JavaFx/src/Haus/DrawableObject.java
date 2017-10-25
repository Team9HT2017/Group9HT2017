package Haus;

import javafx.scene.image.Image;

import java.util.Random;

public class DrawableObject {
	
	public String name;
	public Image image;
	public int x, y;
	
	public DrawableObject(Object obj, int x, int y) {
		Random rand = new Random();
		this.x = rand.nextInt(x);
		this.y = rand.nextInt(y);
		name = obj.toString();
		image = new Image("/img/house.png");
	}
}
