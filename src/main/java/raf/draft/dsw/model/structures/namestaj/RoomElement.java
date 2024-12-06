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


    private int rotacija; ///  Rotacija elementa (0 do 3, gde 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)


    public RoomElement(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public RoomElement(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent);
        this.lokacija = lokacija;
        this.size = size;
        this.rotacija = 0;
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

    public void setRotateRatio(int rotateRatio) {
        if (rotateRatio >= 0 && rotateRatio <= 3) {
            this.rotacija = rotacija;
        } else {
            throw new IllegalArgumentException("Rotacija mora biti između 0 i 3.");
        }
    }
}
