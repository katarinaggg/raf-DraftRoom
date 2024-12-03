package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Room extends DraftNodeComposite {
    double sirina;
    double duzina;
    private ArrayList<Object> namestaj = new ArrayList<>();

    public Room(String ime, DraftNode parent, double sirina, double duzina) {
        super(ime, parent);
        this.sirina = sirina;
        this.duzina = duzina;
    }

    public Room(String ime, DraftNode parent) {
        super(ime, parent);
    }


    // Konverzija centimetara u piksele
    public int convertCmToPx(double cm, double scalingFactor) {
        return (int) (cm * scalingFactor);
    }
    @Override
    public void addChild(DraftNode child) {

    }

    @Override
    public void removeChild(DraftNode child) {

    }

    public double getSirina() {
        return sirina;
    }

    public double getDuzina() {
        return duzina;
    }

    public ArrayList<Object> getNamestaj() {
        return namestaj;
    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
    }
}
