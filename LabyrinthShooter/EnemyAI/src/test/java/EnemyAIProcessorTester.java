/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import gruppe5.common.bullet.BulletSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.enemy.Enemy;
import gruppe5.common.map.MapSPI;
import gruppe5.common.node.MapNode;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGameInitService;
import gruppe5.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;

/**
 *
 * @author Nick
 */
public class EnemyAIProcessorTester {
    
    public EnemyAIProcessorTester() {
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
    
    @After
    public void tearDown() {
    }
    
    private Enemy createEnemy(float x, float y) {
        Enemy enemy = new Enemy();

        enemy.setX(x);
        enemy.setY(y);
        enemy.setMaxSpeed(50);
        enemy.setAcceleration(10);
        enemy.setDeacceleration(15);
        enemy.setRotationSpeed(20);
        enemy.setRadius(gruppe5.common.data.GameData.UNIT_SIZE);
        enemy.setRadians(3.1415f / 1);
        enemy.setCollidable(true);
        enemy.setDynamic(true);
        enemy.setLife(2);
        
        return enemy;
    }

    @Test
    public void testEnemyAIMoveTowardsBullets() {
        GameData gameData = new GameData();
        gameData.setDelta(1.0f);
        World world = new World();
        
        BulletSPI bulletSPI = Lookup.getDefault().lookup(BulletSPI.class);
        Map<Enemy, MapNode> initalEnemyTargetNodes = new HashMap();
        
        for (IGameInitService init : Lookup.getDefault().lookupResult(IGameInitService.class).allInstances()) {
            init.start(gameData, world);
        }
        for (IGamePluginService plugin : Lookup.getDefault().lookupResult(IGamePluginService.class).allInstances()) {
            plugin.start(gameData, world);
        }
        
        // Process world first time
        for (IEntityProcessingService process : Lookup.getDefault().lookupAll(IEntityProcessingService.class))
            process.process(gameData, world);
        
        // Save all initial target mapnodes for comparison later
        for (Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy)e;
            assert(enemy.getTargetNode() != null);
            initalEnemyTargetNodes.put(enemy, enemy.getTargetNode());
        }
        
        // Create bullet coming from player
        bulletSPI.createBullet(Lookup.getDefault().lookup(PlayerSPI.class).getPlayer(world));
        
        // Process world second time
        for (IEntityProcessingService process : Lookup.getDefault().lookupAll(IEntityProcessingService.class))
            process.process(gameData, world);
        
        // Assert that all enemies target node have changed
        for (Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy)e;
            assert(initalEnemyTargetNodes.get(enemy) != enemy.getTargetNode());
        }
    }
}
