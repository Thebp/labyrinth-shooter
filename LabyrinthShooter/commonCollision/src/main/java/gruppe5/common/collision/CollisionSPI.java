/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.collision;

import gruppe5.common.data.Entity;
import gruppe5.common.data.World;

/**
 *
 * @author Daniel
 */
public interface CollisionSPI {
    Boolean checkCollision(Entity entity, World world);
}
