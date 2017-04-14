/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.bullet;

import gruppe5.common.bullet.Bullet;
import gruppe5.common.bullet.BulletSPI;
import gruppe5.common.data.Entity;
import org.openide.util.lookup.ServiceProvider;


@ServiceProvider(service = BulletSPI.class)
/**
 *
 * @author marcn
 */
public class BulletCreator implements BulletSPI{

    @Override
    public Entity createBullet(Entity creator) {
        Entity bullet = new Bullet();
        bullet.setAcceleration(1000);
        bullet.setCollidable(true);
        bullet.setDamage(1);
        bullet.setDeacceleration(0);
        bullet.setDx(creator.getDx());
        bullet.setDy(creator.getDy());
        bullet.setExpiration(3);
        bullet.setLife(1);
        bullet.setMaxSpeed(300);
        bullet.setPosition(creator.getX(), creator.getY());
        bullet.setX(creator.getX() + (float) Math.cos(creator.getRadians()) * (creator.getRadius() + bullet.getRadius() + 1));
        bullet.setY(creator.getY() + (float) Math.sin(creator.getRadians()) * (creator.getRadius() + bullet.getRadius() + 1));
        bullet.setRadians(creator.getRadians());
        bullet.setRadius(8);
        bullet.setRotationSpeed(0);
        bullet.setShapeX(new float[4]);
        bullet.setShapeY(new float[4]);
        
        return bullet;
    }
    
}
