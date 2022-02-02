package net.henrik.gui;

import net.henrik.utils.Content;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Buttons extends JPanel {

    public Buttons(Content content, MainView mainView){
        super();
        JLabel jLabel_name = new JLabel("Name neuer MB:");
        JTextField jTextField_name = new JTextField(20);
        this.setLayout(new BorderLayout());
        JPanel line1 = new JPanel();
        line1.add(jLabel_name,BorderLayout.WEST);
        line1.add(jTextField_name,BorderLayout.EAST);
        this.add(line1,BorderLayout.CENTER);

        JButton jButton_DeleteLastRacf = new JButton("Delete last created");
        JButton jButton_CreateRacf= new JButton("Create RACF");
        JButton jButton_DeleteRacf = new JButton("Delete RACF");
        jButton_CreateRacf.addActionListener(e -> {
            content.add(jTextField_name.getText());
            jButton_DeleteLastRacf.setEnabled(true);
            mainView.update();
        });
        jButton_DeleteLastRacf.setEnabled(false);
        jButton_DeleteLastRacf.addActionListener(e -> {
            content.deleteLast();
            jButton_DeleteLastRacf.setEnabled(false);
            mainView.update();
        });
        jButton_DeleteRacf.addActionListener(e -> {
            content.delete(jTextField_name.getText());
            mainView.update();
        });


        JPanel line2 = new JPanel();
        line2.add(jButton_CreateRacf,BorderLayout.WEST);
        line2.add(jButton_DeleteRacf,BorderLayout.CENTER);
        line2.add(jButton_DeleteLastRacf,BorderLayout.EAST);
        this.add(line2,BorderLayout.PAGE_END);
    }

    public Buttons update(){
        return this;
    }
}
