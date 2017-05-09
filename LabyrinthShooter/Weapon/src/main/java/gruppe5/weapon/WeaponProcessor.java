package gruppe5.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.weapon.Weapon;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)

public class WeaponProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Weapon.class)) {
            Weapon weapon = (Weapon) entity;
            moveWeapon(weapon);
        }
    }

    private void moveWeapon(Weapon weapon) {
        Entity owner = weapon.getOwner();
        if (owner != null) {
            weapon.setX((float) (owner.getX() + Math.cos(owner.getRadians())* weapon.getRadius()));
            weapon.setY((float) (owner.getY() + Math.sin(owner.getRadians())* weapon.getRadius()));
            weapon.setRadians(owner.getRadians());
        }
    }

}
