/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.enemyai;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author nick
 */
public class HeuristicsPriorityQueue {
    private ArrayList<Heuristics> heap;
    private Comparator<Object> comparator;
    
    public HeuristicsPriorityQueue() {
        this.heap = new ArrayList();
    }
    
    /**
     * Extracts min
     * @return 
     */
    public Heuristics remove() {
        if (heap.size() < 1)
            throw new Error("heap underflow");
        Heuristics min = heap.get(0);
        heap.set(0, heap.get(heap.size() - 1));
        heap.remove(heap.size() - 1);
        minHeapify(0);
        return min;
    }
    
    public void add(Heuristics e) {
        heap.add(e);
        int i = heap.indexOf(e);
        while (i > 0 && heap.get(parent(i)).fCost() > heap.get(i).fCost()) {
            exchange(i, parent(i));
            i = parent(i);
        }
    }
    
    public boolean contains(Heuristics heuristics) {
        return heap.contains(heuristics);
    }
    
    public int size() {
        return heap.size();
    }
    
    private int parent(int i) {
        return i % 2 == 0 ? i/2-1 : (i-1)/2;
    }
    
    /**
     * Recursive sorting starting from <param>i</param>
     * @param i 
     */
    private void minHeapify(int i) {
        // Children
        int l = 2*i+1;
        int r = 2*i+2;
        
        int smallest;
        
        if (l < heap.size() && heap.get(l).fCost() < heap.get(i).fCost()) 
            smallest = l;
        else
            smallest = i;
        
        if (r < heap.size() && heap.get(r).fCost() < heap.get(smallest).fCost())
            smallest = r;
        
        if (smallest != i) {
            exchange(i, smallest);
            minHeapify(smallest);
        }
    }
    
    private void exchange(int a, int b) {
        Heuristics ha = heap.get(a);
        Heuristics hb = heap.get(b);
        
        heap.set(a, hb);
        heap.set(b, ha);
    }
}
