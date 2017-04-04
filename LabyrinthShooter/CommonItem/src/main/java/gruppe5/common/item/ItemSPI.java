/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.item;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

/**
 *
 * @author marcn
 */
public interface ItemSPI {
    Entity pickupItem(World world, Entity entity);
    void useItem(Entity item, Entity user);
}
