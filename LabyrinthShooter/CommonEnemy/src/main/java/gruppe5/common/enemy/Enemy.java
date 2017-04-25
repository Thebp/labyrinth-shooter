/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.enemy;

import gruppe5.common.data.Entity;
import gruppe5.common.map.MapNode;

/**
 *
 * @author marcn
 */
public class Enemy extends Entity{
    private Entity target;
    private MapNode targetNode;
    private MapNode nextNode;

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public MapNode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(MapNode targetNode) {
        this.targetNode = targetNode;
    }

    public MapNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(MapNode nextNode) {
        this.nextNode = nextNode;
    }
}
