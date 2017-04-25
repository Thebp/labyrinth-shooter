/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.hud;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.UIElement;
import gruppe5.common.data.World;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IUIService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Nick
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IUIService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class HUD implements IUIService, IGamePluginService {

    private static final String HEART_REL_PATH = "/assets/images/heart_64.png";
    private static final int HEART_SIZE = 42; // Used for scaling of heart
    private BufferedImage heart; // Holds the heart sprite
    
    
    private UIElement healthbar;
    private int prevPlayerLife; // Holds previous player life

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("HUD started");
        // Load heart image
        try {
            InputStream in = getClass().getResourceAsStream(HEART_REL_PATH);
            heart = ImageIO.read(in);
        } catch (IOException ex) {
            System.out.println("HUD.process(): Error when reading image '" + HEART_REL_PATH + "' :\n" + ex);
        }
        
        healthbar = new UIElement();
        gameData.addUIElement(healthbar);
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("HUD stopped");
        gameData.removeUIElement(healthbar);
        healthbar = null;
        prevPlayerLife = 0;
    }

    @Override
    public void process(GameData gameData, World world) {
        if (healthbar == null) {
            System.out.println("HUD has not been initialized.");
            return;
        }
        
        // Get player
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (playerSPI == null) {
            System.out.println("HUD: PlayerSPI module not found.");
            return;
        }
        Entity player = playerSPI.getPlayer(world);
        int playerLife = player.getLife();
        
        // Only draw healthbar if player life has changed
        if (playerLife != prevPlayerLife) {
            prevPlayerLife = playerLife;
            BufferedImage image = drawPlayerHealthbar(playerLife);
            healthbar.setImage(drawPlayerHealthbar(player.getLife()));
            healthbar.setX(gameData.getDisplayWidth() - image.getWidth());
            healthbar.setY(gameData.getDisplayHeight() - 10);
        }
    }

    private BufferedImage drawPlayerHealthbar(int playerLife) {
        if (playerLife <= 0) {
            // Return empty image if player has no life
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        int heartWidth = HEART_SIZE + 10;
        int heartHeight = HEART_SIZE;
        BufferedImage image = new BufferedImage(heartWidth * playerLife, heartHeight, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = image.createGraphics();

        // Draw heart for each life onto image
        for (int i = 0; i < playerLife; i++) {
            g2d.drawImage(heart, i * heartWidth, 0, HEART_SIZE, HEART_SIZE, null);
        }

        g2d.dispose();

        return image;
    }
}