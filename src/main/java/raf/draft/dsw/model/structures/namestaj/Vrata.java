package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Vrata extends RoomElement {

    //private Color color = new Color(8, 5, 0);
    private Stroke stroke = new BasicStroke(3.0f);
    private static int elementCounter = 1;

    public Vrata(DraftNode parent) {
        super("Vrata "+ elementCounter++, parent);
    }

    public Vrata(DraftNode parent, Point lokacija, Dimension size) {
        super("Vrata "+ elementCounter++, parent, lokacija, size,new Color(8, 5, 0));
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
        Point point = new Point(this.getLokacija().x +this.getOffset(), this.getLokacija().y+this.getOffset());
        return new Vrata(this.getParent(),point, this.getSize());
    }
}
