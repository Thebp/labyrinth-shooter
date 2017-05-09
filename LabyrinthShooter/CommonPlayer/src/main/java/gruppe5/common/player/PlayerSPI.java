package gruppe5.common.player;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

public interface PlayerSPI {
    Entity getPlayer(World world);
}
