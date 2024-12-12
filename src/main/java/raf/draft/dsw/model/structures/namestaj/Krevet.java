package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Krevet extends RoomElement{

    //private Color color = new Color(131, 139, 156);
    private Stroke stroke  = new BasicStroke(2.0f);


    private static int elementCounter = 1;

    public Krevet(DraftNode parent) {
        super("Krevet " + elementCounter++, parent);
    }
    public Krevet(DraftNode parent, Point lokacija, Dimension size) {
        super("Krevet " + elementCounter++, parent, lokacija, size,new Color(131, 139, 156));
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
        return new Krevet(this.getParent(),point, this.getSize());
    }
}
