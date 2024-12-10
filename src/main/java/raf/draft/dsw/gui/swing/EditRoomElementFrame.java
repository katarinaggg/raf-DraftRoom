package raf.draft.dsw.gui.swing;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.Message;
import raf.draft.dsw.model.notification.MessageType;
import raf.draft.dsw.model.notification.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class EditRoomElementFrame extends JDialog {

    Dimension dimension;

    Color color;

    private JTextField widthField;
    private JTextField lengthField;
    private JTextField redField;
    private JTextField greenField;
    private JTextField blueField;

    public EditRoomElementFrame() {
        setTitle("Edit Room Element");
        setSize(300, 300);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label i polje za širinu
        JLabel widthLabel = new JLabel("Width:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(widthLabel, gbc);

        JTextField widthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(widthField, gbc);

        // Label i polje za dužinu
        JLabel lengthLabel = new JLabel("Length:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lengthLabel, gbc);

        JTextField lengthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(lengthField, gbc);

        // Label i polje za unos R vrednosti
        JLabel redLabel = new JLabel("Red:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(redLabel, gbc);

        JTextField redField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(redField, gbc);

        // Label i polje za unos G vrednosti
        JLabel greenLabel = new JLabel("Green:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(greenLabel, gbc);

        JTextField greenField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(greenField, gbc);

        // Label i polje za unos B vrednosti
        JLabel blueLabel = new JLabel("Blue:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(blueLabel, gbc);

        JTextField blueField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(blueField, gbc);

        // Dugme Save
        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        saveButton.addActionListener(e -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int length = Integer.parseInt(lengthField.getText());

                if (width <= 0 || length <= 0) {
                    Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli odgovarajuce podatke za dimenziju!", MessageType.ERROR, LocalDateTime.now());
                    ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                    //dispose();
                    return;
                }

                dimension = new Dimension(width, length);

                int red = Integer.parseInt(redField.getText());
                int green = Integer.parseInt(greenField.getText());
                int blue = Integer.parseInt(blueField.getText());

                if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
                    Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli odgovarajuce podatke za boju!", MessageType.ERROR, LocalDateTime.now());
                    ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
                    //dispose();
                    return;
                }

                color = new Color(red, green, blue);

                dispose();

            } catch (NumberFormatException ex) {
                Message message = ApplicationFramework.getInstance().getMessageGenerator().generateMessage("Niste uneli odgovarajuce podatke!", MessageType.ERROR, LocalDateTime.now());
                ApplicationFramework.getInstance().getMessageGenerator().notifySubscribers(new Notification(message));
            }
        });

        add(saveButton, gbc);
        setLocationRelativeTo(null);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Color getColor() {
        return color;
    }
}
