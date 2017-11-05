package Haus;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * This class handles ...
 *
 * @author
 * @version 1.0
 *
 */

public class Road {

    //instance variables
    int xOrigin, yOrigin;
    int xDestination, yDestination;
    int xDiff, yDiff;
    DrawableObject origin;
    DrawableObject destination;


    //static variables
    static ArrayList<Pair>[] segments = new ArrayList[AnimationController.nodes.size()];
    static int counter = 0;

    public Road(DrawableObject origin, DrawableObject destination)
    {
        Arrays.fill(segments,new ArrayList<Pair>());

        this.origin = origin;
        this.destination = destination;
        xOrigin = origin.x;
        yOrigin = origin.y;
        xDestination = destination.x;
        yDestination = destination.y;

        xDiff = xDestination - xOrigin;
        yDiff = yDestination - yOrigin;
        /*
        if(Math.abs(xDiff) < Math.abs(yDiff) && xDiff > 0 && AnimationController.grid[xOrigin + 1][yOrigin] != 'H')
        {
            xOrigin++;
        }
        */

        for (int i = 0; i < Math.abs(xDiff); i++)
        {
            if (xDiff > 0) {
                segments[counter].add(new Pair<>(xOrigin++, yOrigin));
            }
            else
            {
                segments[counter].add(new Pair<>(xOrigin--, yOrigin));

            }

        }
        for (int i = 0; i < Math.abs(yDiff); i++) {
            if (yDiff > 0) {
                segments[counter].add(new Pair<>(xOrigin, yOrigin++));
            } else {
                segments[counter].add(new Pair<>(xOrigin, yOrigin--));

            }
        }
        counter++;
    }
}
