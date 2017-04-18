/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.weapon;

import gruppe5.common.bullet.BulletSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.World;
import gruppe5.common.weapon.Weapon;
import gruppe5.common.weapon.WeaponSPI;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = WeaponSPI.class)
/**
 *
 * @author Marc
 */
public class WeaponSPIImpl implements WeaponSPI{

    @Override
    public Entity equipWeapon(World world, Entity entity) {
        for(Entity weapon : world.getEntities(Weapon.class)) {
            float xDiff = Math.abs(weapon.getX() - entity.getX());
            float yDiff = Math.abs(weapon.getY() - entity.getY());
            if(xDiff < 50 && yDiff < 50) {
                return weapon;
            }
        }
        return null;
    }

    @Override
    public void shoot(World world, Entity entity) {
        Entity bullet = Lookup.getDefault().lookup(BulletSPI.class).createBullet(entity);
        world.addEntity(bullet);
    }
    
}
