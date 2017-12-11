package MVC.Models.AnimationObject;

import MVC.Models.AnimationObject.Graph;

import java.util.ArrayList;

/**
 * Class dijkstra nodes creation and implements the dijkstra shortest path algorithm.
 *  Source code used from: https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
 *
 * @author Leo Persson
 * @version 1.0
 * Creation: - adding variables declarations.
 *           - class's constructor.
 *
 * @author Rema Salman
 * @version 1.1
 * Modification: - adding parameters to the class's constructor.
 *               -  adding the class's methods.
 */

public class DjikstraNode {
    public int x, y;
    public char type;
    public String name;

    public double[] nodeDistances;
    public ArrayList<DjikstraNode> neigbours; // an array for saving the connections between the Dijkstra nodes

    /**
     * Class constructor
     */
    public DjikstraNode (int x, int y, char type, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.type = type;
        neigbours = new ArrayList<DjikstraNode> ();
    }

    /**
     * Method depending on math to calculate the distance between 2 Dijkstra nodes
     *
     * @param dijkstraNodeFrom
     * @param dijkstraNodeTo
     * @return
     */
    public double distance(DjikstraNode dijkstraNodeFrom, DjikstraNode dijkstraNodeTo) {
        // dijkstraNodeFrom: the dijkstra node that the mail will from.
        // dijkstraNodeTo: the dijkstra node the mail will be traveling to.
        double res = 0;
        res = Math.sqrt (Math.pow (dijkstraNodeFrom.x - dijkstraNodeTo.x, 2)
                + Math.pow (dijkstraNodeFrom.y - dijkstraNodeTo.y, 2));
        return res; // returning the distance between the source and destination
    }

    /**
     * Method adds niegbours into the specific node of the djikstra nodes tree
     *
     * @param tree
     */
    public void addNiegbours(ArrayList<DjikstraNode> tree) {
        for (DjikstraNode dn : tree) {
            if (dn.x == x && dn.y == y)
                continue;
            if (dn.x == x) {
                if (dn.type != 'H' && type != 'H') {
                    neigbours.add (dn);
                    System.out.println (this.x + "," + this.y + "  has n " + dn.x + "," + dn.y);
                }
            }
            if (dn.y == y) {
                neigbours.add (dn);
            }
        }
        nodeDistances = new double[neigbours.size ()];
        for (int i = 0; i < neigbours.size (); i++) {
            nodeDistances[i] = distance (this, neigbours.get (i));
            System.out.println (this.x + "," + this.y + "  and " + neigbours.get (i).x + "," + neigbours.get (i).y + " distance is " + nodeDistances[i]);
        }
    }

    /**
     * Method implements the shortest path algorithm from the argument passed source and argument passed destination.
     * The method uses the Graph.java where each dijkstra node is added as an edge in the Graph
     *
     * @param source
     * @param destination
     * @param nodes
     */
    //source https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
    public static void shortestPathAlgorithm(DjikstraNode source, DjikstraNode destination, ArrayList<DjikstraNode> nodes) {
        ArrayList<Graph.Edge> edges = new ArrayList<Graph.Edge> ();
        for (DjikstraNode dji : nodes)
            for (int i = 0; i < dji.neigbours.size (); i++) {
                edges.add (new Graph.Edge(dji.x + "," + dji.y, dji.neigbours.get (i).x + "," + dji.neigbours.get (i).y, dji.nodeDistances[i]));
            }
        Graph g = new Graph(edges.toArray (new Graph.Edge[edges.size ()]));
        g.dijkstra (source.x + "," + source.y);
        g.printPath (destination.x + "," + destination.y);
    }
}
