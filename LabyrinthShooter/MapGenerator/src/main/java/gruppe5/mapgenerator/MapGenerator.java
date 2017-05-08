/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.mapgenerator;

import gruppe5.common.node.MapNode;
import gruppe5.common.node.Node;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.*;
import gruppe5.common.services.IGameInitService;
import gruppe5.mapgenerator.algorithms.RandDivisionMaze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IGameInitService.class)
    ,
    @ServiceProvider(service = MapSPI.class)
})
/**
 *
 * @author nick
 */
public class MapGenerator implements MapSPI, IGameInitService {
    /**
     * For debugging, if true entities for nodes will be created and other info
     * will be shown
     */
    public static final boolean DEBUG_ENABLED = true;
    
    public static final int NODES_IN_CORRIDOR = 5; // Must be odd to have a center node

    private Random rand; // Used for seed generation

    // Used for MapSPI
    private List<MapNode> nodeList;
    private List<MapNode> centerNodeList;
    private List<MapNode> availableSpawnNodes;

    private List<Entity> mazeEntities;

    @Override
    public List<MapNode> getMap() {
        if (nodeList != null) {
            return nodeList;
        }
        System.out.println("MapGenerator.getMap(): MapGenerator not initialized.");
        return null;
    }

    @Override
    public List<MapNode> getCenterMapNodes() {
        if (centerNodeList != null) {
            return centerNodeList;
        }
        System.out.println("MapGenerator.getCenterMapNodes(): MapGenerator not initialized.");
        return null;
    }

    @Override
    public MapNode getRandomSpawnNode() {
        if (availableSpawnNodes == null) {
            System.out.println("MapGenerator.getRandomSpawnNode(): MapGenerator not initialized.");
            return null;
        }

        // Get random MapNode
        MapNode randNode = availableSpawnNodes.get(rand.nextInt(availableSpawnNodes.size()));
        // Remove node from list only if it isn't the last available node
        if (availableSpawnNodes.size() > 1) {
            availableSpawnNodes.remove(randNode);
        } else {
            System.out.println("MapGenerator: Available spawn nodes depleted.");
        }

        return randNode;
    }

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("MapPlugin started");

        rand = new Random();
        RandDivisionMaze generator = new RandDivisionMaze();

        // Calculate the unit dimensions of the maze given the world width and height
        int mazeWidth = (int) Math.floor(world.getWorldWidth() / GameData.UNIT_SIZE / NODES_IN_CORRIDOR);
        int mazeHeight = (int) Math.floor(world.getWorldHeight() / GameData.UNIT_SIZE / NODES_IN_CORRIDOR);

        // Generate a minimalistic version of maze
        boolean[][] maze = generator.generate(mazeWidth, mazeHeight, rand.nextInt());
        // Scale the maze
        boolean[][] scaledMaze = scaleMaze(maze, NODES_IN_CORRIDOR);

        // Prints out the mini-version of the maze to console
        for (boolean[] maze1 : maze) {
            for (int j = 0; j < maze1.length; j++) {
                System.out.print(maze1[j] ? "@@" : "  ");
            }
            System.out.println();
        }

        // Create MapNodes required by MapSPI
        nodeList = createNodeList(scaledMaze);
        centerNodeList = new ArrayList();
        availableSpawnNodes = new ArrayList();

        // Fill centerNodeList and availableSpawnNodes
        for (MapNode node : nodeList) {
            if (node.isMiddle()) {
                centerNodeList.add(node);
                // If node only has one neighbouring center node, it is added to spawn node list
                if (((Node) node).getNeighbouringCenterNodes().size() == 1) {
                    availableSpawnNodes.add(node);
                }
            }
        }

        mazeEntities = createMazeEntities(maze);
        // Add wall entities to world
        for (Entity entity : mazeEntities) {
            world.addEntity(entity);
        }

        // Debug nodes if enabled
        if (DEBUG_ENABLED) {
            debugNodes(world);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("MapPlugin stopped.");

        for (Entity entity : mazeEntities) {
            world.removeEntity(entity);
        }
        mazeEntities = null;

        nodeList = null;
        centerNodeList = null;
        availableSpawnNodes = null;
    }

    private void debugNodes(World world) {
        System.out.println("Starting MapNode debugging...");
        
        for (MapNode n : nodeList) {
            // Spawn node entities into world
            world.addEntity(MapEntityCreator.createNodeEntity(n));

            // Print out debug info
            if (n.getNeighbours().size() <= 1) {
                System.out.println("- Node " + n + " has " + n.getNeighbours().size() + " neighbours!");
            }

            // Check that all nodes neighbors are contained in nodeList
            for (MapNode neighbor : n.getNeighbours()) {
                if (!nodeList.contains(neighbor)) {
                    System.out.println("- Node " + neighbor + " is not contained in nodeList!");
                }
            }

            // Check that duplicates aren't contained in nodeList
            for (int i = 0; i < nodeList.size(); i++) {
                if (n.equals(nodeList.get(i)) && nodeList.indexOf(n) != i) {
                    System.out.println("- Node " + n + " has a duplicate!");
                }
            }
        }
        
        System.out.println("MapNode debugging done.");
    }

    /**
     *
     * @param maze
     * @param corSize
     * @return
     */
    private ArrayList<Entity> createMazeEntities(boolean[][] maze) {
        ArrayList<Entity> entities = new ArrayList();

        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y]) {
                    entities.add(MapEntityCreator.createWallEntity(x, y, neighbors(maze, x, y)));
                } else {
                    entities.add(MapEntityCreator.createFloorEntity(x, y));
                }
            }
        }

        return entities;
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
     * Initiates the recursive function iterateNodes to generate nodes.
     *
     * @param maze The maze the nodes should be generated from. True denotes a
     * wall, False denotes an empty space. All empty spaces should be connected
     * to ensure that all nodes have the correct neighbours.
     * @return A list of all nodes, all connected with their neighbours.
     */
    private ArrayList<MapNode> createNodeList(boolean[][] maze) {
        // Find random starting position
        int x = NODES_IN_CORRIDOR;
        int y = NODES_IN_CORRIDOR;
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
            //Create child node
            Node child = null;
            for (MapNode existingNode : nodeList) {
                //If there is already a node at the chosen position, choose that node
                if (existingNode.getX() == GameData.UNIT_SIZE * x && existingNode.getY() == GameData.UNIT_SIZE * y) {
                    child = (Node) existingNode;
                }
            }
            //If child is still null create a new Node at the chosen position and at it to nodeList
            if (child == null) {
                child = createNode(x, y, isCenter(maze, x, y));
                nodeList.add(child);

                //Create neighbors
                iterateCenterNodes(child, nodeList, maze, x - 1, y);
                iterateCenterNodes(child, nodeList, maze, x + 1, y);
                iterateCenterNodes(child, nodeList, maze, x, y - 1);
                iterateCenterNodes(child, nodeList, maze, x, y + 1);
            }
            if (parent != null) {
                // Link parent and child together
                if (!parent.getNeighbours().contains(child)) {
                    parent.getNeighbours().add(child);
                }
                if (!child.getNeighbours().contains(parent)) {
                    child.getNeighbours().add(parent);
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
        boolean horizontalCenter = true;
        boolean verticalCenter = true;
        
        // Check if there's any walls from the current node and 
        for (int i = 1; i <= (NODES_IN_CORRIDOR - 1)/2; i++) {
            if (maze[x+i][y] || maze[x-i][y]) horizontalCenter = false;
            if (maze[x][y+1] || maze[x][y-1]) verticalCenter = false;
        }
        
        // Is center if one is true
        return horizontalCenter || verticalCenter;
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
        } else if (x >= maze.length || x < 0) {
            return false;
        } else if (y >= maze[x].length || y < 0) {
            return false;
        }
        return true;
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
