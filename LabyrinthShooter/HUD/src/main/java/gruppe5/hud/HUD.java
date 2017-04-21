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
import gruppe5.common.services.IUIService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nick
 */
@ServiceProvider(service = IUIService.class)
public class HUD implements IUIService {

    private static final String HEART_REL_PATH = "/assets/images/heart_32.png";

    private BufferedImage heart; // Holds heart sprite
    private int heartWidth;
    
    private UIElement healthbar;
    private int prevPlayerLife; // Holds previous player life

    @Override
    public void start(GameData gameData, World world) {
        // Load heart image
        try {
            InputStream in = getClass().getResourceAsStream(HEART_REL_PATH);
            heart = ImageIO.read(in);
            heartWidth = heart.getWidth() + 10;
        } catch (IOException ex) {
            System.out.println("HUD.process(): Error when reading image '" + HEART_REL_PATH + "' :\n" + ex);
        }
        
        healthbar = new UIElement();
        gameData.addUIElement(healthbar);
    }

    @Override
    public void stop(GameData gameData, World world) {
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
            prevPlayerLife = player.getLife();
            healthbar.setX(1000 - heartWidth*playerLife);
            healthbar.setY(790);
            healthbar.setImage(drawPlayerHealthbar(player.getLife()));
        }
    }

    private BufferedImage drawPlayerHealthbar(int playerLife) {
        if (playerLife <= 0) {
            // Return empty image if player has no life
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        
        BufferedImage image = new BufferedImage(heartWidth * playerLife, heart.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw heart for each life onto image
        for (int i = 0; i < playerLife; i++) {
            g2d.drawImage(heart, i * heartWidth, 0, null);
        }

        g2d.dispose();

        return image;
    }

}
