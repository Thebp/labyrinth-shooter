/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import gruppe5.common.node.MapNode;
import java.util.Objects;



/**
 *
 * @author Gyhuji
 */
public class Heuristics{
    private int gCost;
    private int hCost;
    private Heuristics parent;
    private MapNode node;
    
    public Heuristics(MapNode node) {
        this.node = node;
    }

    public Heuristics getParent() {
        return parent;
    }

    public void setParent(Heuristics newparent) {
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
    
    public MapNode getNode() {
        return node;
    }
    
    public void setNode(MapNode node) {
        this.node = node;
    }

    @Override
    public boolean equals(Object obj) {
        final Heuristics other = (Heuristics) obj;
        if (!Objects.equals(this.node, other.node)) {
            return false;
        }
        return true;
    }
    
    
}
