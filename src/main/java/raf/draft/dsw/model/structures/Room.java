package raf.draft.dsw.model.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import raf.draft.dsw.model.nodes.DraftNode;
import raf.draft.dsw.model.nodes.DraftNodeComposite;
import raf.draft.dsw.model.notification.Notification;
import raf.draft.dsw.model.structures.namestaj.RoomElement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Room extends DraftNodeComposite {

    private double sirina;
    private double duzina;

    // Polja za crtanje
    private Color lineColor = Color.BLACK; // Boja linija (crna)
    private float lineThickness = 2.0f; // Debljina linija (2 px)

    // njegova deca
    private ArrayList<RoomElement> namestaj = new ArrayList<>();

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
        if(!namestaj.contains((RoomElement) child)){
            namestaj.add((RoomElement) child);
        }

        // Room je vec IPublisher i samo treba da obavesti RoomView
        // (koji je sada postao ISubscriber) da je dodato dete i da treba da se repaintuje

        notifySubscribers(new Notification());

    }

    @Override
    public void removeChild(DraftNode child) {
        namestaj.remove(child);
    }

    public double getSirina() {
        return sirina;
    }

    public double getDuzina() {
        return duzina;
    }

    public ArrayList<RoomElement> getNamestaj() {
        return namestaj;
    }

    public void setSirina(double sirina) {
        this.sirina = sirina;
    }

    public void setDuzina(double duzina) {
        this.duzina = duzina;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public float getLineThickness() {
        return lineThickness;
    }
}
