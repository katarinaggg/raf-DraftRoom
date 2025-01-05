package raf.draft.dsw.model.structures.namestaj;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

@NoArgsConstructor
public class VesMasina extends RoomElement {

    private static int elementCounter = 1;

    //private Color color = new Color(204, 161, 227);
    @JsonIgnore
    private Stroke stroke;

    public VesMasina(DraftNode parent) {
        super("Ves Masina "+ elementCounter++, parent);
        this.stroke = createStroke();
    }

    public VesMasina(DraftNode parent, Point lokacija, Dimension size) {
        super("Ves Masina "+ elementCounter++, parent, lokacija, size, new Color(204, 161, 227));
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
        return new VesMasina(this.getParent(),point, this.getSize());
    }
}
