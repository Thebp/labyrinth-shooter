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

    Entity enemy;
    float y;
    float x;
    float dy;
    float dx;
    float acceleration;
    float deacceleration;
    float maxSpeed;
    float radians;
    float rotationspeed;
    float dt;

    public void enemyMovementSystem(Entity enemy, GameData gameData) {

        //Getters for movement variables
        y = enemy.getY();
        x = enemy.getX();
        dy = enemy.getDy();
        dx = enemy.getDx();
        acceleration = enemy.getAcceleration();
        deacceleration = enemy.getDeacceleration();
        maxSpeed = enemy.getMaxSpeed();
        radians = enemy.getRadians();
        rotationspeed = enemy.getRotationSpeed();
        dt = gameData.getDelta();
    }

    //Move forward
    public void moveForwards(Entity enemy) {
        dx += (float) Math.cos(radians) * acceleration * dt;
        dy += (float) Math.sin(radians) * acceleration * dt;
        enemy.setDx(dx);
        enemy.setDy(dy);
    }

    //Turn 90 degrees left
    public void turnLeft(Entity enemy) {
        radians += 3.1415f / 2;
        enemy.setRadians(radians);
    }

    //Turn 90 degrees right
    public void turnRight(Entity enemy) {
        radians -= 3.1415f / 2;
        enemy.setRadians(radians);
    }

}