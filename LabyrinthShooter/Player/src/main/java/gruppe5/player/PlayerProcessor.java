/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.player;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.GameKeys;
import gruppe5.common.data.World;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.weapon.Weapon;
import gruppe5.common.weapon.WeaponSPI;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christian
 */
@ServiceProvider(service = IEntityProcessingService.class)

public class PlayerProcessor implements IEntityProcessingService, PlayerSPI {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(Player.class)) {
            float x = player.getX();
            float y = player.getY();
            float dx = player.getDx();
            float dy = player.getDy();
            float acceleration = player.getAcceleration();
            float deceleration = player.getDeacceleration();
            float maxSpeed = player.getMaxSpeed();
            float radians = player.getRadians();
            float[] shapeX = player.getShapeX();
            float[] shapeY = player.getShapeY();
            float dt = gameData.getDelta();
            int rotationSpeed = player.getRotationSpeed();
            float b = (3.1415f / 4);

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                radians += rotationSpeed * dt;
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                radians -= rotationSpeed * dt;
            }

            WeaponSPI weaponSPI = Lookup.getDefault().lookup(WeaponSPI.class);
            //shooting
            if (gameData.getKeys().isPressed(GameKeys.SPACE) && weaponSPI != null) {
                Entity weapon = null;
                for(Entity entity : player.getEntities(Weapon.class)){
                    weapon = entity;
                    
                }
                if(weapon != null){
                    weaponSPI.shoot(world, weapon);
                }
            }

            if (gameData.getKeys().isPressed(GameKeys.ENTER) && weaponSPI != null) {

                Weapon newWeapon = (Weapon) weaponSPI.equipWeapon(world, player);
                
                if (newWeapon != null) {
                    for (Entity entity : player.getEntities(Weapon.class)) {
                        Weapon weapon = (Weapon) entity;
                        player.removeSubEntity(weapon);
                        weapon.setOwner(null);
                    }
                    player.addSubEntity(newWeapon);
                    newWeapon.setOwner(player);
                }
            }

            if (player.isHit()) {
                player.setLife(player.getLife() - 1);
                player.setIsHit(false);
            }
            //Taken from asteroids just to remember to implement it in this project
//            if(player.getIsHit() == true){
//                world.removeEntity(player);
//                player.setIsHit(false);
//            }
            //accelerating
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                dx = (float) Math.cos(radians) * maxSpeed;
                dy = (float) Math.sin(radians) * maxSpeed;
            }

            // set position
            x += dx * dt;
            y += dy * dt;

            shapeX[0] = (float) (player.getX() + Math.cos(radians - b) * player.getRadius());
            shapeY[0] = (float) (player.getY() + Math.sin(radians - b) * player.getRadius());

            shapeX[1] = (float) (player.getX() + Math.cos(radians + b) * player.getRadius());
            shapeY[1] = (float) (player.getY() + Math.sin(radians + b) * player.getRadius());

            shapeX[2] = (float) (player.getX() + Math.cos(radians + b * 3) * player.getRadius());
            shapeY[2] = (float) (player.getY() + Math.sin(radians + b * 3) * player.getRadius());

            shapeX[3] = (float) (player.getX() + Math.cos(radians + b * 5) * player.getRadius());
            shapeY[3] = (float) (player.getY() + Math.sin(radians + b * 5) * player.getRadius());

            player.setX(x);
            player.setY(y);
            player.setDx(0);
            player.setDy(0);
            player.setRadians(radians);
            player.setAcceleration(acceleration);
            player.setDeacceleration(deceleration);
            player.setRotationSpeed(rotationSpeed);
            player.setMaxSpeed(maxSpeed);
            player.setShapeX(shapeX);
            player.setShapeY(shapeY);

        }
    }

    @Override
    public Entity getPlayer(World world) {
        for(Entity player : world.getEntities(Player.class)) {
            return player;
        }
        return null;
    }
}
