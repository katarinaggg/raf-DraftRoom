package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Ormar extends RoomElement implements Prototype{

    private static int elementCounter = 1;

    //private Color color = new Color(74, 45, 24);
    private Stroke stroke = new BasicStroke(2.0f);

    public Ormar(DraftNode parent) {
        super("Ormar "+ elementCounter++, parent);
    }

    public Ormar(DraftNode parent, Point lokacija, Dimension size) {
        super("Ormar "+ elementCounter++, parent, lokacija, size,new Color(74, 45, 24));
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
