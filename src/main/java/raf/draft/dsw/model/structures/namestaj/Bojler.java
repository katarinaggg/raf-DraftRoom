package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Bojler extends RoomElement {

    private static int elementCounter = 1;
    //private Color color = new Color(213, 231, 240);

    // posto ne mozemo da serijalizujemo Stroke treba nam neki float za tu vrednost
    @JsonIgnore
    private Stroke stroke;

    public Bojler(DraftNode parent) {
        super("Bojler " + elementCounter++, parent);
        this.stroke = createStroke();
    }

    public Bojler(DraftNode parent, Point lokacija, Dimension size) {
        super("Bojler "+ elementCounter++, parent, lokacija, size, new Color(213, 231, 240));
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
        return new Bojler(this.getParent(),point, this.getSize());
    }
}
