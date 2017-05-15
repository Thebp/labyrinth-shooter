/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import gruppe5.common.node.MapNode;



/**
 *
 * @author Gyhuji
 */
public class Heuristics{
    int gCost;
    int hCost;
    MapNode parent;

    public MapNode getParent() {
        return parent;
    }

    public void setParent(MapNode newparent) {
        this.parent = newparent;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int newgCost) {
        this.gCost = newgCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int newhCost) {
        this.hCost = newhCost;
    }
    
    public float fCost(){
        return gCost + hCost;
    }
}
