package api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DWG implements DirectedWeightedGraph{

    private HashMap<Integer, NodeData> node_data;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edge_data;

    public DWG(){
        this.node_data = new HashMap<>();
        this.edge_data = new HashMap<>();
    }

    @Override
    public NodeData getNode(int key) {
        if (this.node_data.containsKey(key))
            return this.node_data.get(key);
        return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        if (this.edge_data.containsKey(src) && this.edge_data.get(src).containsKey(dest))
            return this.edge_data.get(src).get(dest);
        return null;
    }

    @Override
    public void addNode(NodeData n) {
        this.node_data.put(n.getKey(), n);
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (this.node_data.containsKey(src) && this.node_data.containsKey(dest)){
            EdgeData e = new ED(src, dest, w);
            if (!this.edge_data.containsKey(src)){
                HashMap<Integer, EdgeData> temp = new HashMap<>();
                temp.put(dest, e);
                this.edge_data.put(src, temp);
            }
            this.edge_data.get(src).put(dest, e);
        }

    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return this.node_data.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        List<EdgeData> edges = new ArrayList<>();
        for (HashMap<Integer, EdgeData> map : this.edge_data.values()) {
            edges.addAll(map.values());
        }
        return edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return this.edge_data.get(node_id).values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        this.edge_data.remove(key);
        HashMap<Integer, Integer> removeList = new HashMap<>();
        for (int h : this.edge_data.keySet()){
            for (int e : this.edge_data.get(h).keySet()){
                if (e == key){
                    removeList.put(h, e);
                }
            }
        }

        for (int k : removeList.keySet()){
            this.edge_data.get(k).remove(removeList.get(k));
        }
        return this.node_data.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (this.edge_data.containsKey(src)){
            return this.edge_data.get(src).remove(dest);
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return this.node_data.size();
    }

    @Override
    public int edgeSize() {
        return this.edge_data.size();
    }

    @Override
    public int getMC() {
        return 0;
    }
}
