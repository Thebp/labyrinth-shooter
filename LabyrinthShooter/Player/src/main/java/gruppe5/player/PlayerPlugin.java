package gruppe5.player;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.node.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.services.IGamePluginService;
import java.io.File;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)

public class PlayerPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        
        Entity player = createPlayer(gameData);
        world.addEntity(player);
        System.out.println("Playerplugin started");
        System.out.println(new File("").getAbsolutePath());
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Player.class)) {
            world.removeEntity(entity);
        }
        System.out.println("PlayerPlugin stopped");
    }

    private Entity createPlayer(GameData gameData) {
        MapSPI mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        Player player = new Player();
        if(mapSPI != null){
        MapNode spawnPoint = mapSPI.getRandomSpawnNode();
        player.setPosition(spawnPoint.getX(),spawnPoint.getY());
        }else{
            player.setPosition(gameData.getDisplayWidth()/2, gameData.getDisplayHeight()/2);
        }
        player.setAcceleration(1000);
        player.setMaxSpeed(100);
        player.setDeacceleration(5);
        player.setShapeX(new float[8]);
        player.setShapeY(new float[8]);
        player.setRadius(GameData.UNIT_SIZE);
        player.setRadians(3.1415f / 2);
        player.setRotationSpeed(3);
        player.setCollidable(true);
        player.setDynamic(true);
        player.setLife(10);
        player.setImagePath("Player/target/Player-1.0.0-SNAPSHOT.jar!/assets/images/player.png");
        player.setSoundPath("Player/target/Player-1.0.0-SNAPSHOT.jar!/assets/sound/playerdeath.ogg");

        return player;
    }

}
