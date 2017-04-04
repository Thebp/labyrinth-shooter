/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.bullet;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;

/**
 *
 * @author marcn
 */
public interface BulletSPI {
    Entity createBullet(GameData gameData, Entity creator);
}
