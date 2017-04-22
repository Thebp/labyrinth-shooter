package gruppe5.enemy;

import gruppe5.common.data.Entity;

public class Enemy extends Entity{
    private Entity target;
    
    public Entity getTarget() {
        return target;
    }
    
    public void setTarget(Entity entity) {
        this.target = entity;
    }
}