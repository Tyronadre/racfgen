package net.henrik.gui;

import net.henrik.utils.Content;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainView extends JComponent {
    RACFList racfList;
    Buttons buttons;
    Content content;
    Gui gui;

    public MainView(Gui gui){
        super();
        super.setLayout(new BorderLayout());
        content = new Content();
        racfList = new RACFList(content);
        buttons = new Buttons(content,this);
        this.gui = gui;

        this.add(buttons, BorderLayout.PAGE_END);
        this.add(racfList, BorderLayout.CENTER);

        buttons.setVisible(true);
        racfList.setVisible(true);
    }

    public void update(){
        this.racfList = racfList.update(content);
        this.buttons = buttons.update();
        repaint();
        gui.update(this);
    }

}
