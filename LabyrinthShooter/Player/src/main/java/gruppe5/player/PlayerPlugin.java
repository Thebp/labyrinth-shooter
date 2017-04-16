/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.player;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.player.Player;
import gruppe5.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christian
 */
@ServiceProvider(service = IGamePluginService.class)

public class PlayerPlugin implements IGamePluginService{

    @Override
    public void start(GameData gameData, World world) {
        Entity player = createPlayer(gameData);
        world.addEntity(player);
        System.out.println("Playerplugin started");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Player.class)) {
            world.removeEntity(entity);
        }
        System.out.println("PlayerPlugin stopped");
    }

    private Entity createPlayer(GameData gameData) {
        Entity player = new Entity();

        player.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        player.setAcceleration(100);
        player.setMaxSpeed(200);
        player.setDeacceleration(10);
        player.setShapeX(new float[8]);
        player.setShapeY(new float[8]);
        player.setRadius(8);
        player.setRadians(3.1415f / 2);
        player.setCollidable(true);
        //player.setType("Player");

        return player;
    }
    
}
