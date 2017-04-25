/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import gruppe5.common.enemy.Enemy;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.services.IEntityProcessingService;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author marcn
 */
public class EnemyAIProcessor implements IEntityProcessingService {
    private MapSPI mapSPI = null;
    @Override
    public void process(GameData gameData, World world) {
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        if (mapSPI != null) {
            for (Entity entity : world.getEntities(Enemy.class)) {
                Enemy enemy = (Enemy) entity;
                moveTowardsNextNode(enemy, gameData);
//                if (enemy.getTarget() != null) {
//                    //Attack mode
//                } else if (enemy.getTargetNode() != null) {
//                    //Investigation mode
//                } else {
//                    //Patrolling mode
//                }
            }
        }
    }

    private void checkPlayerVisibility(Enemy enemy, World world) {

    }

    private void moveTowardsNextNode(Enemy enemy, GameData gameData) {
        MapNode nextNode = enemy.getNextNode();
        if(enemy.getX() != nextNode.getX() || enemy.getY() != nextNode.getY()) {
            float dx = nextNode.getX() - enemy.getX();
            float dy = nextNode.getY() - enemy.getY();
            float dt = gameData.getDelta();
            float maxspeed = enemy.getMaxSpeed();
            
            //Normalize
            float length = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            dx = dx / length;
            dy = dy / length;
            
            //Set speed vector
            dx = dx * maxspeed;
            dy = dy * maxspeed;
            
            enemy.setPosition(enemy.getX() + dx * dt, enemy.getY() + dy * dt);
            
        }
    }

}
