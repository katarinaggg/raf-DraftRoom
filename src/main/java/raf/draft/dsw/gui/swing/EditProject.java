package raf.draft.dsw.gui.swing;

import raf.draft.dsw.controller.actions.SaveButtonAction;

import javax.swing.*;
import java.awt.*;

public class EditProject extends JDialog {

    public EditProject() {

        setTitle("Edit Project");
        //setSize(300, 200);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JTextField field1 = new JTextField(20);
        JTextField field2 = new JTextField(20);
        JTextField field3 = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Author:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field1, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field2, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Path:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(field3, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        saveButton.addActionListener(new SaveButtonAction(field1, field2, field3,this));
        add(saveButton, gbc);

        pack();
        setLocationRelativeTo(null);
    }

}
