package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Vrata extends RoomElement{

    private static int elementCounter = 1;

    public Vrata(DraftNode parent) {
        super("Vrata "+ elementCounter++, parent);
    }

    public Vrata(DraftNode parent, Point lokacija, Dimension size) {
        super("Vrata "+ elementCounter++, parent, lokacija, size);
    }
}
