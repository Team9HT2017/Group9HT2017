package Haus.TechnicalFramework.Controllers;

import Haus.NetworkHandlers.TCPClient;
import Haus.NetworkHandlers.TCPListener;
import Haus.TechnicalFramework.AnimationObjects.DjikstraNode;
import Haus.TechnicalFramework.AnimationObjects.DrawableObject;
import Haus.TechnicalFramework.AnimationObjects.Graph;
import Haus.TechnicalFramework.AnimationObjects.Road;
import Haus.PresentationUI.Main;
import Haus.TechnicalFramework.DataHandler.Parser;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.text.FontSmoothingType;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class will handle the animation page, where the user can see the diagram
 * animation, see the log created, and leave the animation.
 *
 * @author Leo Persson and Rema Salman
 * @author Laiz Figueroa
 * @version 1.1 Modification: Changed the layout and disposition of elements;
 *          Added the settings functionality;
 *          Changed some of the configurations for printing the user's name above the houses;
 *          Deleted the settings functionality.
 */
public class AnimationController implements Initializable {

    GraphicsContext gc;

    public static boolean doAnimate = false;
    public static boolean runFirstFrame = false;
    public static boolean firstMapDraw = true;
    int dX, dY;
    int i = 1;

    int housecontrol=0;

    private ArrayList <Pair<Rectangle,DrawableObject>> houseinfo = new ArrayList <Pair<Rectangle,DrawableObject>>();

    public static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();

    public static char[][] grid;

    static ArrayList<Road> roads = new ArrayList<Road>();

    static double mapScale;

    private ArrayList<ArrayList<Object>> logs;

    static ArrayList<DjikstraNode> djikstraNodes = new ArrayList<DjikstraNode>();

    private Stage stage1;

    Parent root;

    private Stage stage = new Stage();

    private String messageLogCheck="";

    @FXML
    public Button leaveAnimation;

    @FXML
    public Button sendMessage;

    @FXML
    Canvas canvas;

    @FXML
    private TextArea messageLog;

    @FXML
    private Label username;

    String userNames;

    int framesPerSecond;

    //Load images:
    Image roadCross = new Image("/Haus/DataStorage/img/Isotile_roadCross.png");
    Image roadTminY = new Image("/Haus/DataStorage/img/Isotile_roadT-Y.png");
    Image roadTplsY = new Image("/Haus/DataStorage/img/Isotile_roadT+Y.png");
    Image roadTminX = new Image("/Haus/DataStorage/img/Isotile_roadT-X.png");
    Image roadTplsX = new Image("/Haus/DataStorage/img/Isotile_roadT+X.png");
    Image roadCurveSouth = new Image("/Haus/DataStorage/img/Isotile_road^.png");
    Image roadCurveWest = new Image("/Haus/DataStorage/img/Isotile_road}.png");
    Image roadCurveNorth = new Image("/Haus/DataStorage/img/Isotile_roadv.png");
    Image roadCurveEast = new Image("/Haus/DataStorage/img/Isotile_road{.png");
    Image roadYY = new Image("/Haus/DataStorage/img/Isotile_roadY.png");
    Image roadXX = new Image("/Haus/DataStorage/img/Isotile_road.png");
    //Aesthetic images
    Image tree = new Image("/Haus/DataStorage/img/Isotile_tree.png");
    Image grass = new Image("/Haus/DataStorage/img/Isotile_grass.png");
    //Bubble image
    Image bubble = new Image("/Haus/DataStorage/img/bubble.png");

    public static int x = 0, y = 0;

    public static List<Pair <String, Image>> deviceImages;

    long lastUpdate = 0;

    AnimationTimer frameTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastUpdate >= 28_000_000) {
                redraw();
                if (TCPClient.teacherUsername != "") {
                    username.setText(TCPClient.teacherUsername);
                } else {
                    username.setText(TCPClient.studentUsername);
                }
                if (TCPListener.messageReceiveLog != messageLogCheck) {
                    messageLog.clear();
                    messageLog.appendText(TCPListener.messageReceiveLog);
                    messageLogCheck = TCPListener.messageReceiveLog;
                }
                lastUpdate = now ;
            }
        }
    };

    /**
     * Method to give action to the Leave Animation button. When the users press, it
     * will leave the animation and go back to the first page.
     *
     * @throws Exception
     */
    @FXML
    private void leaveAnimation() throws Exception {
        String stopServer;
        if (TeacherController.user == "teacher") {
            stopServer = "./stopserver.sh";
            TeacherController.alert.close();
            TeacherController.uploaded = false;
            if (TCPClient.studentUsername.isEmpty()) {
                TeacherController.runScript(stopServer);
            }
        }

        messageLog.setText("");
        TCPListener.messageReceiveLog = "";
        stage1 = (Stage) leaveAnimation.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../../PresentationUI/FXML/UserSelection.fxml"));
        Main.getScene(root, stage1);

    }

    /**
     * Method to extract the last exchange between a specific sender and receiver .
     *param ArrayList of objects
     */
    private  ArrayList<Object> priorityMessaging (String message ) {

        ArrayList<ArrayList<Object>> input = Parser.ParseInorder(TeacherController.toParse);
        ArrayList<Object> inner  ;
        ArrayList<Object> messages =new ArrayList<>();

        ArrayList<Object> one= new  ArrayList<>(1) ;

        one.add(message);
        int position=input.indexOf(one);
        int pos=0;
        for (int i =0 ; i< input.size() ; i ++) {
            inner = input.get(i);

            for (int j = 0; j < inner.size(); j++) {
                String[] split = message.split(",");

                if ((inner.get(j).toString().substring(inner.indexOf("{ ") + 2, inner.indexOf(",")).equals(split[0].substring(split[0].indexOf("{")) + 1) &&
                        inner.get(j).toString().substring(inner.toString().indexOf(",", 8) + 2, inner.indexOf(",")).equals(split[2].substring(split[2].indexOf("to")) + 1)) && message.equals(split[3].substring(24))) {
                    pos = input.indexOf(inner);

                }
                if (pos < position) {

                    messages= inner;
                }


            }
        }
        return messages;

    }

    /**
     * Method to check whether the message exchanged between sender and receiver is a reply or not .
     *param Sender , Receiver
     */
    public static boolean checkOrder (String sender, String receiver ){
        ArrayList<ArrayList<Object>> result = Parser.ParseInorder(TeacherController.toParse);

        int pos1=0;
        int pos2=0;
        ArrayList<Object> inner  ;
        for (int j = 0; j < result.size(); j++) {
            inner = result.get(j);

            String index0 = (String) inner.get(0);
            String index1 = (String) inner.get(1);
            String index2 = (String) inner.get(2);
            String index3 = (String) inner.get(3);
            if (sender.equals(index0.substring(1)) && receiver.equals(index2.substring(2,index2.length()))) {
                pos1 = inner.indexOf(j);
            }
            if (receiver.equals(index0.substring(1)) && sender.equals(index2.substring(2,index2.length()))) {
                pos2 = inner.indexOf(j);
            }
        }
        if (pos2>=pos1) {

            return true;
        } else {
            return false;
        }

    }

    /**
     * Method to send a message when send button is clicked.
     *
     * @throws IOException
     */


    @FXML
    private void sendMessage() throws IOException {
        String sending = "nothing";
        int control = 0;
     //   System.out.println(Arrays.toString(logs.toString().split("\\|,")));

      if (logs != null && logs.isEmpty()!=true) { // if user is teacher
            String[] check = logs.toString().split("\\|,"); // get array of messages
            for (int i = 0; i < check.length; i++) { // loop through array of messages
            	String [] mess = TCPClient.teacherUsername.trim().split(","); // get list of teacher username(s)
            	System.out.println((check[0].split("=")[1].split("@")[0]));
            	System.out.println(Arrays.toString(mess));
              //  System.out.println(TCPListener.allowMessage+"ii"+Integer.parseInt((check[i].split("\\?")[1].split("\\]")[0])));
            	//System.out.println(Arrays.toString(mess).trim()+" 1 ="+mess[1].trim()+"77"+check[i].substring(check[i].indexOf("{ ") + 2, check[i].indexOf(",")).trim()+"77");//+"00"+check[i].substring(check[i].indexOf("{ ") + 2, check[i].indexOf(",")).trim()+"00");
            	for (int b=0;b<mess.length;b++){ // loop through teacher username(s) to find highest priority message
                if (check[i].substring(check[i].indexOf("{ ") + 2, check[i].indexOf(",")).trim().equals(mess[b].replaceAll("\\[", "").replaceAll("\\]", "").trim()) && control < 1 // compare each message's sender to sending user to find his/her highest priority message, control is used to send onlu one message at a time
                    && Parser.flows.get(Integer.parseInt((check[i].split("=")[1].split("@")[0])))==Integer.parseInt(check[i].split("=")[1].split("@")[1])) { //priority counter that allows to send messages only in correct order
                    System.out.println("Check== " + check[i]); // print message that is being sent
                    sending = check[i];
                    control++;
                   // logs.remove(i); //delete this message after sending
                    System.out.println("New logs "+logs);
                }}
            }
        } else {
            String[] check = StudentController.topars[2].split("\\|, ");  //get array of messages       
            for (int i = 0; i < check.length; i++) { // loop through array of messages

                if (check[i].substring(check[i].indexOf("{ ") + 2, check[i].indexOf(",")).trim().equals(TCPClient.studentUsername.split("\\|")[0]) && control < 1 // compare each message's sender to sending user to find his/her highest priority message, control is used to send onlu one message at a time
                	    && Parser.flows.get(Integer.parseInt((check[i].split("=")[1].split("@")[0])))==Integer.parseInt(check[i].split("=")[1].split("@")[1])) { //priority counter that allows to send messages only in correct order

                    System.out.println("Check== " + check[i]);
                    sending = check[i];
                    //StudentController.topars[2].replaceAll(check[i].substring(0, check[i].length()-2), ""); //delete this message after sending
                    control++;
                }
            }
        }
        try {
            //TCPClient.sendMessage(" [{ u2,  send, to g,  the following message [lol] } ]",false);
        	if (!sending.equals("nothing")){ // if no message is available to send, nothing is sent to server
            TCPClient.sendMessage(sending.replaceAll("\\\\", ""), false);}// sending message if it was found
        	else{
        		System.out.println("Error sending message. Message is: \"nothing\"");
                UserController.dialog ("Error sending message","You have no messages to send");
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Messagelog function
    public void logMessages(String msgs) {
        try {
            logs = Parser.parParsing(TeacherController.toParse); // message list
            System.out.println("Logs " + logs);
            String transmission;
            ArrayList<Object> inner;

            for (int j = 0; j < logs.size(); j++) {
                inner = logs.get(j);
                for (int i = 0; i < inner.size(); i++) {
                    transmission = String.format("%s%n", inner.get(i));
                   // this.messageLog.appendText("" + transmission);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Before: " + StudentController.topars[2]); // student message list
            String[] arr1 = StudentController.toMessageLog.split("\\| ");
            arr1[0] = arr1[0].substring(1, arr1[0].length());
            arr1[arr1.length - 1] = arr1[arr1.length - 1].substring(0, arr1[arr1.length - 1].length() - 1);
            System.out.println("After: " + arr1[0] + " || " + arr1[arr1.length - 1]);
            String inner;
            String transmission;
            for (int j = 0; j < arr1.length; j++) {
                inner = arr1[j];
                String[] inn = inner.split(", ");
                for (int i = 0; i < inn.length; i++) {
                    transmission = String.format("%s%n", inn[i]);
                    messageLog.appendText("" + transmission);
                }
            }
        }
    }

    /*
     * Method to random generate map on teachers end (previously runAnim)
     */
    public static char[][] generateMap(Map<?, ?> map) {
        //Multiplier for map scale
        Double mapMultiplier = 2.0;

        //Power function to calculate mapSize without crashing due to lack of space
        mapScale = 3 * Math.pow((double) map.keySet().size(), -0.6) * mapMultiplier;
        int mapSize = (int) (map.keySet().size() * mapScale);

        //Set grid size
        grid = new char[mapSize][mapSize];
        Random rand = new Random();

        System.out.println("Creating DrawableObjects");
        nodes.clear();
        for (Object obj : map.keySet()) {
            //if (TeacherController.user.equals("teacher")) {
                nodes.add(new DrawableObject(obj, mapSize, mapSize));
            //}
        }

        // Build 2d grid map ('G'rass): Fills up the entire grid with 'G' and then overwrites 'G' with 'H' and 'R' etc.
        for (int i = 0; i < mapSize; i++) {
            Arrays.fill(grid[i], 'G');
        }


        String[] houseLocs = new String[nodes.size()]; // array for near-house locations
        // Build 2d grid map ('H'ouse)
        int m = 0;
        for (DrawableObject node : nodes) {
            while ((node.x == mapSize / 2 || node.x == mapSize / 2 + 1) || grid[node.x][node.y] == 'H') { // putting houses in random order, avoididing center of map (main road will be there)
                node.x = rand.nextInt((mapSize) - 2) + 1;
            }
            while (node.y % 2 != 0 || grid[node.x][node.y] == 'H') { //ensuring houses will be placed on even Y axis so that roads can be built between them
                node.y = rand.nextInt((mapSize) - 2) + 1;
            }


            grid[node.x][node.y] = 'H'; // placing house on map
            houseLocs[m] = node.x + "," + (node.y - 1); // adding location near the house to array for road building
            m++;
            System.out.println("X= " + node.x);
            System.out.println("Y= " + node.y);
        }

        Road road = new Road(mapSize / 2, 1, mapSize / 2, mapSize - 1); // building road in the middle of the map
        // roads.add(road);
        int k = 0;
        for (Pair tile : road.segments[k]) { // building road in the middle of the map
            if (grid[(int) tile.getKey()][(int) tile.getValue()] != 'H') {
                grid[(int) tile.getKey()][(int) tile.getValue()] = 'R';
            }
            k++;
        }
        Arrays.sort(houseLocs);
        for (String h : houseLocs) {
            System.out.println("H=" + h);
        }
        System.out.println(Arrays.toString(houseLocs));
        for (String h : houseLocs) { // building road from each house to the main road
            String[] divide = h.split(",");
            System.out.println("Divide =" + Arrays.toString(divide));
            int x1 = Integer.parseInt(divide[0]);
            int y1 = Integer.parseInt(divide[1]);
            if (x1 < mapSize / 2) {
                for (int u = x1; u < mapSize / 2; u++) {
                    grid[u][y1] = 'R';

                }
            } else {
                for (int u = x1; u > mapSize / 2; u--) {
                    grid[u][y1] = 'R';

                }
            }

        }

        // Print 2d char map to terminal for debugging purposes
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                System.out.print(grid[j][i]);
            }
            System.out.println();
        }
        return grid;
    }

    // from 2D to an Isometric
    public Point twoDToIso(Point point) {
        Point tempPt = new Point(0, 0);
        tempPt.x = point.x - point.y + (int) canvas.getWidth() / 2 - 16;
        tempPt.y = (point.x + point.y) / 2 + (int) ((canvas.getHeight() / 2) - nodes.size() * (8 * mapScale));
        return (tempPt);
    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing AnimationPage");

        //Case for when the current user is a teacher
        if (TeacherController.user == "teacher") {

            //TeacherController.map = userNames.split(Pattern.quote("~"))[1];


            String map = TeacherController.map;
            String[] mapArr = map.split(Pattern.quote("~"));
            username.setText(TCPClient.teacherUsername);
            try {
                logMessages(mapArr[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            initAnim(mapArr[0]);
            frameTimer.start();
            //redraw();
        }
        //case for when the current user is not a teacher (e.g. student) sets variables not set because of lack of runanim
        else {
            String[] data = StudentController.topars;
            createStudentObjects(data[1]);
            username.setText(TCPClient.studentUsername);
            //int mapSize = (data[0].split(Pattern.quote("], ["))[0].length()) / 3;
            mapScale = 3 * Math.pow((double) nodes.size(), -0.6) * 2;
            grid = new char[(int) (nodes.size() * mapScale)][(int) (nodes.size() * mapScale)];
            //logMessages(data[2]);
            initAnim(data[0]);
            frameTimer.start();
            //redraw();
        }
    }

    /**
     * Method to redraw the canvas. Called every
     */
    public void redraw() {

        //System.out.println("Redrawing Canvas, FPS: " + framesPerSecond);
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        DrawableObject node;
        int housenum = 0;

        for (int i = 0; i < (int) (nodes.size() * mapScale); i++) {

            for (int j = 0; j < (int) (nodes.size() * mapScale); j++) {

                switch (grid[i][j]) {

                    //Case for drawing grass for every 'G' in the grid
                    case 'G':
                        gc.drawImage(grass, twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new Point(i * 16, j * 16)).y);
                        break;

                    //Case for drawing houses for every 'H' in the grid as well as the text above each
                    case 'H':
                        node = nodes.get(housenum);
                        housenum++;
                        gc.drawImage(node.image, twoDToIso(new Point(i * 16, j * 16)).x,
                                twoDToIso(new Point(i * 16, j * 16)).y - node.image.getHeight() + 26);

                        // adding this node to the dijkstraNodes
                        if (firstMapDraw) addToDjikstraNodes(i, j - 1, 'H', node.name);
                        if (housecontrol < nodes.size()) { // add invisible rectangle to each house for message animation and additional information pop-up
                            int x1 = twoDToIso(new Point(i * 16, j * 16)).x;
                            int y1 = twoDToIso(new Point(i * 16, j * 16)).y;
                            int[] arrh = new int[2];
                            arrh[0] = x1;
                            arrh[1] = y1;
                            Rectangle n1 = new Rectangle(x1, y1 - 16, node.image.getHeight(), node.image.getHeight());
                            Pair<Rectangle, DrawableObject> p = new Pair<Rectangle, DrawableObject>(n1, node);
                            houseinfo.add(p);
                            housecontrol++;
                        }
                        //For printing the names above the houses
                        gc.strokeText(node.name.split("\\|")[0], twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new
                                Point(i * 16, j * 16)).y - 16);
                        gc.fillText(node.name.split("\\|")[0], twoDToIso(new Point(i * 16, j * 16)).x, twoDToIso(new
                                Point(i * 16, j * 16)).y - 16);
                        break;

                    //Case for drawing roads for every 'R' in the grid
                    case 'R':
                        if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R'
                                && grid[i][j - 1] == 'R') {
                            gc.drawImage(roadCross, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j - 1] == 'R') {
                            gc.drawImage(roadTminY, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i + 1][j] == 'R' && grid[i - 1][j] == 'R' && grid[i][j + 1] == 'R') {
                            gc.drawImage(roadTplsY, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(roadTminX, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j + 1] == 'R' && grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(roadTplsX, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j + 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(roadCurveSouth, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j + 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(roadCurveWest, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j - 1] == 'R' && grid[i - 1][j] == 'R') {
                            gc.drawImage(roadCurveNorth, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j - 1] == 'R' && grid[i + 1][j] == 'R') {
                            gc.drawImage(roadCurveEast, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                            // adding this node to the dijkstraNodes
                            if (firstMapDraw) addToDjikstraNodes(i, j, 'R', null);
                        } else if (grid[i][j + 1] == 'R' || grid[i][j - 1] == 'R') {
                            gc.drawImage(roadYY, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'R' || grid[i - 1][j] == 'R') {
                            gc.drawImage(roadXX, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i + 1][j] == 'H' && grid[i - 1][j] == 'H') {
                            gc.drawImage(roadXX, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else if (grid[i][j + 1] == 'H' && grid[i][j - 1] == 'H') {
                            gc.drawImage(roadYY, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        } else {
                            gc.drawImage(roadYY, twoDToIso(new Point(i * 16, j * 16)).x,
                                    twoDToIso(new Point(i * 16, j * 16)).y);
                        }
                        break;

                    //Case for drawing trees for every 'T' in the grid, purely aesthetic and not used as of 30/11
                    case 'T':
                        gc.drawImage(tree, twoDToIso(new Point(i * 16, j * 16)).x,
                                twoDToIso(new Point(i * 16, j * 16)).y - 8);
                        break;
                }
            }
        }

        firstMapDraw = false;

        Point pDest;
        Point pStart;
        if (runFirstFrame) {
            sendMessage.setDisable(true);
            runFirstFrame = false;
            runDjikstra();
            i = Graph.pathArrayList.size() - 2;
            pStart = twoDToIso(new Point(Graph.pathArrayList.get(i + 1).x * 16, Graph.pathArrayList.get(i + 1).y * 16));
            pDest = twoDToIso(new Point(Graph.pathArrayList.get(i).x * 16, Graph.pathArrayList.get(i).y * 16));
            x = pStart.x + 16;
            y = pStart.y + 8;
            int diffX = pDest.x - pStart.x;
            int diffY = pDest.y - pStart.y;

            dX = diffX / Math.abs(diffY);
            dY = diffY / Math.abs(diffY);
        }

        if (doAnimate && i != -1) {

            pDest = twoDToIso(new Point(Graph.pathArrayList.get(i).x * 16, Graph.pathArrayList.get(i).y * 16));
           // if(x > pDest.x && x < pDest.x + 32 && y > pDest.y && y < pDest.y + 16){
            if (x == pDest.x + 16 && y == pDest.y + 8) {
                x = pDest.x + 16;
                y = pDest.y + 8;
                i--;

                pStart = twoDToIso(new Point(Graph.pathArrayList.get(i + 1).x * 16, Graph.pathArrayList.get(i + 1).y * 16));
                pDest = twoDToIso(new Point(Graph.pathArrayList.get(i).x * 16, Graph.pathArrayList.get(i).y * 16));
                int diffX = pDest.x - pStart.x;
                int diffY = pDest.y - pStart.y;

                dX = diffX / Math.abs(diffY);
                dY = diffY / Math.abs(diffY);

            }
            x += dX;
            y += dY;

            gc.drawImage(bubble, x - bubble.getWidth() / 2, y - bubble.getHeight());
        }
        if (i <= -1) {
            doAnimate = false;
            Graph.pathArrayList.clear();
            sendMessage.setDisable(false);
        }
    }

    public void runDjikstra(){
        // ......... Dijkstra section ...........
        //ArrayList<SourceDestinationPair> pairSequence = fillSDPairs ();
        for (DjikstraNode djiNode : djikstraNodes) {
            djiNode.addNiegbours (djikstraNodes);
        }

        Pair<DjikstraNode, DjikstraNode> nodePair = fillSDPairs();
        DjikstraNode.shortestPathAlgorithm (nodePair.getKey(), nodePair.getValue(), djikstraNodes);
        for (int i = 0; i < Graph.pathArrayList.size (); i++) {
            PathPoint pathNode = pathPointDrawableObject (Graph.pathArrayList.get (i));
            //gc.drawImage (new Image ("/Haus/DataStorage/img/NodeImg.png"), twoDToIso (new Point (pathNode.x * 16, pathNode.y * 16)).x, twoDToIso (new Point (pathNode.x * 16, pathNode.y * 16)).y);
        }
    }

    //Function for creating objects required for drawing in case they are not available (ex. student)
    public void createStudentObjects(String objString) {

        //Split the string into subcomponents to separate variables
        String[] objArray = objString.split (Pattern.quote ("}{"));
        objArray[0] = objArray[0].split (Pattern.quote ("{"))[1];
        objArray[objArray.length - 1] = objArray[objArray.length - 1].split (Pattern.quote ("}"))[0];


        for (String str : objArray) {
            String[] houseInfoArr = str.split (Pattern.quote (","));
            nodes.add (new DrawableObject (houseInfoArr[0], Integer.parseInt (houseInfoArr[1]), Integer.parseInt (houseInfoArr[2])));
        }

        Image building;

        if (nodes.get(0).name.contains("Device: ")) {
            Set<String> set = new HashSet<>();

            for (int i = 0; i < AnimationController.nodes.size(); i++) {
                set.add(AnimationController.nodes.get(i).name.split(Pattern.quote("Device: "))[1]);
            }
            deviceImages = new ArrayList<Pair<String, Image>>(set.size());

            int counter = 0;
            while (counter < set.size()) {

                if (counter == 0) {
                    building = new Image("/Haus/DataStorage/img/apartmentbuilding.png");
                } else if (counter == 1) {
                    building = new Image("/Haus/DataStorage/img/school.png");
                } else {
                    building = new Image("/Haus/DataStorage/img/house.png");
                }

                deviceImages.add(new Pair<String, Image>(set.toArray()[counter].toString(), building));
                counter++;
            }
            for (DrawableObject node : AnimationController.nodes) {
                node.checkDevice();
            }
        }
        else {
            for (DrawableObject node : AnimationController.nodes) {
                node.image = new Image("/Haus/DataStorage/img/house.png");
            }
        }

    }


        private class PathPoint {
            public int x, y;

            public PathPoint (int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        private PathPoint pathPointDrawableObject (Point pathPoint){
            //String[] cooardinates = pathPoint.split (",");
            return new PathPoint (pathPoint.x, pathPoint.y);
        }

        /**
         * Method to add the roads injections to be dijkstraNodes.
         *
         * @param i: represetns the dijkstra's x coordinate of the house/ injection (road).
         * @param j  :
         *           represetns the dijkstra's y coordinate of the house/injection (road).
         */
        private static void addToDjikstraNodes ( int i, int j, char type, String name){
            DjikstraNode dn = new DjikstraNode (i, j, type, name);
            if (!djikstraNodes.contains (dn))
                djikstraNodes.add (dn);
        }


        private Pair<DjikstraNode, DjikstraNode> fillSDPairs () {
            Pair<DjikstraNode, DjikstraNode> pair = new Pair<DjikstraNode, DjikstraNode> (findNodebyName (TCPListener.srcDest[0]), findNodebyName (TCPListener.srcDest[1]));
            return  pair;
                //pairs.add (new Pair<DjikstraNode, DjikstraNode> (findNodebyName (TCPListener.srcDest[0]), findNodebyName (TCPListener.srcDest[1])));
        }


        private DjikstraNode findNodebyName (String name){
            for (int i = 0; i < djikstraNodes.size (); i++) {
                //if (djikstraNodes.get (i).name == null)
                //    continue;
                if (djikstraNodes.get (i).name != null && djikstraNodes.get (i).name.contains (name + "|")) {
                    // System.out.println ("  [#] found " + name + "=" + djikstraNodes.get (i).name + "  at " + djikstraNodes.get (i).x + "," + djikstraNodes.get (i).y);
                    return djikstraNodes.get (i);
                }
            }
            System.out.println ("Should not reach this point");
            return null;
        }

    //Function for initialising the animation on the canvas required for both the teacher and the student
    public void initAnim(String map) {

        //Set necessary canvas and GraphicsContext properties
        canvas.setWidth((nodes.size() * 32) * mapScale + 80);
        canvas.setHeight((nodes.size() * 16) * mapScale + 80);
        canvas.setOnMouseClicked( // add mouse listener to call extended information pop-up when house is clicked
    	        new EventHandler<MouseEvent>() {
    	            @Override
    	            public void handle(MouseEvent t) {
    	                 for (int m=0;m<houseinfo.size();m++){

    	                	// System.out.println(Arrays.toString(houseinfo.toArray()));
    	                	 if (houseinfo.get(m).getKey().contains(t.getX(),t.getY())){

    	                		 UserController.dialog("House info",houseinfo.get(m).getValue().name);
    	                	 }

    	                 }
    	            }
    	        });
        canvas.setFocusTraversable(false);

        gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font("Calibri", 10));
        gc.setFontSmoothingType(FontSmoothingType.GRAY);
        gc.setLineWidth(4);
        gc.setStroke(new Color(1, 1, 1, 1));


        ArrayList<ArrayList<Character>> chararr = new ArrayList<ArrayList<Character>>();

        //Remove starting array brackets
        map = map.split(Pattern.quote("[["))[1];
        map = map.split(Pattern.quote("]]"))[0];

        {
            //Split each grid x line into a y array
            String[] array1 = map.split(Pattern.quote("], ["));
            int i = 0;
            while (i != array1.length && array1[i].length() >= 0) {
                chararr.add(new ArrayList<Character>());

                //Split each tile into an x array
                String[] strarr = array1[i].split(Pattern.quote(", "));
                for (String str : strarr) {

                    //Add each x array into large x/y array
                    chararr.get(i).add(str.toCharArray()[0]);
                }
                i++;
            }

            //Insert x/y array into grid for drawing
            for (int j = 0; j < chararr.size(); j++)
                for (int k = 0; k < chararr.get(j).size(); k++)
                    grid[j][k] = chararr.get(j).get(k);
        }
    }
}
