package gruppe5.common.item;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

public interface ItemSPI {
    Entity pickupItem(World world, Entity entity);
    void useItem(Entity item, Entity user);
}
