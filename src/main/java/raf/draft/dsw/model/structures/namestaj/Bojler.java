package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Bojler extends RoomElement{
    private Color color = new Color(139, 148, 199);;
    private Stroke stroke  = new BasicStroke(2.0f);

    public Bojler(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public Bojler(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
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
