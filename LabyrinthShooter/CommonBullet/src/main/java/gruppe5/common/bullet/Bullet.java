package gruppe5.common.bullet;

import gruppe5.common.data.Entity;

public class Bullet extends Entity{
    private Entity owner;
    
    public Entity getOwner() {
        return owner;
    }
    
    public void setOwner(Entity e) {
        this.owner = e;
    }
}
