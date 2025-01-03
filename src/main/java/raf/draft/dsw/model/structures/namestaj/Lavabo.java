package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Lavabo extends RoomElement{

    private static int elementCounter = 1;
    //private Color color = new Color(218, 237, 228);
    @JsonIgnore
    private Stroke stroke;

    public Lavabo(DraftNode parent) {
        super("Lavabo " + elementCounter++, parent);
        this.stroke = createStroke();
    }

    public Lavabo(DraftNode parent, Point lokacija, Dimension size) {
        super("Lavabo "+ elementCounter++, parent, lokacija, size,new Color(218, 237, 228));
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
        return new Lavabo(this.getParent(),point, this.getSize());
    }
}
