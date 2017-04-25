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
import gruppe5.common.node.Node;
import gruppe5.common.map.MapSPI;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import java.util.Collections;
import java.util.List;
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
    private Node Node = null;


    @Override
    public void process(GameData gameData, World world) {
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (mapSPI != null) {
            for (Entity entity : world.getEntities(Enemy.class)) {
                Enemy enemy = (Enemy) entity;
                checkPlayerProximity(enemy, world);
                if (enemy.getTarget() != null) {
                    enemyAttack(enemy, world, gameData);
                } else if (enemy.getTargetNode() != null) {
                    //Investigation mode
                } else {
                    //Patrolling mode
                }
                moveTowardsNextNode(enemy, gameData);
            }
        }
    }

    private void checkPlayerProximity(Enemy enemy, World world) {
        if (playerSPI != null) {
            Entity player = playerSPI.getPlayer(world);
            float xDiff = player.getX() - enemy.getX();
            float yDiff = player.getY() - enemy.getY();
            float distance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

            if (distance < 200) {
                enemy.setTarget(player);
            } else {
                enemy.setTarget(null);
            }
        }
    }

    private void enemyAttack(Enemy enemy, World world, GameData gameData) {
        if (playerSPI != null) {
            Entity player = playerSPI.getPlayer(world);
            float dx = player.getX() - enemy.getX();
            float dy = player.getY() - enemy.getY();

            float radians = (float) Math.atan2(dy, dx);
            enemy.setRadians(radians);

            MapNode nextNode = enemy.getNextNode();
            MapNode closestNode = nextNode;
            float closestDistance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

            if (enemy.getX() == closestNode.getX() && enemy.getY() == closestNode.getY()) {
                for (MapNode mapNode : nextNode.getNeighbours()) {
                    float xDiff = player.getX() - mapNode.getX();
                    float yDiff = player.getY() - mapNode.getY();
                    float nodeDistance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
                    
                    if(nodeDistance < closestDistance && mapNode.getNeighbours().size() > 0) {
                        closestDistance = nodeDistance;
                        closestNode = mapNode;
                    }
                }
            }
            
            enemy.setNextNode(closestNode);
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
            float distance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

            if (distance < 1) {
                enemy.setPosition(nextNode.getX(), nextNode.getY());
            } else {
                enemy.setPosition(enemy.getX() + dx * dt, enemy.getY() + dy * dt);
            }

        }
    }

    private void findPath(MapNode startNode, MapNode targetNode, Enemy enemy, GameData gameData) {
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        //node = Lookup.getDefault().lookup(Node.class);
        List map = mapSPI.getMap();

        List<MapNode> openList = null;
        List<MapNode> closedList = null;
        openList.add(startNode);

        while (openList.size() > 0) {
            MapNode current = openList.get(0);
            
            for(int i = 0; i < openList.size(); i++){
                if(openList.get(i).fCost() < current.fCost() || 
                        openList.get(i).fCost() == current.fCost() && 
                        openList.get(i).hCost() < current.hCost()){
                    current = openList.get(i);
                }
                openList.remove(current);
                closedList.add(current);
                
                if(current == targetNode){
                    return;
                }
                
                for(MapNode neighbour : current.getNeighbours()){
                    if(closedList.contains(current)){
                        continue;
                    }
                    int newMovementCostToNeighbour = current.gCost() + getDistance(current,neighbour);
                    if(newMovementCostToNeighbour < neighbour.gCost() || !openList.contains(neighbour)){
                        neighbour.setGCost(newMovementCostToNeighbour);
                        neighbour.setHCost(getDistance(neighbour, targetNode));
                        neighbour.setParent(current);
                        
                        if(!openList.contains(neighbour)){
                            openList.add(neighbour);
                        }
                        
                    }
                    
                }
            }
        }

    }
    
    private void retracePath(MapNode startNode, MapNode targetNode){
        List<MapNode> path = null;
        MapNode current = targetNode;
        
        while(current != startNode){
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        
    }
    
    private int getDistance(MapNode pos1, MapNode pos2){
        int xDis = Math.round(Math.abs(pos1.getX() - pos2.getX()));
        int yDis = Math.round(Math.abs(pos1.getY() - pos2.getY()));
        
        if(xDis > yDis){
            return 14 * yDis + 10 * (xDis - yDis);
        }
        return 14 * xDis + 10 * (yDis - xDis);
    }

    private MapNode getEnemyPosition(Enemy enemy){
        Node enemyPosition = null;
        enemyPosition.setX(enemy.getX());
        enemyPosition.setY(enemy.getY());
        return enemyPosition;
    }
    

}
