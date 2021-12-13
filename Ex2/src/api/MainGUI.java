package api;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class MainGUI extends JFrame implements ActionListener {
    private JButton loadGraphButton;
    private JButton createNewGraphButton;
    private JPanel mainMenu;
    private JButton openGiven;
    public static DirectedWeightedGraph graph;
    public static DirectedWeightedGraphAlgorithms graphAlgo;
    private JPanel graphPanel;
    private UI ui;
    private String given = "";
    private final FileFilter jsonFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().endsWith(".json") || f.isDirectory();
        }

        @Override
        public String getDescription() {
            return null;
        }
    };

    public MainGUI(String json_file){
        // this is the main window;
        super("Graph");
        this.setContentPane(mainMenu);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.given = json_file;
        this.loadGraphButton.addActionListener(this::loadGraph);
        this.createNewGraphButton.addActionListener(this::createGraph);
        this.openGiven.addActionListener(this::openGiven);

        this.setMinimumSize(new Dimension(600, 600));
        this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Back to Menu":
                this.back();
                break;
            case "Save Graph to File":
                this.save();
                break;
            case "Delete Selected Vertex":
                this.delete();
                break;
            case "Delete Selected Edge":
                this.deleteEdge();
                break;
            case "Create New Vertex":
                this.ui.createMode = "V"; //the "createMode" is used to make actions with the mouse:
                //create node and edge, and calculate the shortest path.
                this.ui.instructor.setText("Click anywhere on the filed");
                break;
            case "Create New Edge":
                this.ui.createMode = "E";
                Graph.selected = new ArrayList<>(); //clear selected nodes
                this.ui.instructor.setText("Click the Source Node");
                break;
            case "Create Vertex":
                this.create();
                break;
            case "Create Edge":
                this.createEdge();
                break;
            case "Calculate Shortest Path":
                this.ui.createMode = "P";
                Graph.selected = new ArrayList<>();
                this.ui.instructor.setText("Click the Source Node");
                break;
            case "Calculate Center":
                Graph.selected = new ArrayList<>();
                this.calcCenter();
                break;
        }
    }
    public void openGiven(ActionEvent e){
        if (this.given.equals("")){
            this.createGraph(e);
        }
        else
            loadAnyGraph(this.given);
    }

    public void loadAnyGraph(String file){
        graphAlgo = new DWGA();
        graphAlgo.load(file);
        graph = graphAlgo.getGraph();
        this. ui = new UI(this, false);
        this.ui.panel.setPreferredSize(this.getContentPane().getSize()); //open the UI panel
        this.setContentPane(ui.panel);
        this.pack();
    }

    public void loadGraph(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(this.jsonFilter); //filter all files but .json and folders
        fileChooser.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toString()));
        // set current directory as this one.
        int response = fileChooser.showOpenDialog(null);

        if(response == JFileChooser.APPROVE_OPTION){
            this.loadAnyGraph(fileChooser.getSelectedFile().getAbsolutePath());
        }
        else
            System.out.println("An Error has Occurred/The window was closed");
    }
    public void createGraph(ActionEvent e){
        graph = new DWG();
        graphAlgo = new DWGA();
        graphAlgo.init(graph);
        this.ui = new UI(this, true);
        this.ui.panel.setPreferredSize(this.getContentPane().getSize()); //open an empty UI panel
        this.setContentPane(ui.panel);
        this.pack();
    }
    public void back(){
        this.mainMenu.setPreferredSize(this.getContentPane().getSize()); //return to the main panel
        this.setContentPane(this.mainMenu);
        this.pack();
    }
    public void save(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(Paths.get("").toAbsolutePath().toString()));
        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION){
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();
            // check if file name ends in ".json", if not - add ".json" to the end.
            if (!fileName.endsWith(".json"))
                fileName += ".json";
            graphAlgo.save(fileName);
        }
        else
            System.out.println("An Error has Occurred");
    }
    public void delete(){
        //delete selected node
        graph.removeNode(Graph.selected.get(0));
        Graph.selected = new ArrayList<>();
        this.ui.panel.updateUI();
    }
    public void deleteEdge(){
        //delete selected edge
        graph.removeEdge(Graph.selectedEdge.get(0)[0], Graph.selectedEdge.get(0)[1]);
        Graph.selectedEdge = new ArrayList<>();
        this.ui.panel.updateUI();
    }
    public void create(){
        //create a new vertex, use an unused key and the clicked location.
        int k = 0;
        List<Integer> taken = new ArrayList<>();
        Iterator<NodeData> nd = graph.nodeIter();
        while (nd.hasNext()){
            taken.add(nd.next().getKey());
        }
        while (taken.contains(k)){ //get an unused key
            k++;
        }
        double x = (this.ui.clickedLocation[0] - 12 + Graph.factorX * Graph.minX)/Graph.factorX; //the opposite formula from "Graph.java"
        double y = (this.ui.clickedLocation[1] - 12 + Graph.factorY * Graph.minY)/Graph.factorY;
        GeoLocation l = new GL(Math.abs(x), Math.abs(y), 0);
        try{
            //get the text fields in CreateGUI.
            double w = Double.parseDouble(this.ui.cGUI.textField1.getText().replace(" ",""));
            String i = this.ui.cGUI.textField2.getText();
            int t = Integer.parseInt(this.ui.cGUI.textField3.getText().replace(" ",""));
            graph.addNode(new ND(k, l, w, i, t));
            this.ui.cGUI.dispose(); //close the Create window
            this.ui.panel.updateUI();
            this.ui.clickedLocation = new int[2];
        }
        catch (NumberFormatException err){
            err.printStackTrace();
        }
    }
    public void createEdge(){
        try{
            //create a new edge, use the selected vertices
            double w = Double.parseDouble(this.ui.cGUI.textField1.getText().replace(" ",""));
            String i = this.ui.cGUI.textField2.getText();
            int t = Integer.parseInt(this.ui.cGUI.textField3.getText().replace(" ",""));
            graph.connect(this.ui.clickedNodes[0], this.ui.clickedNodes[1], w);
            graph.getEdge(this.ui.clickedNodes[0], this.ui.clickedNodes[1]).setInfo(i);
            graph.getEdge(this.ui.clickedNodes[0], this.ui.clickedNodes[1]).setTag(t);
            this.ui.cGUI.dispose(); //close the window
            this.ui.panel.updateUI();
            this.ui.clickedNodes = new int[2];
            Graph.selected = new ArrayList<>();
        }
        catch (NumberFormatException err){
            err.printStackTrace();
        }
    }
    public static void calcShortPath(){
        //calculate the shortest path from the first selected vertex to the second.
        //select the vertices and edges in the way
        graphAlgo.init(graph);
        List<NodeData> path = graphAlgo.shortestPath(Graph.selected.get(0), Graph.selected.get(1));
        Graph.selected = new ArrayList<>();
        for (int i = 1; i < path.size(); i++){
            Graph.selected.add(path.get(i-1).getKey());
            Graph.selectedEdge.add(new Integer[]{path.get(i-1).getKey(), path.get(i).getKey()});
        }
        Graph.selected.add(path.get(path.size()-1).getKey());
    }
    public void calcCenter(){
        //calculate center and select it
        graphAlgo.init(graph);
        NodeData center = graphAlgo.center();
        if (center == null)
            this.ui.instructor.setText("The graph is not connected");
        else
            Graph.selected.add(center.getKey());
        this.ui.panel.updateUI();
    }
}
