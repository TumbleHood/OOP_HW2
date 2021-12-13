package api;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

public class Graph extends JPanel {
    private HashMap<Integer, GeoLocation> nodeList;
    private HashMap<Integer, HashMap<Integer, Line>> edgeList;
    public static HashMap<Circle, NodeData> clickList;
    public static HashMap<Line, EdgeData> clickEdgeList;
    public static List<Integer> selected = new ArrayList<>();
    public static List<Integer[]> selectedEdge = new ArrayList<>();
    public static double factorX, factorY;
    public static double minX, minY, maxX, maxY;
    private boolean isNew;

    public Graph(boolean isNew){
        this.isNew = isNew;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        nodeList = new HashMap<>();
        edgeList = new HashMap<>();
        clickList = new HashMap<>();
        clickEdgeList = new HashMap<>();
        Dimension size = this.getSize();
        Iterator<NodeData> nd = MainGUI.graph.nodeIter();
        if (isNew){ //check if we create a new graph or use an existing one.
            minX = 0;
            minY = 0;
            maxX = this.getWidth();
            maxY = this.getHeight();
        }
        else{ //if the graph is not new, calculate the border
            minX = Double.MAX_VALUE;
            maxX = Double.MIN_VALUE;
            minY = Double.MAX_VALUE;
            maxY = Double.MIN_VALUE;
            while (nd.hasNext()) {
                NodeData n = nd.next();
                GeoLocation l = n.getLocation();
                if (l.x() < minX)
                    minX = l.x();
                if (l.x() > maxX)
                    maxX = l.x();
                if (l.y() < minY)
                    minY = l.y();
                if (l.y() > maxY)
                    maxY = l.y();
            }
        }

        factorX = (size.width - 50) / (maxX - minX); //calculate the factor we need to multiply the X values of the given vertices
        factorY = (size.height - 50) / (maxY - minY); //the factor for the Y values

        nd = MainGUI.graph.nodeIter();
        while (nd.hasNext()) {
            NodeData n = nd.next();
            GeoLocation l = n.getLocation();
            GeoLocation tempL = new GL((int) ((l.x() - minX) * factorX) + 12, (int) ((l.y() - minY) * factorY) + 12, 0);
            //set the vertex to be relative to the window, with a margin of 12
            this.nodeList.put(n.getKey(), tempL);
            clickList.put(new Circle(tempL.x(), tempL.y(), 10), n); //add to the click box list
            g.fillOval((int) tempL.x(), (int) tempL.y(), 10, 10); //draw an oval - vertex
        }

        Iterator<EdgeData> ed = MainGUI.graph.edgeIter();
        while (ed.hasNext()){
            EdgeData e = ed.next();
            HashMap<Integer,Line> temp = new HashMap<>();
            //since the vertices are circles with a radius of 5, we add 5 to the values.
            Line line = new Line((int) this.nodeList.get(e.getSrc()).x() + 5, (int) this.nodeList.get(e.getSrc()).y() + 5, (int) this.nodeList.get(e.getDest()).x() + 5, (int) this.nodeList.get(e.getDest()).y() + 5);
            // Shortening a line by "p" pixels:
            // line = (Ax, Ay) , (Bx, By)
            // shortened line = (Ax + (p/dis)(Bx-Ax), Ay + (p/dis)(By-Ay)) , (By + (p/dis)(Ax-Bx), By + (p/dis)(Ay-By))
            double dis = Math.sqrt(Math.pow((line.getStartX() - line.getEndX()), 2) + Math.pow((line.getStartY() - line.getEndY()), 2));
            double Ax = line.getStartX(), Ay = line.getStartY(), Bx = line.getEndX(), By = line.getEndY();
            line = new Line(Ax + (10/dis)*(Bx-Ax), Ay + (10/dis)*(By-Ay), Bx + (10/dis)*(Ax-Bx), By + (10/dis)*(Ay-By));
            clickEdgeList.put(line, e);
            temp.put(e.getDest(), line);
            if (this.edgeList.containsKey(e.getSrc()))
                this.edgeList.get(e.getSrc()).put(e.getDest(), temp.get(e.getDest()));
            else
                this.edgeList.put(e.getSrc(), temp);

            // How to draw an Arrow: https://math.stackexchange.com/questions/1314006/drawing-an-arrow
            g.drawLine((int) line.getStartX(), (int) line.getStartY(), (int) line.getEndX(), (int) line.getEndY());
            g.drawLine((int) line.getEndX(), (int) line.getEndY(), (int) (line.getEndX() + (10/dis)*((line.getStartX()-line.getEndX())*Math.cos(70)+(line.getStartY()-line.getEndY())*Math.sin(70))), (int) (line.getEndY() + (10/dis)*((line.getStartY()-line.getEndY())*Math.cos(70)-(line.getStartX()-line.getEndX())*Math.sin(70))));
            g.drawLine((int) line.getEndX(), (int) line.getEndY(), (int) (line.getEndX() + (10/dis)*((line.getStartX()-line.getEndX())*Math.cos(70)-(line.getStartY()-line.getEndY())*Math.sin(70))), (int) (line.getEndY() + (10/dis)*((line.getStartY()-line.getEndY())*Math.cos(70)+(line.getStartX()-line.getEndX())*Math.sin(70))));
        }

        for (int s : selected){
            g.setColor(Color.GREEN); //draw a green oval
            g.fillOval((int) nodeList.get(s).x(), (int) nodeList.get(s).y(), 10, 10);
        }

        for (Integer[] s : selectedEdge){
            g.setColor(Color.GREEN); //draw a green arrow
            Line line = edgeList.get(s[0]).get(s[1]);
            double dis = Math.sqrt(Math.pow((line.getStartX() - line.getEndX()), 2) + Math.pow((line.getStartY() - line.getEndY()), 2));
            g.drawLine((int) line.getStartX(), (int) line.getStartY(), (int) line.getEndX(), (int) line.getEndY());
            g.drawLine((int) line.getEndX(), (int) line.getEndY(), (int) (line.getEndX() + (10/dis)*((line.getStartX()-line.getEndX())*Math.cos(70)+(line.getStartY()-line.getEndY())*Math.sin(70))), (int) (line.getEndY() + (10/dis)*((line.getStartY()-line.getEndY())*Math.cos(70)-(line.getStartX()-line.getEndX())*Math.sin(70))));
            g.drawLine((int) line.getEndX(), (int) line.getEndY(), (int) (line.getEndX() + (10/dis)*((line.getStartX()-line.getEndX())*Math.cos(70)-(line.getStartY()-line.getEndY())*Math.sin(70))), (int) (line.getEndY() + (10/dis)*((line.getStartY()-line.getEndY())*Math.cos(70)+(line.getStartX()-line.getEndX())*Math.sin(70))));
        }
    }
}
