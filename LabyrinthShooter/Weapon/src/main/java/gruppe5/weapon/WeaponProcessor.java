package gruppe5.weapon;

import gruppe5.common.audio.AudioSPI;
import gruppe5.common.bullet.BulletSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.MapSPI;
import gruppe5.common.node.MapNode;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.weapon.Weapon;
import gruppe5.common.weapon.WeaponSPI;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = WeaponSPI.class)
})

/**
 *
 * @author Marc
 */
public class WeaponProcessor implements IEntityProcessingService, IGamePluginService, WeaponSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Weapon.class)) {
            Weapon weapon = (Weapon) entity;
            moveWeapon(weapon);

            weapon.reduceCooldown(gameData.getDelta());
        }
    }

    private void moveWeapon(Weapon weapon) {
        Entity owner = weapon.getOwner();
        if (owner != null) {
            weapon.setX((float) (owner.getX() + Math.cos(owner.getRadians()) * weapon.getRadius()));
            weapon.setY((float) (owner.getY() + Math.sin(owner.getRadians()) * weapon.getRadius()));
            weapon.setRadians(owner.getRadians());
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("WeaponPlugin started");
        MapSPI mapSPI = Lookup.getDefault().lookup(MapSPI.class);

        if (mapSPI != null) {
            for (int i = 0; i < 10; i++) {
                MapNode node = mapSPI.getRandomSpawnNode();
                Weapon weapon = createWeapon(world);
                weapon.setPosition(node.getX(), node.getY());
                world.addEntity(weapon);
                weapon.setSoundPath("Weapon/target/Weapon-1.0.0-SNAPSHOT.jar!/assets/sound/bullet.ogg");
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
            world.removeEntity(weapon);
        }
    }

    @Override
    public Entity equipWeapon(World world, Entity entity) {
        for (Entity weaponEntity : world.getEntities(Weapon.class)) {
            Weapon weapon = (Weapon) weaponEntity;
            float xDiff = Math.abs(weapon.getX() - entity.getX());
            float yDiff = Math.abs(weapon.getY() - entity.getY());
            if (xDiff < 50 && yDiff < 50) {
                Entity oldWeapon = null;
                for (Entity entityWeapon : entity.getEntities(Weapon.class)) {
                    oldWeapon = entityWeapon;
                }
                if (oldWeapon != weapon && weapon.getOwner() == null) {
                    return weapon;
                }
            }
        }
        return null;
    }

    @Override
    public void shoot(World world, Entity entity) {
        Weapon weapon = (Weapon) entity;

        if (weapon.getCooldown() <= 0) {
            BulletSPI bulletSPI = Lookup.getDefault().lookup(BulletSPI.class);
            if (bulletSPI != null) {
                Entity bullet = bulletSPI.createBullet(entity);
                world.addEntity(bullet);
                weapon.setCooldown(0.5f);
                AudioSPI audioSPI = Lookup.getDefault().lookup(AudioSPI.class);

                if (audioSPI != null) {
                    audioSPI.playAudio(weapon);
                }
            }
        }
    }

    @Override
    public Weapon createWeapon(World world) {
        Weapon weapon = new Weapon();
        weapon.setCollidable(false);
        weapon.setDynamic(true);
        weapon.setRadius(GameData.UNIT_SIZE);
        weapon.setImagePath("Weapon/target/Weapon-1.0.0-SNAPSHOT.jar!/assets/images/weapon01.png");

        return weapon;
    }
}
