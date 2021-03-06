package gruppe5.hud;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.UIElement;
import gruppe5.common.data.World;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IUIService;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IUIService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})
public class HUD implements IUIService, IGamePluginService {

    private static final String HEART_REL_PATH = "/assets/images/heart_64.png";
    private static final int HEART_SIZE = 42; // Used for scaling of heart
    private BufferedImage heart; // Holds the heart sprite

    private UIElement healthbarElement;
    private UIElement scoreElement;
    private int prevPlayerLife; // Holds previous player life
    private int prevLebel = -1;

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

        healthbarElement = new UIElement();
        scoreElement = new UIElement();
        gameData.addUIElement(healthbarElement);
        gameData.addUIElement(scoreElement);
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("HUD stopped");
        gameData.removeUIElement(healthbarElement);
        gameData.removeUIElement(scoreElement);
        healthbarElement = null;
        scoreElement = null;
        prevPlayerLife = 0;
    }

    @Override
    public void process(GameData gameData, World world) {
        if (healthbarElement == null) {
            System.out.println("HUD has not been initialized.");
            return;
        }

        // Get player
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        if (playerSPI == null) {
            return;
        }

        Entity player = playerSPI.getPlayer(world);
        if (player != null) {
            updateHealthbar(gameData, player);
            updateLevel(gameData);
        }
    }

    private void updateLevel(GameData gameData) {
        int level = gameData.getLevel();

        if (level != prevLebel) {
            prevLebel = level;
            BufferedImage image = createPlayerScore(level);
            scoreElement.setImage(image);
            scoreElement.setX(10);
            scoreElement.setY(image.getHeight() + 10);
        }
    }

    private BufferedImage createPlayerScore(int level) {
        BufferedImage image = new BufferedImage(80, 15, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setFont(new Font("Consolas", 1, 20));
        g2d.drawString("LEVEL:" + level, 0, 15);

        g2d.dispose();

        return image;
    }

    private void updateHealthbar(GameData gameData, Entity player) {
        int playerLife = 0;
        if (player != null) {
            playerLife = player.getLife();
        }
        // Only draw healthbar if player life has changed
        if (playerLife != prevPlayerLife) {
            prevPlayerLife = playerLife;
            BufferedImage image = createPlayerHealthbar(playerLife);
            healthbarElement.setImage(createPlayerHealthbar(playerLife));
            healthbarElement.setX(gameData.getDisplayWidth() - image.getWidth());
            healthbarElement.setY(gameData.getDisplayHeight() - 10);
        }
    }

    private BufferedImage createPlayerHealthbar(int playerLife) {
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
