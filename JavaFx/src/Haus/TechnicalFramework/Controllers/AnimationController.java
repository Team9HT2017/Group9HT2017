package Haus.TechnicalFramework.Controllers;

import Haus.TechnicalFramework.AnimationObjects.DjikstraNode;
import Haus.TechnicalFramework.AnimationObjects.DrawableObject;
import Haus.TechnicalFramework.AnimationObjects.Road;
import Haus.PresentationUI.Main;
import Haus.TechnicalFramework.DataHandler.Parser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
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
 * Added the settings functionality;
 * Changed some of the configurations for printing the user's name above the houses.
 */
public class AnimationController implements Initializable {

    GraphicsContext gc;

    public static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();

    public static Character[][] grid;

    static ArrayList<Road> roads = new ArrayList<Road>();

    static double mapScale;

    static ArrayList<DjikstraNode> djikstraNodes = new ArrayList<DjikstraNode>();

    private Stage stage1;

    Parent root;

    public static Comparator<TreeMap<Integer, DrawableObject>> distSorterComp = new Comparator<TreeMap<Integer, DrawableObject>>() {
        public int compare(TreeMap<Integer, DrawableObject> nodeDist1, TreeMap<Integer, DrawableObject> nodeDist2) {
            if((int)nodeDist1.firstKey() < (int)nodeDist2.firstKey()) return 1;
            return 0;
        }
    };

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
     *
     * @throws Exception
     */
    @FXML
    private void getScene1() throws Exception {

        TeacherController.uploaded = false;
        stage1 = (Stage) leaveAnimation.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/UserSelection.fxml"));
        Main.getScene(root, stage1);

    }

    /**
     * Method to give action to the Settings button. When the users press, it will
     * open a new window with all the changes possible for the system.
     *
     * @throws IOException
     */
    @FXML
    private void openSettings() throws IOException {

        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../../PresentationUI/FXML/SettingsPage.fxml"));
        Parent root = fxmlloader.load();
        stage.setTitle("Settings");
        stage.setScene(new Scene(root));
        stage.show();
    } 

  public void logMessages ()  {
         try {
		ArrayList<ArrayList<Object>>  logs = Parser.ParseInorder(TeacherController.toParse);
		System.out.println("Logs "+logs);
		String transmission ;
		ArrayList<Object>  inner  ;

		for (int j = 0; j < logs.size() ; j++) {
			inner = logs.get(j);
			for (int i = 0; i < inner.size(); i++) {
				transmission = String.format("%s%n", inner.get(i));
				this.messageLog.appendText("" + transmission);
			}
		}
         }catch (Exception E){
        	 System.out.println("Before: "+StudentController.toMessageLog);
        	 String [] arr1 = StudentController.toMessageLog.split("], ");
        	 arr1[0]=arr1[0].substring(1, arr1[0].length());
        	 arr1[arr1.length-1]=arr1[arr1.length-1].substring(0, arr1[arr1.length-1].length()-1);
        	 System.out.println("After: "+arr1[0]+" || "+arr1[arr1.length-1]);
        	 String inner;
        	 String transmission;
        	 for (int j = 0; j < arr1.length ; j++) {
     			inner = arr1[j];
     			String [] inn = inner.split(", ");
     			for (int i = 0; i < inn.length; i++) {
     				transmission = String.format("%s%n", inn[i]);
     				this.messageLog.appendText("" + transmission);
     			}
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
        String [] houseLocs = new String [nodes.size()]; // array for near-house locations
        // Build 2d grid map ('H'ouse)
        int m=0;
        for (DrawableObject node : nodes) {
            	while ((node.x == mapSize/2 || node.x==mapSize/2+1) || grid[node.x][node.y]=='H'){ // putting houses in random order, avoididing center of map (main road will be there)
                node.x = rand.nextInt((mapSize) - 2) + 1;}
                while (node.y % 2 != 0|| grid[node.x][node.y]=='H'){ //ensuring houses will be placed on even Y axis so that roads can be built between them
                node.y = rand.nextInt((mapSize) - 2) + 1;}

           
            grid[node.x][node.y] = 'H'; // placing house on map
            houseLocs[m]=node.x+","+(node.y-1); // adding location near the house to array for road building
            m++;
            System.out.println("X= "+node.x);
            System.out.println("Y= "+node.y);
        }

        Road road = new Road(mapSize/2,1,mapSize/2,mapSize-1); // building road in the middle of the map
        // roads.add(road);
        int k = 0;
        for (Pair tile : road.segments[k]) { // building road in the middle of the map
        	if (grid[(int) tile.getKey()][(int) tile.getValue()]!='H'){
            grid[(int) tile.getKey()][(int) tile.getValue()] = 'R';}
            k++;
        }
        Arrays.sort(houseLocs);
        for (String h:houseLocs){
        	System.out.println("H="+h);
        }
        System.out.println(Arrays.toString(houseLocs));
        for (String h:houseLocs){ // building road from each house to the main road
        	String [] divide = h.split(",");
        	System.out.println("Divide ="+Arrays.toString(divide));
        	int x1=Integer.parseInt(divide[0]);
        	int y1=Integer.parseInt(divide[1]);
        	if (x1<mapSize/2){
        	      for (int u=x1;u<mapSize/2;u++){       	    	
        	    	  grid[u][y1]='R';
        	    	 
        	      }}else {
        	    	  for (int u=x1;u>mapSize/2;u--){       	    	
            	    	  grid[u][y1]='R';
            	    	
            	      }
        	      }
                
        	}
      
          
		/*
         * Draw road first, then add junctions to djikstraNodes and draw next based on available nodes in network.
		 */

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Initializing anim 1st");

        animate();
        // gc.drawImage(node.image, node.x * 32, node.y * 32);
    }

    /**
     * Method to clean the canvas when needed.
     */
    public void cleanAnimationCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());


    }

    public void animate() {
        System.out.println("Initializing anim 2nd");
        canvas.setWidth((nodes.size() * 32) * mapScale + 80);
        canvas.setHeight((nodes.size() * 16) * mapScale + 80);
        gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font("Consolas", 10));
        DrawableObject node = nodes.get(0);
        int housenum = 0;
        Image grass = new Image("/Haus/DataStorage/img/Isotile_grass.png");

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
                        if (SettingsController.names == 1) {
                            gc.setFont(new Font("Calibri", 10));
                            gc.setFontSmoothingType(FontSmoothingType.GRAY);
                            gc.strokeText(node.name, twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new
                                    Point(i * 16, j * 16)).y - 16);
                            gc.setStroke(new Color(1, 1, 1, 1));
                            gc.setLineWidth(4);
                            gc.fillText(node.name, twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new
                                    Point(i * 16, j * 16)).y - 16);
                        }

                        System.out.println(node.name);
                        break;

                    case 'R':
                        if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R'
                                && grid[i][j - 1] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadCross.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j - 1] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadT-Y.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadT+Y.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadT-X.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadT+X.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_road^.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_road}.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadv.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_road{.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'R' || grid[i][j - 1] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'R' || grid[i - 1][j] == 'R') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'H' && grid[i - 1][j] == 'H') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_road.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'H' && grid[i][j - 1] == 'H') {
                            gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_roadY.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        }
                        break;

                    case 'T':
                        gc.drawImage(new Image("/Haus/DataStorage/img/Isotile_tree.png"), twoDToIso(new Point(i * 16, j * 16)).x,
                                twoDToIso(new Point(i * 16, j * 16)).y - 8);
                        break;
                }
                for (DjikstraNode djiNode : djikstraNodes) {
                    gc.drawImage(new Image("/Haus/DataStorage/img/NodeImg.png"), twoDToIso(new Point(djiNode.x * 16, djiNode.y * 16)).x, twoDToIso(new Point(djiNode.x * 16, djiNode.y * 16)).y);
                }
//
//				for ()
//				gc.
            }
        }
    }

}
