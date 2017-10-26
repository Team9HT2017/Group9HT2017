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

//@Author Leo;
public class AnimationController implements Initializable {


    @FXML
    public Button Kill;
	@FXML
	Canvas canvas;
	
	GraphicsContext gc;
	static ArrayList<DrawableObject> nodes = new ArrayList<DrawableObject>();
	static Character[][] grid;
    Controller controller = new Controller();

    @FXML
    private void GetScene1() throws IOException {

        controller.HideWindow(Kill);

    }

    
	public static void runAnim(Map<?, ?> map)
	{
		int nodeNum = map.keySet().size();
		grid = new Character[nodeNum][nodeNum];
		Random rand = new Random();

		System.out.println("Creating DrawableObjects");
		for(Object obj : map.keySet())
		{
			nodes.add(new DrawableObject(obj, nodeNum, nodeNum));
		}

		//Build 2d grid map ('G'rass)
		for(int i = 0; i < nodeNum; i++)
		{
			Arrays.fill(grid[i], 'G');
		}

		//Build 2d grid map ('H'ouse)
		for(DrawableObject node : nodes)
		{
			//For loop makes sure there are no houses sharing the same tilespace
			for(; grid[node.x][node.y] == 'H'; )
			{
				node.x = rand.nextInt(nodeNum);
				node.y = rand.nextInt(nodeNum);
			}

			grid[node.x][node.y] = 'H';
		}

		//Print 2d char map to terminal for debugging purposes
		for(int i = 0; i < nodeNum; i++)
		{
			for(int j = 0; j < nodeNum; j++) {
				System.out.print(grid[j][i]);
			}
			System.out.println();
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("Initializing anim");
		Image grass = new Image("/img/Isotile.png");
		gc = canvas.getGraphicsContext2D();
		gc.setFont(new Font("Consolas", 10));
		int i = 0;
		for(DrawableObject node : nodes)
		{
			for(int j = 0; j < nodes.size(); j++)
			{
				switch (grid[i][j]){
					case 'G':
						gc.drawImage(grass, i * 32, j * 16);
						break;
					case 'H':
						gc.drawImage(node.image, i * 32, (j * 16) - 16);
						gc.fillText(node.name, i * 32, (j * 16) - 16);
						break;
				}
			}

			i++;


			//gc.drawImage(node.image, node.x * 32, node.y * 32);
		}
	}
	
}
