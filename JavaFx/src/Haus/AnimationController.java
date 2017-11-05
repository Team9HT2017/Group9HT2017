package Haus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.util.Pair;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

/**
 * This class will handle the animation page, where the user can
 * see the diagram animation, see the log created, do the settings
 * adjustments, and leave the animation.
 *
 * @author Leo Persson and Rema Salman
 * @version 1.0
 *
 */
public class AnimationController implements Initializable {

	@FXML
	public Button Kill;

	@FXML
	Canvas canvas;

	GraphicsContext gc;

	static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();

	static Character[][] grid;

	static ArrayList<Road> roads = new ArrayList<Road>();

	static double mapScale;

	Controller controller = new Controller();

	@FXML
	private void GetScene1() throws IOException {

		controller.HideWindow(Kill);

	}

	public static void runAnim(Map<?, ?> map)
	{
		mapScale = 3 * Math.pow((double) map.keySet().size(), -0.6)*2;
		int nodeNum = (int) (map.keySet().size() * mapScale);
		grid = new Character[nodeNum][nodeNum];
		Random rand = new Random();

		System.out.println("Creating DrawableObjects");
		for (Object obj : map.keySet()) {
			nodes.add(new DrawableObject(obj, nodeNum, nodeNum));
		}

		// Build 2d grid map ('G'rass)
		for (int i = 0; i < nodeNum; i++) {
			Arrays.fill(grid[i], 'G');
		}

		// Build 2d grid map ('R'oads)
		for(int i = 0; i < nodes.size() - 1; i++) {
			Road road = new Road(nodes.get(i), nodes.get(i + 1));
			//roads.add(road);
			int j = 0;
			for (Pair tile : road.segments[j]) {
				grid[(int) tile.getKey()][(int) tile.getValue()] = 'R';
				j++;
			}
		}

		for(int i = 1; i < nodeNum - 1; i++) {
			for (int j = 1; j < nodeNum - 1; j++) {
				if (grid[i + 1][j] == 'R' && (grid[i - 1][j] == 'R' || grid[i - 1][j] == 'T') && (grid[i][j + 1] == 'R' || grid[i][j + 1] == 'T') && (grid[i][j - 1] == 'R' || grid[i][j - 1] == 'T') && grid[i + 1][j + 1] == 'R' && (grid[i - 1][j - 1] == 'R' || grid[i - 1][j - 1] == 'T') && grid[i - 1][j + 1] == 'R' && grid[i + 1][j - 1] == 'R' && (grid[i][j] == 'R')) {
					grid[i][j] = 'T';
				}
			}
		}

		// Build 2d grid map ('H'ouse)
		for (DrawableObject node : nodes) {
			// For loop makes sure there are no houses sharing a diagonally adjacent
			// tilespace
			for (; grid[node.x - 1][node.y - 1] == 'H' || grid[node.x + 1][node.y - 1] == 'H'
					|| grid[node.x][node.y] == 'H' || grid[node.x - 1][node.y + 1] == 'H'
					|| grid[node.x + 1][node.y + 1] == 'H';) {
				node.x = rand.nextInt((nodeNum) - 2) + 1;
				node.y = rand.nextInt((nodeNum) - 2) + 1;

			}
			grid[node.x][node.y] = 'H';
		}

		// Print 2d char map to terminal for debugging purposes
		for (int i = 0; i < nodeNum; i++) {
			for (int j = 0; j < nodeNum; j++) {
				System.out.print(grid[j][i]);
			}
			System.out.println();
		}
	}

	// from 2D to an Isometric
	public Point twoDToIso(Point point) {
		Point tempPt = new Point(0, 0);
		tempPt.x = point.x - point.y + (int) canvas.getWidth() / 2 - 16;
		tempPt.y = (point.x + point.y) / 2 + (int) ((canvas.getHeight() / 2) - nodes.size() * (8 * mapScale));
		return (tempPt);
	}

	public void initialize(URL location, ResourceBundle resources) {
		canvas.setWidth((nodes.size() * 32) * mapScale + 80);
		canvas.setHeight((nodes.size() * 16) * mapScale + 80);
		int housenum = 0;
		DrawableObject node = nodes.get(0);
		System.out.println("Initializing anim");
		Image grass = new Image("/img/Isotile_grass.png");
		Image road = new Image("/img/Isotile_road.png");
		gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font("Consolas", 10));
		for (int i = 0; i < (int) (nodes.size() * mapScale); i++) {

			for (int j = 0; j < (int) (nodes.size() * mapScale); j++) {

				switch (grid[i][j]) {

					case 'G':
						gc.drawImage(grass, twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						break;

					case 'H':
						node = nodes.get(housenum);
						housenum++;
						gc.drawImage(node.image, twoDToIso(new Point(i * 16, j * 16)).x,
								twoDToIso(new Point(i * 16, j * 16)).y - 16);
						// gc.fillText(node.name, twoDToIso(new Point(i* 16, j * 16)).x, twoDToIso(new
						// Point(i* 16, j * 16)).y - 16);
						System.out.println(node.name);
						break;

					case 'R':
						if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R') {
							gc.drawImage(new Image("/img/Isotile_roadCross.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j - 1] == 'R' ) {
							gc.drawImage(new Image("/img/Isotile_roadT-Y.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R' ) {
							gc.drawImage(new Image("/img/Isotile_roadT+Y.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R' ) {
							gc.drawImage(new Image("/img/Isotile_roadT-X.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R' ) {
							gc.drawImage(new Image("/img/Isotile_roadT+X.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'R' && grid[i + 1][j] == 'R') {
							gc.drawImage(new Image("/img/Isotile_road^.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'R' && grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/img/Isotile_road}.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/img/Isotile_roadv.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
							gc.drawImage(new Image("/img/Isotile_road{.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'R' || grid[i][j - 1] == 'R') {
							gc.drawImage(new Image("/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i + 1][j] == 'R' || grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i + 1][j] == 'H' && grid[i - 1][j] == 'H')
						{
							gc.drawImage(new Image("/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						else if (grid[i][j + 1] == 'H' && grid[i][j - 1] == 'H')
						{
							gc.drawImage(new Image("/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
						}
						break;

					case 'T':
						gc.drawImage(new Image("/img/Isotile_tree.png"), twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y - 8);
						break;
				}
			}
			// gc.drawImage(node.image, node.x * 32, node.y * 32);
		}
	}

}