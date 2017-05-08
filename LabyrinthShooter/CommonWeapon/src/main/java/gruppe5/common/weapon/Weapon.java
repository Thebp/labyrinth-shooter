package gruppe5.common.weapon;

import gruppe5.common.data.Entity;

public class Weapon extends Entity{
    private Entity owner;
    private float cooldown;
    
    public Entity getOwner() {
        return owner;
    }
    
    public void setOwner(Entity entity) {
        this.owner = entity;
    }
    
    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
    
    public void reduceCooldown(float dt) {
        this.cooldown -= dt;
    }
    
    public float getCooldown() {
        return cooldown;
    }
    
}
