package net.henrik.gui;

import net.henrik.utils.Content;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RACFList extends JPanel {
    JTable table;
    DefaultTableModel defaultTableModel;

    public RACFList(Content content) {
        super();
        table = new JTable();
        JTextField tf = new JTextField();
        tf.setEditable(false);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(tf));
        update(content);
        this.add(new JScrollPane(table));
    }

    public RACFList update(Content content) {
        defaultTableModel = new DefaultTableModel(content.getContent(), content.getTitle());
        table.setModel(defaultTableModel);
        return this;
    }
}
