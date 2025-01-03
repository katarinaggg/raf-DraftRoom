package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class Kada extends RoomElement{

    //private Color color = new Color(197, 206, 212);

    @JsonIgnore
    private Stroke stroke;

    private static int elementCounter = 1;

    public Kada(DraftNode parent) {
        super("Kada " + elementCounter++, parent);
        this.stroke = createStroke();
    }

    public Kada(DraftNode parent, Point lokacija, Dimension size) {
        super("Kada " + elementCounter++, parent, lokacija, size, new Color(197, 206, 212));
        this.stroke = createStroke();
    }

//    public Color getColor() {
//        return color;
//    }
//
//    public void setColor(Color color) {
//        this.color = color;
//    }

    public Stroke getStroke() {
        if (stroke == null) {
            stroke = createStroke(); // Rekonstruiši Stroke ako je null
        }
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public Prototype clone() {
        Point point = new Point(this.getLokacija().x +this.getOffset(), this.getLokacija().y+this.getOffset());
        return new Kada(this.getParent(),point, this.getSize());
    }

}