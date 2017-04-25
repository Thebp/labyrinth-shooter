/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.weapon;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.weapon.Weapon;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author Marc
 */
public class WeaponProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Weapon.class)) {
            Weapon weapon = (Weapon) entity;
            moveWeapon(weapon);
        }
    }

    private void moveWeapon(Weapon weapon) {
        Entity owner = weapon.getOwner();
        if (owner != null) {
            weapon.setPosition(owner.getX(), owner.getY());
            weapon.setRadians(owner.getRadians());
        }
    }

//    private void drawWeapon(Weapon weapon) {
//        float[] shapex = weapon.getShapeX();
//        float[] shapey = weapon.getShapeY();
//        float x = weapon.getX();
//        float y = weapon.getY();
//        float radians = weapon.getRadians();
//        float radius = weapon.getRadius();
//
//        shapex[0] = (float) (x + Math.cos(radians - Math.PI * 0.1) * radius);
//        shapey[0] = (float) (y + Math.sin(radians - Math.PI * 0.1) * radius);
//
//        shapex[1] = (float) (x + Math.cos(radians + Math.PI * 0.1) * radius);
//        shapey[1] = (float) (y + Math.sin(radians + Math.PI * 0.1) * radius);
//
//        shapex[2] = (float) (x + Math.cos(radians - Math.PI * 1.1) * radius);
//        shapey[2] = (float) (y + Math.sin(radians - Math.PI * 1.1) * radius);
//
//        shapex[3] = (float) (x + Math.cos(radians + Math.PI * 1.1) * radius);
//        shapey[3] = (float) (y + Math.sin(radians + Math.PI * 1.1) * radius);
//
//        weapon.setShapeX(shapex);
//        weapon.setShapeY(shapey);
//    }

}
