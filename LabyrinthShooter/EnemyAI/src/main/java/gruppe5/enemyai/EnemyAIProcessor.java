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
import gruppe5.common.node.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import java.util.List;
import java.util.PriorityQueue;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author marcn
 */
public class EnemyAIProcessor implements IEntityProcessingService {

    private MapSPI mapSPI = null;
    private MapNode mapNode = null;
    private PlayerSPI playerSPI = null;

    @Override
    public void process(GameData gameData, World world) {
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (mapSPI != null) {
            for (Entity entity : world.getEntities(Enemy.class)) {
                Enemy enemy = (Enemy) entity;
                checkPlayerProximity(enemy, world);
                if (enemy.getTarget() != null) {
                    enemyAttack(enemy, world);
                } else if (enemy.getTargetNode() != null) {
                    //Investigation mode
                } else {
                    //Patrolling mode
                }
            }
        }
    }

    private void checkPlayerProximity(Enemy enemy, World world) {
        if(playerSPI != null) {
            Entity player = playerSPI.getPlayer(world);
            float xDiff = Math.abs(player.getX() - enemy.getX());
            float yDiff = Math.abs(player.getY() - enemy.getY());
            float distance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            
            if(distance < 200) {
                enemy.setTarget(player);
            } else {
                enemy.setTarget(null);
            }
        }
    }
    
    private void enemyAttack(Enemy enemy, World world) {
        if(playerSPI != null) {
            Entity player = playerSPI.getPlayer(world);
            float dx = Math.abs(player.getX() - enemy.getX());
            float dy = Math.abs(player.getY() - enemy.getY());
            
            float radians = (float) Math.atan2(dy, dx);
            enemy.setRadians(radians);
        }
    }

    private void moveTowardsNextNode(Enemy enemy, GameData gameData) {
        MapNode nextNode = enemy.getNextNode();
        if (enemy.getX() != nextNode.getX() || enemy.getY() != nextNode.getY()) {
            float dx = nextNode.getX() - enemy.getX();
            float dy = nextNode.getY() - enemy.getY();
            float dt = gameData.getDelta();
            float maxspeed = enemy.getMaxSpeed();
            float x = enemy.getX();
            float y = enemy.getY();

            //Normalize
            float length = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            dx = dx / length;
            dy = dy / length;

            //Set speed vector
            dx = dx * maxspeed;
            dy = dy * maxspeed;

            x += dx * dt;
            y += dy * dt;

            float xDiff = Math.abs(x - nextNode.getX());
            float yDiff = Math.abs(y - nextNode.getY());

            if (xDiff < 5 && yDiff < 5) {
                enemy.setPosition(nextNode.getX(), nextNode.getY());
            } else {
                enemy.setPosition(enemy.getX() + dx * dt, enemy.getY() + dy * dt);
            }

        }
    }
    
    private void findPath(MapNode startNode, MapNode targetNode, Enemy enemy, GameData gameData){
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        mapNode = Lookup.getDefault().lookup(MapNode.class);
        List map = mapSPI.getMap();
        
        
        List<MapNode> openList = null;
        List<MapNode> closedList;
        openList.add(startNode);
        
        while(openList.size() > 0){
            MapNode current = openList.get(0);
            
            for(int i = 0; i < openList.size(); i++){
                
            }
        }
        
        
    }
    
    private float getDistance(MapNode pos1, MapNode pos2){
        float xDis = Math.abs(pos1.getX() - pos2.getX());
        float yDis = Math.abs(pos1.getY() - pos2.getY());
        
        if(xDis > yDis){
            return 14 * yDis + 10 * (xDis - yDis);
        }
        return 14 * xDis + 10 * (yDis - xDis);
    }
    


}
