package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Lavabo extends RoomElement{

    private static int elementCounter = 1;

    public Lavabo(DraftNode parent) {
        super("Lavabo "+ elementCounter++, parent);
    }

    public Lavabo(DraftNode parent, Point lokacija, Dimension size) {
        super("Lavabo "+ elementCounter++, parent, lokacija, size);
    }
}
