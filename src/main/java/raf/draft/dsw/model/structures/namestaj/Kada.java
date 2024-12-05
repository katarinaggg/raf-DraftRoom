package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Kada extends RoomElement{
    public Kada(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public Kada(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
    }
}
