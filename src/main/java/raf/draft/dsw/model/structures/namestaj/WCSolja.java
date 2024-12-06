package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class WCSolja extends RoomElement{

    private static int elementCounter = 1;

    public WCSolja(DraftNode parent) {
        super("WC Solja "+ elementCounter++, parent);
    }

    public WCSolja(DraftNode parent, Point lokacija, Dimension size) {
        super("WC Solja "+ elementCounter++, parent, lokacija, size);
    }
}
