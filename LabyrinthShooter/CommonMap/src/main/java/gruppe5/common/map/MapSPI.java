/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.map;

import java.util.List;

/**
 *
 * @author nick
 */
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
