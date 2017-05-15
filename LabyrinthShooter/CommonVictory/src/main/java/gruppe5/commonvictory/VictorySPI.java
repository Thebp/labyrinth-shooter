/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.commonvictory;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;

/**
 *
 * @author nick
 */
public interface VictorySPI {
    /**
     * Should be called when a level is complete. The implementor makes sure
     * to do all necessary tasks.
     * @param gameData
     * @param world
     */
    void setLevelComplete(GameData gameData, World world);
}
