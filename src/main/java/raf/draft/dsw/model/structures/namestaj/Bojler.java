package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Bojler extends RoomElement implements Prototype{

    private static int elementCounter = 1;
    //private Color color = new Color(213, 231, 240);
    private Stroke stroke  = new BasicStroke(2.0f);

    public Bojler(DraftNode parent) {
        super("Bojler " + elementCounter++, parent);
    }

    public Bojler(DraftNode parent, Point lokacija, Dimension size) {
        super("Bojler "+ elementCounter++, parent, lokacija, size, new Color(213, 231, 240));
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
