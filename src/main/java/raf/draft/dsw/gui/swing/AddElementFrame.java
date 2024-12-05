package raf.draft.dsw.gui.swing;

import raf.draft.dsw.core.ApplicationFramework;
import raf.draft.dsw.model.notification.MessageType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddElementFrame extends JDialog {

    private String selectedElement;

    public AddElementFrame(Frame parent) {
        super(parent, "Odaberite element koji želite da dodate:", true); // Postavlja modalnost
        this.selectedElement = createDialog();
    }

    public String getSelectedElement() {
        return selectedElement;
    }

    private String createDialog() {
        String[] elements = {"Bojler", "Kada", "Krevet", "Lavabo", "Ormar", "Sto", "Ves masina", "Vrata", "WC solja"};

        Icon[] icons = {
                loadScaledIcon("namestaj/boiler.png", 50, 50),
                loadScaledIcon("namestaj/bath.png", 50, 50),
                loadScaledIcon("namestaj/double-bed.png", 50, 50),
                loadScaledIcon("namestaj/sink.png", 50, 50),
                loadScaledIcon("namestaj/closet.png", 50, 50),
                loadScaledIcon("namestaj/desk.png", 50, 50),
                loadScaledIcon("namestaj/laundry-machine.png", 50, 50),
                loadScaledIcon("namestaj/door.png", 50, 50),
                loadScaledIcon("namestaj/toilet.png", 50, 50)
        };

        // Kreiranje panela za dugmiće
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        ArrayList<JButton> buttons = new ArrayList<JButton>();

        for (int i = 0; i < elements.length; i++) {
            JButton button = new JButton(elements[i], icons[i]);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setPreferredSize(new Dimension(100, 120));

            // Dodaj akciju dugmetu
            int index = i;
            button.addActionListener(e -> {
                selectedElement = elements[index];
                dispose(); // Zatvara dijalog
            });

            gridPanel.add(button);
            buttons.add(button);
        }

        // Postavljanje na JDialog
        setLayout(new BorderLayout());
        add(gridPanel, BorderLayout.CENTER);

        // Podešavanje prozora
        setSize(400, 400);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setVisible(true);

        return selectedElement;
    }

    // Metoda za skaliranje ikonica
    private Icon loadScaledIcon(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + path));
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Ikonica nije učitana sa putanje: " + path);
            return UIManager.getIcon("OptionPane.errorIcon");
        }
    }
}

