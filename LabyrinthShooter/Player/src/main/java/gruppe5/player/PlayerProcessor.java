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
import gruppe5.common.player.Player;
import gruppe5.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Christian
 */
@ServiceProvider(service = IEntityProcessingService.class)

public class PlayerProcessor implements IEntityProcessingService {

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

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                radians += rotationSpeed * dt;
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                radians -= rotationSpeed * dt;
            }

            //Taken from asteroids just to remember to implement it in this project
            //shooting
//            if(gameData.getKeys().isPressed(GameKeys.SPACE)){
//                gameData.addEvent(new Event(PLAYER_SHOOT, player.getID()));
//            }
//            
//            if(player.getIsHit() == true){
//                world.removeEntity(player);
//                player.setIsHit(false);
//            }
            //accelerating
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                dx += Math.cos(radians) * acceleration * dt;
                dy += Math.sin(radians) * acceleration * dt;
            }

            // deceleration
            float vec = (float) Math.sqrt(dx * dx + dy * dy);
            if (vec > 0) {
                dx -= (dx / vec) * deceleration * dt;
                dy -= (dy / vec) * deceleration * dt;
            }
            if (vec > maxSpeed) {
                dx = (dx / vec) * maxSpeed;
                dy = (dy / vec) * maxSpeed;
            }

            // set position
            x += dx * dt;
            y += dy * dt;

            shapeX[0] = (float) (player.getX() + Math.cos(radians + Math.PI * 1) * player.getRadius());
            shapeY[0] = (float) (player.getY() + Math.sin(radians + Math.PI * 1) * player.getRadius());

            shapeX[1] = (float) (player.getX() + Math.cos(radians + Math.PI * 1.25) * player.getRadius());
            shapeY[1] = (float) (player.getY() + Math.sin(radians + Math.PI * 1.25) * player.getRadius());

            shapeX[2] = (float) (player.getX() + Math.cos(radians + Math.PI * 1.5) * player.getRadius());
            shapeY[2] = (float) (player.getY() + Math.sin(radians + Math.PI * 1.5) * player.getRadius());

            shapeX[3] = (float) (player.getX() + Math.cos(radians + Math.PI * 1.75) * player.getRadius());
            shapeY[3] = (float) (player.getY() + Math.sin(radians + Math.PI * 1.75) * player.getRadius());

            shapeX[4] = (float) (player.getX() + Math.cos(radians + Math.PI * 2) * player.getRadius());
            shapeY[4] = (float) (player.getY() + Math.sin(radians + Math.PI * 2) * player.getRadius());

            shapeX[5] = (float) (player.getX() + Math.cos(radians + Math.PI * 2.25) * player.getRadius());
            shapeY[5] = (float) (player.getY() + Math.sin(radians + Math.PI * 2.25) * player.getRadius());

            shapeX[6] = (float) (player.getX() + Math.cos(radians + Math.PI * 2.5) * player.getRadius());
            shapeY[6] = (float) (player.getY() + Math.sin(radians + Math.PI * 2.5) * player.getRadius());

            shapeX[7] = (float) (player.getX() + Math.cos(radians + Math.PI * 2.75) * player.getRadius());
            shapeY[7] = (float) (player.getY() + Math.sin(radians + Math.PI * 2.75) * player.getRadius());

            player.setX(x);
            player.setY(y);
            player.setDx(dx);
            player.setDy(dy);
            player.setRadians(radians);
            player.setAcceleration(acceleration);
            player.setDeacceleration(deceleration);
            player.setRotationSpeed(rotationSpeed);
            player.setMaxSpeed(maxSpeed);
            player.setShapeX(shapeX);
            player.setShapeY(shapeY);

        }
    }
}
