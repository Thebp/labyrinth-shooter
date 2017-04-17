/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.map.MapNode;
import gruppe5.common.map.MapSPI;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.weapon.Weapon;
import java.util.List;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IGamePluginService.class)
/**
 *
 * @author Marc
 */
public class WeaponPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("WeaponPlugin started.");
        MapSPI mapSPI = Lookup.getDefault().lookup(MapSPI.class);

        if (mapSPI != null) {
            List<MapNode> mapNodes = mapSPI.getMap();
            if (mapNodes != null) {
                MapNode node = mapNodes.get(5);
                Weapon weapon = new Weapon();
                weapon.setCollidable(false);
                weapon.setDynamic(true);
                weapon.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
                weapon.setRadius(12);
            }
        }
        Weapon weapon = new Weapon();
        weapon.setCollidable(false);
        weapon.setDynamic(true);
        weapon.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        weapon.setRadius(12);
        world.addEntity(weapon);
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity weapon : world.getEntities(Weapon.class)) {
            world.removeEntity(weapon);
        }
    }

}
