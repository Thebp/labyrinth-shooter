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
    public static final boolean SHOW_NODES = false; // For debugging, if true entities for nodes will be created

    private Random rand; // Used for seed generation
    private List<MapNode> nodeList;

    @Override
    public List<MapNode> getMap() {
        if (nodeList != null) {
            return nodeList;
        }
        System.out.println("MapGenerator.getMap(): NodeList not initialized.");
        return null;
    }

    @Override
    public void start(GameData gameData, World world) {
        rand = new Random();
        RandDivisionMaze generator = new RandDivisionMaze();

        // Calculate the unit dimensions of the maze given the game width and height
        int mazeWidth = (int) Math.floor(gameData.getDisplayWidth() / GameData.UNIT_SIZE / NODES_IN_CORRIDOR);
        int mazeHeight = (int) Math.floor(gameData.getDisplayHeight() / GameData.UNIT_SIZE / NODES_IN_CORRIDOR);

        // Generate a minimalistic version of maze
        boolean[][] maze = generator.generate(mazeWidth, mazeHeight, rand.nextInt());
        // Scale the maze
        boolean[][] scaledMaze = scaleMaze(maze, NODES_IN_CORRIDOR);

        // Prints out the mini-version of the maze to console
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.print(maze[i][j] ? "@@" : "  ");
            }
            System.out.println();
        }

        // Create MapNodes required by MapSPI
        nodeList = createNodeList(scaledMaze);

        // Add wall entities to world
        for (Entity wall : createWallEntities(scaledMaze)) {
            world.addEntity(wall);
        }

        // Add node entities to world if enabled
        if (SHOW_NODES) {
            for (MapNode n : nodeList) {
                world.addEntity(createNodeEntity(n));
            }
        }
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    /**
     *
     * @param maze
     * @return
     */
    private ArrayList<Entity> createWallEntities(boolean[][] maze) {
        ArrayList<Entity> walls = new ArrayList();

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y]) {
                    walls.add(createWallEntity(x, y, neighbors(maze, x, y)));
                }
            }
        }

        return walls;
    }

    /**
     *
     * @param mazeX
     * @param mazeY
     * @param neighbors
     * @return
     */
    private Entity createWallEntity(int mazeX, int mazeY, boolean[] neighbors) {
        Entity wall = new Entity();

        float unit = GameData.UNIT_SIZE;

        float x = mazeX * unit;
        float y = mazeY * unit;

        wall.setPosition(x, y);
        wall.setDynamic(false);
        wall.setCollidable(true);

        float[] shapex = new float[4];
        float[] shapey = new float[4]; 
        
        shapex[0] = x - unit/2;
        shapey[0] = y + unit/2;
        
        shapex[1] = x + unit/2;
        shapey[1] = y + unit/2;
        
        shapex[2] = x + unit/2;
        shapey[2] = y - unit/2;
        
        shapex[3] = x - unit/2;
        shapey[3] = y - unit/2;

        wall.setShapeX(shapex);
        wall.setShapeY(shapey);

        return wall;
    }

    /**
     *
     * @param n
     * @return An entity representing a node
     */
    private Entity createNodeEntity(MapNode n) {
        Entity node = new Entity();

        float x = n.getX();
        float y = n.getY();
        float unit = GameData.UNIT_SIZE;

        node.setPosition(x, y);
        node.setCollidable(false);
        node.setDynamic(false);

        float[] shapex;
        float[] shapey;
        if (n.isMiddle()) {
            shapex = new float[6];
            shapey = new float[6]
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
     * Recursive method that generates a node (with connection to its neighbors)
     * for every empty location in maze.
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
        if (!safelyGetValue(maze, x, y)) {
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
            } else// If nodeList already contains this node but it is not linked to its parent
            {
                if (!parent.getNeighbours().contains(child)) {
                    parent.getNeighbours().add(child);
                }
            }
        }
        return nodeList;
    }

    /**
     *
     * @param maze
     * @param x
     * @param y
     * @return A boolean indicating whether the position is the center of a
     * corridor
     */
    private boolean isCenter(boolean[][] maze, int x, int y) {
        boolean center = true;
        boolean[] neighbors = neighbors(maze, x, y);

        // If the position has no neighbouring walls, it is a center node
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i]) {
                center = false;
            }
        }

        return center;
    }

    /**
     *
     * @param maze
     * @param x
     * @param y
     * @return An array indicating the neighboring positions that contains walls
     * [0]: Up, [1]: UpRight, [2]: Right, [3]: RightDown, [4]: Down, [5]:
     * DownLeft, [6]: Left, [7]: LeftUp
     */
    private boolean[] neighbors(boolean[][] maze, int x, int y) {
        boolean[] neighbors = new boolean[8];

        neighbors[0] = safelyGetValue(maze, x, y + 1);
        neighbors[1] = safelyGetValue(maze, x + 1, y + 1);
        neighbors[2] = safelyGetValue(maze, x + 1, y);
        neighbors[3] = safelyGetValue(maze, x + 1, y - 1);
        neighbors[4] = safelyGetValue(maze, x, y - 1);
        neighbors[5] = safelyGetValue(maze, x - 1, y - 1);
        neighbors[6] = safelyGetValue(maze, x - 1, y);
        neighbors[7] = safelyGetValue(maze, x - 1, y + 1);

        return neighbors;
    }

    /**
     *
     * @param maze
     * @param x
     * @param y
     * @return The specified maze value or false if out of bounds
     */
    private boolean safelyGetValue(boolean[][] maze, int x, int y) {
        if (x > 0 && y > 0 && x < maze.length && y < maze[x].length) {
            return maze[x][y];
        }
        return false;
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
