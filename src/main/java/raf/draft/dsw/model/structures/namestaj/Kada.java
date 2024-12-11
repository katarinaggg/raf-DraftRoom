package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Kada extends RoomElement implements Prototype{

    //private Color color = new Color(197, 206, 212);

    private Stroke stroke = new BasicStroke(2.0f);

    private static int elementCounter = 1;

    public Kada(DraftNode parent) {
        super("Kada " + elementCounter++, parent);
    }

    public Kada(DraftNode parent, Point lokacija, Dimension size) {
        super("Kada " + elementCounter++, parent, lokacija, size, new Color(197, 206, 212));
    }

//    public Color getColor() {
//        return color;
//    }
//
//    public void setColor(Color color) {
//        this.color = color;
//    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public Prototype clone() {
        return null;
    }
}