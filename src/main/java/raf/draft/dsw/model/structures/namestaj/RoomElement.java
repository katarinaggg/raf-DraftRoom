package raf.draft.dsw.model.structures.namestaj;

import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

public abstract class RoomElement extends DraftNode {
    private String lokacija; // mozda i nije String?
    private double sirina;
    private double duzina;
    private int rotacija; /// nismo sigurne za tip :/

    public RoomElement(String ime, DraftNode parent) {
        super(ime, parent);
    }

    public RoomElement(String ime, DraftNode parent, String lokacija, double sirina, double duzina, int rotacija) {
        super(ime, parent);
        this.lokacija = lokacija;
        this.sirina = sirina;
        this.duzina = duzina;
        this.rotacija = rotacija;
    }
}
