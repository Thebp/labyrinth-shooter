/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemy;

import gruppe5.common.audio.AudioSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.enemy.Enemy;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import java.util.Random;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Gyhuji
 */
@ServiceProviders(value
        = @ServiceProvider(service = IEntityProcessingService.class)
)
public class EnemyProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        int enemyCount = 1;
        for (Entity enemy : world.getEntities(Enemy.class)) {

            //Getters
            float dx = enemy.getDx();
            float dy = enemy.getDy();
            
            //Shape     
            float[] shapex = new float[5];
            float[] shapey = new float[5];
            float x = enemy.getX();
            float y = enemy.getY();
            float radians = enemy.getRadians();
            float radius = enemy.getRadius();

            shapex[0] = x + (float) Math.cos(radians) * radius;
            shapey[0] = y + (float) Math.sin(radians) * radius;

            shapex[1] = x + (float) Math.cos(radians - 2 * 3.1415f / 5) * radius;
            shapey[1] = y + (float) Math.sin(radians - 2 * 3.1145f / 5) * radius;

            shapex[2] = x + (float) Math.cos(radians - 4 * 3.1415f / 5) * radius;
            shapey[2] = y + (float) Math.sin(radians - 4 * 3.1415f / 5) * radius;

            shapex[3] = x + (float) Math.cos(radians + 4 * 3.1415f / 5) * radius;
            shapey[3] = y + (float) Math.sin(radians + 4 * 3.1415f / 5) * radius;

            shapex[4] = x + (float) Math.cos(radians + 2 * 3.1415f / 5) * radius;
            shapey[4] = y + (float) Math.sin(radians + 2 * 3.1415f / 5) * radius;

            enemy.setShapeX(shapex);
            enemy.setShapeY(shapey);

            AudioSPI audio = Lookup.getDefault().lookup(AudioSPI.class);
            if (enemy.getLife() <= 0) {
                world.removeEntity(enemy);
                audio.playAudio(enemy.getSoundPath(), enemy);
            }

            //Saving the dx and dy before movement for moveForwards() gets called
            float oldDX = dx;
            float oldDY = dy;
            float oldX = x;
            float oldY = y;
            
            //Reset hit detection
            if(enemy.isHit()) {
                enemy.setIsHit(false);
            }


        }
    }

}