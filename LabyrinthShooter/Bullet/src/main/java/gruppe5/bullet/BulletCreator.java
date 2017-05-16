package gruppe5.bullet;

import gruppe5.common.bullet.Bullet;
import gruppe5.common.bullet.BulletSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = BulletSPI.class)

public class BulletCreator implements BulletSPI {
    private static final int N_BULLET_SPRITES = 2; // Number of bullet sprites
    private static final Random rand = new Random();
    
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
        bullet.setX(creator.getX() + (float) Math.cos(creator.getRadians()) * (creator.getRadius() / 3));
        bullet.setY(creator.getY() + (float) Math.sin(creator.getRadians()) * (creator.getRadius() / 3));
        bullet.setRadians(creator.getRadians());
        bullet.setRadius(GameData.UNIT_SIZE / 4);
        bullet.setRotationSpeed(0);
        bullet.setShapeX(new float[4]);
        bullet.setShapeY(new float[4]);
        bullet.setImagePath("Bullet/target/Bullet-1.0.0-SNAPSHOT.jar!/assets/images/bullet" + rand.nextInt(N_BULLET_SPRITES) + ".png");

        return bullet;
    }

}
