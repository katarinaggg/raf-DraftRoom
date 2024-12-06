package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Ormar extends RoomElement{

    private static int elementCounter = 1;

    public Ormar(DraftNode parent) {
        super("Ormar "+ elementCounter++, parent);
    }

    public Ormar(DraftNode parent, Point lokacija, Dimension size) {
        super("Ormar "+ elementCounter++, parent, lokacija, size);
    }
}
