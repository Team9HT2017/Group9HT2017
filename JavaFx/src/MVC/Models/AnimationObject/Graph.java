package MVC.TechnicalFramework.AnimationObjects;

import java.awt.*;
import java.util.*;

/**
 * Class Graph creates graphs used for implementing the dijkstra shortest path algorithm.
 * Source code used from: https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
 *
 * @author Rema Salman
 * @version 1.0
 * Modification: - adding parameters to the class's constructor.
 *               -  adding the class's methods.
 */

public class Graph {
    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

    /**
     * One edge of the graph (only used by Graph constructor)
     */
    public static class Edge {
        public final String v1, v2;
        public final double dist;

        // class constructor
        public Edge (String v1, String v2, double dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /**
     * INNER CLASS: One vertex of the graph, complete with mappings to neighbouring vertices
     */
    public static class Vertex implements Comparable<Vertex> {
        public final Point name;
        public double dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Double> neighbours = new HashMap<> ();

        // Inner class constructor
        public Vertex (String name) {
            String[] coordinate = name.split (",");
            this.name = new Point (Integer.parseInt (coordinate[0]), Integer.parseInt (coordinate[1]));
        }

        /**
         * Method for adding the path to the path array list, used in animation
         */
        private void printPath () {
            if (this == this.previous) {
                System.out.printf ("%s", this.name);
                pathArrayList.add (this.name);
            } else if (this.previous == null) {
                System.out.printf ("%s(unreached)", this.name);
            } else {
                System.out.print (" -> " + this.name + "(" + this.dist + ")");
                pathArrayList.add (this.name);
                this.previous.printPath ();
            }
        }

        public int compareTo (Vertex other) {
            if (dist == other.dist)
                return (name.x + "," + name.y).compareTo (other.name.x + "," + other.name.y);
            return Double.compare (dist, other.dist);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + dist + ")";
        }
    }

    /**
     * Builds a graph from a set of edges
     */
    public Graph (Edge[] edges) {
        graph = new HashMap<> (edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {

            if (!graph.containsKey (e.v1)) graph.put (e.v1, new Vertex (e.v1));
            if (!graph.containsKey (e.v2)) graph.put (e.v2, new Vertex (e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get (e.v1).neighbours.put (graph.get (e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /**
     * Runs dijkstra using a specified source vertex
     */
    public void dijkstra (String startName) {
        if (!graph.containsKey (startName)) {
            System.err.printf ("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get (startName);
        NavigableSet<Vertex> q = new TreeSet<> ();

        // set-up vertices
        for (Vertex v : graph.values ()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add (v);
        }

        dijkstra (q);
    }

    /**
     * Implementation of dijkstra's algorithm using a binary heap.
     */
    private void dijkstra (final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty ()) {

            u = q.pollFirst (); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE)
                break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Double> a : u.neighbours.entrySet ()) {
                v = a.getKey (); //the neighbour in this iteration

                final double alternateDist = u.dist + a.getValue ();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove (v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add (v);
                }
            }
        }
    }

    public static ArrayList<Point> pathArrayList = new ArrayList<> (); // adding the path array

    /**
     * Prints a path from the source to the specified vertex
     */
    public void printPath (String endName) {
        if (!graph.containsKey (endName)) {
            System.err.printf ("Graph doesn't contain end vertex \"%s\"\n", endName);
            return;
        }
        graph.get (endName).printPath ();
        System.out.println ();
    }

}