/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.collision;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
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
public class CollisionControlSystemTest {
    String e1ID;
    String e2ID;
    String e3ID;
    String entity1ID;
    String entity2ID;
    
    class E1 extends Entity {}
    class E2 extends Entity {}
    class E3 extends Entity {}
    
    public CollisionControlSystemTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    private Object createTestEntity(float x, float y, Class<? extends Entity> type) {
        Entity e;
        if (type == E1.class)
            e = new E1();
        else if (type == E2.class)
            e = new E2();
        else if (type == E3.class)
            e = new E3();
        else
            e = new Entity();
        
        e.setDynamic(true);
        e.setCollidable(true);
        e.setIsBackground(false);
        e.setX(x);
        e.setY(y);
        
        int size = 5;
        
        // Create a rectangle
        float[] shapex = new float[4];
        float[] shapey = new float[4];
        
        shapex[0] = x + size;
        shapey[0] = y + size;
        shapex[1] = x + size;
        shapey[1] = y - size;
        shapex[2] = x - size;
        shapey[2] = y - size;
        shapex[3] = x - size;
        shapey[3] = y + size;
        
        e.setShapeX(shapex);
        e.setShapeY(shapey);
        
        e.setRadius(size*2);
        e.setDamage(50);
        
        return e;
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testEntityProcessing() {
        World world = new World();
        GameData gameData = new GameData();
        
        E1 e1 = (E1)createTestEntity(0, 0, E1.class);
        e1.setLife(100);
        E2 e2 = (E2)createTestEntity(2, 2, E2.class);
        e2.setLife(20);
        E3 e3 = (E3)createTestEntity(15, 15, E3.class);
        e3.setLife(70);
        Entity entity1 = (Entity)createTestEntity(50, 50, Entity.class);
        entity1.setLife(100);
        Entity entity2 = (Entity)createTestEntity(51, 50, Entity.class);
        entity2.setLife(80);
        
        e1ID = world.addEntity(e1);
        e2ID = world.addEntity(e2);
        e3ID = world.addEntity(e3);
        entity1ID = world.addEntity(entity1);
        entity2ID = world.addEntity(entity2);
        
        CollisionControlSystem collision = new CollisionControlSystem();
        collision.process(gameData, world);
        
        assert(world.getEntity(e1ID).isHit());
        assert(world.getEntity(e1ID).getLife() == 50);
        assert(world.getEntity(e2ID).isHit());
        assert(world.getEntity(e2ID).getLife() == -30);
        assert(!world.getEntity(e3ID).isHit());
        assert(world.getEntity(e3ID).getLife() == 70);
        assert(!world.getEntity(entity1ID).isHit());
        assert(world.getEntity(entity1ID).getLife() == 100);
        assert(!world.getEntity(entity2ID).isHit());
        assert(world.getEntity(entity2ID).getLife() == 80);
    }
    
    @Test
    public void testCheckCollision() {
        E1 e1 = (E1)createTestEntity(0, 0, E1.class);
        E2 e2 = (E2)createTestEntity(0, 0, E2.class);
        e2.setDynamic(false);
        E2 e3 = (E2)createTestEntity(0, 0, E2.class);
        
        World world = new World();
        world.addEntity(e1);
        world.addEntity(e2);
        world.addEntity(e3);
        
        CollisionControlSystem collision = new CollisionControlSystem();
        
        assert(collision.checkCollision(e1, world));
        assert(!collision.checkCollision(e2, world));
        assert(!collision.checkCollision(e3, world));
    }
}
