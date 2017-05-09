package gruppe5.common.node;

import java.util.ArrayList;
import java.util.List;

public class Node implements MapNode {
    private List<MapNode> neighbours = new ArrayList<>();
    private boolean isMiddle = false;
    private float x = 0.0f;
    private float y = 0.0f;
    
    @Override
    public List<MapNode> getNeighbours() {
        return neighbours;
    }
    
    public List<MapNode> getNeighbouringCenterNodes() {
        ArrayList<MapNode> centerNodes = new ArrayList();
        for (MapNode neighbour : neighbours) {
            if (neighbour.isMiddle())
                centerNodes.add(neighbour);
        }
        return centerNodes;
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
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + (isMiddle ? " M" : "") + ")";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.isMiddle ? 1 : 0);
        hash = 17 * hash + Float.floatToIntBits(this.x);
        hash = 17 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
}
