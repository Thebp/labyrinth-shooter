/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.fpscounter;

import gruppe5.common.data.GameData;
import gruppe5.common.data.UIElement;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IUIService;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Font;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author nick
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IUIService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class FPSCounter implements IGamePluginService, IUIService {
    // For calculating FPS
    private static final int MAX_FPS_SAMPLES = 60;
    private float[] fpsSamples = new float[MAX_FPS_SAMPLES];
    private int currentSampleIndex = 0;
    
    // FPSCounter
    private UIElement counter;
    
    @Override
    public void start(GameData gameData, World world) {
        System.out.println("FPSCounter started");
        counter = new UIElement();
        gameData.addUIElement(counter);
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("FPSCounter stopped");
        gameData.removeUIElement(counter);
    }
    
    @Override
    public void process(GameData gameData, World world) {
        if (!gameData.getUIElements().contains(counter)) {
            System.out.println("FPSCounter has not been initialized.");
            return;
        }
        
        counter.setImage(createCounterImage(gameData, world));
        counter.setX(10);
        counter.setY(gameData.getDisplayHeight());
    }
    
    private BufferedImage createCounterImage(GameData gameData, World world) {
        BufferedImage image = new BufferedImage(120, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        
        int fps = Math.round(calcAverageFPS(gameData.getDelta()));
        
        g.setFont(new Font("Sans", Font.BOLD, 14));
        g.setColor(Color.GREEN);
        
        g.drawString("FPS: " + fps, 0, 20);
        g.drawString("Entities: " + world.getEntities().size(), 0, 35);
        g.dispose();
        
        return image;
    }
    
    /**
     * Calculates the average FPS to give a more stable count
     * @param delta This cycle's delta value
     * @return 
     */
    private float calcAverageFPS(float delta) {
        // Add current fps to samples
        if (++currentSampleIndex < MAX_FPS_SAMPLES)
            fpsSamples[currentSampleIndex] = 1.0f/delta;
        else
            currentSampleIndex = 0;
        
        // Calculate average fps for all samples
        float avg = 0;
        for (int i = 0; i < MAX_FPS_SAMPLES; i++) {
            avg += fpsSamples[i];
        }
        avg /= MAX_FPS_SAMPLES;
        
        return avg;
    }
}
