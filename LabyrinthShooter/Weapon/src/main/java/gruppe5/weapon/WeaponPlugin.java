package gruppe5.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.node.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.weapon.Weapon;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)

public class WeaponPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("WeaponPlugin started");
        MapSPI mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        
        if (mapSPI != null) {
            for(int i = 0; i < 20; i++) {
                MapNode node = mapSPI.getRandomSpawnNode();
                Weapon weapon = new Weapon();
                weapon.setCollidable(false);
                weapon.setDynamic(true);
                weapon.setPosition(node.getX(), node.getY());
                weapon.setRadius(GameData.UNIT_SIZE);
                weapon.setImagePath("Weapon/target/Weapon-1.0.0-SNAPSHOT.jar!/assets/images/weapon01.png");
                weapon.setSoundPath("Weapon/target/Weapon-1.0.0-SNAPSHOT.jar!/assets/sound/bullet.ogg");
                world.addEntity(weapon);
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
            world.removeEntity(weapon);
        }
        System.out.println("Weapon plugin stopped");
    }

}
