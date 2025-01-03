package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Vrata extends RoomElement {

    //private Color color = new Color(8, 5, 0);

    @JsonIgnore
    private Stroke stroke;
    private static int elementCounter = 1;

    public Vrata(DraftNode parent) {
        super("Vrata "+ elementCounter++, parent);
        this.stroke = createStroke();
    }

    public Vrata(DraftNode parent, Point lokacija, Dimension size) {
        super("Vrata "+ elementCounter++, parent, lokacija, size,new Color(8, 5, 0));
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
        return new Vrata(this.getParent(),point, this.getSize());
    }
}
