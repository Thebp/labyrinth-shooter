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
import gruppe5.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author marcn
 */
public class EnemyAIProcessor implements IEntityProcessingService{

    @Override
    public void process(GameData gameData, World world) {
        for(Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;
            
            if(enemy.getTarget() != null) {
                //Attack mode
            } else if(enemy.getTargetNode() != null) {
                //Investigation mode
            } else {
                //Patrolling mode
            }
        }
    }
    
    private void checkPlayerVisibility(Enemy enemy, World world) {
        
    }
    
}
