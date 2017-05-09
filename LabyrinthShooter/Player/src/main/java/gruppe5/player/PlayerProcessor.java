package gruppe5.player;

import gruppe5.common.audio.AudioSPI;
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
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = PlayerSPI.class)
})

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
            float b = (3.1415f / 2);

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                radians += rotationSpeed * dt;
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                radians -= rotationSpeed * dt;
            }
            AudioSPI audioSPI = Lookup.getDefault().lookup(AudioSPI.class);
            WeaponSPI weaponSPI = Lookup.getDefault().lookup(WeaponSPI.class);
            //shooting
            if (gameData.getKeys().isPressed(GameKeys.SPACE) && weaponSPI != null) {
                Entity weapon = null;
                for (Entity entity : player.getEntities(Weapon.class)) {
                    weapon = entity;

                }
                if (weapon != null) {
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
                        //world.removeEntity(weapon);
                    }
                    player.addSubEntity(newWeapon);
                    newWeapon.setOwner(player);
                    player.setImagePath("Player/target/Player-1.0.0-SNAPSHOT.jar!/assets/images/shooting.png");
                }
            }
            double diffx = gameData.getMouseX() - player.getX();
            double diffy = gameData.getMouseY() - player.getY();
//            double diffx = player.getX() - gameData.getMouseX();
//            double diffy = player.getY() - gameData.getMouseY();
            radians = (float) Math.atan2(diffy, diffx);

            if (player.getLife() <= 0) {
                world.removeEntity(player);
                if (audioSPI != null) {
                    audioSPI.playAudio(player.getSoundPath(), player);
                }
            }
            //Movement
//            if (gameData.getKeys().isDown(GameKeys.UP)) {
//                dx = (float) Math.cos(radians) * maxSpeed;
//                dy = (float) Math.sin(radians) * maxSpeed;
//            }
//            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
//                radians += rotationSpeed * dt;
//            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
//                radians -= rotationSpeed * dt;
//            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                dy += 100;
                if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                    dx += 100;
                }
                if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                    dx -= 100;
                }
            } else if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                dy -= 100;
                if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                    dx += 100;
                }
                if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                    dx -= 100;
                }
            } else if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                dx -= 100;
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                dx += 100;
            }

            // set position
            x += dx * dt;
            y += dy * dt;

            if (gameData.isNoclip()) {
                shapeX[0] = 0;
                shapeY[0] = 0;

                shapeX[1] = 0;
                shapeY[1] = 0;

                shapeX[2] = 0;
                shapeY[2] = 0;

                shapeX[3] = 0;
                shapeY[3] = 0;

                shapeX[4] = 0;
                shapeY[4] = 0;

                shapeX[5] = 0;
                shapeY[5] = 0;

                shapeX[6] = 0;
                shapeY[6] = 0;

                shapeX[7] = 0;
                shapeY[7] = 0;
            } else {
                int modifier = 2;
                shapeX[0] = (float) (player.getX() + Math.cos(radians) * player.getRadius() / modifier);
                shapeY[0] = (float) (player.getY() + Math.sin(radians) * player.getRadius() / modifier);

                shapeX[1] = (float) (player.getX() + Math.cos(radians + b / 2) * player.getRadius() / modifier);
                shapeY[1] = (float) (player.getY() + Math.sin(radians + b / 2) * player.getRadius() / modifier);

                shapeX[2] = (float) (player.getX() + Math.cos(radians + b) * player.getRadius() / modifier);
                shapeY[2] = (float) (player.getY() + Math.sin(radians + b) * player.getRadius() / modifier);

                shapeX[3] = (float) (player.getX() + Math.cos(radians + b * 1.5) * player.getRadius() / modifier);
                shapeY[3] = (float) (player.getY() + Math.sin(radians + b * 1.5) * player.getRadius() / modifier);

                shapeX[4] = (float) (player.getX() + Math.cos(radians + b * 2) * player.getRadius() / modifier);
                shapeY[4] = (float) (player.getY() + Math.sin(radians + b * 2) * player.getRadius() / modifier);

                shapeX[5] = (float) (player.getX() + Math.cos(radians + b * 2.5) * player.getRadius() / modifier);
                shapeY[5] = (float) (player.getY() + Math.sin(radians + b * 2.5) * player.getRadius() / modifier);

                shapeX[6] = (float) (player.getX() + Math.cos(radians + b * 3) * player.getRadius() / modifier);
                shapeY[6] = (float) (player.getY() + Math.sin(radians + b * 3) * player.getRadius() / modifier);

                shapeX[7] = (float) (player.getX() + Math.cos(radians + b * 3.5) * player.getRadius() / modifier);
                shapeY[7] = (float) (player.getY() + Math.sin(radians + b * 3.5) * player.getRadius() / modifier);
            }

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
        for (Entity player : world.getEntities(Player.class)) {
            return player;
        }
        return null;
    }
}
