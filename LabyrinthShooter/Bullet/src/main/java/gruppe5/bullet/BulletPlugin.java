/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.bullet;

import gruppe5.common.bullet.Bullet;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;

/**
 *
 * @author marcn
 */
public class BulletPlugin implements IGamePluginService{

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Bullet plugin started");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity bullet : world.getEntities(Bullet.class)) {
            world.removeEntity(bullet);
        }
    }
    
}
