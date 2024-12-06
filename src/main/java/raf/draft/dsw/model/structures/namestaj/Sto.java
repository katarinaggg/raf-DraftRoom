package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Sto extends RoomElement{

    private static int elementCounter = 1;

    public Sto(DraftNode parent) {
        super("Sto "+ elementCounter++, parent);
    }

    public Sto(DraftNode parent, Point lokacija, Dimension size) {
        super("Sto "+ elementCounter++, parent, lokacija, size);
    }
}
