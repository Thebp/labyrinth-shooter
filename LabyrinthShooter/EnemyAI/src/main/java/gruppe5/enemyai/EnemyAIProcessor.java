/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import gruppe5.common.audio.AudioSPI;
import gruppe5.common.bullet.Bullet;
import gruppe5.common.collision.CollisionSPI;
import gruppe5.common.enemy.Enemy;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.node.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.weapon.Weapon;
import gruppe5.common.weapon.WeaponSPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author marcn
 */
public class EnemyAIProcessor implements IEntityProcessingService {

    private MapSPI mapSPI = null;
    private PlayerSPI playerSPI = null;
    private CollisionSPI collisionSPI = null;

    private Entity currentTargetBullet;

    @Override
    public void process(GameData gameData, World world) {
        mapSPI = Lookup.getDefault().lookup(MapSPI.class);
        playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        collisionSPI = Lookup.getDefault().lookup(CollisionSPI.class);
        Entity player = null;
        if (playerSPI != null) {
            player = playerSPI.getPlayer(world);
        }
        if (mapSPI != null) {
            checkBullets(world);
            for (Entity entity : world.getEntities(Enemy.class)) {
                Enemy enemy = (Enemy) entity;
                checkPlayerProximity(enemy, player, world);

                if (enemy.getTarget() != null) {
                    enemyAttack(enemy, world, gameData, player);
                } else {
                    pathRequest(enemy, gameData);
                }
                moveTowardsNextNode(enemy, gameData);
            }
        }
    }

    /**
     * Checks for bullets and sets target as bullet.
     * @param world 
     */
    private void checkBullets(World world) {
        List<Entity> bullets = world.getEntities(Bullet.class);

        if (bullets.size() >= 1) {
            Entity bullet = bullets.get(0);
            if (bullet != currentTargetBullet) {
                MapNode closestNode = getClosestNode(bullets.get(0).getX(), bullets.get(0).getY());
                if (closestNode != null) {
                    for (Entity entity : world.getEntities(Enemy.class)) {
                        Enemy enemy = (Enemy) entity;
                        if (enemy.getTarget() == null) {
                            enemy.setTargetNode(closestNode);
                        }
                    }
                }
                currentTargetBullet = bullet;
            }
        }
    }

    /**
     * Gets closest node to the coordinates given.
     * @param x
     * @param y
     * @return 
     */
    private MapNode getClosestNode(float x, float y) {
        if (mapSPI != null) {
            List<MapNode> nodes = mapSPI.getCenterMapNodes();

            if (nodes != null && nodes.size() > 0) {
                MapNode closestNode = nodes.get(0);
                float closestDistance = getDistance(closestNode.getX(), closestNode.getY(), x, y);

                for (int i = 1; i < nodes.size(); i++) {
                    MapNode node = nodes.get(i);
                    float distance = getDistance(node.getX(), node.getY(), x, y);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestNode = node;
                    }
                }

                return closestNode;
            }

        }
        return null;

    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float xDiff = x1 - x2;
        float yDiff = y1 - y2;
        return (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
    }

    /**
     * If close to Player and in direct sight, set player as target node.
     *
     * @param enemy
     * @param player
     * @param world
     */
    private void checkPlayerProximity(Enemy enemy, Entity player, World world) {
        if (player != null) {
            float xDiff = player.getX() - enemy.getX();
            float yDiff = player.getY() - enemy.getY();
            float distance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

            if (distance < 7 * GameData.UNIT_SIZE && checkPlayerVisibility(enemy, player, world)) {
                enemy.setTarget(player);
            } else if (enemy.getTarget() != null) {
                if (mapSPI != null) {
                    MapNode closestNode = enemy.getNextNode();
                    float closestDistance = distance;
                    for (MapNode node : mapSPI.getMap()) {
                        float nodeXDiff = player.getX() - node.getX();
                        float nodeYDiff = player.getY() - node.getY();
                        float nodeDistance = (float) Math.sqrt(Math.pow(nodeXDiff, 2) + Math.pow(nodeYDiff, 2));

                        if (nodeDistance < closestDistance) {
                            closestNode = node;
                            closestDistance = nodeDistance;
                        }
                    }

                    enemy.setTargetNode(closestNode);

                }
                enemy.setTarget(null);
            }
        }
    }

    /**
     * Checks if player is in direct sight.
     *
     * @param enemy
     * @param player
     * @param world
     * @return
     */
    private boolean checkPlayerVisibility(Enemy enemy, Entity player, World world) {
        if (collisionSPI != null) {
            //Entity entity = new Entity();
            float[] shapex = new float[4];
            float[] shapey = new float[4];

            shapex[0] = enemy.getX();
            shapey[0] = enemy.getY();

            shapex[1] = enemy.getX();
            shapey[1] = enemy.getY();

            shapex[2] = player.getX();
            shapey[2] = player.getY();

            shapex[3] = player.getX();
            shapey[3] = player.getY();

            Entity visibility = new Visibility();

            visibility.setShapeX(shapex);
            visibility.setShapeY(shapey);

            visibility.setCollidable(true);
            visibility.setX(enemy.getX());
            visibility.setY(enemy.getY());
            visibility.setDynamic(false);

            if (!collisionSPI.checkCollision(visibility, world)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Should be called when in attack state. Moves enemy and shoots towards
     * Player.
     *
     * @param enemy
     * @param world
     * @param gameData
     * @param player
     */
    private void enemyAttack(Enemy enemy, World world, GameData gameData, Entity player) {
        if (player != null) {

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

                    if (nodeDistance < closestDistance && mapNode.getNeighbours().size() > 0) {
                        closestDistance = nodeDistance;
                        closestNode = mapNode;
                    }
                }
            }

            if (checkPlayerVisibility(enemy, player, world)) {
                Weapon weapon = null;
                for (Entity subEntity : enemy.getEntities(Weapon.class)) {
                    weapon = (Weapon) subEntity;
                }

                if (weapon != null) {
                    WeaponSPI weaponSPI = Lookup.getDefault().lookup(WeaponSPI.class);
                    if (weaponSPI != null) {
                        weaponSPI.shoot(world, weapon);
                    }
                }
            }

            enemy.setNextNode(closestNode);
        }

    }

    /**
     * Moves an enemy towards its next node
     *
     * @param enemy
     * @param gameData
     */
    private void moveTowardsNextNode(Enemy enemy, GameData gameData) {
        MapNode nextNode = enemy.getNextNode();
        if (enemy.getNextNode() != null && enemy.getX() != nextNode.getX() || enemy.getY() != nextNode.getY()) {
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

            //Set radians to walking direction, if not aiming at target
            if (enemy.getTarget() == null) {
                enemy.setRadians((float) Math.atan2(dy, dx));
            }

            if (distance < 1) {
                enemy.setPosition(nextNode.getX(), nextNode.getY());
            } else {
                enemy.setPosition(enemy.getX() + dx * dt, enemy.getY() + dy * dt);
            }
        }
    }

    /**
     * A* algorythm. Gets called from pathRequest. Creates a list of mapNodes
     * which is the path enemy takes when moving longer distances.
     *
     * @param startNode
     * @param targetNode
     * @param enemy
     * @param gameData
     * @return
     */
    private List<MapNode> findPath(MapNode startNode, MapNode targetNode, Enemy enemy, GameData gameData) {
        // Sets the sent startNode and targetNode to be Hearistics
        Heuristics startHeuristics = new Heuristics(startNode);
        Heuristics targetHeuristics = new Heuristics(targetNode);

        List map = mapSPI.getMap();
        HeuristicsPriorityQueue frontier = new HeuristicsPriorityQueue();
        Set<MapNode> explored = new HashSet();
        frontier.add(startHeuristics);

        while (frontier.size() > 0) {
            Heuristics current = frontier.remove();

            // Returns call to retracePath method. Happens when the targetNode has been reached
            if (current.equals(targetHeuristics)) {
                return retracePath(startHeuristics, targetHeuristics, enemy);
            }

            explored.add(current.getNode());

            for (MapNode neighbour : current.getNode().getNeighbours()) {
                if (explored.contains(neighbour)) {
                    continue; //Skips code underneath for this neighbour, and repeats with next.
                }

                Heuristics neighbourHeuristics = new Heuristics(neighbour);

                // The A* calculation which finds and sets the "cost" of a node
                int newMovementCostToNeighbour = current.getgCost() + getDistance(current.getNode(), neighbour);
                if (newMovementCostToNeighbour < neighbourHeuristics.getgCost() || !frontier.contains(neighbourHeuristics)) {
                    neighbourHeuristics.setgCost(newMovementCostToNeighbour);
                    neighbourHeuristics.sethCost(getDistance(neighbour, targetHeuristics.getNode()));
                    neighbourHeuristics.setParent(current);

                    if (!frontier.contains(neighbourHeuristics)) {
                        frontier.add(neighbourHeuristics);

                        if (neighbourHeuristics.equals(startHeuristics)) {
                            startHeuristics = neighbourHeuristics;
                        }
                        if (neighbourHeuristics.equals(targetHeuristics)) {
                            targetHeuristics = neighbourHeuristics;
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
        given the nodes from findPath(), loops through and creates a List of 
        MapNodes and reverses it.
     */
    private List<MapNode> retracePath(Heuristics startNode, Heuristics targetNode, Enemy enemy) {
        List<MapNode> path = enemy.getPath();
        Heuristics current = targetNode;
        while (current != startNode) {
            path.add(current.getNode());
            current = current.getParent();
        }
        // Reverses the path, as it was made from targetNode to startNode
        Collections.reverse(path);
        return path;
    }

    /*
        Gets called from the process-method. Calls the findPath(A*) method.
        If the enemy isn't at the target location it calls findPath().
        Otherwise it sets the enemy's next node to be the first Node on the list.
     */
    private void pathRequest(Enemy enemy, GameData gameData) {
        List<MapNode> path = enemy.getPath();
        MapNode targetNode = enemy.getTargetNode();

        boolean newPath = false;

        if (path == null) {
            path = new ArrayList<MapNode>();
            enemy.setPath(path);
        }

        if (path.isEmpty() && targetNode == getEnemyPosition(enemy)) {
            targetNode = null;
            enemy.setTargetNode(null);
        }

        if (targetNode == null) {
            targetNode = randomTargetNode();
            enemy.setTargetNode(targetNode);
        }

        if (!path.isEmpty() && path.get(path.size() - 1) != targetNode) {
            path.clear();
        }

        if (path.isEmpty()) {
            newPath = true;
        }

        if (newPath) {
            MapNode startNode = getEnemyPosition(enemy);
            if (startNode != targetNode) {
                path = findPath(startNode, targetNode, enemy, gameData);
            }
        }

        if (!path.isEmpty() && enemy.getX() == enemy.getNextNode().getX() && enemy.getY() == enemy.getNextNode().getY()) {
            enemy.setNextNode(path.remove(0));
        }

    }

    /*
        Returns the distance between 2 nodes. 
     */
    private int getDistance(MapNode pos1, MapNode pos2) {
        int xDis = Math.round(Math.abs(pos1.getX() - pos2.getX()));
        int yDis = Math.round(Math.abs(pos1.getY() - pos2.getY()));

        if (xDis > yDis) {
            return 14 * yDis + 10 * (xDis - yDis);
        }
        return 14 * xDis + 10 * (yDis - xDis);
    }

    private MapNode getEnemyPosition(Enemy enemy) {
        return getClosestNode(enemy.getX(), enemy.getY());
    }

    /*
    Returns a random centerNode. Better solution is to get a random spawn location,
    however atm. getting a spawnLocation removes it from List. new List? Ask Nick.
     */
    private MapNode randomTargetNode() {
        Random rand = new Random();
        int index = rand.nextInt(mapSPI.getCenterMapNodes().size());
        MapNode target = mapSPI.getCenterMapNodes().get(index);
        return target;
    }

    /**
     * Used as Enemy's "vision", to find out if enemy can see the player.
     */
    class Visibility extends Entity {
    }

}
