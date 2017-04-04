/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.mapgenerator;

import gruppe5.common.*;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.*;
import gruppe5.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author nick
 */
public class MapGenerator implements MapSPI, IGamePluginService {
    private List<MapNode> nodeList = new ArrayList(); 
    
    
    @Override
    public List<MapNode> getMap() {
        return nodeList;
    }

    @Override
    public void start(GameData gameData, World world) {
        
    }

    @Override
    public void stop(GameData gameData, World world) {
        
    }
    
}
