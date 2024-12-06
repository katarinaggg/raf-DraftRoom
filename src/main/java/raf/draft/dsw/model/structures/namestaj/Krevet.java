package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Krevet extends RoomElement{

    private Color color = new Color(94, 70, 41);;
    private Stroke stroke  = new BasicStroke(2.0f);


    private static int elementCounter = 1;

    public Krevet(DraftNode parent) {
        super("Krevet " + elementCounter++, parent);
    }
    public Krevet(DraftNode parent, Point lokacija, Dimension size) {
        super("Krevet " + elementCounter++, parent, lokacija, size);
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }
}
