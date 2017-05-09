package gruppe5.common.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

public interface WeaponSPI {
    Entity equipWeapon(World world, Entity entity);
    void shoot(World world, Entity entity);
}
