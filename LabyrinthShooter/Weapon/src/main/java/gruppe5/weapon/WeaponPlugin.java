/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.weapon.Weapon;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)
/**
 *
 * @author Marc
 */
public class WeaponPlugin implements IGamePluginService{

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("WeaponPlugin started.");
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity weapon : world.getEntities(Weapon.class)) {
            world.removeEntity(weapon);
        }
    }
    
}
