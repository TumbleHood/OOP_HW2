package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DWGA implements DirectedWeightedGraphAlgorithms{

    private DirectedWeightedGraph graph;
    private Stack<Integer> stack = new Stack<>();

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        DirectedWeightedGraph gf = new DWG();
        Iterator<NodeData> n = this.graph.nodeIter();

        while(n.hasNext())
        {
            NodeData node=n.next();
            int the_key=node.getKey();
            gf.addNode(node);
            gf.getNode(the_key).setLocation(this.graph.getNode(the_key).getLocation());
            gf.getNode(the_key).setInfo(this.graph.getNode(the_key).getInfo());

            gf.getNode(the_key).setTag(this.graph.getNode(the_key).getTag());
        }
        Iterator<EdgeData> e = this.graph.edgeIter();
        while(e.hasNext()){
            EdgeData edge = e.next();
            int src = edge.getSrc();
            int dest = edge.getDest();
            double weight = edge.getWeight();
            gf.connect(src, dest, weight);
            gf.getEdge(src, dest).setInfo(this.graph.getEdge(src, dest).getInfo());
            gf.getEdge(src, dest).setTag(this.graph.getEdge(src, dest).getTag());
        }
        return gf;
    }

    @Override
    public boolean isConnected() {
        Iterator<NodeData> n1 = this.graph.nodeIter();

        while (n1.hasNext()){
            Iterator<NodeData> n2 = this.graph.nodeIter();
            NodeData v1 = n1.next();
            while (n2.hasNext()){
                NodeData v2 = n2.next();
                if (v1.getKey() != v2.getKey()){
                    if (this.shortestPath(v1.getKey(), v2.getKey()) == null)
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest){
        List<NodeData> l = this.shortestPath(src, dest);
        double path = 0;
        for (int i = 1; i < l.size(); i++)
            path += this.graph.getEdge(l.get(i-1).getKey(), l.get(i).getKey()).getWeight();
        for (NodeData n : l)
            path += n.getWeight();
        return path;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest){
        if (src == dest){
            List<NodeData> temp = new ArrayList<>();
            temp.add(this.graph.getNode(dest));
            return temp;
        }
        Iterator<EdgeData> e = this.graph.edgeIter(src);
        if (!e.hasNext())
            return null;

        List<List<NodeData>> pathsList = new ArrayList<>();
        while (e.hasNext()){
            EdgeData edge = e.next();
            if (!stack.contains(edge.getSrc())){
                stack.push(edge.getSrc());
                List<NodeData> temp = new ArrayList<>(shortestPath(edge.getDest(), dest));
                stack.pop();
                if (temp.size() > 0){
                    temp.add(0, this.graph.getNode(edge.getSrc()));
                    pathsList.add(temp);
                }
            }
        }

        double min = Double.MAX_VALUE;
        List<NodeData> shortPath = new ArrayList<>();
        for (List<NodeData> path : pathsList){
            double sum = 0;
            for (int i = 1; i < path.size(); i++){
                sum += this.graph.getEdge(path.get(i-1).getKey(), path.get(i).getKey()).getWeight();
            }
            if (sum < min){
                min = sum;
                shortPath = new ArrayList<>(path);
            }
        }
        return shortPath;
    }

    @Override
    public NodeData center() {
        if (this.isConnected()){
            Iterator<NodeData> n1 = this.graph.nodeIter();
            NodeData center = null;
            double min = Double.MAX_VALUE;
            while (n1.hasNext()){
                double sum = 0;
                NodeData v1 = n1.next();
                Iterator<NodeData> n2 = this.graph.nodeIter();
                while (n2.hasNext()){
                    NodeData v2 = n2.next();
                    sum += this.shortestPathDist(v1.getKey(), v2.getKey());
                }
                if (sum < min){
                    min = sum;
                    center = v1;
                }
            }
            return center;
        }
        return null;
    }

//    @Override List<NodeData> tsp(List<NodeData> cities){
//        NodeData first = cities.remove(0);
//        List<List<NodeData>> pathsList = new ArrayList<>();
//        for (int i = 0; i < cities.size(); i++){
//            List<NodeData> path = new ArrayList<>();
//            path.add(first);
//            if (this.shortestPath(first.getKey(), cities.get(i).getKey()) != null){
//                NodeData temp = cities.remove(i);
//                cities.add(0, temp);
//                path.addAll(tsp)
//            }
//        }
//    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        List<List<NodeData>> tspList = new ArrayList<>();

        NodeData current = cities.get(0);
        cities.remove(current);
        List<NodeData> tsp = new ArrayList<>();
        tsp.add(current);
        if (cities.isEmpty())
            return tsp;
        for (int i = 0; i < cities.size(); i++){
            Iterator<EdgeData> edges = this.graph.edgeIter();
            while(edges.hasNext()){
                EdgeData edge = edges.next();
                if (current.getKey() == edge.getSrc() && cities.get(i).getKey() == edge.getDest()){
                    cities.add(0, cities.get(i));
                    cities.remove(i+1);
                    stack.push(1);
                    tsp.addAll(this.tsp(new ArrayList<>(cities)));
                    stack.pop();

                    if (stack.isEmpty()){
                        Iterator<EdgeData> tempEdges = this.graph.edgeIter();
                        while(tempEdges.hasNext()) {
                            EdgeData tempEdge = tempEdges.next();
                            if (tsp.get(tsp.size() - 1).getKey() == tempEdge.getSrc() && current.getKey() == tempEdge.getDest()) {
                                tsp.add(current);
                                tspList.add(tsp);
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (stack.isEmpty()){
            double min = Double.MAX_VALUE;
            List<NodeData> tspMin = new ArrayList<>();
            for (List<NodeData> l : tspList){
                double sum = 0;
                for (int i = 1; i < l.size(); i++){
                    Iterator<EdgeData> edges = this.graph.edgeIter();
                    while (edges.hasNext()){
                        EdgeData edge = edges.next();
                        if (l.get(i-1).getKey() == edge.getSrc() && l.get(i).getKey() == edge.getDest()){
                            sum += edge.getWeight();
                        }
                    }
                }
                if (sum < min){
                    min = sum;
                    tspMin = l;
                }
            }
            return tspMin;
        }
        return tsp;
    }

    @Override
    public boolean save(String file) {
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //if this doesn't work, use the line above.
        Map<String, ArrayList<LinkedTreeMap<String, Object>>> map = new HashMap<>();
        ArrayList<LinkedTreeMap<String, Object>> nodeList = new ArrayList<>();
        ArrayList<LinkedTreeMap<String, Object>> edgeList = new ArrayList<>();

        Iterator<NodeData> nodes = this.graph.nodeIter();
        while (nodes.hasNext()){
            NodeData node = nodes.next();
            LinkedTreeMap<String, Object> ltm = new LinkedTreeMap<>();
            GeoLocation gl = node.getLocation();
            String l = String.join(",", String.valueOf(gl.x()), String.valueOf(gl.y()), String.valueOf(gl.z()));
            ltm.put("pos", l);
            ltm.put("id", node.getKey());
            ltm.put("info", node.getInfo());
            ltm.put("tag", node.getTag());
            nodeList.add(ltm);
        }

        Iterator<EdgeData> edges = this.graph.edgeIter();
        while(edges.hasNext()){
            EdgeData edge = edges.next();
            LinkedTreeMap<String, Object> ltm = new LinkedTreeMap<>();
            ltm.put("src", edge.getSrc());
            ltm.put("w", edge.getWeight());
            ltm.put("dest", edge.getDest());
            ltm.put("info", edge.getInfo());
            ltm.put("tag", edge.getTag());
            edgeList.add(ltm);
        }

        map.put("Edges", edgeList);
        map.put("Nodes", nodeList);

        try{
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            gson.toJson(map, writer);
            writer.close();
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            Map<String, ArrayList<LinkedTreeMap<String, Object>>> map = gson.fromJson(reader, Map.class);
            this.graph = new DWG();

            for (LinkedTreeMap<String, Object> values : map.get("Nodes")) {
                NodeData nd = new ND(((Double) (values.get("id"))).intValue());
                String[] pos = ((String) values.get("pos")).split(",");
                if (values.containsKey("info")){
                    String info = ((String) values.get("info"));
                    nd.setInfo(info);
                }
                if (values.containsKey("tag")){
                    int tag = (((Double) (values.get("tag"))).intValue());
                    nd.setTag(tag);

                }
                GeoLocation gl = new GL(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
                nd.setLocation(gl);
                this.graph.addNode(nd);
            }

            for (LinkedTreeMap<String, Object> values : map.get("Edges")) {
                EdgeData ed = new ED(((Double) (values.get("src"))).intValue(), ((Double) (values.get("dest"))).intValue(), (Double) (values.get("w")));
                if (values.containsKey("info")){
                    String info = ((String) values.get("info"));
                    ed.setInfo(info);
                }
                if (values.containsKey("tag")){
                    int tag = (((Double) (values.get("tag"))).intValue());
                    ed.setTag(tag);

                }
                this.graph.connect(ed.getSrc(), ed.getDest(), ed.getWeight());
            }
            return true;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }
}
