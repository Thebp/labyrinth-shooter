package gruppe5.common.bullet;

import gruppe5.common.data.Entity;

public interface BulletSPI {
    Entity createBullet(Entity creator);
}
