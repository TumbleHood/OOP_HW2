package api;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateGUI extends JFrame {
    private JPanel panel1;
    public JTextField textField1;
    public JTextField textField2;
    public JTextField textField3;
    private JButton create;
    private boolean mode;

    public CreateGUI(boolean isVertex, ActionListener listener){
        super("Create");
        this.setLocationRelativeTo(null); //create window at the middle of the screen
        this.setMinimumSize(new Dimension(250, 250));
        this.setPreferredSize(this.getMinimumSize());
        this.setMinimumSize(this.getMinimumSize());
        this.mode = isVertex;
        this.setContentPane(this.panel1);
        if (this.mode)
            create.setText("Create Vertex");
        else
            create.setText("Create Edge");
        this.pack();

        create.addActionListener(listener);
    }
}
