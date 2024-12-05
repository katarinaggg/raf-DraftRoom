package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class Ormar extends RoomElement{
    public Ormar(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public Ormar(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
    }
}
