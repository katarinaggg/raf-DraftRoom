package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

public class Kada extends RoomElement{
    public Kada(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public Kada(String ime, DraftNode parent, double x, double y, double sirina, double duzina) {
        super(ime, parent, x, y, sirina, duzina);
    }
}
