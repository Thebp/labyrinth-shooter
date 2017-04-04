/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.mapgenerator;

import gruppe5.common.map.MapNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nick
 */
public class Node implements MapNode {
    private List<MapNode> neighbours = new ArrayList<>();
    private boolean isMiddle = false;
    private float x = 0.0f;
    private float y = 0.0f;
    
    @Override
    public List<MapNode> getNeighbours() {
        return neighbours;
    }

    @Override
    public boolean isMiddle() {
        return isMiddle;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
    
    public void setIsMiddle(boolean isMiddle) {
        this.isMiddle = isMiddle;
    }
    
    public void setX(float x) {
        this.x = x;
    }
    
    public void setY(float y) {
        this.y = y;
    }
}
