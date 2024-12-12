package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Sto extends RoomElement{

    private static int elementCounter = 1;
    //private Color color = new Color(153, 126, 107);
    private Stroke stroke = new BasicStroke(2.0f);

    public Sto(DraftNode parent) {
        super("Sto "+ elementCounter++, parent);
    }

    public Sto(DraftNode parent, Point lokacija, Dimension size) {
        super("Sto "+ elementCounter++, parent, lokacija, size,new Color(153, 126, 107));
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
        return new Sto(this.getParent(),point, this.getSize());
    }
}
