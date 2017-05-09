/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.enemy;

import gruppe5.common.data.Entity;
import gruppe5.common.node.MapNode;
import java.util.List;

/**
 *
 * @author marcn
 */
public class Enemy extends Entity{
    private List<MapNode> path;
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

    public List<MapNode> getPath() {
        return path;
    }

    public void setPath(List<MapNode> path) {
        this.path = path;
    }
    
    
}
