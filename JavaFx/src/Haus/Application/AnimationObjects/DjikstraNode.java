package Haus.Application.AnimationObjects;

public class DjikstraNode {
    public int x, y;
    public int[] nodeDistances;
    DrawableObject affiliatedHouse;
    public DjikstraNode(int x, int y, DrawableObject affiliatedHouse)
    {
        this.x = x;
        this.y = y;
        this.affiliatedHouse = affiliatedHouse;
    }
}
