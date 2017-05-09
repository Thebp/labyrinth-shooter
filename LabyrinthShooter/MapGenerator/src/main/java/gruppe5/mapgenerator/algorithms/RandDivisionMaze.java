package gruppe5.mapgenerator.algorithms;

import java.util.Random;

public class RandDivisionMaze {
    private static final int N = 1;
    private static final int S = 2;
    private static final int E = 4;
    private static final int W = 8;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    
    private Random rand;

    public RandDivisionMaze() {
    }

    public boolean[][] generate(int width, int height, int seed) {
        rand = new Random(Integer.toUnsignedLong(seed));

        boolean[][] maze = new boolean[width][height];
        
        maze = divide(maze, true, 1, maze.length - 2, 1, maze[0].length - 2);
        maze = addOuterWalls(maze);

        return maze;
    }
    
    /**
     * 
     * @param grid
     * @param h Horizontal
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @return 
     */
    private boolean[][] divide(boolean[][] grid, boolean h, int minX, int maxX, int minY, int maxY) {
        if (h) {
            if (maxX - minX < 2) 
                return grid;
            
            int y = Math.floorDiv(rand.nextInt(maxY - minY) + minY, 2) * 2;
            grid = addHWall(grid, minX, maxX, y);
            
            grid = divide(grid, !h, minX, maxX, minY, y-1);
            grid = divide(grid, !h, minX, maxX, y+1, maxY);
        } else {
            if (maxY - minY < 2)
                return grid;
            
            int x = Math.floorDiv(rand.nextInt(maxX - minX) + minX, 2) * 2;
            grid = addVWall(grid, minY, maxY, x);
            
            grid = divide(grid, !h, minX, x-1, minY, maxY);
            grid = divide(grid, !h, x+1, maxX, minY, maxY);
        }

        return grid;
    }
    
    private boolean[][] addHWall(boolean[][] grid, int minX, int maxX, int y) {
        int hole = Math.floorDiv(rand.nextInt(maxX - minX) + minX, 2) * 2 + 1;
        
        for (int i = minX; i <= maxX; i++) 
            grid[i][y] = i != hole;
        
        return grid;
    }
    
    private boolean[][] addVWall(boolean[][] grid, int minY, int maxY, int x) {
        int hole = Math.floorDiv(rand.nextInt(maxY - minY) + minY, 2) * 2 + 1;
        
        for (int i = minY; i <= maxY; i++) 
            grid[x][i] = i != hole;
        
        return grid;
    }
    
    private boolean[][] addOuterWalls(boolean[][] maze) {
        for (int x = 0; x < maze.length; x++) {
            maze[x][0] = true;
            maze[x][maze[0].length - 1] = true;  
        }
        
        for (int y = 0; y < maze[0].length; y++) {
            maze[0][y] = true;
            maze[maze.length - 1][y] = true;
        }
        
        return maze;
    }
}
