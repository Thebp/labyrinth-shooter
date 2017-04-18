/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.hud;

import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IUIService;
import java.awt.Graphics2D;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Nick
 */
@ServiceProvider(service = IUIService.class)
public class HUD implements IUIService {

    @Override
    public void process(GameData gameData, World world) {
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        
        if (playerSPI == null) {
            System.out.println("HUD: PlayerSPI not found.");
            return;
        }
        
        
    }
    
}
