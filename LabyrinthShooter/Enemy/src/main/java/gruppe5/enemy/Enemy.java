package gruppe5.enemy;

import gruppe5.common.data.Entity;
import gruppe5.common.map.MapNode;

public class Enemy extends Entity{
    private Entity target;
    private MapNode targetNode;
    
    public Entity getTarget() {
        return target;
    }
    
    public void setTarget(Entity entity) {
        this.target = entity;
    }
    
    public MapNode getTargetNode() {
        return targetNode;
    }
    
    public void setTargetNode(MapNode targetNode) {
        this.targetNode = targetNode;
    }
}