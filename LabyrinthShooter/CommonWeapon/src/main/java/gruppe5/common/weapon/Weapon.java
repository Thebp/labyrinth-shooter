package gruppe5.common.weapon;

import gruppe5.common.data.Entity;

public class Weapon extends Entity{
    private Entity owner;
    public Entity getOwner() {
        return owner;
    }
    
    public void setOwner(Entity entity) {
        this.owner = entity;
    }
}
