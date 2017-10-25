package Haus;

import java.util.Random;

import javafx.scene.image.Image;

public class DrawableObject {
	
	public String name;
	public Image image;
	public double x, y;
	
	public DrawableObject(Object obj) {
		Random rand = new Random();
		x = rand.nextInt(10) * 32;
		y = rand.nextInt(10) * 16;
		name = obj.toString();
		image = new Image("/img/house.png");
	}
}
