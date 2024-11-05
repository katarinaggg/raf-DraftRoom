package raf.draft.dsw.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AboutUs extends JPanel {

    public AboutUs(){
        // Postavljanje layout-a
        setLayout(new GridLayout(2, 2, 10, 10));

        setPreferredSize(new Dimension(400, 400));

        // Učitavanje slika
        Icon image1 = loadIcon("/images/anja.png");
        Icon image2 = loadIcon("/images/katarina.png");

        // Kreiranje JLabel za slike i tekstove
        JLabel label1 = new JLabel("Anja Stanković RN 58/23", image1, JLabel.CENTER);
        JLabel label2 = new JLabel("Katarina Gordić RN 41/23", image2, JLabel.CENTER);

        // Dodavanje JLabel komponenti u panel
        add(label1);
        add(label2);
    }
    public Icon loadIcon(String path) {

        Icon icon = null;
        URL ImageURL = getClass().getResource(path);
        if(ImageURL != null)
        {
            Image img = new ImageIcon(ImageURL).getImage();
            Image newImg = img.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
            icon = new ImageIcon(newImg);
        }
        else
        {
            System.err.println("File " + path + " not found");
        }
        return icon;
    }
}
