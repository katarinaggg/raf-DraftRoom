package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class VesMasina extends RoomElement implements Prototype{

    private static int elementCounter = 1;

    //private Color color = new Color(204, 161, 227);
    private Stroke stroke = new BasicStroke(2.0f);

    public VesMasina(DraftNode parent) {
        super("Ves Masina "+ elementCounter++, parent);
    }

    public VesMasina(DraftNode parent, Point lokacija, Dimension size) {
        super("Ves Masina "+ elementCounter++, parent, lokacija, size, new Color(204, 161, 227));
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
