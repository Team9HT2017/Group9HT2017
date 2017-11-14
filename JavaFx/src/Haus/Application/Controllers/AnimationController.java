package Haus.Application.Controllers;

import Haus.Application.AnimationObjects.DjikstraNode;
import Haus.Application.AnimationObjects.DrawableObject;
import Haus.Application.AnimationObjects.Road;
import Haus.Application.Main;
import Haus.Application.Parser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

/**
 * This class will handle the animation page, where the user can see the diagram
 * animation, see the log created, do the settings adjustments, and leave the
 * animation.
 *
 * @author Leo Persson and Rema Salman
 * @version 1.0
 *
 * @author Laiz Figueroa
 * @version 1.1 Modification: Changed the layout and disposition of elements;
 *          Added the settings functionality;
 *          Changed some of the configurations for printing the user's name above the houses.
 *
 */
public class AnimationController implements Initializable {


	GraphicsContext gc;

	public static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();

	static Character[][] grid;

	static ArrayList<Road> roads = new ArrayList<Road>();

	static double mapScale;

	Main main = new Main();

	private Stage stage = new Stage();

	@FXML
	public Button leaveAnimation;

	@FXML
	public Button settingsButton;

	@FXML
	Canvas canvas;

    @FXML
    private TextArea messageLog;

	/**
	 * Method to give action to the Leave Animation button. When the users press, it
	 * will leave the animation and go back to the first page.
	 * @throws Exception 
	 *
	 */
	@FXML
	private void getScene1() throws Exception {

		main.hideWindow(leaveAnimation);
		TeacherController.uploaded = false;

	}

	/**
	 * Method to give action to the Settings button. When the users press, it will
	 * open a new window with all the changes possible for the system.
	 *
	 * @throws IOException
	 *
	 */
	@FXML
	private void openSettings() throws IOException {

		FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../FXML/SettingsPage.fxml"));
		Parent root = fxmlloader.load();
		stage.setTitle("Settings");
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void logMessages ()  {

		ArrayList<ArrayList<Object>>  logs = Parser.ParseInorder(TeacherController.toParse);
		String transmission ;
		ArrayList<Object>  inner  ;

		for (int j = 0; j < logs.size() ; j++) {
			inner = logs.get(j);
			for (int i = 0; i < inner.size(); i++) {
				transmission = String.format("%s%n", inner.get(i));
				this.messageLog.appendText("" + transmission);
			}
		}
	}

	public static void runAnim(Map<?, ?> map) {
		mapScale = 3 * Math.pow((double) map.keySet().size(), -0.6) * 2;
		int mapSize = (int) (map.keySet().size() * mapScale);
		grid = new Character[mapSize][mapSize];
		Random rand = new Random();

		System.out.println("Creating DrawableObjects");
		for (Object obj : map.keySet()) {
			nodes.add(new DrawableObject(obj, mapSize, mapSize));
		}

		// Build 2d grid map ('G'rass)
		for (int i = 0; i < mapSize; i++) {
			Arrays.fill(grid[i], 'G');
		}

		// Build 2d grid map ('R'oads)
		ArrayList<DjikstraNode> djikstraNodes = new ArrayList<DjikstraNode>();
		for (DrawableObject node : nodes)
		{
			//Change the x and y to be the coordinate we want on each house
			djikstraNodes.add(new DjikstraNode(node.x + 1, node.y, nodeDistances(node, nodes), node));
		}

		/*
		 * Draw road first, then add junctions to djikstraNodes and draw next based on available nodes in network.
		 */

		for (int i = 0; i < nodes.size() - 1; i++) {
			Road road = new Road(nodes.get(i), nodes.get(i + 1));
			// roads.add(road);
			int j = 0;
			for (Pair tile : road.segments[j]) {
				grid[(int) tile.getKey()][(int) tile.getValue()] = 'R';
				j++;
			}
		}

		for (int i = 1; i < mapSize - 1; i++) {
			for (int j = 1; j < mapSize - 1; j++) {
				if (grid[i + 1][j] == 'R' && (grid[i - 1][j] == 'R' || grid[i - 1][j] == 'T')
						&& (grid[i][j + 1] == 'R' || grid[i][j + 1] == 'T')
						&& (grid[i][j - 1] == 'R' || grid[i][j - 1] == 'T') && grid[i + 1][j + 1] == 'R'
						&& (grid[i - 1][j - 1] == 'R' || grid[i - 1][j - 1] == 'T') && grid[i - 1][j + 1] == 'R'
						&& grid[i + 1][j - 1] == 'R' && (grid[i][j] == 'R')) {
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
				node.x = rand.nextInt((mapSize) - 2) + 1;
				node.y = rand.nextInt((mapSize) - 2) + 1;

			}
			grid[node.x][node.y] = 'H';
		}

		// Print 2d char map to terminal for debugging purposes
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
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
		try {
			logMessages();
		}catch (Exception e ) {
			e.printStackTrace();
		}
		System.out.println("Initializing anim 1st");

		animate();
		// gc.drawImage(node.image, node.x * 32, node.y * 32);
	}
	/**
	 * Method to clean the canvas when needed.
	 *
	 */
	public void cleanAnimationCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


	}

	public void animate(){
		System.out.println("Initializing anim 2nd");
		canvas.setWidth((nodes.size() * 32) * mapScale + 80);
		canvas.setHeight((nodes.size() * 16) * mapScale + 80);
		gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font("Consolas", 10));
		DrawableObject node = nodes.get(0);
		int housenum = 0;
		Image grass = new Image("/Haus/Application/img/Isotile_grass.png");

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

						//If the settings are selected to print the name of the users on the houses it goes inside this condition.
						if (SettingsController.names == 1){
                            gc.setFont(new Font("Calibri", 10));
                            gc.setFontSmoothingType(FontSmoothingType.GRAY);
                            gc.strokeText(node.name, twoDToIso(new Point(i* 16, j * 16)).x, twoDToIso(new
                                    Point(i* 16, j * 16)).y - 16);
                            gc.setStroke(new Color(1, 1, 1, 1));
                            gc.setLineWidth(4);
							gc.fillText(node.name, twoDToIso(new Point(i* 16, j * 16)).x, twoDToIso(new
									Point(i* 16, j * 16)).y - 16);
						}

						System.out.println(node.name);
						break;

					case 'R':
						if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R'
								&& grid[i][j - 1] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadCross.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j - 1] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadT-Y.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadT+Y.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadT-X.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadT+X.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'R' && grid[i + 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_road^.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'R' && grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_road}.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadv.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_road{.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'R' || grid[i][j - 1] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i + 1][j] == 'R' || grid[i - 1][j] == 'R') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i + 1][j] == 'H' && grid[i - 1][j] == 'H') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						} else if (grid[i][j + 1] == 'H' && grid[i][j - 1] == 'H') {
							gc.drawImage(new Image("/Haus/Application/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x,
									twoDToIso(new Point(i * 16, j * 16)).y);
						}
						break;

					case 'T':
						gc.drawImage(new Image("/Haus/Application/img/Isotile_tree.png"), twoDToIso(new Point(i * 16, j * 16)).x,
								twoDToIso(new Point(i * 16, j * 16)).y - 8);
						break;
				}

//
//				for ()
//				gc.
			}
		}
	}

	static int[] nodeDistances (DrawableObject node, ArrayList<DrawableObject> nodes)
	{
		int[] dists = new int[nodes.size()];
		int i = 0;
		for (DrawableObject node2: nodes) {
			dists[i] = Math.abs(node2.x - node.x) + Math.abs(node2.y - node.y);
			i++;
		}
		Arrays.sort(dists);
		return dists;
	}

}