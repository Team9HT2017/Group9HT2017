package Haus;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

//@Author Leo; Rema

public class AnimationController implements Initializable {

	@FXML
	public Button Kill;
	@FXML
	Canvas canvas;

	GraphicsContext gc;
	static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();
	static Character[][] grid;
	Controller controller = new Controller();

	/*
    IMPORTANT!
    CHANGE THE FOLLOWING VARIABLE TO CHANGE THE SCALE (X and Y = nodes*scale)
    Note: There is a minimum map scale for each number of nodes (ex. 1.25 for 4 nodes, much lower for 50 (a bit lower than 0.3)). The program WILL crash if set too low.
     */
	static double mapScale;

	@FXML
	private void GetScene1() throws IOException {

		controller.HideWindow(Kill);

	}

	public static boolean otherNotConnectedExists() {
		for (DrawableObject e : nodes) {
			if (!e.isConnected)
				return true;
		}
		return false;
	}

	// connection between a and b and print the connected nodes
	public static void connectTwoNodes(int a, int b) {
		nodes.get(a).addConnection(nodes.get(b));
		nodes.get(b).addConnection(nodes.get(a));
		System.out.println(nodes.get(a).name + " is connected to " + nodes.get(b).name);
	}

	/**
	 * @return the index of the node that the passed node with index a should
	 *         connect with
	 * @param a
	 *            the index of the node who we want to find a connection for
	 */
	public static int findConnectionNode(int a) {
		int ret = -1;
		if (!nodes.get(a).isConnected || otherNotConnectedExists()) {
			// return the index of the node that has the shortest distance
			double minDistance = 3000;
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(a).distance(nodes.get(i)) < minDistance && i != a
						&& !nodes.get(a).isConnectedTo(nodes.get(i))) {
					minDistance = nodes.get(a).distance(nodes.get(i));
					ret = i;
				}
			}
		}
		return ret;
	}


	public static void runAnim(Map<?, ?> map)
	{
		mapScale = 3 * Math.pow((double) map.keySet().size(), -0.6);
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
		// build 2D grid map ('R'oad)
		for (int i = 0; i < nodes.size(); i++) {
			int con = findConnectionNode(i);
			if (con != -1)
				connectTwoNodes(con, i);

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
				}
			}
			// gc.drawImage(node.image, node.x * 32, node.y * 32);
		}
	}

}