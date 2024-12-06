package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Kada extends RoomElement {
    private Color color = new Color(13, 152, 184);
    ;
    private Stroke stroke = new BasicStroke(2.0f);

    private static int elementCounter = 1;

    public Kada(DraftNode parent) {
        super("Kada " + elementCounter++, parent);
    }

    public Kada(DraftNode parent, Point lokacija, Dimension size) {
        super("Kada " + elementCounter++, parent, lokacija, size);
    }
}