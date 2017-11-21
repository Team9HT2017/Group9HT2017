package Haus.Application.AnimationObjects;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class DjikstraNode {
	public int x, y;
	public double[] nodeDistances;
	public DrawableObject affiliatedHouse;
	public static ArrayList<DjikstraNode> dijkstraNodes;
	public ArrayList<DjikstraNode> neigbours; // an array for saving the connections between the Dijkstra nodes
	boolean visited;

	public DjikstraNode(int x, int y, DrawableObject affiliatedHouse) {
		this.x = x;
		this.y = y;
		this.affiliatedHouse = affiliatedHouse;
		System.out.println("this dijNode is" + this.x + "," + this.y + affiliatedHouse.name);
		neigbours=new ArrayList<DjikstraNode>();
	}

	
	/**
	 * Method depending on math to calculate the distance between 2 Dijkstra nodes
	 * 
	 * @param dijkstraNodeFrom:
	 *            the dijkstra node that the mail will from.
	 * @param dijkstraNodeTo
	 *            the dijkstra node the mail will be traveling to.
	 */
	public double distance(DjikstraNode dijkstraNodeFrom, DjikstraNode dijkstraNodeTo) {
		double res = 0;
		res = Math.sqrt(Math.pow(dijkstraNodeFrom.x - dijkstraNodeTo.x, 2)
				+ Math.pow(dijkstraNodeFrom.y - dijkstraNodeTo.y, 2));
		return res;
	}

	public void addNiegbours(ArrayList<DjikstraNode> tree) {
		for(DjikstraNode dn:tree) {
			if(dn.x==x &&dn.y==y)
				continue;
			if(dn.x==x || dn.y==y) {
				neigbours.add(dn);
				System.out.println(this.x+","+this.y +"  has n "+dn.x+","+dn.y );
			}
		}
		nodeDistances=new double[neigbours.size()];
		for(int i=0;i<neigbours.size();i++) {
			nodeDistances[i]=distance(this,neigbours.get(i));
			System.out.println(this.x+","+this.y +"  and "+neigbours.get(i).x+","+neigbours.get(i).y +" distance is "+nodeDistances[i]);
		}
	}
	
	// method for adding the connection to the list
	public void addConnection(DjikstraNode d) {
		neigbours.add(d);
	}

	/**
	 * Method to check if the D is connected to the node in the argument or not
	 * 
	 * @param dijkstraNode:
	 *            the next dijkstra node the mail will travel to.
	 * 
	 */
	public boolean isConnectedTo(DjikstraNode d) {
		if (neigbours.size() == 0)
			return false;
		for (int i = 0; i < neigbours.size(); i++) {
			if (neigbours.get(i).x == (d.x) && neigbours.get(i).y == (d.y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to check if there is another dijkstraNode connected and not visited 
	 * 
	 * @param dijkstraNode:
	 *            the next dijkstra node the mail will travel to.
	 * 
	 */
	 public static boolean otherNotConnectedExists() {
	 for (DjikstraNode e : dijkstraNodes) {
	 if (!e.visited)
	 return true;
	 }
	 return false;
	 }
	 
	 //source https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
	 public static void shortestPathAlgorithm(DjikstraNode source,DjikstraNode destination,ArrayList<DjikstraNode> nodes) {	
		 ArrayList<Graph.Edge> edges=new ArrayList<Graph.Edge>();
		 for(DjikstraNode dji: nodes)
			 for(int i=0;i<dji.neigbours.size();i++) {
				 edges.add(new Graph.Edge(dji.x+","+dji.y, dji.neigbours.get(i).x+","+dji.neigbours.get(i).y, dji.nodeDistances[i]));
				 System.out.println(dji.x+","+dji.y+"-->"+dji.neigbours.get(i).x+","+dji.neigbours.get(i).y+"  d="+ dji.nodeDistances[i]);
			 }
		
		 Graph g=new Graph(edges.toArray(new Graph.Edge[edges.size()]));
		 g.dijkstra(source.x+","+source.y);
		 g.printPath(destination.x+","+destination.y);
	 }
	

	 @Override
	 public boolean equals(Object o) {
		 return (o instanceof DjikstraNode)&&(x==((DjikstraNode)o).x && y==((DjikstraNode)o).y);
	 }
	 
	 

}
