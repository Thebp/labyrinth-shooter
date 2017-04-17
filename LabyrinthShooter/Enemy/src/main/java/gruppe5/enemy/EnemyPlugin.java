/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemy;

import gruppe5.common.data.Entity;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Gyhuji
 */
@ServiceProvider(service = IGamePluginService.class)
public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemy(GameData gameData) {
        enemy = new Enemy();

        //Setters for various features of the Enemy entity
        enemy.setPosition(new Float(gameData.getDisplayWidth() / 2.4), //Temporary location for demo -> Right next to the center
                new Float(gameData.getDisplayHeight() / 2.2));
        enemy.setMaxSpeed(2);
        enemy.setAcceleration(2);
        enemy.setDeacceleration(5);
        enemy.setRotationSpeed(1);
        enemy.setRadius(8);
        enemy.setRadians(3.1415f / 1);
        enemy.setCollidable(true);
        enemy.setDynamic(true);
        
        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }

}