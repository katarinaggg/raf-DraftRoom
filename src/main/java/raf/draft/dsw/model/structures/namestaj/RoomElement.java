package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

import java.awt.*;

public abstract class RoomElement extends DraftNode {
     private Point lokacija;

    // umesto ovoga moze klasa Dimension
     private Dimension size;
//    private double sirina;
//    private double duzina;

    // svaki element ima svoj Color
    private Color color;


    private int rotacija; ///  Rotacija elementa (0 do 3, gde 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)


    public RoomElement(String ime, DraftNode parent) {
        super(ime, parent);
    }


    public RoomElement(String ime, DraftNode parent, Point lokacija, Dimension size, Color color) {
        super(ime, parent);
        this.lokacija = lokacija;
        this.size = size;
        this.rotacija = 0;
        this.color = color;
    }

    //  Apstraktna metoda za prikazivanje u JTree
    //    public abstract void render();


    public Point getLokacija() {
        return lokacija;
    }

    public void setLokacija(Point lokacija) {
        this.lokacija = lokacija;
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension size) {
        this.size = size;
    }

    public int getRotacija() {
        return rotacija;
    }

//    public void setRotateRatio(int rotateRatio) {
//        if (rotateRatio >= 0 && rotateRatio <= 3) {
//            this.rotacija = rotacija;
//        } else {
//            throw new IllegalArgumentException("Rotacija mora biti između 0 i 3.");
//        }
//    }

    public void setRotateRatio(int rotateRatio) {
        this.rotacija = (rotateRatio % 4 + 4) % 4; // Osigurava opseg 0-3
    }

    public Dimension getScaledDimensions(double scalingFactor) {
        // Skaliranje širine i visine dimenzija elementa
        int scaledWidth = (int) (size.getWidth() * scalingFactor);
        int scaledHeight = (int) (size.getHeight() * scalingFactor);
        return new Dimension(scaledWidth, scaledHeight);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}
