package Haus;

public class DjikstraNode {
    public int x, y;
    public int[] nodeDistances;
    DrawableObject affiliatedHouse;
    public DjikstraNode(int x, int y, int[] nodeDistances, DrawableObject affiliatedHouse)
    {
        this.x = x;
        this.y = y;
        this.nodeDistances = nodeDistances;
        this.affiliatedHouse = affiliatedHouse;
    }
}
