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
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nick
 */
@ServiceProvider(service = IUIService.class)
public class HUD implements IUIService {
    UIElement healthbar;
    
    @Override
    public void process(GameData gameData, World world) {
        if (healthbar != null) {
            gameData.removeUIElement(healthbar);
        }
        
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (playerSPI == null) {
            System.out.println("HUD: PlayerSPI not found.");
            return;
        }
        Entity player = playerSPI.getPlayer(world);
        
        healthbar = new UIElement();
        healthbar.setX(player.getX() + 100);
        healthbar.setY(player.getY() + 100);
        BufferedImage image = drawHealthbar(player.getLife());
        healthbar.setImage(image);
        healthbar.setLen(200);
        
        gameData.addUIElement(healthbar);
    }
    
    private BufferedImage drawHealthbar(int life) {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR);
        
        Graphics2D g2d = image.createGraphics();
        
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(10, 10, 100, 100);
        g2d.dispose();
        
        return image;
    }
    
    private byte[] toByteArray(BufferedImage image) {
        byte[] byteImage = null;
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "BMP", baos);
            baos.flush();
            byteImage = baos.toByteArray();
        } catch (IOException ex) {
            System.out.println("HUD.toByteArray(): " + ex);
        }
        
        return byteImage;
    }
}
