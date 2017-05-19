/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.node.MapNode;
import gruppe5.mapgenerator.MapGenerator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nick
 */
public class MapGeneratorTest {
    MapGenerator map = new MapGenerator();
    GameData gameData = new GameData();
    World world = new World();
    
    public MapGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        world.setWorldWidth(1000);
        world.setWorldHeight(1000);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testStartStopEntityCreation() {
        map.start(gameData, world);
        
        assert(!world.getEntities().isEmpty());
        
        map.stop(gameData, world);
        
        assert(world.getEntities().isEmpty());
    }
    
    @Test
    public void testNoEntityOutsideWorldBounds() {
        map.start(gameData, world);
        
        for (Entity e : world.getEntities()) {
            assert(e.getX() > 0 && e.getX() < world.getWorldWidth());
            assert(e.getY() > 0 && e.getY() < world.getWorldHeight());
        }
    }
    
    @Test
    public void testNodeAtLeast2Neighbors() {
        map.start(gameData, world);
        List<MapNode> nodes = map.getMap();
        
        for (MapNode n : nodes) {
            assert(n.getNeighbours().size() >= 2);
        }
    }
    
    @Test
    public void testAllNodeNeighborsInMap() {
        map.start(gameData, world);
        List<MapNode> nodes = map.getMap();
        
        for (MapNode n : nodes) {
            for (MapNode neighbor : n.getNeighbours()) {
                assert(nodes.contains(neighbor));
            }
        }
    }
    
    @Test
    public void testNoNodeDuplicates() {
        map.start(gameData, world);
        List<MapNode> nodes = map.getMap();
        
        for (MapNode n1 : nodes) {
            for (MapNode n2 : nodes) {
                if (n1 != n2) {
                    assert(n1.getX() != n2.getX() || n1.getY() != n2.getY());
                }
            }
        }
    }
    
    @Test
    public void testGetCenterMapNodesAllNodesAreCenter() {
        map.start(gameData, world);
        List<MapNode> centerNodes = map.getCenterMapNodes();
        
        for (MapNode n : centerNodes) {
            assert(n.isMiddle());
        }
    }
    
    @Test
    public void testGetRandomSpawnNodeIsACenterNodeWithOnly1CenterNeighbor() {
        map.start(gameData, world);
        MapNode spawnNode = map.getRandomSpawnNode();
        
        assert(spawnNode.isMiddle());
        
        int centerCount = 0;
            for (MapNode neighbor : spawnNode.getNeighbours()) {
                if (neighbor.isMiddle())
                    centerCount++;
            }
        assert(centerCount == 1);
    }
    
    @Test
    public void testReserveRandomSpawnNodeIsCenter() {
        map.start(gameData, world);
        MapNode spawnNode = map.reserveRandomSpawnNode();
        
        assert(spawnNode.isMiddle());
    }
    
    @Test 
    public void testAllCenterNodesAtLeast1NeighbouringCenterNode() {
        map.start(gameData, world);
        List<MapNode> centerNodes = map.getCenterMapNodes();
        
        for (MapNode n : centerNodes) {
            int centerCount = 0;
            for (MapNode neighbor : n.getNeighbours()) {
                if (neighbor.isMiddle())
                    centerCount++;
            }
            assert(centerCount >= 1);
        }
    }
    
    @Test
    public void testGroundEntitesHaveACenterNode() {
        map.start(gameData, world);
        List<MapNode> centerNodes = map.getCenterMapNodes();
        
        for (Entity e : world.getEntities()) {
            if (e.isBackground()) {
                boolean haveCenterNode = false;
                for (MapNode n : centerNodes) {
                    if (e.getX() == n.getX() && e.getY() == n.getY())
                        haveCenterNode = true;
                }
                
                assert(haveCenterNode);
            }
        }
    }
}
