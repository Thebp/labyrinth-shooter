/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.mapgenerator;

import gruppe5.common.data.Entity;
import gruppe5.common.node.MapNode;
import static gruppe5.mapgenerator.MapGenerator.NODES_IN_CORRIDOR;
import java.util.Random;

/**
 *
 * @author Nick
 */
public class MapEntityCreator {
    private static final Random rand = new Random();
    
    // Random sprite chances
    private static final int WALL_CRACK_CHANCE = 15; // 1/chance
    private static final int NUM_FLOOR_EFFECTS = 3; // Number of floor effect images in assets
    private static final int FLOOR_EFFECT_CHANCE = 10; // 1/chance
    
     /**
     *
     * @param mazeX
     * @param mazeY
     * @return
     */
    public static Entity createFloorEntity(int mazeX, int mazeY) {
        Entity floor = new Entity();

        float floorSize = MapGenerator.MAP_UNIT_SIZE * NODES_IN_CORRIDOR;

        float x = mazeX * floorSize + (MapGenerator.MAP_UNIT_SIZE * (NODES_IN_CORRIDOR - 1) / 2);
        float y = mazeY * floorSize + (MapGenerator.MAP_UNIT_SIZE * (NODES_IN_CORRIDOR - 1) / 2);

        floor.setPosition(x, y);
        floor.setDynamic(false);
        floor.setCollidable(false);
        floor.setIsBackground(true);
        floor.setRadius(floorSize + 1);
        floor.setRadians(0);
        String path = "MapGenerator/target/MapGenerator-1.0.0-SNAPSHOT.jar!/assets/images/floor_ground/floor";
        if (rand.nextInt(FLOOR_EFFECT_CHANCE) == 0) {
            path += "_effect" + (rand.nextInt(NUM_FLOOR_EFFECTS) + 1);
        }
        path += ".png";
        floor.setImagePath(path);

        return floor;
    }
    
    /**
     *
     * @param mazeX
     * @param mazeY
     * @param neighbors
     * @return
     */
    public static Entity createWallEntity(int mazeX, int mazeY, boolean[] neighbors) {
        Entity wall = new Entity();

        float wallSize = MapGenerator.MAP_UNIT_SIZE * NODES_IN_CORRIDOR;

        float x = mazeX * wallSize + (MapGenerator.MAP_UNIT_SIZE * (NODES_IN_CORRIDOR - 1) / 2);
        float y = mazeY * wallSize + (MapGenerator.MAP_UNIT_SIZE * (NODES_IN_CORRIDOR - 1) / 2);

        wall.setPosition(x, y);
        wall.setDynamic(false);
        wall.setCollidable(true);
        wall.setRadius(wallSize + 1);
        wall.setRadians(0); // Up

        // Set image depending on wall's neighbors
        String imagePath = "MapGenerator/target/MapGenerator-1.0.0-SNAPSHOT.jar!/assets/images/wall_tiles/wall";
        if (!neighbors[0]) {
            imagePath += "_up";
        }
        if (!neighbors[2]) {
            imagePath += "_right";
        }
        if (!neighbors[4]) {
            imagePath += "_down";
        }
        if (!neighbors[6]) {
            imagePath += "_left";
        }
        
        // Apply crack chance
        if (rand.nextInt(WALL_CRACK_CHANCE) == 0)
            imagePath += "_cracked";
        
        imagePath += ".png";
        wall.setImagePath(imagePath);

        float[] shapex = new float[4];
        float[] shapey = new float[4];

        shapex[0] = x - wallSize / 2;
        shapey[0] = y + wallSize / 2;

        shapex[1] = x + wallSize / 2;
        shapey[1] = y + wallSize / 2;

        shapex[2] = x + wallSize / 2;
        shapey[2] = y - wallSize / 2;

        shapex[3] = x - wallSize / 2;
        shapey[3] = y - wallSize / 2;

        wall.setShapeX(shapex);
        wall.setShapeY(shapey);

        return wall;
    }

    /**
     *
     * @param n
     * @return An entity representing a node
     */
    public static Entity createNodeEntity(MapNode n) {
        Entity node = new Entity();

        float x = n.getX();
        float y = n.getY();
        float unit = MapGenerator.MAP_UNIT_SIZE;

        node.setPosition(x, y);
        node.setCollidable(false);
        node.setDynamic(false);
        node.setRadius(MapGenerator.MAP_UNIT_SIZE);

        // load sprite
        boolean[] neighbors = new boolean[4];
        for (MapNode neighbor : n.getNeighbours()) {
            if (neighbor.getX() == n.getX() && neighbor.getY() > n.getY()) {
                neighbors[0] = true;
            }
            if (neighbor.getY() == n.getY() && neighbor.getX() > n.getX()) {
                neighbors[1] = true;
            }
            if (neighbor.getX() == n.getX() && neighbor.getY() < n.getY()) {
                neighbors[2] = true;
            }
            if (neighbor.getY() == n.getY() && neighbor.getX() < n.getX()) {
                neighbors[3] = true;
            }
        }

        String imagePath = "MapGenerator/target/MapGenerator-1.0.0-SNAPSHOT.jar!/assets/images/node/node";
        
        if (neighbors[0]) {
            imagePath += "_up";
        }
        if (neighbors[1]) {
            imagePath += "_right";
        }
        if (neighbors[2]) {
            imagePath += "_down";
        }
        if (neighbors[3]) {
            imagePath += "_left";
        }

        imagePath += ".png";
        node.setImagePath(imagePath);

        float[] shapex;
        float[] shapey;
        if (n.isMiddle()) {
            shapex = new float[6];
            shapey = new float[6];
        } else {
            shapex = new float[4];
            shapey = new float[4];
        }

        shapex[0] = x;
        shapey[0] = y + unit / 4;

        shapex[1] = x + unit / 4;
        shapey[1] = y;

        shapex[2] = x;
        shapey[2] = y - unit / 4;

        shapex[3] = x - unit / 4;
        shapey[3] = y;

        // Create a line in the middle to indicate that this node is a center node
        if (n.isMiddle()) {
            shapex[4] = x;
            shapey[4] = y + unit / 4;

            shapex[5] = x;
            shapey[5] = y - unit / 4;
        }

        node.setShapeX(shapex);
        node.setShapeY(shapey);

        return node;
    }
}
