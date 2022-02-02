package net.henrik.gui;

import net.henrik.utils.Content;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Buttons extends JPanel {

    public Buttons(Content content, MainView mainView) {
        super();
        JLabel jLabel_name = new JLabel("Name neuer Mitarbeiter (\"Vorname Nachname\"):");
        JTextField jTextField_name = new JTextField(20);
        JButton jButton_Update = new JButton("Update");
        JButton jButton_DeleteLastRacf = new JButton("Delete last created");
        JButton jButton_CreateRacf = new JButton("Create RACF");
        JButton jButton_Cheat = new JButton("ImportCSV");
        JPanel line1 = new JPanel();
        JPanel line2 = new JPanel();

        jTextField_name.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        jTextField_name.setText(content.add(jTextField_name.getText()));
                        jButton_DeleteLastRacf.setEnabled(true);
                    } catch (Exception err) {
                        ErrorMessage.infoBox("Failed to create new RACF:\n" + err.getMessage(), "Error");
                    }
                    mainView.update();
                }
                if (e.getKeyCode() == KeyEvent.VK_F1)
                    jButton_Cheat.setEnabled(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        jButton_CreateRacf.addActionListener(e -> {
            try {
                jTextField_name.setText(content.add(jTextField_name.getText()));
                jButton_DeleteLastRacf.setEnabled(true);
            } catch (Exception err) {
                ErrorMessage.infoBox("Failed to create new RACF:\n" + err.getMessage(), "Error");
            }
            mainView.update();
        });
        jButton_DeleteLastRacf.addActionListener(e -> {
            try {
                jTextField_name.setText(content.deleteLast());
            } catch (Exception err) {
                ErrorMessage.infoBox("Unable to delete last" + err.getMessage(), "Error");
            }
            jButton_DeleteLastRacf.setEnabled(false);
            mainView.update();
        });
        jButton_Update.addActionListener(e -> mainView.update());
        jButton_Cheat.addActionListener(e -> {
            try {
                content.importCSV();
            } catch (IOException ex) {
                ErrorMessage.infoBox("Couldn't import. File has to be in src/export.csv\n"+ex.getMessage(),"Error");
            }
        });
        jButton_DeleteLastRacf.setEnabled(false);
        jButton_Cheat.setEnabled(false);

        this.setLayout(new BorderLayout());

        line1.add(jLabel_name, BorderLayout.WEST);
        line1.add(jTextField_name, BorderLayout.CENTER);
        this.add(line1, BorderLayout.CENTER);

        line2.add(jButton_CreateRacf, BorderLayout.WEST);
        line2.add(jButton_DeleteLastRacf, BorderLayout.CENTER);
        line2.add(jButton_Update, BorderLayout.EAST);
        line2.add(jButton_Cheat, BorderLayout.PAGE_END);

        this.add(line2, BorderLayout.PAGE_END);
    }

    public Buttons update() {
        return this;
    }
}
