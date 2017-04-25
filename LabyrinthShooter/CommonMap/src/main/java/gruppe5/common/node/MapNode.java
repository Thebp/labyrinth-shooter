package gruppe5.common.node;

import java.util.List;

public interface MapNode {
    /**
     * 
     * @return Returns a list of the MapNode's neighbours.
     */
    public List<MapNode> getNeighbours();
    /**
     * 
     * @return A boolean value that indicates whether this node is the center
     * node in a path.
     */
    public boolean isMiddle();
    /**
     * 
     * @return The corresponding X-coordinate in the game world.
     */
    public float getX();
    /**
     * 
     * @return The corresponding Y-coordinate in the game world.
     */
    public float getY();
    
    public float fCost();
    
    public int gCost();
    
    public int hCost();
    
    public MapNode getParent();
    
    public void setGCost(int gCost);
    
    public void setHCost(int gCost);
    
    public void setParent(MapNode parent);
}
