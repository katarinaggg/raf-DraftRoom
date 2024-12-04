package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

public class VesMasina extends RoomElement{
    public VesMasina(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public VesMasina(String ime, DraftNode parent, double x, double y, double sirina, double duzina) {
        super(ime, parent, x, y, sirina, duzina);
    }
}
