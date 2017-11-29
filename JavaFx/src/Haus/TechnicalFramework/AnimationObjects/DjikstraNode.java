package Haus.TechnicalFramework.AnimationObjects;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class DjikstraNode {
    public int x, y;
    public double[] nodeDistances;

    public ArrayList<DjikstraNode> neigbours; // an array for saving the connections between the Dijkstra nodes
    public char type;
    public String name;


    public DjikstraNode (int x, int y, char type, String name) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.name = name;
        //System.out.println("this dijNode is" + this.x + "," + this.y + affiliatedHouse.name);
        neigbours = new ArrayList<DjikstraNode> ();
    }


    /**
     * Method depending on math to calculate the distance between 2 Dijkstra nodes
     *
     * @param dijkstraNodeFrom: the dijkstra node that the mail will from.
     * @param dijkstraNodeTo    the dijkstra node the mail will be traveling to.
     */
    public double distance (DjikstraNode dijkstraNodeFrom, DjikstraNode dijkstraNodeTo) {
        double res = 0;
        res = Math.sqrt (Math.pow (dijkstraNodeFrom.x - dijkstraNodeTo.x, 2)
                + Math.pow (dijkstraNodeFrom.y - dijkstraNodeTo.y, 2));
        return res;
    }

    /**
     * Method for adding niegbours (tree) to the node
     *
     * @param tree: the dijkstra node that the mail will from.
     */
    public void addNiegbours (ArrayList<DjikstraNode> tree) {
        for (DjikstraNode dn : tree) {
            //dont look at the same point
            if (dn.x == x && dn.y == y)
                continue;
            //adding the neigbours if they are on the same x axis and not houses
            if (dn.x == x) {
                if (dn.type != 'H' && type != 'H') {
                    neigbours.add (dn);
                }
            }
            if (dn.y == y) {
                neigbours.add (dn);
            }
        }
        // each node distance with its nneigbours
        nodeDistances = new double[neigbours.size ()];
        for (int i = 0; i < neigbours.size (); i++) {
            nodeDistances[i] = distance (this, neigbours.get (i));
            // System.out.println (this.x + "," + this.y + "  and " + neigbours.get (i).x + "," + neigbours.get (i).y + " distance is " + nodeDistances[i]);
        }
    }

    /**
     * Method for finding the shortest path between the nodes(houses)
     *
     *@param source: the next dijkstra node the mail will travel from.
     *@param destination: the next dijkstra node the mail will travel to.
     * @param nodes: the next dijkstra nodes array list.
     */
    //source https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
    public static void shortestPathAlgorithm (DjikstraNode source, DjikstraNode destination, ArrayList<DjikstraNode> nodes) {
        ArrayList<Graph.Edge> edges = new ArrayList<Graph.Edge> ();
        for (DjikstraNode dji : nodes)
            for (int i = 0; i < dji.neigbours.size (); i++) {
                edges.add (new Graph.Edge (dji.x + "," + dji.y, dji.neigbours.get (i).x + "," + dji.neigbours.get (i).y, dji.nodeDistances[i]));
                System.out.println (dji.x + "," + dji.y + "-->" + dji.neigbours.get (i).x + "," + dji.neigbours.get (i).y + "  d=" + dji.nodeDistances[i]);
            }

        Graph g = new Graph (edges.toArray (new Graph.Edge[edges.size ()]));
        g.dijkstra (source.x + "," + source.y);
        g.printPath (destination.x + "," + destination.y);
    }


//    @Override
//    public boolean equals (Object o) {
//        return (o instanceof DjikstraNode) && (x == ((DjikstraNode) o).x && y == ((DjikstraNode) o).y);
//    }


}
