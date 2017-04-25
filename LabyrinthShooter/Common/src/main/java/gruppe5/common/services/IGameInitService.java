/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.services;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;

/**
 *
 * @author nick
 */
public interface IGameInitService {
    void start(GameData gameData, World world);
    void stop(GameData gameData, World world);
}
