package MVC.Models.AnimationObject;

import MVC.Models.AnimationObject.DrawableObject;
import MVC.Controllers.AnimationController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class handles ...
 *
 * @author Leo Persson
 * @version 1.0
 * Creation:
 *
 * @author Leo
 * @version 1.1
 * Modification:
 */

public class Road {
    //instance variables
    int xOrigin, yOrigin;
    int xDestination, yDestination;
    int xDiff, yDiff;
    DrawableObject origin;
    DrawableObject destination;


    //static variables
    public static ArrayList<Pair>[] segments = new ArrayList[AnimationController.nodes.size ()];
    static int counter = 0;

    public Road (DrawableObject origin, DrawableObject destination) {
        Arrays.fill (segments, new ArrayList<Pair> ());

        this.origin = origin;
        this.destination = destination;
        xOrigin = origin.x;
        yOrigin = origin.y;
        xDestination = destination.x;
        yDestination = destination.y;

        xDiff = xDestination - xOrigin;
        yDiff = yDestination - yOrigin;

        for (int i = 0; i < Math.abs (xDiff); i++) {
            if (xDiff > 0) {
                segments[counter].add (new Pair<> (xOrigin++, yOrigin));
            } else {
                segments[counter].add (new Pair<> (xOrigin--, yOrigin));
            }
        }
        for (int i = 0; i < Math.abs (yDiff); i++) {
            if (yDiff > 0) {
                segments[counter].add (new Pair<> (xOrigin, yOrigin++));
            } else {
                segments[counter].add (new Pair<> (xOrigin, yOrigin--));
            }
        }
        counter++;
    }

    public Road (int xOrigin, int yOrigin, int xDestination, int yDestination) {
        Arrays.fill (segments, new ArrayList<Pair> ());

        xDiff = xDestination - xOrigin;
        yDiff = yDestination - yOrigin;

        for (int i = 0; i < Math.abs (xDiff); i++) {
            if (xDiff > 0) {
                int jm = xOrigin + 1;
                if (AnimationController.grid[jm][yOrigin] != 'H') {
                    segments[counter].add (new Pair<> (xOrigin++, yOrigin));
                }
                //if house is in the way
                else {
                    int left = yOrigin;
                    int right = yOrigin;
                    while (AnimationController.grid[jm][left] == 'H' && AnimationController.grid[jm][right] == 'H') {
                        segments[counter].add (new Pair<> (xOrigin, left++));
                        segments[counter].add (new Pair<> (xOrigin, right--));
                    }
                    if (AnimationController.grid[jm][left] != 'H') {
                        segments[counter].add (new Pair<> (xOrigin++, left));
                        segments[counter].add (new Pair<> (xOrigin++, left));
                        while (left > yOrigin) {
                            segments[counter].add (new Pair<> (xOrigin, left--));
                        }
                    } else {
                        segments[counter].add (new Pair<> (xOrigin++, right));
                        segments[counter].add (new Pair<> (xOrigin++, right));
                        while (right < yOrigin) {
                            segments[counter].add (new Pair<> (xOrigin, right++));
                        }
                    }
                }

            } else {
                int jm = xOrigin - 1;
                if (AnimationController.grid[jm][yOrigin] != 'H') {
                    segments[counter].add (new Pair<> (xOrigin--, yOrigin));
                } else {
                    int left = yOrigin;
                    int right = yOrigin;
                    while (AnimationController.grid[jm][left] == 'H' && AnimationController.grid[jm][right] == 'H') {
                        segments[counter].add (new Pair<> (xOrigin, left++));
                        segments[counter].add (new Pair<> (xOrigin, right--));
                    }
                    if (AnimationController.grid[jm][left] != 'H') {
                        segments[counter].add (new Pair<> (xOrigin--, left));
                        segments[counter].add (new Pair<> (xOrigin--, left));
                        while (left > yOrigin) {
                            segments[counter].add (new Pair<> (xOrigin, left--));
                        }
                    } else {
                        segments[counter].add (new Pair<> (xOrigin--, right));
                        segments[counter].add (new Pair<> (xOrigin--, right));
                        while (right < yOrigin) {
                            segments[counter].add (new Pair<> (xOrigin, right++));
                        }
                    }
                }
            }

        }
        for (int i = 0; i < Math.abs (yDiff); i++) {
            if (yDiff > 0) {
                int jm = yOrigin + 1;
                if (AnimationController.grid[xOrigin][jm] != 'H') {
                    segments[counter].add (new Pair<> (xOrigin, yOrigin++));
                } else {
                    int left = xOrigin;
                    int right = xOrigin;
                    while (AnimationController.grid[left][jm] == 'H' && AnimationController.grid[right][jm] == 'H') {
                        segments[counter].add (new Pair<> (left++, yOrigin));
                        segments[counter].add (new Pair<> (right--, yOrigin));
                    }
                    if (AnimationController.grid[left][jm] != 'H') {
                        segments[counter].add (new Pair<> (left, yOrigin++));
                        segments[counter].add (new Pair<> (left, yOrigin++));
                        while (left > xOrigin) {
                            segments[counter].add (new Pair<> (left--, yOrigin));
                        }
                    } else {
                        segments[counter].add (new Pair<> (right, yOrigin++));
                        segments[counter].add (new Pair<> (right, yOrigin++));
                        while (right < xOrigin) {
                            segments[counter].add (new Pair<> (right++, yOrigin));
                        }
                    }
                }
            } else {
                int jm = yOrigin - 1;
                if (AnimationController.grid[xOrigin][jm] != 'H') {
                    segments[counter].add (new Pair<> (xOrigin, yOrigin--));
                } else {
                    int left = xOrigin;
                    int right = xOrigin;
                    while (AnimationController.grid[left][jm] == 'H' && AnimationController.grid[right][jm] == 'H') {
                        segments[counter].add (new Pair<> (left++, yOrigin));
                        segments[counter].add (new Pair<> (right--, yOrigin));
                    }
                    if (AnimationController.grid[left][jm] != 'H') {
                        segments[counter].add (new Pair<> (left, yOrigin--));
                        segments[counter].add (new Pair<> (left, yOrigin--));
                        while (left > xOrigin) {
                            segments[counter].add (new Pair<> (left--, yOrigin));
                        }
                    } else {
                        segments[counter].add (new Pair<> (right, yOrigin--));
                        segments[counter].add (new Pair<> (right, yOrigin--));
                        while (right < xOrigin) {
                            segments[counter].add (new Pair<> (right++, yOrigin));
                        }
                    }
                }

            }
        }
        counter++;
    }
}
