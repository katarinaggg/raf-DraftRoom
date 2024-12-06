package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Krevet extends RoomElement{

    private static int elementCounter = 1;

    public Krevet(DraftNode parent) {
        super("Krevet "+ elementCounter++, parent);
    }

    public Krevet(DraftNode parent, Point lokacija, Dimension size) {
        super("Krevet "+ elementCounter++, parent, lokacija, size);
    }

}
