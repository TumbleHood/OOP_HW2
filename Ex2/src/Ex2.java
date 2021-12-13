import api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraphAlgorithms g = new DWGA();
        g.load(json_file);

        return g.getGraph();
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new DWGA();
        ans.load(json_file);

        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        if (new File(json_file).exists()){
            MainGUI mainGui = new MainGUI(json_file);
            mainGui.setVisible(true);
        }
        else
            System.out.println("file does not exist");
    }

    @Test
    public static void checkAlgorithms(String json_file){
        DirectedWeightedGraph graph = new DWG();
        NodeData n1 = new ND(1, new GL(1.0,2.0,3.0), 0.0, "test", 0);
        NodeData n2 = new ND(2, new GL(4.0,5.0,6.0), 0.0, "test", 0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.connect(1, 2, 30.0);
        EdgeData e = new ED(1, 2, 30.0);
        Assertions.assertEquals(e.getSrc(), graph.edgeIter().next().getSrc());
        Assertions.assertEquals(e.getDest(), graph.edgeIter().next().getDest());
        Assertions.assertEquals(e.getWeight(), graph.edgeIter().next().getWeight());

        DirectedWeightedGraphAlgorithms algo = new DWGA();
        graph.connect(2,1, 20.0);
        NodeData n3 = new ND(3, new GL(7.0, 8.0, 9.0), 0.0, "test", 0);
        graph.addNode(n3);
        graph.connect(1, 3, 5.0);
        graph.connect(3, 2, 5.0);

        algo.init(graph);
        Assertions.assertTrue(algo.isConnected());
        Assertions.assertEquals(algo.shortestPathDist(1, 2), 10.0);
        Assertions.assertEquals(algo.shortestPathDist(2, 1), 20.0);

        for (int x = 1; x <= 1000; x *= 10){
            graph = new DWG();
            System.out.println(1000 * x);
            for (int i = 0; i < 1000 * x; i++){
                NodeData temp = new ND(i);
                temp.setLocation(new GL(i, i*3, 0));
                graph.addNode(temp);
            }
            for (int i = 0; i < 20; i++){
                Random rand = new Random();
                int r1 = rand.nextInt(1000);
                int r2 = 0;
                do{
                    r2 = rand.nextInt(1000);
                } while (r1 == r2);
                graph.connect(r1, r2, rand.nextDouble());
            }
            algo.init(graph);

            long start = System.currentTimeMillis();
            algo.save("temp.json");
            System.out.println((System.currentTimeMillis() - start)/1000.0);
            start = System.currentTimeMillis();
            algo.load("temp.json");
            System.out.println((System.currentTimeMillis() - start)/1000.0);
        }
    }

    public static void main(String[] args){
        runGUI(args[0]);
    }
}