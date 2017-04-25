/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.node;

import java.util.List;

/**
 *
 * @author nick
 */
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
}
