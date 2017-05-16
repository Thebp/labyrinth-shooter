/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.levelexit;

import gruppe5.common.audio.AudioSPI;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.UIElement;
import gruppe5.common.data.World;
import gruppe5.common.map.MapSPI;
import gruppe5.common.node.MapNode;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGamePluginService;
import gruppe5.commonvictory.VictorySPI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author nick
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})
public class LevelExitController implements IGamePluginService, IEntityProcessingService {
    private Entity exit;
    
    @Override
    public void start(GameData gameData, World world) {
        System.out.println("LevelExit started");
        
        exit = new Entity();
        exit.setIsBackground(false);
        exit.setImagePath("LevelExit/target/LevelExit-1.0.0-SNAPSHOT.jar!/assets/images/ladder.png");
        exit.setCollidable(false);
        exit.setDynamic(false);
        exit.setRadius(GameData.UNIT_SIZE);
        exit.setSoundPath("LevelExit/target/LevelExit-1.0.0-SNAPSHOT.jar!/assets/sounds/winning.ogg");
        
        // Set position on a spawn node
        MapSPI map = Lookup.getDefault().lookup(MapSPI.class);
        MapNode node = map.reserveRandomSpawnNode();
        exit.setX(node.getX());
        exit.setY(node.getY());
        
        float[] shapeX = new float[4];
        float[] shapeY = new float[4];
        
        shapeX[0] = exit.getX();
        shapeY[0] = exit.getY() + GameData.UNIT_SIZE / 4;

        shapeX[1] = exit.getX() + GameData.UNIT_SIZE / 4;
        shapeY[1] = exit.getY();

        shapeX[2] = exit.getX();
        shapeY[2] = exit.getY() - GameData.UNIT_SIZE / 4;

        shapeX[3] = exit.getX() - GameData.UNIT_SIZE / 4;
        shapeY[3] = exit.getY();
        
        exit.setShapeX(shapeX);
        exit.setShapeY(shapeY);
        
        world.addEntity(exit);
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("LevelExit stopped");
        world.removeEntity(exit);
        exit = null;
    }

    @Override
    public void process(GameData gameData, World world) {
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (playerSPI == null) {
            System.out.println("LevelExit: Could not find PlayerSPI");
            return;
        }
        
        Entity player = playerSPI.getPlayer(world);
        if (player == null) {
            return;
        }
        float diffX = Math.abs(player.getX() - exit.getX());
        float diffY = Math.abs(player.getY() - exit.getY());
        
        // If the player touches exit, call setLevelComplete
        if (diffX <= GameData.UNIT_SIZE / 2 && diffY <= GameData.UNIT_SIZE / 2) {
            VictorySPI victorySPI = Lookup.getDefault().lookup(VictorySPI.class);
            AudioSPI audioSPI = Lookup.getDefault().lookup(AudioSPI.class);
            if (victorySPI != null) {
                // Play winning audio
                audioSPI.playAudio("LevelExit/target/LevelExit-1.0.0-SNAPSHOT.jar!/assets/sounds/winning.ogg", exit);
                // Increase level
                gameData.setLevel(gameData.getLevel() + 1);
                // Set win
                victorySPI.setLevelComplete(gameData, world);
                // Show Level Complete text
                UIElement text = createLevelCompleteText(gameData);
                gameData.addUIElement(text);
                // Create runnable that removes text after a while
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                    gameData.removeUIElement(text);
                }).start();
            } else {
                System.out.println("LevelExit: Level is complete, but cannot find VictorySPI");
            }
        }
    }
    
    private UIElement createLevelCompleteText(GameData gameData) {
        UIElement uiText = new UIElement();
        
        BufferedImage image = new BufferedImage(400, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        g.setFont(new Font("Consolas", 1, 50));
        g.setColor(Color.GREEN);
        g.drawString("LEVEL COMPLETE!", 0, 50);
        g.setFont(new Font("Consolas", 1, 20));
        g.drawString("Level: " + gameData.getLevel(), 150, 80);
        
        g.dispose();
        
        uiText.setImage(image);
        uiText.setX(gameData.getDisplayWidth() / 2 - image.getWidth() / 2);
        uiText.setY(gameData.getDisplayHeight() / 2 + image.getHeight());
        
        return uiText;
    }
}
