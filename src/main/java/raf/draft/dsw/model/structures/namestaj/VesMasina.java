package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class VesMasina extends RoomElement{
    public VesMasina(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public VesMasina(String ime, DraftNode parent, Point lokacija, Dimension size) {
        super(ime, parent, lokacija, size);
    }

}
