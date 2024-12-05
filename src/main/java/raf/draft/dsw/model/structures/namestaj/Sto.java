package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Sto extends RoomElement{
    public Sto(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public Sto(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
    }
}
