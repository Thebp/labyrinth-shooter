/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import gruppe5.common.node.MapNode;
import java.util.Comparator;

/**
 *
 * @author Gyhuji
 */
public class HeapComparator implements Comparator<Object> {

    @Override
    public int compare(Object t, Object t1) {
        MapNode n1 = (MapNode) t;
        MapNode n2 = (MapNode) t1;

        float compare = n1.fCost() - n2.fCost();
        if (compare == 0) {
            compare = n1.hCost() - n2.hCost();
        }
        return Math.round(-compare); // if openSet is wrongly sorted, flip compare

    }

}
