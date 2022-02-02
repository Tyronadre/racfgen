package net.henrik.gui;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;

public class Gui extends JFrame {

    public Gui() {
        super("RACF-GEN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 1000));

        //size//
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        double scale = dimension.width > 2000 ? 0.3 : 0.5;
        dimension.width *= scale;
        dimension.height *= scale;
        setPreferredSize(dimension);
        JComponent activeView = new MainView(this);

        setContentPane(activeView);
        pack();
        setVisible(true);
        requestFocus();
    }

    public void update(JComponent view){
        setContentPane(view);
        revalidate();
        requestFocus();
    }
}
