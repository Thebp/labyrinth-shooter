package gruppe5.bullet;

import gruppe5.common.bullet.Bullet;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)

public class BulletPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Bullet plugin started");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            world.removeEntity(bullet);
        }
        System.out.println("Bullet plugin stopped");
    }

}
