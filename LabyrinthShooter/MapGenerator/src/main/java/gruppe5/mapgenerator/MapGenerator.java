/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.mapgenerator;

import gruppe5.common.*;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.*;
import gruppe5.common.services.IGamePluginService;
import gruppe5.mapgenerator.algorithms.RandDivisionMaze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = MapSPI.class)
})
/**
 *
 * @author nick
 */
public class MapGenerator implements MapSPI, IGamePluginService {
    public static final int NODES_IN_CORRIDOR = 3; // Must be odd to have a center node
    private Random rand;
    private List<MapNode> nodeList;
    
    @Override
    public List<MapNode> getMap() {
        if (nodeList != null)
            return nodeList;
        System.out.println("MapGenerator: NodeList not initialized.");
        return null;
    }

    @Override
    public void start(GameData gameData, World world) {
        rand = new Random();
        RandDivisionMaze generator = new RandDivisionMaze();

        boolean[][] maze = generator.generate(10, 10, rand.nextInt());
        boolean[][] scaled = scaleMaze(maze, NODES_IN_CORRIDOR);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] ? "@@" : "  ");
            }
            System.out.println();
        }

        nodeList = createNodeList(scaled);
        
        for (Entity wall : createWallEntities(scaled))
            world.addEntity(wall);
    }

    @Override
    public void stop(GameData gameData, World world) {
        
    }
    
    private ArrayList<Entity> createWallEntities(boolean[][] maze) {
        ArrayList<Entity> walls = new ArrayList();
        
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y]) {
                        boolean[] neighbors = new boolean[4];
                        if (y < maze[x].length - 1 && maze[x][y+1]) neighbors[0] = true;
                        if (x < maze.length - 1 && maze[x+1][y]) neighbors[1] = true;
                        if (y > 0 && maze[x][y-1]) neighbors[2] = true;
                        if (x > 0 && maze[x-1][y]) neighbors[3] = true;
                        
                        walls.add(createWall(x, y, neighbors));
                }
            }
        }
        
        return walls;
    }
    
    /**
     * 
     * @param neighbors [0]: Up, [1]: Right, [2]: Down, [3]: Left
     */
    private Entity createWall(int mazeX, int mazeY, boolean[] neighbors) {
        Entity wall = new Entity();
        
        float unit = GameData.UNIT_SIZE;
        
        float x = mazeX*unit;
        float y = mazeY*unit;
        
        wall.setPosition(x, y);
        wall.setDynamic(false);
        wall.setCollidable(true);
        
        boolean[] points = new boolean[4];
        if (!neighbors[0]) {
            points[0] = true;
            points[1] = true;
        }
        if (!neighbors[1]) {
            points[1] = true;
            points[2] = true;
        }
        if (!neighbors[2]) {
            points[2] = true;
            points[3] = true;
        }
        if (!neighbors[3]) {
            points[3] = true;
            points[0] = true;
        }
        
        int pointCount = 0;
        for (int i = 0; i < points.length; i++) {
            if (points[i])
                pointCount++;
        }
        
        float[] shapex = new float[pointCount];
        float[] shapey = new float[pointCount];
        
        
        int sCount = 0;
        if (points[0]) {
            shapex[sCount] = x - unit/2;
            shapey[sCount] = y + unit/2;
            sCount++;
        }
        if (points[1]) {
            shapex[sCount] = x + unit/2;
            shapey[sCount] = y + unit/2;
            sCount++;
        }
        if (points[2]) {
            shapex[sCount] = x + unit/2;
            shapey[sCount] = y - unit/2;
            sCount++;
        }
        if (points[3]) {
            shapex[sCount] = x - unit/2;
            shapey[sCount] = y - unit/2;
            sCount++;
        }
        
        wall.setShapeX(shapex);
	wall.setShapeY(shapey);
        
        return wall;
    }
    
    /**
     *
     * @param maze
     * @param corSize How many nodes wide a corridor should be. Has to be an odd
     * number.
     * @return
     */
    private boolean[][] scaleMaze(boolean[][] maze, int corSize) {
        boolean[][] scaled = new boolean[maze.length * corSize][maze[0].length * corSize];

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                for (int i = 0; i < corSize; i++) {
                    for (int j = 0; j < corSize; j++) {
                        scaled[x * corSize + j][y * corSize + i] = maze[x][y];
                    }
                }
            }
        }

        return scaled;
    }

    /**
     *
     * @param maze
     * @return A transposed version of maze.
     */
    private boolean[][] transposeMaze(boolean[][] maze) {
        boolean[][] transposed = new boolean[maze.length][maze[0].length];

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                transposed[x][y] = maze[y][x];
            }
        }
        return transposed;
    }

    /**
     * Initiates the recursive function iterateNodes to generate nodes.
     *
     * @param maze The maze the nodes should be generated from. True denotes a
     * wall, False denotes an empty space. All empty spaces should be connected
     * to ensure that all nodes have the correct neighbours.
     * @return A list of all nodes, all connected with their neighbours.
     */
    private ArrayList<MapNode> createNodeList(boolean[][] maze) {
        // Find random starting position
        int x = 3;
        int y = 3;
        for (; x < maze.length && maze[x][y]; x++) {
            for (; y < maze[x].length && maze[x][y]; y++);
        }

        // Return result of recursive function
        return iterateCenterNodes(null, new ArrayList<MapNode>(), maze, x, y);
    }

    /**
     * Recursive method that generates a center node (with connection to its
     * neighbors) for every empty location in maze.
     *
     * @param root Parent Node that the current Node should have a connection
     * to.
     * @param nodeList A list containing all Nodes in maze.
     * @param maze The maze the nodes should be generated from. True denotes a
     * wall, False denotes an empty space. All empty spaces should be connected
     * to ensure that all nodes have the correct neighbors.
     * @param x X-position in maze.
     * @param y Y-position in maze.
     * @return A list of all nodes generated from this method.
     */
    private ArrayList<MapNode> iterateCenterNodes(Node parent, ArrayList<MapNode> nodeList, boolean[][] maze, int x, int y) {
        if (x < maze.length && y < maze[x].length && !maze[x][y]) {
            Node child = createNode(x, y, isCenter(maze, x, y));
            // If not already created
            if (!nodeList.contains(child)) {
                if (parent != null) {
                    // Link parent and child together
                    parent.getNeighbours().add(child);
                    child.getNeighbours().add(parent);
                }
                // Add child to nodeList
                nodeList.add(child);
                // Create node for all of childs neighbours
                iterateCenterNodes(child, nodeList, maze, x - 1, y);
                iterateCenterNodes(child, nodeList, maze, x + 1, y);
                iterateCenterNodes(child, nodeList, maze, x, y - 1);
                iterateCenterNodes(child, nodeList, maze, x, y + 1);
            } else // If nodeList already contains this node but it is not linked to its parent
            if (!parent.getNeighbours().contains(child)) {
                parent.getNeighbours().add(child);
            }
        }
        return nodeList;
    }

    /**
     * 
     * @param maze
     * @param x
     * @param y
     * @return A boolean indicating whether the position is the center of a corridor 
     */
    private boolean isCenter(boolean[][] maze, int x, int y) {
        boolean horizontal = false;
        if (maze[x + NODES_IN_CORRIDOR][y] || maze[x - NODES_IN_CORRIDOR][y]) 
            horizontal = true;
       
        if (horizontal) 
            return (y - 1) % NODES_IN_CORRIDOR == 0;
        else 
            return (x - 1) % NODES_IN_CORRIDOR == 0;
    }

    /**
     * Creates a new Node without any neighbors
     *
     * @param x Corresponding boolean maze x-coordinate
     * @param y Corresponding boolean maze y-coordinate
     * @param center Specifies whether this node is the center node in a
     * corridor
     * @param offsetX Specifies the node distance between this node and its
     * center node
     * @param offsetY Specifies the node distance between this node and its
     * center node
     * @return
     */
    private Node createNode(int x, int y, boolean center) {
        Node n = new Node();
        n.setIsMiddle(center);
        n.setX(GameData.UNIT_SIZE * x);
        n.setY(GameData.UNIT_SIZE * y);
        return n;
    }
}
