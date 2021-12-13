package api;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class UI extends JFrame{
    public JPanel panel;
    private JButton back;
    private JButton save;
    private JButton createV;
    private JButton createE;
    private JPanel graphPanel;
    private JPanel infoPanel;
    public JLabel key;
    public JLabel locationX;
    public JLabel locationY;
    public JLabel locationZ;
    public JLabel weight;
    public JLabel info;
    public JLabel tag;
    private JButton deleteV;
    private JButton deleteE;
    private JButton shortestPath;
    public JLabel instructor;
    private JButton center;
    public String createMode = "";
    public CreateGUI cGUI;
    public int[] clickedLocation;
    public int[] clickedNodes;
    private boolean isNew;

    public UI(ActionListener listener, boolean isNew){
        this.isNew = isNew;
        // this is where we take mouse actions
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Circle c : Graph.clickList.keySet()){ //check if we clicked a vertex
                    if (c.contains(new Point2D(e.getX(), e.getY()))){ //if we clicked inside a vertex click box.
                        Graph.selectedEdge = new ArrayList<>(); //select the node and unselect the rest
                        if (!createMode.contains("E") && !createMode.contains("P"))//unless we need to select a pair of nodes
                            Graph.selected = new ArrayList<>();
                        Graph.selected.add(Graph.clickList.get(c).getKey());
                        this.updateInfo(Graph.clickList.get(c));
                    }
                }
                for (Line l : Graph.clickEdgeList.keySet()){ //check if we clicked (near) an edge
                    double distance = Line2D.ptSegDist(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), e.getX(), e.getY());
                    if (distance < 7){ //check if we clicked 7 pixels near an edge, I found this amount to be the best
                        Graph.selected = new ArrayList<>(); //select the edge and unselect the rest
                        Graph.selectedEdge = new ArrayList<>();
                        Graph.selectedEdge.add(new Integer[]{Graph.clickEdgeList.get(l).getSrc(), Graph.clickEdgeList.get(l).getDest()});
                        this.updateEdgeInfo(Graph.clickEdgeList.get(l));
                    }
                }
                if (createMode.equals("V")){ //if we create a vertex
                    clickedLocation = new int[]{e.getX(), e.getY()};
                    cGUI = new CreateGUI(true, listener); //open a new Create window, with the option isVertex set to true
                    cGUI.setVisible(true);
                    instructor.setText("");
                    createMode = "";
                }
                if (createMode.equals("E")){ //if we connect an edge between 2 vertices
                    clickedNodes = new int[2];
                    if (Graph.selected.size() > 0){ //click the first vertex
                        clickedNodes[0] = Graph.selected.get(0);
                        createMode = "E2"; //set the mode to check for a 2nd vertex
                        instructor.setText("Click the Destination Node");
                    }
                }
                if (createMode.equals("E2")){ //if we check for the second vertex to connect
                    if (Graph.selected.size() > 1){
                        clickedNodes[1] = Graph.selected.get(1);
                        createMode = "";
                        instructor.setText("");
                        cGUI = new CreateGUI(false, listener); //open a new Create window, with the option "isVertex" set to false.
                        cGUI.setVisible(true);
                    }
                }
                if (createMode.equals("P")){ //if we calculate the shortest path between 2 vertices
                    clickedNodes = new int[2];
                    if (Graph.selected.size() > 0){ //click the first vertex
                        clickedNodes[0] = Graph.selected.get(0);
                        createMode = "P2";
                        instructor.setText("Click the Destination Node");
                    }
                }
                if (createMode.equals("P2")){ //if we need to click the second vertex to calculate the shortest path
                    if (Graph.selected.size() > 1){
                        clickedNodes[1] = Graph.selected.get(1);
                        createMode = "";
                        instructor.setText("");
                        MainGUI.calcShortPath();//call the function to calculate it
                    }
                }
            }

            private void updateInfo(NodeData n){ //these function update the "info" section, where you can see info about the selected vertex/edge
                key.setText("Key: " + n.getKey());
                double[] l = new double[]{n.getLocation().x(), n.getLocation().y(), n.getLocation().z()};
                locationX.setText("X Location: " + l[0]);
                locationY.setText("Y Location: " + l[1]);
                locationZ.setText("Z Location: " + l[2]);
                weight.setText("Weight: " + n.getWeight());
                info.setText("Info: " + n.getInfo());
                tag.setText("Tag: " + n.getTag());
                panel.updateUI();
            }

            private void updateEdgeInfo(EdgeData e){
                key.setText("Source: " + e.getSrc());
                locationX.setText("Destination: " + e.getDest());
                locationY.setText("Weight: " + e.getWeight());
                locationZ.setText("Info: " + e.getInfo());
                weight.setText("Tag: " + e.getTag());
                info.setText("");
                tag.setText("");
                panel.updateUI();
            }
        });
        this.back.addActionListener(listener);
        this.save.addActionListener(listener);
        this.deleteV.addActionListener(listener);
        this.deleteE.addActionListener(listener);
        this.createV.addActionListener(listener);
        this.createE.addActionListener(listener);
        this.shortestPath.addActionListener(listener);
        this.center.addActionListener(listener);
    }

    private void createUIComponents() {
        this.graphPanel = new Graph(this.isNew);
    }
}
