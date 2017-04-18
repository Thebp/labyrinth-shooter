/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.player;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christian
 */
@ServiceProvider(service = IGamePluginService.class)

public class PlayerPlugin implements IGamePluginService {

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
        Player player = new Player();

        player.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        player.setAcceleration(1000);
        player.setMaxSpeed(100);
        player.setDeacceleration(5);
        player.setShapeX(new float[4]);
        player.setShapeY(new float[4]);
        player.setRadius(8);
        player.setRadians(3.1415f / 2);
        player.setRotationSpeed(5);
        player.setCollidable(true);
        player.setDynamic(true);
        player.setLife(3);

        return player;
    }

}
