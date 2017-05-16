package gruppe5.common.collision;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

public interface CollisionSPI {
    Boolean checkCollision(Entity entity, World world);
}
