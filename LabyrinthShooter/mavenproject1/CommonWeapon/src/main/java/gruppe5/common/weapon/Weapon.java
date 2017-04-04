/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.weapon;

import gruppe5.common.data.Entity;

/**
 *
 * @author marcn
 */
public class Weapon extends Entity{
    private Entity owner;
    
    public Entity getOwner() {
        return owner;
    }
    
    public void setOwner(Entity entity) {
        this.owner = entity;
    }
}
