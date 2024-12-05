package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class WCSolja extends RoomElement{
    public WCSolja(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public WCSolja(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
    }
}
