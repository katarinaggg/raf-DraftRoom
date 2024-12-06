package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Lavabo extends RoomElement{

    private static int elementCounter = 1;
    private Color color = new Color(35, 170, 232);;
    private Stroke stroke  = new BasicStroke(2.0f);

    public Lavabo(DraftNode parent) {
        super("Lavabo "+ elementCounter++, parent);
        }

    public Lavabo(DraftNode parent, Point lokacija, Dimension size) {
        super("Lavabo "+ elementCounter++, parent, lokacija, size);
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
