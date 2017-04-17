/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemy;

import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;

/**
 *
 * @author Gyhuji
 */
public class EnemyMovementSystem {



    public void enemyMovementSystem(Entity enemy, GameData gameData) {

    }

    //Move forward
    public void moveForwards(Entity enemy, GameData gameData) {
        float x = enemy.getX();
        float y = enemy.getY();
        float radians = enemy.getRadians();
        float maxSpeed = enemy.getMaxSpeed();
        float dt = gameData.getDelta();
        float dx = enemy.getDx();
        float dy = enemy.getDy();
                
        dx += (float) Math.cos(radians) * maxSpeed;
        dy += (float) Math.sin(radians) * maxSpeed;
        enemy.setDx(dx);
        enemy.setDy(dy);
        enemy.setPosition(x + dx * dt, y + dy * dt);
    }

    //Turn 90 degrees left
    public void turnLeft(Entity enemy) {
        float radians = enemy.getRadians();
        radians += 3.1415f / 2;
        enemy.setRadians(radians);
    }

    //Turn 90 degrees right
    public void turnRight(Entity enemy) {
        float radians = enemy.getRadians();
        radians -= 3.1415f / 2;
        enemy.setRadians(radians);
    }

}