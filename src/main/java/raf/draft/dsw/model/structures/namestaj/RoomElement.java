package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import raf.draft.dsw.gui.swing.RoomView;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.serializer.ColorDeserializer;
import raf.draft.dsw.serializer.ColorSerializer;

import java.awt.*;

public abstract class RoomElement extends DraftNode implements Prototype{
     private Point lokacija;

    // umesto ovoga moze klasa Dimension
     private Dimension size;
//    private double sirina;
//    private double duzina;

    // svaki element ima svoj Color
    @JsonSerialize(using = ColorSerializer.class)
    @JsonDeserialize(using = ColorDeserializer.class)
    private Color color;

    private float strokeWidth = 2.0f;

    @JsonIgnore
    // kada kopiramo da se izmesti
    private int offset = 20;

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

    public int getOffset() {
        return offset;
    }

    @Override
    public Prototype clone() {
        return null;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public Stroke createStroke() {
        return new BasicStroke(strokeWidth);
    }
}
