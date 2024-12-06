package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;

import java.awt.*;

public class VesMasina extends RoomElement{

    private static int elementCounter = 1;

    public VesMasina(DraftNode parent) {
        super("Ves Masina "+ elementCounter++, parent);
    }

    public VesMasina(DraftNode parent, Point lokacija, Dimension size) {
        super("Ves Masina "+ elementCounter++, parent, lokacija, size);
    }

}
