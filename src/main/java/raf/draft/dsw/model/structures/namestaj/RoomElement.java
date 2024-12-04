package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

public abstract class RoomElement extends DraftNode {
    private double x; // X koordinata elementa unutar sobe
    private double y; // Y koordinata elementa unutar sobe
    private double sirina;
    private double duzina;
    private int rotacija; ///  Rotacija elementa (0 do 3, gde 0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°)
    private static int elementCounter = 1; // Brojač elemenata za automatske nazive, npr. krevet1, krevet2, solja1, solja2...

    public RoomElement(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public RoomElement(String ime, DraftNode parent, double x, double y, double sirina, double duzina) {
        super(ime + " " + (elementCounter++), parent);
        this.x = x;
        this.y = y;
        this.sirina = sirina;
        this.duzina = duzina;
        this.rotacija = 0;
    }

    //  Apstraktna metoda za prikazivanje u JTree
    //    public abstract void render();

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSirina() {
        return sirina;
    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public double getDuzina() {
        return duzina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
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
