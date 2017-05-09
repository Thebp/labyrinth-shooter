package gruppe5.common.map;

import gruppe5.common.node.MapNode;
import java.util.List;

public interface MapSPI {
    /**
     * 
     * @return A list of all MapNodes in the game.
     */
    public List<MapNode> getMap();
    /**
     * Returns all center nodes, i.e. nodes that do not touch walls
     * @return 
     */
    public List<MapNode> getCenterMapNodes();
    /**
     * Finds and reserves a MapNode such that this MapNode won't be returned again by this function
     * @return 
     */
    public MapNode getRandomSpawnNode();
}
