package gruppe5.enemy;

import gruppe5.common.data.Entity;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.node.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.services.IGamePluginService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)
public class EnemyPlugin implements IGamePluginService {

    private Entity enemy;

    public EnemyPlugin() {

    }

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemy(gameData);
        world.addEntity(enemy);
        System.out.println("Enemy plugin started");
    }

    private Entity createEnemy(GameData gameData) {
        enemy = new Enemy();
        MapNode spawnlocation = null;
        MapSPI mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        if (mapSPI != null) {
            spawnlocation = mapSPI.getRandomSpawnNode();
            enemy.setPosition(spawnlocation.getX(),
                    spawnlocation.getY());
        } else {
            enemy.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        }

        //Setters for various features of the Enemy entity
        enemy.setMaxSpeed(50);
        enemy.setAcceleration(10);
        enemy.setDeacceleration(15);
        enemy.setRotationSpeed(20);
        enemy.setRadius(8);
        enemy.setRadians(3.1415f / 1);
        enemy.setCollidable(true);
        enemy.setDynamic(true);
        enemy.setLife(2);
        enemy.setSoundPath("Enemy/target/Enemy-1.0.0-SNAPSHOT.jar!/assets/sound/enemydeath.ogg");

        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
        System.out.println("Enemy plugin stopped");
    }

}
